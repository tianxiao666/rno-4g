Ext.BLANK_IMAGE_URL = "jslib/extjs/resources/images/default/s.gif";

/**
 * 
 * 2012-5-23 gmh brief
 * 
 * 页面隐藏域： schemeId ：记录选中的门户方案id orgRoleId：记录选中的组织角色Id workZoneSiteId：记录选中的功能标签id
 * 
 * 组件id：
 * 
 * 门户方案 scheme_grid scheme_store
 * 
 * 组织角色 orgrole_grid orgrole_store
 * 
 * 门户方案下的功能标签 workzonesite_grid workzonesite_store
 * 
 * 功能标签下的门户组件 assportalitem_grid assportalitem_store
 * 
 * 组织角色下的所有的门户组件 allportalitem_grid allportalitem_store
 * 
 * 
 * url: 获取所有的门户方案 ：getAllPortalProjectList 获取所有的组织角色：getAllOrgRoleAction
 * 获取门户方案下的功能标签：getAllPortalProjectWorkZoneSiteListAction
 * 获取组织角色下的门户组件：getOrgRoleAssPortalItemAction
 * 
 * 获取指定门户方案、组织角色下的功能标签下的门户组件
 * ：getPortalItemsUnderWorkZoneSiteOfProjectAndOrgRoleAction
 * 增加门户组件到指定门户方案、组织角色下的功能标签下
 * ：addPortalItemsToWorkZoneSiteOfProjectAndOrgRoleAction
 * 删除指定门户方案、组织角色下的功能标签下的指定的门户组件：deletePortalItemsFromWorkZoneSiteOfProjectAndOrgRoleAction
 * 编辑指定门户方案、组织角色下的功能标签下的门户组件的配置数据
 * ：savePortalItemsUnderWorkZoneSiteOfProjectAndOrgRoleAction
 * 
 * 
 * 
 * 
 * 
 * 响应函数： 添加门户组件到功能标签下： addPortalItemToWorkZoneSite
 * 
 * 保存门户组件在功能标签下的设置： savePortalItemConfigInWorkZoneSite
 * 
 * 删除功能标签下的选定的门户组件：deletePortalItemInWorkZoneSite
 * 
 * 
 * 刷新门户方案下的功能标签：refreshWorkZoneSiteUnderScheme
 * 
 * 刷新组织角色关联的门户组件：refreshPortalItemUnderOrgRole();
 * 
 * 刷新功能标签下的关联的门户组件：refreshPortalItemUnderWorkZoneSite();
 */

// 刷新门户方案关联的功能标签
function refreshWorkZoneSiteUnderScheme() {
	var schemeId = document.getElementById('schemeId').value;
	if (!schemeId) {
		return;
	}

	var workzonesite_grid = Ext.getCmp('workzonesite_grid');
	var workzonesite_store = workzonesite_grid.getStore();
	workzonesite_store.load({
				params : {
					portalProjectId : schemeId
				}
			})
}

// 刷新组织角色关联的门户组件
function refreshPortalItemUnderOrgRole() {

	var orgRoleId = document.getElementById('orgRoleId').value;
	if (!orgRoleId) {
		return;
	}

	var allportalitem_grid = Ext.getCmp('allportalitem_grid');
	var allportalitem_store = allportalitem_grid.getStore();
	allportalitem_store.load({
				params : {
					orgRoleId : orgRoleId
				}
			});
}

/**
 * 获取功能标签下的门户组件
 */
function refreshPortalItemUnderWorkZoneSite() {
	var assportalitem_grid = Ext.getCmp('assportalitem_grid');
	var assportalitem_store = assportalitem_grid.getStore();

	var schemeId = document.getElementById('schemeId').value;
	var orgRoleId = document.getElementById('orgRoleId').value;
	var workZoneSiteId = document.getElementById('workZoneSiteId').value;

	if (!schemeId || !orgRoleId || !workZoneSiteId) {
		// alert('请先确定：门户方案、组织角色，以及功能标签，再刷新相关数据');
		return;
	}

	// 刷新
	assportalitem_store.load({
				params : {
					schemeId : schemeId,
					orgRoleId : orgRoleId,
					workZoneSiteId : workZoneSiteId
				}
			});
}

/**
 * 添加门户组件到功能标签 需要检查选择的门户组件以及要添加的功能标签的类型是否匹配 如：porlet类型的门户组件不能添加到iframe类型的功能标签
 * 而:iframe类型的门户组件目前也只允许添加一个iframe组件
 */
function addPortalItemToWorkZoneSite() {
	// 判断所选的功能标签
	// 如果是框架类型的，则只允许出现一个框架类型的门户组件
	// 不匹配的要去掉

	// 如果是门户组件类型的功能标签，则只需要排除重复的就可以了

	// 发送到后台的要进行一下标识：replace为true，针对的是框架类型的功能标签，替换其下的门户组件（框架类型的）
	// replace为false，则表示执行插入操作

	var schemeId = document.getElementById('schemeId').value;
	var orgRoleId = document.getElementById('orgRoleId').value;
	var workZoneSiteId = document.getElementById('workZoneSiteId').value;
	if (!schemeId || !orgRoleId || !workZoneSiteId) {
		// alert('请先确定：门户方案、组织角色，以及功能标签，再刷新相关数据');
		return;
	}

	var workzonesite_grid = Ext.getCmp('workzonesite_grid');
	var count = workzonesite_grid.getSelectionModel().getCount();
//	alert("count==" + count);
	if (count == 0) {
		alert("还未选择功能标签，请先选择");
		return;
	}

	// 获取选择的功能标签
	var record = workzonesite_grid.getSelectionModel().getSelected();
	if (!record) {
		return;
	}

	// type 为1 ：门户组件类型
	// type 为2：应用框架类型
	var workzoneSiteType = record.data.type;
	// alert(record.data.workItemName+","+record.data.type);

	// 门户组件的类型：
	// type为1：porlet组件
	// type 为2：iframe框架组件

	// 获取选中的门户组件
	var allportalitem_grid = Ext.getCmp('allportalitem_grid');
	var selecteItemCount = allportalitem_grid.getSelectionModel().getCount();
	if (selecteItemCount == 0) {
		alert("请先选择要添加的门户组件");
		return;
	}
	var selectedItems = allportalitem_grid.getSelectionModel().getSelections();

	// 获取已在功能标签下的门户组件
	var assportalitem_grid = Ext.getCmp('assportalitem_grid');
	var assportalitem_store = assportalitem_grid.getStore();

	var addItems = [];// 最终添加的门户组件
	var replace = false;// 是替换还是插入

	// 判断功能标签的类型，决定如何处理添加动作
	var find = false;
	var matchType = false;
	if (workzoneSiteType == 1) {// 门户组件类型的，里面可以存放多个porlet组件
		// 将当前选择的门户组件与已经存在的进行比较，去掉重复，去掉非porlet类型的组件
		console.log("功能标签类型：1-》门户组件类型")
		for (var i = 0; i < selectedItems.length; i++) {
			var si = selectedItems[i];
			find = false;
			// 比较类型
			if (si.data.type != 1) {
				console.log("门户组件类型：" + si.data.type + ",不合要求")
				continue;
			} else {
				// 是否有重复
				assportalitem_store.each(function(existRecord) {
							if (existRecord.data.portalItemId == si.data.id)
								find = true;
							return false;
						});
				if (!find) {
					// 未重复
					console.log("加入符合条件的：" + si.data.title)
					addItems.push(si.data);
				} else {
					console.log("重复：" + si.data.title)
				}
			}
		}
	} else {
		// 应用框架类型
		console.log("功能标签类型：2-》应用框架类型")
		var selectedItem;
		var fillRequirementCount = 0;
		for (var i = 0; i < selectedItems.length; i++) {
			var si = selectedItems[i];
			// 只选第一个
			find = false;
			if (si.data.type == 2) {
				assportalitem_store.each(function(existRecord) {
							if (existRecord.data.portalItemId == si.data.id)
								find = true;
							return false;
						});
				if (!find) {
					console.log(si.data.title + " 类型为 2，符合条件")
					selectedItem = si;
					fillRequirementCount++;
				}else{
					console.log("重复："+si.data.title)
				}
			}
		}

		console.log("总共符合条件的有：" + fillRequirementCount)

		// 所选择的门户组件，都不符合类型要求
		if (fillRequirementCount == 0) {
			alert("由于本功能标签是 应用框架类型，而所选择要添加的组件类型都是porlet类型的组件，不能执行添加。只能添加类型为iframe框架组件");
			return;
		} else if (fillRequirementCount > 1) {
			if (!confirm("选中了多个iframe类型的组件，但由于该应用框架类型的功能标签只能允许一个组件，所以，只有第一个组件："
					+ selectedItem.data.title + " 会被添加。是否往下执行?")) {
				return;
			}
		}

		addItems.push(selectedItem.data);
		if (assportalitem_store.getCount() > 0) {// 本来就有iframe类型的组件，那么要执行替换
			replace = true;
		}
	}

	if(addItems.length==0){
		alert("所选择的门户组件都不符合要求");
		return;
	}
	var json = Ext.encode(addItems);
	// 开始执行添加动作
	Ext.Ajax.request({
		       timeout:60000,
				url : 'addPortalItemsToWorkZoneSiteOfProjectAndOrgRoleAction',
				params : {
					json : json,
					replace : replace,
					schemeId : schemeId,
					orgRoleId : orgRoleId,
					workZoneSiteId : workZoneSiteId
				},
				callback : function(opts, success, response) {
					if (success) {
						var text = response.responseText;
						if (text.indexOf('success') >= 0) {
							alert('添加门户组件到功能标签下成功!');
							// 刷新
							refreshPortalItemUnderWorkZoneSite();
						} else {
							alert("添加门户组件到功能标签下失败")
						}
					} else {
						alert('出现未知错误！');
					}
				}
			})
}

/**
 * 保存门户组件在功能标签下的设置
 */
function savePortalItemConfigInWorkZoneSite() {
///直接在editor的handler写了
}

/**
 * 删除功能标签下的选定的门户组件
 */
function deletePortalItemInWorkZoneSite() {
	//获取选中要从功能标签删除的门户组件
	var assportalitem_grid=Ext.getCmp('assportalitem_grid');
	var assportalitem_store=assportalitem_grid.getStore();
	var records=assportalitem_grid.getSelectionModel().getSelections();
	console.log(records.length);
	if(records.length==0){
		alert('请先选择要从功能标签删除的门户组件');
		return;
	}
	
	var deletes=[];
	for(var i=0;i<records.length;i++){
		deletes.push(records[i].data);
	}
	
	var json=Ext.encode(deletes);
	
	Ext.Ajax.request({
	    url:'deletePortalItemsFromWorkZoneSiteOfProjectAndOrgRoleAction',
	    params:{json:json},
	    callback:function(opts,success,response){
	    	 if(success){
	    	 	var text=response.responseText;
	    	 	if(text.indexOf('success')>=0){
	    	 		alert('从功能标签删除门户组件成功！');
	    	 		//刷新
	    	 		refreshPortalItemUnderWorkZoneSite();
	    	 		
	    	 	}else{
	    	 		alert("从功能标签删除门户组件失败！");
	    	 	}
	    	 }else{
	    	 	alert("出现未知错误！错误描述："+response.statusText);
	    	 }
	    	
	    }
		
	});
	
}

// 配置组织角色下，某个门户方案的某个功能标签下的门户组件
Ext.onReady(function() {
	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();

	// --------------------------------------门户方案
	// begin------------------------------------------//
	// store
	var scheme_store = new Ext.data.JsonStore({
				fields : ['projectId', 'projectName', 'projectDesc'],
				url : 'getAllPortalProjectList',
				root : 'result',
				autoLoad : true
			});

	var scheme_grid = new Ext.grid.GridPanel({
		title : '门户方案列表',
		store : scheme_store,
		id : 'scheme_grid',
		// frame:true,
		columns : [new Ext.grid.RowNumberer(),{
					header : '方案名称',
					dataIndex : 'projectName'
				}, {
					header : '方案描述',
					dataIndex : 'projectDesc'
				}],
		listeners : {
			rowClick : function(grid, rowIndex, e) {
				var record = grid.getStore().getAt(rowIndex);
				document.getElementById('schemeId').value = record.data.projectId;

				// 刷新门户方案关联的功能标签
				refreshWorkZoneSiteUnderScheme();
			}
		}

	});

	// --------------------------------------门户方案
	// end------------------------------------------//

	// --------------------------------------组织角色
	// begin------------------------------------------//
	var orgrole_store = new Ext.data.JsonStore({
				//fields : ['orgRoleId', 'orgRoleName', 'orgRoleCode',',orgRoleType','orgCode','requirement',',level', 'description'], modified 2012-12-24
				fields:['orgRoleId', 'orgRoleName', 'orgRoleCode','orgRoleType'],
				url : 'getAllOrgRoleAction',
				root : 'result',
				autoLoad : true
			});

	var orgrole_grid = new Ext.grid.GridPanel({
				id : 'orgrole_grid',
				store : orgrole_store,
				title : '组织角色列表',
				columns : [new Ext.grid.RowNumberer(),{
							header : '组织角色名称',
							dataIndex : 'orgRoleName' 
							
						}, {
							header : '组织角色编码',
							dataIndex : 'orgRoleCode' 
						},
						//{
							//header : '组织角色描述',
							//dataIndex : 'description'  
						//}
						],
				listeners : {
					rowClick : function(grid, rowIndex, e) {
						var record = grid.getStore().getAt(rowIndex);
						document.getElementById('orgRoleId').value = record.data.orgRoleId;

						// 刷新组织角色关联的门户组件
						refreshPortalItemUnderOrgRole();
						//刷新功能标签、组织角色管理的门户组件
				      refreshPortalItemUnderWorkZoneSite();
					}

				}

			});

	// --------------------------------------组织角色
	// end------------------------------------------//

	// --------------------------------------门户方案下的功能标签
	// begin------------------------------------------//
	var workzonesite_store = new Ext.data.JsonStore({
		url : 'getAllPortalProjectWorkZoneSiteListAction',
		root : 'result',
		fields : ['id', 'projectId', 'workItemName', 'type', 'layout', '_order','workZoneSiteId']
	});

	var workzonesite_grid = new Ext.grid.GridPanel({
		title : '门户方案下的功能标签',
		id : 'workzonesite_grid',
		store : workzonesite_store,
		autoScroll : true,
		columns : [new Ext.grid.RowNumberer(),{
					header : '功能标签名称',
					dataIndex : 'workItemName'
				}, {
					header : '功能标签类型',
					dataIndex : 'type',
					renderer : function(value) {
						if (value == "1") {
							return "门户组件类型";
						} else if (value == "2") {
							return "应用框架类型";
						} else {
							return "请选择";
						}
					}
				}],
		listeners : {
			rowClick : function(grid, rowIndex, e) {
				var record = grid.getStore().getAt(rowIndex);
				document.getElementById('workZoneSiteId').value = record.data.workZoneSiteId;

				// alert(record.data.type);
				// 刷新功能标签下的关联的门户组件
				refreshPortalItemUnderWorkZoneSite();
				
			}
		}
	});

	// --------------------------------------门户方案下的功能标签
	// end------------------------------------------//

	// --------------------------------------门户方案下的功能标签下与组织角色共同决定的门户组件，
	// begin------------------------------------------//
	var assportalitem_store = new Ext.data.JsonStore({
				url : 'getPortalItemsUnderWorkZoneSiteOfProjectAndOrgRoleAction',
				root : 'result',
				fields : ['id', 'portalItemId', 'workZoneSiteId',
						'portalItemHeight', 'portalItemWidth', '_row',
						'_column', '_order', 'orgRoleId', 'projectId', 'title']
			});

	// 更新插件
	var assportalitem_editor = new Ext.ux.grid.RowEditor({
		id : 'assportalitem_editor',
		saveText : '保存',
		cancelText : '取消',
		clicksToEdit : 2,
		listeners : {
			beforeedit : function(RowEditor) {
				// alert(RowEditor.record);
				// return false;
			},
			// 保存
			afteredit : function(r, o, record, n) {
				var grid = this.grid;
				var store = grid.getStore();
				var data = [];
				data.push(record.data);
				
				var json = Ext.encode(data);
				// alert(Ext.encode(json));

				// alert(Ext.encode(store.json));
				// AJAX保存
				Ext.Ajax.request({
					url : 'savePortalItemsUnderWorkZoneSiteOfProjectAndOrgRoleAction.action',
					params : {
						json : json
					},
					success : function() {
						var msg = Ext.MessageBox.alert('提示', '保存成功!');
						setTimeout(function() {
									Ext.MessageBox.hide()
								}, 1000);

						// 刷新
						refreshPortalItemUnderWorkZoneSite();
					},
					failure : function() {

					}
				});
			},
			canceledit : function(RowEditor) {
				if (RowEditor.record.data.projectId == ""
						&& RowEditor.record.phantom) {
					this.grid.getStore().removeAt(RowEditor.rowIndex);
				}
			}

		}
	});

	var assportalitem_sm = new Ext.grid.CheckboxSelectionModel({
         singleSelect:false
	})
	var assportalitem_grid = new Ext.grid.GridPanel({
				title : '功能标签下的门户组件列表',
				id : 'assportalitem_grid',
				store : assportalitem_store,
				sm:assportalitem_sm,
				plugins : [assportalitem_editor],
				autoScroll : true,
				tbar : [
//					{
//							text : '保存配置',
//							iconCls : 'icon-save',
//							handler : function() {
//								savePortalItemConfigInWorkZoneSite();
//							}
//						}, 
							{
							text : '删除门户组件',
							iconCls : 'silk-delete',
							handler : function() {
								deletePortalItemInWorkZoneSite();
							}
						}, {
							text : '刷新数据',
							iconCls : 'silk-table-refresh',
							handler : function() {
								refreshPortalItemUnderWorkZoneSite();
							}
						}],
				columns : [new Ext.grid.RowNumberer(), assportalitem_sm, {
							header : '功能标签名称',
							dataIndex : 'title'
						}, {
							header : '行',
							dataIndex : '_row',
							sortable:true,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '列',
							dataIndex : '_column',
							sortable:true,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '顺序',
							dataIndex : '_order',
							sortable:true,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '高度',
							dataIndex : 'portalItemHeight',
							editor : new Ext.form.NumberField({
										minValue : 1
									})
						}, {
							header : '宽度',
							dataIndex : 'portalItemWidth',
							editor : new Ext.form.NumberField({
										minValue : 1
									})
						}

				]

			});

	// --------------------------------------门户方案下的功能标签下与组织角色共同决定的门户组件，
	// end------------------------------------------//

	// --------------------------------------组织角色下的门户组件
	// begin------------------------------------------//
	// ----组织角色关联门户组件 开始----//

	var allportalitem_store = new Ext.data.JsonStore({
				id : 'allportalitem_store',
				fields : ['id', 'title', 'showTitle', 'type', 'isLocal', 'url',
						'maxUrl', 'orgRoleId', 'defaultHeight', 'defaultWidth'],
				url : 'getOrgRoleAssPortalItemAction',
				root : 'result'
			});

	var orgRoleAssPortalItem_selectModel = new Ext.grid.CheckboxSelectionModel();

	var allportalitem_grid = new Ext.grid.GridPanel({
				title : '组织角色关联的的门户组件情况',
				id : 'allportalitem_grid',
				store : allportalitem_store,
				sm : orgRoleAssPortalItem_selectModel,
				height : 300,
				columns : [new Ext.grid.RowNumberer(),orgRoleAssPortalItem_selectModel, {
							header : '组件标题',
							dataIndex : 'title'
						}, {
							header : '是否显示面板头部',
							width : 120,
							dataIndex : 'showTitle',
							renderer : function(value) {
								if (value == '1') {
									return "显示面板头部";
								} else {
									return "不显示面板头部";
								}
							}
						}, {
							header : '组件类型',
							dataIndex : 'type',
							renderer : function(value) {
								if (value == 1) {
									return "porlet组件";
								} else {
									return "iframe框架组件";
								}
							}
						}, {
							header : '引用资源类型',
							dataIndex : 'isLocal',
							renderer : function(value) {
								if (value == '0') {
									return "内部资源";
								} else {
									return '外部资源';
								}
							}
						}, {
							header : '资源url',
							dataIndex : 'url',
							width : 200
						}, {
							header : '最大化时的url',
							dataIndex : 'maxUrl',
							width : 200
						}],
				tbar : [
						// '->',
						{
					text : '添加到功能标签',
					iconCls : 'silk-add',
					handler : function() {
						addPortalItemToWorkZoneSite();
					}
				}]

			});

	// ----组织角色关联门户组件 结束----//

	// --------------------------------------组织角色下的门户组件
	// end------------------------------------------//

	// --------------------------------------界面布局
	// begin------------------------------------------//
	var mainPanel = new Ext.Panel({
				layout : 'border',
				items : [{
							region : 'west',
							layout : 'border',
							id : 'west-panel',
							width : 600,
							collapsible : true,
							autoScroll : true,
							items : [{
										region : 'north',
										height : 300,
										autoScroll : true,
										layout : 'border',
										items : [
										      {
										         region:'west',
										         layout:'fit',
										         width:300,
										         items:[scheme_grid]
										      },
										      {
										      	region:'center',
										      	layout:'fit',
										      	items:[workzonesite_grid]
										      }
										]
									}, {
										region : 'center',
										autoScroll : true,
										layout : 'border',
										items : [
										      {
										         region:'west',
										         layout:'fit',
										         width:300,
										         items:[orgrole_grid]
										      }
										      ,
										      {
										      	region:'center',
										      	layout:'fit',
										      	items:[allportalitem_grid]
										      }
										]
									}
									]
						}
						,
						{
							region : 'center',
							id : 'center-panel',
							layout : 'fit',
							collapsible : true,
							items : [
								assportalitem_grid
						    ]
						}
				]
			});

	var viewPort = new Ext.Viewport({
				layout : 'fit',
				items : [mainPanel]
			});
		// --------------------------------------界面布局
		// begin------------------------------------------//
});