Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	

//---------------------系统信息配置------------------------begin--

var sysconfig = Ext.data.Record.create([{
    name: 'id',
    type: 'string'
}, {
    name: 'sysName',
    type: 'string'
}, {
    name: 'sysVersion',
    type: 'string'
}, {
    name: 'logoUrl',
    type: 'string'
},
 {
    name: 'bottomInfo',
    type: 'string'
}  
]);



//更新插件
 var sysconfig_editor = new Ext.ux.grid.RowEditor({
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
				   url: 'saveSysconfigAction.action',
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
 var sysconfig_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
//ColumnModel定义
 var sysconfig_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),sysconfig_check_select, 
            {
                header: 'ID',
                dataIndex: 'id',
                width: width*0.15,
                align: 'center'
            },
            {
                header: '系统名称',
                dataIndex: 'sysName',
                width: width*0.15,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            },
            {
                header: '系统版本',
                dataIndex: 'sysVersion',
                width: width*0.05,
                align: 'center',
                editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false
	            })
            },
            {
                header: 'LOGO路径',
                dataIndex: 'logoUrl',
                width: width*0.2,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            }, 
            {
                header: '系统声明信息',
                dataIndex: 'bottomInfo',
                width: width*0.35,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            }
        ]
});

// Store定义
var sysconfig_dataUrl = 'getSysconfigList.action';
var sysconfig_store = new Ext.data.JsonStore({
	    url: sysconfig_dataUrl,
	    root: 'result',
	    fields: ['id', 'sysName', 'sysVersion', 'logoUrl','bottomInfo']
	    //sortInfo: {field:'_order', direction:'ASC'}
	    	
	    	
});
	
//GRID定义
var sysconfig_grid = new Ext.grid.GridPanel({
        store:sysconfig_store,
        cm: sysconfig_cm,
        sm:sysconfig_check_select,
        loadMask: {msg:'正在加载数据……'},
        width: width*0.9,
        height: 130,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [sysconfig_editor],
        tbar: [
    	{
            text: '添加系统信息',
            iconCls:'silk-add',
            handler : function(){    		
    			var sysconfig_store = sysconfig_grid.getStore();
                var count = sysconfig_store.getCount();
                var lastRecord =sysconfig_store.getAt(count-1);
                var order_ = 0;
                if(count!=0){
                	order_ = parseInt(lastRecord.data.order)+1;
                }

                var p = new sysconfig({
                	id:'',
                    sysName: '请输入系统名称',
                    sysVersion: '1',
                    logoUrl: '请输入LOGO图片路径',
                    bottomInfo: '请输入系统声明信息'
                });

                sysconfig_grid.stopEditing();
                sysconfig_store.insert(count, p);
                sysconfig_grid.getView().refresh();
                sysconfig_grid.getSelectionModel().selectRow(count);
                sysconfig_editor.startEditing(count);             

    		
            }
        },
    	{
            text: '删除系统信息',
            iconCls:'silk-delete',
            handler : function(){
        		var s = sysconfig_grid.getSelectionModel().getSelected();
        		if(s==null){
        			alert("请选择要删除的对象!");
        			return false;        			
        		}
        		
        		var ss = sysconfig_grid.getSelectionModel().getSelections();
        		var countt = sysconfig_grid.getSelectionModel().getCount();
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
				   url: 'deleteSysconfigAction.action',
				   params: { ids:ids},
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		sysconfig_store.reload();
				   },
				   failure:function(){
				   
				   }
				});


		    }
        },{
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		sysconfig_store.reload();
	    	}
   		}
        ]
});

 sysconfig_store.load({});


//---------------------系统信息配置---------------------end--

 
var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
    		region: 'center', 
    		layout:'fit',
    		items:[
    			sysconfig_grid
    		]
    	}
    	
    ]
});
    
    
});