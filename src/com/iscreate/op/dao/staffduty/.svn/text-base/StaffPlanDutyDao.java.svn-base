package com.iscreate.op.dao.staffduty;

import java.util.List;
import java.util.Map;

public interface StaffPlanDutyDao {

	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * (请求参数) 	dutyDate 日期List集合
	 *  			account 人员账号
	 *  			freId 班次List集合
	 *  			bizId 人员所在组织List集合
	 * @return (List<Map<String,Object>>) 排班信息集合
	 */
	public abstract List<Map<String,String>> findStaffListByStaffName(String staffName , List<String> bizIds );

	/**
	 * 读取人员班次信息集合
	 * @return 人员班次信息集合
	 */
	public abstract List<Map<String, String>> loadStaffDutyFreq();

	/**
	 * 根据人员姓名,查询人员的信息集合
	 * @param staffName - 人员姓名 
	 * @return (List<Map<String,String>>) 人员信息集合
	 */
	public abstract List<Map<String,Object>> getStaffDutyList(String staffId,List<String> dutyDate, List<String> bizId,
			List<String> freId, String account);

	/**
	 * 根据条件删除排班信息
	 * @param dutyDate - 值班日期
	 * @param freId - 班次
	 * @param account - 人员账号 
	 * @return (Integer) 是否成功
	 */
	public abstract Integer deleteStaffDutyTemplate(String dutyDate, String freId,
			String account);

	/**
	 * 根据条件,查询排班信息集合
	 * @param dutyDate - 排班日期
	 * @param freId - 班次id
	 * @param account - 人员账号
	 * @return (List<Map<String,String>>) 排班信息集合
	 */
	public abstract List<Map<String,String>> findStaffDutyTemplate(String dutyDate, String freId,
			String account);

	/**
	 * 保存人员排班
	 * @param dutyDate - 排班日期
	 * @param freId - 班次id
	 * @param account - 人员账号
	 * @return (Integer) 是否成功
	 */
	public Integer saveStaffDutyTemplate ( String dutyDate , String freId , String account );

}
