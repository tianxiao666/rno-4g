package com.iscreate.op.service.staffduty;

import java.util.List;
import java.util.Map;

public interface StaffPlanDutyService {

	public abstract Map<String, Map<String, Map<String, Map<String, Object>>>> staffNewDutyList(String staffId,String bizId, String name,
			String date_string);

	public abstract Map<String, Map<String, List<Map<String, Object>>>> staffNewDutyCalendar(String staffId,String bizId, String name,
			String date_string);

	public abstract List<Map<String, String>> loadStaffDutyFreq();

	public abstract List<Map<String, String>> findStaffListByStaffName( String staffName , String bizId );

	public abstract boolean staffDutyUpdate( String dutyDate , String freId , List<Map<String, String>> delIds , List<Map<String, String>> addIds );

	public abstract boolean deleteStaffDutyTemplateByDate(String dutyDate ,String freId );


}
