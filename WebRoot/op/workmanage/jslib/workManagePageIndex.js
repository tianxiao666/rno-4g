
var Forum = {};

var treeId="forum-tree";

Ext.onReady(function(){
    Ext.QuickTips.init();
    
    //var height = Ext.get(document.body).getHeight();
	//var width = Ext.get(document.body).getWidth();
	
	var type = document.getElementById("type").value;
	var permission_id = document.getElementById("permission_id").value;
	var defaultAction = "";
	var dataUrl = "getChildrenMenuListByParentIdAndTypeAction?menuType="+type+"&permission_id="+permission_id;
	
	var treeLoader = new Ext.tree.TreeLoader({
            dataUrl:dataUrl
     });
	
	treeLoader.on("load", function(treeLoader, node) {
		var tree_ = Ext.getCmp("forum-tree");
		tree_.getRootNode().cascade(function(node){
		    if (node.attributes.isDefault==1) {
		    	for(var key in node.attributes){
		    		//console.log(key+"==="+node.attributes[key]);
		    	}
		    	//alert(node.attributes.href);
		    	var defaultURL = node.attributes.href;
		    	defaultAction = defaultURL;
		    	Ext.getCmp("center-panel").setTitle(node.text);
		    	document.getElementById("main_right").src=defaultAction;
		    }
		});
    }, this);
    
    
	
	//che.yd增加判断是否弹出新窗口链接
	if(isNewUrl){
		var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[
	            new Ext.tree.TreePanel({
	                id:'forum-tree',
	                region:'west',
	                title:"<a href='javascript:loadWorkzoneUrl(\""+workzoneUrl+"\")' style='text-decoration:none;color:#15428B;'>"+workzonesiteName+"</a>",
	                split:true,
	                width: 200,
	                minSize: 100,
	                maxSize: 300,
	                collapsible: true,
	                margins:'0 0 0 0',
	                //loader: new Forum.TreeLoader(),
	                rootVisible:false,
	                lines:false,
	                autoScroll:true,
	                loader: treeLoader,
			        root: {
			            nodeType: 'async',
			            text: type,
			            draggable: false,
			            id: '0'
			        },listeners: {
		            	"click": function(node, e) {
			                if (node.expanded == false) {
			                    node.expand();
			                }
			                else {
			                    node.collapse();
			                }
			                if(node.leaf==true){	
			                	var href_ = node.attributes.href;
			                	var target_ = node.attributes.hrefTarget;
			                	window.open(href_,target_);  	
			                	Ext.getCmp("center-panel").setTitle(node.text);
			                }
			                
			            }
	        		}
	            }),
				{
	                region: 'center',
	                id: 'center-panel', 
	                title:'工作区',
	                split: false,                
	                margins: '0 0 0 0',                
	                html:'<IFRAME name="main_right" id="main_right" style="WIDTH: 100%; HEIGHT: 100%" src="" frameBorder=0 scrolling=auto></IFRAME>'
	            }
	         ]
	    });
	}else{
		var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[
	            new Ext.tree.TreePanel({
	                id:'forum-tree',
	                region:'west',
	                title:workzonesiteName,
	                split:true,
	                width: 200,
	                minSize: 100,
	                maxSize: 300,
	                collapsible: true,
	                margins:'0 0 0 0',
	                //loader: new Forum.TreeLoader(),
	                rootVisible:false,
	                lines:false,
	                autoScroll:true,
	                loader: treeLoader,
			        root: {
			            nodeType: 'async',
			            text: type,
			            draggable: false,
			            id: '0'
			        },listeners: {
		            	"click": function(node, e) {
		            		//removeSelectNodeCls(treeId);
			                if (node.expanded == false) {
			                    node.expand();
			                }
			                else {
			                    node.collapse();
			                }
			                if(node.leaf==true){
			                	var href_ = node.attributes.href;    
			                	var target_ = node.attributes.hrefTarget;
			                	var text=node.attributes.text;
			                	window.open(href_,target_);
			                	Ext.getCmp("center-panel").setTitle(node.text);
			                }
			                
			            }
	        		}
	            }),
				{
	                region: 'center',
	                id: 'center-panel', 
	                title:'工作区',
	                split: false,                
	                margins: '0 0 0 0',
	                //width:width,
	                html:'<IFRAME name="main_right" id="main_right" style="WIDTH: 100%; HEIGHT: 100%" src="" frameBorder=0 scrolling=auto></IFRAME>'
	            }
	         ]
	    });
	}
    
});


function loadWorkzoneUrl(targetUrl){
	window.open(targetUrl);
}

function removeSelectNodeCls(treeId){
	var tree_ = Ext.getCmp(treeId);
	tree_.getRootNode().cascade(function(node){
	    if (node.attributes.isDefault==1) {
	    	var nodeId=node.attributes.id;
	    	$(".x-tree-selected").removeClass("x-tree-selected");
	    	
	    	//removeClass (  string cls  ) : Ext.Component 
	    	
	    }
	});
}


