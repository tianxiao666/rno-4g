var enterprisePage = null;		//企业信息分页实例
var enterprisePageSize = 10;
var enterprise_look_dialog = null; //查看企业信息对话框

$(document).ready(function(){
	//企业信息集合分页
	var enterpriseOpt = {
				"pageSize" : enterprisePageSize , 
				"dataArray" : new Array() , 
				"table" : $("#enterprise_list_table") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["fullName"] ) {
										return;
									}
									var fullNameTd = $("<td/>");
									$(fullNameTd).text(getObjectData(tdData,"fullName",""));
									var registerNumberTd = $("<td/>").text(getObjectData(tdData,"registerNumber",""));
									var ownershipTd = $("<td/>").text(getObjectData(tdData,"ownership",""));
									var legalRepresentativeTd = $("<td/>").text(getObjectData(tdData,"legalRepresentative",""));
									var businessSphereTd = $("<td/>").text(getObjectData(tdData,"businessSphere",""));
									var addressTd = $("<td/>").text(getObjectData(tdData,"registerAddress",""));
									var cooperativeTd = $("<td/>").text(getObjectData(tdData,"cooperative",""));
									$(tr).append($(fullNameTd));
									$(tr).append($(registerNumberTd));
									$(tr).append($(ownershipTd));
									$(tr).append($(legalRepresentativeTd));
									$(tr).append($(businessSphereTd));
									$(tr).append($(addressTd));
									$(tr).append($(cooperativeTd));
									
									var operaTd = $("<td/>");
									var resource = obj2String(tdData);
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
										find_single_enterprise(d);
										$("#ent_Dialog").find("[cancelBtn]").click();
										$(".black").fadeIn(200);
										dialogShow($("#ent_Dialog"),200);
									});
									//修改按钮
									$(editA).click( {"tdData" : tdData} , function(_event){
										var d = _event.data.tdData;
										find_single_enterprise(d);
										setTimeout(function(){
											$("#ent_Dialog").find("[editBtn]").click();
											$(".black").fadeIn(200);
											dialogShow($("#ent_Dialog"),200);
										},100);
									});
								 }
			}
	enterprisePage = new TablePage(enterpriseOpt);
})




