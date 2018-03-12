package com.iscreate.op.service.rno.parser;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.ServletContextAware;

import com.iscreate.op.constant.RnoConstant;

public class AreaLockManagerImpl implements ServletContextAware,AreaLockManager{

	private ServletContext sc;

	@Override
	public void setServletContext(ServletContext arg0) {
		sc = arg0;
	}

	public OperResult lockAreas(Set<Long> areaIds) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(false);
				res.setMessage("未指定需锁定的区域列表！");
				return res;
			}
			Set<Long> lcAreaIds = (Set<Long>) sc
					.getAttribute(RnoConstant.ApplicationConstant.LockAreaForImportList);
			if (lcAreaIds == null || lcAreaIds.isEmpty()) {
				if (lcAreaIds == null) {
					lcAreaIds = new HashSet<Long>();
					sc.setAttribute(
							RnoConstant.ApplicationConstant.LockAreaForImportList,
							lcAreaIds);
				}
				lcAreaIds.addAll(areaIds);
				res.setFlag(true);
			} else {
				areaIds.retainAll(lcAreaIds);
				if (areaIds.isEmpty()) {
					res.setFlag(true);
				} else {
					res.setFlag(false);
					res.setMessage(areaIds.toString());
				}
			}
			return res;
		}
	}

	/**
	 * 解锁指定区域
	 * 
	 * @param areaIds
	 * @return
	 * @author brightming 2014-5-16 上午11:37:03
	 */
	public OperResult unlockAreas(Set<Long> areaIds) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(true);// 也算成功
				res.setMessage("未指定需解锁的区域列表！");
				return res;
			}
			Set<Long> lcAreaIds = (Set<Long>) sc
					.getAttribute(RnoConstant.ApplicationConstant.LockAreaForImportList);
			if (lcAreaIds == null || lcAreaIds.isEmpty()) {
				res.setFlag(true);
			} else {
				lcAreaIds.removeAll(areaIds);
				res.setFlag(true);
			}
			return res;
		}
	}

	/**
	 * 导入mrr时，锁定指定区域
	 * 
	 * @param areaIds
	 * @return
	 * @author peng.jm 2014年7月23日16:52:41
	 */
	public OperResult lockAreasForMrr(Set<Long> areaIds) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(false);
				res.setMessage("未指定需锁定的区域列表！");
				return res;
			}
			Set<Long> lcAreaIds = (Set<Long>) sc
					.getAttribute(RnoConstant.ApplicationConstant.LockAreaForImportEriMrr);
			if (lcAreaIds == null || lcAreaIds.isEmpty()) {
				if (lcAreaIds == null) {
					lcAreaIds = new HashSet<Long>();
					sc.setAttribute(
							RnoConstant.ApplicationConstant.LockAreaForImportEriMrr,
							lcAreaIds);
				}
				lcAreaIds.addAll(areaIds);
				res.setFlag(true);
			} else {
				areaIds.retainAll(lcAreaIds);
				if (areaIds.isEmpty()) {
					res.setFlag(true);
				} else {
					res.setFlag(false);
					res.setMessage(areaIds.toString());
				}
			}
			return res;
		}

	}

	/**
	 * mrr完成导入，解锁指定区域
	 * 
	 * @param areaIds
	 * @return
	 * @author peng.jm 2014年7月23日16:52:41
	 */
	public OperResult unlockAreasForMrr(Set<Long> areaIds) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(true);// 也算成功
				res.setMessage("未指定需解锁的区域列表！");
				return res;
			}
			Set<Long> lcAreaIds = (Set<Long>) sc
					.getAttribute(RnoConstant.ApplicationConstant.LockAreaForImportEriMrr);
			if (lcAreaIds == null || lcAreaIds.isEmpty()) {
				res.setFlag(true);
			} else {
				lcAreaIds.removeAll(areaIds);
				res.setFlag(true);
			}
			return res;
		}
	}


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
	public OperResult lockAreas(Set<Long> areaIds, String serviceType) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			//业务类型至区域集合的映射
			Map<String, Set<Long>> serviceToAreaIds;
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(false);
				res.setMessage("未指定需锁定的区域列表！");
				return res;
			}
			Field[] interfaceFields = RnoConstant.ApplicationConstant.class
					.getFields();
			String interVal = "";
			//遍历常量是否存在该 业务类型
			for (Field field : interfaceFields) {
				try {
					String constVal = field.get(null).toString();
					if (constVal.equals(serviceType)) {
						interVal = constVal;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (!"".equals(interVal)) {
				serviceToAreaIds = (Map<String, Set<Long>>) sc.getAttribute(serviceType);
				if (serviceToAreaIds == null || serviceToAreaIds.isEmpty()) {
					if (serviceToAreaIds == null) {
						serviceToAreaIds = new HashMap<String, Set<Long>>();
						Set<Long> lcAreaIds=new HashSet<Long>();
						lcAreaIds.addAll(areaIds);
						serviceToAreaIds.put(serviceType, lcAreaIds);
						sc.setAttribute(serviceType, serviceToAreaIds);
					}
					res.setFlag(true);
				} else {
					Set<Long> lcAreaIds=serviceToAreaIds.get(serviceType);
					boolean flag=false;
					if (lcAreaIds!=null) {
//						flag=lcAreaIds.contains(areaIds);
						flag=lcAreaIds.containsAll(areaIds);
					}
					
					if (!flag) {
						res.setFlag(true);
						lcAreaIds.addAll(areaIds);
					} else {
						res.setFlag(false);
						res.setMessage(areaIds.toString());
						
					}
				}
			} else {
				res.setFlag(false);
				res.setMessage("输入的业务类型在应用上下文的常量字段不存在！");
				return res;
			}
			return res;
		}
	}

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
	public OperResult unlockAreas(Set<Long> areaIds, String serviceType) {
		synchronized (AreaLockManagerImpl.class) {
			OperResult res = new OperResult();
			//业务类型至区域集合的映射
			Map<String, Set<Long>> serviceToAreaIds;
			if (areaIds == null || areaIds.isEmpty()) {
				res.setFlag(true);// 也算成功
				res.setMessage("未指定需解锁的区域列表！");
				return res;
			}
			Field[] interfaceFields = RnoConstant.ApplicationConstant.class
					.getFields();
			String interVal = "";
			// 遍历常量是否存在该 业务类型
			for (Field field : interfaceFields) {
				try {
					String constVal = field.get(null).toString();
					if (constVal.equals(serviceType)) {
						interVal = constVal;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (!"".equals(interVal)) {
				serviceToAreaIds = (Map<String, Set<Long>>) sc.getAttribute(serviceType);
				if (serviceToAreaIds!=null) {
					Set<Long> lcAreaIds=serviceToAreaIds.get(serviceType);
					if (lcAreaIds!=null) {
						lcAreaIds.removeAll(areaIds);
						res.setFlag(true);
						res.setMessage("对以下区域"+areaIds.toString()+"解锁成功！");
					}
				}	
			} else {
				res.setFlag(false);
				res.setMessage("输入的业务类型在应用上下文的常量字段不存在！");
				return res;
			}

			return res;
		}
	}
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
public boolean checkAreaServiceState(long cityId,String serviceType) {
	//业务类型至区域集合的映射
	Map<String, Set<Long>> serviceToAreaIds;
	boolean flag=false;
	serviceToAreaIds=(Map<String, Set<Long>>)sc.getAttribute(serviceType);
	if(serviceToAreaIds!=null){
		Set<Long> lcAreaIds=serviceToAreaIds.get(serviceType);
		if (lcAreaIds!=null) {
			flag=lcAreaIds.contains(cityId);
		}
	}
	return flag;
}
}
