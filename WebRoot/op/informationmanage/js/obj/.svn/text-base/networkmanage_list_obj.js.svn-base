var projectPage = null;		//企业信息分页实例
var projectPageSize = 10;
var project_look_dialog = null; //查看企业信息对话框


$(document).ready(function(){
	//企业信息集合分页
	var projectOpt = {
				"pageSize" : projectPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#project_list_table") , 
				"effect" : 2 , 
				"columnMethod" : function ( i , key,tdData , tr ) { 
									if ( tdData == null || !tdData["id"] ) {
										return;
									}
									var idTd = $("<td/>");
									$(idTd).text(getObjectData(tdData,"projectNumber",""));
									var nameTd = $("<td/>").text(getObjectData(tdData,"name",""));
									var responsibilityDescriptionTd = $("<td/>").text(getObjectData(tdData,"responsibilityDescription",""));
									var clientNameTd = $("<td/>").text(getObjectData(tdData,"clientEnterpriseName",""));
									var serverNameTd = $("<td/>").text(getObjectData(tdData,"serverEnterpriseName",""));
									$(tr).append($(idTd));
									$(tr).append($(nameTd));
									$(tr).append($(responsibilityDescriptionTd));
									$(tr).append($(clientNameTd));
									$(tr).append($(serverNameTd));
									var operaTd = $("<td/>");
									var resource = obj2String(tdData);
									var findA = $("<a/>");
									var projectId = getObjectData(tdData,"id","");
									$(findA).attr({"target":"blank","href":"show_networkresource_info.jsp?projectId="+projectId}).addClass("ent_showBtn").text("查看");
									$(tr).append($(operaTd));
									$(operaTd).append($(findA));
								 }
			}
	projectPage = new TablePage(projectOpt);
})




