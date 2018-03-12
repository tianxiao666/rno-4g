Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

function portalProjectAction(record){	
	var projectId = record.data.projectId;	
	var projectName  =record.data.projectName;
//	Ext.getCmp('center-panel').setTitle('['+projectName+']:工作空间配置');
	document.getElementById("projectId").value =  projectId;
	
//	var workZone_grid = Ext.getCmp('workZone_grid');
//   	var workZone_store=workZone_grid.getStore();
//	workZone_store.load({params:{portalProjectId:projectId}});
//	
//	//2012-5-16 gmh add
//	Ext.getCmp('south-panel').setTitle("["+projectName+"]:功能标签配置")
//	var workZoneSite_grid = Ext.getCmp('workZoneSite_grid');
//   	var workZoneSite_store=workZoneSite_grid.getStore();
//	workZoneSite_store.load({params:{portalProjectId:projectId}});
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
    	clicksToEdit:2,//3-->2
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
			   		//workzoneAction(record);
			   		portalProject_store.reload();
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
        }
//        ,    	
//        {
//            header: '系统信息',
//            dataIndex: 'sysconfigId',
//            width: 150,
//            align: 'center',
//            renderer:function(value){
//				return value;
//            },
//            editor: new fm.TextField({
//                allowBlank: false
//            })
//        }
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
        },
        {
            header: '操作',
            dataIndex: 'projectId',
            width: 100,
            align: 'center',
            renderer:function(value){
				return '<a href="javascript:portalProjectEditAction()">编辑</a>';
            }
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
	{
        text: '增加门户方案',
        iconCls:'silk-add',
        handler : function(){
            // access the Record constructor through the grid's store
            var count = portalProject_grid.getStore().getCount();
            var lastRecord = portalProject_grid.getStore().getAt(count-1);
            
            var p = new portalProject({
            	projectId:'',
                sysconfigId: '',
                projectName: '请输入门户方案名称',
                projectDesc: ''
            });
            portalProject_editor.stopEditing();
            portalProject_store.insert(count, p);
            portalProject_grid.getView().refresh();
            portalProject_grid.getSelectionModel().selectRow(count);
            portalProject_editor.startEditing(count);             
        }
    },
	{
        text: '删除门户方案',
        iconCls:'silk-delete',
        handler : function(){
    	
			var ss = portalProject_grid.getSelectionModel().getSelections();
			var countt = portalProject_grid.getSelectionModel().getCount();
			
			if(countt==0){
				alert("请选择要删除的对象!");
				return false;        			
			}
			var ids  ="";
			for(var i=0;i<countt;i++){
				record  = ss[i];
				ids+=record.data.projectId+";";
			}
			
			if(!confirm("确定要删除选定的对象?")){
				return false;
			}   
			
			
			
			//AJAX删除
			Ext.Ajax.request({
			   url: 'deletePortalProjectAction.action',
			   params: { json:ids},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
			   		portalProject_store.reload();
			   },
			   failure:function(){
			   
			   }
			});
		}
    },{
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




//---------用户工作空间相关配置--begin--------
var userConfigPanel = new Ext.Panel({
    layout:'border',
    height:height,
    items:[{
        region:'center',
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