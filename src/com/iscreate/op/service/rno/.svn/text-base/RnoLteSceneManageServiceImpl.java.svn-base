package com.iscreate.op.service.rno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.Line;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.dao.rno.RnoLteCellDao;
import com.iscreate.op.dao.rno.RnoLteSceneManageDao;

public class RnoLteSceneManageServiceImpl implements RnoLteSceneManageService {
	private static Log log = LogFactory
			.getLog(RnoLteSceneManageServiceImpl.class);

	private RnoLteSceneManageDao rnoLteSceneManageDao;

	public void setRnoLteSceneManageDao(RnoLteSceneManageDao rnoLteSceneManageDao) {
		this.rnoLteSceneManageDao = rnoLteSceneManageDao;
	}

	/**
	 * 
	 * @title 查询lte网格数据数量通过城市ID
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:07:41
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryLteGridDataCntByCityId(long cityId) {
		return rnoLteSceneManageDao.queryLteGridDataCntByCityId(cityId);
	}
	/**
	 * 
	 * @title 分页查询符合条件的LTE网格数据记录
	 * @param cityId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:08:06
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByPage(long cityId,Page page) {
		return rnoLteSceneManageDao.queryLteGridDataByPage(cityId, page);
	}
	/**
	 * 
	 * @title 通过城市ID获取符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6上午9:49:00
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByCityId(final 
			long cityId){
		return rnoLteSceneManageDao.queryLteGridDataByCityId(cityId);
	}
	/**
	 * 
	 * @title 通过城市ID导出符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author li.tf
	 * @date 2015-9-14上午16:37:01
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> exportLteGridDataByCityId(final 
			long cityId){
		return rnoLteSceneManageDao.exportLteGridDataByCityId(cityId);
	}
	/**
	 * 通过场景ID查询地理场景信息
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#queryLteRegionSceneInfo()
	 * @author chen.c10
	 * @date 2015-7-7 下午1:49:41
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteRegionSceneInfoBySceneId(String sceneId) {
		return rnoLteSceneManageDao.queryLteRegionSceneInfoBySceneId(sceneId);
	}

	/**
	 * 更新地理场景管理信息
	 * @param sceneDataMap
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#updateLteRegionSceneData(java.util.Map)
	 * @author chen.c10
	 * @date 2015-7-8 下午6:17:34
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteRegionSceneDataBySceneId(Map<String, String> sceneParam) {	
		return (boolean)rnoLteSceneManageDao.updateLteRegionSceneDataBySceneId(sceneParam);
	}

	/**
	 * 通过地理场景类型查询场景名称列表
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#queryLteSceneNameListBySceneType()
	 * @author chen.c10
	 * @date 2015-7-9 下午12:17:44
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteRegionSceneNameList() {
		return rnoLteSceneManageDao.queryLteRegionSceneNameList();
	}
	/**
	 * 通过场景ID删除地理场景数据
	 * @param sceneId
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#deleteLteSceneDataForAjaxAction(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-10 上午11:09:44
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteRegionSceneData(String sceneId){
		return (boolean)rnoLteSceneManageDao.deleteLteRegionSceneData(sceneId); 
	}
/**
 * 插入新地理场景管理数据
 * @param sceneParam
 * @return
 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#insertLteSceneData(java.util.Map)
 * @author chen.c10
 * @date 2015-7-10 下午1:45:30
 * @company 怡创科技
 * @version V1.0
 */
	public boolean insertLteRegionSceneData(Map<String, String> sceneParam) {
		return (boolean)rnoLteSceneManageDao.insertLteRegionSceneData(sceneParam);
	}
	/**
	 * 更新时间场景管理信息
	 * @param sceneDataMap
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#updateLteRegionSceneData(java.util.Map)
	 * @author chen.c10
	 * @date 2015-7-8 下午6:17:34
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteTimeSceneDataBySceneId(Map<String, String> sceneParam) {	
		return (boolean)rnoLteSceneManageDao.updateLteTimeSceneDataBySceneId(sceneParam);
	}

	/**
	 * 查询时间场景名称列表
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#queryLteSceneNameListBySceneType()
	 * @author chen.c10
	 * @date 2015-7-9 下午12:17:44
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteTimeSceneNameList() {
		return rnoLteSceneManageDao.queryLteTimeSceneNameList();
	}
	/**
	 * 通过场景ID删除时间场景数据
	 * @param sceneId
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#deleteLteSceneDataForAjaxAction(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-10 上午11:09:44
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteTimeSceneData(String sceneId){
		return (boolean)rnoLteSceneManageDao.deleteLteTimeSceneData(sceneId); 
	}
/**
 * 插入新时间场景管理数据
 * @param sceneParam
 * @return
 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#insertLteSceneData(java.util.Map)
 * @author chen.c10
 * @date 2015-7-10 下午1:45:30
 * @company 怡创科技
 * @version V1.0
 */
	public boolean insertLteTimeSceneData(Map<String, String> sceneParam) {
		return (boolean)rnoLteSceneManageDao.insertLteTimeSceneData(sceneParam);
	}
	/**
	 * 通过场景ID查询时间场景信息
	 * @param sceneId
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteSceneManageService#queryLteTimeSceneInfoBySceneId(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-13 下午1:46:02
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteTimeSceneInfoBySceneId(String sceneId) {
	return rnoLteSceneManageDao.queryLteTimeSceneInfoBySceneId(sceneId);
	}
	
	/**
	 * 创建网格小区数据excel文件
	 * @param fileRealPath
	 * @param res 小区数据
	 * @param grid 网格
	 * @return
	 */
	public Boolean createExcelFile(List<Map<String,Object>> res,List<Map<String,Object>> grid,String fileRealPath) {
		
		FileOutputStream fos = null;
		
		try {
			File file = new File(fileRealPath);
	        if(!file.getParentFile().exists()){
	            file.getParentFile().mkdirs();
				}
	        if(!file.exists()){
	            try {
	            	file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return false;
	            }
	        }
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
		Row row;
		Cell cell;
		row = sheet.createRow((short)0);
		cell = row.createCell(0);
		cell.setCellValue("BAND_TYPE");
		cell = row.createCell(1);
		cell.setCellValue("COVER_TYPE");
		cell = row.createCell(2);
		cell.setCellValue("PCI");
		cell = row.createCell(3);
		cell.setCellValue("CELL_RADIUS");
		cell = row.createCell(4);
		cell.setCellValue("LOCAL_CELLID");
		cell = row.createCell(5);
		cell.setCellValue("AREA_ID");	
		cell = row.createCell(6);
		cell.setCellValue("STATUS");
		cell = row.createCell(7);
		cell.setCellValue("LATITUDE");
		cell = row.createCell(8);
		cell.setCellValue("TAC");
		cell = row.createCell(9);
		cell.setCellValue("M_DOWNTILT");
		cell = row.createCell(10);
		cell.setCellValue("BAND");
		cell = row.createCell(11);
		cell.setCellValue("CELL_NAME");
		cell = row.createCell(12);
		cell.setCellValue("TAL");	
		cell = row.createCell(13);
		cell.setCellValue("ENODEB_ID");
		cell = row.createCell(14);
		cell.setCellValue("DOWNTILT");
		cell = row.createCell(15);
		cell.setCellValue("BUSINESS_CELL_ID");	
		cell = row.createCell(16);
		cell.setCellValue("AZIMUTH");
		cell = row.createCell(17);
		cell.setCellValue("E_DOWNTILT");
		cell = row.createCell(18);
		cell.setCellValue("EARFCN");	
		cell = row.createCell(19);
		cell.setCellValue("SECTOR_ID");
		cell = row.createCell(20);
		cell.setCellValue("GROUND_HEIGHT");
		cell = row.createCell(21);
		cell.setCellValue("LTE_CELL_ID");
		cell = row.createCell(22);
		cell.setCellValue("LONGITUDE");
		cell = row.createCell(23);
		cell.setCellValue("RRUNUM");
		cell = row.createCell(24);
		cell.setCellValue("RRUVER");
		cell = row.createCell(25);
		cell.setCellValue("ANTENNA_TYPE");
		cell = row.createCell(26);
		cell.setCellValue("INTEGRATED");
		cell = row.createCell(27);
		cell.setCellValue("PDCCH");
		cell = row.createCell(28);
		cell.setCellValue("PA");
		cell = row.createCell(29);
		cell.setCellValue("PB");
		cell = row.createCell(30);
		cell.setCellValue("RSPOWER");
		cell = row.createCell(31);
		cell.setCellValue("COVER_RANGE");
		cell = row.createCell(32);
		cell.setCellValue("GRID_ID");
		int num = 0; 
		for (int i = 0; i < res.size(); i++) {
					
			for(int j=0;j<grid.size();j++){
				
				 Point p=null;
				 Line line=null;
			     String[] array = grid.get(j).get("GRID_LNGLATS").toString().split(";");
			     
			     //网格经纬度点集
			     List<Point> plist = new ArrayList<Point>();
			     for(int k=0;k<array.length;k++){
			    	 String[] point = array[k].split(","); 
				     p = new Point(Double.parseDouble(point[0]),Double.parseDouble(point[1]));
				     plist.add(p);
			 	}
			     /*System.out.println(plist);*/
			    
			     //网格经纬度边集
			    List<Line> llist = new ArrayList<Line>();
			 	for(int l = 1;l<plist.size();l++){	 	   
			 	    line=new Line(plist.get(l-1),plist.get(l));
			 	    llist.add(line);
			 	}
			 	/*System.out.println(llist);*/
			 	
			 	int intersectnum = 0;//射线与网格边交点个数
			 	for(int m = 0;m<llist.size();m++){
			 		//在当前网格边集中找出与过当前小区位置的水平线相交的网格边
			 		if((llist.get(m).getPoint1().getLat()<Double.parseDouble(res.get(i).get("LATITUDE").toString())
			 				&&Double.parseDouble(res.get(i).get("LATITUDE").toString())<llist.get(m).getPoint2().getLat())
			 				||(llist.get(m).getPoint2().getLat()<Double.parseDouble(res.get(i).get("LATITUDE").toString())
			 				&&Double.parseDouble(res.get(i).get("LATITUDE").toString())<llist.get(m).getPoint1().getLat())){
			 			
			 			//斜率
			 		    Double slope = (llist.get(m).getPoint1().getLng()-llist.get(m).getPoint2().getLng())/
			 					 (llist.get(m).getPoint1().getLat()-llist.get(m).getPoint2().getLat());
			 		    //水平线与网格边交点的经度
			 		    Double intersectlng = slope*(Double.parseDouble(res.get(i).get("LATITUDE").toString())-
			 		    		                llist.get(m).getPoint1().getLat())+llist.get(m).getPoint1().getLng();
			 		    //选取右射线
			 			if(Double.parseDouble(res.get(i).get("LONGITUDE").toString())<intersectlng){
			 			  intersectnum++;
			 			}
			 		}
			 	}
			 	//过滤不相交以及交点个数为偶数的网格
			 	if(intersectnum!=0&&intersectnum%2!=0){
			 		row = sheet.createRow((short)num + 1);
			 		cell = row.createCell(0);
					cell.setCellValue(res.get(i).get("BAND_TYPE")==null?"":res.get(i).get("BAND_TYPE").toString());
					cell = row.createCell(1);
					cell.setCellValue(res.get(i).get("COVER_TYPE")==null?"":res.get(i).get("COVER_TYPE").toString());
					cell = row.createCell(2);
					cell.setCellValue(res.get(i).get("PCI")==null?"":res.get(i).get("PCI").toString());//f
					cell = row.createCell(3);
					cell.setCellValue(res.get(i).get("CELL_RADIUS")==null?"":res.get(i).get("CELL_RADIUS").toString());//f
					cell = row.createCell(4);
					cell.setCellValue(res.get(i).get("LOCAL_CELLID")==null?"":res.get(i).get("LOCAL_CELLID").toString());//i
					cell = row.createCell(5);
					cell.setCellValue(res.get(i).get("AREA_ID")==null?"":res.get(i).get("AREA_ID").toString());//i	
					cell = row.createCell(6);
					cell.setCellValue(res.get(i).get("STATUS")==null?"":res.get(i).get("STATUS").toString());
					cell = row.createCell(7);
					cell.setCellValue(res.get(i).get("LATITUDE")==null?"":res.get(i).get("LATITUDE").toString());//d
					cell = row.createCell(8);
					cell.setCellValue(res.get(i).get("TAC")==null?"":res.get(i).get("TAC").toString());//f
					cell = row.createCell(9);
					cell.setCellValue(res.get(i).get("M_DOWNTILT")==null?"":res.get(i).get("M_DOWNTILT").toString());//f
					cell = row.createCell(10);
					cell.setCellValue(res.get(i).get("BAND")==null?"":res.get(i).get("BAND").toString());
					cell = row.createCell(11);
					cell.setCellValue(res.get(i).get("CELL_NAME")==null?"":res.get(i).get("CELL_NAME").toString());
					cell = row.createCell(12);
					cell.setCellValue(res.get(i).get("TAL")==null?"":res.get(i).get("TAL").toString());//f
					cell = row.createCell(13);
					cell.setCellValue(res.get(i).get("ENODEB_ID")==null?"":res.get(i).get("ENODEB_ID").toString());//f
					cell = row.createCell(14);
					cell.setCellValue(res.get(i).get("DOWNTILT")==null?"":res.get(i).get("DOWNTILT").toString());//f
					cell = row.createCell(15);
					cell.setCellValue(res.get(i).get("BUSINESS_CELL_ID")==null?"":res.get(i).get("BUSINESS_CELL_ID").toString());
					cell = row.createCell(16);
					cell.setCellValue(res.get(i).get("AZIMUTH")==null?"":res.get(i).get("AZIMUTH").toString());//f
					cell = row.createCell(17);
					cell.setCellValue(res.get(i).get("E_DOWNTILT")==null?"":res.get(i).get("E_DOWNTILT").toString());//f
					cell = row.createCell(18);
					cell.setCellValue(res.get(i).get("EARFCN")==null?"":res.get(i).get("EARFCN").toString());
					cell = row.createCell(19);
					cell.setCellValue(res.get(i).get("SECTOR_ID")==null?"":res.get(i).get("SECTOR_ID").toString());//i
					cell = row.createCell(20);
					cell.setCellValue(res.get(i).get("GROUND_HEIGHT")==null?"":res.get(i).get("GROUND_HEIGHT").toString());//f
					cell = row.createCell(21);
					cell.setCellValue(res.get(i).get("LTE_CELL_ID")==null?"":res.get(i).get("LTE_CELL_ID").toString());//i
					cell = row.createCell(22);
					cell.setCellValue(res.get(i).get("LONGITUDE")==null?"":res.get(i).get("LONGITUDE").toString());//d
					cell = row.createCell(23);
					cell.setCellValue(res.get(i).get("RRUNUM")==null?"":res.get(i).get("RRUNUM").toString());//f
					cell = row.createCell(24);
					cell.setCellValue(res.get(i).get("RRUVER")==null?"":res.get(i).get("RRUVER").toString());
					cell = row.createCell(25);
					cell.setCellValue(res.get(i).get("ANTENNA_TYPE")==null?"":res.get(i).get("ANTENNA_TYPE").toString());
					cell = row.createCell(26);
					cell.setCellValue(res.get(i).get("INTEGRATED")==null?"":res.get(i).get("INTEGRATED").toString());
					cell = row.createCell(27);
					cell.setCellValue(res.get(i).get("PDCCH")==null?"":res.get(i).get("PDCCH").toString());//f
					cell = row.createCell(28);
					cell.setCellValue(res.get(i).get("PA")==null?"":res.get(i).get("PA").toString());//f
					cell = row.createCell(29);
					cell.setCellValue(res.get(i).get("PB")==null?"":res.get(i).get("PB").toString());//f
					cell = row.createCell(30);
					cell.setCellValue(res.get(i).get("RSPOWER")==null?"":res.get(i).get("RSPOWER").toString());//f
					cell = row.createCell(31);
					cell.setCellValue(res.get(i).get("COVER_RANGE")==null?"":res.get(i).get("COVER_RANGE").toString());
			 		cell = row.createCell(32);
			 		cell.setCellValue(j+1);
			 		num++;
			 		break;
			 	}
		     }			       			    
		}	
        //最终写入文件
        try {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
}
