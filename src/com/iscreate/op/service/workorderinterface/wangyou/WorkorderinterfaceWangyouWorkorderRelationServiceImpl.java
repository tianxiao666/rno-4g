package com.iscreate.op.service.workorderinterface.wangyou;

import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelationDao;
import com.iscreate.op.pojo.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelation;

public class WorkorderinterfaceWangyouWorkorderRelationServiceImpl implements
		WorkorderinterfaceWangyouWorkorderRelationService {

	
	private WorkorderinterfaceWangyouWorkorderRelationDao workorderinterfaceWangyouWorkorderRelationDao;
	
	public void setWorkorderinterfaceWangyouWorkorderRelationDao(
			WorkorderinterfaceWangyouWorkorderRelationDao workorderinterfaceWangyouWorkorderRelationDao) {
		this.workorderinterfaceWangyouWorkorderRelationDao = workorderinterfaceWangyouWorkorderRelationDao;
	}
	
	
	/**
	 * 保存IOSM工单与客户工单关系
	 * @param relation
	 */
	public void saveWorkorderinterfaceWangyouWorkorderRelation(WorkorderinterfaceWangyouWorkorderRelation relation){
		this.workorderinterfaceWangyouWorkorderRelationDao.saveWorkorderinterfaceWangyouWorkorderRelation(relation);
	}

	
	/**
	 * 根据条件获取网优2g工单列表
	 * @param params
	 */
	public List<WorkorderinterfaceWangyouWorkorderRelation> getWorkorderinterfaceWangyouWorkorderRelationList(
			Map<String, String> params) {
		List<WorkorderinterfaceWangyouWorkorderRelation> list=null;
		if(params!=null && !params.isEmpty()){
			return this.workorderinterfaceWangyouWorkorderRelationDao.getWorkorderinterfaceWangyouWorkorderRelationList(params);
		}
		
		return list;
	}

}
