Ext.BLANK_IMAGE_URL = "jslib/extjs/resources/images/default/s.gif";


// 刷新角色关联的门户组件
function refreshRoleHomItem() {

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
 * 刷新不与选中角色关联的门户组件
 */
function refreshHomItem() {
	var assportalitem_grid = Ext.getCmp('assportalitem_grid');
	var assportalitem_store = assportalitem_grid.getStore();
	var orgRoleId = document.getElementById('orgRoleId').value;
	if ( !orgRoleId ) {
		return;
	}
	// 刷新
	assportalitem_store.load({
				params : {
					orgRoleId : orgRoleId
				}
			});
}

/**
 * 
 * 删除角色门户组件关联
 */
function deleteRoleHomeItem() {
	var orgRoleId = document.getElementById('orgRoleId').value;
	if (!orgRoleId) {
		return;
	}
	// 获取选中的门户组件
	var allportalitem_grid = Ext.getCmp('allportalitem_grid');
	var selecteItemCount = allportalitem_grid.getSelectionModel().getCount();
	if (selecteItemCount == 0) {
		alert("请先选择要删除关联的门户组件");
		return;
	}
	var selectedItems = allportalitem_grid.getSelectionModel().getSelections();
	var addItems = [];// 最终添加的门户组件

	
	for (var i = 0; i < selectedItems.length; i++) {
		var si = selectedItems[i];
		addItems.push(si.data.id);
	}
	var json = Ext.encode(addItems);
	// 开始执行删除动作
	Ext.Ajax.request({
		       timeout:60000,
				url : 'op/home/deleteRoleHomeItemAction',
				params : {
					json : json,
					orgRoleId : orgRoleId,
				},
				callback : function(opts, success, response) {
					if (success) {
						var text = response.responseText;
						if (text.indexOf('success') >= 0) {
							alert('删除关联门户组件成功!');
							// 刷新
							refreshHomItem();
							refreshRoleHomItem();
						} else {
							alert("删除关联门户组件失败")
						}
					} else {
						alert('删除关联门户组件失败！');
					}
				}
			})
}
//批量增加角色组件
function addRoleHomeItem(){
	var orgRoleId = document.getElementById('orgRoleId').value;
	if (!orgRoleId) {
		return;
	}
	// 获取选中的门户组件
	var allportalitem_grid = Ext.getCmp('assportalitem_grid');
	var selecteItemCount = allportalitem_grid.getSelectionModel().getCount();
	if (selecteItemCount == 0) {
		alert("请先选择要添加关联的门户组件");
		return;
	}
	var selectedItems = allportalitem_grid.getSelectionModel().getSelections();
	var addItems = [];// 最终添加的门户组件

	
	for (var i = 0; i < selectedItems.length; i++) {
		var si = selectedItems[i];
		addItems.push(si.data.id);
	}
	var json = Ext.encode(addItems);
	// 开始执行删除动作
	Ext.Ajax.request({
		       timeout:60000,
				url : 'op/home/saveRoleHomeItemAction',
				params : {
					json : json,
					orgRoleId : orgRoleId,
					operateType :'addMore'
				},
				callback : function(opts, success, response) {
					if (success) {
						var text = response.responseText;
						if (text.indexOf('success') >= 0) {
							alert('批量增加角色组件成功!');
							// 刷新
							refreshHomItem();
							refreshRoleHomItem();
						} else {
							alert("批量增加角色组件失败")
						}
					} else {
						alert('批量增加角色组件组件失败！');
					}
				}
			})
}


// 配置角色下，门户组件
Ext.onReady(function() {
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();

	

	// --------------------------------------组织角色
	// begin------------------------------------------//
	var orgrole_store = new Ext.data.JsonStore({
				fields:['orgRoleId', 'orgRoleName', 'orgRoleCode','orgRoleType'],
				url : 'op/home/getAllRolesForHomeAction',//yuan.yw
				root : 'result',
				autoLoad : true
			});

	var orgrole_grid = new Ext.grid.GridPanel({
				id : 'orgrole_grid',
				store : orgrole_store,
				title : '角色列表',
				columns : [new Ext.grid.RowNumberer(),{
							header : '角色名称',
							dataIndex : 'orgRoleName' 
							
						}, {
							header : '角色编码',
							dataIndex : 'orgRoleCode' 
						}
						],
				listeners : {
					rowClick : function(grid, rowIndex, e) {
						var record = grid.getStore().getAt(rowIndex);
						document.getElementById('orgRoleId').value = record.data.orgRoleId;

						// 刷新组织角色关联的门户组件
						refreshRoleHomItem();
						//刷新功能标签、组织角色管理的门户组件
				      refreshHomItem();
					}

				}

			});

	// --------------------------------------组织角色
	// end------------------------------------------//

	

	// --------------------------------------门户方案下的功能标签下与组织角色共同决定的门户组件，
	// begin------------------------------------------//
	var assportalitem_store = new Ext.data.JsonStore({
				url : 'op/home/getRoleHomeItemListNoAssociateRoleIdAction',
				root : 'result',
				fields : ['id', 'title', 'showTitle', 'url',
						'maxUrl', 'orgRoleId', 'defaultHeight', 'defaultWidth','itemHeight','itemWidth','itemRow','itemColumn']
			});

	// 更新插件
	var assportalitem_editor = new Ext.ux.grid.RowEditor({
		id : 'assportalitem_editor',
		saveText : '关联角色保存',
		cancelText : '取消',
		clicksToEdit : 2,
		listeners : {
			beforeedit : function(RowEditor) {
			},
			// 保存
			afteredit : function(r, o, record, n) {
				var grid = this.grid;
				var store = grid.getStore();
				var data =  record.data;  
				var json = Ext.encode(data);
				// AJAX保存
				Ext.Ajax.request({
					url : 'op/home/saveRoleHomeItemAction',
					params : {
						json : json
					},
					success : function() {
						var msg = Ext.MessageBox.alert('提示', '保存成功!');
						setTimeout(function() {
									Ext.MessageBox.hide()
								}, 1000);

						// 刷新
						refreshHomItem();
						refreshRoleHomItem();
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
				title : '角色未关联门户组件列表',
				id : 'assportalitem_grid',
				store : assportalitem_store,
				sm:assportalitem_sm,
				plugins : [assportalitem_editor],
				autoScroll : true,
				loadMask: {msg:'正在加载数据……'},
				tbar : [
                        {
							text : '批量添加角色门户组件关联',
							iconCls : 'silk-add',
							handler : function() {
								addRoleHomeItem();
							}
						}, {
							text : '刷新数据',
							iconCls : 'silk-table-refresh',
							handler : function() {
								//refreshHomItem();
								assportalitem_store.reload();
							}
						}],
				columns : [new Ext.grid.RowNumberer(), assportalitem_sm, {
							header : '组件标题',
							dataIndex : 'title'
						}, {
							header : '行',
							dataIndex : 'itemRow',
							sortable:true,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '列',
							dataIndex : 'itemColumn',
							sortable:true,
							editor : new Ext.form.NumberField({
										minValue : 0,
										maxValue : 2
										
									})
						},{
							header : '高度',
							dataIndex : 'itemHeight',
							editor : new Ext.form.NumberField({
										minValue : 1
									})
						}, {
							header : '宽度',
							dataIndex : 'itemWidth',
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
				fields : ['id', 'title', 'showTitle', 'url',
						'maxUrl', 'orgRoleId', 'defaultHeight', 'defaultWidth','itemHeight','itemWidth','itemRow','itemColumn'],
				url : 'op/home/getRoleHomeItemListByRoleIdAction',
				root : 'result'
			});
	// 更新插件
	var allportalitem_editor = new Ext.ux.grid.RowEditor({
		id : 'allportalitem_editor',
		saveText : '更新',
		cancelText : '取消',
		clicksToEdit : 2,
		listeners : {
			beforeedit : function(RowEditor) {
			},
			// 保存
			afteredit : function(r, o, record, n) {
				var grid = this.grid;
				var store = grid.getStore();
				var data =  record.data;  
				var json = Ext.encode(data);
				// AJAX保存
				Ext.Ajax.request({
					url : 'op/home/saveRoleHomeItemAction',
					params : {
						json : json,
						operateType : "update"
					},
					success : function() {
						var msg = Ext.MessageBox.alert('提示', '保存成功!');
						setTimeout(function() {
									Ext.MessageBox.hide()
								}, 1000);

						// 刷新
						refreshRoleHomItem();
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

	var orgRoleAssPortalItem_selectModel = new Ext.grid.CheckboxSelectionModel();

	var allportalitem_grid = new Ext.grid.GridPanel({
				title : '角色关联的的门户组件',
				id : 'allportalitem_grid',
				store : allportalitem_store,
				sm : orgRoleAssPortalItem_selectModel,
				height : 300,
				plugins : [allportalitem_editor],
				columns : [new Ext.grid.RowNumberer(),orgRoleAssPortalItem_selectModel, {
							header : '组件标题',
							dataIndex : 'title'
						},  {
							header : '行',
							dataIndex : 'itemRow',
							width : 50,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '列',
							dataIndex : 'itemColumn',
							width : 50,
							editor : new Ext.form.NumberField({
										minValue : 0,
										maxValue : 2
									})
						}, {
							header : '高度',
							dataIndex : 'itemHeight',
							width : 50,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '宽度',
							dataIndex : 'itemWidth',
							width : 50,
							editor : new Ext.form.NumberField({
										minValue : 0
									})
						}, {
							header : '默认高度',
							dataIndex : 'itemHeight',
							width : 50
						}, {
							header : '默认宽度',
							dataIndex : 'itemWidth',
							width : 50
						},{
							header : '是否显示',
							width : 120,
							dataIndex : 'showTitle',
							renderer : function(value) {
								if (value == '1') {
									return "显示";
								} else {
									return "不显示";
								}
							}
						},{
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
					text : '删除角色组件关联关系',
					iconCls : 'silk-delete',
					handler : function() {
						deleteRoleHomeItem();
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
										region : 'center',
										autoScroll : true,
										layout : 'border',
										items : [
										      {
										         region:'west',
										         layout:'fit',
										         width:230,
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