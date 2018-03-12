Ext.onReady(function(){
	new Ext.Viewport({
    layout: 'border',
    items: [{
        region: 'west',
        collapsible: true,
        title: '自助服务',
        xtype: 'treepanel',
        width: 200,
        autoScroll: true,
        split: true,
        lines:false,
        loader: new Ext.tree.TreeLoader(),
        root: new Ext.tree.AsyncTreeNode({
            expanded: true,
            children:[{
            	text:'用户个人资料',
            	leaf:false,
            	cls:'forum-ct',
            	iconCls:'forum-parent',
            	expanded:true,
            	children:[{
            		id:'viewandadd',
	                text: '个人资料查看/修改',
	                cls:'forum',
            	    iconCls:'icon-forum',
            	    href:'loadSelfServiceInfoViewAndEditAction',
            	    hrefTarget:'main_right',
	                leaf: true
	            }, {
	                text: '用户账号密码修改',
	                cls:'forum',
            	    iconCls:'icon-forum',
            	    href:'loadSelfServiceChangePasswordAction',
            	    hrefTarget:'main_right',
	                leaf: true
	            }]
	        }]
        }),
        rootVisible: false,
        listeners: {
            click: function(n) {
                //Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.attributes.text + '"');
            }
        }
    }, {
        region: 'center',
        id: 'center-panel', 
        title:'工作区',
        header:false,
        split: false,                
        margins: '0 0 0 0',  
        //
        html:'<IFRAME name="main_right" id="main_right" style="WIDTH: 100%; HEIGHT: 100%" src="loadSelfServiceInfoViewAndEditAction" frameBorder=0 scrolling=auto></IFRAME>'
    }]
});
});
