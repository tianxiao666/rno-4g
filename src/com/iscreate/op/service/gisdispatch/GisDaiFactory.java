package com.iscreate.op.service.gisdispatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchDriverDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.service.cardispatch.CardispatchDutyService;
import com.iscreate.op.service.cardispatch.CardispatchManageService;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.report.OrgStaffCountMonthReportService;
import com.iscreate.op.service.staffduty.StaffDutyService;
import com.iscreate.op.service.staffduty.StaffService;
import com.iscreate.op.service.staffduty.StaffSkillService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.location.service.PdaGpsService;

/**
 * @author che.yd
 * Gis数据访问接口工厂
 */
public class GisDaiFactory {
	
	
	private NetworkResourceInterfaceService networkResourceService;	//网络资源Service
	
		
	private PdaGpsService pdaGpsService;							//终端GPS Service
	
	private WorkManageService workManageService;					//工作管理Service
	
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;	//工单管理资源Service
		
	private CardispatchCarDao cardispatchCarDao;					//车辆调度车辆Service
	
	private CardispatchDriverDao cardispatchDriverDao;				//车辆调度司机Service
	
	private CardispatchTerminalDao cardispatchTerminalDao;			//车辆调度终端Service
	
	private StaffSkillService staffSkillService;					//人员技能Service
	
	private StaffDutyService staffDutyService;						//人员值班Service
	
	private CardispatchDutyService cardispatchDutyService;			//车辆排班Service
	
	private CardispatchManageService cardispatchManageService;		//车辆管理Service
	
	private OrgStaffCountMonthReportService orgStaffCountMonthReportService;//组织人员数量Service
	
//	private CardispatchWorkorderService cardispatchWorkorderService;//车辆调度工单Service
	
	
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	

	public PdaGpsService getPdaGpsService() {
		return pdaGpsService;
	}

	public void setPdaGpsService(PdaGpsService pdaGpsService) {
		this.pdaGpsService = pdaGpsService;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}

	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}

	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}

	public CardispatchDriverDao getCardispatchDriverDao() {
		return cardispatchDriverDao;
	}

	public void setCardispatchDriverDao(CardispatchDriverDao cardispatchDriverDao) {
		this.cardispatchDriverDao = cardispatchDriverDao;
	}

	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}

	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}

	public StaffSkillService getStaffSkillService() {
		return staffSkillService;
	}

	public void setStaffSkillService(StaffSkillService staffSkillService) {
		this.staffSkillService = staffSkillService;
	}

	public StaffDutyService getStaffDutyService() {
		return staffDutyService;
	}

	public void setStaffDutyService(StaffDutyService staffDutyService) {
		this.staffDutyService = staffDutyService;
	}

	public CardispatchDutyService getCardispatchDutyService() {
		return cardispatchDutyService;
	}

	public void setCardispatchDutyService(
			CardispatchDutyService cardispatchDutyService) {
		this.cardispatchDutyService = cardispatchDutyService;
	}

	public CardispatchManageService getCardispatchManageService() {
		return cardispatchManageService;
	}

	public void setCardispatchManageService(
			CardispatchManageService cardispatchManageService) {
		this.cardispatchManageService = cardispatchManageService;
	}

	public OrgStaffCountMonthReportService getOrgStaffCountMonthReportService() {
		return orgStaffCountMonthReportService;
	}

	public void setOrgStaffCountMonthReportService(
			OrgStaffCountMonthReportService orgStaffCountMonthReportService) {
		this.orgStaffCountMonthReportService = orgStaffCountMonthReportService;
	}

}
