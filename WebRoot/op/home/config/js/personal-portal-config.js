Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

/**
 * 
 * 结构：
 * 
 *  | 我的工作空间  |    我能用的工作空间 |
 *  | 我的标签栏     |    我能用的标签栏    |
 *  | 我的组件        |    我能用的组件       |
 *  
 *  ”我的工作空间“
 *  myworkzone_grid
 *  
 * ”我能用的工作空间“
 *   allworkzone_grid
 *   
 *   ”我的标签栏“
 *   myworkzonesite_grid
 *   
 *   ”我能用的标签栏“
 *    allworkzonesite_grid
 *    
 *    ”我的组件“
 *    myportalitem_grid
 *    
 *    ”我能用的组件“
 *    allportalitem_grid
 * 
 * 
 * 
 * 
 * 响应函数：
 * 
 * 新增工作空间到我的工作空间列表
 * addWorkZoneToMyWorkZoneList   对应 url：addToPersonalWorkZoneAction
 * 
 * 删除选中的工作空间
 * deleteUserWorkZone  对于url：deleteFromPersonalWorkZoneAction
 * 
 * 获取用户工作空间下的功能标签 
 * getMyWorkZoneSites();   url：getUserWorkZoneSitesUnderWorkZoneAndSchemeForConfigAction
 * 
 * 删除用户的某个功能标签
 * deleteUserWorkZoneSite url:deleteFromPersonalWorkZoneSiteAction
 * 
 * 添加某个标签栏到工作空间
 * addWorkZoneSiteToMyWorkZoneSiteList  url：addToPersonalWorkZoneSiteAction
 * 
 */

/**
 * 新增工作空间到我的工作空间列表
 */
function addWorkZoneToMyWorkZoneList(){
	var allworkzone_grid=Ext.getCmp('allworkzone_grid');
	var allworkzone_store=allworkzone_grid.store;
	
	var count=allworkzone_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选择要添加的工作空间");
		return;
	}
	
	var selectRecord=allworkzone_grid.getSelectionModel().getSelected();
	
	var myworkzone_grid=Ext.getCmp('myworkzone_grid');
	var myworkzone_store=myworkzone_grid.getStore();
	
	//判断有没有与现有的重复
	var find=false;
//	console.log("11")
	
	var totalMyZoneCount=myworkzone_store.getCount();
//	alert("totalMyZoneCount="+totalMyZoneCount)
	for(var i=0;i<totalMyZoneCount;i++){
		var record=myworkzone_store.getAt(i);
		if(record.data.workZoneId==selectRecord.data.id){
	     	  find=true;
	     	  break;
	     }
	}
	
//	console.log("2")
	if(find){
		alert("我的工作空间里当前已经存在此工作空间，不必再添加");
		return;
	}
//	alert("3")
	var data=[];
	data.push(selectRecord.data)
	var json=Ext.encode(data);
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
//	alert("4")
	if(!schemeId || !orgRoleId || !userId){
		alert("相关的门户方案、用户的组织角色、用户账号存在问题，不予执行添加操作！");
		return;
	}
//	alert("5")
	
//	return;
	Ext.Ajax.request({
	   url:'addToPersonalWorkZoneAction',
	   params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId},
	   callback:function(opts,success,response){
	   	    if(success){
	   	    	var text=response.responseText;
	   	    	if(text.indexOf('success')>=0){
	   	    		alert('添加成功');
	   	    		refreshMyWorkZone();
	   	    	}else{
	   	    		alert('添加失败');
	   	    	}
	   	    }else{
	   	    	alert("出现异常问题："+response.statusText)
	   	    }
	   }
		
	})
	return;
}

/**
 * 刷新我的工作空间
 */
function refreshMyWorkZone(){
	var myworkzone_grid=Ext.getCmp('myworkzone_grid');
	var myworkzone_store=myworkzone_grid.store;
	myworkzone_store.removeAll();
	myworkzone_store.load();
}

/**
 * 刷新我的标签栏
 */
function refreshMyWorkZoneSite(){
	var myworkzonesite_grid=Ext.getCmp('myworkzonesite_grid');
	var myworkzonesite_store=myworkzonesite_grid.store;
	myworkzonesite_store.removeAll();
	myworkzonesite_store.reload();
}

/**
 * 刷新我的门户组件
 */
function refreshMyPortalItem(){
	var myportalitem_grid=Ext.getCmp('myportalitem_grid');
	var myportalitem_store=myportalitem_grid.store;
	myportalitem_store.removeAll();
	myportalitem_store.reload();
}

/**
 * 删除选中的工作空间
 */
function deleteUserWorkZone(){
	var myworkzone_grid=Ext.getCmp('myworkzone_grid');
	var count=myworkzone_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选定再删除");
		return;
	}
	var selectedRecord=myworkzone_grid.getSelectionModel().getSelected();
	
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	
	if(!schemeId || !orgRoleId || !userId){
		alert("相关的门户方案、用户的组织角色、用户账号存在问题，不予执行删除操作！");
		return;
	}
	
	var json=Ext.encode([selectedRecord.data]);
	
	Ext.Ajax.request({
	    url:'deleteFromPersonalWorkZoneAction',
	    params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId},
	    callback:function(opts,success,response){
	    	 if(success){
	    	 	var text=response.responseText;
	    	 	if(text.indexOf('success')>=0){
	    	 		 alert("删除成功");
	    	 		 
	    	 		 //刷新 我的工作空间
	    	 		 refreshMyWorkZone();
	    	 		 //清除相关的 功能标签
	    	 		 var myworkzonesite_grid = Ext.getCmp('myworkzonesite_grid');
					 var myworkzonesite_store = myworkzonesite_grid.store;
					 myworkzonesite_store.removeAll();
					 
					 //清除相关的门户组件
					 var myportalitem_grid = Ext.getCmp('myportalitem_grid');
					 var myportalitem_store = myportalitem_grid.store;
					 myportalitem_store.removeAll();
	    	 	}else{
	    	 		alert("删除失败！");
	    	 	}
	    	 }else{
	    	 	alert("未知错误："+response.statusText)
	    	 }
	    }
	})
	
	
}

//获取用户工作空间下的功能标签
function getMyWorkZoneSites(){
	var myworkzone_grid=Ext.getCmp('myworkzone_grid');
	var count=myworkzone_grid.getSelectionModel().getCount();
	if(count==0){
		return;
	}
	var selectedRecord=myworkzone_grid.getSelectionModel().getSelected();
	
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById('workZoneId').value;
	
	//workZoneId,schemeId,userId
	if(!workZoneId || !schemeId || !userId){
		return;
	}
	
	var myworkzonesite_grid=Ext.getCmp('myworkzonesite_grid');
	var myworkzonesite_store=myworkzonesite_grid.store;
	myworkzonesite_store.removeAll();
	myworkzonesite_store.load({
	  params:{
	  	 schemeId:schemeId,
	  	 workZoneId:workZoneId,
	  	 userId:userId
	  }
	})
}

/**
 * 删除用户的某个功能标签
 */
function deleteUserWorkZoneSite(){
	var myworkzonesite_grid=Ext.getCmp('myworkzonesite_grid');
	var count=myworkzonesite_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选定再删除");
		return;
	}
	var selectedRecord=myworkzonesite_grid.getSelectionModel().getSelected();
	
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById('workZoneId').value;
	
	if(!schemeId || !orgRoleId || !userId || !workZoneId){
		alert("相关的门户方案、用户的组织角色、用户账号、工作空间存在问题，不予执行删除操作！");
		return;
	}
	
	var json=Ext.encode([selectedRecord.data]);
	
	Ext.Ajax.request({
	    url:'deleteFromPersonalWorkZoneSiteAction',
	    params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId,workZoneId:workZoneId},
	    callback:function(opts,success,response){
	    	 if(success){
	    	 	var text=response.responseText;
	    	 	if(text.indexOf('success')>=0){
	    	 		 alert("删除成功");
	    	 		 myworkzonesite_grid.getStore().reload();
	    	 	}else{
	    	 		alert("删除失败！");
	    	 	}
	    	 }else{
	    	 	alert("未知错误："+response.statusText)
	    	 }
	    }
	})
}


/**
 * 添加某个标签栏到工作空间
 */
function addWorkZoneSiteToMyWorkZoneSiteList(){
	var allworkzonesite_grid=Ext.getCmp('allworkzonesite_grid');
	var allworkzonesite_store=allworkzonesite_grid.store;
	
	var count=allworkzonesite_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选择要添加的功能标签");
		return;
	}
	
	var selectRecord=allworkzonesite_grid.getSelectionModel().getSelected();
	
	var myworkzonesite_grid=Ext.getCmp('myworkzonesite_grid');
	var myworkzonesite_store=myworkzonesite_grid.getStore();
	
	//判断有没有与现有的重复
	var find=false;
//	console.log("11")
	
	var totalMyZoneCount=myworkzonesite_store.getCount();
//	alert("totalMyZoneCount="+totalMyZoneCount)
	for(var i=0;i<totalMyZoneCount;i++){
		var record=myworkzonesite_store.getAt(i);
		if(record.data.workZoneSiteId==selectRecord.data.id){
	     	  find=true;
	     	  break;
	     }
	}
	
//	console.log("2")
	if(find){
		alert("我的标签栏里当前已经存在此标签栏，不必再添加");
		return;
	}
//	alert("3")
	var data=[];
	data.push(selectRecord.data)
	var json=Ext.encode(data);
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById("workZoneId").value;
//	alert("4")
	if(!schemeId || !orgRoleId || !userId || !workZoneId){
		alert("相关的门户方案、用户的组织角色、用户账号、工作空间存在问题，不予执行添加操作！");
		return;
	}
//	alert("5")
	
//	return;
	Ext.Ajax.request({
	   url:'addToPersonalWorkZoneSiteAction',
	   params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId,workZoneId:workZoneId},
	   callback:function(opts,success,response){
	   	    if(success){
	   	    	var text=response.responseText;
	   	    	if(text.indexOf('success')>=0){
	   	    		alert('添加成功');
	   	    		refreshMyWorkZoneSite();
	   	    	}else{
	   	    		alert('添加失败');
	   	    	}
	   	    }else{
	   	    	alert("出现异常问题："+response.statusText)
	   	    }
	   }
		
	})
	return;
}

/**
 * 获取用户功能标签下的门户组件设置
 */
function getMyPortalItems(){
	var myworkzonesite_grid=Ext.getCmp('myworkzonesite_grid');
	var count=myworkzonesite_grid.getSelectionModel().getCount();
	if(count==0){
		return;
	}
	var selectedRecord=myworkzonesite_grid.getSelectionModel().getSelected();
	
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById('workZoneId').value;
	var workzonesiteId=document.getElementById('workzonesiteId').value;
	
	//workZoneId,schemeId,userId
	if(!workZoneId || !schemeId || !userId ||!orgRoleId || !workzonesiteId){
		return;
	}
	
	var myportalitem_grid=Ext.getCmp('myportalitem_grid');
	var myportalitem_store=myportalitem_grid.store;
	myportalitem_store.removeAll();
	myportalitem_store.load({
	  params:{
	  	 schemeId:schemeId,
	  	 workZoneId:workZoneId,
	  	 userId:userId,
	  	 orgRoleId:orgRoleId,
	  	 workzonesiteId:workzonesiteId
	  }
	})
}

/**
 * 从我的门户组件中删除一个门户组件
 */
function deleteMyPortalItem(){
	var myportalitem_grid=Ext.getCmp('myportalitem_grid');
	var count=myportalitem_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选定再删除");
		return;
	}
	var selectedRecord=myportalitem_grid.getSelectionModel().getSelected();
	
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById('workZoneId').value;
	var workzonesiteId=document.getElementById('workzonesiteId').value;
	if(!schemeId || !orgRoleId || !userId || !workZoneId || !workzonesiteId){
		alert("相关的门户方案、用户的组织角色、用户账号、工作空间、标签栏存在问题，不予执行删除操作！");
		return;
	}
	
	var json=Ext.encode([selectedRecord.data]);
	
	Ext.Ajax.request({
	    url:'deleteFromPersonalPortalItemAction',
	    params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId,workZoneId:workZoneId,workzonesiteId:workzonesiteId},
	    callback:function(opts,success,response){
	    	 if(success){
	    	 	var text=response.responseText;
	    	 	if(text.indexOf('success')>=0){
	    	 		 alert("删除成功");
	    	 		 myportalitem_grid.getStore().reload();
	    	 	}else{
	    	 		alert("删除失败！");
	    	 	}
	    	 }else{
	    	 	alert("未知错误："+response.statusText)
	    	 }
	    }
	})
	
}



/**
 * 添加某个门户组件到用户设置
 */
function addPortalItemToMyPortalItemList(){
	var allportalitem_grid=Ext.getCmp('allportalitem_grid');
	var allportalitem_store=allportalitem_grid.store;
	
	var count=allportalitem_grid.getSelectionModel().getCount();
	if(count==0){
		alert("请先选择要添加的门户组件");
		return;
	}
	
	var selectRecord=allportalitem_grid.getSelectionModel().getSelected();
	
	//比较功能标签的类型和门户组件的类型是否匹配
	var workzonesiteType=document.getElementById('workzonesiteType').value;
	if(selectRecord.data.type!=workzonesiteType){
		alert("该门户组件与标签栏的类型不匹配，不能执行添加!");
		return;
	}
	
	var myportalitem_grid=Ext.getCmp('myportalitem_grid');
	var myportalitem_store=myportalitem_grid.getStore();
	
	//判断有没有与现有的重复
	var find=false;
	
	var totalMyZoneCount=myportalitem_store.getCount();
	for(var i=0;i<totalMyZoneCount;i++){
		var record=myportalitem_store.getAt(i);
		if(record.data.id==selectRecord.data.id){
	     	  find=true;
	     	  break;
	     }
	}
	
//	console.log("2")
	if(find){
		alert("我的门户组件里当前已经存在此门户组件，不必再添加");
		return;
	}
//	alert("3")
	var data=[];
	data.push(selectRecord.data)
	var json=Ext.encode(data);
	var schemeId=document.getElementById('schemeId').value;
	var orgRoleId=document.getElementById('orgRoleId').value;
	var userId=document.getElementById('userId').value;
	var workZoneId=document.getElementById("workZoneId").value;
	var workzonesiteId=document.getElementById("workzonesiteId").value;
	
//	alert("4")
	if(!schemeId || !orgRoleId || !userId || !workZoneId || !workzonesiteId){
		alert("相关的门户方案、用户的组织角色、用户账号、工作空间、标签栏存在问题，不予执行添加操作！");
		return;
	}
//	alert("5")
	
//	return;
	//提醒用户
	if(workzonesiteType==2){
		if(!window.confirm("此类标签栏下面只能有一个门户组件，执行添加将会替换原有的门户组件，而不是新增一个，是否继续？")){
			return;
		}
	}
	Ext.Ajax.request({
	   url:'addToPersonalPortalItemAction',
	   params:{json:json,schemeId:schemeId,orgRoleId:orgRoleId,userId:userId,workZoneId:workZoneId,workzonesiteId:workzonesiteId},
	   callback:function(opts,success,response){
	   	    if(success){
	   	    	var text=response.responseText;
	   	    	if(text.indexOf('success')>=0){
	   	    		alert('添加成功');
	   	    		refreshMyPortalItem();
	   	    	}else{
	   	    		alert('添加失败');
	   	    	}
	   	    }else{
	   	    	alert("出现异常问题："+response.statusText)
	   	    }
	   }
		
	})
	return;
}



Ext.onReady(function(){
	var height = Ext.get(document.body).getHeight();
    var width = Ext.get(document.body).getWidth();
    
	//====================================我当前的工作空间==============================
	var myworkzone_store=new Ext.data.JsonStore({
	     url:'getUserWorkZoneListAction',
	     root:'result',
	     fields:['id','projectId','workZoneId','userId','visible','_order','zoneName','assId'],
	     //工作空间的id，门户方案的id，工作空间的id（有重复），用户账户，是否可见，用户设置的工作空间的顺序，工作空间的名称,用户设置表的id
	     autoLoad:true
	})
	
	var myworkzone_editor= new Ext.ux.grid.RowEditor({
        saveText: '保存',
    	cancelText: '取消',
        listeners: { 
 			beforeedit:function(RowEditor){
 				//alert(RowEditor.record);
 				//return false; 		
 			}, 		
 			//保存
	        afteredit:function (r, o, record,n){
 				
				var grid = this.grid;
				var store = grid.getStore();
                var data = [record.data];  
                var json = Ext.encode(data);
//               	alert(Ext.encode(json));
			
//               	alert(json);
			//alert(Ext.encode(store.json));
			//AJAX保存
			Ext.Ajax.request({
			   url: 'saveUserWorkZoneConfigAction',
			   params: { json:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
					myworkzone_grid.store.reload();
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
	
   var myworkzone_grid=new Ext.grid.GridPanel({
      id:'myworkzone_grid',
      title:'我的工作空间',
      store:myworkzone_store,
      plugins:[myworkzone_editor],
      columns:[
        new Ext.grid.RowNumberer(),
        {
          header:'工作空间名称',
          dataIndex:'zoneName'
         },
         {
          header:'顺序',
          dataIndex:'_order',
          editor:new Ext.form.NumberField(
            {
              allowBlank:false
          	}
           )
         }
         ,
         {
           header:'是否显示',
           dataIndex:'visible',
           renderer:function(value){
           	   if(value==0){
           	   	   return "不显示";
           	   }else{
           	   	  return "显示";
           	   }
           },
           editor:new Ext.form.ComboBox({
                   typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'workzoneVisible',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
           	
           })
         	
         }
      ]
      ,
      tbar:[
      {
       text:'刷新',
       iconCls:'silk-table-refresh',
       handler:function(){
       	             myworkzone_store.reload();
                   }
      }
      ,
      {
        text:'删除该工作空间',
        iconCls:'silk-delete',
        handler:function(){
        	       deleteUserWorkZone();
               }
      }
      ]
      ,
      listeners:{
      	rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    	          document.getElementById("workZoneId").value=record.data.id;
    	          getMyWorkZoneSites();
    		}
      }
   	
   })
   
   //====================================我能用的工作空间==============================
   
   var allworkzone_store=new Ext.data.JsonStore({
      url:'getUserAllAllowedWorkZonesAction',
      fields:['id','projectId','zoneName','workzoneId'],
      root:'result',
      params:{schemeId:document.getElementById('schemeId').value}
   });
   
   var allworkzone_sm=new Ext.grid.CheckboxSelectionModel({
       singleSelect:true
   })
   
   var allworkzone_grid=new Ext.grid.GridPanel({
   	     title:'我能用的工作空间',
         id:'allworkzone_grid',
         store:allworkzone_store,
         sm:allworkzone_sm,
         columns:[
               new Ext.grid.RowNumberer(),allworkzone_sm,
             	{
             	  header:'工作空间名称',
             	  dataIndex:'zoneName'
             	}
         ],
         tbar:[
             {
               text:'加入我的工作空间',
               iconCls:'silk-add',
               handler:function(){
               	   //alert("加入我的工作空间");
               	   addWorkZoneToMyWorkZoneList();
               }
             }
             ,
             {
               text:'刷新',
               iconCls:'silk-table-refresh',
               handler:function(){
               	    allworkzone_store.removeAll();
               	     allworkzone_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value
               	            }
               	     });
               }
             }
         ]
   });
   
   allworkzone_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value
               	            }
               	     });
   
               	     
//----------------------------我的功能标签 开始--------------------------------------//            	     
var myworkzonesite_store=new Ext.data.JsonStore({
  url:'getUserWorkZoneSitesUnderWorkZoneAndSchemeForConfigAction',
  root:'result',
  fields:['id','workItemName','type','layout','assId','projectId','workZoneId','workZoneSiteId','userId','visible','_order']
  
})


var myworkzonesite_editor= new Ext.ux.grid.RowEditor({
        saveText: '保存',
    	cancelText: '取消',
        listeners: { 
 			beforeedit:function(RowEditor){
 				//alert(RowEditor.record);
 				//return false; 		
 			}, 		
 			//保存
	        afteredit:function (r, o, record,n){
 				
				var grid = this.grid;
				var store = grid.getStore();
                var data = [record.data];  
                var json = Ext.encode(data);
			Ext.Ajax.request({
			   url: 'saveUserWorkZoneSiteSettingConfigAction',
			   params: { json:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
//					myworkzone_grid.store.reload();
					myworkzonesite_store.reload();
				   },
				   failure:function(){
				   
				   }
				});
	        },
	        canceledit: function(RowEditor){  
//	          if (RowEditor.record.phantom) {        	  
//	          	this.grid.getStore().removeAt(RowEditor.rowIndex);  
//	          }  
	        }
 		}
    });
	


var myworkzonesite_grid=new Ext.grid.GridPanel({
   id:'myworkzonesite_grid',
   store:myworkzonesite_store,
   title:'我的标签栏',
   plugins:[myworkzonesite_editor],
   columns:[
       new Ext.grid.RowNumberer(),
       {
       	 header:'标签栏名称',
       	 dataIndex:'workItemName'
       },
       {
       	header:'标签栏布局类型',
       	dataIndex:'type',
       	renderer:function(value){
   			if (value == "1") {
				return "门户组件类型";
			} else if (value == "2") {
				return "应用框架类型";
			} else {
				return "请选择";
			}
       	}
       },
       {
       	header:'排列顺序',
       	dataIndex:'_order',
       	editor:new Ext.form.NumberField({
       	   allowBlank:false
       	})
       },
       {
       	header:'是否显示',
       	dataIndex:'visible',
       	renderer:function(value){
       		if(value==1){
       			return "显示";
       		}else{
       			return "隐藏";
       		}
       	}
       	,
       	editor:new Ext.form.ComboBox({
       	    typeAhead: true,
	        triggerAction: 'all',
	        // transform the data already specified in html
	        transform: 'workzoneSiteVisible',
	        lazyRender: true,
	        listClass: 'x-combo-list-small'
       	})
       }
   ]
   ,
   tbar:[
   {
     text:'从我的标签栏中删除',
     iconCls:'silk-delete',
     handler:function(){
     	deleteUserWorkZoneSite();
     }
   }
   ]
   ,
   listeners:{
   	  rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    	          document.getElementById("workzonesiteId").value=record.data.id;
    	          document.getElementById('workzonesiteType').value=record.data.type;
    	          getMyPortalItems();
    		}
   	
   }
	
})

//----------------------------我的功能标签 结束--------------------------------------//       


//----------------------------我能用的功能标签 开始--------------------------------------//

var allworkzonesite_store =new Ext.data.JsonStore({
   url:'getUserAllAllowedWorkZoneSitesAction',
   fields:['id','workItemName','type'],
   root:'result',
   params:{schemeId:document.getElementById('schemeId')}
})


var allworkzonesite_sm=new Ext.grid.CheckboxSelectionModel({
       singleSelect:true
   })
   
   var allworkzonesite_grid=new Ext.grid.GridPanel({
   	     title:'我能用的标签栏',
         id:'allworkzonesite_grid',
         store:allworkzonesite_store,
         sm:allworkzonesite_sm,
         columns:[
               new Ext.grid.RowNumberer(),allworkzone_sm,
             	{
             	  header:'标签栏名称',
             	  dataIndex:'workItemName'
             	},
             	{
             	  header:'标签栏布局类型',
             	  dataIndex:'type',
             	  renderer:function(value){
					if (value == "1") {
						return "门户组件类型";
					} else if (value == "2") {
						return "应用框架类型";
					} else {
						return "请选择";
					}
				  }
             	}
         ],
         tbar:[
             {
               text:'加入到我的标签栏',
               iconCls:'silk-add',
               handler:function(){
               	   //alert("加入我的标签栏");
               	   addWorkZoneSiteToMyWorkZoneSiteList();
               }
             }
             ,
             {
               text:'刷新',
               iconCls:'silk-table-refresh',
               handler:function(){
               	    allworkzonesite_store.removeAll();
               	     allworkzonesite_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value
               	            }
               	     });
               }
             }
         ]
   });
   
   allworkzonesite_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value
               	            }
               	     });
   
               	 
//----------------------------我能用的功能标签 结束--------------------------------------//

               	     
//----------------------------我的门户组件   开始--------------------------------------//

//编辑、删除
var myportalitem_store=new Ext.data.JsonStore({
   url:'getUserPortalItemsAction',
   root:'result',
   fields : ['id',//门户组件id
             'assId',//用户设置表的主键
             'title', //门户组件标题
             'showTitle',//是否显示头部面板
             'type',//组件类型，1为门户组件，2为框架组件
             'isLocal', //是否引用本地资源
             'url', //资源的url
             'maxUrl', //面板最大化时的url
             'defaultHeight',//默认高度
             'defaultWidth', //默认宽度
             'projectId', //门户方案id
             'orgRoleId', //组织角色id
             'userId',//用户账号
             'workZoneId', //工作空间id
             'workZoneSiteId',//功能标签id 
             'visible', //是否可见
             'rowIndex',//组件所在行
             '_column', //组件所在列
             'width', //该组件被用户设置的宽度
             'height'//该组件被用户设置的高度
             ]
})

var  myportalitem_editor=new Ext.ux.grid.RowEditor({
     saveText: '保存',
    	cancelText: '取消',
        listeners: { 
 			beforeedit:function(RowEditor){
 				//alert(RowEditor.record);
 				//return false; 		
 			}, 		
 			//保存
	        afteredit:function (r, o, record,n){
 				
				var grid = this.grid;
				var store = grid.getStore();
                var data = [record.data];  
                var json = Ext.encode(data);
//               	alert(Ext.encode(json));
			
//               	alert(json);
			//alert(Ext.encode(store.json));
			//AJAX保存
			Ext.Ajax.request({
			   url: 'saveUserPortalItemConfigAction',
			   params: { json:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
					myportalitem_grid.store.reload();
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



var myportalitem_grid=new Ext.grid.GridPanel({
    id:'myportalitem_grid',
    title:'我的门户组件',
    plugins:[myportalitem_editor],
    store:myportalitem_store,
    columns:[
       new Ext.grid.RowNumberer(),
       {
       	header:'组件名称',
       	dataIndex:'title'
       }
       ,
       {
       	header:'组件类型',
       	dataIndex:'type',
       	renderer:function(value){
       	     if(value==1){
       	        return "可拖拉组件";	
       	     }else{
       	     	return "iframe组件";
       	     }
       	}
       }
       ,
       {
       	header:'所在行',
       	dataIndex:'rowIndex',
       	sortable:true,
       	editor:new Ext.form.NumberField()
       }
       ,
       {
       	 header:'所在列',
       	 dataIndex:'_column',
       	 sortable:true,
       	 editor:new Ext.form.NumberField()
       }
       ,
       {
       	 header:'是否可见',
       	 dataIndex:'visible',
       	 renderer:function(value){
       	 	if(value==1){
       	 		return "显示";
       	 	}else {
       	        return "不显示";	
       	 	}
       	 },
       	 editor:new Ext.form.ComboBox({
       	        typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'itemVisible',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
       	 })
       },
       {
       	 header:'高度',
       	 dataIndex:'height',
       	 renderer:function(value){
       	    return value+"px";
       	 }
       	 ,
       	 editor:new Ext.form.NumberField()
       }
//       ,
//       {
//       	 header:'宽度',
//       	 dataIndex:'width',
//       	 renderer:function(value){
//       	    return value+"px";
//       	 }
////       	 ,
////       	 editor:new Ext.form.NumberField()
//       }
    ]
    ,
    tbar:[
       {
         text:'从我的门户组件中删除',
         iconCls:'silk-delete',
         handler:function(){
              deleteMyPortalItem();	
         }
       }
    ]
    
})
               	     
//----------------------------我能用的门户组件  结束--------------------------------------//
               	     
  //----------------------------我能用的门户组件 开始--------------------------------------//

var allportalitem_store =new Ext.data.JsonStore({
   url:'getUserAllAllowedPortalItemsAction',
   fields:['id','title','showTitle','type','isLocal','url','maxUrl','defaultHeight','defaultWidth'],
   root:'result'
})


var allportalitem_sm=new Ext.grid.CheckboxSelectionModel({
       singleSelect:true
   })
   
   var allportalitem_grid=new Ext.grid.GridPanel({
   	     title:'我能用的门户组件',
         id:'allportalitem_grid',
         store:allportalitem_store,
         sm:allportalitem_sm,
         columns:[
               new Ext.grid.RowNumberer(),allportalitem_sm,
               {
                 header:'组件标题',
                 dataIndex:'title'
               },
               {
               	 header:'是否显示面板头部',
               	 dataIndex:'showTitle',
               	 renderer:function(value){
               	 	if(value=="1"){
               	 		return "显示";
               	 	}else{
               	 		return '不显示';
               	 	}
                  }
               }
               ,
               {
               	 header:'组件类型',
               	 dataIndex:'type',
               	 renderer:function(value){
               	    	if(value==1){
			       	        return "可拖拉组件";	
			       	     }else{
			       	     	return "iframe组件";
			       	     }
               	 }
               }
               
             	
         ],
         tbar:[
             {
               text:'加入到我的门户组件里',
               iconCls:'silk-add',
               handler:function(){
               	   //alert("加入我的标签栏");
               	   addPortalItemToMyPortalItemList();
               }
             }
             ,
             {
               text:'刷新',
               iconCls:'silk-table-refresh',
               handler:function(){
               	    allportalitem_store.removeAll();
               	    allportalitem_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value,
               	             workZoneId:document.getElementById('workZoneId').value,
               	             workZoneSiteId:document.getElementById('workzonesiteId').value,
               	             orgRoleId:document.getElementById('orgRoleId').value
               	             
               	            }
               	     });
               }
             }
         ]
   });
   
   allportalitem_store.load({
               	           params:{
               	             schemeId:document.getElementById('schemeId').value,
               	             workZoneId:document.getElementById('workZoneId').value,
               	             orgRoleId:document.getElementById('orgRoleId').value
               	            }
               	     });
   
               	 
//----------------------------我能用的门户组件 结束--------------------------------------//
             	     
               	     
               	     
   //----------------------------布局--------------------------------------//
   var mainPanel=new Ext.Panel({
        layout:'border',
        height:height,
        items:[
           {
              region:'north',
              layout:'border',
              height:200,
              items:[
                  {
                    region:'west',
                    width:750,
                    layout:'fit',
                    items:[myworkzone_grid]
                  }
                  ,
                  {
                    region:'center',
                    layout:'fit',
                    items:[allworkzone_grid]
                  }
              ]
           }
           ,
           {
	           	region:'center',
	           	layout:'border',
	           	height:500,
	           	items:[
		           	{
		           		region:'west',
		           		width:750,
		           		layout:'fit',
		           		items:[myworkzonesite_grid]
		           	}
		           	,
		           	{
		           		region:'center',
		           		layout:'fit',
		           		items:[allworkzonesite_grid]
		           	}
	           	]
           }
           ,
           
           {
	           	region:'south',
	           	layout:'border',
	           	height:260,
	           	items:[
	           	    {
			       		region:'west',
			       		width:750,
			       		layout:'fit',
			       		items:[myportalitem_grid]
	           		},
	           		{
	           			region:'center',
	           			layout:'fit',
	           			items:[allportalitem_grid]
	           		}
	           	]
           }
        ]
   })
   
   var viewport=new Ext.Viewport({
   	layout:'fit',
      items:[mainPanel]
   })
	
});