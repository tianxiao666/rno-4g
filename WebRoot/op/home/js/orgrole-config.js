Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";


/*
组件id:

orgrole_grid
orgrole_item
orgrole_store
orgrole_editor

*/

/**
 * 刷新组织角色
 */
function refreshOrgRole(){
	var orgrole_grid=Ext.getCmp('orgrole_grid');
	var orgrole_store=orgrole_grid.getStore();
	orgrole_store.reload();
}

/**
 * 删除选定的组织角色
 */
function deleteOrgRole() {

	var orgrole_grid = Ext.getCmp('orgrole_grid');
	var s = orgrole_grid.getSelectionModel().getSelected();
	if (s == null) {
		alert("请选择要删除的组件!");
		return false;
	}
	if (confirm("确定要删除选定的组件吗？")) {
		var orgRoleId = s.data.orgRoleId;
		// AJAX删除
		Ext.Ajax.request({
					url : 'deleteOrgRoleAction',
					params : {
						orgRoleId:orgRoleId
					},
					success : function() {
						var msg = Ext.MessageBox.alert('提示', '删除成功!');
						setTimeout(function() {
									Ext.MessageBox.hide()
								}, 1000);
						refreshOrgRole();
					},
					failure : function() {

					}
				});
	}

}

/**
 * 增加组织角色
 */
function addOrgRole(){
	
}

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var orgrole_item=Ext.data.Record.create(
[
  {name:'orgCode',type:'string'},
  {name:'orgRoleId',type:'string'},
  {name:'orgRoleType',type:'string'},
  {name:'orgRoleName',type:'string'},
  {name:'orgRoleCode',type:'string'},
  {name:'requirement',type:'string'},
  {name:'level',type:'string'},
  {name:'description',type:'string'}
]
);

var orgrole_editor=new Ext.ux.grid.RowEditor({
   saveText:'保存',
   cancelText:'取消',
   clicksToEdit:2,
   listeners:{
   	beforeedit:function(RowEditor){
 			},
   	  canceledit:function(roweditor){
   	  	 if (!roweditor.record.data.orgRoleId) {        	  
	          	this.grid.getStore().removeAt(roweditor.rowIndex);  
	         }  
   	  }
   	  ,
   	  afteredit:function (r, o, record,n){
   	  	  var ds=record.data;
   	  	  var tip=ds.orgRoleId?'修改':'增加';
   	  	  var json=Ext.encode(ds);
   	  	  //alert(json);
   	  	  Ext.Ajax.request({
   	  	    url:'addOrgRoleAction',
   	  	    params:{json:json},
   	  	    callback:function(opts,success,response){
   	  	    	if(success){
   	  	    		var text=response.responseText;
   	  	    		if(text.indexOf('success')>=0){
   	  	    			alert(tip+"成功！");
   	  	    		}else{
   	  	    			alert(tip+'失败。原因：'+text.substring(text.indexOf(":")+1));
   	  	    		}
   	  	    		refreshOrgRole();
   	  	    	}else{
   	  	    		alert(tip+"时出现未知错误！"+response.statusText)
   	  	    	}
   	  	    }
   	  	  })
   	  }
   	
   }
});

var orgrole_store=new Ext.data.JsonStore({
	id:'orgrole_store',
    url:'getAllOrgRoleAction',
    autoLoad:true,
    root:'result',
    fields:['orgRoleId','orgRoleType','orgRoleName','orgRoleCode','requirement','level','description','orgCode']
})

var orgrole_sm=new Ext.grid.CheckboxSelectionModel({

	singleSelect:true
});

var orgrole_grid=new Ext.grid.GridPanel({
   id:'orgrole_grid',
   title:'组织角色列表',
   store:orgrole_store,
   sm:orgrole_sm,
   plugins:[orgrole_editor],
   columns:[
       new Ext.grid.RowNumberer(),orgrole_sm,
       {
         header:'组织编码',
         dataIndex:'orgCode',
         editor:new Ext.form.TextField({
             allowBlank:false         
         })
       },
       {
       	header:'组织角色类型',
       	dataIndex:'orgRoleType',
       	editor:new Ext.form.ComboBox({
       	  typeAhead:true,
       	  allowBlank:false,
       	  triggerAction:'all',
       	  transform:'roleType',
       	  lazyRender:true,
       	  listClass: 'x-combo-list-small'
       	})
       },
       {
       	header:'组织角色名称',
       	dataIndex:'orgRoleName',
       	editor:new Ext.form.TextField(
       		{allowBlank:false}
       		)
       },
       {
       	header:'组织角色编码',
       	dataIndex:'orgRoleCode',
       	width:200,
       	editor:new Ext.form.TextField(
       	 	{allowBlank:false}
       	 	)
      
       },
        {
       	header:'级别',
       	dataIndex:'level',
       	editor:new Ext.form.TextField()
       },
       {
       	header:'组织角色岗位要求',
       	dataIndex:'requirement',
       	width:200,
       	editor:new Ext.form.TextArea()
       },
      
       {
       	header:'备注',
       	dataIndex:'description',
       	width:200,
       	editor:new Ext.form.TextArea()
       }
   ]
   ,
   tbar:[
   {
      text:'增加组织角色',
      iconCls:'silk-add',
      handler : function() {
										var org = new orgrole_item({
											       orgCode:'',
													orgRoleId : '',
													orgRoleType : '',
													orgRoleName : '',
													orgRoleCode : '',
													requirement : '',
													level : '',
													description : ''
												});
										var count=orgrole_store.getCount();
										orgrole_editor.stopEditing();
//										orgrole_grid.stopEditing();
										orgrole_store.insert(count,org);
										orgrole_grid.getView().refresh();
										orgrole_grid.getSelectionModel().selectRow(count);
										orgrole_editor.startEditing(count);
									}
   },
   {
      text:'删除组织角色',
      iconCls:'silk-delete',
      handler:function(){
      	deleteOrgRole();
      }
      },
       {
      	text:'刷新',
      	iconCls:'silk-table-refresh',
      	handler:function(){
      		refreshOrgRole();
      	}
   }
   ]
   
	
  
   })
   
    //布局
   var mainPanel=new Ext.Panel({
        layout:'fit',
        items:[orgrole_grid]
   });

   var viewPort=new Ext.Viewport({
       layout:'fit',
       items:[mainPanel]
   })
})

