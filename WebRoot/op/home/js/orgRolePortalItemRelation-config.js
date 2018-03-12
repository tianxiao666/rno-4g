Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

/**
 * 添加门户组件到组织角色
 */
function addPortalItemToOrgRole(){
	console.log("addPortalSchemeToUserGroup");
	
	var orgRoleId=document.getElementById("orgRoleId").value;
	
	if(!orgRoleId){
		var msg = Ext.MessageBox.alert('提示', '请先选择一个组织角色!');		
//		setTimeout(function(){Ext.MessageBox.hide()},1000);
		return;
	}
	
	//取得选中要添加的门户组件
	var portalItem_grid=Ext.getCmp('portalItem_grid');
	var selectItems=portalItem_grid.getSelectionModel().getSelections();
	if(selectItems.length==0){
		Ext.MessageBox.alert('提示','请先选择要添加的门户组件');
		return;
	}
	
	var orgRoleAssPortalItem_grid=Ext.getCmp('orgRoleAssPortalItem_grid');
	var orgRoleAssPortalItem_store=orgRoleAssPortalItem_grid.getStore();
	
//	console.log(orgRoleAssPortalItem_store);
	//是否已经存在了关联
	var needAddItems=[];//提交到后台的
	//排除重复
	if(orgRoleAssPortalItem_store.getTotalCount()>0){
        var existItem=false;
		for (var j = 0; j < selectItems.length; j++) {
			existItem=false;
            orgRoleAssPortalItem_store.each(function(record) {
	            if (record.data.id == selectItems[j].data.id) {
//				    console.log('已存在  ' + record.data.title);
				    existItem=true;
				    return;
			    }
		    });
		    if(!existItem){
		    	needAddItems.push(selectItems[j].data);
		    }
		}
	}else{
		for (var j = 0; j < selectItems.length; j++) {
//			console.log("er 添加："+selectItems[j].data.title)
			needAddItems.push(selectItems[j].data);
		}
	}
	
	if(needAddItems.length==0){
		console.log("所选择的都已经存在");
		return;
	}
	
	//添加门户方案到用户群的关联
	var addAssUrl="addPortalItemsToOrgRoleAction";
	var json=Ext.encode(needAddItems);
	
	Ext.Ajax.request({
	   url:addAssUrl,
	   params:{json:json,orgRoleId:orgRoleId},
	   callback :function(opts,success,response){
//	   	 	console.log(response.responseText);
	   	    var text=response.responseText;
	   	   if(success){
	   	   	   if(text.indexOf('success')>=0){
	   	   	  
	   	   	    Ext.MessageBox.alert('提示', '保存组织角色与门户组件的关联成功!');		
	   	   	   }else{
	   	   	   	Ext.MessageBox.alert('提示', '保存组织角色与门户组件的关联失败!详情：<br/>'+text.substring(text.indexOf(':')+1));		
	   	   	   }
	   	   }else{
	   	   	    Ext.MessageBox.alert('提示', '保存组织角色与门户组件的关联发生未知错误!详情：<br/>'+text.substring(text.indexOf(':')+1));		
	   	   }
	   	   
	   	   //重新加载与组织角色与门户组件的关联的内容
	      orgRoleAssPortalItem_store.load({params:{'orgRoleId':orgRoleId}});
	   }
	});
	
	
}

/**
 * 处理单击组织角色事件
 * @param {} record
 */
function  dealOrgRoleClick(record){
	
	//判断是否处于选中状态
	var orgRole_grid=Ext.getCmp('orgRole_grid');
	var orgRole_columnModel=orgRole_grid.getColumnModel();
//	console.log(userGroup_columnModel);
//	console.log("select userGroup count:"+userGroup_columnModel.getColumnById('userGroup_selectModel').getCount());
	
	var count=orgRole_columnModel.getColumnById('orgRole_selectModel').getCount();
//	console.log("count=="+count)
	if(count==1){//有选中
     	document.getElementById("orgRoleId").value=record.orgRoleId;
	}else{//未选中
		document.getElementById("orgRoleId").value="";
	}
	
	//加载关联的门户组件的情况
	var orgRoleId=document.getElementById("orgRoleId").value;
	var orgRoleAssPortalItem_grid = Ext
				.getCmp('orgRoleAssPortalItem_grid');
	if (orgRoleId == '') {
        orgRoleAssPortalItem_grid.getStore().removeAll();
	} else {
		orgRoleAssPortalItem_grid.getStore().load({
					params : {
						'orgRoleId' : orgRoleId
					}
				});
	}
	
}

/**
 * 删除与组织角色与门户组件的关联
 */
function deletePortalItemFromOrgRole(){
	var orgRoleAssPortalItem_grid=Ext.getCmp('orgRoleAssPortalItem_grid');
	var tstore=orgRoleAssPortalItem_grid.getStore();
	
	if(orgRoleAssPortalItem_grid.getSelectionModel().getCount()>0){
	   var records = orgRoleAssPortalItem_grid.getSelectionModel()
				.getSelections();
			
		var selectedItems=[];
		for(var i=0;i<records.length;i++){
			selectedItems.push(records[i].data);
		}
		var json=Ext.encode(selectedItems);
		Ext.Ajax.request({
		  url:'deletePortalItemsFromOrgRoleAction',
		  params:{json:json},
		  callback:function(opt,success,response){
		  	var text=response.responseText;
		  	 if(success){
                   if(text.indexOf('success')>=0){
                   	  Ext.Msg.alert('提示','组织角色和门户组件的关系删除成功');
                   }else{
                   	  Ext.Msg.alert('提示','组织角色和门户组件的关系删除失败');
                   }
		  	 }else{
		  	 	  Ext.Msg.alert('提示','删除组织角色和门户组件的关系时出现未知错误！');
		  	 }
		  	 
		  	 //刷新
		  	 	var orgRoleId = document.getElementById("orgRoleId").value;

				var orgRoleAssPortalItem_grid = Ext
						.getCmp('orgRoleAssPortalItem_grid');
				var orgRoleAssPortalItem_store = orgRoleAssPortalItem_grid
						.getStore();
				orgRoleAssPortalItem_store.load({
							params : {
								'orgRoleId' : orgRoleId
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



//----组织角色展示信息 开始-------//

var orgRoleStore=new Ext.data.JsonStore({
     url:'getAllOrgRoleAction.action',
     root:'result',
     autoLoad:true,
     fields:['id','orgRoleName','orgRoleId']
});

var orgRole_selectModel=new Ext.grid.CheckboxSelectionModel({
	     id:'orgRole_selectModel',
	     width:30,
         singleSelect:true
       });
   //---grid----//
var orgRole_grid=new Ext.grid.GridPanel({
    title:'组织角色',
    store:orgRoleStore,
    sm:orgRole_selectModel,
    id:'orgRole_grid',
    height:290,
    columns:[
       new Ext.grid.RowNumberer(),orgRole_selectModel,
       {
       	 header:'组织角色名称',
       	 sortable:true,
       	 dataIndex:'orgRoleName',
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
    		dealOrgRoleClick(record);
    	}
    }
});
//----组织角色展示信息 结束-------//

//----组织角色关联门户组件 开始----//

var orgRoleAssPortalItem_store=new Ext.data.JsonStore({
	id:'orgRoleAssPortalItem_store',
    fields:['id','title','showTitle','type','isLocal','url','maxUrl','orgRoleId'],
    url:'getOrgRoleAssPortalItemAction',
    root:'result'
});

var orgRoleAssPortalItem_selectModel=new Ext.grid.CheckboxSelectionModel(
{
 singleSelect:false//多选
});

var orgRoleAssPortalItem_grid=new Ext.grid.GridPanel({
    title:'组织角色关联的的门户组件情况',
    id:'orgRoleAssPortalItem_grid',
    store:orgRoleAssPortalItem_store,
    sm:orgRoleAssPortalItem_selectModel,
    height:300,
    columns:[
       new Ext.grid.RowNumberer(),orgRoleAssPortalItem_selectModel,
       {
       	 header:'组件标题',
       	 dataIndex:'title'
       }
       ,
       {
       	 header:'是否显示面板头部',
       	 width:120,
       	 dataIndex:'showTitle',
       	 renderer:function(value){
       	 	if(value=='1'){
       	 		return "显示面板头部";
       	 	}else{
       	 		return "不显示面板头部";
       	 	}
       	 }
       }
       ,
       {
       	header:'组件类型',
       	dataIndex:'type',
       	renderer:function(value){
       		if(value==1){
       			return "porlet组件";
       		}else{
       			return "iframe框架组件";
       		}
       	}
       }
       ,
       {
       	header:'引用资源类型',
       	dataIndex:'isLocal',
       	renderer:function(value){
       		if(value=='0'){
       			return "内部资源";
       		}else{
       			return '外部资源';
       		}
       	}
       }
       ,
       {
       	header:'资源url',
       	dataIndex:'url',
       	width:400
       }
       ,
       {
       	header:'最大化时的url',
       	dataIndex:'maxUrl',
       	width:400
       }
    ],
    tbar:[
      {
    	text:'删除与组织角色的关联',
    	iconCls:'silk-delete',
    	handler:function(){
    		deletePortalItemFromOrgRole();
    	}
      }
    ]
	
});

//----组织角色关联门户组件 结束----//


//----所有门户组件 开始---------//


 	//checkbox
 	var portalItem_check_select = new Ext.grid.CheckboxSelectionModel({
 	    singleSelect:false,
 	    id:'portalItem_check_select',
 	    width:30
 	});
 
	
    //ColumnModel定义
var portalItem_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    //'id','title','showTitle','type','isLocal','url','maxUrl'
    columns: [
    	new Ext.grid.RowNumberer(),portalItem_check_select, 
        {
       	 header:'组件标题',
       	 dataIndex:'title'
       }
       ,
       {
       	 header:'是否显示面板头部',
       	 dataIndex:'showTitle',
       	 renderer:function(value){
       	 	if(value=='1'){
       	 		return "显示面板头部";
       	 	}else{
       	 		return "不显示面板头部";
       	 	}
       	 }
       }
       ,
       {
       	header:'组件类型',
       	dataIndex:'type',
       	renderer:function(value){
       		if(value==1){
       			return "porlet组件";
       		}else{
       			return "iframe框架组件";
       		}
       	}
       }
       ,
       {
       	header:'引用资源类型',
       	dataIndex:'isLocal',
       	renderer:function(value){
       		if(value=='0'){
       			return "内部资源";
       		}else{
       			return '外部资源';
       		}
       	}
       }
       ,
       {
       	header:'资源url',
       	dataIndex:'url',
       	width:400
       }
       ,
       {
       	header:'最大化时的url',
       	dataIndex:'maxUrl',
       	width:400
       }
    ]
});

// Store定义
var portalItem_dataUrl = 'getAllPortalItemsAction.action';
var portalItem_store = new Ext.data.JsonStore({
    url: portalItem_dataUrl,
    root: 'result',
    fields:['id','title','showTitle','type','isLocal','url','maxUrl','orgRoleId']
    //sortInfo: {field:'_order', direction:'ASC'}
});

//GRID定义
var portalItem_grid = new Ext.grid.GridPanel({
	id:'portalItem_grid',
    store: portalItem_store,
    cm: portalItem_cm,
    loadMask: {msg:'正在加载数据……'},
    sm:portalItem_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    // specify the check column plugin on the grid so the plugin is initialized
//    listeners: { 
//		//行单击
//		rowclick:function(grid_,rowIndex,e){
//			var record = portalProject_grid.getStore().getAt(rowIndex).data;
//			dealPortalItemClick(record);
//		}
//	},
	viewConfig : {
		forceFit : true
	},
    tbar: [
    {
    	text:'添加到组织角色',
    	iconCls:'silk-add',
    	handler:function(){
    		addPortalItemToOrgRole();
    	}
    	
    }
    ,
	{
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		portalItem_store.reload();
    	}
   }
    ]
});

portalItem_store.load({});

//----所有门户组件 结束---------//
    
//--------------------总布局---------------------------
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'north',
    		height:300,
    		layout:'border',
    		autoScroll:true,
    		collapsible:true,
    		items:[
    		{
    			region:'west',
    			width:300,
//    			height:290,
//    			layour:'fit',
    			autoScroll:true,
    			margins:'0 10px 0 0',
    			items:[
    		      orgRole_grid
    		    ]
    		}
    		,
    		{
    			region:'center',
    			layout:'fit',
    			width:600,
    			items:[
    			  orgRoleAssPortalItem_grid
    			]
    		}
    		]
    	}
    	,
    	{
    		region:'center',
    		layout:'fit',
    		collapsible:true,
    		autoScroll:true,
    		items:[portalItem_grid]
    	}
    	
    ]
});
    
    
});