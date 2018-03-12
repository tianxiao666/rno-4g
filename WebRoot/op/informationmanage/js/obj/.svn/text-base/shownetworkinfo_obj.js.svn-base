var projectBaseInfo = null;		//项目信息tab
var projectPageSize = [10,20,30];

$(document).ready(function(){
	//企业信息集合分页
	var projectOpt = {
				"pageSize" : projectPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#project_list_table") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["id"] ) {
										return;
									}
									var idTd = $("<td/>");
									$(idTd).text(getObjectData(tdData,"id",""));
									var nameTd = $("<td/>").text(getObjectData(tdData,"name",""));
									var responsibilityDescriptionTd = $("<td/>").text(getObjectData(tdData,"responsibilityDescription",""));
									var clientNameTd = $("<td/>").text(getObjectData(tdData,["clientEnterprise","fullName"],""));
									var serverNameTd = $("<td/>").text(getObjectData(tdData,["serverEnterprise","fullName"],""));
									$(tr).append($(idTd));
									$(tr).append($(nameTd));
									$(tr).append($(responsibilityDescriptionTd));
									$(tr).append($(clientNameTd));
									$(tr).append($(serverNameTd));
									var operaTd = $("<td/>");
									var resource = tdData.toSource();
									var findA = $("<a/>");
									$(findA).attr({"href":"javascript:void(0);"}).addClass("ent_showBtn").text("查看");
									var editA = $("<a/>");
									$(editA).attr({"href":"javascript:void(0);"}).text("修改");
									$(tr).append($(operaTd));
									$(operaTd).append($(findA));
									$(operaTd).append("&nbsp;/&nbsp;");
									$(operaTd).append($(editA));
									//查看按钮
									$(findA).click( {"tdData" : tdData} , function(_event){
										var d = _event.data.tdData;
										find_single_project(d);
										$("#project_Dialog").find("[cancelBtn]").click();
										$(".black").fadeIn(200);
										dialogShow($("#project_Dialog"),200);
									});
									//修改按钮
									$(editA).click( {"tdData" : tdData} , function(_event){
										var d = _event.data.tdData;
										find_single_project(d);
										setTimeout(function(){
											$("#project_Dialog").find("[editBtn]").click();
											$(".black").fadeIn(200);
											dialogShow($("#project_Dialog"),200);
										},100);
									});
								 }
			}
	projectPage = new TablePage(projectOpt);
})




