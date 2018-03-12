Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	


//---------------------组件配置------------------------begin--

var Item = Ext.data.Record.create([{
    name: 'id',
    type: 'string'
}, {
    name: 'title',
    type: 'string'
}, {
    name: 'showTitle',
    type: 'string'
}, {
    name: 'isLocal',
    type: 'string'
}, {
    name: 'type',
    type: 'string'
},
 {
    name: 'url',
    type: 'string'
},
 {
    name: 'maxUrl',
    type: 'string'
},
{
	name:'defaultHeight',
	type:'string'
}
,
{
    name:'defaultWidth',
    type:'string'
}
]);



//更新插件
 var item_editor = new Ext.ux.grid.RowEditor({
        saveText: '保存',
        cancelText: '取消',
        listeners: { 
 			beforeedit:function(RowEditor){
 			}, 		
 			//保存
	        afteredit:function (r, o, record,n){
				var grid = this.grid;
				var store = grid.getStore();
                var data = record.data;  
                var json = Ext.encode(data);
				//AJAX保存
				Ext.Ajax.request({
				   url: 'saveItemAction.action',
				   params: { itemJson:json },
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		store.reload();
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
 var item_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
//ColumnModel定义
 var item_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),item_check_select, 
            {
                header: '组件ID',
                dataIndex: 'id',
                width: width*0.15,
                align: 'center'
            },
            {
                header: '组件名称',
                dataIndex: 'title',
                width: width*0.1,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            },
            {
                header: '是否显示标题',
                dataIndex: 'showTitle',
                width: width*0.08,
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
                    transform: 'showTitle',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
                })
            },
            {
                header: '是否本地资源',
                dataIndex: 'isLocal',
                width: width*0.08,
                align: 'center',
                renderer:function(value){
            		if(value=="1"){
            			return "本地资源";
            		}else{
            			return "跨域资源";
            		}
                },
                editor: new fm.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'isLocal',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
                })
            },
            {
                header: '组件类型',
                iconCls:'silk-add',
                dataIndex: 'type',
                width: width*0.1,
                align: 'center',
                renderer:function(value){
            		if(value=="1"){
            			return "门户组件类型";
            		}else{
            			return "应用框架类型";
            		}
                },

                editor: new fm.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'type',
                    lazyRender: true,
                    listClass: 'x-combo-list-small'
                })
            }, 
            {
            	header:'默认高度(px)',
            	dataIndex:'defaultHeight',
            	align:'center',
            	editor:new Ext.form.NumberField({
            	   minValue:1
            	})
            }
            ,
            {
            	header:'默认宽度(px)',
            	dataIndex:'defaultWidth',
            	align:'center',
            	editor:new Ext.form.NumberField({
            	   minValue:1
            	})
            },
            {
                header: '组件URL',
                dataIndex: 'url',
                width: width*0.25,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            }, 
            {
                header: '详细内容URL',
                dataIndex: 'maxUrl',
                width: width*0.25,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: true,
                    selectOnFocus:true
                })
            }
            
        ]
});

// Store定义
var item_dataUrl = 'getItemList.action';
var item_store = new Ext.data.JsonStore({
	    url: item_dataUrl,
	    root: 'result',
	    fields: ['id', 'title', 'showTitle', 'isLocal','type','url','maxUrl','defaultHeight','defaultWidth']
	    //sortInfo: {field:'_order', direction:'ASC'}
	    	
	    	
});
	
//GRID定义
var item_grid = new Ext.grid.GridPanel({
        store: item_store,
        cm: item_cm,
        width: width*0.9,
        loadMask: {msg:'正在加载数据……'},
        height: 130,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [item_editor],
        tbar: [
    	{
            text: '添加组件',
            iconCls:'silk-add',
            handler : function(){    		
    			var item_store = item_grid.getStore();
                var count = item_store.getCount();
                var lastRecord = item_store.getAt(count-1);
                var order_ = 0;
                if(count!=0){
                	order_ = parseInt(lastRecord.data.order)+1;
                }

                var p = new Item({
                	id:'',
                    title: '请输入组件名称',
                    showTitle: '1',
                    type: '1',
                    url: '请输入组件URL',
                    maxUrl: '',
                    defaultHeight:'10',
                    defaultWidth:'10'
                });

                item_grid.stopEditing();
                item_store.insert(count, p);
                item_grid.getView().refresh();
                item_grid.getSelectionModel().selectRow(count);
                item_editor.startEditing(count);             

    		
            }
        },
    	{
            text: '删除组件',           
			iconCls:'silk-delete',
            handler : function(){
        		var s = item_grid.getSelectionModel().getSelected();
        		if(s==null){
        			alert("请选择要删除的组件!");
        			return false;        			
        		}
                if(confirm("确定要删除选定的组件吗？")){
	        		var id = s.data.id;
					//AJAX删除
					Ext.Ajax.request({
					   url: 'deleteItemAction.action',
					   params: { id:id},
					   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
					   		item_store.reload();
					   },
					   failure:function(){
					   
					   }
					});
				}

		    }
        },
        {
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		item_store.reload();
	    	}
   		}
        ]
});

 item_store.load({});


//---------------------组件配置------------------------end--
 
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'center', 
    		layout:'fit',
    		items:[
    			item_grid
    		]
    	}
    	
    ]
});
    
    
});