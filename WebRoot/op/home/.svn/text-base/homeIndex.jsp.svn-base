<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'homeIndex.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<%-- ###ExtJS必须脚本#begin## --%>
		<script type="text/javascript"
			src="../../jslib/extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="../../jslib/extjs/ext-all.js"></script>
		<link rel="stylesheet" type="text/css"
			href="../../jslib/extjs/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css"
			href="../../jslib/extjs/resources/css/ext-patch.css">
		<link rel="stylesheet" type="text/css"
			href="../../jslib/extjs/plugin/ux/css/Portal.css">
		<link rel="stylesheet" type="text/css"
			href="../../jslib/extjs/plugin/portal/sample.css">
		<%-- ###ExtJS必须脚本#end## --%>
		<%-- extensions --%>
		<script type="text/javascript"
			src="../../jslib/extjs/plugin/ux/Portal.js"></script>
		<script type="text/javascript"
			src="../../jslib/extjs/plugin/ux/PortalColumn.js"></script>
		<script type="text/javascript"
			src="../../jslib/extjs/plugin/ux/Portlet.js"></script>
	<style>
		
body {
    font-size: 12px;
    height: 100%;
    margin: 0;
    padding: 0 0 0;
    width: 100%;
}
iframe {
    height: 100%;
    margin: 0;
    padding: 0 0 0;
    width: 100%;
}
		
	</style>
  </head>
  <script type="text/javascript">
  $(function(){
  	//加载登录用户门户组件
  	$.ajax({
		url: "getHmoeItemByRoleIdAndAccountAction",
		async:false,
		type:"POST",
		dataType : 'json',
		success : function(result) {
			var context = "";
			for(var i = 0;i <= 2;i++){
				//对应列数据不为空
				if(result.homeItem[i]){
					context = context + "{";
						context = context + "columnWidth:";
							if(i == 0 || i == 2){
								context = context + "0.197,";
							}else{
								context = context + "0.597,";
							}
						context = context + "style:'padding:3px 0 3px 3px',items:[ ";
						$.each(result.homeItem[i],function(k,v){
								context = context + "{id:'"+v.home_item_id+"',";
								context = context + "title:'"+v.title+"',";
								if(v.showtitle != null && v.showtitle == 1){
									context = context + "header:true,";
								}else{
									context = context + "header:false,";
								}
								context = context + "height:"+v.item_height+",";
								context = context + "draggable: false,tools:["
													+"{"
													+"id:'close',"
													+"qtip:'关闭组件',"
													+"handler: function(e, target, panel){"
													+"portletCloseAction(panel,"+v.home_item_id+","+v.role_id+","+result.sysOrgUser.orgUserId+");"
													+"}},";
								if(v.maxurl != null && v.maxurl != ""){
									context = context + "{id:'maximize',"
														+"qtip:'更多信息',"
														+"handler: function(){";
									if(v.url.indexOf("http://") >= 0){
										context = context + "window.open('"+v.maxurl+"','_blank');}},";
									}else{
										context = context + "window.open('../../"+v.maxurl+"','_blank');}},";
									}
								}
								if(context != null && context != "") {
									context = context.substring(0,context.length - 1);
								}
								context = context + "],";
								context = context + "html: ";
								if(v.url != null){
									if(v.url.indexOf("http://") >= 0){
										context = context + "\"<iframe src='"+v.url+"' frameBorder=0 scrolling=auto ></iframe>\""
														  + "},";
									}else{
										context = context + "\"<iframe src='../../"+v.url+"' frameBorder=0 scrolling=auto ></iframe>\""
														  + "},";
									}
								}
					});
						if (context != null && context != "") {
							context = context.substring(0,context.length - 1);
						}
						context = context + "]},";
				}else{
				//对应列数据为空
				//生成空的extjs组件
				context = context + "{";
						context = context + "columnWidth:";
							if(i == 0 || i == 2){
								context = context + "0.197,";
							}else{
								context = context + "0.597,";
							}
						context = context + "style:'padding:3px 0 3px 3px',items:null},";
				}
			}
		if (context != null && context != "") {
			context = context.substring(0,context.length - 1);
		}
		var itemsDate = eval("["+context+"]");
		createViewPort(itemsDate)
		}
	});
  	$("#maininnerdivif").css("height", "600px");
  	
				
			})

		//创建门户extjs组件
		function createViewPort(itemsDate){
			Ext.QuickTips.init();	
			Ext.onReady(function(){
				var viewport = new Ext.Viewport({
				layout:'border',
				items:[{
				xtype:'portal',
				frame:false,
				border:false,
				region:'center',
				margins:'0 0 0 0',
				items:itemsDate
				}]
				});
				});
		}			
			
				//组件关闭删除
		    	function portletCloseAction(panel,item_id,role_Id,org_user_id){
                    if(!confirm("删除以后，以后将不再显示此组件内容，是否继续?")){
                    	return false;                    	
                    }else{
                    	$.ajax({
                    		//关闭用户门户控件并且记录到个人设置中
							url: "closeUserHomeItemAction",
							async:false,
							type:"POST",
							data : {"orgUserId":role_Id,"homeItemId":item_id,"roleId":org_user_id},
							success : function(result) {
								if(result == "1"){
                    				panel.ownerCt.remove(panel, true);
								}
                    		}
                    		});
                    }
                }
  </script>
  <body style="height: 800px;">
  	<input type="hidden" id="totalHeight" value="800px" />
  </body>
</html>
