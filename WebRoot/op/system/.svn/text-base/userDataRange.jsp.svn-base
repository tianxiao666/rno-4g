<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title><s:if test="showType==\"view\"">用户</s:if><s:else>设置</s:else>数据范围</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
		<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
		<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
		<link rel="stylesheet" type="text/css" href="../project/css/projectInfoManage.css"/>
		<link rel="stylesheet" href="css/jquery.treetable.css" />
		<style>
			.orgUserManage_standard_budget_title {
		    background-color: #F0F8FF;
		    border: 1px solid #DDDDDD;
		    height: 30px;
		    line-height: 30px;
		    margin-top: 5px;
		    text-align: center;
		}
		
		
		.orgUserManage_main {
		    background: none repeat scroll 0 0 #C3D5ED;
		    margin: 5px;
		    min-width: 500px;
		}
		
		
		.orgUserManage_content {
		    background: none repeat scroll 0 0 #FFFFFF;
		    border: 1px solid #99BCE8;
		    min-height: 700px;
		    overflow: hidden;
		}
		
		.orgUserManage_top {
		    background: url("../../images/white-top-bottom.gif") repeat-x scroll 0 -1px rgba(0, 0, 0, 0);
		    border-bottom: 1px solid #99BBE8;
		    color: #15428B;
		    font-weight: bold;
		    line-height: 26px;
		    padding: 0 5px;
		}
		
		.orgUserManage_title {
		    padding: 0 10px;
		}
		
		#resultListTable td{font-size:12px}
		</style>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../js/tab.js"></script>
		<script type="text/javascript">
			//checkbox 点击 只对PM
			function checkSelect(me){
				var value = $(me).val();
				var trId  = $("tr[id='"+value+"']").attr("data-tt-id");
				var parentTrId = $("tr[id='"+value+"']").attr("data-tt-parent-id");
				var ischecked = false;
				if($(me).attr("checked")){
					ischecked = true;
				}
				checkBoxSelectByTrId(trId,ischecked);//根据当前trId 对其下子级进行checkbox选择与否操作
				checkBoxSelectByParentTrId(parentTrId,ischecked);//根据当前parentTrId 对其上级进行checkbox选择与否操作
				
			}
			
			
			/**
			**根据当前trId 对其下子级进行checkbox选择与否操作
			** ischecked 是否选中 只对PM
			**/
			function checkBoxSelectByTrId(trId,ischecked){
				$("tr[data-tt-parent-id='"+trId+"']").each(function(){
					if(ischecked){
						$(this).find("input[type='checkbox']").attr("checked","checked");
					}else{
						$(this).find("input[type='checkbox']").removeAttr("checked");
					}
					var curId = $(this).attr("data-tt-id");
					var checkFlag = ischecked;
					checkBoxSelectByTrId(curId,checkFlag);
				})
				
			}
			/***
			** 根据当前parentTrId 对其上级进行checkbox选择与否操作 只对PM
			***
			**/
			function checkBoxSelectByParentTrId(parentTrId,ischecked){
				$("tr[data-tt-id='"+parentTrId+"']").each(function(){
					if(ischecked){
						$(this).find("input[type='checkbox']").attr("checked","checked");
					}else{
						var hasChildCheck = false;
						if(parentTrId==0){//怡创公司checkbox
							$("tr[data-tt-parent-id='"+parentTrId+"']").each(function(){
								if($(this).find("input[type='checkbox']").attr("checked")){
									hasChildCheck = true;
								}
							})
							if(!hasChildCheck){
								$(this).find("input[type='checkbox']").removeAttr("checked");
							}
						}
					}
					var curId = $(this).attr("data-tt-parent-id");
					var checkFlag = ischecked;
					checkBoxSelectByParentTrId(curId,checkFlag);
				})
			}
			/**
			**保存PM设置
			**/
			function saveUserDataRange(){
				var permissionIds = "";
				var orgUserId = $("#orgUserID").val();
				if($("input[name='dataPermissionIds']:checked").size()>0){
					$("input[name='dataPermissionIds']:checked").each(function(){
						permissionIds+=","+$(this).val();
					})
					permissionIds = permissionIds.substring(1);
					if(!confirm("确认保存所选中的数据范围?")){
						return false;
					}
				}else{
					if(!confirm("未选择任何数据范围，确认保存?")){
						return false;
					}
				}
				$.ajax({
		            url:"saveUserDataRangeAction",
		            async:false,
		            type:"POST",
		            dataType:"json",
		            data:{"dataPermissionIds":permissionIds,orgUserId:orgUserId},
		            success : function(result) {
						if(result=="success"){
							alert("设置数据范围成功!");
							location.href="viewUserDataRangeAction?orgUserId="+orgUserId+"&showType=view";
						}else{
							alert(result);
						}
		            }
		        });
			}
			/**
			*修改用户数据范围 链接
			*/
			function forwardUpdateUserDataRange(){
				var orgUserId = $("#orgUserID").val();
				location.href="viewUserDataRangeAction?orgUserId="+orgUserId;
			}
			
		</script>
		
	</head>

	<body>
		<%--主体开始--%>
		<div class="orgUserManage_main">
			<div class="orgUserManage_content">
				<div class="orgUserManage_top">
					<s:if test="showType==\"view\"">用户</s:if><s:else>设置</s:else>数据范围
				</div>
				<div class="orgUserManage_title" style="padding:0px">
			           	<div class="orgUserManage_standard_budget_title">
			           		<h4><s:if test="showType==\"view\"">用 户 </s:if><s:else>设 置 </s:else> 数 据 范 围</h4>                
			       		 </div>
			    </div>
			    <div style="padding:12px">
			    	<span style="font-weight:bold;font-size:16px">账号:</span><span>${pageMap.userMap.ACCOUNT}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    	<span style="font-weight:bold;font-size:16px">姓名：</span><span>${pageMap.userMap.NAME}</span>
			    </div>
				<%--主工作区开始--%>
				 <div id="projectService_tab" class="projectService_tab tab_menu" style="padding-top:10px; padding-left:10px">
                            <ul>
                            	 <s:if test="pageMap.systemList != null && pageMap.systemList.size() > 0">
		  							<s:iterator id="system" value="pageMap.systemList" status="st">
		  								<s:if test="#st.index==0">
		  									<li class="selected" style="width:150px">${system.NAME}</li>
		  								</s:if>
		  								<s:else>
		  									<li style="width:150px">${system.NAME}</li>
		  								</s:else>
		  						 	</s:iterator>
		  						 </s:if>
                            </ul>
                            
                            <div style="text-align:right;margin-right:10px">
                            <s:if test="showType==\"view\"">
                                <input type="button" class="input_button" value="修改设置" onclick="forwardUpdateUserDataRange();"/>
                            </s:if>
                            <s:else>
                            	<span style="color:red">未选择任何数据范围，保存将删除用户旧关联数据范围</span>
                            	<input type="button" class="input_button" value="保存设置" onclick="saveUserDataRange();"/>
                            </s:else>
                            
                            </div>
                 </div>                    
               	<div class="projectService_content" style="padding-top:10px;">
               			<input type="hidden" value="${orgUserId}" id="orgUserID" />
               			<s:if test="pageMap.permissionMap != null && pageMap.permissionMap.size() > 0">
  							<s:iterator id="map" value="pageMap.permissionMap" status="st">
  								<s:if test="#st.index==0">
  									<div id="projectService_tab_${st.index}"  > 
  								</s:if>
  								<s:else>
  									<div id="projectService_tab_${st.index}" style="display:none"> 
  								</s:else>
		                           <%-- 分隔--%>
		                           <div style="padding-top:0px; width: inherit;overflow: auto;height:800px;border:1px solid #99BBE8" > 
		                           <s:if test="value!=null && value.size()>0 ">
		                           		<table  id="resultListTable">  
			                           		<s:if test="key==\"PM\"">
			                           			<tr  data-tt-id="0" id="0" >
								                  <td><input type="checkbox" class="checkSelect" value="0" onclick="checkSelect(this)" <s:if test="showType==\"view\"">disabled="disabled"</s:if> /><span>${pageMap.topOrgMap.NAME}</span></td>
								                </tr>
			                           		</s:if>
			                           		<s:iterator id="permission" value="value" status="st">
					                           	<tr data-tt-id="${permission.PERMISSION_ID}" data-tt-parent-id="${permission.PARENT_ID}" id="${permission.PERMISSION_ID}">
										           		<td>
										            		<s:if test="#permission.RELA_PERMISSION_ID>0">
										            			<input type="checkbox" class="checkSelect" onclick="checkSelect(this)" value="${permission.PERMISSION_ID}" name="dataPermissionIds" checked="checked" <s:if test="showType==\"view\"">disabled="disabled"</s:if> />
										            		</s:if> 
										            		<s:else>
										            			<input type="checkbox" class="checkSelect" onclick="checkSelect(this)" value="${permission.PERMISSION_ID}" name="dataPermissionIds" <s:if test="showType==\"view\"">disabled="disabled"</s:if> />
										            		</s:else>
										            		<span>${permission.NAME}</span>
										           		</td>
								           		</tr>
							           		</s:iterator>
							           	</table>
		                           </s:if>
		                           <s:else>
		                           		<div style="text-align:center">暂无数据范围</div>
		                           		
		                           </s:else>
		                           </div>                                              
		                       </div>
  						 	</s:iterator>
  						 </s:if>
			   </div>
			   <div style="text-align:center">
			   		<s:if test="showType!=\"view\"">
                        <span style="color:red">未选择任何数据范围，保存将删除用户旧关联数据范围</span>
						<input type="button"
							class="input_button"
							value="保存设置" onclick="saveUserDataRange();" />
                    </s:if>
				</div>
				<br>
		</div>
	</body>
	<script src="jslib/jquery-1.10.0.js"></script>
	<script src="jslib/jquery.treetable.js"></script>
	<script type="text/javascript">
			var jq = jQuery.noConflict();
			jq(function(){		
				//tab选项卡
				tab("projectService_tab","li","onclick");//项目服务范围类别切换
				jq(".projectService_content table").each(function(){
					jq(this).treetable({ expandable: true });
					jq(this).treetable('expandAll');
				})
				
				//是否选中根节点 怡创  只对PM
				var rootChecked = false;
				jq("tr[data-tt-parent-id='0']").each(function(){
					if(jq(this).find("input[type='checkbox']").attr("checked")){
						rootChecked = true;
					}
				})
				if(rootChecked){
					jq("tr[id='0'] input[type='checkbox']").attr("checked","checked")
				}
			})
			
		</script>
</html>
