Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";

Ext.onReady(function(){
	

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
var height = Ext.get(document.body).getHeight();
var width = Ext.get(document.body).getWidth();

var fm = Ext.form;	








var tabs = new Ext.TabPanel({
    resizeTabs:true, // turn on tab resizing
    minTabWidth: 115,
    tabWidth:135,
    border:true,
    frame:true,
    activeTab:0,
    enableTabScroll:true,
    defaults: {autoScroll:true},
    items:[
   	]    	
});



//导航树-------------begin-----
var rootNode=new Ext.tree.AsyncTreeNode({
        text: '门户后台管理',
        id: 'root',
        expanded:true,
        leaf:false,
        children:[  //yuan.yw
           {id:'portalItemT',text:"门户组件",leaf:true,url:"op/home/homeItemConfig.jsp"},
           {id:'scheme-orgrole-workzonesite-itemT',text:'角色门户组件配置',leaf:true,url:'op/home/roleHomeItemConfig.jsp'}
       ]
});


var navigation_tree = new Ext.tree.TreePanel({    
    autoScroll: true,
    animate: true,
    containerScroll: true,
    border: false,
    root: rootNode,
    listeners: {
    	 beforeclick : function( node, e ){
    	 	 if(!node.attributes.url){
    	 	 	return false;
    	 	 }else{
    	 	 	return true;
    	 	 }
    	 },
            click: function(n) {
				var tabId = 'tab_'+n.id;
				var tab = Ext.getCmp(tabId);
                //Ext.Msg.alert('提示', n.text);
				if(n.id!=null){
					if(tab==null){
						addViewIframeTab(n);
					}else{
						tabs.setActiveTab(tabId);
					}					
				}

            }
   }

});




function addViewIframeTab(node){	
        tabs.add({
            title: node.text,
            id:'tab_'+node.id,
            layout:'fit',
            iconCls: 'icon-config',
            html:'<IFRAME name="iframe_'+node.id+'" id="iframe_'+node.id+'" style="WIDTH: 100%; HEIGHT: 100%;" src="'+node.attributes.url+'" frameBorder=0 scrolling=auto></IFRAME>',
		   	closable:true
        }).show();
}


function addViewTab(node,tabId){	
        tabs.add({
            title: node.text,
            id:'tab_'+node.id,
            layout:'fit',
            iconCls: 'icon-config',
           	items:[tabId]
            //closable:true
        }).show();
}

//导航树-----------------end----





var viewport = new Ext.Viewport({
    layout: 'border',
    items: [
    	{
			region: 'west',
	        id: 'view-west-panel', 
	        iconCls:'icon-folder_go',
	        title: '门户后台配置导航',
	        split: false,
	        width:180,//145,
	        minSize: 100,
	        maxSize: 400,
	        //frame:true,
	        //border:true,
	        collapsible: true,
	        margins: '0 0 0 0',
	        items:[navigation_tree]
    	},{
    		region: 'center', 
    		layout:'fit',
    		items:[
    			tabs
    		]
    	}
    	
    ]
});
tabs.add({//初始加载
        title: '门户组件',
        id:'tab_portalItemT',
        layout:'fit',
        iconCls: 'icon-config',
        html:'<IFRAME name="iframe_portalItemT" id="iframe_portalItemT" style="WIDTH: 100%; HEIGHT: 100%;" src="op/home/homeItemConfig.jsp" frameBorder=0 scrolling=auto></IFRAME>',
 	closable:true
    }).show();

});