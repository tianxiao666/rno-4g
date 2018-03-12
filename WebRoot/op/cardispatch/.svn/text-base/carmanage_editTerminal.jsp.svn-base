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
<script type="text/javascript" src="js/class/terminal.js"></script>
<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
<script type="text/javascript" src="js/tool/showedit.js"></script>
<script type="text/javascript" src="js/util/dateutil.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js""></script>


"

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
		
		var terminalId = null;
		var mobileInfo = "mobileInfo";
		$(document).ready(function(){
			terminalId = "${param.terminalId}";
			if ( terminalId == "" ) {
				return;
			}
			$.ajax({
				"url" : "cardispatchManage!findMobileInfoList.action" , 
				"type" : "post" , 
				"data" : { "terminal#terminalId" : terminalId } , 
				"async" : true , 
				"success" : function( data ){
					data = eval("(" + data + ")" );
					if ( data && data.length > 0 ) {
						var terminal = new Terminal(data[0]);
						terminal.putInfo("body");
					}
				}
			});
			formcheck({
						"form" : $("#form1") , 
						"subButton" : $("#saveBtn") , 
						"isAjax" : true , 
						"showLoading" : true , 
						"ajaxSuccess" : function( data ) {
							var src = $("#terminal_info_left_img").attr("src");
							$("#terminal_img").attr({"src":  src});
							alert("操作成功");
							var op = window.opener.document;
							$(op).find("#simpleQueryButton").click();
						}
			
			});	
			$("#form1 .formcheckspan").attr("editButton","true");
			showedit("#form1");
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
				
				$("#form1").attr({"action":"cardispatchCommon_ajax!getFileURL.action"});
				$("#form1").ajaxSubmit(function(data) {
					$("#form1").attr({"action":"cardispatchManage!updateTerminalById"});
					$("#saveBtn").removeAttr("disabled");
					$("#returnBtn").removeAttr("disabled");
					$("#terminal_info_left_img").attr( {"src" : data});
					$("#terminalPic").val(data);
				});
			});
		})
	
		function windowClose( data ) {
			alert("添加成功");
			var op = window.opener.document;
			$(op).find("#firstPage").click();
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
          	<form id="form1" action="cardispatchManage!updateTerminalById" method="post" enctype="multipart/form-data">
          		<input type="hidden" value="${param.terminalId}" name="terminal#id"/>
	            <div class="tab_content">
	                <div class="content_m">
	                    <h4 class="content_m_tit">
	                        <span class="doc_tit">车载终端信息</span>
	                        <%-- 按钮 --%>
							<span class="doc_btn">
								<input type="button" class="input_button" editBtn="true" id="editBtn" value="修改" />
			                    <input type="button" class="input_button" saveBtn="true" id="saveBtn" value="保存" />
			                    <input type="button" class="input_button" cancelBtn="true" removeMsgSpan="true" id="returnBtn" value="返回" />
							</span>
	                    </h4>
	                </div>
	                <div class="content_left" style="margin-right:10px; ">
	                	<img class="content_left_img"  id="terminal_img"  column="terminal#terminalPic"
									width="170" height="185" showSpan="true" showImg="true" />
						<div editDiv="true" id="aa" style="height:185px; width:170px; text-align:center;" >
							<div style="width:170px; height:165px;">
								<img class="content_left_img" id="terminal_info_left_img" src=""
									width="170" height="165" editImg="true" />
							</div>
							<input type="button" value="选择图片" />
							<input type="file" style="z-index:999;position:relative;left:-75px; top:-24px; filter:alpha(opacity=0);opacity:0;" id="terminalFile" name="file" />
							<input type="hidden" name="terminal#terminalPic" id="terminalPic" />
						</div>	
					</div>
	                <div class="content_right">
	                    <table class="pi_table">
	                        <tr>
	                            <td class="menuTd">终端类型：</td>
	                            <td>
	                            	<span column="terminal#mobileType" showSpan="true" ></span>
	                            	<select id="mobileType" editInput="true" name="terminal#mobileType" style="width:120px;" >
	                            		<option value="gt02">gt02</option>
	                            		<option value="gt03">gt03</option>
	                            		<option value="gt06">gt06</option>
	                            	</select>
	                            	<input type="hidden"  checknull="设备类型不能为空" checkTarget="#mobileType" />
	                            </td>
	                            <td class="menuTd">终端号：</td>
	                            <td>
	                                <span  class="mobileInfo" id="clientimei1" column="terminal#clientimei"></span>
		                    		<input  type="hidden" checknumber="终端号格式不规范" class="mobileInfoText" id="clientimei" column="terminal#clientimei" name="terminal#clientimei"  />
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">终端电话(SIM卡号)：</td>
	                            <td>
	                                <span showSpan="true" class="mobileInfo" id="telphoneNo1" column="terminal#telphoneNo"></span>
		                     		<input editInput="true" checkphone="SIM号格式不规范" type="text" class="mobileInfoText" id="telphone" name="terminal#telphoneNo" checknull="设备电话不能为空"  />
	                            </td>
	                            <td class="menuTd">名称：</td>
	                            <td>
	                            	<span showSpan="true" class="mobileInfo" id="telphoneNo1" column="terminal#terminalName"></span>
		                     		<input editInput="true" type="text" class="mobileInfoText" id="name" name="terminal#terminalName"  />
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">开通时间：</td>
	                            <td>
	                                <span showSpan="true" class="mobileInfo" column="terminal#launchedTime" >
				                    </span>
				                    <input editInput="true" readonly="readonly"  type="text" class="mobileInfoText" id="launchedTimetxt" name="terminal#launchedTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'expiredTime\')||\'2020-10-01\'}'})" />
				                    
	                            </td>
	                            <td class="menuTd">到期时间：</td>
	                            <td>
	                            	<span showSpan="true" class="mobileInfo" id="launchedTime" column="terminal#expiredTime" >
		                    		</span>
				                    <input editInput="true" readonly="readonly"  type="text" class="mobileInfoText" id="expiredTime" name="terminal#expiredTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'launchedTimetxt\')||\'2020-10-01\'}'})"/>
				                    
					                <input type="hidden" checkdatecompare="{'msg':'&emsp;&emsp;&emsp;&emsp;必须大于开通时间','compare':'moreThan','target':'#expiredTime','comparetarget':'#launchedTime'}" />
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="menuTd">所属区域：</td>
	                            <td colspan="2">
	                                <input type="hidden" id="bizunitText" name="terminal#terminalBizId" column="terminal#terminalBizId" />
	                            	<span id="clientimei1" column="terminal#terminalBizName" showSpan="true" ></span>
				                	<input type="text" editInput="true" class="input_text" readonly="readonly" id="bizunitNameText"/>&nbsp;
				                	<input type="button" editButton="true" id="choose_button" class="input_button" value="选择区域"  checknull="所属区域不能为空" checktarget="#bizunitText"  />
				                    <div id="chooseOrgDiv" style="width:180px; min-height:200px; z-index:999; position:absolute; border:1px solid #ccc; background:#fff;margin-top:-2px; display:none;">
				                        <%-- 放置组织架构树 --%>
				                    </div>
				                   
	                            </td>
	                            <td>
	                            	 <span id="checkDate"></span>
	                            </td>
	                        </tr>
	                    </table>
	                </div>
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

