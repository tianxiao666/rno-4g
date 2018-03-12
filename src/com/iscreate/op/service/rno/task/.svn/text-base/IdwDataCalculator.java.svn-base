package com.iscreate.op.service.rno.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.iscreate.op.service.rno.tool.RenderTool;
import com.iscreate.op.service.rno.vo.Grid;
import com.iscreate.op.service.rno.vo.PicArea;
import com.iscreate.op.service.rno.vo.PicBS;
import com.iscreate.plat.tools.LatLngHelper;

public class IdwDataCalculator {

	private static Log log = LogFactory.getLog(IdwDataCalculator.class);
	
	/**
	 * 通过idw算法计算渲染图格子的参考值，并将结果保存在文件中
	 * @param extra
	 * @param finPicArea
	 * @param grids
	 * @param sampleGrids
	 * @param finBsList
	 * @param threadNum
	 * @return
	 * @author peng.jm
	 * 2014年7月9日19:07:54
	 */
	public void saveGridsToFile(Map<String, Object> extra, PicArea finPicArea,
			List<List<Grid>> grids, List<Grid> sampleGrids, List<PicBS> finBsList, int threadNum) {
		
		String idwFilePath = extra.get("rootPath") + "/idw_data/idw_data_"
				+ extra.get("taskId") + "_" + extra.get("ncsRendererType")
				+ "_" + threadNum + ".txt";
		log.debug("要保存的idw文件路径:"+idwFilePath);
		
		File file = new File(idwFilePath);
		if(file.isDirectory()) {
			log.error("请输入文件名称！");
			return;
		}
		if(file.getParentFile().exists()) {
			file.delete();
		} else {
			file.getParentFile().mkdirs();
		}

		List<Double> disVs=null;
		List<Boolean> needs=null;
		disVs= new ArrayList<Double>();
		needs= new ArrayList<Boolean>();
		double totV = 0;
		
		//渲染进度结果
		Map<String, Object> renderProgress = null;
		int num = 0;
		
		StringBuffer sb = new StringBuffer();
		//标题
		sb.append("leftX" + "\t" + "upY" + "\t" + "rightX" + "\t" + "bottomY" + "\t" +	
				"sumValue" + "\t" + "maxValue" + "\t" + "minValue" + "\t" + "minLng" + "\t" +	
				"maxLng" + "\t" + "minLat" + "\t" + "maxLat" + "\t" + "meanValueThroughIDW" + "\t" + "\r\n");
		
		for (List<Grid> rows : grids) {
			for (Grid grid : rows) {

				if (grid.getSumValue() > 0) {
					continue;
				}
				disVs.clear();
				needs.clear();
				totV = 0;
				// 求所有的倒数
				for (Grid sg : sampleGrids) {

					double dV = LatLngHelper.Distance(sg.getCenterGisPoint()
							.getLng() / 3600,
							sg.getCenterGisPoint().getLat() / 3600, grid
									.getCenterGisPoint().getLng() / 3600, grid
									.getCenterGisPoint().getLat() / 3600);
					
					if (dV > RenderTool.SEARCH_DISTANCE) {// 大于规定的最大影响距离,不纳入考量
						dV = -1d;
						disVs.add(0d);
						needs.add(false);
					} else {
						dV=Math.pow(dV, RenderTool.POWER);
						totV += dV;// 倒数的和
						disVs.add(dV);
						needs.add(true);
					}

				}

				double fV = 0;
				for (int k = 0; k <sampleGrids.size(); k++) {
					Grid sg = sampleGrids.get(k);
					if (needs.get(k) == true) {// 有效数据
						fV += (double) (disVs.get(k) / totV) * sg.getMeanValue();// 系数乘于样本的值
					}
				}
				grid.setMeanValueThroughIDW(fV);
			
				sb.append(grid.getLeftX() + "\t" + grid.getUpY() + "\t"
						+ grid.getRightX() + "\t" + grid.getBottomY() + "\t" 
						+ grid.getSumValue() + "\t" + grid.getMaxValue() + "\t" 
						+ grid.getMinValue() + "\t" + grid.getMinLng() + "\t" 
						+ grid.getMaxLng() + "\t" + grid.getMinLat() + "\t" 
						+ grid.getMaxLat() + "\t" + grid.getMeanValueThroughIDW() 
						+ "\r\n");
			}
			num++;
			if (num >= ((int) grids.size() / 10)
				&& num % ((int) grids.size() / 10) == 0) {
				log.debug("线程 "+threadNum+" idw插值计算进度：" + (num*100)/grids.size() + "%");
				renderProgress = RenderTool.getRenderProgressFromTaskIdAndType(
						Long.parseLong(extra.get("taskId").toString()),
						extra.get("ncsRendererType").toString());
				renderProgress.put("taskId", extra.get("taskId"));
				renderProgress.put("rendererType", extra.get("ncsRendererType"));
				renderProgress.put("status", "working");
				renderProgress.put("modifTime", new Date().getTime());
				renderProgress.put("spendTime", extra.get("spendTime"));
				renderProgress.put("progressDesc", "图形数据处理中...." );
				renderProgress.put("threadProgress"+threadNum, ((num*100)/grids.size())/2);
				RenderTool.setProgress(renderProgress);
			}
		}

		BufferedWriter writerAppend;
		try{
			writerAppend = new BufferedWriter(new FileWriter(new File(idwFilePath), true));
			writerAppend.append(sb);
	        writerAppend.flush();
	        writerAppend.close(); 
		} catch (IOException e) {
			//删除建立的idw计算文件
			file.delete();
			//清空sb
			sb.delete(0,sb.length());
			//清除渲染进度缓存
			RenderTool.removeProgress(Long.parseLong(extra.get(
					"taskId").toString()), extra.get(
					"ncsRendererType").toString());
			e.printStackTrace(); 
		}
		//清空sb
		sb.delete(0,sb.length());
		log.debug("线程 "+threadNum+" 完成计算！");

	}
}
