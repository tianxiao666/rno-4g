<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>填写车载终端信息</title>

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
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js""></script>
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
	})
		$(document).ready(function(){
			formcheck({
					"form" : $("#form1") , 
					"subButton" : $("#subBtn") , 
					"isAjax" : true , 
					"showLoading" : true , 
					"ajaxSuccess" : function( data ) {
						windowClose(data);
					}
			});
			providerOrgTree();
			
			//打开选择区域
			$("#choose_button").click(function(){
				$("#chooseOrgDiv").slideToggle("fast");
			});
			
			//选择图片
			$("#terminalFile").change(function() {
				$("#terminal_info_left_img").attr( {
					"src" : "images/big_loading.gif"
				});
				$("#terminal_img_form").ajaxSubmit(function(data) {
					$("#terminal_info_left_img").attr( {"src" : data});
					$("#terminalPic").val(data);
				});
			});
		})
	
		function windowClose( data ) {
			alert("添加成功");
			var op = window.opener.document;
			$(op).find("#simpleQueryButton").click();
			window.close();
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
						createOrgTreeOpenFirstNode(data,"chooseOrgDiv","terminalmanage_org_div","a","orgTreeClick");
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
<style type="text/css">
.content_left{height:190px; width:170px; text-align:center; overflow:hidden; position:relative;}
.content_left_title{position:absolute; bottom:0px; left:0px; width:100%; background:#eee; border-top:1px solid #ccc; line-height:28px; cursor:pointer;}
</style>
</head>

<body>

<div class="p_container"  style="height:330px;"> 
    <%--主体开始--%>
    <div class="pi_right">
        <div class="pi_menu_title clearfix">
            <p class="fl">填写车载终端信息</p>
        </div>
        
        <%-- content开始 --%>
        <div class="pi_right_content">
        		<form id="terminal_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post">
	                <div class="content_m">
	                    <h4 class="content_m_tit">
	                        <span class="doc_tit">车载终端信息</span>
	                    </h4>
	                </div>
	                <div class="content_left" style="margin-right:10px; ">
						<div editDiv="true" id="aa" style="height:185px; width:170px; text-align:center;" >
							<div style="width:170px; height:165px;">
								<img class="content_left_img" id="terminal_info_left_img" src=""
									width="170" height="165" editImg="true" />
							</div>
							<input type="button" value="选择图片" />
							<input type="file" id="terminalFile" style="z-index:999;position:relative;left:-75px; top:-24px; filter:alpha(opacity=0);opacity:0;" name="file" />
						</div>	
					</div>
				</form>
				<form id="form1" action="cardispatchManage!saveTerminal.action" method="post" enctype="multipart/form-data" >
					<input type="hidden" name="terminal#terminalPic" id="terminalPic" />
	                <div class="content_right">
	                    <table class="pi_table">
	                        <tr>
	                            <td class="menuTd">终端类型：</td>
	                            <td>
	                            	<select id="mobileType" name="terminal#mobileType" style="width:120px;" >
	                            		<option value="gt02">gt02</option>
	                            		<option value="gt03">gt03</option>
	                            		<option value="gt06">gt06</option>
	                            	</select>
	                            	<input type="hidden"  checknull="设备类型不能为空" checkTarget="#mobileType" />
	                            </td>
	                            <td class="menuTd">终端号：</td>
	                            <td>
	                                <input type="text" checknumber="终端号格式不规范" class="input_text" id="clientimei"  name="terminal#clientimei" checkajax="{url:'cardispatchManage!checkTerminalImeiIsExists.action',msg:'设备号已经存在了!'}" checknull="设备号不能为空"  />
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">终端电话(SIM卡号)：</td>
	                            <td>
	                                <input type="text" checkphone="sim卡格式不规范" class="input_text" id="telphoneNo" name="terminal#telphoneNo"  checknull="设备电话不能为空"   />
	                            </td>
	                            <td class="menuTd">名称：</td>
	                            <td>
	                                <input type="text" class="input_text"  name="terminal#terminalName"/>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">开通时间：</td>
	                            <td>
	                                <input type="text" readonly="readonly" class="input_text" name="terminal#launchedTime" id="launchedTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'expiredTime\')||\'2020-10-01\'}'})" />
		                    		
	                            </td>
	                            <td class="menuTd">到期时间：</td>
	                            <td>
	                                <input type="text" readonly="readonly" class="input_text"  name="terminal#expiredTime" id="expiredTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'launchedTime\')||\'2020-10-01\'}'})"/>
		                    		
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">所属区域：</td>
	                            <td colspan="3">
	                                <input type="hidden" name="terminal#terminalBizId" id="bizunitText"/>
				                	<input type="text"  class="input_text" readonly="readonly" id="bizunitNameText"/>&nbsp;
				                	<input type="button" id="choose_button" class="input_button" value="选择区域"  checknull="所属区域不能为空" checktarget="#bizunitText"  />
				                    <div id="chooseOrgDiv" style="width:180px; min-height:200px; z-index:999; position:absolute; border:1px solid #ccc; background:#fff;margin-top:-2px; display:none;">
				                        <%-- 放置组织架构树 --%>
				                    </div>
	                            </td>
	                        </tr>
	                    </table>
	                </div>
	            
	            <div class="pi_right_content_bottom_tc">
	                <input type="button" id="subBtn" class="input_submit" value="确定" />&nbsp;
		            <input type="button" class="input_button" onclick="window.close();" value="取消" />
	            </div>
            </form>
        </div>
    	<%-- content结束 --%> 
    </div>
	<%--主体结束--%> 
</div>
    
</div>
</body>
</html>

