<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<s:if test="distinction == 'Reading'">
	    		<title>车辆仪表读数查看</title>
			</s:if>
			<s:else>
				<title>车辆油费查看</title>
			</s:else>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
<style type="text/css">
.select_button {
    background: url("images/select.png") no-repeat scroll 0 0 transparent;
    display: inline-block;
    height: 24px;
    position: absolute;
    width: 24px;
}

#treeDiv {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background: none repeat scroll 0 0 #F9F9F9;
    border-color: -moz-use-text-color #CCCCCC #CCCCCC;
    border-image: none;
    border-right: 1px solid #CCCCCC;
    border-style: none solid solid;
    border-width: medium 1px 1px;
    display: none;
    height: 200px;
    left: 99px;
    line-height: 18px;
    overflow: auto;
    position: absolute;
    text-align: left;
    top: 64px;
    width: 200px;
    z-index: 199;
}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="jslib/public.js "></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript">

//逻辑删除
function deleteReadingAndBills(){
	var id = $("#id").val();
	var distinction = $("#distinction").val();
	var param={
		id:id,
		distinction:distinction
	};
	$.ajax({
		"url" : "deleteReadingOrBillsAction" , 
		"type" : "post" , 
		"data":param,
		"dataType":'json',
		"async" : false , 
		"success" : function(result){
			if(result.flag == true){
				alert("删除成功");
				//刷新页面
				window.location.reload(); 
			}else{
				alert("删除失败");
			}
		}
	});
}                      
</script>
</head>

<body>
	<div id="header960">
    	<div class="header-top960">
	    	<s:if test="distinction == 'Reading'">
	    		<h2>车辆仪表读数查看</h2>
			</s:if>
			<s:else>
				<h2>车辆油费查看</h2>
			</s:else>
    	</div>

    </div>
    <%--头部结束--%>			
    <div id="container960">
    		<%--tab1开始--%>
    	<input type="hidden" id="id"
			value="${ dataMap.instrument_reading_id}${ dataMap.fuel_bills_id}" />	
    	<input type="hidden" id="distinction" value="<s:property value="distinction"/>"/>
    	<div class="container-tab1">
    		<fieldset id="fieldset-1">
            	<legend>
                	<span>组织车辆信息</span>
                    	
                </legend>
                
                <div class="container-main">

                    
                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />组织车辆信息</td></tr>
                            
                            <tr>
                            	<td class="menuTd" style="width: 100px;">所属组织：</td>
                                <td  style="width: 360px;">
                                	<s:property value="dataMap.orgName"/>
                                </td>
                                <td class="menuTd" style="width: 100px;">车牌号码：</td>
                                <td>
									<s:property value="dataMap.car_car"/>
                                </td>
                            </tr>
                        </table>
                        
                       
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
        	<fieldset id="fieldset-1">
            	<legend>
                	<span>录入信息</span>
                    	
                </legend>
                
                <div class="container-main">

                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />录入信息</td></tr>
                            
                            <tr>
                            	<s:if test="distinction == 'Reading'">
	                            	<td class="menuTd" style="width: 100px;">仪表读数(公里)：</td>
                            	</s:if>
                            	<s:else>
                            		<td class="menuTd" style="width: 100px;">油费(元)：</td>
                            	</s:else>
                                <td style="width: 360px;">
									${dataMap.instrument_reading }
									${dataMap.fuel_bills }
                                </td>
                                <td class="menuTd" style="width: 100px;">记录时间：</td>
                                <td>
									<s:date name="dataMap.recording_time" format="yyyy-MM" /></td>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd" style="width: 100px;">录入人：</td>
                                <td style="width: 360px;">
									${dataMap.create_user}
                                </td>
                                <td class="menuTd" style="width: 100px;">录入时间：</td>
                                <td>
									<s:date name="dataMap.create_time" format="yyyy-MM-dd HH:mm:ss" /></td>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd" style="width: 100px;">删除人：</td>
                                <td style="width: 360px;">
									${dataMap.delete_user}
                                </td>
                                <td class="menuTd" style="width: 100px;">删除时间：</td>
                                <td>
									<s:date name="dataMap.delete_time" format="yyyy-MM-dd HH:mm:ss" /></td>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd" style="width: 100px;">状态：</td>
                                <td colspan="3">
		                            <s:if test="dataMap.status == 1">
										正常
									</s:if>
									<s:else>
										无效
									</s:else>
								</td>
							</tr>
                            <tr>
                            	<td class="menuTd" style="width: 100px;">读数备注：</td>
                                <td colspan="3">
									${dataMap.create_remarks}
                                </td>
                            </tr>
                        </table>
                        
                       
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
           
            <div class="container-bottom">
            	<s:if test="dataMap.status == 1">
	            	<input type="button" value="删  除" style="width:60px;" onclick="deleteReadingAndBills();" name="save" />
				</s:if>
            </div>
        </div>
        	<%--tab1结束，可重写--%>
    </div>
   
</body>
</html>

