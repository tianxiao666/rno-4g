Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

/*    _________________
 *    |________________|__________________
 *    |________________|__________________|
 *    |________________|
 * 
 * 
 * 
 * 
 * 
 * 
 */

function portalProjectAction(record){	
	var projectId = record.data.projectId;	
	var projectName  =record.data.projectName;
	Ext.getCmp('center-panel').setTitle('['+projectName+']:工作空间列表');
	document.getElementById("projectId").value =  projectId;
	
	var workZone_grid = Ext.getCmp('workZone_grid');
   	var workZone_store=workZone_grid.getStore();
	workZone_store.load({params:{portalProjectId:projectId}});
	
	//2012-5-16 gmh add
	Ext.getCmp('south-panel').setTitle("["+projectName+"]:功能标签列表")
	var allWorkZoneSite_grid = Ext.getCmp('allWorkZoneSite_grid');
   	var allWorkZoneSite_store=allWorkZoneSite_grid.getStore();
	allWorkZoneSite_store.load({params:{portalProjectId:projectId}});
}

//编辑工作空间标签
function workZoneSiteEditAction(){
	var grid = Ext.getCmp('workZoneSite_grid');
	var workZoneSite_editor = Ext.getCmp('workZoneSite_editor');
	var record = grid.getSelectionModel().getSelected();
	var index = grid.store.indexOf(record);
	workZoneSite_editor.startEditing(index);
}



function workZoneIsCheckedOnClick(value){
	var grid = Ext.getCmp('workZone_grid');
//	var workZone_editor = Ext.getCmp('workZone_editor');
	var record = grid.getSelectionModel().getSelected();
	var isChecked=0;
	if(value==true){
		isChecked=1;
	}else{
		isChecked=0;
	}
	record.data.isChecked = isChecked;
}


/**
 * 将选中的功能标签，添加到门户方案的工作空间下
 */
function addWorkZoneSiteToPortalProjectWorkZone(){
	//找到选中的功能标签
	var allWorkZoneSite_grid=Ext.getCmp("allWorkZoneSite_grid");
	var count=allWorkZoneSite_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选中要添加的功能标签");
		return;
	}
	
	var portalProjectId=document.getElementById('projectId').value;
	var workZoneId=document.getElementById("workZoneId").value;
	
	if(workZoneId==""){
		alert("请先选择目标工作空间");
		return;
	}
	
	
	var records=allWorkZoneSite_grid.getSelectionModel().getSelections();
	
	var need=[];
	
	//判断与已经存在于此门户方案下的工作空间的功能标签是否由重复，去掉重复部分
	var workZoneSite_grid = Ext.getCmp("workZoneSite_grid");
	var existsStore = workZoneSite_grid.getStore();
	var find = false;
	if (existsStore.getTotalCount() > 0) {
		Ext.each(records, function(record) {// 循环排除已经存在的记录
					find = false;
					existsStore.each(function(exist) {
								if (exist.data.workzoneSiteId == record.data.id) {
									console.log(exist.data.workItemName
											+ " 已经存在")
									find = true;
									return;
								}
							});
					if (!find) {
						console.log("添加：" + record.data.workItemName)
						need.push(record.data);
					}
				})
	}else{
		Ext.each(records, function(record){
			   need.push(record.data);
		});
	}

	if (need.length == 0) {
		// 执行添加，添加成功后，刷新
		alert("所选择的功能标签目前都已经处在该工作空间下，无需再添加");
		return;
	}
	
	
	var json=Ext.encode(need);
	
	Ext.Ajax.request({
	   url:'addWorkZoneSitesToWorkZoneUnderPortalProjectAction',
	   params:{json:json,portalProjectId:portalProjectId,workZoneId:workZoneId},
	   callback:function(options,success,response){
	   	   if(success){
                var text=response.responseText;
                if(text.indexOf('success')>=0){
                	alert("添加功能标签到工作空间成功！")
                	//刷新
                	existsStore.load({
                	  params:{
                	  	portalProjectId:portalProjectId,
                	  	workZoneId:workZoneId
                	  }
                	})
                }else{
                	alert("添加功能标签到工作空间失败！")
                }
	   	   }else{
	   	   	  alert("出现未知错误! statusCode =" +response.status);
	   	   }
	   	
	   }
		
	})
	
}


function workZoneSiteOrderOnChange(value){
	var grid = Ext.getCmp('workZoneSite_grid');
	var record = grid.getSelectionModel().getSelected();
	record.data._order = value;
	
}

/**
 * 保存功能标签在工作空间下的设置
 */
function workZoneSiteOnSave(){
	var workZoneSite_grid = Ext.getCmp("workZoneSite_grid");
	var existsStore = workZoneSite_grid.getStore();
	
	
	var changes = [];
	if (existsStore.getTotalCount() > 0) {
		existsStore.each(function(record) {
					changes.push(record.data);
				});
	}
	
	if(changes.length==0){
		return;
	}
	
	var json=Ext.encode(changes);
	
//	alert(json);
	var portalProjectId=document.getElementById('projectId').value;
	var workZoneId=document.getElementById("workZoneId").value;
	
	
	Ext.Ajax.request({
	   url:'saveWorkZoneSiteConfigInWorkZoneUnderProjectAction',
	    params:{json:json,portalProjectId:portalProjectId,workZoneId:workZoneId},
	    callback:function(opts,success,response){
	    	if(success){
                var text=response.responseText;
                if(text.indexOf('success')>=0){
                	alert("更新功能能标签成功！")
                	//刷新
                	existsStore.load({
                	  params:{
                	  	portalProjectId:portalProjectId,
                	  	workZoneId:workZoneId
                	  }
                	})
                }else{
                	alert("更新功能能标签失败！")
                }
	   	   }else{
	   	   	  alert("出现未知错误! statusCode =" +response.status);
	   	   }
	    }
		
	});
}


/**
 * 将功能标签从工作空间中删除
 */
function deleteWorkZoneSiteFromWorkZone(){
	
	var portalProjectId=document.getElementById('projectId').value;
	var workZoneId=document.getElementById("workZoneId").value;
	
	
	
	//收集待删除的选中的功能标签
	var workZoneSite_grid = Ext.getCmp("workZoneSite_grid");
	var existsStore = workZoneSite_grid.getStore();
	
	var records=workZoneSite_grid.getSelectionModel().getSelections();
	if(records.length==0){
		alert('请先选择要从工作空间删除的功能标签');
		return;
	}
	
	
	var deletes = [];
	
	for(var i=0;i<records.length;i++){
		deletes.push(records[i].data);
	}
	
	var json=Ext.encode(deletes);
	
	//alert(json);
//	return;
	Ext.Ajax.request({
	  url:'deleteWorkZoneSiteFromWorkZoneUnderProjectAction',
	  params:{json:json},
	  callback:function(opts,success,response){
	  	var text=response.responseText;
	  	if(success){
	  		if(text.indexOf("success")>=0){
	  			alert("从工作空间删除功能标签成功");
	  		}else{
	  			alert("从工作空间删除功能标签失败!");
	  		}
	  		//刷新
                	existsStore.load({
                	  params:{
                	  	portalProjectId:portalProjectId,
                	  	workZoneId:workZoneId
                	  }
                	})
	  	}else{
	  		alert("从工作空间删除功能标签时出现未知错误，导致失败!");
	  	}
	  }
	});
}

Ext.onReady(function(){


//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	

//---------------------门户方案配置------------------------begin--
var portalProject = Ext.data.Record.create([
{
    name: 'projectId',
    type: 'string'
}, {
    name: 'sysconfigId',
    type: 'string'
}, {
    name: 'projectName',
    type: 'string'
},{
    name: 'projectDesc',
    type: 'String'
}
]);


// 	//单选
// 	var portalProject_check_select = new Ext.grid.CheckboxSelectionModel({
// 	  singleSelect:true
// 	});
 
	
    //ColumnModel定义
var portalProject_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),//portalProject_check_select, 
        {
            header: '门户方案ID',
            dataIndex: 'projectId',
            width: 150,
            align: 'center'
        },    	
        {
            header: '系统信息',
            dataIndex: 'sysconfigId',
            width: 150,
            align: 'center',
            renderer:function(value){
				return value;
            }
        }
    	, {
            header: '门户方案名称',
            dataIndex: 'projectName',
            width:200,
            align: 'center'
        },
        {
            header: '门户方案描述',
            dataIndex: 'projectDesc',
            width: 250,
            align: 'center'
        }
    ]
});

// Store定义
var portalProject_dataUrl = 'getAllPortalProjectList.action';
var portalProject_store = new Ext.data.JsonStore({
    url: portalProject_dataUrl,
    root: 'result',
    fields: ['projectId', 'sysconfigId', 'projectName', 'projectDesc']
    //sortInfo: {field:'_order', direction:'ASC'}
});

//GRID定义
var portalProject_grid = new Ext.grid.GridPanel({
	id:'portalProject_grid',
    store: portalProject_store,
    cm: portalProject_cm,
    loadMask: {msg:'正在加载数据……'},
//    sm:portalProject_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
			var record = portalProject_grid.getStore().getAt(rowIndex);
			portalProjectAction(record);
		}
	},
	viewConfig : {
		forceFit : true
	},
    tbar: [
       {
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		portalProject_store.reload();
    	}
   }
    ]
});

portalProject_store.load({});


//---------------------门户方案配置------------------------end--



//---------------------工作空间配置------------------------begin--
var workZone = Ext.data.Record.create([{
    name: 'id',
    type: 'string'
}, {
    name: 'projectId',
    type: 'string'
}, {
    name: 'zoneName',
    type: 'string'
},{
    name: '_order',
    type: 'String'
},{
    name: 'state',
    type: 'String'
},{
	name:'isChecked',
	type:'String'
}
]);


// 	//多选
// 	var workZone_check_select = new Ext.grid.CheckboxSelectionModel({
// 	  singleSelect:true
// 	});
 
	
    //ColumnModel定义
	var workZone_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),//workZone_check_select, 
        {
            header: '工作空间名称',
            dataIndex: 'zoneName',
            width:100,
            align: 'center'
        },
        {
            header: '排序号',
            dataIndex: '_order',
            width: 50,
            align: 'center'
        },
		{
            header: '是否显示',
            dataIndex: 'isChecked',
            width: 50,
            align: 'center',
            renderer:function(value){
            	if(value=="1"){
            		return "显示";            	
            	}else{
					return "不显示";
				}
            }
        }
    ]
});

// Store定义
var workZone_dataUrl = 'getAllPortalProjectWorkZoneList.action';
var workZone_store = new Ext.data.JsonStore({
    url: workZone_dataUrl,
    root: 'result',
    fields: ['id', 'zoneName','workZoneId', 'state','_order','isChecked','projectId'] //原为：['id', 'userId', 'zoneName', 'state','_order','isChecked']  2012-5-15 gmh modify
    //sortInfo: {field:'_order', direction:'ASC'}
});

//GRID定义
var workZone_grid = new Ext.grid.GridPanel({
	id:'workZone_grid',
    store: workZone_store,
    cm: workZone_cm,
    loadMask: {msg:'正在加载数据……'},
//    sm:workZone_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    height:height,
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
			var record = workZone_grid.getStore().getAt(rowIndex);
//			alert(record.data.id);
			workzoneAction(record);
		}
	},
	viewConfig : {
		forceFit : true
	},
    tbar: [
    {
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		workZone_store.reload();
    	}
   }
    ]
});

// manually trigger the data store load
//workZone_store.load({});

/**
 * 点击工作空间时的响应
 */
function workzoneAction(record){	
	var WorkZoneId = record.data.workZoneId;	
	var zoneName  =record.data.zoneName;
	
			// 选中，更新hidden的workzone的id，刷新其下的标签
		document.getElementById("workZoneId").value = WorkZoneId;
		var projectId = document.getElementById("projectId").value;
		Ext.getCmp('south-panel').setTitle('[' + zoneName + ']:工作空间标签配置');
		var projectId = document.getElementById("projectId").value;
		workZoneSite_store.load({
					params : {
						portalProjectId : projectId,
						workZoneId : WorkZoneId
					}
				});
}

//---------------------工作空间配置------------------------end--



//---------------------门户方案下的工作空间下的标签栏配置------------------------begin--
	var workZoneSite = Ext.data.Record.create([{
        name: 'id',
        type: 'string'
    }, {
        name: 'projectId',
        type: 'string'
    },
    {
       name:'workZoneSiteId',
       type:'string'
    },
    {
        name: 'workItemName',
        type: 'string'
    }, {
        name: 'type',
        type: 'string'
    },
     {
        name: 'layout',
        type: 'string'
    },   
     {
        name: 'enabled',
        type: 'string'
    },
    {
        name: '_order',
        type: 'String'
    }]);
	
	//更新插件
 	var workZoneSite_editor = new Ext.ux.grid.RowEditor({
 		id:'workZoneSite_editor',
        saveText: '保存',
        cancelText: '取消',
        clicksToEdit:3,
        listeners: { 
 			beforeedit:function(RowEditor){
 			}, 		
 			//保存
	        afteredit:function (r, o, record,n){
				var grid = this.grid;
				var store = grid.getStore();
                var data = record.data;  
                var json = Ext.encode(data);
               	//alert(Ext.encode(json));
				
				//alert(Ext.encode(store.json));
				//AJAX保存
				Ext.Ajax.request({
				   url:'',// 'saveUserworkzonesiteConfig.action',
				   params: { userWorkZonesiteConfig:json },
				   success: function(data){
					   	workZoneSite_store.reload();
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		//var index = parseInt(workZoneSite_store.getCount())-1;
				   		workZoneSite_check_select.selectLastRow();
   						var record = workZoneSite_grid.getSelectionModel().getSelected();
   						//alert(record.data.id);
				   		workzonesitelayoutAction(record);					   

				   },
				   failure:function(){
				   
				   }
				});

	        },
	        //取消
	        canceledit: function(RowEditor){  
	          if (RowEditor.record.phantom) {        	  
	          	this.grid.getStore().removeAt(RowEditor.rowIndex);  
	          }  

	        }
 		}
    });
	
 	//多选
 	var workZoneSite_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
	var workZoneSite_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),workZoneSite_check_select, 
            {
                header: '标签栏名称',
                dataIndex: 'workItemName',
                width: 130,
                align: 'center'		
            }
        	, {
                header: '布局类型',
                dataIndex: 'type',
                width: 100,
                align: 'center',
                renderer:function(value){
        			if(value=="1"){
        				return "门户组件类型";
        			}else if(value=="2"){
        				return "应用框架类型";
        			}else{
        				return "请选择";
        			}
        		
                },
                editor: new fm.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'layout',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
                })
            }
            , 
            	{
                header: '排序号',
                dataIndex: '_order',
                width: 150,
                align: 'center',
            	renderer:function(value){
	            	var reuturnText = "";
	            	reuturnText +="<select onchange='workZoneSiteOrderOnChange(this.value);'>";
	            	reuturnText +="<option value=''>请选择排序号</option>";
	            	for(var i=0;i<=20;i++){
	            		if(value==i){
	            			reuturnText +="<option value='"+i+"' selected>"+i+"</option>";
	            		}else{
	            			reuturnText +="<option value='"+i+"'>"+i+"</option>";
	            		}
	            	}
	            	reuturnText +="</select>";
	            	return reuturnText;
	            }
            }
//            ,
//	        {
//	            header: '是否选入方案',
//	            dataIndex: 'isChecked',
//	            width: 100,
//	            align: 'center',
//	            renderer:function(value){
//	            	if(value=="1"){
//	            		return '<input type="checkBox" value="1" checked/ onclick="workZoneSiteIsCheckedOnClick(this.checked);"/>';            	
//	            	}else{
//						return '<input type="checkBox" value="0" onclick="workZoneSiteIsCheckedOnClick(this.checked);"/>';
//					}
//	            }
//	        },
//	        {
//	            header: '操作',
//	            dataIndex: 'id',
//	            width: 100,
//	            align: 'center',
//	            renderer:function(value){
//					return '<a href="javascript:workZoneSiteEditAction()">编辑</a>';
//	            }
//	        }
        ]
    });

	// Store定义
	var workZoneSite_dataUrl = 'getWorkZoneSitesUnderPortalProjectAndWorkZoneAction';
	var workZoneSite_store = new Ext.data.JsonStore({
	    url: workZoneSite_dataUrl,
	    root: 'result',
	    fields: ['projectId','workItemName', 'type','layout','_order','id','workZoneSiteId','workZoneId','enabled']
	    //sortInfo: {field:'_order', direction:'ASC'}
	    	
	    	
	});
	
    //GRID定义
    var workZoneSite_grid = new Ext.grid.GridPanel({
    	id:'workZoneSite_grid',
        store: workZoneSite_store,
        cm: workZoneSite_cm,
        loadMask: {msg:'正在加载数据……'},
        sm:workZoneSite_check_select,
//        width: 750,
        border:false,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [workZoneSite_editor],
        // specify the check column plugin on the grid so the plugin is initialized
        listeners: { 
    		//行单击
    		rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    			//alert(record.get('id'));
    			//workzonesitelayoutAction(record);
    	
    		}
    	},
		viewConfig : {
			forceFit : true
		},
        tbar: [
    	{
            text: '保存配置',
            iconCls:'icon-save',
            handler : function(){
				workZoneSiteOnSave();
            }
        },
        {
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		workZoneSite_store.reload();
	    	}
   		}
   		,
   		 {
			text: '从工作空间删除该功能标签',
	        iconCls:'silk-delete',
	        handler : function(){
	    		deleteWorkZoneSiteFromWorkZone();
	    	}
   		}
        ]
    });

    

//---------------------门户方案下的工作空间下的标签栏配置------------------------end--





//2012-5-16 gmh add begin

    
//显示所有的门户方案下的功能标签列表，供选入某个门户方案的某个工作空间下    
    
//2012-5-16 gmh add end

var  allWorkZoneSite_check_select = new Ext.grid.CheckboxSelectionModel();
    
var allWorkZoneSite_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),workZoneSite_check_select, 
            {
                header: '功能标签名称',
                dataIndex: 'workItemName',
                width: 130,
                align: 'center'		
            },
            {
                
                header: '标签类型',
                dataIndex: 'type',
                width: 130,
                align: 'center'	,
                renderer:function(value){
                	 if(value==1){
                	 	 return "门户组件类型";
                	 }else if(value==2){
                	    return "应用框架类型";
                	 }
                }
            }
            ,
            {
                
                header: '标签布局详情',
                dataIndex: 'layout',
                width: 130,
                align: 'center'	,
                renderer:function(value){
                	 var idx=value.indexOf("{");
                	 if(idx>0){
                	     var num=value.substring(0,idx);
                	     return num+" 列布局";
                	 }else{
                	    return "无";	
                	 }
                }
            }
        ]
    });
     
    var allWorkZoneSite_store=new Ext.data.JsonStore({
        url:'getAllPortalProjectWorkZoneSiteListAction',
        root: 'result',
        fields:['id','workItemName','type','layout','projectId','workZoneSiteId']
//        autoLoad:true
    });
    var allWorkZoneSite_grid=new Ext.grid.GridPanel({
       id:'allWorkZoneSite_grid',
        store: allWorkZoneSite_store,
        cm: allWorkZoneSite_cm,
        loadMask: {msg:'正在加载数据……'},
        sm:allWorkZoneSite_check_select,
        //width: 300,
        border:false,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
        autoScroll:true,
        header:false,
        frame: false,
        // specify the check column plugin on the grid so the plugin is initialized
        listeners: { 
    		//行单击
    		rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    			//alert(record.get('id'));
    			//workzonesitelayoutAction(record);
    	
    		}
    	},
		viewConfig : {
			forceFit : true
		},
        tbar: [
    	{
            text: '添加进门户方案下的工作空间配置',
            iconCls:'silk-add',
            handler : function(){
				addWorkZoneSiteToPortalProjectWorkZone();//添加进门户方案
            }
        },
        {
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	        	//获取门户方案id
	        	var portalProjectId=document.getElementById("projectId").value;
	        	var workZoneId=document.getElementById("workZoneId").value;
	    		allWorkZoneSite_store.reload({
	    		   params:{
	    		   	  portalProjectId:portalProjectId,
	    		   	  workZoneId:workZoneId
	    		   }
	    		});
	    	}
   		}
        ]
    });





 
 
 



//---------用户工作空间相关配置--begin--------
var userConfigPanel = new Ext.Panel({
    layout:'border',
    height:height,
    items:[
	    	{
	        region:'north',
	        id:'north-panel',
	        iconCls:'icon-cog_edit',
	        title:'门户方案列表',
	        split:false,
	        //frame:true,
	        //border:false,
	        height: 200,
	        collapsible:true,
	        layout:'fit',
	        items:[portalProject_grid]
	       
	       }
    ,
	    	{
	        region:'center',
	        id:'center-panel',
	        iconCls:'icon-cog_edit',
	        title:'工作空间列表',
	        layout:'border',
	        //autoScroll:true,
	        collapsible:true,
	        split:false,
	        border:true,
	        frame:true,
	        items : [
	          {
	          	region:'west',
	          	title:'门户方案已有工作空间',
	          	margins: '0 10px 0 0',
	          	width:500,
	          	height:500,
	          	layout:'fit',
	          	autoScroll:true,
	          	items:[workZone_grid]
	          },
	          {
	            region:'center',
	            title:'工作空间下的功能标签',
	            autoScroll:true,
	            items:[workZoneSite_grid]
	          }
	         ]
						
	    },
		{
	        region:'south',
	        id:'south-panel',
	        iconCls:'icon-cog_edit',
	        height:200,
	        collapsible:true,
	        layout:'border',
	        split:false,
	        //border:false,
	        //frame:true,
	        title:'功能标签列表',
	        items:[
	                {
	                  title:'已存在门户方案的功能标签',
	                  region:'center',
	                  margins: '0 10px 0 0',
	          	      width:800,
	          	      height:500,
	          	      autoScroll:true,
	          	      layout:'fit',
	          	      items:[allWorkZoneSite_grid]
	                }
	        ]//gmh 2012-5-16 add allWorkZoneSite_grid
	    }
	    
    
    ]
	
});
//---------用户工作空间相关配置--end--------
 
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'center', 
    		layout:'fit',
    		items:[
    			userConfigPanel
    		]
    	}
    	
    ]
});
    
    
});