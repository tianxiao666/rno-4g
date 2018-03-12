
//projectPage 项目列表对象

$(document).ready(function(){
	find_all_project_list();
})


//查找所有企业信息
function find_all_project_list () {
	//读取所有企业信息
	$.ajax({
			"url" : "projectmanage_ajax!findAllProjectInfoAjax.action" , 
			"type" : "post" , 
			"success" : function( result ) {
							result = eval ( result );
							projectPage.setDataArray(result);
							projectPage.refreshTable();
							projectPage.checkButton();
						} 
	});
}
