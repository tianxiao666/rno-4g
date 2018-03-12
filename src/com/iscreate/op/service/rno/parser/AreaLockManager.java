package com.iscreate.op.service.rno.parser;

import java.util.Set;

public interface AreaLockManager {

	public OperResult lockAreas(Set<Long> areaIds) ;
	
	public OperResult unlockAreas(Set<Long> areaIds) ;

	public OperResult lockAreasForMrr(Set<Long> areaIds);
	
	public OperResult unlockAreasForMrr(Set<Long> areaIds);
	/**
	 * 
	 * @title 通过业务类型锁定相应区域
	 * @param areaIds
	 * @param serviceType
	 * @return
	 * @author chao.xj
	 * @date 2014-7-31下午3:33:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public OperResult lockAreas(Set<Long> areaIds,String serviceType);
	/**
	 * 
	 * @title 通过业务类型解锁相应区域
	 * @param areaIds
	 * @param serviceType
	 * @return
	 * @author chao.xj
	 * @date 2014-7-31下午3:33:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public OperResult unlockAreas(Set<Long> areaIds,String serviceType);
	/**
	 * 
	 * @title 检测区域业务状态
	 * @param cityId
	 * @param serviceType
	 * @return
	 * @author chao.xj
	 * @date 2014-10-10下午3:10:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean checkAreaServiceState(long cityId,String serviceType);
}
