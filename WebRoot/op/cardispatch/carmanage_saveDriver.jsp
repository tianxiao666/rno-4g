<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>添加司机</title>
		<link rel="stylesheet" href="css/base.css" type="text/css" />
		<link rel="stylesheet" href="css/public.css" type="text/css" />
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
		
		<%-- 基本js --%>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../js/public.js"></script>
		<script type="text/javascript"src="../../jslib/jquery/jquery.treeview.js"></script>
		<script type="text/javascript" src="../../jslib/common.js"></script>
		<script type="text/javascript" src="../jslib/generateTree.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
		
		<script type="text/javascript">
			$(function(){
				/*input样式*/
				$(":button,:submit").mousedown(function(){
					$(this).addClass("input_button_down");
				});
				$(":button,:submit").mouseup(function(){
					$(this).removeClass("input_button_down");
				});
				//打开选择区域
				$("#choose_button").click(function(){
					$("#chooseOrgDiv").slideToggle("fast");
				});
			})
			$(function(){
				var ajaxFormSub = "";
				providerOrgTree();
				
				//获取当前登录人企业后缀
				$.ajax({
					"url" : "cardispatchForeign_ajax!findLoginUserEnterprise.action" , 
					"type" : "post" , 
					"async" : false , 
					"success" : function ( result ) {
						var data = eval( "(" + result + ")" );
						$("#accountSuffix").text(data.enterpriseSuffix);
					}
				});
				
				//添加司机
				formcheck({
						"form" : $("#addDriverForm") , 
						"subButton" : $("#addDriverButton") , 
						"isAjax" : true , 
						"showLoading" : true , 
						"formSubmiting" : function () {
							var account = $("#ITAccountIdText").val();
							var accountSuffix = $("#accountSuffix").text();
							var accountId = "";
							if ( account != "" && accountSuffix != ""  ) {
								accountId = account + accountSuffix;
							} else {
								alert("填写的账号有误！");
								return false;
							}
							$("#accountId_hidden").val(accountId);
						} ,  
						"ajaxSuccess" : function( data ) {
							windowClose(data);
						}
				});		
				//选择图片
				$("#driverFile").change(function() {
					$("#driver_info_left_img").attr( {
						"src" : "images/big_loading.gif"
					});
					$("#driver_img_form").ajaxSubmit(function(data) {
						$("#driver_info_left_img").attr( {"src" : data});
						$("#driverPic").val(data);
					});
				});
			});
			
			function windowClose( data ) {
				alert("添加成功");
				var op = window.opener.document;
				$(op).find("#simpleQueryButton").click();
				window.close();
			}
			
			
			function checkDriverAccount (  ) {
				var ITAccountIdText = $("#ITAccountIdText").val();
				var accountSuffix = $("#accountSuffix").text();
				var flag = false;
				$.ajax({
					"url" : "cardispatchManage!checkAccountIdIsExists.action" , 
					"type" : "post" , 
					"data" : { "driver#accountId" : (ITAccountIdText+accountSuffix) } , 
					"async" : false , 
					"success" : function ( result ) {
						flag = !(result=="true");
					}
				});
				return flag;
			}
			
			
			
			/***************** 组织结构 *******************/
				
			//生成组织架构树
			function providerOrgTree(){
				var orgId = "16";
				
				$.ajax({
					"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
					"type" : "post" , 
					"async" : true , 
					"success" : function ( data ) {
						data = eval( "(" + data + ")" );
						orgId = data.orgId;
						if(orgId==null||orgId==""){
							orgId="16";
						}  
						var values = {"orgId":orgId}
						var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
						$.post(myUrl,values,function(data){
							createOrgTreeOpenFirstNode(data,"chooseOrgDiv","drivermanage_org_div","a","orgTreeClick");
						},"json");
					}
				});
				
			}
			function a(){}
			 
			//显示组织信息
			function orgTreeClick(dataStr,tableId){
				var data = eval( "(" + dataStr + ")" ) ;
				var orgId = data.orgId;
				$("#bizunitNameText").val(data.name);
				$("#bizunitText").val(data.orgId);
				$("#chooseOrgDiv").slideUp("fast");
			} 
		</script>
	</head>
	<style type="text/css">
		.content_left {
			height: 190px;
			width: 170px;
			text-align: center;
			overflow: hidden;
			position: relative;
		}
		
		.content_left_title {
			position: absolute;
			bottom: 0px;
			left: 0px;
			width: 100%;
			background: #eee;
			border-top: 1px solid #ccc;
			line-height: 28px;
			cursor: pointer;
		}
</style>
	<body>
		<div class="p_container" style="height:330px;">
			<%--主体开始--%>
			<div class="pi_right">
				<div class="pi_menu_title clearfix">
					<p class="fl">
						填写司机信息
					</p>
				</div>
				<%-- content开始 --%>
				<div class="pi_right_content">
					<div class="tab_content">
						<form id="driver_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post">
							<div class="content_m">
								<h4 class="content_m_tit">
									<span class="doc_tit">司机信息</span>
									<%-- 按钮 --%>
									</span>
								</h4>
							</div>
							<div class="content_left" style="margin-right:10px; ">
								<div editDiv="true" id="aa" style="height:185px; width:170px; text-align:center;" >
									<div style="width:170px; height:165px;">
										<img class="content_left_img" id="driver_info_left_img" src=""
											width="170" height="165" editImg="true" />
									</div>
									<input type="button" value="选择图片" />
									<input type="file" id="driverFile" style="z-index:999;position:relative;left:-75px; top:-24px; filter:alpha(opacity=0);opacity:0;" name="file" />
								</div>	
							</div>
						</form>
						<form action="cardispatchManage!saveDriver.action" enctype="multipart/form-data" method="post" id="addDriverForm">
							<input type="hidden" name="driver#driverPic" id="driverPic" />
							<div class="content_right">
								<table class="pi_table">
									<tr>
										<td class="menuTd">
											司机姓名：
										</td>
										<td>
											<input type="text" checkopera="存在特殊字符" checkchinese="只能输入中文"  
												checknull="司机姓名不能为空" class="input_text" name="driver#driverName"/>
										</td>
										<td class="menuTd">
											IT账号：
										</td>
										<td>
											<input type="text" class="input_text" id="ITAccountIdText" checknull="&nbsp;&nbsp;&emsp;&emsp;&emsp; IT账号不能为空" checkajax="{'method':'checkDriverAccount',msg:'&nbsp;&nbsp;&emsp;&emsp;&emsp; IT账号已经存在了'}" checkregex="{'reg' : '/^[A-z]+[.][A-z]+\\d*$/' , 'msg' : '&nbsp;&nbsp; IT账号命名不规范'}"/>
											<input type="hidden"  name="driver#accountId" id="accountId_hidden" />
											<span id="accountSuffix"></span>
											<input type="hidden" checknull="&nbsp;&nbsp;&emsp;&emsp;&emsp; IT账号不能为空" checkajax="{'method':'checkDriverAccount',msg:'&nbsp;&nbsp;&emsp;&emsp;&emsp; IT账号已经存在了'}" checkregex="{'reg' : '/^[A-z]+[.][A-z]+\\d*$/' , 'msg' : '&nbsp;&nbsp; IT账号命名不规范'}" checkTarget="#ITAccountIdText" "/>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											电话号码：
										</td>
										<td>
											<input type="text" checkphone="电话格式不规范" checknull="电话号码不能为空" class="input_text" name="driver#driverPhone" />
										</td>
										<td class="menuTd">
											年龄：
										</td>
										<td>
											<input type="text" number="keypress" class="input_text" name="driver#driverAge" checknumber="给个数吧!" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											身份证号：
										</td>
										<td>
											<input type="text" checkidcard="身份证格式不规范" class="input_text" name="driver#identificationId" />
										</td>
										<td class="menuTd">
											工资：
										</td>
										<td>
											<input type="text" dnumber="keypress" class="input_text" name="driver#wage" checknumber="给个数吧!" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											所属区域：
										</td>
										<td>
											<div class="with_div" style="position:relative; z-index:1;">
												<input type="hidden" name="driver#driverBizId" id="bizunitText"/>
							                	<input type="text"  class="input_text" readonly="readonly" id="bizunitNameText"/>&nbsp;
							                	<input type="button" id="choose_button" class="input_button" value="选择区域" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;区域不能为空" checktarget="#bizunitNameText" />
												<div id="chooseOrgDiv" style=" width:200px; height:200px; overflow:auto; position:absolute;border:1px solid #ccc; background:#fff;*+left:0px;*+top:30px; display:none;">
							                        <%-- 放置组织架构树 --%>
							                    </div>
											</div>
										</td>
										<td class="menuTd">
											驾照类型：
										</td>
										<td>
											<input type="text" class="input_text" name="driver#driverLicenseType" checkOpera="亲,不要输入特殊字符喔！"/>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											住址：
										</td>
										<td colspan="3">
											<input type="text" class="input_text"  style="width:480px;" name="driver#driverAddress" checkOpera="亲,不要输入特殊字符喔！"/>
										</td>
									</tr>
								</table>
							</div>
						</div>
					
						<div class="pi_right_content_bottom_tc">
							<input type="button" class="input_submit" value="确定" id="addDriverButton" />&nbsp;
	                    <input type="button" class="input_button" value="取消" onclick="window.close()"/>
						</div>
					</form>
				</div>
			</div>
			<%-- content结束 --%>
		</div>
		<%--主体结束--%>
	</body>
</html>
