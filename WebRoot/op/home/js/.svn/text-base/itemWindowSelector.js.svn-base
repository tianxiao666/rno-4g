Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){

var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();


//------------门户组件添加窗口-------begin--
 //多选
var itemWindow_check_select = new Ext.grid.CheckboxSelectionModel();
 
	
//ColumnModel定义
 var itemWindow_cm = new Ext.grid.ColumnModel({
        defaults: {
            sortable: true // columns are not sortable by default           
        },
        columns: [
        	new Ext.grid.RowNumberer(),itemWindow_check_select, 
            {
                header: '门户组件名称',
                dataIndex: 'title',
                width: 150,
                align: 'center'
            }, {
                header: '组件URL',
                dataIndex: 'url',
                width: 280,
                align: 'center'
            }
        ]
});

// Store定义
var itemWindow_dataUrl = 'getPortalItemList.action';
var itemWindow_store = new Ext.data.JsonStore({
	    url: itemWindow_dataUrl,
	    root: 'result',
	    fields: ['id', 'title', 'showTitle', 'type','url']
	    //sortInfo: {field:'order', direction:'ASC'}	    	
});
	
//GRID定义
var itemWindow_grid = new Ext.grid.GridPanel({
        store: itemWindow_store,
        id:'itemWindow_grid_',
        cm: itemWindow_cm,
        sm:itemWindow_check_select,
        width: width*0.9,
        height: 130,
        //autoExpandColumn: 'common', // column with this id will be expanded
        header:false,
        frame: false
});

 //itemWindow_store.load({});




var itemWindow_win = new Ext.Window({
    applyTo:'itemWindow',
    id:'itemWindow_',
    title:'添加门户组件',
    layout:'fit',
    floating:true,
    shim:false,
    //animEl: parent.Ext.getBody(),
    width:500,
    height:300,
    closeAction:'hide',
    plain: true,	
    items:[
    	itemWindow_grid
    ],	
    buttons: [{
        text:'确定',
        handler:function(){
    		var ss = itemWindow_grid.getSelectionModel().getSelections();
    		var count = itemWindow_grid.getSelectionModel().getCount();
    		if(count==0){
    			Ext.MessageBox.alert('提示', '请选择要添加的对象!');
    			return false;        			
    		}
    		var ids  ="";
    		for(var i=0;i<count;i++){
    			record  = ss[i];
    			if(count==1){
    				ids+=record.data.id;
    			}else{
    				ids+=record.data.id+";";
    			}
    			
    		}
    		var json ='{"id":"","userWorkZoneSiteId":"'+Ext.get('userWorkZoneSiteId').dom.value+'","portalItemId":"'+ids+'","portalHeight":"300","_column":"1","_order":"10"}';
			//AJAX保存
			Ext.Ajax.request({
			   url: 'saveUserportalitemAction.action',
			   params: { userportalitemJson:json},
			   success: function(){
			   		itemWindow_win.hide();
			   		var iframe_ = window.frames["workzonesitelayout_frame"];
			   		var store = iframe_.Ext.getCmp('grid_').getStore();			   		
			   		store.reload();
			   },
			   failure:function(){
			   
			   }
			});    		
        }
    },{
        text: '取消',
        handler: function(){
            itemWindow_win.hide();
        }
        }]
});
//------------门户组件添加窗口-------end--
	
    
});