<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>对用户设置角色权限</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css" />
<style type="text/css">
.systemService_content{border-top: 1px solid #99BBE8;
    padding: 10px;}
 .orgUserManage_top {
    background: url("../../images/white-top-bottom.gif") repeat-x scroll 0 -1px rgba(0, 0, 0, 0);
    border-bottom: 1px solid #99BBE8;
    color: #15428B;
    font-weight: bold;
    line-height: 26px;
    padding: 0 5px;
}
.orgUserManage_standard_title {
    background-color: #F0F8FF;
    border: 1px solid #DDDDDD;
    height: 30px;
    line-height: 30px;
    margin-top: 5px;
    text-align: center;
}
.UserManage_standard_bottom {
    background-color: #F0F8FF;
    border: 1px solid #DDDDDD;
    height: 30px;
    line-height: 30px;
    margin-top: 25px;
    text-align: center;
}
.orgUserManage_content {
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #99BCE8;
    min-height: 650px;
    overflow: hidden;
    margin:5px;
}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/tab.js"></script>
<script type="text/javascript" src="jslib/authorityManage.js"></script>
<script type="text/javascript" src="jslib/authorityrole.js"></script>
<script type="text/javascript" src="jslib/authorityAccount.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript">
$(function(){
	//系统的点击事件
	$("#systemService_tab li").click(function(){
		$("#systemService_tab li").removeClass("selected");
		$(this).addClass("selected");
		$(".systemService_content").children("div").css("display","none");
		$("#systemService_"+$(this).attr("id")).css("display","block");
<%--		if($(this).attr("id")=="tab_0"){
			initTree("PM");
		}else if($(this).attr("id")=="tab_1"){
			initTree("IOSM");
		}else if($(this).attr("id")=="tab_2"){
			initTree("RNO");
		} --%>
	});
	//默认加载三个系统权限树
	initTree("PM");
	initTree("IOSM");
	initTree("RNO");
	//角色选择的点击事件
	$(".roleid").click(function(){	
		var roleids="";
		$(".roleid:checked").each(function(){
			if(roleids==""){
				roleids+=$(this).val();
			}
			else{
				roleids+=","+$(this).val();
			}
		});
		//通过角色得到权限
		$.ajax({url:"ajaxInitPermissionTreeByRolesAction",data:{roleids:roleids},async:false,dataType:"text",success:function(data){
			var permissionids=data.replace(/"/g,"").split(",");
			$(".permission").each(function(){
				if($(this).attr("disabled")=="disabled"){
					$(this).attr("checked",false);
					$(this).attr("disabled",false);
				}
			});
			$(".permission").each(function(){
				for(var i=0;i<permissionids.length;i++){
					if($(this).val()==permissionids[i]){
							$(this).attr("checked","checked");
							$(this).attr("disabled","disabled");
						break;
					}
					
				}
			
			});
		},type:"post"});
	});
	
});

//根据系统和用户初始化权限树
function initTree(system){
	var myUrl = "ajaxInitPermissionTreeByUserAction";
	var treediv ="";
	var rtree ="";
	if(system=="PM"){
		treediv = "role_permissionTreeDiv1";
		rtree ="rptree1";
	}
	if(system=="IOSM"){
		treediv = "role_permissionTreeDiv2";
		rtree ="rptree2";	
		}
	if(system=="RNO"){
		treediv = "role_permissionTreeDiv3";
		rtree ="rptree3";
	}
	$.post(myUrl,{system:system,orgUserId:$("#orgUserId").val()},function(data){
		createPermissionTreeByRoles(data,treediv,rtree,"","");
	},"json");
}

/**
 * 权限树
 */
function createPermissionTreeByRoles(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachInitPermissionByRolesTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}

/**
 * 递归生成权限树
 */
function foreachInitPermissionByRolesTree(data,ul,tableId,functionName) {
	//var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2.children);
		//dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.children;
		var auth;
		var perAccessText = "";
		if(data2.FLAG != null && data2.FLAG == 1){
			
		    if(data2.ROLE_FLAG== 1){
		    	auth="<input type=\"checkbox\" class=\"permission\" name=\"permission\" disabled=\"disabled\"  checked=\"checked\" value=\""+data2.PERMISSION_ID+"\"></input>";
		    }
		    else auth="<input type=\"checkbox\" class=\"permission\" name=\"permission\"  checked=\"checked\" value=\""+data2.PERMISSION_ID+"\"></input>";
		}else{
		//	auth="";
			auth="<input type=\"checkbox\" class=\"permission\" name=\"permission\"  value=\""+data2.PERMISSION_ID+"\"></input>";
		}
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder'   >"+data2.NAME+auth+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder'  >"+data2.NAME+auth+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachInitPermissionByRolesTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}

//保存用户角色和用户权限
function saveUserRelaPermission(){
    var roleids="";
	$(".roleid:checked").each(function(){
		if(roleids==""){
			roleids+=$(this).val();
		}
		else{
			roleids+=","+$(this).val();	
		}});
	var permissionids="";
	$(".permission:checked").each(function(){
		if($(this).attr("disabled")=="disabled"){
			
		}else{
			if(permissionids==""){
				permissionids+=$(this).val();
			}else{
				permissionids+=","+$(this).val();
			}			
		}
	});
	var myUrl = "setUserRelaPermissionAction";
	$.post(myUrl,{roleids:roleids,permissionids:permissionids,orgUserId:$("#orgUserId").val()},function(data){	
		if(data==0){
			alert("保存失败！");
		}else{
			alert("保存成功！");
		}
	},"text");
}
</script>


</head>
<body>
<div class="role_content clearfix ">
<div class="orgUserManage_content ">

<div class="orgUserManage_top"> 角色权限设置 </div>
<div class="orgUserManage_standard_title">
角色权限设置
</div>
<div style="padding:12px">
<font style="font-size:14px;font-weight: bold;">账号：</font>${account.ACCOUNT }&nbsp;&nbsp;<span style="font-size:14px;font-weight: bold;">姓名：</span>${orgUser.name}
<input type="hidden" value="${orgUser.orgUserId}" name="orgUserId" id="orgUserId"/>
</div>
<%--  
<div>请选择系统：<select id="system">
<s:iterator value="systems"><option name="system" value="code"><s:property  value="name"/> </option><option name="" value=""></s:iterator>
<option name="" value="1">PM系统</option><option name="" value="2">IOSM系统</option></select></div>		--%>
<br/>

<div id="systemService_tab" class="tab_menu">
<ul>
<li id="tab_0" class="selected">PM系统</li>
<li id="tab_1">IOSM系统</li>
<li id="tab_2">网优系统</li>
</ul>
</div>
<div class="systemService_content" style="border-top: 1px solid #99BBE8;">
<%-- PM系统结束--%>
<div id="systemService_tab_0">
<div class="role_userGroups">
<h3>授予角色：</h3>
<ul id="orgrole_role_ul" class="role_userGroups_ul">
<table  id="table1">
		                     <s:iterator value="#request.Roles1" id="role">              
		                 <s:if test="FLAG==1"><tr><td> <input type="checkbox" class="roleid"   name="roleids" checked="checked" value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:if>
		                         <s:else><tr><td> <input type="checkbox" class="roleid"  name="roleids"  value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:else>
		                 </s:iterator>
</table>  
                 
 </ul>

</div>
<div class="role_authorize" >
									<div id="role_org_tab" class="role_authorize_tab tab_menu">
										<h3>
											授予权限：<span style="font-weight:normal; ">（已经选择的角色包含权限在这里只显示，不能取消，但可以增加角色外的权限）</span>
										</h3>
										
									</div>
									<div id="orgrole_function_role_authorize_tab">
										<div class="filetree role_authorizeTree" id="role_permissionTreeDiv1">
											
										</div>
											<input type="hidden" id="role_rela_per_perId" />
											<input type="hidden" id="role_rela_per_roleId"/>
											
									</div>
</div>
</div>
<%-- PM系统结束--%>
<%-- iosm系统开始 --%>
<div id="systemService_tab_1" style="display:none;">
		<div class="role_userGroups">
		<h3>角色：</h3>
		<ul id="orgrole_role_ul" class="role_userGroups_ul">
		<table  id="table2">
				                       <s:iterator value="#request.Roles2" id="role">              
		                 <s:if test="FLAG==1"><tr><td> <input type="checkbox" class="roleid" onclick="checkedRole(this,<s:property value="ROLE_TYPE_ID"/>,<s:property value="ROLE_ID"/>,'<s:property value="CODE"/>','<s:property value="NAME"/>')"  name="roleids" checked="checked" value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:if>
		                         <s:else><tr><td> <input type="checkbox" class="roleid"  onclick="checkedRole(this,<s:property value="ROLE_TYPE_ID"/>,<s:property value="ROLE_ID"/>,'<s:property value="CODE"/>','<s:property value="NAME"/>')" name="roleids"  value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:else>
		                 </s:iterator>
		</table>  
		                 
		 </ul>
		
		</div>
		<div class="role_authorize" >
											<div id="role_org_tab" class="role_authorize_tab tab_menu">
												<h3>
													用户授权：
												</h3>
											</div>
											<div id="orgrole_function_role_authorize_tab">
												<div class="filetree role_authorizeTree" id="role_permissionTreeDiv2">
													
												</div>
													<input type="hidden" id="role_rela_per_perId" />
													<input type="hidden" id="role_rela_per_roleId"/>
													
											</div>
		</div>
</div>
<%-- iosm系统结束--%>
<%--网优系统开始--%>
<div id="systemService_tab_2" style="display:none;">
      		<div class="role_userGroups">
		<h3>角色：</h3>
		<ul id="orgrole_role_ul" class="role_userGroups_ul">
		<table  id="table3">
				                        <s:iterator value="#request.Roles3" id="role">              
		                 <s:if test="FLAG==1"><tr><td> <input type="checkbox" class="roleid" onclick="checkedRole(this,<s:property value="ROLE_TYPE_ID"/>,<s:property value="ROLE_ID"/>,'<s:property value="CODE"/>','<s:property value="NAME"/>')"  name="roleids" checked="checked" value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:if>
		                         <s:else><tr><td> <input type="checkbox" class="roleid"  onclick="checkedRole(this,<s:property value="ROLE_TYPE_ID"/>,<s:property value="ROLE_ID"/>,'<s:property value="CODE"/>','<s:property value="NAME"/>')" name="roleids"  value="<s:property value="ROLE_ID"/>"><s:property value="NAME"/>（<s:property value="CODE"/>）</input></td></tr></s:else>
		                 </s:iterator>
		</table>  
		                 
		 </ul>
		
		</div>
		<div class="role_authorize" >
											<div id="role_org_tab" class="role_authorize_tab tab_menu">
												<h3>
													用户授权：
												</h3>
											</div>
											<div id="orgrole_function_role_authorize_tab">
												<div class="filetree role_authorizeTree" id="role_permissionTreeDiv3">
													
												</div>
													<input type="hidden" id="role_rela_per_perId" />
													<input type="hidden" id="role_rela_per_roleId"/>
													
											</div>
		</div>

</div>
<%-- 网优系统结束--%>
</div>
<div class="UserManage_standard_bottom"
												id="orgrole_function_oper_div">											
<input type="button" class="input_button"
														value="保 存" id="orgrole_function_oper_save_btn" onclick="saveUserRelaPermission()"/>
</div>
	</div>							


</div>

</body>
</html>