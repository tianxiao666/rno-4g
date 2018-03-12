package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.GisAnalysisCellQueryResult;
import com.iscreate.op.action.rno.model.GisInterferenceCellQueryResult;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCell;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCellTopN;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoInterferenceAnalysisGisCell;

public interface RnoInterferenceAnalysisService {
	
	/**
	 * 分页获取区/县的rnoAnalysisGisCells
	 * @author ou.jh
	 * @date Nov 6, 2013
	 * @Description: TODO
	 * 
	 * @param areaId
	 *            指定区域
	 * @param page
	 *            分页参数
	 * 
	 */
	public GisAnalysisCellQueryResult getRnoAnalysisGisCellByPage(long areaId,long cellConfigId,boolean isCellTempStorage, Page page,long interConfigId,boolean isInterTemp);
	/**
	 * top-N 最大干扰小区标注
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
//	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long configId,final long rank,final long areaId,final boolean isTempStorage);
	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final long rank,final long areaId);
	
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
//	public RnoInterferenceAnalysisGisCell getCellInterferenceAnalysis(final long configId,final long areaId,final String cellLabel,final boolean isTempStorage);
	public RnoInterferenceAnalysisGisCell getCellInterferenceAnalysis(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final long areaId,final String cellLabel);
	/**
	 * 根据CELLLABEL获取干扰邻区
	* @author ou.jh
	* @date Nov 11, 2013 4:51:44 PM
	* @Description: TODO 
	* @param @param label        
	* @throws
	 */
//	public List<RnoGisCell> getInterferenceCellByLabel(final long configId,final boolean isTempStorage,final String label);
	public List<RnoGisCell> getInterferenceCellByLabel(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final String label);
	/**
	 * 根据小区获取干扰频点
	* @author ou.jh
	* @date Nov 12, 2013 11:22:01 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
//	public Map<String, Object> getInterferenceTCH(final long configId,final long areaId,final boolean isTempStorage,final String label);
	public List<Map<String, Object>> getInterferenceTCH(long cellConfigId,boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String label);
}
