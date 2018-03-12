Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

var fm = Ext.form;	

//---------------------焦点新闻分类配置------------------------begin--
var focusNewsType = Ext.data.Record.create([{
    name: 'id',
    type: 'string'
}, {
    name: 'focusNewsTypeName',
    type: 'string'
}, {
    name: 'focusNewsDesc',
    type: 'string'
}
]);

//更新插件
 	var focusNewsType_editor = new Ext.ux.grid.RowEditor({
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
               	//alert(Ext.encode(json));
			
			//alert(Ext.encode(store.json));
			//AJAX保存
			Ext.Ajax.request({
			   url: 'saveFocusNewsTypeAction.action',
			   params: { json:json},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
					
			   		var record = focusNewsType_grid.getSelectionModel().getSelected();
			   		//workzoneAction(record);
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
 	var focusNewsType_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
var focusNewsType_cm = new Ext.grid.ColumnModel({
    defaults: {
        sortable: true // columns are not sortable by default           
    },
    columns: [
    	new Ext.grid.RowNumberer(),focusNewsType_check_select, 
    	{
            header: '焦点新闻专题名称',
            dataIndex: 'focusNewsTypeName',
            width:150,
            align: 'center',
            editor: new fm.TextField({
                allowBlank: false,
                selectOnFocus:true
            })
        },
        {
            header: '焦点新闻专题描述',
            dataIndex: 'focusNewsDesc',
            width: 550,
            align: 'center',
            editor: new fm.TextField({
                allowBlank: false
            })
        }
    ]
});

// Store定义
var focusNewsType_dataUrl = 'getFocusNewTypeListAction.action';
var focusNewsType_store = new Ext.data.JsonStore({
    url: focusNewsType_dataUrl,
    root: 'result',
    fields: ['id', 'focusNewsTypeName', 'focusNewsDesc']
    //sortInfo: {field:'_order', direction:'ASC'}
});

//GRID定义
var focusNewsType_grid = new Ext.grid.GridPanel({
    store: focusNewsType_store,
    height:180,
    cm: focusNewsType_cm,
    loadMask: {msg:'正在加载数据……'},
    sm:focusNewsType_check_select,
    //autoExpandColumn: 'common', // column with this id will be expanded
    header:false,
    frame: false,
    border:false,
    plugins: [focusNewsType_editor],
    // specify the check column plugin on the grid so the plugin is initialized
    listeners: { 
		//行单击
		rowclick:function(grid_,rowIndex,e){
			var record = focusNewsType_grid.getStore().getAt(rowIndex);
			//workzoneAction(record);
		}
	},
    tbar: [
	{
        text: '增加专题',
        iconCls:'silk-add',
        handler : function(){
            // access the Record constructor through the grid's store
            var count = focusNewsType_grid.getStore().getCount();
            var lastRecord = focusNewsType_grid.getStore().getAt(count-1);
            
            var p = new focusNewsType({
            	id:'',
                focusNewsTypeName: '',
                focusNewsDesc: ''
            });
            focusNewsType_editor.stopEditing();
            focusNewsType_store.insert(count, p);
            focusNewsType_grid.getView().refresh();
            focusNewsType_grid.getSelectionModel().selectRow(count);
            focusNewsType_editor.startEditing(count);             
        }
    },
	{
        text: '删除专题',
        iconCls:'silk-delete',
        handler : function(){
    	
			var ss = focusNewsType_grid.getSelectionModel().getSelections();
			var countt = focusNewsType_grid.getSelectionModel().getCount();
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
			   url: 'deleteFocusNewsTypeByIdAction.action',
			   params: { ids:ids},
			   success: function(){
			   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
					setTimeout(function(){Ext.MessageBox.hide()},1000);
			   		focusNewsType_store.reload();
			   },
			   failure:function(){
			   
			   }
			});
		}
    },{
		text: '刷新数据',
        iconCls:'silk-table-refresh',
        handler : function(){
    		focusNewsType_store.reload();
    	}
   }
    ]
});

// manually trigger the data store load
//focusNewsType_store.load({});

//---------------------焦点新闻分类配置------------------------end--



function focusNewsTypeAction(record){
	
	var WorkZoneId = record.data.id;	
	var zoneName  =record.data.zoneName;
	Ext.getCmp('center-panel').setTitle('工作空间标签配置——>'+zoneName);
	focusNewsTypeSite_store.load({params:{WorkZoneId:WorkZoneId}});
	
}



 var focusNewsPanel = new Ext.FormPanel({
     layout:'border',
     items:[
     	{
     		region: 'north',
     		title:'焦点新闻专题',
     		height:200,
     		items:[focusNewsType_grid]
     	},
		{
             region: 'center',
             id: 'center-panel', 
             title:'焦点新闻列表',
             split: false,                
             margins: '0 0 0 0',       
             items:[]         
         }
      ]
 });

 
