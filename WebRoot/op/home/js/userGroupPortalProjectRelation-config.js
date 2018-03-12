Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

/**
 * 添加门户方案到用户群
 */
function addPortalSchemeToUserGroup(){
//	console.log("addPortalSchemeToUserGroup");
	
	var userGroupId=document.getElementById("userGroupId").value;
	var portalProjectId=document.getElementById("portalProjectId").value;
	
	if(userGroupId=='' || portalProjectId==''){
		var msg = Ext.MessageBox.alert('提示', '请先选择一个用户群以及要关联的门户方案!');		
//		setTimeout(function(){Ext.MessageBox.hide()},1000);
		return;
	}
	
	var userGroupId=document.getElementById("userGroupId").value;

	var userGroupAssPortalProject_grid=Ext.getCmp('userGroupAssPortalProject_grid');
	var userGroupAssPortalProject_store=userGroupAssPortalProject_grid.getStore();
	
//	console.log(userGroupAssPortalProject_store);
	//是否已经存在了关联
	var exist=false;
	userGroupAssPortalProject_store.each(function(record){
	    if(record.data.userGroupId==userGroupId && record.data.projectId==portalProjectId){
	    	Ext.MessageBox.alert("提示","已存在此关联");
	    	exist=true;
	    	return;
	    }
	});
	
	if(exist){
		return;
	}
	
	//添加门户方案到用户群的关联
	var addAssUrl="addPortalSchemeToUserGroupAction";
	var json={'userGroupId':userGroupId,'portalProjectId':portalProjectId};
	
	Ext.Ajax.request({
	   url:addAssUrl,
	   params:json,
	   callback :function(opts,success,response){
//	   	 	console.log(response.responseText);
	   	    var text=response.responseText;
	   	   if(success){
	   	   	   if(text.indexOf('success')>=0){
	   	   	  
	   	   	    Ext.MessageBox.alert('提示', '保存用户群与门户方案的关联成功!');		
//		        setTimeout(function(){Ext.MessageBox.hide()},1000);
	   	   	   }else{
	   	   	   	Ext.MessageBox.alert('提示', '保存用户群与门户方案的关联失败!详情：<br/>'+text.substring(text.indexOf(':')+1));		
//		        setTimeout(function(){Ext.MessageBox.hide()},1000);
	   	   	   }
	   	   }else{
	   	   	    Ext.MessageBox.alert('提示', '保存用户群与门户方案的关联发生未知错误!详情：<br/>'+text.substring(text.indexOf(':')+1));		
//		        setTimeout(function(){Ext.MessageBox.hide()},1000);
	   	   }
	   	   
	   	   //重新加载与用户群关联的门户方案面板的内容
	       //加载关联的门户方案的情况
	      userGroupAssPortalProject_store.load({params:{'userGroupId':userGroupId}});
	   }
	});
	
	
}

/**
 * 处理单击用户群事件
 * @param {} record
 */
function  dealUserGroupClick(record){
	
	//判断是否处于选中状态
	var userGroup_grid=Ext.getCmp('userGroup_grid');
	var userGroup_columnModel=userGroup_grid.getColumnModel();
//	console.log(userGroup_columnModel);
//	console.log("select userGroup count:"+userGroup_columnModel.getColumnById('userGroup_selectModel').getCount());
	
	var count=userGroup_columnModel.getColumnById('userGroup_selectModel').getCount();
	if(count==1){//有选中
     	document.getElementById("userGroupId").value=record.userGroupId;
	}else{//未选中
		document.getElementById("userGroupId").value="";
	}
	
	//加载关联的门户方案的情况
	var userGroupId=document.getElementById("userGroupId").value;
	var userGroupAssPortalProject_grid = Ext
				.getCmp('userGroupAssPortalProject_grid');
	if (userGroupId == '') {
        userGroupAssPortalProject_grid.getStore().removeAll();
	} else {
		userGroupAssPortalProject_grid.getStore().load({
					params : {
						'userGroupId' : userGroupId
					}
				});
	}
	
}

/**
 * 处理门户方案的点击事件
 * @param {} data
 */
function dealPortalProjectClick(data){
	//判断是否处于选中状态
	var portalProject_grid=Ext.getCmp('portalProject_grid');
	var portalProject_columnModel=portalProject_grid.getColumnModel();
	
	var count=portalProject_columnModel.getColumnById('portalProject_check_select').getCount();
	if(count==1){//有选中
     	document.getElementById("portalProjectId").value=data.projectId;
	}else{//未选中
		document.getElementById("portalProjectId").value="";
	}
	
//	console.log("portal project count:"+count);
}

/**
 * 删除门户方案与用户群的关联
 */
function deletePortalProjectFromUserGroup(){
	var userGroupAssPortalProject_grid=Ext.getCmp('userGroupAssPortalProject_grid');
	var tstore=userGroupAssPortalProject_grid.getStore();
	
	if(userGroupAssPortalProject_grid.getSelectionModel().getCount()>0){
	   var record = userGroupAssPortalProject_grid.getSelectionModel()
				.getSelected();
		var json={'userGroupId':record.data.userGroupId,'portalProjectId':record.data.projectId};
		
		Ext.Ajax.request({
		  url:'deletePortalSchemeFromUserGroupAction',
		  params:json,
		  callback:function(opt,success,response){
		  	var text=response.responseText;
		  	 if(success){
                   if(text.indexOf('success')>=0){
                   	  Ext.Msg.alert('提示','用户群和门户方案的关系删除成功');
//                   	alert('用户群和门户方案的关系删除成功');
                   }else{
                   	  Ext.Msg.alert('提示','用户群和门户方案的关系删除失败');
//                   	  alert('用户群和门户方案的关系删除失败');
                   }
		  	 }else{
		  	 	  Ext.Msg.alert('提示','删除用户群和门户方案的关系时出现未知错误！');
//		  	 	  alert('删除用户群和门户方案的关系时出现未知错误！');
		  	 }
		  	 
		  	 //刷新
		  	 	var userGroupId = document.getElementById("userGroupId").value;

				var userGroupAssPortalProject_grid = Ext
						.getCmp('userGroupAssPortalProject_grid');
				var userGroupAssPortalProject_store = userGroupAssPortalProject_grid
						.getStore();
				userGroupAssPortalProject_store.load({
							params : {
								'userGroupId' : userGroupId
							}
						});
		  }
		});
		

	}
	
}


Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	



//----用户群展示信息 开始-------//

var userGroupStore=new Ext.data.JsonStore({
     url:'getAllUserGroupAction.action',
     root:'result',
     autoLoad:true,
     fields:['id','userGroupName','userGroupId']
});

var userGroup_selectModel=new Ext.grid.CheckboxSelectionModel({
	     id:'userGroup_selectModel',
	     width:30,
         singleSelect:true
       });
   //---grid----//
var userGroup_grid=new Ext.grid.GridPanel({
    title:'用户群',
    store:userGroupStore,
    sm:userGroup_selectModel,
    id:'userGroup_grid',
    height:290,
    columns:[
       new Ext.grid.RowNumberer(),userGroup_selectModel,
       {
       	 header:'用户群名称',
       	 sortable:true,
       	 dataIndex:'userGroupName',
       	 align:'center'
       }
    ]
    ,
    listeners:{
    	rowClick:function(grid,rowIndex, e){
//    		alert(grid.getStore().getAt(rowIndex).data.userGroupName)
    		var record=grid.getStore().getAt(rowIndex).data;
//    		console.log(record.userGroupId+","+record.userGroupName);
    		//获取用户群关联的门户方案
    		dealUserGroupClick(record);
    	}
    }
});
//----用户群展示信息 结束-------//

//----用户群关联门户方案 开始----//

var userGroupAssPortalProject_store=new Ext.data.JsonStore({
	id:'userGroupAssPortalProject_store',
    fields:['projectId','projectName','userGroupId','id'],
    url:'getUserGroupAssPortalSchemeAction',
    root:'result'
});

var userGroupAssPortalProject_selectModel=new Ext.grid.CheckboxSelectionModel(
{
 singleSelect:true
});

var userGroupAssPortalProject_grid=new Ext.grid.GridPanel({
    title:'用户关联的门户方案情况',
    id:'userGroupAssPortalProject_grid',
    store:userGroupAssPortalProject_store,
    sm:userGroupAssPortalProject_selectModel,
    height:300,
    columns:[
       userGroupAssPortalProject_selectModel,
       {
       	 header:'门户方案名称',
       	 dataIndex:'projectName'
       }
    ],
    tbar:[
      {
    	text:'删除与用户群的关联',
    	iconCls:'silk-delete',
    	handler:function(){
    		deletePortalProjectFromUserGroup();
    	}
      }
    ]
	
});

//----用户群关联门户方案 结束----//


//----所有门户方案 开始---------//
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


 	//checkbox
 	var portalProject_check_select = new Ext.grid.CheckboxSelectionModel({
 	    singleSelect:true,
 	    id:'portalProject_check_select',
 	    width:30
 	});
 
	
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
    sm:portalProject_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
			var record = portalProject_grid.getStore().getAt(rowIndex).data;
			dealPortalProjectClick(record);
		}
	},
	viewConfig : {
		forceFit : true
	},
    tbar: [
    {
    	text:'添加到用户群',
    	iconCls:'silk-add',
    	handler:function(){
    		addPortalSchemeToUserGroup();
    	}
    	
    }
    ,
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

//----所有门户方案 结束---------//
    
//--------------------总布局---------------------------
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'north',
    		height:300,
    		layout:'border',
    		autoScroll:true,
    		items:[
    		{
    			region:'center',
    			width:600,
//    			height:290,
//    			layour:'fit',
    			autoScroll:true,
    			margins:'0 10px 0 0',
    			items:[
    		      userGroup_grid
    		    ]
    		}
    		,
    		{
    			region:'east',
    			layout:'fit',
    			width:600,
    			items:[
    			  userGroupAssPortalProject_grid
    			]
    		}
    		]
    	}
    	,
    	{
    		region:'center',
    		layout:'fit',
    		autoScroll:true,
    		items:[portalProject_grid]
    	}
    	
    ]
});
    
    
});