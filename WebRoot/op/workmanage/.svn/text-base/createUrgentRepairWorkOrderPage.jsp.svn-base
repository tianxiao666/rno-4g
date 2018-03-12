<%@ page contentType="text/html; charset=utf-8" language="java"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>


<meta http-equiv=Content-Type content=text/html;charset=utf-8>

<link rel="stylesheet" type="text/css" href="css/base.css"/>
<link rel="stylesheet" type="text/css" href="css/public.css"/>
<style type="text/css">
	.aa_button ul li{margin: 0 5px;}
	.aa_button label{display: inline-block; vertical-align:top; font-size: 14px; margin-top: 15px; text-align: center; width: 35px;}
	.aa_button span{display: inline-block; padding:0 8px;  overflow: hidden;}
	.aa_button button{white-space:nowrap; vertical-align:top; width:140px;  font-size: 14px; height:50px; margin:5px;}
</style>


<%@include file="common.jsp"%>
<script type="text/javascript" src="jslib/jquery/jquery-1.6.2.min.js"></script>
<script src="op/workmanage/jslib/createUrgentRepairWorkOrderPage.js" type="text/javascript"></script>


</head>

<body>
	

	<div class="aa_button">
		<ul style="">
			<li id="om_product">
				<label>运维生产</label>
				<span>
					<%-- <button>创建故障抢修工单</button>
					<button>创建故障抢修工单</button>
					<button>创建故障抢修工单</button>
					<button>创建故障抢修工单</button> --%>
				<button onclick="createButtonAction('op/urgentrepair/createWorkOrder.jsp')" id="btn_createurgentrepairworkorder">创建故障抢修工单</button><button onclick="createButtonAction('op/urgentrepair/quickCreateWorkOrder.jsp')" id="btn_createnetworkoptimizeworkorder">快速创建EMOS工单</button><button disabled="disabled" onclick="createButtonAction('undefined')" id="btn_createrepairworkorder">创建修缮工单</button><button disabled="disabled" onclick="createButtonAction('undefined')" id="btn_createownerkeepupworkorder">创建业主维系工单</button></span>
			</li>
			<li id="om_pay">
				<label>运维交付</label>
				<span>
				<button disabled="disabled" onclick="createButtonAction('undefined')" id="btn_projectmanageworkorder">项目管理工单</button></span>
			</li>
			<li id="om_support">
				<label>运维支撑</label>
				<span>
				<button disabled="disabled" onclick="createButtonAction('undefined')" id="btn_projectbudgetapply">项目预算申请</button></span>
			</li>
		</ul>
	</div>
	
	<div id="draftWorkOrder-Grid" style="margin-top:30px;"></div>
</body>

</html>