package com.iscreate.op.action.workmanage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.sso.session.UserInfo;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.workmanage.BizInfoFactoryService;
import com.iscreate.op.service.workmanage.WorkManageService;

public class WorkManageActionForMobile {

	private static final Log logger = LogFactory.getLog(WorkManageActionForMobile.class);
	
	private BizInfoFactoryService bizInfoFactoryService;
	
	private NetworkResourceInterfaceService networkResourceService;
	
	private WorkManageService workManageService;
	
	public void setBizInfoFactoryService(BizInfoFactoryService bizInfoFactoryService) {
		this.bizInfoFactoryService = bizInfoFactoryService;
	}
	
	
	/**
	 * 加载用户抢修待办任务
	 */
	public void loadUrgentRepairTaskForMobileAction() {
		try {
			
			logger.info("enter loadUrgentRepairTaskForMobileAction() method");
			
			String userId = (String) SessionService.getInstance().getValueByKey("userId");

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			if (userId != null) {

				String resourceType = "Station"; // hardcode
				String bizType = "urgentRepair";
				String taskType="";

				String content = mobilePackage.getContent();
				MobileContentHelper mch = new MobileContentHelper();
				mch.setContent(content);
				Map<String, String> formJsonMap = mch.getGroupByKey("request");
				String pageIndex = "1";
				String pageCount = "10";
				
				String conditionValue="";
				String sortName="";
				String sortOrder="";
				String longitude="";
				String latitude="";
				
				
				if (formJsonMap != null) {
					pageIndex = formJsonMap.get("pageIndex");
					taskType=formJsonMap.get("taskType");
					pageCount = formJsonMap.get("pageCount");
					conditionValue=formJsonMap.get("conditionValue");
					sortName=formJsonMap.get("sortName");
					sortOrder=formJsonMap.get("sortOrder");
					longitude=formJsonMap.get("longitude");
					latitude=formJsonMap.get("latitude");
				}

				if (pageIndex == null || "".equals(pageIndex)) {
					MobilePackage newMobilePackage = new MobilePackage();
					newMobilePackage.setResult("error");
					// 返回content的JSON字符串信息
					String resultPackageJsonStr = gson.toJson(newMobilePackage);
					response.getWriter().write(resultPackageJsonStr);
					return;
				}
				
				if (pageCount == null || "".equals(pageCount)) {
					MobilePackage newMobilePackage = new MobilePackage();
					newMobilePackage.setResult("error");
					// 返回content的JSON字符串信息
					String resultPackageJsonStr = gson.toJson(newMobilePackage);
					response.getWriter().write(resultPackageJsonStr);
					return;
				}
				
				
//				hardcode_test
//				sortName="distance";
//				longitude="113.3174";
//				latitude="23.08135";
				
				
				Map<String,String> sortCondition=null;
				if(sortName!=null && !"".equals(sortName)){
					sortCondition=new HashMap<String,String>();
					sortCondition.put(sortName, sortOrder);
				}
				
				
				List<Map> taskBeList=this.bizInfoFactoryService.getTaskOrderHandleService().getUserTaskOrdersByTaskOrderTypeWithPageForMobile(userId, Integer.parseInt(pageIndex), Integer.parseInt(pageCount),
						taskType,conditionValue,sortCondition,longitude,latitude);
				
//				System.out.println("pageIndex = "+pageIndex +" , taskType = "+taskType+" , 获取结果："+(taskBeList==null?0:taskBeList.size()));
				//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
//				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();
				if (taskBeList != null && !taskBeList.isEmpty()) {
					for (int i = 0; i < taskBeList.size(); i++) {
						Map<String, Object> map = (Map<String, Object>)taskBeList.get(i);
						
						
						//更改时间格式
						if(map.get("requireCompleteTime")!=null && !"".equals(map.get("requireCompleteTime").toString())){
							map.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(map.get("requireCompleteTime")));
						}
						
						if(map.get("assignTime")!=null && !"".equals(map.get("assignTime").toString())){
							map.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(map.get("assignTime")));
						}
						
						if(map.get("acceptTime")!=null && !"".equals(map.get("acceptTime").toString())){
							map.put("acceptTime", TimeFormatHelper.getTimeFormatBySecond(map.get("acceptTime")));
						}
						
						if(map.get("rejectTime")!=null && !"".equals(map.get("rejectTime").toString())){
							map.put("rejectTime", TimeFormatHelper.getTimeFormatBySecond(map.get("rejectTime")));
						}
						
						if(map.get("finalCompleteTime")!=null && !"".equals(map.get("finalCompleteTime").toString())){
							map.put("finalCompleteTime", TimeFormatHelper.getTimeFormatBySecond(map.get("finalCompleteTime")));
						}
						
						if(map.get("cancelTime")!=null && !"".equals(map.get("cancelTime").toString())){
							map.put("cancelTime", TimeFormatHelper.getTimeFormatBySecond(map.get("cancelTime")));
						}
						
						if(map.get("reassignTime")!=null && !"".equals(map.get("reassignTime").toString())){
							map.put("reassignTime", TimeFormatHelper.getTimeFormatBySecond(map.get("reassignTime")));
						}
						
						
						String tempMobileUrl = map.get("terminalFormUrl")==null?"": map.get("terminalFormUrl").toString();
						map.put("MOBILEFORMURL", tempMobileUrl);

						try {
//							if (map.get("assignTime") != null) {
//								Date beginTime = TimeFormatHelper.setTimeFormat((String) map.get("assignTime"));
//								map.put("assignTime", TimeFormatHelper.getTimeFormatByFree(beginTime, "dd/MM HH:mm"));
////								System.out.println("ori begtime = "+ map
////										.get("ASSIGNEDTIME")+", after = "+sdf.format(beginTime));
//							}else{
//								map.put("assignTime", "");
//							}
//							if (map.get("requireCompleteTime") != null) {
//								Date endTime =  TimeFormatHelper.setTimeFormat((String) map.get("requireCompleteTime"));
//								map.put("OVERTIME", TimeFormatHelper.getTimeFormatByFree(endTime,"dd/MM HH:mm"));
//								//由于比较时间需要这种格式，所以用此格式将时间保存到这个key
//								map.put("OVERTIME2",  TimeFormatHelper.getTimeFormatBySecond(endTime));
////								System.out.println("ori overtime = "+ map
////										.get("OVERTIME")+", after = "+sdf.format(endTime));
//							}else{
//								map.put("OVERTIME","");
//							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						Calendar calendar = Calendar.getInstance();
						String nowTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
						calendar.add(Calendar.MINUTE, 30);
						String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
						map.put("nowTime", nowTime); //系统当前时间
						map.put("halfLaterTime", halfLaterTime); //当前时间后的半小时
						map.put("TOID", map.get("toId"));
						map.put("WOID", map.get("woId"));
						taskMapList.add(map);
					}
				}
				
				Map<String, String> taskResultMap = new HashMap<String, String>();
				taskResultMap.put("resourceTaskOrderDiv", gson.toJson(taskMapList));
				mch.addGroup("resourceTaskOrderDivArea", taskResultMap);

				mobilePackage.setResult("success");
				mobilePackage.setContent(gson.toJson(taskMapList));
//				mobilePackage.setContent(mch.mapToJson());
			} else {
				mobilePackage.setResult("error");
			}

			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			// //System.out.println("" + resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索附近设施
	 * 
	 * 第一次搜索：搜索10公里以内，20个设施，不管有没有够20个，都返回
	 * 
	 * 点击更多进行的搜索： 需要获取更多的10个设施， 按照传入的距离参数查找，如果找不够10个，也返回，但是距离增加5公里，作为下一次查找的范围
	 * 
	 * @param type：设施类型
	 * @param center_lng,center_lat：中心坐标经纬度
	 * @param count:搜索几个 ,
	 *            第一次传入为20
	 * @param start:从第几个开始搜索,
	 *            第一次传入为0
	 * @param innerDistance：距离1,
	 *            第一次传入的为0
	 * @param outterDistance:距离2,
	 *            第一次传入的为10*1000 即10公里
	 * 
	 * Jun 26, 2012 5:43:40 PM gmh
	 */
	public void searchNearByFacilitiesActionForMobile() {
		// Date begin=new //Date();
		try {
			boolean extendRange = false;// 下次是否需要扩大范围查找
			int maxSearchCount=5;//如果一次搜索得不到任何数据（数量为0），则最多扩大搜索5次
			
			
			String myStructure = "networkresourcemanager";

			logger.debug("进入方法：sarchNearByFacilities");
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			String userId = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			if (mobilePackage == null) {
				logger.error("获取的mobilePackage为null");
				mobilePackage = new MobilePackage();
				mobilePackage.setResult("error");
				String resultPackageJsonStr = gson.toJson(mobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}

			// 获取前台传输过来的参数
			String content = mobilePackage.getContent();

			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> params = mch.getGroupByKey("request");

			logger.debug("前台传入的所有参数为：" + params);
//			System.out.println("前台传入的所有参数为：" + params);
			// 拆分各参数
			String type = params.get("type");
			double center_lng = params.get("center_lng") == null ? 0.0 : Double
					.parseDouble(params.get("center_lng"));
			double center_lat = params.get("center_lat") == null ? 0.0 : Double
					.parseDouble(params.get("center_lat"));
			
			String taskType=params.get("taskType");//任务类型：待办、跟踪、监督
			// 找几条
			int count = 20;
			if (params.get("count") == null
					|| params.get("count").trim().equals("")) {
				// System.out.println("参数中没有count");
				count = 20;
			} else {
				count = Integer.parseInt(params.get("count"));
			}
			// 起点
			int start = 0;
			if (params.get("start") == null
					|| params.get("start").trim().equals("")) {
				start = 0;
			} else {
				start = Integer.parseInt(params.get("start"));
			}
			// 近距离中心点距离
			double innerDistance = 0;
			if (params.get("innerDistance") == null
					|| params.get("innerDistance").trim().equals("")) {
				innerDistance = 0;
			} else {
				innerDistance = Double.parseDouble(params.get("innerDistance"));
			}

			// 远距离中心点距离
			double outterDistance = 500;
			if (params.get("outterDistance") == null
					|| params.get("outterDistance").trim().equals("")) {
				outterDistance = 500;
			} else {
				outterDistance = Double.parseDouble(params
						.get("outterDistance"));
			}
			logger.debug("taskType = "+taskType+", type = "+ type + ",center_lng=" + center_lng
					+ ",center_lat=" + center_lat + ",inner distance="
					+ innerDistance + "outter distance = " + outterDistance
					+ ",start=" + start + ",count=" + count);
//			 System.out.println("总的参数： taskType = "+taskType+" , type=" + type + ",center_lng="
//			 + center_lng + ",center_lat=" + center_lat
//			 + ",inner distance=" + innerDistance + "outter distance = "
//			 + outterDistance + ",start=" + start + ",count=" + count);

			// 实际获取的
			int realGetCount = 0;
			int searchTime=0;//当前搜索的次数
			
			// 寻找中心点指定范围内的指定数量的设施
			if (type == null || "".equals(type)) {
				type = "Search_8_ResWithLatLon";
			}

			List<Map<String, String>> allResource = new ArrayList<Map<String, String>>();
			int tempCount = count;
			int tempStart = start;
			double lastOutterDistance = outterDistance;
			// 数量不够，则扩展范围，继续搜索

			// 新范围的搜索
			// Date b4Dis=new //Date();
			do {
				logger.debug("第 " + (searchTime + 1) + " 次搜索 。 搜索条件： 资源类型 = " + type
						+ ", 范围=[" + innerDistance + "," + outterDistance
						+ "],起点= " + tempStart + ",需要记录数= " + tempCount);
//				System.out.println("第 " + (searchTime + 1) + " 次搜索 。 搜索条件： 资源类型 = " + type
//						+ ", 范围=[" + innerDistance + "," + outterDistance
//						+ "],起点= " + tempStart + ",需要记录数= " + tempCount);
				searchTime++;// 搜索次数加一
				//hardCode
				List<Map<String,String>> temp = this.networkResourceService.
						getEntityListRingByAetgAndGPSDistanceAndPagingService(type,
								innerDistance, outterDistance, center_lng,
								center_lat, null, tempStart, tempStart
										+ tempCount - 1, myStructure);
				// Date afDis=new //Date();
				// System.out.println("查询距离内的设施耗时 ：
				// "+(afDis.getTime()-b4Dis.getTime())+"毫秒");
				if (temp != null && temp.size() > 0) {
					realGetCount += temp.size();
					allResource.addAll(temp);
				}

				// 找到了需要的个数的设施
				if (realGetCount == count) {
					// 搜索范围不变
					start = start + temp.size();// 下一次发起的请求需要的数据记录的起点
				} else if(realGetCount == 0){
					//一个都没有找到
					// 准备新范围搜索的条件
					innerDistance = outterDistance;
					outterDistance += getOutterAddedDistance(searchTime,5*1000);//5*searchTime * 1000;// 向外扩展5公里
					start=0;
					extendRange = true;// 下次需要扩展范围
				}else {
					// 有找到部分数据，但没有找够，则下次要扩展范围，先将此次范围搜索的结果返回
					innerDistance = outterDistance;
					outterDistance += 5 * 1000;// 向外扩展5公里
					extendRange = true;// 下次需要扩展范围
				}
				logger.debug("搜索得到结果记录数量为：" + (temp == null ? 0 : temp.size()));
				logger.debug("进行了范围：[" + innerDistance + "," + outterDistance
						+ "]以后，获取的记录总数：" + realGetCount);
				
			
			} while (realGetCount == 0 && searchTime < maxSearchCount);
			
			// 下次查找10个
			count = 10;

			

			// 准备返回的map
			Map<String,String> result = new HashMap<String,String>();
			List<Map<String,String>> mid = new ArrayList<Map<String,String>>();

			// Date b4TaskCount=new //Date();
			// 找各个设施的关联的任务数
			for (int i = 0; i < allResource.size(); i++) {
				// Date b4OneCount=new //Date();
				Map<String,String> one = allResource.get(i);
				//System.out.println(one.get("_entityType")+"       "+one.get("id"));
				List<Map> bes = workManageService.getTaskOrderListByResourceAndUserId(one.get("_entityType"),one.get("id") ,userId,taskType);//2012-7-5 修改，之前用的前3个参数的函数，现在改为加多任务类型
				// Date afOneCount=new //Date();
				// System.out.println("查询一个设施的任务数耗时："+(afOneCount.getTime()-b4OneCount.getTime())+"毫秒");
				int taskCount = bes == null ? 0 : bes.size();

				// Map<String, Object> one = ae.toMap();
				one.put("taskCount", taskCount+"");
				one.put("importancegrade",
						(one.get("importancegrade") == null || one.get(
								"importancegrade").equals("null")) ? "普通" : one
								.get("importancegrade"));
				one.put("amountDistance", lastOutterDistance+"");
				String address = one.get("address");
				if(address.length()>30){
					address = address.substring(0, 28) + "...";
					one.put("address", address);
				}
				mid.add(one);

			}

			// Date afTaskCount=new //Date();
			// System.out.println("查询设施任务总耗时：
			// "+(afTaskCount.getTime()-b4TaskCount.getTime())+"毫秒");

			// 返回
			/**
			 * 1、当前的innerDistance,outterDistance 2、当前的start 3、count 4、得到的设施列表
			 */
			// String json = gson.toJson(mid);
			result.put("searchResult", gson.toJson(mid));// 得到的设施列表
			mch.addGroup("searchResultArea", result);

			Map<String, String> paramMap = new HashMap<String, String>();
			// 其他参数
			if (extendRange) {
				//下次如果扩展范围查找，那么就将起点设置为从0开始
				paramMap.put("start", "0");
			} else {
				paramMap.put("start", start + "");
			}
			paramMap.put("innerDistance", innerDistance + "");
			paramMap.put("outterDistance", outterDistance + "");
			paramMap.put("realGetCount", realGetCount + "");
			paramMap.put("count", count + "");

			mch.addGroup("paramsArea", paramMap);

			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson());

			String resultPackageJsonStr = gson.toJson(mobilePackage);

			// //System.out.println(resultPackageJsonStr);

			// Date end=new //Date();
			// System.out.println("查询设施耗时："+(end.getTime()-begin.getTime())/1000
			// +"秒");
			logger.debug("返回的json字符串为：" + resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取设施关联的任务单
	 * 
	 * @param resourceId
	 * @param resourceType
	 *            Jun 26, 2012 9:10:51 PM gmh
	 */
	public void getFacilityAssTaskOrderActionForMobile() {
		try {
			String myStructure = "networkresourcemanager";

			logger.debug("进入方法：sarchNearByFacilities");
			HttpSession session = ServletActionContext.getRequest()
					.getSession();
			String userId = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			if (mobilePackage == null) {
				logger.error("获取的mobilePackage为null");
				mobilePackage = new MobilePackage();
				mobilePackage.setResult("error");
				String resultPackageJsonStr = gson.toJson(mobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}

			// 获取前台传输过来的参数
			String content = mobilePackage.getContent();

			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> params = mch.getGroupByKey("request");

			logger.debug("前台传入的所有参数为：" + params);

			// System.out.println("获取设施方法： 前台传入的所有参数为：" +
			// params+",userId="+userId);
			// 分析参数
			String resourceType = params.get("resourceType");
			String resourceId = params.get("resourceId");

			if ("BaseStation".equals(resourceType)) {
				resourceType = "Station";
			}

			// resourceId="cd4ebad2edbf4ada8077e06462a3b5f1";
			// resourceType="Station";
			// //System.out.println("resourceType =
			// "+resourceType+",resourceId="+resourceId+",userId="+userId);
			
			List<Map> bes = workManageService.getTaskOrderListByResourceAndUserId(resourceType,resourceId ,userId,"pendingTaskOrder");//2012-7-5 修改，之前用的前3个参数的函数，现在改为加多任务类型

			// 测试数据
			// bes=getTestData();

			logger.debug("获取到设施：" + resourceType + ",id = " + resourceId
					+ " , 用户 ：" + userId + " 的  任务数量为:"
					+ (bes == null ? 0 : bes.size()));
			// System.out.println("获取到设施：" + resourceType + ",id = " +
			// resourceId
			// + " , 用户 ：" + userId + " 的 任务数量为:"
			// + (bes == null ? 0 : bes.size()));
			Map<String, String> result = new HashMap<String, String>();
			// List<Map<String, Object>> mid = new ArrayList<Map<String,
			// Object>>();
			//
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (bes != null && bes.size() > 0) {
				for (int i = 0; i < bes.size(); i++) {
					String toTypeName = "";
					Map<String, Object> be = bes.get(i);
					// System.out.println(be);
					try {
						// //System.out.println("totempId =
						// "+bes.get(i).get("TOTEMPID"));
//						int totempId = Integer.parseInt(be.get("toTitle")
//								.toString());
						// 获取任务单的名称
						toTypeName = be.get("bizProcessName")+"";
						if (be.get("assignTime") != null) {
							Date beginTime = sdf2.parse((String) be
									.get("assignTime"));
							be.put("assignTime", sdf.format(beginTime));
						} else {
							be.put("assignTime", "");
						}
						if (be.get("requireCompleteTime") != null) {
							Date endTime = sdf2.parse((String) be
									.get("requireCompleteTime"));
							be.put("requireCompleteTime", sdf.format(endTime));
						} else {
							be.put("requireCompleteTime", "");
						}
						// System.out.println("beginTime =
						// "+be.get("ASSIGNEDTIME")+",endTime =
						// "+be.get("OVERTIME"));

						// System.out.println("after sdf : beginTime =
						// "+sdf.format(beginTime)+", endTime =
						// "+sdf.format(endTime));

					} catch (Exception e) {
						e.printStackTrace();
					}
					be.put("toTypeName", toTypeName);
				}
			}

			String json = gson.toJson(bes);// 将任务列表转换为json字符串
			result.put("taskOrders", json);// 任务列表的json字符串作为value

			mch.addGroup("taskPanel", result);
			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson());

			String resultPackageJsonStr = gson.toJson(mobilePackage);
			logger.debug("返回的json字符串为：" + resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据搜索的次数，决定扩展的范围
	 * @param searchTime
	 * @param stepDistance
	 * @return
	 * Jul 2, 2012 11:08:56 AM
	 * gmh
	 */
	private int getOutterAddedDistance(int searchTime,int stepDistance){
		return searchTime*stepDistance;
	}

	/**
	 * 获取任务单的中文名
	 * 
	 * @param toTempId
	 * @return Jun 28, 2012 11:26:19 AM gmh
	 */
	private String getToTypeName(int toTempId) {
		String toTypeName = "任务单";
		switch (toTempId) {
		case 20:
			toTypeName = "人员调度单";
			break;
		case 21:
			toTypeName = "预处理单";
			break;
		case 22:
			toTypeName = "现场单";
			break;
		case 23:
			toTypeName = "技术支援单";
			break;

		case 36:
			toTypeName = "安排日常巡检任务单";
			break;
		case 37:
			toTypeName = "";
			break;
		case 38:
			toTypeName = "执行站点巡检任务单";
			break;

		}
		return toTypeName;
	}

	
	
	
	/**
	 * @author che.yd 指定条件查询抢修任务单
	 */
	public void searchUrgentRepairTaskOrderByConditionAction(){
		// Date begin=new //Date();
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			if (mobilePackage == null) {
				logger.error("获取的mobilePackage为null");
				mobilePackage = new MobilePackage();
				mobilePackage.setResult("error");
				String resultPackageJsonStr = gson.toJson(mobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}

			// 获取前台传输过来的参数
			String content = mobilePackage.getContent();

			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> params = mch.getGroupByKey("request");

			logger.error("前台传入的所有参数为：" + params);
			System.out.println("前台传入的所有参数为：" + params);
			
			StringBuilder sb_sql=new StringBuilder(" and 1=1 ");
			
			
			Map<String,Object> resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService("\"assignTime\"","desc","V_WM_URGENTREPAIR_TASKORDER",sb_sql.toString());
			List<Map> taskBeList=new ArrayList<Map>();
			if(resultMap!=null){
				taskBeList=(List<Map>)resultMap.get("entityList");
			}
			if(params!=null && !params.isEmpty()){
				String value="";
				for(Map.Entry<String, String> entry:params.entrySet()){
					String key=entry.getKey();
					if(params.get(key)!=null && !params.get("key").toString().isEmpty()){
						value=params.get(key).toString();
						
						if("taskOrderOrBaseStation".equals(key)){	//任务单号或者基站名称
							sb_sql.append(" and toId='"+value+"' or faultStationName='"+value+"'");
							
						}else if("assignTime".equals(key)){	//任务派发开始时间assignTime
							sb_sql.append("and assignTime>='"+value+"'");
							
						}else if("assignTime_".equals(key)){	//任务派发结束时间assignTime_
							sb_sql.append("and assignTime<='"+value+"'");
							
						}else{	//任务单类型toType,任务状态status
							sb_sql.append("and "+key+"='"+value+"'");
						}
					}
				}
			}
			
			
			List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();
			if (taskBeList != null && !taskBeList.isEmpty()) {
				for (int i = 0; i < taskBeList.size(); i++) {
					Map<String, Object> map = (Map<String, Object>)taskBeList.get(i);

					String tempMobileUrl = map.get("terminalFormUrl")==null?"": map.get("terminalFormUrl").toString();
					map.put("MOBILEFORMURL", tempMobileUrl);
					try {
						if (map.get("assignTime") != null) {
							Date beginTime = TimeFormatHelper.setTimeFormat((String) map.get("assignTime"));
							map.put("assignTime", TimeFormatHelper.getTimeFormatByFree(beginTime, "dd/MM HH:mm"));
//							System.out.println("ori begtime = "+ map
//									.get("ASSIGNEDTIME")+", after = "+sdf.format(beginTime));
						}else{
							map.put("assignTime", "");
						}
						if (map.get("requireCompleteTime") != null) {
							Date endTime =  TimeFormatHelper.setTimeFormat((String) map.get("requireCompleteTime"));
							map.put("OVERTIME", TimeFormatHelper.getTimeFormatByFree(endTime,"dd/MM HH:mm"));
							//由于比较时间需要这种格式，所以用此格式将时间保存到这个key
							map.put("OVERTIME2",  TimeFormatHelper.getTimeFormatBySecond(endTime));
//							System.out.println("ori overtime = "+ map
//									.get("OVERTIME")+", after = "+sdf.format(endTime));
						}else{
							map.put("OVERTIME","");
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					Calendar calendar = Calendar.getInstance();
					String nowTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
					calendar.add(Calendar.MINUTE, 30);
					String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
					map.put("nowTime", nowTime); //系统当前时间
					map.put("halfLaterTime", halfLaterTime); //当前时间后的半小时
					map.put("TOID", map.get("toId"));
					map.put("WOID", map.get("woId"));
					taskMapList.add(map);
				}
			}

			Map<String, String> taskResultMap = new HashMap<String, String>();
			taskResultMap.put("resourceTaskOrderDiv", gson.toJson(taskMapList));
			mch.addGroup("resourceTaskOrderDivArea", taskResultMap);
			
			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson());

			String resultPackageJsonStr = gson.toJson(mobilePackage);
			logger.debug("返回的json字符串为：" + resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 加载用户巡检待办任务
	 */
	public void loadRoutineInspectionTaskOrderForMobileAction() {
		try {
			
			String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			if (userId != null) {

				String taskType="";
				if(mobilePackage==null){
					mobilePackage = new MobilePackage();
				}
				String content = mobilePackage.getContent();
				MobileContentHelper mch = new MobileContentHelper();
//				System.out.println("mobilePackage====="+mobilePackage.toString());
//				System.out.println("content====="+content);
				mch.setContent(content);
				Map<String, String> formJsonMap = mch.getGroupByKey("request");
				String pageIndex = "1";
				String pageCount = "10";
				
				String conditionValue="";
				String sortName="";
				String sortOrder="";
				String longitude="";
				String latitude="";
				
				if (formJsonMap != null) {
					pageIndex = formJsonMap.get("pageIndex");
					taskType=formJsonMap.get("taskType");
					pageCount = formJsonMap.get("pageCount");
					conditionValue=formJsonMap.get("conditionValue");
					sortName=formJsonMap.get("sortName");
					sortOrder=formJsonMap.get("sortOrder");
					longitude=formJsonMap.get("longitude");
					latitude=formJsonMap.get("latitude");
				}

				if (pageIndex == null || "".equals(pageIndex)) {
					MobilePackage newMobilePackage = new MobilePackage();
					newMobilePackage.setResult("error");
					// 返回content的JSON字符串信息
					String resultPackageJsonStr = gson.toJson(newMobilePackage);
					response.getWriter().write(resultPackageJsonStr);
					return;
				}
				
				if (pageCount == null || "".equals(pageCount)) {
					MobilePackage newMobilePackage = new MobilePackage();
					newMobilePackage.setResult("error");
					// 返回content的JSON字符串信息
					String resultPackageJsonStr = gson.toJson(newMobilePackage);
					response.getWriter().write(resultPackageJsonStr);
					return;
				}
				
//				pageCount="25";
				
				
				//hardcode
//				sortName="requireCompleteTime";
//				sortOrder="desc";
				
//				sortName="distance";
//				sortOrder="asc";
//				longitude="113.3174";
//				latitude="23.08135";
				
				Map<String,String> sortCondition=null;
				if(sortName!=null && !"".equals(sortName)){
					sortCondition=new HashMap<String,String>();
					sortCondition.put(sortName, sortOrder);
				}
				
				
				
				List<Map> taskBeList=this.bizInfoFactoryService.getTaskOrderHandleService().getUserRoutineInspectionTaskOrderWithPageForMobile(userId, Integer.parseInt(pageIndex), Integer.parseInt(pageCount),
						taskType,conditionValue,sortCondition,longitude,latitude);
//				System.out.println(taskBeList);
				List<Map<String, Object>> taskMapList = new ArrayList<Map<String, Object>>();
				if (taskBeList != null && !taskBeList.isEmpty()) {
					for (int i = 0; i < taskBeList.size(); i++) {
						Map<String, Object> map = (Map<String, Object>)taskBeList.get(i);
						
						
						
						//更改时间格式
						if(map.get("requireCompleteTime")!=null && !"".equals(map.get("requireCompleteTime").toString())){
							map.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(map.get("requireCompleteTime")));
						}
						
						if(map.get("assignTime")!=null && !"".equals(map.get("assignTime").toString())){
							map.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(map.get("assignTime")));
						}
						
						if(map.get("acceptTime")!=null && !"".equals(map.get("acceptTime").toString())){
							map.put("acceptTime", TimeFormatHelper.getTimeFormatBySecond(map.get("acceptTime")));
						}
						
						
						if(map.get("finalCompleteTime")!=null && !"".equals(map.get("finalCompleteTime").toString())){
							map.put("finalCompleteTime", TimeFormatHelper.getTimeFormatBySecond(map.get("finalCompleteTime")));
						}
						
						if(map.get("taskPlanBeginTime")!=null && !"".equals(map.get("taskPlanBeginTime").toString())){
							map.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatBySecond(map.get("taskPlanBeginTime")));
						}
						if(map.get("taskPlanEndTime")!=null && !"".equals(map.get("taskPlanEndTime").toString())){
							map.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatBySecond(map.get("taskPlanEndTime")));
						}
						
						if(map.get("signInTime")!=null && !"".equals(map.get("signInTime").toString())){
							map.put("signInTime", TimeFormatHelper.getTimeFormatBySecond(map.get("signInTime")));
						}
						
						if(map.get("signOutTime")!=null && !"".equals(map.get("signOutTime").toString())){
							map.put("signOutTime", TimeFormatHelper.getTimeFormatBySecond(map.get("signOutTime")));
						}
						
						
						
						

						

						String tempMobileUrl = map.get("terminalFormUrl")==null?"": map.get("terminalFormUrl").toString();
						map.put("MOBILEFORMURL", tempMobileUrl);

//						try {
//							if (map.get("assignTime") != null) {
//								Date beginTime = TimeFormatHelper.setTimeFormat((String) map.get("assignTime"));
//								map.put("assignTime", TimeFormatHelper.getTimeFormatByFree(beginTime, "dd/MM HH:mm"));
////								System.out.println("ori begtime = "+ map
////										.get("ASSIGNEDTIME")+", after = "+sdf.format(beginTime));
//							}else{
//								map.put("assignTime", "");
//							}
//							if (map.get("requireCompleteTime") != null) {
//								Date endTime =  TimeFormatHelper.setTimeFormat((String) map.get("requireCompleteTime"));
//								map.put("OVERTIME", TimeFormatHelper.getTimeFormatByFree(endTime,"dd/MM HH:mm"));
//								//由于比较时间需要这种格式，所以用此格式将时间保存到这个key
//								map.put("OVERTIME2",  TimeFormatHelper.getTimeFormatBySecond(endTime));
////								System.out.println("ori overtime = "+ map
////										.get("OVERTIME")+", after = "+sdf.format(endTime));
//							}else{
//								map.put("OVERTIME","");
//							}
//							
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						
						Calendar calendar = Calendar.getInstance();
						String nowTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
						calendar.add(Calendar.MINUTE, 30);
						String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
						map.put("nowTime", nowTime); //系统当前时间
						map.put("halfLaterTime", halfLaterTime); //当前时间后的半小时
						map.put("TOID", map.get("toId"));
						map.put("WOID", map.get("woId"));
						taskMapList.add(map);
					}
				}

				Map<String, String> taskResultMap = new HashMap<String, String>();
				taskResultMap.put("resourceTaskOrderDiv", gson.toJson(taskMapList));
				mch.addGroup("resourceTaskOrderDivArea", taskResultMap);

				mobilePackage.setResult("success");
//				mobilePackage.setContent(mch.mapToJson());
				mobilePackage.setContent(gson.toJson(taskMapList));  //mch.mapToJson()
			} else {
				mobilePackage.setResult("error");
			}

			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			// //System.out.println("" + resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 终端对巡检任务签到
	 */
	public void taskRoutineInspectionTaskForMobileAction(){
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
		
		if(mobilePackage!=null){
			
			String toId="";
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if (formJsonMap != null) {
				toId=formJsonMap.get("toId");
			}
			
			if (toId == null || "".equals(toId)) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				// 返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				try {
					response.getWriter().write(resultPackageJsonStr);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("toId值为空，终端获取巡检任务失败");
				}
				return;
			}
			
			boolean isSuccess=this.workManageService.takeTask(toId, userId);
			
			if(isSuccess){
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("success");
				// 返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				try {
					response.getWriter().write(resultPackageJsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				try {
					response.getWriter().write(resultPackageJsonStr);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("根据toId【值："+toId+"】，终端获取巡检任务出错");
				}
			}
		}else{
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("响应【终端获取巡检任务】出错");
			}
			return;
		}
	}
	
	
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	
}
