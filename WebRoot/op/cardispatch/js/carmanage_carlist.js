	
	//查询条件
	var carNumber = "";							//车牌号
	var carType = "";							//车类型
	var driverName = "";							//司机姓名
	var clientimei = "";							//终端号
	var bizId = "";								//业务单元
	
	$(function(){
		providerOrgTree();
		promptCueDiv($("#carNumberSQText"),"请输入车牌号");
	})
	
	/**
	 * 高级查询
	 */
	function highSearchBtn () {
		
		//获取查询条件
		bizId = $("#choice_bizId").val();
		carNumber = $("#carNumber").val();							//车牌号
		carType = $("#carType").val();								//车类型
		driverName = $("#driverName").val();							//司机姓名
		clientimei = $("#clientimei").val();								//终端号
		if (carType == "请选择") {
			carType = "";
		}
		showAllCarByCondition();
	}
	
	/**
	 * 普通普通搜索
	 */
	function searchDriver () {
		carType = "";								//车类型
		driverName = "";							//司机姓名
		clientimei = "";								//终端号
		bizId = $("#choice_bizId").val();
		carNumber = $("#carNumberSQText").val();				//人员名称
		showAllCarByCondition();
	}
	
	
	/**
	 * 查询所有终端信息带条件
	 * @param oper 操作
	 */
	function showAllCarByCondition () {
		carPage.showLoading();
		//数据库操作
		$.ajax ({
			url : "cardispatchManage_ajax!findCarInfoList.action" ,
			datatype : "json" ,
			type : "post" ,
			data : {  "car#carNumber" : carNumber , "car#carType" : carType , "car#driverName" : driverName , "car#clientimei" : clientimei , "car#carBizId" : $("#choice_bizId").val()  } ,
			type : "post" , 
			success : function ( data ) {
				var data = eval (data);
				carPage.setDataArray(data);
				carPage.refreshTable();
				carPage.checkButton();
			}
		}); 
	}
	
	/**
	 * 勾选全部
	 */ 
	function allCheck(){
		if ($("#allcheckbox").attr("checked")) {
			$("#carInfoTable tbody .input_checkbox").attr("checked",true);
		} else {
			$("#carInfoTable tbody .input_checkbox").attr("checked",false);
		}
	}
	
	/**
	 * 删除车辆信息
	 */ 
	function deleteCar () {
		//获取删除id
		var strId = "";
		var chks = $("#carInfoTable tbody .input_checkbox:checked");
		if(chks.length==0) {
			alert("请选择车辆！！");
			return;
		}
		
		$(chks).each(function(i){
			var title = $(this).attr("title");
			strId += title;
			if ( i!= chks.length-1 ) {
				strId += ",";
			}
		});
		
		//数据库操作
		$.ajax ({
			async : false ,
			url : "cardispatchManage!deleteCarByIdsAjax.action" ,
			data : { "carIds" : strId } ,
			type : "post" ,
			datatype : "text" , 
			success : function (data) {
				if ( !!data ) {
					alert("操作成功");
				} else {
					alert("操作失败");
				}
			} 
		}); 
		showAllCarByCondition();
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
			$("#choice_bizName").val(data.name);
			$("#choice_bizId").val(data.orgId);
			showAllCarByCondition();
			if(orgId==null||orgId==""){
				orgId="16";
			}
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"selectWorkPlace","carmanage_org_div","a","orgTreeClick");
			},"json");
		}
	});
}
function a(){}

//显示组织信息
function orgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#choice_bizName").val(data.name);
	$("#choice_bizId").val(data.orgId);
	$("#selectWorkPlace").slideUp("fast");
} 
	