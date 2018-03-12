Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";




Ext.onReady(function(){
	Ext.QuickTips.init();
	var userId = document.getElementById("userId").value;
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();	
	var fm = Ext.form;
	
	var workZone = Ext.data.Record.create([{
        name: 'id',
        type: 'string'
    }, {
        name: 'userId',
        type: 'string'
    }, {
        name: 'zoneName',
        type: 'string'
    },{
        name: '_order',
        type: 'String'
    }]);
	
	//更新插件
 	var editor = new Ext.ux.grid.RowEditor({
        saveText: '保存',
        cancelText: '取消',
        listeners: { 
 			beforeedit:function(RowEditor){
 				var grid = this.grid;
				var store = grid.getStore();
				var record = grid.getSelectionModel().getSelected();
				//alert(record.data.userId);
				if(record==null){
					return false;
				}
				if(record.data.userId=='default'){
					alert("默认工作空间不能修改!");
					return false;
				}
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
				   url: 'saveUserworkzoneConfig.action',
				   params: { userWorkZoneConfig:json,userId:userId },
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		Ext.get('isModify').dom.value='1';
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
 	var check_select = new Ext.grid.CheckboxSelectionModel();
 
	
    //ColumnModel定义
	var cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),check_select, 
            {
                header: '是否显示',
                dataIndex: 'state',
                width: width*0.2,
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
                width: width*0.4,
                align: 'center',
                editor: new fm.TextField({
                    allowBlank: false,
                    selectOnFocus:true
                })
            }, {
                header: '排序号',
                dataIndex: '_order',
                width: width*0.3,
                align: 'center',
	            editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false
	            })
            }
        ]
    });

	// Store定义
	var dataUrl = 'getUserworkzoneInfoByUserId.action?userId='+userId;
	var store = new Ext.data.JsonStore({
	    url: dataUrl,
	    root: 'result',
	    fields: ['id', 'userId', 'zoneName', 'state','_order']
	    //sortInfo: {field:'_order', direction:'ASC'}
	});
	
    //GRID定义
    var grid = new Ext.grid.GridPanel({
        store: store,
        cm: cm,
        sm:check_select,
        renderTo: 'editor-grid',   
        loadMask: {msg:'正在加载数据……'},
        width: width,
        height: height,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: true,
        plugins: [editor],
        // specify the check column plugin on the grid so the plugin is initialized
        tbar: [
    	{
            text: '增加工作空间',
            iconCls:'silk-add',
            handler : function(){
                // access the Record constructor through the grid's store
                var count = grid.getStore().getCount();
                var lastRecord = grid.getStore().getAt(count-1);
                
                var p = new workZone({
                	id:'',
                    userId: userId,
                    zoneName: '请输入工作空间名称',
                    state: '1',
                    _order: parseInt(lastRecord.data._order)+1
                });
                editor.stopEditing();
                store.insert(count, p);
                grid.getView().refresh();
                grid.getSelectionModel().selectRow(count);
                editor.startEditing(count);             
            }
        },
    	{
            text: '删除工作空间',
            iconCls:'silk-delete',
            handler : function(){
        		//var s = grid.getSelectionModel().getSelected();
        		var ss = grid.getSelectionModel().getSelections();
        		var countt = grid.getSelectionModel().getCount();
        		if(countt==0){
        			alert("请选择要删除的对象!");
        			return false;        			
        		}
        		var defaultName = "";
        		var ids  ="";
        		for(var i=0;i<countt;i++){
        			record  = ss[i];
        			ids+=record.data.id+";";
        			if(record.data.userId=='default'){
        				defaultName+=record.data.zoneName+"\n";
        			}
        		}
        		if(defaultName!=''){
        			alert(defaultName+"以上选择项为默认空间，不能删除，请重新选择!");
        			return false;
        		}        		
       		
        		if(!confirm("确定要删除选定的对象?")){
        			return false;
        		}        		
				//AJAX删除
				Ext.Ajax.request({
				   url: 'deleteUserworkzoneConfig.action',
				   params: { ids:ids},
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		Ext.get('isModify').dom.value='1';
				   		store.reload();
				   },
				   failure:function(){
				   
				   }
				});
				

		    }
        }
        ]
    });

    // manually trigger the data store load
    store.load({});

    
});