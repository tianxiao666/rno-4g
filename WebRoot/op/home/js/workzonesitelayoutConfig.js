Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	
var userWorkZoneSiteId = Ext.get('userWorkZoneSiteId').dom.value;
var layoutVal = Ext.get('layoutVal').dom.value;
var type = Ext.get('type').dom.value;
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();



var radioId="portalLayoutRadio_"+userWorkZoneSiteId;

var portalLayoutHTML='<div style="top:0px;">';
portalLayoutHTML+='<table align="left" ><tr>';
portalLayoutHTML+='<td valign="top"><div style="margin-left: 10px;"><input type="radio"   checked=true value="1{1:}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/frameset01.png"/></div></td>';
portalLayoutHTML+='<td valign="top">';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="2{0.495:0.495}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-2-1.png"/></div>';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="2{0.3:0.695}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-2-2.png"/></div>';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="2{0.7:0.3}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-2-3.png"/></div>';
portalLayoutHTML+='</td>';
portalLayoutHTML+='<td valign="top">';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="3{0.33:0.33:0.33}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-3-1.png"/></div>';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="3{0.199:0.6:0.199}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-3-2.png"/></div>';
portalLayoutHTML+='</td>';
portalLayoutHTML+='<td valign="top">';
portalLayoutHTML+='<div style="margin-left: 10px;"><input type="radio"  value="4{0.25:0.25:0.25:0.25}" name="'+radioId+'" id="'+radioId+'"/><img src="op/portal/resource/images/portal-4.png"/></div>';
portalLayoutHTML+='</td>';
portalLayoutHTML+='</tr></table>';



var fm = Ext.form;	
/*
//---------------------门户组件类型------------------------begin--
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
				   url: 'saveUserportalitemAction.action',
				   params: { userportalitemJson:json },
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
						parent.Ext.get('isModify').dom.value='1';
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
                header: '门户组件名称',
                dataIndex: 'portalItemName',
                width: 140,
                align: 'center'
            }, {
                header: '组件高度(px)',
                dataIndex: 'portalHeight',
                width: 80,
                align: 'center',
	            editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false,
	                minValue: 0
	            })
            }
        	, {
                header: '列位置',
                dataIndex: '_column',
                width: 50,
                align: 'center',
	            editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false,
	                minValue: 1
	            })
            }
            , {
                header: '列中位置',
                dataIndex: '_order',
                width: 70,
                align: 'center',
	            editor: new Ext.ux.form.SpinnerField({
	                allowBlank: false,
	                minValue: 1
	            })
            }
        ]
});

// Store定义
var dataUrl = 'getUserportalitemListByUserworkzonesiteId.action?userworkzonesiteId='+userWorkZoneSiteId;
var store = new Ext.data.JsonStore({
	    url: dataUrl,
	    loadMask: {msg:'正在加载数据……'},
	    root: 'result',
	    fields: ['id', 'userWorkZoneSiteId', 'portalItemId', '_column','portalHeight','portalItemName','_order']
	    //sortInfo: {field:'order', direction:'ASC'}
	    	
	    	
});
	
//GRID定义
var grid = new Ext.grid.GridPanel({
        store: store,
        id:'grid_',
        cm: cm,
        sm:check_select,
        width: width*1.5,
        height: 250,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false,
        plugins: [editor],
        tbar: [
    	{
            text: '添加门户组件',
            iconCls:'silk-add',
            handler : function(){
	    			parent.Ext.getCmp('itemWindow_grid_').getStore().load();    			
	    			var itemWindow_win = parent.Ext.getCmp('itemWindow_');    			
	    			itemWindow_win.show();      
	    	}
        },
    	{
            text: '删除门户组件',
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
				   url: 'deleteUserportalitemAction.action',
				   params: { id:ids},
				   success: function(){
				   		var msg = Ext.MessageBox.alert('提示', '删除成功!');		
						setTimeout(function(){Ext.MessageBox.hide()},1000);
				   		store.reload();
				   		parent.Ext.get('isModify').dom.value='1';
				   },
				   failure:function(){
				   
				   }
				});
		    }
        },{
			text: '刷新数据',
	        iconCls:'silk-table-refresh',
	        handler : function(){
	    		store.reload();
	    	}
   		}
        ]
});

 store.load({});


//---------------------门户组件类型------------------------end--

*/
 
 
 
    
////------------------应用框架类型---------------------------- begin--
// //多选
// var check_select2 = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
// 
//	
////ColumnModel定义
// var cm2 = new Ext.grid.ColumnModel({
//        defaults: {
//            sortable: true // columns are not sortable by default           
//        },
//        columns: [
//        	new Ext.grid.RowNumberer(),check_select2, 
//            {
//                header: '应用框架组件名称',
//                dataIndex: 'title',
//                width: 150,
//                align: 'center'
//            }, {
//                header: '应用URL',
//                dataIndex: 'url',
//                width: 300,
//                align: 'center'
//            }
//        ]
//});
//
//// Store定义
//var dataUrl2 = 'getFrameItemList.action';
//var store2 = new Ext.data.JsonStore({
//	    url: dataUrl2,
//	    root: 'result',
//	    fields: ['id', 'title', 'showTitle', 'type','url']
//	    //sortInfo: {field:'order', direction:'ASC'}
//});
//	
////GRID定义
//var grid2 = new Ext.grid.GridPanel({
//        store: store2,
//        cm: cm2,
//        sm:check_select2,
//        width: width*0.9,
//        height: 450,
//        //autoExpandColumn: 'common', // column with this id will be expanded
//        header:false,
//        frame: false
//});
//
// store2.load({});
// 
// store2.on('load',function(){		
// 	if(type!="1"){
//		framesetSelectAction();
//	}
//})
// 
// 
// 
///*
////单选radio近扭
//var gridEl=grid2.getEl(); 
//gridEl.select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker'); //删除表头的checkbox 
//grid2.store.on('load', function() { //数据加载完毕替换为checkbox列增加一个class，然后我们在这个class中修改图片 
//     gridEl.select("div[class=x-grid3-row-checker]").each(function(x) { 
//     x.addClass('x-grid3-row-radioBtn'); 
//     }); 
//});
//
//grid2.getView().on('rowupdated', function() { //数据加载完毕替换为checkbox列增加一个class，然后我们在这个class中修改图片 
//     gridEl.select("div[class=x-grid3-row-checker]").each(function(x) { 
//     x.addClass('x-grid3-row-radioBtn');   
//     }); 
//});
//*/
//    
////---------------------应用框架类型------------------------end--
//



var formPanle = new Ext.FormPanel({
        labelWidth: 75, // label settings here cascade unless overridden
        frame:false,
        header:false,
        layout:'fit',
        monitorResize:true,
        bodyStyle:'padding:5px 5px 0',
        height:height, 
        width: width,
        tbar: [
    	{
            text: '保存',
            iconCls:'icon-save',
            handler : function(){
    			var portal =Checkbox_portal_fd.collapsed;
//    			var frameset =Checkbox_frameset_fd.collapsed;
//    			if(portal==false&&frameset==false){
//    				alert("不能两种类型都配置!");
//    				return false;    				
//    			}
//    			if(portal==true&&frameset==true){
//    				alert("不能两种类型都不配置!");
//    				return false;    				
//    			}
    			//portal类型
    			if(portal==false){
    				var layoutVal = "";
    				var layoutRadios = document.getElementsByName(radioId);
    				for(var i=0;i<layoutRadios.length;i++){
    					if(layoutRadios[i].checked==true){
    						layoutVal = layoutRadios[i].value;
    					}
    				}
    				
    				var json = '{"id":"'+userWorkZoneSiteId+'","layout":"'+layoutVal+'"}';
					//AJAX保存
					Ext.Ajax.request({
					   url: 'saveWorkZoneSiteConfigAction',//'saveUserworkzonesiteConfig.action', 2012-5-21 gmh modify
					   params: { workZonesiteConfig:json },
					   success: function(){
					   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
							setTimeout(function(){Ext.MessageBox.hide()},1000);
					   		var grid_ = parent.Ext.getCmp('workzonesite_grid');
					   		grid_.getStore().reload();
				   			parent.Ext.get('isModify').dom.value='1';

					   },
					   failure:function(){
					   
					   }
					});
    			}
//    			//应用框架类型
//    			else if(frameset==false){
//	        		var s = grid2.getSelectionModel().getSelected();
//	        		if(s==null){
//	        			alert("请选择要添加的对象!");
//	        			return false;        			
//	        		}
//	        		var id = s.data.id;   
//	        		var json ='{"id":"","userWorkZoneSiteId":"'+userWorkZoneSiteId+'","frameId":"'+id+'"}';
//					//AJAX保存
//					Ext.Ajax.request({
//					   url: 'saveUserFramesetlitemAction.action',
//					   params: { userframesetitemJson:json},
//					   success: function(){	
//						    json ='{"id":"'+userWorkZoneSiteId+'","type":"2"}';
//							//AJAX保存
//							Ext.Ajax.request({
//							   url: 'saveUserworkzonesiteConfig.action',
//							   params: { userWorkZonesiteConfig:json},
//							   success: function(){
//							   		var msg = Ext.MessageBox.alert('提示', '保存成功!');		
//									setTimeout(function(){Ext.MessageBox.hide()},1000);
//   							   		var grid_ = parent.Ext.getCmp('workzonesite_grid');
//					   				grid_.getStore().reload();
//					   				parent.Ext.get('isModify').dom.value='1';
//							   },
//							   failure:function(){
//							   
//							   }
//							});    						   		
//					   },
//					   failure:function(){
//					   
//					   }
//					});    
//				
//    			}
            }
        },
    	{
            text: '取消',
            iconCls:'silk-cross',
            handler : function(){
        		window.close();
		    }
        }
        ],
        items: [{
            xtype:'fieldset',
            id:'Checkbox_portal_fd',
            checkboxToggle:true,
            checkboxName:'Checkbox_portal',
            title: '门户组件类型',
            autoHeight:true,
            defaultType: 'textfield',
            collapsed: false,
            items :[{
					xtype:'fieldset',
		            title: '布局',
		            autoHeight:true,
		            html:portalLayoutHTML
		        }
//		        ,{
//					xtype:'fieldset',
//		            title: '门户组件',
//		            layout:'fit',
//		            autoHeight:true,
//		            items:[grid]		            	
//                }
            ]
        }
//        ,{
//            xtype:'fieldset',
//            id:'Checkbox_frameset_fd',
//            title: '应用框架类型',
//            checkboxName:'Checkbox_frameset',
//            collapsed: true,
//            checkboxToggle:true,
//            autoHeight:true,
//            layout:'fit',
//            defaultType: 'textfield',
//            items :[grid2]
//        }
        ]
    });

    formPanle.render(document.body);
    
    var Checkbox_portal_fd =  Ext.getCmp('Checkbox_portal_fd');
    var Checkbox_frameset_fd =  Ext.getCmp('Checkbox_frameset_fd');

    
    //------------初始化布局值-------begin--
	var layoutRadios = document.getElementsByName(radioId);
	for(var i=0;i<layoutRadios.length;i++){
		if(layoutVal==""){
			layoutRadios[0].checked=true;
			break;
		}
		if(layoutRadios[i].value==layoutVal){
			layoutRadios[i].checked=true;
		}
	}
    //------------初始化布局值-------end--
	
	
	
	
	
	


	
	
	//-------------布局选择相关--------begin------
	var portal =Checkbox_portal_fd;
	var frameset =Checkbox_frameset_fd;
	//portal类型
//	if(type=="1"){
//		portal.expand();
//		frameset.collapse();		
//	}else{
//		portal.collapse();
//		frameset.expand();
//		//framesetSelectAction();
//	}	
//	portal.on('expand',function(){		
//		frameset.collapse();	
//	})
//	portal.on('collapse',function(){		
//		frameset.expand();	
//		framesetSelectAction();
//	})	
//	frameset.on('expand',function(){		
//		portal.collapse();	
//	})
//	frameset.on('collapse',function(){		
//		portal.expand();	
//	})

	//-------------布局选择相关--------end------
	
	
//	function framesetSelectAction(){
//		
//		Ext.Ajax.request({
//		   url: 'getUserFramesetitemByUserworkzonesiteId.action',
//		   params: { userworkzonesiteId:userWorkZoneSiteId},
//		   success: function(response, options){
//		   		var obj=Ext.util.JSON.decode(response.responseText); 		
//		   		if(typeof(obj.result)=='undefined'){
//		   			return false;
//		   		}
//		   		var id = obj.result.frameId;	
//		   		for(var i=0;i<store2.getCount();i++){		   			
//		   			if(store2.getAt(i).get('id')==id){
//		   				grid2.getSelectionModel().selectRow(i);
//		   				break;
//		   			}
//		   		}
//		   },
//		   failure:function(){
//		   
//		   }
//		});    		
//
//	}
//	
	
	

    
});