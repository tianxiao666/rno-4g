Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	

//---------------------工作空间配置------------------------begin--
var workZone = Ext.data.Record.create([{
    name: 'id',
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
}
]);

//更新插件
 	var workZone_editor = new Ext.ux.grid.RowEditor({
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
                var data = record.data;  
                var json = Ext.encode(data);
//               	alert(Ext.encode(json));
			
//               	alert(json);
			//alert(Ext.encode(store.json));
			//AJAX保存
			Ext.Ajax.request({
			   url: 'saveWorkZoneConfigAction',//'saveUserworkzoneConfig.action', //2012-5-21 gmh modify
			   params: { workZoneConfig:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
//			   		var record = workZone_grid.getSelectionModel().getSelected();
//			   		workzoneAction(record);
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
 	
// 	Ext.applyIf(workZone_editor, {
//  		getEditor: function() {
//    		return false;
//  		}
//	}); 
 	
	
    //ColumnModel定义
var workZone_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),workZone_check_select, 
        {
            header: '是否显示',
            dataIndex: 'state',
            width: 65,
            align: 'center',
            renderer:function(value){
    			if(value=="1"){
    				return "显示";
    			}else{
    				return "不显示";
    			}    		
            },
            editor: new fm.ComboBox({
                typeAhead: true,
                triggerAction: 'all',
                // transform the data already specified in html
                transform: 'state',
                lazyRender: true,
                listClass: 'x-combo-list-small'
            })
        }
    	, {
            header: '工作空间名称',
            dataIndex: 'zoneName',
            width:100,
            align: 'center',
            editor: new fm.TextField({
                allowBlank: false,
                selectOnFocus:true
            })
        },
        {
            header: '排序号',
            dataIndex: '_order',
            width: 50,
            align: 'center',
            editor: new Ext.ux.form.SpinnerField({
                allowBlank: false
            })
        }
    ]
});

// Store定义
var workZone_dataUrl = 'getAllWorkZoneListAction';//'getAllUserworkzoneList.action';  //2012-5-21 gmh  modify
var workZone_store = new Ext.data.JsonStore({
    url: workZone_dataUrl,
    root: 'result',
    fields: ['id',  'zoneName', 'state','_order']
    //sortInfo: {field:'_order', direction:'ASC'}
});

//GRID定义
var workZone_grid = new Ext.grid.GridPanel({
    store: workZone_store,
    cm: workZone_cm,
    loadMask: {msg:'正在加载数据……'},
    sm:workZone_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    plugins: [workZone_editor],
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
//			var record = workZone_grid.getStore().getAt(rowIndex);
//			workzoneAction(record);
		}
	},
    tbar: [
	{
        text: '增加工作空间',
        iconCls:'silk-add',
        handler : function(){
            // access the Record constructor through the grid's store
            var count = workZone_grid.getStore().getCount();
            var lastRecord = workZone_grid.getStore().getAt(count-1);
            
            var p = new workZone({
            	id:'',
                zoneName: '请输入工作空间名称',
                state: '1',
                _order: '1'
            });
            workZone_editor.stopEditing();
            workZone_store.insert(count, p);
            workZone_grid.getView().refresh();
            workZone_grid.getSelectionModel().selectRow(count);
            workZone_editor.startEditing(count);             
        }
    },
	{
        text: '删除工作空间',
        iconCls:'silk-delete',
        handler : function(){
    	
			var ss = workZone_grid.getSelectionModel().getSelections();
			var countt = workZone_grid.getSelectionModel().getCount();
			if(countt==0){
				alert("请选择要删除的对象!");
				return false;        			
			}
			var ids  ="";
			for(var i=0;i<countt;i++){
				record  = ss[i];
				ids+=record.data.id+";";
			}
			if(!confirm("确定要删除选定的对象?")){
				return false;
			}   
			
			//AJAX删除
			Ext.Ajax.request({
			   url: 'deleteWorkZoneListAction',//'deleteUserworkzoneConfig.action', //2012-5-21 gmh modify
			   params: { ids:ids},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
			   		workZone_store.reload();
			   },
			   failure:function(){
			   
			   }
			});
		}
    },{
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		workZone_store.reload();
    	}
   }
    ]
});

// manually trigger the data store load
workZone_store.load({});

//---------------------工作空间配置------------------------end--



//function workzoneAction(record){
//	
//	var WorkZoneId = record.data.id;	
//	var zoneName  =record.data.zoneName;
//	Ext.getCmp('center-panel').setTitle('工作空间标签配置——>'+zoneName);
//	workZoneSite_store.load({params:{WorkZoneId:WorkZoneId}});
//	
//}


//---------用户工作空间相关配置--begin--------
var userConfigPanel = new Ext.Panel({
    layout:'border',
    height:height,
    items:[{
        region:'center',
        id:'west-panel',
        iconCls:'icon-cog_edit',
        title:'用户空间配置',
        split:false,
        frame:true,
        border:false,
        //width: 340,
        layout:'fit',
        items:[workZone_grid]
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