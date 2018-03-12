
	//查询条件
	var driverName;							//司机姓名
	var bizId;								//终端号

	
	
	/**
	 * 高级查询
	 */
	function highSearchBtn () {
		//获取查询条件
		driverName = $("#driverNameHQText").val();							//司机姓名
		bizId = $("#bizId").val();								//区域名
		showAllDriverByCondition();
	}
	
	/**
	 * 普通司机搜索
	 */
	function searchDriver () {
		driverName = $("#driverNameSQText").val();				//人员名称
		bizId = $("#choice_bizId").val();
		showAllDriverByCondition();
	}
	
	
	$(document).ready(function(){
		providerOrgTree();
		promptCueDiv($("#driverNameSQText"),"请输入司机名");
		$("#multiQueryButton").click(function(){
			$("#confirm_div").show();
		})
		
		$("#searchBtn,#cancelSearch").click(function(){
			$(".advanced-search").toggle("fast");
		})
	})
	
	//查询所有司机信息带条件
	function showAllDriverByCondition () {
		driverPage.showLoading();
		//数据库操作
		$.ajax ({
			url : "cardispatchManage!findDriverInfoList.action" ,
			datatype : "json" ,
			type : "post" ,
			data : {  "driver#driverName" : driverName , "driver#driverBizId" : bizId } ,
			type : "post" , 
			success : function ( data ) {
				var data = eval ( "(" + data + ")" );
				driverPage.setDataArray(data);
				driverPage.refreshTable();
				driverPage.checkButton();
			}
		}); 
	}
	
	/**
	 * 勾选全部
	 */ 
	function allCheck(){
		if ($("#allcheckbox").attr("checked")) {
			$("#resultListTable tbody .input_checkbox").attr("checked",true);
		} else {
			$("#resultListTable tbody .input_checkbox").attr("checked",false);
		}
	}
	
	/**
	 * 删除司机信息
	 */ 
	function deleteDriver () {
		//获取删除id
		var strId = "";
		var chks = $("#resultListTable tbody .input_checkbox:checked");
		$(chks).each(function(index){
			var title = $(this).attr("title");
			strId += title;
			if ( index!= chks.length-1 ) {
				strId += ",";
			}
		});
		//判断是否有点选司机
		if ( chks == null || chks.length == 0) {
			alert("请选择司机");
			return;
		}
		//数据库操作
		$.ajax ({
			async : false ,
			url : "cardispatchManage!deleteDriverByIdsAjax.action" ,
			data : { "driverIds" : strId } ,
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
		showAllDriverByCondition();
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
			 $("#bizName").val(data.name);
			 $("#bizId").val(data.id);
			 searchDriver();
			if(orgId==null||orgId==""){
				orgId="16";
			}
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){ 
				createOrgTreeOpenFirstNode(data,"selectWorkPlace","carmanage_org_div","a","orgTreeClick");
				createOrgTreeOpenFirstNode(data,"selectWorkPlace2","carmanage_choice_org_div","a","choiceOrgTreeClick");
			},"json");
		}
	});
	 
}
function a(){}

//显示组织信息
function choiceOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#bizName").val(data.name);
	$("#bizId").val(data.orgId);
	$("#selectWorkPlace2").slideUp("fast");
} 

//显示组织信息
function orgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#choice_bizName").val(data.name);
	$("#choice_bizId").val(data.orgId);
	$("#selectWorkPlace").slideUp("fast");
} 