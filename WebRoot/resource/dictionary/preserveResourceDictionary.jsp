<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源模型字典维护</title>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<link rel="stylesheet" href="css/public.css" type="text/css" />
<link rel="stylesheet" href="css/tab.css" type="text/css" />
<link rel="stylesheet" href="css/dialog.css" type="text/css" />
<style type="text/css">
.result_info{border:1px solid #ccc; height:200px; overflow-y:auto;}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery.chromatable.js"></script>
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/importNetworkResource.js"></script>
<script type="text/javascript">
	$(function(){
		$(".tab_menu ul li").each(function(index){//切换卡切换
			$(this).click(function(){
				$("#loading_div").show();
				$("#black").show();
				$(".tab_menu ul li").removeClass("ontab");
				$(this).addClass("ontab");
				var url="";
				//alert(index);
				if(index==0){
					url="getResourceEntityDictionaryAction";
				}else if(index==1){
					url="getResourceAttributesDictionaryAction";
				}else if(index==2){
					url="getResourceEntityDropdownDictionaryAction";
				}else{
					url="getResourceNormalDropdownDictionaryAction";
				}
				$.ajax({
						url:url,
						data:'',
						async:true,
						type:'post',
						success:function(data){
							$(".tab_content").html(data);
							$("#loading_div").hide();
							$("#black").hide();
						}
				})	
			})
		})
		/*input样式*/
		$(":button,:submit").mousedown(function(){
			$(this).addClass("input_button_down");
		});
		$(":button,:submit").mouseup(function(){
			$(this).removeClass("input_button_down");
		});
		$.post('getResourceEntityDictionaryAction','',function(data){//加载entity中英文字典
			$(".tab_content").html(data);
		});
		
		$(document).keydown(function(event){ 
			//alert(event.keyCode);
			if(event.keyCode==27){
				$("tr[id='resAdd']").hide();
				$("tr input[type='text']").hide();
				$("input[id='save']").hide();
				$("input[id='cancel']").hide();
				$("input[class='display-button']").show();
				$("tr em[id='hideem']").show();
			}
		}); 
		
	})
	
	
	function showimport(){
		var de = $("#tab_ul .ontab").text();
		$("#entityTable tr").hide();
		if(de == '资源类型定义'){
			$("#entitynameDiv").show();
		}
		if(de == '资源属性定义'){
			$("#attributeDiv").show();
		}
		if(de == '特定资源及属性选择项定义'){
			$("#enetityDiv").show();
		}
		if(de == '通用属性选择项定义'){
			$("#tongyongDiv").show();
		}
		
		$("#copy_dialog").show();
	}
	
	function hideimpoert(){
		$("#copy_dialog").hide();
		$("#loading").hide();
		$("#tishi").hide();
	}
	
</script>

</head>

<body>
    <div class="tab1">
        <div class="tab_menu">
            <ul id="tab_ul">
                <li class="ontab first_tab">资源类型定义</li>
                <li>资源属性定义</li>
                <li>特定资源及属性选择项定义</li>
                <li>通用属性选择项定义</li>
            </ul>
        </div>
        
        <div class="tab_container">
            <div class="tab_content">
            	
            </div>
        </div>
    </div>
    <%-- 遮盖层 --%>
	<div id="black" style="z-index:5;width:100%;position:absolute; top:0; left:0; height:100%; background:#FFF; display:none; z-index:2;filter:alpha(opacity=42); opacity:0.6;"></div>

	<%-- 数据加载提示层 --%>
  <%--<div id="loading_div" style="z-index:6;display:none; position:absolute; width:450px; height:200px; left:50%;top:200px;margin-left:-225px; border-radius:3px;">
    	<div style="height:100px;padding-top:20px; width:300px; margin:auto;text-align:center;">
        	<img src="images/ajax-loaderCccc.gif" />
        </div>
    	<div style="padding-top:5px;width:300px; margin:auto;text-align:center;">
        	数据加载中，请稍侯....
        </div>
    </div>--%>
    <div class='insp_black' id="loading_div" style="display:none">
       	<img src="image/loading_img.gif"><br>数据处理中，请稍侯...
   	</div>
    
		
		<%-- 复制其他模板巡检内容弹出框 --%>
	    <div id="copy_dialog" style="display:none;">
	        <div class="black"></div>
		        <div class="dialog" style="width:600px; left:30%; top:100px; position: absolute;">
		        	<div class="dialog_header">
	                <div class="dialog_title">字典导入</div>
	                <div class="dialog_tool">
	                   <div class="dialog_tool_close copy_dialog_close" onclick="hideimpoert();"></div>
	                </div>
	            </div>
	            <div class="dialog_content" style="padding:4px;">
	            	<div class="content_main">
	            		<table class="main-table1" id="entityTable">
							
							<%--  <tr>
								<th class="tl" colspan ="2">导入资源信息</th>
							</tr>--%>
							<tr id="entitynameDiv" style="display: none;">
								<td style="background:#E8EDFF;text-align:right;width:20%">
									资源类型定义
								</td>
								<td>
									<input type="file" id="file1" name="file1" size='50' />
									<input type="button" id="importOne" class="bt11" value="导入" />
								</td>				
							</tr>
							
							<tr id="attributeDiv" style="display: none;">
								<td style="background:#E8EDFF;text-align:right;width:20%">
									资源属性定义
								</td>
								<td>
									<input type="file" id="file2" name="file2"  size='50'/>
									<input type="button" id="importSec" class="bt11" value="导入" />
								</td>				
							</tr>
							<tr id="enetityDiv" style="display: none;">
								<td style="background:#E8EDFF;text-align:right;width:20%">
									特定资源及属性选择项定义
								</td>
								<td>
									<input type="file" id="file3" name="file3"  size='50' />
									<input type="button" id="importThr" class="bt11" value="导入" />
								</td>				
							</tr>
							<tr id="tongyongDiv" style="display: none;">
								<td style="background:#E8EDFF;text-align:right;width:20%">
									通用属性选择项定义
								</td>
								<td>
									<input type="file" id="file4" name="file4"  size='50'/>
									<input type="button" id="importFou" class="bt11" value="导入" />
								</td>				
							</tr>
						</table>
				
						<div style="line-height:30px; font-weight:bold; padding-left:5px;">导入结果</div>
						<table class="main-table1">
							<tr>
								<td class="tdb1 tdr" style="padding: 5px 0">
									<div id="loading" style="display:none">
									<div
										style="width: 100%; text-align: center;">
										<img src="image/ajax-loaderCccc.gif" />
									</div>
									<div style="padding-top:5px;width:300px; margin:auto;text-align:center;">
				        				导入中，请稍侯....
				       				 </div>
				       				 </div>
									<textarea name="textarea" id="tishi" cols="45" rows="5"
										style="width: 99%; height: 200px; display: none;"
										disabled="disabled">
										<s:property value="message" />
									</textarea>
								</td>
							</tr>
						</table>	
	            	</div>
	            </div>
	            
	        </div>
	    </div>
</body>
</html>
