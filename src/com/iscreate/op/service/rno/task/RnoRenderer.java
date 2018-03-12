package com.iscreate.op.service.rno.task;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.dao.rno.RnoStructureQueryDao;
import com.iscreate.op.dao.rno.RnoTaskDao;
import com.iscreate.op.pojo.rno.RenderColor;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.service.rno.RnoStructureServiceImpl;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.GisToPicHelper;
import com.iscreate.op.service.rno.tool.GridTool;
import com.iscreate.op.service.rno.tool.RenderTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.vo.ColorRange;
import com.iscreate.op.service.rno.vo.GisArea;
import com.iscreate.op.service.rno.vo.GisPoint;
import com.iscreate.op.service.rno.vo.Grid;
import com.iscreate.op.service.rno.vo.PicArea;
import com.iscreate.op.service.rno.vo.PicBS;
import com.iscreate.op.service.rno.vo.PicPoint;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoRenderer {

	private static Log log = LogFactory.getLog(RnoRenderer.class);
	//注入
	private RnoTaskDao rnoTaskDao;
	private RnoStructureQueryDao rnoStructureQueryDao;
	
	//渲染进度
	private Map<String, Object> renderProgress;
	
	public RnoTaskDao getRnoTaskDao() {
		return rnoTaskDao;
	}

	public void setRnoTaskDao(RnoTaskDao rnoTaskDao) {
		this.rnoTaskDao = rnoTaskDao;
	}

	public RnoStructureQueryDao getRnoStructureQueryDao() {
		return rnoStructureQueryDao;
	}

	public void setRnoStructureQueryDao(RnoStructureQueryDao rnoStructureQueryDao) {
		this.rnoStructureQueryDao = rnoStructureQueryDao;
	}

	/**
	 * 通过传入渲染类型获取对应的ro文件数据，生成渲染图并存储
	 * @param ncsRendererType
	 * @param taskId
	 * @param filePath
	 * @author peng.jm
	 * 2014年7月9日15:40:04
	 */
	private void createRenderImg(String ncsRendererType, long taskId, String filePath) {
		
		long startTime = System.currentTimeMillis();
		
		/** 获取报告生成的.ro文件，并解析出对应的数据 **/
		
		//获取ro文件的cellres数据
		List<Map<String, Object>> cellres = getRoFileDataByTaskId(taskId, filePath, "cellres", ncsRendererType);
		if(cellres.size() <= 0) {
			log.error("读取" + taskId + "的ro文件出错！");
			return;
		}
		log.info("ro文件数据读取完成,cellres.size()=" + cellres.size());
			
		
		/** 利用解析出来的数据转成画图需要的数据 **/
		
		//通过taskId获取区域id
		List<Map<String,Object>> res = rnoStructureQueryDao.getStructureTaskByJobId(taskId);
		if(res.size() <= 0) {
			log.error("找不到对应jobId="+taskId+"的任务！");
			return;
		}
		String cityId = "0";
		if(res.get(0).get("CITY_ID") != null) {
			cityId = res.get(0).get("CITY_ID").toString();
		} 
//		RnoTask task = rnoTaskDao.getTaskById(taskId);
//		String areaId = task.getLevelId();
		//areaId = "103";
		
		
		
		//通过区域id获取区域经纬度范围
		Map<String, Object> areaRange = rnoStructureQueryDao.getAreaRangeByAreaId(cityId);
		if(areaRange.size() <= 0) {
			log.error("cityId="+cityId+",获取所属区域的经纬度范围失败！");
			return;
		}
		Double areaMaxLng = Double.parseDouble(areaRange.get("MAX_LNG").toString());
		Double areaMinLng = Double.parseDouble(areaRange.get("MIN_LNG").toString());
		Double areaMaxLat = Double.parseDouble(areaRange.get("MAX_LAT").toString());
		Double areaMinLat = Double.parseDouble(areaRange.get("MIN_LAT").toString());
		log.info("所属区域的id=" + cityId + ",maxlng=" + areaMaxLng + ",minlng="
				+ areaMinLng + ",maxlat=" + areaMaxLat + ",minlat=" + areaMinLat);
		
		//找出最大经纬度
		Double lng, lat;
		double maxLng = -1, maxLat = -1, minLng = Double.MAX_VALUE, minLat = Double.MAX_VALUE;
		Map<String, Object> oneCellres = null;
		
		List<Double> sampleVals = new ArrayList<Double>(); //指标值
		List<GisPoint> gpoints = new ArrayList<GisPoint>(); //指标值对应坐标
		List<Double> preSampleVals = new ArrayList<Double>(); //超出额定区域范围的指标值
		List<GisPoint> preGpoints = new ArrayList<GisPoint>(); //超出额定区域范围的对应坐标
		double val;
		int n1=0;
		int n2=0;
		int n3=0;
		int n4=0;
		
		for (int i = 0; i < cellres.size(); i++) {
			oneCellres = cellres.get(i);
			
			if (("").equals(oneCellres.get("lng"))
					|| oneCellres.get("lng") == null) {
				n1++;
				continue;
			}
			if (("").equals(oneCellres.get("lat"))
					|| oneCellres.get("lat") == null) {
				n1++;
				continue;
			}
			if (("").equals(oneCellres.get(ncsRendererType).toString())
					|| oneCellres.get(ncsRendererType) == null) {
				n2++;
				continue;
			}
			
			lng = Double.parseDouble(oneCellres.get("lng").toString());
			lat = Double.parseDouble(oneCellres.get("lat").toString());
			val = Double.parseDouble(oneCellres.get(ncsRendererType).toString());
			
			//判断对应指标值的坐标是否在区域内
			if (lng < areaMinLng 
					|| lng > areaMaxLng 
					|| lat < areaMinLat
					|| lat > areaMaxLat) {
				log.warn("lng=" + lng + ",lat=" + lat + ",该指标值的坐标不属于所在区域内。");
				n3++;
				preSampleVals.add(val);
				preGpoints.add(new GisPoint(lng, lat));
				continue;
			}
			
			//判断对应指标值的坐标是否为0
			if(lng == 0 || lat == 0) {
				log.warn("lng=" + lng + ",lat=" + lat + ",该指标值的坐标为零。");
				n4++;
				continue;
			}
			
			lng = lng * 3600;// 需要以秒为单位
			lat = lat * 3600;
			if (maxLng < (lng - 0.0001)) {
				maxLng = lng;
			}
			if (minLng > (lng - 0.0001)) {
				minLng = lng;
			}
			if (maxLat < (lat - 0.0001)) {
				maxLat = lat;
			}
			if (minLat > (lat - 0.0001)) {
				minLat = lat;
			}

			gpoints.add(new GisPoint(lng, lat));
			sampleVals.add(val);	
		}

		if(n1 > 0) {
			log.info("忽略了" + n1 + "行数据，原因：坐标为空。");
		}
		if(n2 > 0) {
			log.info("忽略了" + n2 + "行数据，原因：对应指标值为空。");
		}
		if(n3 > 0) {
			log.info("获取到" + n3 + "行待参考数据，原因：坐标不在所属区域额定范围内，但数据可以作为参考。");
		}
		if(n4 > 0) {
			log.info("忽略了" + n4 + "行数据，原因：坐标值为0。");
		}
		
		log.info("在有效数据中，maxLat=" + maxLat + ",maxLng=" + maxLng + ",minLat=" + minLat
				+ ",minLng=" + minLng);
		
		GisPoint preGisPoint = null;
		double preVal;
		double maxLatTemp = maxLat;
		double minLatTemp = minLat;
		double maxLngTemp = maxLng;
		double minLngTemp = minLng;
		
		//循环检验待参考点加入参考后是否满足网格数量要求
		//满足则加入，反之则不加入
		double r, areaWidth;
		int width,heigth,gridPix;
		
		for (int i = 0; i < preGpoints.size(); i++) {
			
			preGisPoint = preGpoints.get(i);
			preVal = preSampleVals.get(i);
			
			lng = preGisPoint.getLng();
			lat = preGisPoint.getLat();
			if (maxLngTemp < (lng - 0.0001)) {
				maxLngTemp = lng;
			}
			if (minLngTemp > (lng - 0.0001)) {
				minLngTemp = lng;
			}
			if (maxLatTemp < (lat - 0.0001)) {
				maxLatTemp = lat;
			}
			if (minLatTemp > (lat - 0.0001)) {
				minLatTemp = lat;
			}
			//比例
			r = Math.abs(maxLngTemp-minLngTemp)/Math.abs(maxLatTemp-minLatTemp);
			width = RenderTool.PIC_WIDTH;
			heigth = (int) (width / r);
			
			areaWidth = LatLngHelper.Distance(minLngTemp/3600,maxLatTemp/3600,maxLngTemp/3600,maxLatTemp/3600);	
			gridPix = (int)Math.round(width/(areaWidth/RenderTool.GRID_UNIT));
			if(gridPix == 0) {
				continue;
			}
			
			int num = ((width/gridPix)*(heigth/gridPix));
			if(num > RenderTool.GRID_LIMIT_NUM) {
				continue;
			} else {
				gpoints.add(preGisPoint);
				sampleVals.add(preVal);
			}
		}
		log.info("sampleVals=" + sampleVals);
		//判断指标值是否全部为空
		if(sampleVals.size() == 0) {
			log.error("对应指标值全部为空，不再执行渲染图生成");
			//保存渲染进度
			renderProgress = new HashMap<String, Object>();
			renderProgress.put("taskId", taskId);
			renderProgress.put("rendererType", ncsRendererType);
			renderProgress.put("status", "working");
			renderProgress.put("modifTime", new Date().getTime());
			renderProgress.put("progressDesc", "生成渲染图失败，原因：数据获取出错！" );
			RenderTool.setProgress(renderProgress);
			return;
		}
		
		//构建gisArea
		GisArea gisArea = new GisArea();
		gisArea.setMaxLat(maxLat);
		gisArea.setMaxLng(maxLng);
		gisArea.setMinLat(minLat);
		gisArea.setMinLng(minLng);
		//构建picArea
		int picWidth = RenderTool.PIC_WIDTH;  //默认渲染图的宽度
		double picHeightD = picWidth / gisArea.getLngLatRatio();
		int picHeight = (int) picHeightD;
		PicArea picArea = new PicArea();
		picArea.setHeight(picHeight);
		picArea.setWidth(picWidth);
		log.info("渲染图大小设置：width=" + picArea.getWidth() + ", height=" + picArea.getHeight());
		
		//对应gis区域的宽度
		double gisAreaWidth = LatLngHelper.Distance(minLng/3600,maxLat/3600,maxLng/3600,maxLat/3600);	
		
		int gridPixel = 1;
		int gridsNum = 0;
		int dou = 1; //扩大倍数
		
		//限制网格个数
		while(true){
			
			//渲染图格子的像素大小
			gridPixel = (int)Math.round(picArea.getWidth()/(gisAreaWidth/(RenderTool.GRID_UNIT*dou)));
			if(gridPixel == 0) {
				dou++;
				continue;
			}
			
			gridsNum = ((picArea.getWidth()/gridPixel)*(picArea.getHeight()/gridPixel));
			
			if(gridsNum > RenderTool.GRID_LIMIT_NUM) {
				log.debug("gridsNum="+gridsNum+",生成网格数量大于额定数值，重新分配..");
				dou++;
			} else {
				log.debug("gridsNum="+gridsNum+",生成网格数量符合额定数值");
				break;
			}
		}
		
		//构建渲染图网格
		log.info("建立网格数据....开始");
		
		log.debug("gridPixel=" + gridPixel + ", 以" + (RenderTool.GRID_UNIT*dou) + "m为一个网格划分");
		
		List<List<Grid>> grids = GridTool.splitGridByStep(picArea.getWidth(),
				picArea.getHeight(), (int)gridPixel, (int)gridPixel);

		GisToPicHelper gtp = new GisToPicHelper();
		gtp.build(gisArea, picArea);
		// 对于每个grid，都计算出其经纬度情况
		GisPoint gp = null;
		for (List<Grid> gds : grids) {
			for (Grid gd : gds) {
				gp = gtp.picPoint2GisPoint(new PicPoint(gd.getLeftX(), gd
						.getUpY()));
				gd.setMinLng(gp.getLng());
				gd.setMaxLat(gp.getLat());
				gp = gtp.picPoint2GisPoint(new PicPoint(gd.getRightX(), gd
						.getBottomY()));
				gd.setMaxLng(gp.getLng());
				gd.setMinLat(gp.getLat());
			}
		}
		log.info("建立网格数据....完成");
		
		// 转换为像素坐标，并建立样本数据
		log.info("建立样本网格数据....开始");
		List<Grid> sampleGrids = new ArrayList<Grid>();	
		List<PicPoint> pps = new ArrayList<PicPoint>();
		
		PicPoint pp = null;
		boolean find = false;
		Grid cacheGrid = null;
		for (int i = 0; i < gpoints.size(); i++) {
			gp = gpoints.get(i);
			pp = gtp.gisPoint2PicPoint(gp);
			pps.add(pp);

			// 判断落在哪个grid内
			// 首先判断cache
			if (cacheGrid != null && cacheGrid.contains(pp)) {
				cacheGrid.addValue(sampleVals.get(i));
				// System.out.println("find in cache");
				continue;
			}

			// 再判断样本
			find = false;
			// 先从已有的里面找
			for (Grid grid : sampleGrids) {
				if (grid.contains(pp)) {
					grid.addValue(sampleVals.get(i));
					cacheGrid = grid;
					// System.out.println("find in sample");
					break;
				}
			}
			if (find) {
				continue;
			}

			// 最后从头找到尾找
			for (List<Grid> gds : grids) {
				if (pp.getY() >= gds.get(0).getUpY()
						&& pp.getY() < gds.get(0).getBottomY()) {
					// 找到一行
					for (Grid g : gds) {
						if (g.contains(pp)) {
							cacheGrid = g;
							//System.out.println(g);
							g.addValue(sampleVals.get(i));
							sampleGrids.add(g);
							//g.addValue(values.get(i));
							//System.out.println(g);
							// System.out.println("find in big arrry");
							break;
						}
					}
					break;
				}
			}
		}
		log.info("建立样本网格数据完成，共" + sampleGrids.size()+ "个样本点");
	
		//建立基站数据
		List<PicBS> bsList = new ArrayList<PicBS>();
		PicBS pBS = null;
		for (PicPoint picPoint : pps) {
			pBS = new PicBS();
			pBS.setStartX(picPoint.getX());
			pBS.setStartY(picPoint.getY());
			bsList.add(pBS);
		}
		// 到此为止，网格已经划分好，画图所需的数据已经建立好
		log.info("画图所需的网格数据已经建立好： grids=" + grids.size() * grids.get(0).size()
				+ ",sampleGrids.size=" + sampleGrids.size());
		
		int spendTime = Math.round(grids.size() * grids.get(0).size()/45000);
		
		//保存渲染进度
		renderProgress = new HashMap<String, Object>();
		renderProgress.put("taskId", taskId);
		renderProgress.put("rendererType", ncsRendererType);
		renderProgress.put("status", "working");
		renderProgress.put("modifTime", new Date().getTime());
		renderProgress.put("spendTime", spendTime);
		renderProgress.put("progressDesc", "数据处理中...." );
		renderProgress.put("threadProgress1", 0);//初始化线程进度
		renderProgress.put("threadProgress2", 0);
		RenderTool.setProgress(renderProgress);
	
		/** 建立多个线程，将画图需要的数据分配给各自的线程计算，计算结果保存到文件 **/
		
		final PicArea finPicArea = picArea;
		final List<Grid> finSampleGrids = new ArrayList<Grid>();
		finSampleGrids.addAll(sampleGrids);
		final List<PicBS> finBsList = new ArrayList<PicBS>();
		finBsList.addAll(bsList);
		
		//分配数据给多个线程进行计算
		int threadNum = 2; //线程数量
		final CountDownLatch runningThreadNum = new CountDownLatch(threadNum);
		log.info("建立了" + threadNum + "个线程进行idw插值运算");
		final List<List<Grid>> grids1 = new ArrayList<List<Grid>>();
		final List<List<Grid>> grids2 = new ArrayList<List<Grid>>();
		for (int i = 0; i < grids.size(); i++) {
			if(i < grids.size()/2) {
				grids1.add(grids.get(i));
			} else {
				grids2.add(grids.get(i));
			}
		}
		
		//filePath是路径+文件名,只获取路径rootPath
		String rootPath = "";
		File f1=new File(filePath);
		if(f1.isFile()){
			rootPath=f1.getParentFile().getAbsolutePath();
		}else{
			rootPath=filePath;
		}
		
		final Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("taskId", taskId); 
		extra.put("ncsRendererType", ncsRendererType);
		extra.put("rootPath", rootPath);
		extra.put("spendTime", spendTime);
		log.info("机器对数据进行idw插值运算中.......");
		new Thread(new Runnable(){
			@Override
			public void run() {
				IdwDataCalculator idwDataCalculator = new IdwDataCalculator();
				idwDataCalculator.saveGridsToFile(extra, finPicArea, grids1,
						finSampleGrids, finBsList, 1);	
				runningThreadNum.countDown();//正在运行的线程数减一
			}
			
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				IdwDataCalculator idwDataCalculator = new IdwDataCalculator();
				idwDataCalculator.saveGridsToFile(extra, finPicArea, grids2,
						finSampleGrids, finBsList, 2);	
				runningThreadNum.countDown();//正在运行的线程数减一
			}
			
		}).start();
		
		//等待子线程都执行完了再执行主线程剩下的动作
        try {
			runningThreadNum.await();
			log.info("idw插值运算结束，计算结果保存在文件中");
		} catch (InterruptedException e) {
			e.printStackTrace();
			//清除渲染进度缓存
			RenderTool.removeProgress(taskId, ncsRendererType);
		}
		/*boolean flag = true;
		while (flag) {
			if(RenderTool.getRenderThreadNum() == 0) {
				log.info("idw插值运算结束，计算结果保存在文件中");
				flag = false;
			}
		}*/
		

		/** 获取线程计算结果的文件数据，结合画图需要的数据，画成渲染图 **/
		
		//渲染图的保存路径
		String folderString = rootPath + "/image/";
		File folder=new File(folderString);
		if (!folder.exists()&&!folder.isDirectory()) {
			log.info("不存在此文件夹需创建folder:"+folderString);
			folder.mkdir();  
		}
		//保存渲染进度
		renderProgress = new HashMap<String, Object>();
		renderProgress.put("taskId", taskId);
		renderProgress.put("rendererType", ncsRendererType);
		renderProgress.put("status", "working");
		renderProgress.put("modifTime", new Date().getTime());
		renderProgress.put("spendTime", spendTime);
		renderProgress.put("progressDesc", "数据计算完成，图形渲染中...." );
		RenderTool.setProgress(renderProgress);
		
		//开始画图
		log.info("渲染图片处理中.....");
		
		//通过指标值获取渲染颜色配置
		List<Map<String, Object>> colorConfigList = getColorConfigList(sampleVals);
		if(colorConfigList.size() <= 0) {
			log.error(ncsRendererType + ":渲染规则不存在！");
			return ;
		}
		log.info("开始画图，渲染颜色配置：" + colorConfigList);
		drawPicByGridsData(rootPath, taskId, picArea, sampleGrids, bsList,
				ncsRendererType, colorConfigList, threadNum);
		log.info("画图结束！");
		
		log.info("开始生成渲染图对应的地理位置数据文件");
		String swPoint[] = CoordinateHelper.changeFromGpsToBaidu((minLng/3600)+"", (minLat/3600)+"");
		String nePoint[] = CoordinateHelper.changeFromGpsToBaidu((maxLng/3600)+"", (maxLat/3600)+"");
		if(swPoint.length > 0 && nePoint.length > 0) {
			String dataFile = rootPath + "/image/" + taskId + "_" + ncsRendererType + ".data";
			
			StringBuffer sb = new StringBuffer();
			sb.append("minLng" + "##" + "minLat" + "##" + "maxLng" + "##" + "maxLat"+ "\r\n");
			sb.append(swPoint[0] + "##" + swPoint[1] + "##" + nePoint[0] + "##" + nePoint[1]+ "\r\n");
			
			BufferedWriter writerAppend = null;
			try{
				writerAppend = new BufferedWriter(new FileWriter(new File(dataFile), true));
				writerAppend.append(sb);
		        writerAppend.flush();
		        writerAppend.close(); 
			} catch (IOException e) {
				e.printStackTrace(); 
				//清空sb
				sb.delete(0,sb.length());
				try {
					writerAppend.flush();
					writerAppend.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			//清空sb
			sb.delete(0,sb.length());
		}
		log.info("生成渲染图对应的地理位置数据文件完成！");
		
		long endTime = System.currentTimeMillis();
		log.info("渲染图片处理结束");
		log.info("生成渲染图过程总共耗时： " + (endTime - startTime) + "ms");
		
		//保存渲染进度
		renderProgress = new HashMap<String, Object>();
		renderProgress.put("taskId", taskId);
		renderProgress.put("rendererType", ncsRendererType);
		renderProgress.put("status", "finish");
		renderProgress.put("modifTime", new Date().getTime());
		renderProgress.put("spendTime", spendTime);
		renderProgress.put("progressDesc", "渲染完成" );
		RenderTool.setProgress(renderProgress);
		
		//删除idw算法产生的临时数据文件
		RenderTool.deleteIdwFile(rootPath, taskId ,ncsRendererType);
	}

	/**
	 * 
	 * @param filePath
	 * @param taskId
	 * @param picArea
	 * @param sampleGrids
	 * @param bsList
	 * @param colorConfigList
	 * @param fileNum
	 * @param ncsRendererType
	 * @author peng.jm
	 * 2014年7月9日19:40:06
	 */
	private void drawPicByGridsData(String filePath, long taskId,
			PicArea picArea, List<Grid> sampleGrids, List<PicBS> bsList, 
			String ncsRendererType,List<Map<String, Object>> colorConfigList, int fileNum) {
		
		// 创建BufferedImage对象
		BufferedImage image = new BufferedImage(picArea.getWidth(),
				picArea.getHeight(), BufferedImage.TYPE_INT_RGB);
		// 获取Graphics2D
		Graphics2D g2d = image.createGraphics();
		// ---------- 增加下面的代码使得背景透明 -----------------

		image = g2d.getDeviceConfiguration().createCompatibleImage(picArea.getWidth(),
				picArea.getHeight(), Transparency.TRANSLUCENT);

		g2d.dispose();

		g2d = image.createGraphics(); 
		// ---------- 背景透明代码结束 -----------------
		
		//获取对应的渲染规则
		/*List<Map<String, Object>> colorConfigList = new ArrayList<Map<String,Object>>();
		colorConfigList = rnoStructureAnalysisDao.getRnoTrafficRendererConfig(ncsRendererType);
		//System.out.println("ncsRendererType渲染规则:"+colorConfigList);
		if(colorConfigList.size() <= 0) {
			log.error(ncsRendererType + ":渲染规则不存在！");
			return ;
		}*/
		
		//重新计算渲染规则
		List<ColorRange> colorMap = new ArrayList<ColorRange>();
		colorMap = recaluteColor(colorConfigList);
	
		//画样本点
		log.info("drawing sample grids....");
		for (int i = 0; i < sampleGrids.size(); i++) {
			drawSampleGrid(g2d, sampleGrids.get(i), colorMap);
		}
		log.info("共 " + sampleGrids.size() + " 个样本点");
		
		//读取文件汇总数据,画非样本点
		log.info("drawing grids....");
		List<Grid> totGrids = new ArrayList<Grid>();
		List<Grid> grids = null;
		for (int i = 1; i <= fileNum; i++) {
			grids = new ArrayList<Grid>();
			grids = getNonSampleValueFromFile(i, taskId, ncsRendererType, filePath);
			totGrids.addAll(grids);
		}
		log.info("共 " + totGrids.size() + " 个非样本点");
		if(totGrids.size() <= 0) {
			log.info("画图所需样本数据文件为空或者不存在！");
			return;
		}
		for (int i = 0; i < totGrids.size(); i++) {
			drawNonSampleGrid(g2d, totGrids.get(i), colorMap);
		}
		
		//画基站
		/*log.info("drawing BaseStation ....");
		drawBaseStation(g2d, bsList, RenderTool.BaseStationColor);*/
		
		String picPath = filePath + "/image/"+taskId+"_"+ncsRendererType+".png";
		log.info("picPath:"+picPath);
		
		//获取图片文件
		File picFile = new File(picPath);
		if (picFile.isDirectory()) {
			log.error("请传入文件名称，不要传入路径！");
		}
		if (!picFile.getParentFile().exists()) {
			picFile.getParentFile().mkdirs();
		}
		//clear
		g2d.dispose(); 
		
		// 保存文件
		try {
			ImageIO.write(image, "png", picFile);
		} catch (IOException e) {
			e.printStackTrace();
			//清除渲染进度缓存
			RenderTool.removeProgress(taskId, ncsRendererType);
		}
	}

	/**
	 * 画基站
	 * @param g2d
	 * @param bsList
	 * @param baseStationColor
	 * @author peng.jm
	 */
	private void drawBaseStation(Graphics2D g2d, List<PicBS> bsList,
			String baseStationColor) {
		int color = Integer.parseInt(baseStationColor.substring(1), 16);
		for (PicBS pBs : bsList) {
			g2d.setColor(new Color(color));
			g2d.drawLine(pBs.getStartX(), pBs.getStartY(), pBs.getEndX(), pBs.getEndY());
		}
	}

	/**
	 * 画非样本点
	 * @param g2d
	 * @param grid
	 * @param colorMap
	 * @author peng.jm
	 */
	private void drawNonSampleGrid(Graphics2D g2d, Grid grid,
			List<ColorRange> colorMap) {
		Color color = getColorByValue(grid.getMeanValueThroughIDW(), colorMap);
		g2d.setColor(color);
		g2d.fillRect((int) grid.getLeftX(), (int) grid.getUpY(),
				(int) grid.getWidth(), (int) grid.getHeight());
	}

	/**
	 * 画样本点
	 * @param g2d
	 * @param grid
	 * @param colorMap
	 * @author peng.jm
	 */
	private void drawSampleGrid(Graphics2D g2d, Grid grid,
			List<ColorRange> colorMap) {
		Color color = getColorByValue(grid.getMeanValue(), colorMap);
		g2d.setColor(color);
		g2d.fillRect((int) grid.getLeftX(), (int) grid.getUpY(),
				(int) grid.getWidth(), (int) grid.getHeight());
	}

	/**
	 * 
	 * @param meanValue
	 * @param colorMap
	 * @return
	 * @author peng.jm
	 */
	private Color getColorByValue(double meanValue, List<ColorRange> colorMap) {
		Color colorObj = null;
		double min=0,max=0;
		String color = "";
		int r,g,b;
		for (int n = 0; n < colorMap.size(); n++) {
			color = colorMap.get(n).getColor();
			r = Integer.parseInt(color.substring(0,2), 16);
			g = Integer.parseInt(color.substring(2,4), 16);
			b = Integer.parseInt(color.substring(4,6), 16);
			min = colorMap.get(n).getMinValue();
			max = colorMap.get(n).getMaxValue();
			if (meanValue >= min && meanValue < max) {
				colorObj = new Color(r,g,b,100);
				break;
			}
		}
		return colorObj;
	}

	
	
	/**
	 * 重新分配颜色，为了颜色过渡自然
	 * @param colorConfigList
	 * @return
	 * @author peng.jm
	 */
	private List<ColorRange> recaluteColor(
			List<Map<String, Object>> colorConfigList) {
		//获取颜色对应参考值
	/*	List<Double> colorVals = new ArrayList<Double>();
		for (int i = 0; i < colorConfigList.size(); i++) {
			double value = Double.parseDouble(colorConfigList.get(i).get("MIN_VALUE").toString());
			colorVals.add(value);
		}
		colorVals.add(100000d); //增加最大值
		log.debug("colorVals=" + colorVals);
		//获取颜色代码
		List<String> colorStyles = new ArrayList<String>();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		RenderColor renderColor = null;
		int num = 0;
		for (int i = 0; i < colorConfigList.size(); i++) {
			String colorStr = colorConfigList.get(i).get("STYLE").toString();
			renderColor = gson.fromJson(colorStr,RenderColor.class);
			colorStyles.add(renderColor.getColor());
			num = i;
		}
		String colorStr = colorConfigList.get(num).get("STYLE").toString();
		renderColor = gson.fromJson(colorStr,RenderColor.class);
		colorStyles.add(renderColor.getColor());
		log.debug("colorStyles=" + colorStyles);
		*/
		
		//获取过渡颜色
		List<ColorRange> colorMap = new ArrayList<ColorRange>();
		List<ColorRange> tmp = null;
		for (int i = 0; i < colorConfigList.size(); i++) {
			tmp = RenderTool.getGradientColor(
					colorConfigList.get(i).get("startColor").toString(), 
					colorConfigList.get(i).get("endColor").toString(),
					Double.parseDouble(colorConfigList.get(i).get("minVal").toString()),
					Double.parseDouble(colorConfigList.get(i).get("maxVal").toString()),
					RenderTool.SPLIT_BLOCK_CNT);
			colorMap.addAll(tmp);
		}
		return colorMap;
	}

	/**
	 * 通过读取文件获取idw的计算结果
	 * @param fileNum
	 * @param taskId
	 * @param ncsRendererType
	 * @param filePath
	 * @return
	 * @author peng.jm
	 */
	private List<Grid> getNonSampleValueFromFile(int fileNum, long taskId, String ncsRendererType, String filePath) {
		
		List<Grid> result = new ArrayList<Grid>();
		String realFilePath = filePath + "/idw_data/idw_data_" + taskId + "_" + ncsRendererType + "_" + fileNum + ".txt";

		//System.out.println(realFilePath);
		File txtFile = new File(realFilePath);		
		if (txtFile.isDirectory()) {
			log.error("请传入文件名称，不要传入路径！");
		}
		if (!txtFile.getParentFile().exists()) {
			txtFile.getParentFile().mkdirs();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(realFilePath)));
		} catch (FileNotFoundException e1) {
			log.error("需要解析的txt文件不存在！");
			//清除渲染进度缓存
			RenderTool.removeProgress(taskId, ncsRendererType);
			return result;
		}
		try {
			br.readLine(); //忽略第一行
			String line;
			Grid grid = null;
			//System.out.println(br.readLine());
			while((line = br.readLine()) != null) {
				String[] vs = line.split("	");
				if(vs.length < 12) {
					System.out.println(line);
					continue;
				}
				grid = new Grid();
				grid.initByArray(vs);
				result.add(grid);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			//清除渲染进度缓存
			RenderTool.removeProgress(taskId, ncsRendererType);
		}
		return result;
	}
	
	/**
	 * 通过指标值获取渲染颜色配置
	 * @param sampleVals
	 * @return
	 */
	public List<Map<String, Object>> getColorConfigList(List<Double> sampleVals) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(sampleVals.size() == 0) {
			return result;
		}
		List<Double> anotherList = new ArrayList<Double>();
		anotherList.addAll(sampleVals);

		//升序排序
		Collections.sort(anotherList);
		int size = anotherList.size();
		//获取蓝色对应的值
		double blueValue = RenderTool.renderRule.get("Blue");
		//获取浅蓝色对应的值
		double wblueValue = anotherList.get((int) (size * RenderTool.renderRule.get("WBlue")));
		//获取绿色对应的值
		double greenValue = anotherList.get((int) (size * RenderTool.renderRule.get("Green")));
		//获取黄色对应的值
		double yellowValue = anotherList.get((int) (size * RenderTool.renderRule.get("Yellow")));
		//获取红色对应的值
		double redValue = anotherList.get((int) (size *  RenderTool.renderRule.get("Red")));
		
		//最后一个值设置为最大值1000000d
		List<Double> valList = Arrays.asList(blueValue, wblueValue, 
				greenValue, yellowValue, redValue, 1000000d);	
		List<String> colorList = Arrays.asList("#0000FF", "#00FFFF", 
				"#00FF00", "#FFF300", "#FF0000", "#FF0000");
		Map<String, Object> map = null;
		for (int i = 0; i < valList.size() - 1; i++) {
			map = new HashMap<String, Object>();
			map.put("minVal", valList.get(i));
			map.put("maxVal", valList.get(i + 1));
			map.put("startColor", colorList.get(i));
			map.put("endColor", colorList.get(i + 1));
			result.add(map);
		}
		return result;
	}

	
//	/**
//	 * 获取ro文件的对应数据
//	 * @param taskId
//	 * @param filePath
//	 * @param resCode 需要获取数据对应的key，如“cellres”
//	 * @return
//	 */
//	public List<Map<String, Object>> getRoFileDataByTaskId(long taskId,
//			String filePath, String resCode) {
//		
//		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
//		if(taskId == 0) {
//			log.error("taskId为空值！");
//			return result;
//		}
//		RnoTask task = rnoTaskDao.getTaskById(taskId);
//		java.sql.Timestamp createTime = task.getStartTime();
//		Calendar calendar = new GregorianCalendar();
//		Date dt=new Date(createTime.getTime());
//		calendar.setTime(dt);
//		//System.out.println("年:" + calendar.get(Calendar.YEAR) + ",月:" + (calendar.get(Calendar.MONTH) + 1));
//		
//		String roFilePath = filePath + "/" + calendar.get(Calendar.YEAR) + "/"
//					+ (calendar.get(Calendar.MONTH) + 1) + "/";
//		String roFileName = "ncs_res_" + taskId + ".xls"
//				+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX;
//		String roFileFullName = roFilePath + roFileName;
//		log.info("ro文件路径："+ roFileFullName);
//		
//		//判断ro文件是否存在
//		File roFile = new File(roFileFullName);
//		if(!roFile.exists()) {
//			log.error(taskId + "对应的ro文件不存在");
//			return result;
//		}
//		
//		//获取ro文件的resCode数据
//		result = FileTool.readDataFromTxt(roFileFullName, resCode);
//		
//		return result;
//	}

	/**
	 * 检查是否存在对应的渲染图，不存在则生成该参数的渲染图
	 * @param ncsRendererType
	 * @param taskId
	 * @param filePath
	 * @return
	 */
	public Map<String, Object> getRenderImg(final String ncsRendererType,
			final long taskId, final String filePath) {
		
		Map<String, Object> res = new HashMap<String, Object>();
		if (taskId == 0 || ncsRendererType == null
				|| ("").equals(ncsRendererType)) {
			res.put("flag", "error");
			return res;
		}
		
		String imgDir="";
		File f1=new File(filePath);
		if(f1.isFile()){
			imgDir=f1.getParentFile().getAbsolutePath();
		}else{
			imgDir=filePath;
		}

		String imgPath = imgDir + "/image/" + taskId + "_"
				+ ncsRendererType + ".png";

		log.info("渲染图的路径: " + imgPath);

		// 判断渲染图是否已存在
		File file = new File(imgPath);
		if (file.exists()) {
			log.info("渲染图已存在，直接返回结果");
			res.put("flag", "imgExisted");
			return res;
		}
		//判断渲染图是否正在生成
		Map<String, Object> resProgress = RenderTool.getRenderProgressFromTaskIdAndType(
				taskId, ncsRendererType);
		if(resProgress.size() > 0 ) {
			log.info("渲染图生成任务已存在，返回渲染进度");
			res.put("flag", "imgTaskExisted");
			return res;
		}
		
		// 生成渲染图
		log.info("对应的渲染图没找到，现在生成");
		
		//保存渲染进度
		renderProgress = new HashMap<String, Object>();
		renderProgress.put("taskId", taskId);
		renderProgress.put("rendererType", ncsRendererType);
		renderProgress.put("status", "working");
		renderProgress.put("modifTime", new Date().getTime());
		renderProgress.put("spendTime", "");
		renderProgress.put("progressDesc", "对应的渲染图没找到，现在生成中..." );
		RenderTool.setProgress(renderProgress);
		log.debug("进度结果："+RenderTool.getRenderProgressFromTaskIdAndType(taskId, ncsRendererType));
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				
				createRenderImg(ncsRendererType, taskId, filePath);
				//清除渲染进度缓存
				RenderTool.removeProgress(taskId, ncsRendererType);
			}
			
		}).start();
		//System.out.println(RenderTool.getRenderProgressFromTaskId(taskId, ncsRendererType));
		//createRenderImg(ncsRendererType, taskId, filePath);
		
		//清除渲染进度缓存
		//RenderTool.removeProgress(taskId, ncsRendererType);
		
		res.put("flag", "imgNotExisted");
		return res;
	}
	
	/**
	 * 获取ro文件的对应数据
	 * @param taskId
	 * @param filePath 全路径（包括文件名）
	 * @param resCode 需要获取数据对应的key，如“cellres”
	 * @return
	 */
	public List<Map<String, Object>> getRoFileDataByTaskId(long taskId,
			String filePath, String resCode,String idxName) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if(taskId == 0) {
			log.error("taskId为空值！");
			return result;
		}
//		RnoTask task = rnoTaskDao.getTaskById(taskId);
//		java.sql.Timestamp createTime = task.getStartTime();
//		Calendar calendar = new GregorianCalendar();
//		Date dt=new Date(createTime.getTime());
//		calendar.setTime(dt);
		//System.out.println("年:" + calendar.get(Calendar.YEAR) + ",月:" + (calendar.get(Calendar.MONTH) + 1));
		
//		String roFilePath = filePath + "/" + calendar.get(Calendar.YEAR) + "/"
//					+ (calendar.get(Calendar.MONTH) + 1) + "/";
//		String roFileName = "ncs_res_" + taskId + ".xls"
//				+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX;
//		String roFileFullName = roFilePath + roFileName;
		log.info("ro文件路径："+ filePath);
		
		//判断ro文件是否存在
		File roFile = new File(filePath);
		if(!roFile.exists()) {
			log.error(taskId + "对应的ro文件不存在");
			return result;
		}
		
		//获取ro文件的resCode数据
//			result = FileTool.readDataFromTxt(roFileFullName, resCode);
		List<String> needFields=new ArrayList<String>();
		needFields.add("cell");
		needFields.add("lng");
		needFields.add("lat");
		needFields.add(idxName);
		
		result = FileTool.readPartlyFieldsDataFromTxt(filePath, resCode,needFields);
		return result;
	}


}
