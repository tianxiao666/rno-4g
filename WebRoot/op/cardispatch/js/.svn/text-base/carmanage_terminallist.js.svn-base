
	//查询条件
	var mobileType ;						//设备类型
	var clientimei;							//设备号
	var carNo;								//车牌号码
	var launchedTime;						//开通时间
	var telphoneNo;							//电话
	var bizId;
	
	$(function(){
		providerOrgTree();
		promptCueDiv($("#clientImei"),"请输入imei码");
	})
	
	
	/**
	 * 查询所有终端信息带条件
	 * @param oper 操作
	 */
	function showAllMobileByCondition () {
		$("#mobileList_table tbody").empty();
		terminalPage.showLoading();
		//数据库操作
		$.ajax ({
			url : "cardispatchManage!findMobileInfoList.action" ,
			datatype : "json" ,
			type : "post" ,
			data : {  "terminal#terminalBizId" : bizId , "terminal#mobileType" : mobileType , "terminal#clientimei" : clientimei , "terminal#carNumber" : carNo , "terminal#launchedTime" : launchedTime , "terminal#telphoneNo" : telphoneNo } ,
			type : "post" , 
			success : function ( data ) {
				var data = eval ( "(" + data + ")" );
				terminalPage.setDataArray(data);
				terminalPage.refreshTable();
				terminalPage.checkButton();
			}
		}); 
	}
	
	/**
	 * 勾选全部
	 */ 
	function allCheck(){
		if ($("#allcheckbox").attr("checked")) {
			$("#mobileList_table tbody .input_checkbox").attr("checked",true);
		} else {
			$("#mobileList_table tbody .input_checkbox").attr("checked",false);
		}
	}
	
	/**
	 * 删除终端信息
	 */ 
	function deleteMobile () {
		//获取删除id
		var strId = "";
		var chks = $("#mobileList_table tbody .input_checkbox:checked");
		$(chks).each(function(i){
			var title = $(this).attr("title");
			strId += title;
			if ( i!= chks.length-1 ) {
				strId += ",";
			}
		});
		//判断是否有点选终端
		if (chks == null || chks.length == 0 ) {
			alert("请选择终端！");
			return;
		}
		//数据库操作
		$.ajax ({
			async : false ,
			url : "cardispatchManage!deleteTerminalByIdsAjax.action" ,
			data : { "termianlIds" : strId } ,
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
		showAllMobileByCondition();					//查询所有终端的信息
	}
	
	
	/**
	 * 高级查询
	 */
	function highSearchBtn () {
		//获取查询条件
		mobileType = $("#mobileType").val();			//设备类型
		clientimei = $("#clientimei").val();			//设备号
		carNo = $("#carNo").val();						//车牌号码
		launchedTime = $("#launchedTime").val();		//开通时间
		telphoneNo = $("#telphoneNo").val();			//电话
		bizId = $("#mobile_bizId").val();
		carNo = $.trim(carNo);
		showAllMobileByCondition();
		
	}
	
	/**
	 * 普通人员搜索
	 */
	function searchImei () {
		mobileType ="" ;
		carNo = "";
		launchedTime = "";
		telphoneNo = "";
		clientimei = $("#clientImei").val().replace(" ","");				//人员名称
		bizId = $("#mobile_bizId").val().replace(" ","");
		showAllMobileByCondition();
	}
	
	
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
			$("#mobile_bizName").val(data.name);
			$("#mobile_bizId").val(data.orgId);
			searchImei();
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
	$("#mobile_bizName").val(data.name);
	$("#mobile_bizId").val(data.orgId);
	$("#selectWorkPlace").slideUp("fast");
} 
	
	
	
	
	
	
	
	
	
	
	
	
	