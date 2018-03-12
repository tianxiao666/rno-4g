Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	


 //---------------------友情链接配置------------------------begin--

var sysfooterurl = Ext.data.Record.create([{
    name: 'id',
    type: 'string'
}, {
    name: 'urlName',
    type: 'string'
}, {
    name: 'url',
    type: 'string'
}, {
    name: 'state',
    type: 'string'
},
 {
    name: '_order',
    type: 'string'
}  
]);



//更新插件
 var sysfooterurl_editor = new Ext.ux.grid.RowEditor({
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
				   url: 'saveSysfooterurlAction.action',
				   params: { json:json },
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
 var sysfooterurl_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
//ColumnModel定义
 var sysfooterurl_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),sysfooterurl_check_select, 
            {
                header: 'ID',
                dataIndex: 'id',
                width: width*0.15,
                align: 'center'
            },
            {
                header: '链接名称',
                dataIndex: 'urlName',
                width: width*0.15,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            },
            {
                header: 'URL',
                dataIndex: 'url',
                width: width*0.4,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            },
            {
                header: '是否显示',
                dataIndex: 'state',
                width: width*0.1,
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
	                transform: 'state2',
	                lazyRender: true,
	                listClass: 'x-combo-list-small'
	            })
            }, 
            {
                header: '排序号',
                dataIndex: '_order',
                width: width*0.05,
                align: 'center',
                editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false
	            })
            }
        ]
});

// Store定义
var sysfooterurl_dataUrl = 'getAllSysfooterurlList.action';
var sysfooterurl_store = new Ext.data.JsonStore({
	    url: sysfooterurl_dataUrl,
	    root: 'result',
	    fields: ['id', 'urlName', 'url', 'state','_order']
	    //sortInfo: {field:'_order', direction:'ASC'}
});
	
//GRID定义
var sysfooterurl_grid = new Ext.grid.GridPanel({
        store:sysfooterurl_store,
        cm: sysfooterurl_cm,
        sm:sysfooterurl_check_select,
        loadMask: {msg:'正在加载数据……'},
        width: width*0.9,
        height: 130,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [sysfooterurl_editor],
        tbar: [
    	{
            text: '添加友情链接',
            iconCls:'silk-add',
            handler : function(){    		
    			var sysfooterurl_store = sysfooterurl_grid.getStore();
                var count = sysfooterurl_store.getCount();
                var lastRecord =sysfooterurl_store.getAt(count-1);
                var order_ = 0;
                if(count!=0){
                	order_ = parseInt(lastRecord.data.order)+1;
                }

                var p = new sysfooterurl({
                	id:'',
                    urlName: '请输入链接名称',
                    url: '请输入URL',
                    state: '1',
                    _order: '1'
                });

                sysfooterurl_grid.stopEditing();
                sysfooterurl_store.insert(count, p);
                sysfooterurl_grid.getView().refresh();
                sysfooterurl_grid.getSelectionModel().selectRow(count);
                sysfooterurl_editor.startEditing(count);             

    		
            }
        },
    	{
            text: '删除友情链接',
            iconCls:'silk-delete',
            handler : function(){
        		var s = sysfooterurl_grid.getSelectionModel().getSelected();
        		if(s==null){
        			alert("请选择要删除的对象!");
        			return false;        			
        		}
        		
        		var ss = sysfooterurl_grid.getSelectionModel().getSelections();
        		var countt = sysfooterurl_grid.getSelectionModel().getCount();
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
				   url: 'deleteSysfooterurlAction.action',
				   params: { ids:ids},
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		sysfooterurl_store.reload();
				   },
				   failure:function(){
				   
				   }
				});


		    }
        },{
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		sysfooterurl_store.reload();
	    	}
   		}
        ]
});

 sysfooterurl_store.load({});


//---------------------友情链接配置---------------------end--
 
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'center', 
    		layout:'fit',
    		items:[
    			sysfooterurl_grid
    		]
    	}
    	
    ]
});
    
    
});