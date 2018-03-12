Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	var currentWorkZoneId = document.getElementById("currentWorkZoneId").value;
	var workzoneName = document.getElementById("workzoneName").value;
	var userPortalProjectId = document.getElementById("userPortalProjectId").value;
	
	
	var height = Ext.get(document.body).getHeight();

	var fm = Ext.form;
	
	var workZoneSite = Ext.data.Record.create([{
        name: 'id',
        type: 'string'
    }, {
        name: 'userWorkZoneId',
        type: 'string'
    }, {
        name: 'workItemName',
        type: 'string'
    }, {
        name: 'type',
        type: 'string'
    },
     {
        name: 'layout',
        type: 'string'
    },    
    {
        name: 'order',
        type: 'String'
    }]);
	
	//更新插件
 	var editor = new Ext.ux.grid.RowEditor({
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
               	//alert(Ext.encode(json));
				
				//alert(Ext.encode(store.json));
				//AJAX保存
				Ext.Ajax.request({
				   url: 'saveUserworkzonesiteConfig.action',
				   params: { userWorkZonesiteConfig:json },
				   success: function(response, options){
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		var obj=Ext.util.JSON.decode(response.responseText); 
   						var record = grid.getSelectionModel().getSelected();
   						record.set('id',obj.result.id);
				   		workzonesitelayoutAction(record);
				   		Ext.get('isModify').dom.value='1';
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
 	var check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
	var cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),check_select, 
            {
                header: '标签栏名称',
                dataIndex: 'workItemName',
                width: 130,
                align: 'center',    		
                editor:new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            }
        	, {
                header: '布局类型',
                dataIndex: 'type',
                width: 100,
                align: 'center',
                renderer:function(value){
        			if(value=="1"){
        				return "门户组件类型";
        			}else if(value=="2"){
        				return "应用框架类型";
        			}else{
        				return "请选择";
        			}
        		
                },
                editor: new fm.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    // transform the data already specified in html
                    transform: 'layout',
                    lazyRender: true,
                    forceSelection:true,
                    listClass: 'x-combo-list-small'
                })
            }, {
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
	var dataUrl = 'getUserworkzonesiteListByWorkzoneIdForUser.action?WorkZoneId='+currentWorkZoneId+'&userPortalProjectId='+userPortalProjectId;
	var store = new Ext.data.JsonStore({
	    url: dataUrl,
	    root: 'result',
	    fields: ['id', 'userWorkZoneId', 'workItemName', 'type','layout','_order']
	    //sortInfo: {field:'order', direction:'ASC'}
	    	
	    	
	});
	
    //GRID定义
    var grid = new Ext.grid.GridPanel({
        store: store,
        id:'workzonesite_grid',
        cm: cm,
        sm:check_select,
        width: 350,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: true,
        plugins: [editor],
        // specify the check column plugin on the grid so the plugin is initialized
        listeners: { 
    		//行单击
    		rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    			//alert(record.get('id'));
    			workzonesitelayoutAction(record);
    	
    		}
    	},
        tbar: [
    	{
            text: '添加标签栏',
            iconCls:'silk-add',
            handler : function(){
                // access the Record constructor through the grid's store
                var count = grid.getStore().getCount();
                var lastRecord = grid.getStore().getAt(count-1);
                var order_ = 0;
                if(count!=0){
                	order_ = parseInt(lastRecord.data.order)+1;
                }
                
                var p = new workZoneSite({
                	id:'',
                    userWorkZoneId: currentWorkZoneId,
                    workItemName: '请输入标签栏名称',
                    type: '1',
                    order: order_
                });
                editor.stopEditing();
                store.insert(count, p);
                grid.getView().refresh();
                grid.getSelectionModel().selectRow(count);
                editor.startEditing(count);             
            }
        },
    	{
            text: '删除标签栏',
            iconCls:'silk-delete',
            handler : function(){      		
        		var ss = grid.getSelectionModel().getSelections();
        		var countt = grid.getSelectionModel().getCount();
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
				   url: 'deleteUserworkzonesiteConfig.action',
				   params: { ids:ids},
				   success: function(){
					    store.reload()
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
						Ext.get('isModify').dom.value='1';
				   },
				   failure:function(){
				   
				   }
				});

		    }
        },{
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		Ext.getCmp('workzonesite_grid').getStore().reload();
	    	}
	   }
        ]
    });

    // manually trigger the data store load
    store.load({});
    

    


function workzonesitelayoutAction(record){
	var id = record.get('id');
	var workItemName = record.get('workItemName');
	var userWorkZoneId = record.get('userWorkZoneId');
	var layoutVal = record.get('layout');	
	var type = record.get('type');
	
	Ext.get('userWorkZoneSiteId').dom.value=id;
	
	Ext.get('workzonesitelayout_frame').dom.src='workzonesitelayoutConfgiIndexAction.action?userWorkZoneSiteId='+id+'&layoutVal='+layoutVal+'&type='+type;
	Ext.getCmp('center23').setTitle(workItemName);
}
	
	
new Ext.Viewport({
    layout: 'border',
    items: [
    	{
        region: 'west',
        iconCls: 'icon-config',
        width:350,
        title: '工作空间功能模块',
        items:[grid]       
    }, {
        region: 'center',
        height:height,
        id:'center23',        
        iconCls: 'icon-config',
        title:'标签布局配置',
        layout:'fit',
        html:'<IFRAME name="workzonesitelayout_frame" id="workzonesitelayout_frame" style="WIDTH: 100%; HEIGHT: 100%;" src="" frameBorder=0 scrolling=auto></IFRAME>'
    }]
});


//
store.on('load',function(){
    var count = grid.getStore().getCount();
    if(count==0){
    	Ext.get('workzonesitelayout_frame').dom.src='';
    }else{
		grid.getSelectionModel().selectRow(0);
		var record = grid.getSelectionModel().getSelected();
		workzonesitelayoutAction(record);
	}
})


});