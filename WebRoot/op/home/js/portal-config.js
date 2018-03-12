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
        children:[
           {id:'basicresource',text:'基础数据',leaf:false, expanded:true,children:[
              {id:'projectT',text:"门户方案配置",leaf:true,url:"op/portal/portalProject-config.jsp"},
	          {id:'workzoneT',text:"工作空间配置",leaf:true,url:"op/portal/workzone-config.jsp"},
	          {id:'workzoneSiteT',text:"功能标签配置",leaf:true,url:"op/portal/workzoneSite-config.jsp"},
	          {id:'portalItemT',text:"门户组件配置",leaf:true,url:"op/portal/portalItem-config.jsp"},
	          {id:'sysInfoT',text:"系统信息配置",leaf:true,url:"op/portal/sysconfig-config.jsp"},
	          {id:'sysfooterUrlT',text:"友情链接配置",leaf:true,url:"op/portal/sysfooterurl-config.jsp"},
	          /*{id:'orgroleT',text:'组织角色配置',leaf:true,url:'op/portal/orgrole-config.jsp'}*/
           ]},
           {
           	 id:'relationresource',text:'基本数据关系配置',leaf:false, expanded:true,children:[
                 /*{id:'userGroupT',text:'用户群与门户方案关系配置',leaf:true,url:'op/portal/userGroupPortalProjectRelation-config.jsp'},*/
                 {id:'portalProjectT',text:"门户方案与工作空间及功能标签关系配置",leaf:true,url:"op/portal/portalProjectAndWorkZoneWorkZoneSiteRelation-config.jsp"},
                 /*{id:'orgRoleT',text:'组织角色与门户组件关系配置',leaf:true,url:'op/portal/orgRolePortalItemRelation-config.jsp'}*/
           	 ]
           },{
           	 id:'templateRelations',text:'模板关系配置',leaf:false,expanded:true,children:[
           	     {id:'scheme-workzone-workzonesiteT',text:'门户方案下工作空间的功能标签配置',leaf:true,url:'op/portal/schemeWorkZoneWorkZoneSite-config.jsp'},
           	     {id:'scheme-orgrole-workzonesite-itemT',text:'门户方案组织角色决定的功能标签下的门户组件',leaf:true,url:'op/portal/schemeOrgRoleWorkZoneSiteItem-config.jsp'}
           	 ]
           }
	      
	       //{id:'focusNewsUrlT',text:"焦点新闻管理",leaf:true,url:"portal/workzone-config.jsp"},
	       //{id:'portalInfopubUrlT',text:"门户信息发布",leaf:true,url:"portal/workzone-config.jsp"}
//	       {id:'portalRoleT',text:"角色配置",leaf:true}
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
	        width: 380,//145,
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
    
    
});