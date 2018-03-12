package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoAnalysisGisCell;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCellTopN;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoGisCellInterference;
import com.iscreate.op.pojo.rno.RnoInterferenceAnalysisGisCell;

public interface RnoInterferenceAnalysisDao {
	
	/**
	 * 获取指定区/县区域下的小区
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<RnoAnalysisGisCell> getRnoGisCellInArea(final long areaId,final long cellConfigId,final boolean isCellTempStorage,final long interConfigId,final boolean isInterTemp) ;
	/**
	 * top-N 最大干扰小区标注
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long rank,final long areaId); 
	/**
	 * 小区干扰分析
	* @author ou.jh
	* @date Nov 11, 2013 11:07:08 AM
	* @Description: TODO 
	* @param @param configId
	* @param @param areaId
	* @param @param cellLabel        
	* @throws
	 */
//	public List<RnoInterferenceAnalysisGisCell> getCellInterferenceAnalysis(final long configId,final long areaId,final String cellLabel,final boolean isTempStorage);
	public List<RnoInterferenceAnalysisGisCell> getCellInterferenceAnalysis(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String cellLabel);
	/**
	 * 根据CELLLABEL获取干扰邻区
	* @author ou.jh
	* @date Nov 11, 2013 4:51:44 PM
	* @Description: TODO 
	* @param @param label        
	* @throws
	 */
//	public List<RnoGisCell> getInterferenceCellByLabel(final long configId,final boolean isTempStorage,final String label);
	public List<RnoGisCell> getInterferenceCellByLabel(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final String label);
	/**
	 * 根据小区获取干扰频点
	* @author ou.jh
	* @date Nov 12, 2013 11:22:01 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
//	public List<Map<String, Object>> getInterferenceTCH(final long configId,final long areaId,final boolean isTempStorage,final String label);
	public List<Map<String, Object>> getInterferenceTCH(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String label);
	
}


