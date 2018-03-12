<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>添加车辆仪表读数与油费</title>
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
    left: 118px;
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
//判断整型与浮点型
var r = /^((-{0,1}[0-9]+[\\.]?[0-9]+)|-{0,1}[0-9])$/;
$(function(){
	/*弹出组织架构树*/
		$("#chooseAreaButton").click(function(){
			$("#treeDiv").toggle("fast");
		});
		searchProviderOrgTree();
});


//生成服务商的组织架构树
function searchProviderOrgTree(){
	var orgId = $("#orgId").val();
		var values = {"orgId":orgId}
		var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
		$.post(myUrl,values,function(data){
			createOrgTreeOpenFirstNode(data,"treeDiv","carDutySearch_org_div","a","searchOrgTreeClick");
		},"json");
	selectCarByOrgId(orgId);
}

//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#org_Name").val(data.name);
	selectCarByOrgId(orgId);
	$("#treeDiv").slideUp("fast");
}

//获取组织ID获取车辆列表
function selectCarByOrgId(orgId){
	var values = {"orgId":orgId}
	var myUrl = "cardispatchManage!selectCarByOrgIdAction.action";
	$.post(myUrl,values,function(data){
			var context = "<option value=\"\">请选择</option>";
			if(data){
				$.each(data,function(k,v){
					context = context + "<option value=\""+v.carId+"\">"+v.carNumber+"</option>";
				});
			}
			$("#car_select").html(context);
		},"json");
}



//添加读数与油费
function addReadingAndBills(){
	$(".red").text("");
	$("#car_select").next().text("*");
	//车辆ID
	var carId = "";
	//仪表读数
	var instrumentReading = "";
	//读数记录时间
	var readingRecordingTime = "";
	//油费
	var fuelBills = "";
	//油费记录时间
	var billsRecordingTime = "";
	//读数备注
	var readingCreateRemarks = null;
	//油费备注
	var billsCreateRemarks = null;
	//判断是否选择车辆
	if($("#car_select").val() == ""){
		$("#car_select").next().text("*请选择车辆");
		return;
	}else{
		carId = $("#car_select").val();
	}
	//判断读数与油费是否都为空
	if($("#instrumentReading").val() == "" && $("#fuelBills").val() == ""){
		$("#instrumentReading").next().text("请填写仪表读数(公里)");
		$("#fuelBills").next().text("请填写油费(元)");
		return;
	}else{
		if($("#instrumentReading").val() != ""){
			//判断输入读数是否为数字
			if(r.test($("#instrumentReading").val()) && parseFloat($("#instrumentReading").val()) > 0){
				instrumentReading = parseFloat($("#instrumentReading").val());
				if(instrumentReading >  100000000){
					$("#instrumentReading").next().text("填写读数过大");
					return;
				}
			}else{
				$("#instrumentReading").next().text("请填写正数");
				return;
			}
			//判断数据记录时间是否为空
			if($("#readingRecordingTime").val() == "" && $("#fuelBills").val() == ""){
				$("#readingRecordingTime").next().text("请填写记录时间");
				return;
			}else{
				readingRecordingTime = $("#readingRecordingTime").val();
			}
			//判断备注长度
			if($("#readingCreateRemarks").val() != "" && $("#readingCreateRemarks").val().length > 80){
				$("#readingCreateRemarks").next().text("备注请小于80字");
				return;
			}else{
				readingCreateRemarks = $("#readingCreateRemarks").val();
			}
		}else if($("#fuelBills").val() == ""){
			$("#instrumentReading").next().text("请填写仪表读数(公里)");
			return;
		}
		if($("#fuelBills").val() != ""){
			//判断油费是否为数字
			if(r.test($("#fuelBills").val()) && parseFloat($("#fuelBills").val()) > 0){
				fuelBills = parseFloat($("#fuelBills").val());
				if(fuelBills >  100000000){
					$("#fuelBills").next().text("填写油费过大");
					return;
				}
			}else{
				$("#fuelBills").next().text("请填写正数");
				return;
			}
			//判断记录时间
			if($("#billsRecordingTime").val() == "" && $("#instrumentReading").val() == ""){
				$("#billsRecordingTime").next().text("请填写记录时间");
				return;
			}else{
				billsRecordingTime = $("#billsRecordingTime").val();
			}
			//判断油费备注长度
			if($("#billsCreateRemarks").val() != "" && $("#billsCreateRemarks").val().length > 80){
				$("#billsCreateRemarks").next().text("备注请小于80字");
				return;
			}else{
				billsCreateRemarks = $("#billsCreateRemarks").val();
			}
		}else if($("#instrumentReading").val() == ""){
			$("#fuelBills").next().text("请填写油费(元)");
			return;
		}
	}
	//提交后台添加
	var param = {"carId":carId,"instrumentReading":instrumentReading,"readingRecordingTime":readingRecordingTime,"readingCreateRemarks":readingCreateRemarks,"fuelBills":fuelBills,"billsCreateRemarks":billsCreateRemarks,"billsRecordingTime":billsRecordingTime};
	var myUrl = "cardispatchManage!addInstrumentReadingAndFuelBillsAction.action";
	$.ajax({
		"url" : myUrl , 
		"type" : "post" , 
		"data":param,
		"dataType":'json',
		"async" : false , 
		"success" : function(result){
			alert(result.message);
			if(result.message == "添加成功"){
				//跳转查询页面
				window.location.href = "carReadingAndBillsPageViewAction";
			}
		}
	});
}
                    
</script>
</head>

<body>
	<input type="hidden" id="orgId" value="${orgId }"/>
	
	<div id="header960">
    	<div class="header-top960"><h2>添加车辆仪表读数与油费</h2></div>

    </div>
    <%--头部结束--%>			
    <div id="container960">
    		<%--tab1开始--%>
    	<div class="container-tab1">
    		<fieldset id="fieldset-1">
            	<legend>
                	<span>选择车辆</span>
                    	
                </legend>
                
                <div class="container-main">

                    
                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />选择车辆</td></tr>
                            
                            <tr>
                            	<td class="menuTd" style="width: 100px;">所属组织：</td>
                                <td  style="width: 360px;">
									<input type="text" onfocus="$('#treeDiv').toggle('fast');" onblur="$('#treeDiv').toggle('fast');" readonly="readonly" id="org_Name" value="${orgName }"/>
									<a href="javascript:void(0);" style="margin-top:3px; margin-left:-24px; *+margin-top:2px;" class="select_button selectWorkPlaceButton" id="chooseAreaButton" title="选择组织"></a>
									<div id="treeDiv">
							        	<%-- 放置组织架构树 --%>
							        </div>	
                                </td>
                                <td class="menuTd" style="width: 100px;">车牌号码：</td>
                                <td>
									<select style="width: 120px;" id="car_select">
										
									</select>
									<span class="red">*</span>
                                </td>
                            </tr>
                        </table>
                        
                       
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
        	<fieldset id="fieldset-1">
            	<legend>
                	<span>仪表读数</span>
                    	
                </legend>
                
                <div class="container-main">

                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />仪表读数录入信息</td></tr>
                            
                            <tr>
                            	<td class="menuTd" style="width: 100px;">仪表读数(公里)：</td>
                                <td style="width: 360px;">
									<input type="text" id="instrumentReading"/>
									<span class="red"></span>	
                                </td>
                                <td class="menuTd" style="width: 100px;">记录时间：</td>
                                <td>
									<input type="text" id="readingRecordingTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/>
									<span class="red"></span>	
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd higherLine" style="width: 100px;">读数备注：</td>
                                <td colspan="3">
                                	<textarea rows="4" style="width:84%" id="readingCreateRemarks" class="required input-text {required:true,maxlength:1000}" ></textarea><span class="red"></span>
                                </td>
                                
                            </tr>
                        </table>
                        
                       
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
            <fieldset id="fieldset-1">
            	<legend>
                	<span>油费</span>
                    	
                </legend>
                
                <div class="container-main">

                    
                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />油费录入信息</td></tr>
                            <tr>
                            	<td class="menuTd" style="width: 100px;">录入油费(元)：</td>
                                <td style="width: 360px;">
									<input type="text"  id="fuelBills"/>
									<span class="red"></span>	
                                </td>
                                <td class="menuTd" style="width: 100px;">记录时间：</td>
                                <td>
									<input type="text" id="billsRecordingTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/>
									<span class="red"></span>	
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd higherLine" style="width: 100px;">费油备注：</td>
                                <td colspan="3">
                                	<textarea rows="4" style="width:84%" id="billsCreateRemarks" class="required input-text {required:true,maxlength:1000}" ></textarea><span class="red"></span>
                                </td>
                                
                            </tr>
                        </table>
                        
                       
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
            <div class="container-bottom">
            	<input type="button" value="添  加" style="width:60px;" onclick="addReadingAndBills();" name="save" />
            </div>
        </div>
        	<%--tab1结束，可重写--%>
    </div>
   
</body>
</html>

