Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	


//---------------------工作空间标签栏配置------------------------begin--
	var workZoneSite = Ext.data.Record.create([{
        name: 'id',
        type: 'string'
    },{
        name: 'workItemName',
        type: 'string'
    }, {
        name: 'type',
        type: 'string'
    },
     {
        name: 'layout',
        type: 'string'
    }
    ]);
	
	//更新插件
 	var workZoneSite_editor = new Ext.ux.grid.RowEditor({
        saveText: '保存',
        cancelText: '取消',
        clicksToEdit:2,
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
				   url:'saveWorkZoneSiteConfigAction',// 'saveUserworkzonesiteConfig.action',//2012-5-21 gmh modify
				   params: { workZonesiteConfig:json },
				   success: function(data){
					   	workZoneSite_store.reload();
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		//var index = parseInt(workZoneSite_store.getCount())-1;
				   		workZoneSite_check_select.selectLastRow();
   						var record = workZoneSite_grid.getSelectionModel().getSelected();
//   						alert(record.data.id);
				   		workzonesitelayoutAction(record);					   

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
	
 	//单选
 	var workZoneSite_check_select = new Ext.grid.CheckboxSelectionModel(
 	{
 		singleSelect:true
 	}
 	);
 
	
    //ColumnModel定义
	var workZoneSite_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),workZoneSite_check_select, 
            {
                header: '标签栏名称',
                dataIndex: 'workItemName',
                width: 300,
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
                    listClass: 'x-combo-list-small'
                })
            }
//            , {
//                header: '排序号',
//                dataIndex: '_order',
//                width: 50,
//                align: 'center',
//	            editor: new Ext.ux.form.SpinnerField({
//	                allowBlank: false
//	            })
//            }
        ]
    });

	// Store定义
	var workZoneSite_dataUrl ='getAllWorkZoneSiteListAction';// 'getAllUserworkzonesiteListByWorkzoneId.action'; //2012-5-21 gmh modify
	var workZoneSite_store = new Ext.data.JsonStore({
	    url: workZoneSite_dataUrl,
	    root: 'result',
	    fields: ['id', 'workItemName', 'type','layout']
	    //sortInfo: {field:'_order', direction:'ASC'}
	    	
	    	
	});
	
    //GRID定义
    var workZoneSite_grid = new Ext.grid.GridPanel({
        store: workZoneSite_store,
        id:'workzonesite_grid',
        cm: workZoneSite_cm,
        loadMask: {msg:'正在加载数据……'},
        sm:workZoneSite_check_select,
        width: 350,
        border:false,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [workZoneSite_editor],
        // specify the check column plugin on the grid so the plugin is initialized
        listeners: { 
    		//行单击
    		rowclick:function(grid_,rowIndex,e){
    			var record = grid_.getStore().getAt(rowIndex);
    			//alert(record.get('id'));
    			if(record.data.type==1){ //只是portal类型的才调用下面方法，展开门户portal的配置选项
    			   workzonesitelayoutAction(record);
    			}else{
    				//否则置空
    				Ext.getCmp('east-panel').setTitle('工作空间标签布局配置');
    	          Ext.get('workzonesitelayout_frame').dom.src='';
    			}
    		}
    	},
        tbar: [
    	{
            text: '添加标签栏',
            iconCls:'silk-add',
            handler : function(){
                // access the Record constructor through the grid's store
                var count = workZoneSite_grid.getStore().getCount();
                var lastRecord = workZoneSite_grid.getStore().getAt(count-1);
                var order_ = 0;
                if(count!=0){
                	order_ = parseInt(lastRecord.data.order)+1;
                }
                
//                if(workZone_grid.getSelectionModel().getSelected()==null){
//                	alert("请先选择工作空间!");
//					return false; 
//                }
                
//                var userWorkZoneId = workZone_grid.getSelectionModel().getSelected().data.id;
                
               
                var p = new workZoneSite({
                	id:'',
//                    userWorkZoneId: userWorkZoneId,
                    workItemName: '请输入标签栏名称',
                    type: '1'
//                    _order: order_
                });
                workZoneSite_editor.stopEditing();
                workZoneSite_store.insert(count, p);
                workZoneSite_grid.getView().refresh();
                workZoneSite_grid.getSelectionModel().selectRow(count);
                workZoneSite_editor.startEditing(count);             
            }
        },
    	{
            text: '删除标签栏',
            iconCls:'silk-delete',
            handler : function(){
				var ss = workZoneSite_grid.getSelectionModel().getSelections();
				var countt = workZoneSite_grid.getSelectionModel().getCount();
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
				   url:'deleteWorkZoneSiteListAction',// 'deleteUserworkzonesiteConfig.action',  //2012-5-21 gmh modify
				   params: { ids:ids},
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		workZoneSite_store.reload();
				   },
				   failure:function(){
				   
				   }
				});

		    }
        },{
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		workZoneSite_store.reload();
	    	}
   		}
        ]
    });

    
    
    // manually trigger the data store load
    //workZoneSite_store.load({baseParams:{'WorkZoneId':''}});

    workZoneSite_store.on('load',function(){
    	
    	var count = workZoneSite_store.getCount();
    	if(count>0){
    		workZoneSite_check_select.selectFirstRow();
    		var record = workZoneSite_store.getAt(0);
    		workzonesitelayoutAction(record);
    	}else{
    		Ext.get('workzonesitelayout_frame').dom.src='';
    	}  	
    })
    
    workZoneSite_store.load();

//---------------------工作空间标签栏配置------------------------begin--

//var userWorkZoneSiteId = "";

function workzonesitelayoutAction(record){
	var id = record.get('id');
	var workItemName = record.get('workItemName');
//	var userWorkZoneId = record.get('userWorkZoneId');
	var layoutVal = record.get('layout');	
	var type = record.get('type');
	Ext.get('workZoneSiteId').dom.value=id;
	Ext.getCmp('east-panel').setTitle('工作空间标签布局配置——>'+workItemName);
	Ext.get('workzonesitelayout_frame').dom.src='workzonesitelayoutConfgiIndexAction.action?workZoneSiteId='+id+'&layoutVal='+layoutVal+'&type='+type;
	
}



//---------用户工作空间相关配置--begin--------
var userConfigPanel = new Ext.Panel({
    layout:'border',
    height:height,
    items:[{
        region:'center',
        id:'center-panel',
        iconCls:'icon-cog_edit',
        title:'工作空间标签配置',
        layout:'fit',
        split:false,
        border:false,
        frame:true,
        items:[workZoneSite_grid]
    },{
        region:'east',
        id:'east-panel',
        iconCls:'icon-cog_edit',
        width:450,
        layout:'fit',
        split:false,
        border:false,
        frame:true,
        title:'工作空间标签布局配置',
        html:'<IFRAME name="workzonesitelayout_frame" id="workzonesitelayout_frame" style="WIDTH: 100%; HEIGHT: 100%;" src="" frameBorder=0 scrolling=auto></IFRAME>'
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