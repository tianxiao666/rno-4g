Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

function portalProjectAction(record){	
	var projectId = record.data.projectId;	
	var projectName  =record.data.projectName;
	Ext.getCmp('center-panel').setTitle('['+projectName+']:工作空间配置');
	document.getElementById("projectId").value =  projectId;
	
	var workZone_grid = Ext.getCmp('workZone_grid');
   	var workZone_store=workZone_grid.getStore();
	workZone_store.load({params:{portalProjectId:projectId}});
	
	//2012-5-16 gmh add	
	Ext.getCmp('south-panel').setTitle("["+projectName+"]:功能标签配置")
	var workZoneSite_grid = Ext.getCmp('workZoneSite_grid');
   	var workZoneSite_store=workZoneSite_grid.getStore();
	workZoneSite_store.load({params:{portalProjectId:projectId}});
}

//编辑门户方案
function portalProjectEditAction(){
	var grid = Ext.getCmp('portalProject_grid');
	var portalProject_editor = Ext.getCmp('portalProject_editor');
	var record = grid.getSelectionModel().getSelected();
	var index = grid.store.indexOf(record);
	//alert(portalProject_editor);
	portalProject_editor.startEditing(index);
}


//编辑工作空间
function workZoneEditAction(){
	var grid = Ext.getCmp('workZone_grid');
	var workZone_editor = Ext.getCmp('workZone_editor');
	var record = grid.getSelectionModel().getSelected();
	var index = grid.store.indexOf(record);
	alert(portalProject_editor);	workZone_editor.startEditing(index);
}

//编辑工作空间标签
function workZoneSiteEditAction(){
	var grid = Ext.getCmp('workZoneSite_grid');
	var workZoneSite_editor = Ext.getCmp('workZoneSite_editor');
	var record = grid.getSelectionModel().getSelected();
	var index = grid.store.indexOf(record);
	workZoneSite_editor.startEditing(index);
}





function workZoneOrderOnChange(value){
	var grid = Ext.getCmp('workZone_grid');
	var workZone_editor = Ext.getCmp('workZone_editor');
	var record = grid.getSelectionModel().getSelected();
	record.data._order = value;
}

function workZoneIsCheckedOnClick(value){
	var grid = Ext.getCmp('workZone_grid');
	var workZone_editor = Ext.getCmp('workZone_editor');
	var record = grid.getSelectionModel().getSelected();
	var isChecked=0;
	if(value==true){
		isChecked=1;
	}else{
		isChecked=0;
	}
	record.data.isChecked = isChecked;
}

function workZoneOnSave(){
	var grid = Ext.getCmp('workZone_grid');
	var store = grid.getStore();
	var lstAddRecord=new Array(); 	
    store.each(function(record) { 
    	lstAddRecord.push(record.data);    
    });    
    var json = Ext.encode(lstAddRecord);
    var portalProjectId = document.getElementById("projectId").value;
	//AJAX保存
	Ext.Ajax.request({
	   url: 'savePortalProjectWorkZone.action',
	   params: { json:json,portalProjectId:portalProjectId},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '保存成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			var portalProject_grid = Ext.getCmp('portalProject_grid');
   			var record = portalProject_grid.getSelectionModel().getSelected();
   			portalProjectAction(record);
	   },
	   failure:function(){
	   
	   }
	});
}




function workZoneSiteOrderOnChange(value){
	var grid = Ext.getCmp('workZoneSite_grid');
	var record = grid.getSelectionModel().getSelected();
	record.data._order = value;
}


function workZoneSiteIsCheckedOnClick(value){
	var grid = Ext.getCmp('workZoneSite_grid');
	var record = grid.getSelectionModel().getSelected();
	var isChecked=0;
	if(value==true){
		isChecked=1;
	}else{
		isChecked=0;
	}
	record.data.enabled = isChecked;
}


function workZoneSiteOnSave(){
	var grid = Ext.getCmp('workZoneSite_grid');
	var store = grid.getStore();
	var lstAddRecord=new Array(); 	
    store.each(function(record) { 
    	lstAddRecord.push(record.data);    
    });    
    var json = Ext.encode(lstAddRecord);
    var portalProjectId = document.getElementById("projectId").value;
	//AJAX保存
	Ext.Ajax.request({
	   url: 'savePortalProjectWorkZoneSite.action',
	   params: { json:json,portalProjectId:portalProjectId},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '保存成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			var workZone_grid = Ext.getCmp('workZone_grid');
   			var record = workZone_grid.getSelectionModel().getSelected();
   			//workzoneAction(record);
	   },
	   failure:function(){
	   
	   }
	});
}


//2012-5-15 gmh add
function deleteWorkzoneFromScheme(){
	var grid = Ext.getCmp('workZone_grid');
	var records=grid.getSelectionModel().getSelections();
	
	//alert(records.length);
	
	if(records.length==0){
		return;
	}
	var msg="";
	var selectRecords=new Array(); 	
	for(var i=0;i<records.length;i++){
		var rec=records[i].data;
//		for(var k in rec){
//			msg+=k+"="+rec[k]+",";
//		}
//		msg+="<br/>";
		
		selectRecords.push(rec);
	}
	
	
    var json = Ext.encode(selectRecords);
//	Ext.Msg.alert("select",json);
	
    var portalProjectId = document.getElementById("projectId").value;
	//AJAX保存
	Ext.Ajax.request({
	   url: 'deleteWorkZoneFromPortalProjectAction.action',
	   params: { json:json},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '删除成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			
			var workZone_grid = Ext.getCmp('workZone_grid');
   			var workZone_store=workZone_grid.getStore();
   			
			workZone_store.load({
			  params:{
			     portalProjectId:portalProjectId 
			  }
			});
	   },
	   failure:function(){
	   
	   }
	});
}



function  addWorkZoneToPortalProject(){
	//是否已经选择了某个工作空间
	var portalProjectId = document.getElementById("projectId").value;
    
    if(!portalProjectId){
    	return;
    }
    
    
	var grid = Ext.getCmp('allWorkZone_grid');
	var records=grid.getSelectionModel().getSelections();
	
	var msg="";
	var selectRecords=new Array(); 	
	for(var i=0;i<records.length;i++){
		var rec=records[i].data;
		for(var k in rec){
			msg+=k+"="+rec[k]+",";
		}
		msg+="<br/>";
		
		selectRecords.push(rec);
	}
	
	
    var json = Ext.encode(selectRecords);
//	Ext.Msg.alert("select",json);
	
   
    
    //AJAX保存
	Ext.Ajax.request({
	   url: 'addWorkZoneToPortalProjectAction.action',
	   params: { json:json,portalProjectId:portalProjectId},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '添加成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			
			var workZone_grid = Ext.getCmp('workZone_grid');
   			var workZone_store=workZone_grid.getStore();
   			
			workZone_store.load({
			  params:{
			     portalProjectId:portalProjectId 
			  }
			});
	   },
	   failure:function(){
	   
	   }
	});
}

/**
 * 从门户方案删除功能标签
 * 2012-5-16 gmh add
 */
function deleteWorkZoneSiteFromScheme(){
	
	//是否已经选择了某个工作空间
	var portalProjectId = document.getElementById("projectId").value;
    
    if(!portalProjectId){
    	return;
    }
    
    
	var grid = Ext.getCmp('workZoneSite_grid');
	var records=grid.getSelectionModel().getSelections();
	
	var msg="";
	var selectRecords=new Array(); 	
	for(var i=0;i<records.length;i++){
		var rec=records[i].data;
		for(var k in rec){
			msg+=k+"="+rec[k]+",";
		}
		msg+="<br/>";
		
		selectRecords.push(rec);
	}
	
	
    var json = Ext.encode(selectRecords);
	//Ext.Msg.alert("select",json);
	
   
    
    //AJAX保存
	Ext.Ajax.request({
	   url: 'deleteWorkZoneSiteFromPortalProjectAction.action',
	   params: { json:json},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '删除成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			var workZoneSite_grid=Ext.getCmp('workZoneSite_grid');
   			var workZoneSite_store=workZoneSite_grid.getStore();
   			
			workZoneSite_store.load({
			  params:{
			     portalProjectId:portalProjectId 
			  }
			});
	   },
	   failure:function(){
	   
	   }
	});
}

/**
 * 给门户方案添加功能标签
 * 2012-5-16 gmh add
 */
function addWorkZoneSiteToPortalProject(){
	//是否已经选择了某个工作空间
	var portalProjectId = document.getElementById("projectId").value;
    
    if(!portalProjectId){
    	return;
    }
    
    
	var grid = Ext.getCmp('allWorkZoneSite_grid');
	var records=grid.getSelectionModel().getSelections();
	
	var msg="";
	var selectRecords=new Array(); 	
	for(var i=0;i<records.length;i++){
		var rec=records[i].data;
		for(var k in rec){
			msg+=k+"="+rec[k]+",";
		}
		msg+="<br/>";
		
		selectRecords.push(rec);
	}
	
	
    var json = Ext.encode(selectRecords);
//	Ext.Msg.alert("select",json);
	
   
    
    //AJAX保存
	Ext.Ajax.request({
	   url: 'addWorkZoneSiteToPortalProjectAction.action',
	   params: { json:json,portalProjectId:portalProjectId},
	   success: function(){
   			var msg = Ext.MessageBox.alert('提示', '添加成功!');		
			setTimeout(function(){Ext.MessageBox.hide()},1000);
			
			var workZoneSite_grid = Ext.getCmp('workZoneSite_grid');
   			var workZoneSite_store=workZoneSite_grid.getStore();
   			
			workZoneSite_store.load({
			  params:{
			     portalProjectId:portalProjectId 
			  }
			});
	   },
	   failure:function(){
	   
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

//更新插件
 	var portalProject_editor = new Ext.ux.grid.RowEditor({
 		id:'portalProject_editor',
        saveText: '保存',
    	cancelText: '取消',
    	clicksToEdit:3,
        listeners: { 
 			beforeedit:function(RowEditor){
 				//alert(RowEditor.record);
 				//return false; 		
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
			   url: 'savePortalProjectAction.action',
			   params: { json:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
			   		var record = portalProject_grid.getSelectionModel().getSelected();
			   		workzoneAction(record);
				   },
				   failure:function(){
				   
				   }
				});
	        },
	        canceledit: function(RowEditor){ 	        
	          if (RowEditor.record.data.projectId==""&&RowEditor.record.phantom) {        	  
	          	this.grid.getStore().removeAt(RowEditor.rowIndex);  
	          }  
	        }

 		}
    });
	
 	//多选
 	var portalProject_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
var portalProject_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),portalProject_check_select, 
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
            },
            editor: new fm.TextField({
                allowBlank: false
            })
        }
    	, {
            header: '门户方案名称',
            dataIndex: 'projectName',
            width:200,
            align: 'center',
            editor: new fm.TextField({
                allowBlank: false,
                selectOnFocus:true
            })
        },
        {
            header: '门户方案描述',
            dataIndex: 'projectDesc',
            width: 250,
            align: 'center',
            editor: new fm.TextField({
                allowBlank: false
            })
        }
        //2-12-5-18 此处只是引用，不给更改
//        ,
//        {
//            header: '操作',
//            dataIndex: 'projectId',
//            width: 100,
//            align: 'center',
//            renderer:function(value){
//				return '<a href="javascript:portalProjectEditAction()">编辑</a>';
//            }
//        }
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
    sm:portalProject_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    plugins: [portalProject_editor],
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
    //2012-5-18 gmd delete  对门户的增、删、改操作放在 门户方案配置项，这里只负责使用
//	{
//        text: '增加门户方案',
//        iconCls:'silk-add',
//        handler : function(){
//            // access the Record constructor through the grid's store
//            var count = portalProject_grid.getStore().getCount();
//            var lastRecord = portalProject_grid.getStore().getAt(count-1);
//            
//            var p = new portalProject({
//            	projectId:'',
//                sysconfigId: '',
//                projectName: '请输入门户方案名称',
//                projectDesc: ''
//            });
//            portalProject_editor.stopEditing();
//            portalProject_store.insert(count, p);
//            portalProject_grid.getView().refresh();
//            portalProject_grid.getSelectionModel().selectRow(count);
//            portalProject_editor.startEditing(count);             
//        }
//    },
//	{
//        text: '删除门户方案',
//        iconCls:'silk-delete',
//        handler : function(){
//    	
//			var ss = portalProject_grid.getSelectionModel().getSelections();
//			var countt = portalProject_grid.getSelectionModel().getCount();
//			
//			if(countt==0){
//				alert("请选择要删除的对象!");
//				return false;        			
//			}
//			var ids  ="";
//			for(var i=0;i<countt;i++){
//				record  = ss[i];
//				ids+=record.data.projectId+";";
//			}
//			
//			if(!confirm("确定要删除选定的对象?")){
//				return false;
//			}   
//			
//			
//			
//			//AJAX删除
//			Ext.Ajax.request({
//			   url: 'deletePortalProjectAction.action',
//			   params: { json:ids},
//			   success: function(){
//			   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
//					setTimeout(function(){Ext.MessageBox.hide()},1000);
//			   		portalProject_store.reload();
//			   },
//			   failure:function(){
//			   
//			   }
//			});
//		}
//    },
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

//更新插件
 	var workZone_editor = new Ext.ux.grid.RowEditor({
 		id:'workZone_editor',
        saveText: '保存',
    	cancelText: '取消',
    	clicksToEdit:3,
        listeners: { 
 			beforeedit:function(RowEditor){
 				//alert(RowEditor.record);
 				//return false; 		
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
			   url: 'saveUserworkzoneConfig.action',
			   params: { userWorkZoneConfig:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
			   		var record = workZone_grid.getSelectionModel().getSelected();
			   		workzoneAction(record);
				   },
				   failure:function(){
				   
				   }
				});
	        },
	        canceledit: function(RowEditor){  
	          if (RowEditor.record.phantom) {        	  
	          	this.grid.getStore().removeAt(RowEditor.rowIndex);  
	          }  
	        }

 		}
    });
	
 	//多选
 	var workZone_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
	var workZone_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),workZone_check_select, 
//        {
//            header: '是否显示',
//            dataIndex: 'state',
//            width: 65,
//            align: 'center',
//            renderer:function(value){
//    			if(value=="1"){
//    				return "显示";
//    			}else{
//    				return "不显示";
//    			}    		
//            }
//        }, 
        {
            header: '工作空间名称',
            dataIndex: 'zoneName',
            width:100,
            align: 'center'
        },
//        {
//            header: '用户ID',
//            dataIndex: 'userId',
//            width: 70,
//            align: 'center'
//        },
        {
            header: '排序号',
            dataIndex: '_order',
            width: 50,
            align: 'center',
            editor: new Ext.ux.form.SpinnerField({
                allowBlank: false
            }),
            renderer:function(value){
            	var reuturnText = "";
            	reuturnText +="<select onchange='workZoneOrderOnChange(this.value);'>";
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
        },
		{
            header: '是否显示',//原为 '是否选入方案',
            dataIndex: 'isChecked',
            width: 100,
            align: 'center',
            renderer:function(value){
            	if(value=="1"){
            		return '<input type="checkBox" value="1" checked/ onclick="workZoneIsCheckedOnClick(this.checked);"/>';            	
            	}else{
					return '<input type="checkBox" value="0" onclick="workZoneIsCheckedOnClick(this.checked);"/>';
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
    sm:workZone_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    height:height,
    plugins: [workZone_editor],
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
			//var record = workZone_grid.getStore().getAt(rowIndex);
			//workzoneAction(record);
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
			workZoneOnSave();
        }
    },
    {
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		workZone_store.reload();
    	}
   },{//2012-5-15 gmh add
   	text:'从方案删除选定的工作空间',
   	iconCls:'silk-delete',
   	handler:function(){
   		deleteWorkzoneFromScheme();
   	}
   }
    ]
});

// manually trigger the data store load
//workZone_store.load({});


function workzoneAction(record){	
	var WorkZoneId = record.data.id;	
	var zoneName  =record.data.zoneName;
	Ext.getCmp('south-panel').setTitle('['+zoneName+']:工作空间标签配置');
	var projectId = document.getElementById("projectId").value;
	workZoneSite_store.load({params:{portalProjectId:projectId,workZoneId:WorkZoneId}});

}

//---------------------工作空间配置------------------------end--







//---------------------工作空间标签栏配置------------------------begin--
	var workZoneSite = Ext.data.Record.create([{
        name: 'id',
        type: 'string'
    }, {
        name: 'userWorkZoneId',
        type: 'string'
    }, {
        name: 'workItemName',
        type: 'string'
    }, {
        name: 'type',
        type: 'string'
    },
    {
        name: 'enabled',
        type: 'string'
    },
     {
        name: 'layout',
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
				   url: 'saveUserworkzonesiteConfig.action',
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
//            , 
//            	{
//                header: '排序号',
//                dataIndex: '_order',
//                width: 150,
//                align: 'center',
//            	renderer:function(value){
//	            	var reuturnText = "";
//	            	reuturnText +="<select onchange='workZoneSiteOrderOnChange(this.value);'>";
//	            	reuturnText +="<option value=''>请选择排序号</option>";
//	            	for(var i=0;i<=20;i++){
//	            		if(value==i){
//	            			reuturnText +="<option value='"+i+"' selected>"+i+"</option>";
//	            		}else{
//	            			reuturnText +="<option value='"+i+"'>"+i+"</option>";
//	            		}
//	            	}
//	            	reuturnText +="</select>";
//	            	return reuturnText;
//	            }
//            }
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
	var workZoneSite_dataUrl = 'getAllPortalProjectWorkZoneSiteListAction.action'; //2012-5-16 gmd modify原为'getAllPortalProjectWorkZoneSiteList.action';
	var workZoneSite_store = new Ext.data.JsonStore({
	    url: workZoneSite_dataUrl,
	    root: 'result',
	    fields: ['id', 'projectId','workItemName','workZoneSiteId', 'type','layout','enabled','_order']
	    //sortInfo: {field:'_order', direction:'ASC'}
	    	
	    	
	});
	
    //GRID定义
    var workZoneSite_grid = new Ext.grid.GridPanel({
    	id:'workZoneSite_grid',
        store: workZoneSite_store,
        cm: workZoneSite_cm,
        loadMask: {msg:'正在加载数据……'},
        sm:workZoneSite_check_select,
        width: 750,
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
			text: '从方案删除该功能标签',
	        iconCls:'silk-delete',
	        handler : function(){
	    		deleteWorkZoneSiteFromScheme();
	    	}
   		}
        ]
    });

    // manually trigger the data store load
    //workZoneSite_store.load({baseParams:{'WorkZoneId':''}});

    workZoneSite_store.on('load',function(){

    })
    
    

//---------------------工作空间标签栏配置------------------------begin--





//2012-5-16 gmh add begin
//显示所有的工作空间列表，供选入某个门户方案

    //ColumnModel定义
    var  allWorkZone_check_select = new Ext.grid.CheckboxSelectionModel();
	var allWorkZone_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),workZoneSite_check_select, 
            {
                header: '工作空间名称',
                dataIndex: 'zoneName',
                width: 130,
                align: 'center'		
            }
        ]
    });
     
    var allWorkZone_store=new Ext.data.JsonStore({
        url:'getAllWorkZoneListAction',
        root: 'result',
        fields:['id','zoneName','state','_order'],
        autoLoad:true
    });
    var allWorkZone_grid=new Ext.grid.GridPanel({
       id:'allWorkZone_grid',
        store: allWorkZone_store,
        cm: allWorkZone_cm,
        loadMask: {msg:'正在加载数据……'},
        sm:allWorkZone_check_select,
        //width: 300,
        border:false,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
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
            text: '添加进门户方案配置',
            iconCls:'silk-add',
            handler : function(){
				addWorkZoneToPortalProject();//添加进门户方案
            }
        },
        {
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		allWorkZone_store.reload();
	    	}
   		}
        ]
    });
    
    
    
//显示所有的功能标签列表，供选入某个门户方案的某个某个工作空间下    
    
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
        url:'getAllWorkZoneSiteListAction',
        root: 'result',
        fields:['id','workItemName','type','layout'],
        autoLoad:true
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
            text: '添加进门户方案配置',
            iconCls:'silk-add',
            handler : function(){
				addWorkZoneSiteToPortalProject();//添加进门户方案
            }
        },
        {
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		allWorkZoneSite_store.reload();
	    	}
   		}
        ]
    });





 
 
 



//---------用户工作空间相关配置--begin--------
var userConfigPanel = new Ext.Panel({
    layout:'border',
    height:height,
    items:[{
        region:'north',
        id:'north-panel',
        iconCls:'icon-cog_edit',
        title:'门户方案配置',
        split:false,
        //frame:true,
        //border:false,
        height: 200,
        collapsible:true,
        layout:'fit',
        items:[portalProject_grid]
       
    },{
        region:'center',
        id:'center-panel',
        iconCls:'icon-cog_edit',
        title:'工作空间配置',
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
            title:'所有工作空间',
            autoScroll:true,
            items:[allWorkZone_grid]
          }
         ]
					
    },{
        region:'south',
        id:'south-panel',
        iconCls:'icon-cog_edit',
        height:200,
        collapsible:true,
        layout:'border',
        split:false,
        //border:false,
        //frame:true,
        title:'功能标签配置',
        items:[
                {
                  title:'已存在门户方案的功能标签',
                  region:'west',
                  margins: '0 10px 0 0',
          	      width:500,
          	      height:500,
          	      autoScroll:true,
          	      layout:'fit',
          	      items:[workZoneSite_grid]
                }
                ,
                {
                 title:'所有的功能标签',
                 region:'center',
                 autoScroll:true,
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