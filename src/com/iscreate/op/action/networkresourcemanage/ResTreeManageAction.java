package com.iscreate.op.action.networkresourcemanage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.log.service.LogService;
import com.iscreate.op.service.networkresourcemanage.PhysicalresService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;


public class ResTreeManageAction {
	// 日志
	private LogService logService;
	private StructureCommonService structureCommonService;
	private PhysicalresService physicalresService;
	private StaffOrganizationService staffOrganizationService;
	
	private String parentEntityType;//父实体类型
	private String parentEntityId;//父实体id
	private String searchType;//要查找的当前节点的类型
	
	private String currentEntityType;//当前节点类型
	private String currentEntityId;//当前节点id

	private String chooseResEntityId;
	private String chooseResEntityType;
	private String needChildEntityType;
	private String needLinkEntityType;
	private String loadedType;
	
	//用来标识，获取树类型时，除了获取child的，还要获取link的
	private String addLink;
	//需要通过该类型获取子节点
	private String linkType;
	
	private Dictionary dictionary;
	
	private String viewName;
	
	private QuickSort<Map<String,Object>> quickSort; //对entity属性及对象排序

	private String addInputType;
	
	//Structure实例名字
	private String myStructure;
	
	private List<Map<String, Object>> parentAreaList;
	
	
	
	
	
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}

	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}

	public String getMyStructure() {
		return myStructure;
	}

	public void setMyStructure(String myStructure) {
		this.myStructure = myStructure;
	}

	public String getAddInputType() {
		return addInputType;
	}

	public void setAddInputType(String addInputType) {
		this.addInputType = addInputType;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getAddLink() {
		return addLink;
	}

	public void setAddLink(String addLink) {
		this.addLink = addLink;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getNeedLinkEntityType() {
		return needLinkEntityType;
	}

	public void setNeedLinkEntityType(String needLinkEntityType) {
		this.needLinkEntityType = needLinkEntityType;
	}

	public String getChooseResEntityId() {
		return chooseResEntityId;
	}

	public void setChooseResEntityId(String chooseResEntityId) {
		this.chooseResEntityId = chooseResEntityId;
	}

	public String getChooseResEntityType() {
		return chooseResEntityType;
	}

	public void setChooseResEntityType(String chooseResEntityType) {
		this.chooseResEntityType = chooseResEntityType;
	}

	public String getNeedChildEntityType() {
		return needChildEntityType;
	}

	public void setNeedChildEntityType(String needChildEntityType) {
		this.needChildEntityType = needChildEntityType;
	}

	public String getLoadedType() {
		return loadedType;
	}

	public void setLoadedType(String loadedType) {
		this.loadedType = loadedType;
	}

	public String getCurrentEntityType() {
		return currentEntityType;
	}

	public void setCurrentEntityType(String currentEntityType) {
		this.currentEntityType = currentEntityType;
	}

	public String getCurrentEntityId() {
		return currentEntityId;
	}

	public void setCurrentEntityId(String currentEntityId) {
		this.currentEntityId = currentEntityId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getParentEntityType() {
		return parentEntityType;
	}

	public void setParentEntityType(String parentEntityType) {
		this.parentEntityType = parentEntityType;
	}

	public String getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(String parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	public PhysicalresService getPhysicalresService() {
		return physicalresService;
	}

	public void setPhysicalresService(PhysicalresService physicalresService) {
		this.physicalresService = physicalresService;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	/**
	 * 获取一个节点的子节点的类型(同时获取该类型下的节点数量)
	 */
	public void getTypeByEntityAction() {
		long currentEntityId = Long.parseLong(this.currentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		
		if(physicalresAppEntity != null) {
//			if(){
//				
//			}
			//获取父级节点下的类型
			List<Map<String, Object>> lists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity, AssociatedType.CHILD, this.addLink, this.viewName, "networkresourcemanage");
			//判断是否需要获取link的类型
			if(this.addLink != null) {
				List<Map<String, Object>> linkLists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity, AssociatedType.LINK, this.addLink, this.viewName, "networkresourcemanage");
				//child的list和link的list加起来
				lists.addAll(linkLists);
			}
			if(lists != null && !lists.isEmpty()) {
				for (Map<String, Object> typeMap : lists) {
					try {
						//数据字典，中英文转换
						List<BasicEntity> entry = dictionary.getEntry(typeMap.get("type") + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							
							//System.out.println(typeMap.get("type"));
							//System.out.println(entry.get(0).getValue("display"));
							
							
							typeMap.put("chineseType", entry.get(0).getValue("display"));
						}
					} catch (EntryOperationException e) {
						e.printStackTrace();
						//没有中文，继续显示英文
						typeMap.put("chineseType", typeMap.get("type"));
					}
				}
			}
			
			//如果是ODF，额外增加纤芯成端类型节点
			if("ODF".equals(this.currentEntityType) || "FiberDistributionCabinet".equals(this.currentEntityType) 
					|| "FiberCrossCabinet".equals(this.currentEntityType) 
					|| "FiberTerminalCase".equals(this.currentEntityType)) {
				Map<String, Object> extraPhysicalresMap = new HashMap<String, Object>();
				extraPhysicalresMap.put("type", "纤芯成端");
				extraPhysicalresMap.put("chineseType", "纤芯成端");
				int extraPhysicalresCount = 0; //纤芯成端节点的个数
				
				//加载跳纤与尾纤
				Map<String, Object> extraPhysicalresPigtailedFiberMap = new HashMap<String, Object>();
				extraPhysicalresPigtailedFiberMap.put("type", "PigtailedFiber");
				
				try {
					//数据字典，中英文转换(转换跳纤或尾纤的中文)
					List<BasicEntity> entry = dictionary.getEntry("PigtailedFiber,networkResourceDefination", SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						extraPhysicalresPigtailedFiberMap.put("chineseType", entry.get(0).getValue("display"));
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
				}
				
				int extraPhysicalresPigtailedCount = 0; //跳纤与尾纤的节点个数
				
				//因为两个端子连着一个跳纤与尾纤，所以跳纤与尾纤会出现重复的情况，需要进行过滤
				Map<String, Object> pigtailedFiberAppEntityArrsFilterMap = new HashMap<String, Object>();
				
				//获取父级entity下的端子或者父级entity下的ODM的端子
				//父类型为光分纤箱或者终端盒时，不需要ODM
				if("FiberDistributionCabinet".equals(this.currentEntityType) || "FiberTerminalCase".equals(this.currentEntityType)) {
					//获取父级entity下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的纤芯
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								//一个端子只对应一个纤芯
								ApplicationEntity fiberCoreAppEntity = fiberCoreAppEntityArrs[0];
								//获取纤芯所关联的光缆段
								ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
								if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
									extraPhysicalresCount++;
								}
							}
							
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
									//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
									pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
								}
							}
						}
					}
				} else {
					//当父类型为光交接箱和ODF时，需要带ODM
					ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
					if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
						for(ApplicationEntity odmApp: odmAppEntityArrs) {
							//获取ODM下的端子
							ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
							if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
								for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
									//获取与端子关联的纤芯
									ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
									if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
										//一个端子只对应一个纤芯
										for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
											//获取纤芯所关联的光缆段
											ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
											if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
												extraPhysicalresCount++;
											}
										}
									}
									
									//获取与端子关联的跳纤与尾纤
									ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
									if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
										for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
											//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
											pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
										}
									}
								}
							}
						}
					}
				}
				
				extraPhysicalresMap.put("count", extraPhysicalresCount);//获取纤芯个数
				lists.add(extraPhysicalresMap);
				
				if(pigtailedFiberAppEntityArrsFilterMap != null && pigtailedFiberAppEntityArrsFilterMap.size() > 0) {
					extraPhysicalresPigtailedCount = pigtailedFiberAppEntityArrsFilterMap.size();
				}
				extraPhysicalresPigtailedFiberMap.put("count", extraPhysicalresPigtailedCount);//获取跳纤与尾纤个数
				lists.add(extraPhysicalresPigtailedFiberMap);
			} else if("Joint".equals(this.currentEntityType)) {
				Map<String, Object> extraPhysicalresMap = new HashMap<String, Object>();
				extraPhysicalresMap.put("type", "熔纤接续");
				extraPhysicalresMap.put("chineseType", "熔纤接续");
				int extraPhysicalresCount = 0; //纤芯成端节点的个数
				
				//获取接头连接的光缆段
				ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
				if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
					//此map用来过滤重复数据
					List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
					for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
						//查出光缆段连接的纤芯
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							//查出纤芯所连接的纤芯
							for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
								ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
								if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
									for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
										//查出连接纤芯所属的光缆段
										ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
										if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
											//一条纤芯只属于一个光缆段
											ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
											//获取纤芯所连接的纤芯是否属于本接头的光缆段
											
											boolean isTheSameFiberSection = false;
											for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
												//纤芯所连接的纤芯不属于该接头的光缆段的情况
												if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
													isTheSameFiberSection = true;
												}
											}
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(!isTheSameFiberSection) {
												continue;
											}
								
											//进行重复数据的过滤操作
											Map<String ,Object> targetMap = new HashMap<String, Object>();
											targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
											targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
											targetMapList.add(targetMap);
										}
									}
								}
							}
							
							
						}
					}
					for(Map<String, Object> targetMap : targetMapList) {
						for(Map<String, Object> targetOppositeMap : targetMapList) {
							String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
							String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
							if(targetMapStr.equals(targetOppositeMapStr)) {
								targetMap.remove("leftId");
								targetMap.remove("rightId");
							}
						}
					}
					
					for(Map<String, Object> targetMap : targetMapList) {
						if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
							//System.out.println(targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							extraPhysicalresCount++;//累加熔纤接续数量
						}
					}
				}
				extraPhysicalresMap.put("count", extraPhysicalresCount);
				lists.add(extraPhysicalresMap);
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(lists);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取某一节点下，某类型的子节点的个数
	 */
	public void getChildEntityCountByTypeAction() {
		long parentEntityId = Long.parseLong(this.parentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.parentEntityType, parentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		//特殊的物理资源数量获取
		//如果加载的是纤芯成端、跳纤或尾纤、熔纤接续类型，特殊加载其数量
		if("纤芯成端".equals(this.searchType) 
				|| "熔纤接续".equals(this.searchType) 
				|| "PigtailedFiber".equals(this.searchType)) {
			
			int fiberCoreAndTerminalCount = 0; //纤芯成端数量
			int pigtailedFiberCount = 0; //跳纤或尾纤数量
			int fiberCoreAndFiberCoreCount = 0; //熔纤接续数量
			
			if("ODF".equals(this.parentEntityType) || "FiberDistributionCabinet".equals(this.parentEntityType) 
					|| "FiberCrossCabinet".equals(this.parentEntityType) 
					|| "FiberTerminalCase".equals(this.parentEntityType)) {
				
				//因为两个端子连着一个跳纤与尾纤，所以跳纤与尾纤会出现重复的情况，需要进行过滤
				Map<String, Object> pigtailedFiberAppEntityArrsFilterMap = new HashMap<String, Object>();
				
				//获取父级entity下的端子或者父级entity下的ODM的端子
				//父类型为光分纤箱或者终端盒时，不需要ODM
				if("FiberDistributionCabinet".equals(this.parentEntityType) || "FiberTerminalCase".equals(this.parentEntityType)) {
					//获取父级entity下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的纤芯
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								//一个端子只对应一个纤芯
								ApplicationEntity fiberCoreAppEntity = fiberCoreAppEntityArrs[0];
								//获取纤芯所关联的光缆段
								ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
								if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
									fiberCoreAndTerminalCount++;
								}
							}
							
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
									//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
									pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
								}
							}
						}
					}
				} else {
					//当父类型为光交接箱和ODF时，需要带ODM
					ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
					if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
						for(ApplicationEntity odmApp: odmAppEntityArrs) {
							//获取ODM下的端子
							ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
							if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
								for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
									//获取与端子关联的纤芯
									ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
									if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
										//一个端子只对应一个纤芯
										for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
											//获取纤芯所关联的光缆段
											ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
											if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
												fiberCoreAndTerminalCount++;
											}
										}
									}
									
									//获取与端子关联的跳纤与尾纤
									ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
									if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
										for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
											//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
											pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
										}
									}
								}
							}
						}
					}
				}
				
				if(pigtailedFiberAppEntityArrsFilterMap != null && pigtailedFiberAppEntityArrsFilterMap.size() > 0) {
					pigtailedFiberCount = pigtailedFiberAppEntityArrsFilterMap.size();
				}
			} else if("Joint".equals(this.parentEntityType)) {
				//获取接头连接的光缆段
				ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
				if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
					//此map用来过滤重复数据
					List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
					for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
						//查出光缆段连接的纤芯
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							//查出纤芯所连接的纤芯
							for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
								ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
								if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
									for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
										//查出连接纤芯所属的光缆段
										ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
										if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
											//一条纤芯只属于一个光缆段
											ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
											//获取纤芯所连接的纤芯是否属于本接头的光缆段
											
											boolean isTheSameFiberSection = false;
											for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
												//纤芯所连接的纤芯不属于该接头的光缆段的情况
												if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
													isTheSameFiberSection = true;
												}
											}
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(!isTheSameFiberSection) {
												continue;
											}
								
											//进行重复数据的过滤操作
											Map<String ,Object> targetMap = new HashMap<String, Object>();
											targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
											targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
											targetMapList.add(targetMap);
										}
									}
								}
							}
							
							
						}
					}
					for(Map<String, Object> targetMap : targetMapList) {
						for(Map<String, Object> targetOppositeMap : targetMapList) {
							String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
							String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
							if(targetMapStr.equals(targetOppositeMapStr)) {
								targetMap.remove("leftId");
								targetMap.remove("rightId");
							}
						}
					}
					
					for(Map<String, Object> targetMap : targetMapList) {
						if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
							//System.out.println(targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							fiberCoreAndFiberCoreCount++;//累加熔纤接续数量
						}
					}
				}
			}
			
			//数量为0的情况
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			int count = 0;
			//纤芯成端、跳纤或尾纤、熔纤接续的数量获取
			if("纤芯成端".equals(this.searchType)) {
				count = fiberCoreAndTerminalCount;
			} else if ("PigtailedFiber".equals(this.searchType)) {
				count = pigtailedFiberCount;
			} else if("熔纤接续".equals(this.searchType)) {
				count = fiberCoreAndFiberCoreCount;
			}
			String result = gson.toJson(count);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int count = 0;
			String[] aetNameOfAetg = structureCommonService.getAetNameOfAetg(this.searchType, "networkresourcemanage");
			if(aetNameOfAetg == null || aetNameOfAetg.length <= 0){
				aetNameOfAetg = new String[]{this.searchType};
			}
			//非特殊的物理资源数量获取
			//根据父entity和当前要查找的子节点的类型，进行子节点的获取
			List<ApplicationEntity> childAppArrs = new ArrayList<ApplicationEntity>();
			if(aetNameOfAetg != null && aetNameOfAetg.length > 0){
				for(String se:aetNameOfAetg){	
					ApplicationEntity[] childAppA = structureCommonService.getStrutureSelationsApplicationEntity(
							physicalresAppEntity, this.searchType, AssociatedType.CHILD, "networkresourcemanage");
					
					if(childAppA != null && childAppA.length > 0){
						List<ApplicationEntity> asList = Arrays.asList(childAppA);
						childAppArrs.addAll(asList);
						//System.out.println(childAppArrs.size());
					}
				}
			}
			
			if(childAppArrs != null && childAppArrs.size() > 0) {
				//获取子资源数量(child)
				count = childAppArrs.size();
			}
			
			if(this.addLink != null) {
				//如果addLink不为空，获取子资源数量(link)
				ApplicationEntity[] linkAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.LINK, "networkresourcemanage");
				
				if(linkAppArrs != null && linkAppArrs.length > 0) {
					count += linkAppArrs.length;
				}
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(count);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据某一个类型获取某个节点的子节点
	 */
	public void getChildEntityByTypeAction() {
		long parentEntityId = Long.parseLong(this.parentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.parentEntityType, parentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		//根据父entity和当前要查找的子节点的类型，进行子节点的获取
		ApplicationEntity[] appArrs = null;
		
		if(this.linkType != null) {
			//需要根据child或者link获取节点的情况
			if("child".equals(this.linkType)) {
				appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.CHILD, "networkresourcemanage");
			} else if("link".equals(this.linkType)) {
				appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.LINK, "networkresourcemanage");
			}
		} else {
			//无linkType标识，直接获取child节点
			appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
					physicalresAppEntity, this.searchType, AssociatedType.CHILD, "networkresourcemanage");
		}
		
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		
		//如果是从前台传过来的类型为纤芯成端，进行另外的加载处理
		if("纤芯成端".equals(this.searchType)) {
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
			//存在ODM的情况
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						//获取与端子关联的纤芯
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										
										//获取父级entity下的光缆段
										/*ApplicationEntity[] fiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
										boolean isTheSameFiberSection = false;
										if(fiberSectionAppArrs != null && fiberSectionAppArrs.length > 0) {
											for (ApplicationEntity fiberSectionApp : fiberSectionAppArrs) {
												if(fiberSectionAppEntity.getValue("id").equals(fiberSectionApp.getValue("id"))) {
													isTheSameFiberSection = true;
												}
											}
										}
										
										if(!isTheSameFiberSection) {
											continue;
										}*/
										
										
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" +terminalApp.getType());
										lists.add(extraMap);
									}
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					//获取与端子关联的纤芯
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" + terminalApp.getType());
										lists.add(extraMap);
									}
								}
							}
						}
					}
				}
			}
		} else if("PigtailedFiber".equals(this.searchType)) {
			//如果是从前台传过来的类型为跳纤与尾纤，进行另外的加载处理
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
			//存在ODM的情况
			Set<Map<String, Object>> filterSet = new HashSet<Map<String, Object>>();//通过Set对端子连接的同一跳纤与尾纤进行过滤
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								
								for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
									Map<String, Object> extraMap = new HashMap<String, Object>();
									
									extraMap.put("id", pigtailedFiberApp.getValue("id"));
									extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
									extraMap.put("type", pigtailedFiberApp.getType());
									filterSet.add(extraMap);
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						//获取与端子关联的跳纤与尾纤
						ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
						if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
							
							for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
								Map<String, Object> extraMap = new HashMap<String, Object>();
								extraMap.put("id", pigtailedFiberApp.getValue("id"));
								extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
								extraMap.put("type", pigtailedFiberApp.getType());
								filterSet.add(extraMap);
							}
						}
					}
				}
			}
			//通过Set过滤重复的跳纤与尾纤数据，再放进list里面
			for (Map<String, Object> map : filterSet) {
				lists.add(map);
			}
		} else if("熔纤接续".equals(this.searchType)) {
			//获取接头连接的光缆段
			ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
			if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
				//此map用来过滤重复数据
				List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
				for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
					
					//System.out.println("接头关联的光缆段：" + fiberSectionApp.getValue("id"));
					
					
					//查出光缆段连接的纤芯
					ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
					if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
						//查出纤芯所连接的纤芯
						for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
							ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
									//查出连接纤芯所属的光缆段
									ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
									if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
										//获取纤芯所连接的纤芯是否属于本接头的光缆段
										
										//System.out.println("纤芯所属的光缆段：" + fiberSectionLinkAppEntity.getValue("id"));
										
										boolean isTheSameFiberSection = false;
										for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
												isTheSameFiberSection = true;
											}
										}
										//纤芯所连接的纤芯不属于该接头的光缆段的情况
										if(!isTheSameFiberSection) {
											continue;
										}
							
										//进行重复数据的过滤操作
										Map<String ,Object> targetMap = new HashMap<String, Object>();
										targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
										targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
										targetMapList.add(targetMap);
									}
								}
							}
						}
						
						
					}
				}
				for(Map<String, Object> targetMap : targetMapList) {
					for(Map<String, Object> targetOppositeMap : targetMapList) {
						String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
						String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
						if(targetMapStr.equals(targetOppositeMapStr)) {
							targetMap.remove("leftId");
							targetMap.remove("rightId");
						}
					}
				}
				
				for(Map<String, Object> targetMap : targetMapList) {
					if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
						BasicEntity leftFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("leftId").toString()));
						BasicEntity rightFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("rightId").toString()));
						ApplicationEntity leftFiberCoreAppEntity = ApplicationEntity.changeFromEntity(leftFiberCoreEntity);
						ApplicationEntity rightFiberCoreAppEntity = ApplicationEntity.changeFromEntity(rightFiberCoreEntity);
						//获取纤芯所属的光缆段
						ApplicationEntity[] leftFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(leftFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
						ApplicationEntity[] rightFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(rightFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
						if(leftFiberSectionLinkAppEntityArrs != null && leftFiberSectionLinkAppEntityArrs.length > 0) {
							//一条纤芯只属于一条光缆段
							ApplicationEntity leftFiberSectionLinkAppEntity = leftFiberSectionLinkAppEntityArrs[0];
							ApplicationEntity rightFiberSectionLinkAppEntity = rightFiberSectionLinkAppEntityArrs[0];
							String leftEntityName = leftFiberSectionLinkAppEntity.getValue("name") + "/" + leftFiberCoreAppEntity.getValue("label");
							String rightEntityName = rightFiberSectionLinkAppEntity.getValue("name") + "/" + rightFiberCoreAppEntity.getValue("label");
							
							Map<String ,Object> extraMap = new HashMap<String, Object>();
							//拼接两个纤芯的id，以备日后与光缆段解除关系使用
							extraMap.put("id", targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							extraMap.put("entityName", leftEntityName + " - " + rightEntityName);
							//获取纤芯类型
							extraMap.put("type", leftFiberCoreAppEntity.getType() + "-" + rightFiberCoreAppEntity.getType());
							lists.add(extraMap);
						}
					}
				}
			} 
		} else {
			if(appArrs != null && appArrs.length > 0) {
				for (ApplicationEntity app : appArrs) {
					//顺带把所获得的app实例的子级类型求出来
					String[] typeArrs = null;
					if(this.viewName != null) {
						//需要指定视图的情况
						typeArrs = structureCommonService.getStrutureSelationsArray(app, AssociatedType.CHILD, this.viewName, "networkresourcemanage");
					} else {
						typeArrs = structureCommonService.getStrutureSelationsArray(app, AssociatedType.CHILD, "networkresourcemanage");
					}
					String hasType = "";
					if(typeArrs != null && typeArrs.length > 0) {
						hasType = "has"; //存在子级类型
					} else {
						hasType = "no"; //不存在子级类型
					}
					Map<String, Object> map = app.toMap();
					map.put("hasType", hasType);
					lists.add(map);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		quickSort.sort(lists,"name");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(lists);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取隶属资源的指定类型的子资源
	 */
	public void getChooseResAction() {
		long chooseResEntityId = Long.parseLong(this.chooseResEntityId);
		BasicEntity chooseResEntity = physicalresService.getPhysicalresById(this.chooseResEntityType, chooseResEntityId);
		ApplicationEntity chooseResAppEntity = ApplicationEntity.changeFromEntity(chooseResEntity);
		ApplicationEntity[] chooseResChildArrs = structureCommonService.getStrutureSelationsApplicationEntity(chooseResAppEntity,this.needChildEntityType,AssociatedType.CHILD,"networkresourcemanage");
		if(chooseResChildArrs != null && chooseResChildArrs.length > 0) {
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for(ApplicationEntity app : chooseResChildArrs) {
				Map<String, Object> map = app.toMap();
				map.put("type", this.needChildEntityType);
				lists.add(map);
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(lists);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取一个节点的子节点的类型(同时获取该类型下的节点数量)
	 */
	public void getTypeAction() {
		    String[] chooseResEntityTypes = ResourceCommon.getEntityGroupString(this.chooseResEntityType);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(chooseResEntityTypes);
			try {
				ServletActionContext.getResponse().getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	/**
	 * 获取指定某一entity的所有子entity
	 */
	public void getCurrentTypeChildMsgAction() {
		long chooseResEntityId = Long.parseLong(this.chooseResEntityId);
		BasicEntity chooseResEntity = physicalresService.getPhysicalresById(this.chooseResEntityType, chooseResEntityId);
		ApplicationEntity chooseResAppEntity = ApplicationEntity.changeFromEntity(chooseResEntity);
		ApplicationEntity[] chooseResChildArrs = structureCommonService.getStrutureSelationsApplicationEntity(chooseResAppEntity,AssociatedType.CHILD,"networkresourcemanage");
		if(chooseResChildArrs != null && chooseResChildArrs.length > 0) {
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for(ApplicationEntity app : chooseResChildArrs) {
				Map<String, Object> map = app.toMap();
				map.put("type", this.needChildEntityType);
				lists.add(map);
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(lists);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通过父类型获取单向link类型信息
	 */
	public void getAssociatedAetNameByTypeAction() {
		String[] childTypeArrs =  ResourceCommon.getlogicalresEntityClan(this.loadedType);
		List<Map<String, Object>> childTypeMapList = new ArrayList<Map<String,Object>>();
		if(childTypeArrs != null && childTypeArrs.length > 0) {
			for (String str : childTypeArrs) {
				Map<String ,Object> childTypeMap = new HashMap<String, Object>();
				try {
					//数据字典，中英文转换
					List<BasicEntity> entry = dictionary.getEntry(str + ",networkResourceDefination", SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						childTypeMap.put("type", str);
						childTypeMap.put("chineseType", entry.get(0).getValue("display"));
						childTypeMapList.add(childTypeMap);
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(childTypeMapList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建或删除关系
	 */
	public void createRelationAction() {
		/*BasicEntity leftEntity = physicalresService.getPhysicalresById("FiberSection", 24);
		BasicEntity rightEntity = physicalresService.getPhysicalresById("FiberCore", 24);
		ApplicationEntity leftAppEntity = ApplicationEntity.changeFromEntity(leftEntity);
		ApplicationEntity rightAppEntity = ApplicationEntity.changeFromEntity(rightEntity);
		structureCommonService.createAssociatedRelation(leftAppEntity, rightAppEntity, AssociatedType.LINK, "networkresourcemanage");
		structureCommonService.delStrutureAssociation(leftAppEntity, rightAppEntity, AssociatedType.LINK, "networkresourcemanage");*/
		/*ApplicationEntity app = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById("Terminal", 66));
		ApplicationEntity[] appArrs = structureCommonService.getStrutureSelationsApplicationEntity(app, AssociatedType.PARENT, "networkresourcemanage");
		for (ApplicationEntity ap : appArrs) {
			System.out.println(ap.getType() + ap.getValue("id"));
		}*/
		
		BasicEntity rightEntity = physicalresService.getPhysicalresById("Sys_Area", 22);
		ApplicationEntity rightAppEntity = ApplicationEntity.changeFromEntity(rightEntity);
		ApplicationEntity[] arrs = structureCommonService.getStrutureSelationsApplicationEntity(rightAppEntity, AssociatedType.CHILD, "networkresourcemanage");
		//System.out.println(arrs.length + "===================");
		/*ApplicationEntity[] grandpaAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(rightAppEntity, AssociatedType.PARENT, "networkresourcemanage");
		System.out.println(grandpaAppEntityArrs);*/
		//ApplicationEntity[] resultArrs = structureCommonService.getAppArrsByRecursionForSrc(rightAppEntity, "Terminal", "networkresourcemanage");
		/*ApplicationEntity[] resultArrs = structureCommonService.getAppArrsByRecursion(
				 rightAppEntity,new String[]{"ODF","ODM","Terminal"},AssociatedType.CHILD, "networkresourcemanage");*/
		/*if(resultArrs != null && resultArrs.length > 0) {
			for (ApplicationEntity app : resultArrs) {
				System.out.println(app.getValue("name"));
			}
		}*/
		
		/*String[] typeArrs = structureCommonService.getAssociatedAetName("Terminal", AssociatedType.PARENT, "networkresourcemanage");
		if(typeArrs != null && typeArrs.length > 0) {
			for (String type : typeArrs) {
				System.out.println(type);
			}
		}*/
		
	}
	
	public String loadTreeAction() {
		return "success";
	}
	
	
	/**
	 * 根据某一个类型获取某个节点的LINK节点
	 */
	public void getLinkEntityByTypeAction() {
		long parentEntityId = Long.parseLong(this.parentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.parentEntityType, parentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		//根据父entity和当前要查找的子节点的类型，进行子节点的获取
		ApplicationEntity[] appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
				physicalresAppEntity, this.searchType, AssociatedType.LINK, "networkresourcemanage");
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		
		for (ApplicationEntity app : appArrs) {
			//顺带把所获得的app实例的子级类型求出来
			String[] typeArrs = structureCommonService.getStrutureSelationsArray(app, AssociatedType.LINK, "networkresourcemanage");
			String hasType = "";
			if(typeArrs != null && typeArrs.length > 0) {
				hasType = "has"; //存在子级类型
			} else {
				hasType = "no"; //不存在子级类型
			}
			Map<String, Object> map = app.toMap();
			map.put("hasType", hasType);
			lists.add(map);
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(lists);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据EntityGroup获取包含EntityType
	 */
	public void getChildTypeByTypeAction(){
		String[] childs = new String[]{this.currentEntityType,"Sys_Area"};
		Map<String, Object> infoLikeMapChinese =  new HashMap<String, Object>();
		for(String st:childs){
			List<BasicEntity> entry = null;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					infoLikeMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				e.printStackTrace();
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("childs", childs);
		result.put("infoLikeMapChinese", infoLikeMapChinese);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String results = gson.toJson(result);
		try {
			response.getWriter().write(results);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 */
	public void getChildTypeByEntityAction(){
		if(this.parentEntityId != null && !"".equals(this.parentEntityId)){
			ApplicationEntity applicationEntity = structureCommonService.getSectionEntity(this.parentEntityType, this.parentEntityId);
			List<ApplicationEntity> applicationEntitiesList = new ArrayList<ApplicationEntity>();
				String[] cTypes = new String[]{this.currentEntityType};
				ApplicationEntity[] applicationEntities = structureCommonService.getAppArrsByRecursion(applicationEntity,cTypes,AssociatedType.LINK,"networkresourcemanage");
				if(applicationEntities != null && applicationEntities.length > 0){
					applicationEntitiesList.addAll(Arrays.asList(applicationEntities));
				}
				ApplicationEntity[] applicationEntities2 = structureCommonService.getAppArrsByRecursionForSrc(applicationEntity,this.currentEntityType,"networkresourcemanage");	
				if(applicationEntities2 != null && applicationEntities2.length > 0){
					applicationEntitiesList.addAll(Arrays.asList(applicationEntities2));
				}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for(ApplicationEntity entity:applicationEntitiesList){
				Map<String,Object> map = new HashMap<String, Object>();
				map = entity.toMap();
				list.add(map);
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			quickSort.sort(list, "name");
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	
	/**
	 * 获取一个节点的子节点的类型(同时获取该类型下的节点数量)(通用)
	 */
	public void getTypeByEntityUniversalAction() {
		long currentEntityId = Long.parseLong(this.currentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		String[] addInputGroupString = null;
		if(physicalresAppEntity != null&&!"null".equals(physicalresAppEntity)&&!"".equals(physicalresAppEntity)) {
			List<Map<String, Object>> lists = null;
			if(this.addInputType != null&&!"null".equals(addInputType)&&!"".equals(addInputType)){
				addInputGroupString = ResourceCommon.getEntityGroupString(searchType);
			}
			if(this.searchType != null&&!"null".equals(searchType)&&!"".equals(searchType)){
				String[] entityGroupString = ResourceCommon.getEntityGroupString(searchType);
				if(ResourceCommon.isLogicalresEntity(this.currentEntityType)){
					String[] cTypes = new String[]{this.currentEntityType};
					List<Map<String, Object>> linkLists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity,entityGroupString, AssociatedType.LINK, this.addLink, this.viewName, this.myStructure);
					lists.addAll(linkLists);
				}else{
					lists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity,entityGroupString, AssociatedType.CHILD, this.addLink, this.viewName, this.myStructure);
				}
				//获取父级节点下的类型
				//判断是否需要获取link的类型
				if(this.addLink != null&&!"null".equals(addLink)&&!"".equals(addLink)) {
					//child的list和link的list加起来
				}
			}else{
				//获取父级节点下的类型
				lists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity, AssociatedType.CHILD, this.addLink, this.viewName, this.myStructure);
				//判断是否需要获取link的类型
				if(this.addLink != null&&!"null".equals(addLink)&&!"".equals(addLink)) {
					List<Map<String, Object>> linkLists = structureCommonService.getStrutureSelationsTypeMap(physicalresAppEntity, AssociatedType.LINK, this.addLink, this.viewName, this.myStructure);
					//child的list和link的list加起来
					lists.addAll(linkLists);
				}
			}
			if(lists != null && !lists.isEmpty()) {
				for (Map<String, Object> typeMap : lists) {
					try {
						//数据字典，中英文转换
						List<BasicEntity> entry = dictionary.getEntry(typeMap.get("type") + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							
							//System.out.println(typeMap.get("type"));
							//System.out.println(entry.get(0).getValue("display"));
							
							
							typeMap.put("chineseType", entry.get(0).getValue("display"));
						}
					} catch (EntryOperationException e) {
						e.printStackTrace();
						//没有中文，继续显示英文
						typeMap.put("chineseType", typeMap.get("type"));
					}
				}
			}
			
			//如果是ODF，额外增加纤芯成端类型节点
			if("ODF".equals(this.currentEntityType) || "FiberDistributionCabinet".equals(this.currentEntityType) 
					|| "FiberCrossCabinet".equals(this.currentEntityType) 
					|| "FiberTerminalCase".equals(this.currentEntityType)) {
				Map<String, Object> extraPhysicalresMap = new HashMap<String, Object>();
				extraPhysicalresMap.put("type", "纤芯成端");
				extraPhysicalresMap.put("chineseType", "纤芯成端");
				int extraPhysicalresCount = 0; //纤芯成端节点的个数
				
				//加载跳纤与尾纤
				Map<String, Object> extraPhysicalresPigtailedFiberMap = new HashMap<String, Object>();
				extraPhysicalresPigtailedFiberMap.put("type", "PigtailedFiber");
				
				try {
					//数据字典，中英文转换(转换跳纤或尾纤的中文)
					List<BasicEntity> entry = dictionary.getEntry("PigtailedFiber,networkResourceDefination", SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						extraPhysicalresPigtailedFiberMap.put("chineseType", entry.get(0).getValue("display"));
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
				}
				
				int extraPhysicalresPigtailedCount = 0; //跳纤与尾纤的节点个数
				
				//因为两个端子连着一个跳纤与尾纤，所以跳纤与尾纤会出现重复的情况，需要进行过滤
				Map<String, Object> pigtailedFiberAppEntityArrsFilterMap = new HashMap<String, Object>();
				
				//获取父级entity下的端子或者父级entity下的ODM的端子
				//父类型为光分纤箱或者终端盒时，不需要ODM
				if("FiberDistributionCabinet".equals(this.currentEntityType) || "FiberTerminalCase".equals(this.currentEntityType)) {
					//获取父级entity下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, this.myStructure);
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的纤芯
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								//一个端子只对应一个纤芯
								ApplicationEntity fiberCoreAppEntity = fiberCoreAppEntityArrs[0];
								//获取纤芯所关联的光缆段
								ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
								if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
									extraPhysicalresCount++;
								}
							}
							
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
									//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
									pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
								}
							}
						}
					}
				} else {
					//当父类型为光交接箱和ODF时，需要带ODM
					ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, this.myStructure);
					if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
						for(ApplicationEntity odmApp: odmAppEntityArrs) {
							//获取ODM下的端子
							ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, this.myStructure);
							if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
								for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
									//获取与端子关联的纤芯
									ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
									if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
										//一个端子只对应一个纤芯
										for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
											//获取纤芯所关联的光缆段
											ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
											if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
												extraPhysicalresCount++;
											}
										}
									}
									
									//获取与端子关联的跳纤与尾纤
									ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
									if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
										for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
											//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
											pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
										}
									}
								}
							}
						}
					}
				}
				
				extraPhysicalresMap.put("count", extraPhysicalresCount);//获取纤芯个数
				lists.add(extraPhysicalresMap);
				
				if(pigtailedFiberAppEntityArrsFilterMap != null && pigtailedFiberAppEntityArrsFilterMap.size() > 0) {
					extraPhysicalresPigtailedCount = pigtailedFiberAppEntityArrsFilterMap.size();
				}
				extraPhysicalresPigtailedFiberMap.put("count", extraPhysicalresPigtailedCount);//获取跳纤与尾纤个数
				lists.add(extraPhysicalresPigtailedFiberMap);
			} else if("Joint".equals(this.currentEntityType)) {
				Map<String, Object> extraPhysicalresMap = new HashMap<String, Object>();
				extraPhysicalresMap.put("type", "熔纤接续");
				extraPhysicalresMap.put("chineseType", "熔纤接续");
				int extraPhysicalresCount = 0; //纤芯成端节点的个数
				
				//获取接头连接的光缆段
				ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
				if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
					//此map用来过滤重复数据
					List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
					for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
						//查出光缆段连接的纤芯
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, this.myStructure);
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							//查出纤芯所连接的纤芯
							for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
								ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, this.myStructure);
								if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
									for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
										//查出连接纤芯所属的光缆段
										ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, this.myStructure);
										if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
											//一条纤芯只属于一个光缆段
											ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
											//获取纤芯所连接的纤芯是否属于本接头的光缆段
											
											boolean isTheSameFiberSection = false;
											for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
												//纤芯所连接的纤芯不属于该接头的光缆段的情况
												if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
													isTheSameFiberSection = true;
												}
											}
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(!isTheSameFiberSection) {
												continue;
											}
								
											//进行重复数据的过滤操作
											Map<String ,Object> targetMap = new HashMap<String, Object>();
											targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
											targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
											targetMapList.add(targetMap);
										}
									}
								}
							}
							
							
						}
					}
					for(Map<String, Object> targetMap : targetMapList) {
						for(Map<String, Object> targetOppositeMap : targetMapList) {
							String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
							String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
							if(targetMapStr.equals(targetOppositeMapStr)) {
								targetMap.remove("leftId");
								targetMap.remove("rightId");
							}
						}
					}
					
					for(Map<String, Object> targetMap : targetMapList) {
						if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
							//System.out.println(targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							extraPhysicalresCount++;//累加熔纤接续数量
						}
					}
				}
				extraPhysicalresMap.put("count", extraPhysicalresCount);
				lists.add(extraPhysicalresMap);
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("addInputGroupString", addInputGroupString);
			resultMap.put("lists", lists);
			String result = gson.toJson(resultMap);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 获取某一节点下，某类型的子节点的个数(通用)
	 */
	public void getChildEntityCountByTypeUniversalAction() {
		long parentEntityId = Long.parseLong(this.parentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.parentEntityType, parentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		//特殊的物理资源数量获取
		//如果加载的是纤芯成端、跳纤或尾纤、熔纤接续类型，特殊加载其数量
		if("纤芯成端".equals(this.searchType) 
				|| "熔纤接续".equals(this.searchType) 
				|| "PigtailedFiber".equals(this.searchType)) {
			
			int fiberCoreAndTerminalCount = 0; //纤芯成端数量
			int pigtailedFiberCount = 0; //跳纤或尾纤数量
			int fiberCoreAndFiberCoreCount = 0; //熔纤接续数量
			
			if("ODF".equals(this.parentEntityType) || "FiberDistributionCabinet".equals(this.parentEntityType) 
					|| "FiberCrossCabinet".equals(this.parentEntityType) 
					|| "FiberTerminalCase".equals(this.parentEntityType)) {
				
				//因为两个端子连着一个跳纤与尾纤，所以跳纤与尾纤会出现重复的情况，需要进行过滤
				Map<String, Object> pigtailedFiberAppEntityArrsFilterMap = new HashMap<String, Object>();
				
				//获取父级entity下的端子或者父级entity下的ODM的端子
				//父类型为光分纤箱或者终端盒时，不需要ODM
				if("FiberDistributionCabinet".equals(this.parentEntityType) || "FiberTerminalCase".equals(this.parentEntityType)) {
					//获取父级entity下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, this.myStructure);
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的纤芯
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								//一个端子只对应一个纤芯
								ApplicationEntity fiberCoreAppEntity = fiberCoreAppEntityArrs[0];
								//获取纤芯所关联的光缆段
								ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
								if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
									fiberCoreAndTerminalCount++;
								}
							}
							
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
									//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
									pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
								}
							}
						}
					}
				} else {
					//当父类型为光交接箱和ODF时，需要带ODM
					ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, this.myStructure);
					if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
						for(ApplicationEntity odmApp: odmAppEntityArrs) {
							//获取ODM下的端子
							ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, this.myStructure);
							if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
								for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
									//获取与端子关联的纤芯
									ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
									if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
										//一个端子只对应一个纤芯
										for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
											//获取纤芯所关联的光缆段
											ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
											if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
												fiberCoreAndTerminalCount++;
											}
										}
									}
									
									//获取与端子关联的跳纤与尾纤
									ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
									if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
										for(ApplicationEntity app : pigtailedFiberAppEntityArrs) {
											//通过map，进行跳纤与尾纤个数的过滤，同一id，重复put，过滤重复的跳纤与尾纤
											pigtailedFiberAppEntityArrsFilterMap.put(app.getValue("id").toString(), app.getValue("id").toString());
										}
									}
								}
							}
						}
					}
				}
				
				if(pigtailedFiberAppEntityArrsFilterMap != null && pigtailedFiberAppEntityArrsFilterMap.size() > 0) {
					pigtailedFiberCount = pigtailedFiberAppEntityArrsFilterMap.size();
				}
			} else if("Joint".equals(this.parentEntityType)) {
				//获取接头连接的光缆段
				ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
				if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
					//此map用来过滤重复数据
					List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
					for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
						//查出光缆段连接的纤芯
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, this.myStructure);
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							//查出纤芯所连接的纤芯
							for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
								ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, this.myStructure);
								if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
									for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
										//查出连接纤芯所属的光缆段
										ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, this.myStructure);
										if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
											//一条纤芯只属于一个光缆段
											ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
											//获取纤芯所连接的纤芯是否属于本接头的光缆段
											
											boolean isTheSameFiberSection = false;
											for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
												//纤芯所连接的纤芯不属于该接头的光缆段的情况
												if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
													isTheSameFiberSection = true;
												}
											}
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(!isTheSameFiberSection) {
												continue;
											}
								
											//进行重复数据的过滤操作
											Map<String ,Object> targetMap = new HashMap<String, Object>();
											targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
											targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
											targetMapList.add(targetMap);
										}
									}
								}
							}
							
							
						}
					}
					for(Map<String, Object> targetMap : targetMapList) {
						for(Map<String, Object> targetOppositeMap : targetMapList) {
							String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
							String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
							if(targetMapStr.equals(targetOppositeMapStr)) {
								targetMap.remove("leftId");
								targetMap.remove("rightId");
							}
						}
					}
					
					for(Map<String, Object> targetMap : targetMapList) {
						if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
							//System.out.println(targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							fiberCoreAndFiberCoreCount++;//累加熔纤接续数量
						}
					}
				}
			}
			
			//数量为0的情况
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			int count = 0;
			//纤芯成端、跳纤或尾纤、熔纤接续的数量获取
			if("纤芯成端".equals(this.searchType)) {
				count = fiberCoreAndTerminalCount;
			} else if ("PigtailedFiber".equals(this.searchType)) {
				count = pigtailedFiberCount;
			} else if("熔纤接续".equals(this.searchType)) {
				count = fiberCoreAndFiberCoreCount;
			}
			String result = gson.toJson(count);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int count = 0;
			
			//非特殊的物理资源数量获取
			//根据父entity和当前要查找的子节点的类型，进行子节点的获取
			ApplicationEntity[] childAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(
					physicalresAppEntity, this.searchType, AssociatedType.CHILD, this.myStructure);
			
			if(childAppArrs != null && childAppArrs.length > 0) {
				//获取子资源数量(child)
				count = childAppArrs.length;
			}
			
			if(this.addLink != null) {
				//如果addLink不为空，获取子资源数量(link)
				ApplicationEntity[] linkAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.LINK, this.myStructure);
				
				if(linkAppArrs != null && linkAppArrs.length > 0) {
					count += linkAppArrs.length;
				}
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(count);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据某一个类型获取某个节点的子节点(通用)
	 */
	public void getChildEntityByTypeUniversalAction() {
		long parentEntityId = Long.parseLong(this.parentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.parentEntityType, parentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		//根据父entity和当前要查找的子节点的类型，进行子节点的获取
		ApplicationEntity[] appArrs = null;
		
		if(this.linkType != null) {
			//需要根据child或者link获取节点的情况
			if("child".equals(this.linkType)) {
				appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.CHILD, this.myStructure);
			} else if("link".equals(this.linkType)) {
				appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.LINK, this.myStructure);
			}
		} else {
			//无linkType标识，直接获取child节点
			appArrs = structureCommonService.getStrutureSelationsApplicationEntity(
					physicalresAppEntity, this.searchType, AssociatedType.CHILD, this.myStructure);
		}
		
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		
		//如果是从前台传过来的类型为纤芯成端，进行另外的加载处理
		if("纤芯成端".equals(this.searchType)) {
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, this.myStructure);
			//存在ODM的情况
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, this.myStructure);
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						//获取与端子关联的纤芯
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										
										//获取父级entity下的光缆段
										/*ApplicationEntity[] fiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
										boolean isTheSameFiberSection = false;
										if(fiberSectionAppArrs != null && fiberSectionAppArrs.length > 0) {
											for (ApplicationEntity fiberSectionApp : fiberSectionAppArrs) {
												if(fiberSectionAppEntity.getValue("id").equals(fiberSectionApp.getValue("id"))) {
													isTheSameFiberSection = true;
												}
											}
										}
										
										if(!isTheSameFiberSection) {
											continue;
										}*/
										
										
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" +terminalApp.getType());
										lists.add(extraMap);
									}
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, this.myStructure);
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					//获取与端子关联的纤芯
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, this.myStructure);
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" + terminalApp.getType());
										lists.add(extraMap);
									}
								}
							}
						}
					}
				}
			}
		} else if("PigtailedFiber".equals(this.searchType)) {
			//如果是从前台传过来的类型为跳纤与尾纤，进行另外的加载处理
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "ODM", AssociatedType.CHILD, this.myStructure);
			//存在ODM的情况
			Set<Map<String, Object>> filterSet = new HashSet<Map<String, Object>>();//通过Set对端子连接的同一跳纤与尾纤进行过滤
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, this.myStructure);
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的跳纤与尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								
								for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
									Map<String, Object> extraMap = new HashMap<String, Object>();
									
									extraMap.put("id", pigtailedFiberApp.getValue("id"));
									extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
									extraMap.put("type", pigtailedFiberApp.getType());
									filterSet.add(extraMap);
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "Terminal", AssociatedType.CHILD, this.myStructure);
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						//获取与端子关联的跳纤与尾纤
						ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, this.myStructure);
						if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
							
							for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
								Map<String, Object> extraMap = new HashMap<String, Object>();
								extraMap.put("id", pigtailedFiberApp.getValue("id"));
								extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
								extraMap.put("type", pigtailedFiberApp.getType());
								filterSet.add(extraMap);
							}
						}
					}
				}
			}
			//通过Set过滤重复的跳纤与尾纤数据，再放进list里面
			for (Map<String, Object> map : filterSet) {
				lists.add(map);
			}
		} else if("熔纤接续".equals(this.searchType)) {
			//获取接头连接的光缆段
			ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(physicalresAppEntity, "FiberSection", AssociatedType.LINK, this.myStructure);
			if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0) {
				//此map用来过滤重复数据
				List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
				for (ApplicationEntity fiberSectionApp : fiberSectionAppEntityArrs) {
					
					//System.out.println("接头关联的光缆段：" + fiberSectionApp.getValue("id"));
					
					
					//查出光缆段连接的纤芯
					ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, this.myStructure);
					if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
						//查出纤芯所连接的纤芯
						for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
							ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, this.myStructure);
							if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
									//查出连接纤芯所属的光缆段
									ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, this.myStructure);
									if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
										//获取纤芯所连接的纤芯是否属于本接头的光缆段
										
										//System.out.println("纤芯所属的光缆段：" + fiberSectionLinkAppEntity.getValue("id"));
										
										boolean isTheSameFiberSection = false;
										for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppEntityArrs) {
											//纤芯所连接的纤芯不属于该接头的光缆段的情况
											if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
												isTheSameFiberSection = true;
											}
										}
										//纤芯所连接的纤芯不属于该接头的光缆段的情况
										if(!isTheSameFiberSection) {
											continue;
										}
							
										//进行重复数据的过滤操作
										Map<String ,Object> targetMap = new HashMap<String, Object>();
										targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
										targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
										targetMapList.add(targetMap);
									}
								}
							}
						}
						
						
					}
				}
				for(Map<String, Object> targetMap : targetMapList) {
					for(Map<String, Object> targetOppositeMap : targetMapList) {
						String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
						String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
						if(targetMapStr.equals(targetOppositeMapStr)) {
							targetMap.remove("leftId");
							targetMap.remove("rightId");
						}
					}
				}
				
				for(Map<String, Object> targetMap : targetMapList) {
					if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
						BasicEntity leftFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("leftId").toString()));
						BasicEntity rightFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("rightId").toString()));
						ApplicationEntity leftFiberCoreAppEntity = ApplicationEntity.changeFromEntity(leftFiberCoreEntity);
						ApplicationEntity rightFiberCoreAppEntity = ApplicationEntity.changeFromEntity(rightFiberCoreEntity);
						//获取纤芯所属的光缆段
						ApplicationEntity[] leftFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(leftFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, this.myStructure);
						ApplicationEntity[] rightFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(rightFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, this.myStructure);
						if(leftFiberSectionLinkAppEntityArrs != null && leftFiberSectionLinkAppEntityArrs.length > 0) {
							//一条纤芯只属于一条光缆段
							ApplicationEntity leftFiberSectionLinkAppEntity = leftFiberSectionLinkAppEntityArrs[0];
							ApplicationEntity rightFiberSectionLinkAppEntity = rightFiberSectionLinkAppEntityArrs[0];
							String leftEntityName = leftFiberSectionLinkAppEntity.getValue("name") + "/" + leftFiberCoreAppEntity.getValue("label");
							String rightEntityName = rightFiberSectionLinkAppEntity.getValue("name") + "/" + rightFiberCoreAppEntity.getValue("label");
							
							Map<String ,Object> extraMap = new HashMap<String, Object>();
							//拼接两个纤芯的id，以备日后与光缆段解除关系使用
							extraMap.put("id", targetMap.get("leftId") + "-" + targetMap.get("rightId"));
							extraMap.put("entityName", leftEntityName + " - " + rightEntityName);
							//获取纤芯类型
							extraMap.put("type", leftFiberCoreAppEntity.getType() + "-" + rightFiberCoreAppEntity.getType());
							lists.add(extraMap);
						}
					}
				}
			} 
		} else {
			if(appArrs != null && appArrs.length > 0) {
				for (ApplicationEntity app : appArrs) {
					//顺带把所获得的app实例的子级类型求出来
					String[] typeArrs = null;
					if(this.viewName != null) {
						//需要指定视图的情况
						typeArrs = structureCommonService.getStrutureSelationsArray(app, AssociatedType.CHILD, this.viewName, this.myStructure);
					} else {
						typeArrs = structureCommonService.getStrutureSelationsArray(app, AssociatedType.CHILD, this.myStructure);
					}
					String hasType = "";
					if(typeArrs != null && typeArrs.length > 0) {
						hasType = "has"; //存在子级类型
					} else {
						hasType = "no"; //不存在子级类型
					}
					Map<String, Object> map = app.toMap();
					map.put("hasType", hasType);
					lists.add(map);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		quickSort.sort(lists,"name");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(lists);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//获取省级区域
	public void getParentAreaAction(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "省");
		List<Map<String, Object>> listByMap = new ArrayList<Map<String,Object>>();
		List<BasicEntity> entityListByEntity = this.staffOrganizationService.getTopLevelAreaList();
		if(entityListByEntity != null){
			for(BasicEntity ap:entityListByEntity){
				listByMap.add(ap.toMap());
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		quickSort.sort(listByMap,"name");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(listByMap);
		//System.out.println(result);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 根据资源类型与id获得资源名称或label名
	 * @author：     
	 * @return void     
	 * @date：2012-4-25 上午11:52:11
	 */
	public void getResourceByIdAndTypeAction(){
		String currentEntityName="";
		long currentEntityId = Long.parseLong(this.currentEntityId);
		//获取父级的applicationEntity对象
		BasicEntity physicalresEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
		ApplicationEntity physicalresAppEntity = ApplicationEntity.changeFromEntity(physicalresEntity);
		if(physicalresAppEntity!=null){
			if(physicalresAppEntity.getValue("name")!=null){
				currentEntityName = physicalresAppEntity.getValue("name").toString();
			}else{
				currentEntityName = physicalresAppEntity.getValue("label").toString();
			}

		}	
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(currentEntityName);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取登录人区域信息
	 */
	public void getUserAreaAction(){
		//System.out.println("omomom");
		
		//获取登录人ID
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = "";
		if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
			userId = session.getAttribute("userId").toString();
		}
		List<String> operationalAreaList = null;
		//List<String> operationalChildAreaList = null;
		Set<String> operationalChildAreaList = new HashSet<String>();
		Set<String> childAreaList = new HashSet<String>();
		Set<String> oAreaList = new HashSet<String>();
//		System.out.println("sessionId: " + session.getId());
//		System.out.println("userId: " + session.getAttribute("userId"));
//		System.out.println("userName: " + session.getAttribute("userName"));
		//根据登录人ID获取登录人所在组织管理的区域集合
		List<BasicEntity> areaByUserId = staffOrganizationService.getAreaByUserId(userId);
		Map<String, Object> userAreaMap = null;
		Map<String, List<Map<String, Object>>> childArea = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> parentAreaListF = new ArrayList<Map<String, Object>>();
		if(areaByUserId != null && areaByUserId.size() > 0){
			//operationalChildAreaList = new ArrayList<String>();
			operationalAreaList = new ArrayList<String>();
			for(BasicEntity be :areaByUserId){
				lists.add(be.toMap());
			}
			quickSort.sort(lists, "name");
			int i =0;
			for(Map<String, Object> be :lists){
				//ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				String areaId = be.get("id").toString();
				String entityType = be.get("_entityType").toString();
				//System.out.println(ae.getValue("name"));
				//保存可选择当前节点
				operationalAreaList.add(areaId);	
				oAreaList.add(areaId);
				//保存可选择当前子节点
				operationalChildAreaList.add(areaId);
				childAreaList.add(areaId);
				//setop.add(areaId);
				ApplicationEntity changeFromEntity = this.structureCommonService.getSectionEntity(entityType, areaId);
//				ApplicationEntity parentArea = getParentArea(changeFromEntity)
//				if(appArrsByRecursion != null && appArrsByRecursion.length > 0){
//					ApplicationEntity applicationEntity = appArrsByRecursion[0];
//					list.add(applicationEntity);
//					
//				}
				parentAreaList = new ArrayList<Map<String, Object>>();
				//System.out.println(changeFromEntity.getValue("name"));
				
				//根据区域获取其子级区域
				ApplicationEntity[] appArrsByRecursion = structureCommonService.getAppArrsByRecursion(changeFromEntity,new String[]{"Sys_Area"},AssociatedType.CHILD,"networkresourcemanage");
				if(appArrsByRecursion != null && appArrsByRecursion.length > 0){
					for(ApplicationEntity a:appArrsByRecursion){
						String aareaId = Long.toString((Long)a.getValue("id"));
						//System.out.println(a.getValue("name"));
						oAreaList.add(aareaId);
						//保存可选择当前子节点
						operationalChildAreaList.add(aareaId);
						childAreaList.add(aareaId);
						//根据区域获取其子级区域
						ApplicationEntity[] appArrsByRecursions = structureCommonService.getAppArrsByRecursionForSrcSameType(a, "Sys_Area", "networkresourcemanage");
						if(appArrsByRecursions != null && appArrsByRecursions.length > 0){
							for(ApplicationEntity aee:appArrsByRecursions){
								String arId = Long.toString((Long)aee.getValue("id"));
								//System.out.println(aee.getValue("name"));
								//保存可选择当前子节点
								operationalChildAreaList.add(arId);
								childAreaList.add(arId);
								//setop.add(areaId);
							}
						}
					}
				}
				
				//保存当前区域到parentAreaList
				parentAreaList.add(changeFromEntity.toMap());
				//递归查询当前区域的父级区域
				getParentArea(changeFromEntity);
				if(i == 0){
					parentAreaListF = parentAreaList;
				}
				if(parentAreaList != null && parentAreaList.size() >0){
					for(Map<String, Object> ma:parentAreaList){
						String maareaId = Long.toString((Long)ma.get("id"));
						//System.out.println(ma.get("name"));
						operationalAreaList.add(maareaId);
						operationalChildAreaList.add(maareaId);
						//setop.add(areaId);
//						System.out.println("=============="+ma.get("name"));
						BasicEntity fromMap = ApplicationEntity.fromMap(ma);
						ApplicationEntity appFromEntity = ApplicationEntity.changeFromEntity(fromMap);
						//根据区域获取其子级区域
						ApplicationEntity[] appArrsByChild = structureCommonService.getAppArrsByRecursion(appFromEntity,new String[]{"Sys_Area"},AssociatedType.CHILD,"networkresourcemanage");
						if(appArrsByChild != null && appArrsByChild.length >0){
							List<Map<String, Object>> resultList = null;
							resultList = new ArrayList<Map<String,Object>>();
							for(ApplicationEntity aee:appArrsByChild){
								resultList.add(aee.toMap());
							}
							//添加子级区域到子区域集合
							childArea.put(ma.get("id").toString(), resultList);
						}
					}
				}
				
				i++;
			}
			
			if(lists != null && !lists.isEmpty()){
				quickSort.sort(lists, "name");
				userAreaMap = lists.get(0);
				parentAreaList = parentAreaListF;
			}
			
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//quickSort.sort(parentAreaList, "name");
		resultMap.put("parentArea", parentAreaList);
		resultMap.put("areaByUserId", areaByUserId);
		resultMap.put("childAreaList", childAreaList);
		resultMap.put("childArea", childArea);
		resultMap.put("userAreaMap", userAreaMap);
		resultMap.put("operationalAreaList", operationalAreaList);
		resultMap.put("operationalChildAreaList", operationalChildAreaList);
		resultMap.put("oAreaList", oAreaList);
		for(String d:operationalChildAreaList){
			//System.out.println(d);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultMap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//递归获取父级区域
	private void getParentArea(ApplicationEntity changeFromEntity){
		ApplicationEntity[] appArrsByRecursion = structureCommonService.getAppArrsByRecursion(changeFromEntity,new String[]{"Sys_Area"},AssociatedType.PARENT,"networkresourcemanage");
		if(appArrsByRecursion != null && appArrsByRecursion.length > 0){
			ApplicationEntity applicationEntity = appArrsByRecursion[0];
			parentAreaList.add(0,applicationEntity.toMap());
//			System.out.println(applicationEntity.getValue("name"));
			getParentArea(applicationEntity);
		}else{
			return;
		}
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add(0, "1");
		for(String s:list){
			//System.out.println(s);
		}
	}
	
	
	/**
	 * 根据某一个类型获取某个节点的子节点(递归)
	 */
	public void getChildEntityByTypeUniversalRecursionAction(){
		//获取父级的applicationEntity对象
		ApplicationEntity physicalresAppEntity = structureCommonService.getSectionEntity(this.parentEntityType, this.parentEntityId);
		int count = 0;
		String[] aetNameOfAetg = structureCommonService.getAetNameOfAetg(this.searchType, "networkresourcemanage");
		if(aetNameOfAetg == null || aetNameOfAetg.length <= 0){
			aetNameOfAetg = new String[]{this.searchType};
		}
		//非特殊的物理资源数量获取
		//根据父entity和当前要查找的子节点的类型，进行子节点的获取
		List<ApplicationEntity> childAppArrs = new ArrayList<ApplicationEntity>();
		if(aetNameOfAetg != null && aetNameOfAetg.length > 0){
			for(String se:aetNameOfAetg){	
				ApplicationEntity[] childAppA = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.CHILD, "networkresourcemanage");
				
				if(childAppA != null && childAppA.length > 0){
					List<ApplicationEntity> asList = Arrays.asList(childAppA);
					childAppArrs.addAll(asList);
					//System.out.println(childAppArrs.size());
				}
			}
		}
		
		if(childAppArrs != null && childAppArrs.size() > 0) {
			//获取子资源数量(child)
			count = childAppArrs.size();
		}
		
		if(this.addLink != null && !this.addLink.equals("")) {
			//如果addLink不为空，获取子资源数量(link)
			ApplicationEntity[] linkAppArrs = structureCommonService.getAppArrsByRecursionForSrc(
					physicalresAppEntity, this.searchType, "networkresourcemanage");
			
			if(linkAppArrs != null && linkAppArrs.length > 0) {
				count += linkAppArrs.length;
			}
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(count);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 根据某一个类型获取某个节点的子节点(递归)
	 */
	public void getChildEntityByTypeUniversalRecursionForFlatternInfoAction(){
		//获取父级的applicationEntity对象
		ApplicationEntity physicalresAppEntity = structureCommonService.getSectionEntity(this.parentEntityType, this.parentEntityId);
		int count = 0;
			//如果addLink不为空，获取子资源数量(link)
			ApplicationEntity[] linkAppArrs = structureCommonService.getAppArrsByRecursionForSrc(
					physicalresAppEntity, this.searchType, "networkresourcemanage");
			ApplicationEntity[] linkAppArrs1 = null;
			if(ResourceCommon.isLinkType(this.parentEntityType)){
				linkAppArrs1 = structureCommonService.getStrutureSelationsApplicationEntity(
						physicalresAppEntity, this.searchType, AssociatedType.LINK, "networkresourcemanage");
			}
			if(linkAppArrs != null && linkAppArrs.length > 0) {
				count += linkAppArrs.length;
			}
			if(linkAppArrs1 != null && linkAppArrs1.length > 0) {
				count += linkAppArrs1.length;
			}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(count);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
