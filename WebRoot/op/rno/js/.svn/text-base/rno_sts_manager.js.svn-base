
// ----------------------------------------话务性能查询页面v1.2---------------------------------------------//
var defaulttemplate = "<th style=\"width: 8%\">DATE</th>"
		+ "<th style=\"width: 10%\">PERIOD</th>"
		+ "<th style=\"width: 8%\">BSC</th>"
		+ "<th style=\"width: 8%\">CELL</th>"
		+ "<th style=\"width: 8%\">小区</th>"
		+ "<th style=\"width: 10%\">tch信道完好率</th>"
		+ "<th style=\"width: 10%\">定义信道数</th>"
		+ "<th style=\"width: 10%\">可用信道数</th>"
		+ "<th style=\"width: 10%\">载波数</th>"
		+ "<th style=\"width: 10%\">无线资源利用率</th>"
		+ "<th style=\"width: 10%\">话务量</th>"
		+ "<th style=\"width: 10%\">每线话务量</th>";
var defaultTitle = null;
var selectTitle = null;
var form_tab_0_selectedtemplate="";
var form_tab_1_selectedtemplate="";
var form_tab_0_selectedtabfileds=[];
var form_tab_1_selectedtabfileds=[];
var defaultdisplayhead="<th>DATE</th>" +
		"<th>PERIOD</th>" +
		"<th>BSC</th>" +
		"<th>CELL</th>";
$(document).ready(function() {
	// loadevent();
	// V1.2加载话务统计页面完成后：AJAX获取适用于指定用户的统计报表模板
	getPersonalReportTemplate("getPersonalReportTemplateForAjaxAction");
	// 通过选择统计模板的下拉框触发change事件
	var id1 = $("#rptTemplateId").find("option:selected").val();
	var id2 = $("#rptTemplateId2").find("option:selected").val();
	changeTitleHead("rptTemplateId", id1,"getStsRptTemplateFiledForAjaxAction");
	changeTitleHead("rptTemplateId2", id2,"getStsRptTemplateFiledForAjaxAction");
	$("#rptTemplateId").change(function() {
		// console.log("rptTemplateId");
		var id = $("#rptTemplateId").find("option:selected").val();
		// console.log("rptTemplateId改变了"+id);
		if (id > 0) {
			changeTitleHead("rptTemplateId", id,
					"getStsRptTemplateFiledForAjaxAction");
		} else {
			$("#tab_0_queryResultTab thead").html(defaulttemplate);
		}
	});
	$("#rptTemplateId2").change(function() {
		// console.log("rptTemplateId2");
		var id = $("#rptTemplateId2").find("option:selected").val();
		// console.log("rptTemplateId2改变了"+id);
		if (id > 0) {
			changeTitleHead("rptTemplateId2", id,
					"getStsRptTemplateFiledForAjaxAction");
		} else {
			$("#tab_1_queryResultTab thead").html(defaulttemplate);
		}
	});
	
	//获取rnoTableDicts
	getRnoTableDicts();
});
/**
 * 获取适用于指定用户的统计报表模板
 * 
 * @param {}
 *            action
 */
function getPersonalReportTemplate(action) {
	$.ajax({
				url : action,
				//data : ,
				dataType : 'json',
				type : 'post',
				success : function(data) {
					if (!data) {
						return;
					}
					// 绑定数据
					// console.log(data.length);
					for (var i = 0; i < data.length; i++) {
						$("#rptTemplateId").append("<option value='"
								+ data[i].id + "'>" + data[i].reportName
								+ "</option>");
						$("#rptTemplateId2").append("<option value='"
								+ data[i].id + "'>" + data[i].reportName
								+ "</option>");
					}
					/*var id1 = $("#rptTemplateId").find("option:selected").val();
					var id2 = $("#rptTemplateId2").find("option:selected").val();
					changeTitleHead("rptTemplateId", id1,"getStsRptTemplateFiledForAjaxAction");
					changeTitleHead("rptTemplateId2", id2,"getStsRptTemplateFiledForAjaxAction");*/
					/*
					 * animateInAndOut("operInfo", 500, 500, 1000, "operTip",
					 * data);
					 */
				},
				error : function(err, status) {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip", err);
				},
				complete : function() {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"模块加载完成");
				},
				async:false
			});

}
/**
 * 通过change事件：改变标题头
 */
function changeTitleHead(selecteId, id, action) {

	var sendData = {
		'rptTemplateId' : id
	};
	$.ajax({
				url : action,
				data : sendData,
				dataType : 'json',
				type : 'post',
				success : function(data) {
					if (!data) {
						return;
					}
					// 绑定数据
					// console.log(data);
					var str = "";
					// console.log(str);
					if (selecteId == "rptTemplateId") {
						selectTitle = data;
						form_tab_0_selectedtabfileds.length=0;
						//form_tab_0_selectedtabfileds=["STS_DATE","STS_PERIOD","ENGNAME","CELL"];
						//[i+4]
						for (var i = 0; i < data.length; i++) {
						str += "<th>" + data[i].DISPLAY_NAME + "</th>";
						form_tab_0_selectedtabfileds[i]=data[i].TABLE_FIELDS;
						}
						form_tab_0_selectedtemplate=defaultdisplayhead+str;
						$("#tab_0_queryResultTab thead").html(form_tab_0_selectedtemplate);
					}
					if (selecteId == "rptTemplateId2") {
						selectTitle = data;
						form_tab_1_selectedtabfileds.length=0;
						//form_tab_1_selectedtabfileds=["STS_DATE","STS_PERIOD","ENGNAME","CELL"];
						for (var i = 0; i < data.length; i++) {
						str += "<th>" + data[i].DISPLAY_NAME + "</th>";
						form_tab_1_selectedtabfileds[i]=data[i].TABLE_FIELDS;
						}
						form_tab_1_selectedtemplate=defaultdisplayhead+str;
						$("#tab_1_queryResultTab thead").html(form_tab_1_selectedtemplate);
					}
				},
				error : function(err, status) {

				},
				complete : function() {

				}
			});
}

/**
 * 获取rnoTableDicts
 * author:Liang YJ
 * date:2014.1.16
 */
function getRnoTableDicts()
{
	$.ajax({
		url : "getRnoTableDictsAction",
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if (!data) {
				return;
			}
			//console.log(data.length);
			//console.log(data[0]);
			defaultTitle = data;
			/*for(var i = 0; i < defaultTitle.length; i++)
			{
				console.log(i + " : " + defaultTitle[i].fieldDisplayName + "," + defaultTitle[i].fieldDbName);
			}*/
			
		},
		error : function(err, status) {
			//console.log(err);
		},
		complete : function() {

		}
	});
}

/**
 * @author Liang YJ
 * @param jsonObj
 * @date 2014-1-16 15:30
 * description:根据汇总的结果调整表格的字段
 */
function adjustTitle(formId,tabFlag,jsonObj)
{
	//console.log($("#audioStsType").val());
	//清除不作统计的字段对应的标题
	//console.log($("#"+tabFlag+"queryResultTab thead").html());
	var id;
	var ths = $("#"+tabFlag+"queryResultTab th");
	var cnt = "<th>记录数</th>";
	if(formId == "form_tab_0")
	{
		id = $("#rptTemplateId").find("option:selected").val();
		
	}else
	{
		id = $("#rptTemplateId2").find("option:selected").val();
	}
	/*if(id<0)
	{
		ths.each(function(k,v)
		{
			if("DATE"!=$(v).html() && "CELL"!=$(v).html() && "小区"!=$(v).html() && "BSC"!=$(v).html())
			{
				var flag = true;
				outer:for(var i = 0; i < defaultTitle.length; i++)
				{
					if($(v).html() == defaultTitle[i].fieldDisplayName)
					{
						inner:for(var key in jsonObj)
						{	
							if(defaultTitle[i].fieldDbName==key)
							{
								flag = false;
								break outer;
							}
						}
					break;
					}
				}
				if(flag)
				{
					console.log("remove: "+$(v).html());
					$(v).remove();
				}
			}
		});
	}else
	{*/
		//console.log(1+" : "+$("#"+tabFlag+"queryResultTab th:eq(1)").html());
		//$("#"+tabFlag+"queryResultTab th:eq(1)").remove();
		//console.log($("#"+tabFlag+"queryResultTab thead").html());
		//var title = "<th>DATE</th>" + "<th>BSC</th>" + "<th>CELL</th>";
		ths.each(function(k,v)
		{
					if("PERIOD"==$(v).html())
					{
						//console.log("remove:"+$(v).html());
						$(v).remove();
						return false;
					}
		});
		for(var i = 0; i < selectTitle.length; i++)
		{
			
			if("STS_DATE"!=selectTitle[i].TABLE_FIELDS && "CELL"!=selectTitle[i].TABLE_FIELDS && "ENGNAME"!=selectTitle[i].TABLE_FIELDS)
			{
				var flag = false;
				for(var key in jsonObj)
				{
					//console.log("key: "+key);
					if(selectTitle[i].TABLE_FIELDS == key)
					{
						flag = true;
						break;
					}
				}
				if(!flag)
				{
					ths.each(function(k,v)
					{
						if($(v).html()==selectTitle[i].DISPLAY_NAME)
						{
							//console.log("remove:"+$(v).html());
							$(v).remove();
						}
					});
				}
				
			}
			
		}
		//添加记录数字段标题
		$("#"+tabFlag+"queryResultTab th:last").after(cnt);
		//$("#"+tabFlag+"queryResultTab thead").html(title);
	//}
 
}

/**
 * @author Liang YJ
 * @date 2014-1-16 18:30
 * @param statisticData
 * 描述：显示返回的数据
 */
function showStatisticData(formId,table,statisticData,colorarr)
{
	var tr = "";
	var id;
	var ths = null;
	if(formId == "form_tab_0")
	{
		id=$("#rptTemplateId").find("option:selected").val();
		ths = $("#tab_0_queryResultTab th");
	}else
	{
		id=$("#rptTemplateId2").find("option:selected").val();
		ths = $("#tab_1_queryResultTab th");
	}
		/*if(id<0)
		{
			for(var i=0; i<statisticData.length; i++)
			{
				tr += "<tr bgcolor=\"" + colorarr[i % 2] + "\">";
				ths.each(function(k,v)
				{
					for(var j = 0; j < defaultTitle.length; j++)
					{
						if($(v).html() == defaultTitle[j].fieldDisplayName)
						{						
							tr += "<td>" + (statisticData[i][defaultTitle[j].fieldDbName] ? statisticData[i][defaultTitle[j].fieldDbName] : "") + "</td>";
							break;
						}else if("DATE" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["STS_DATE"] ? statisticData[i]["STS_DATE"] : "") + "</td>";
							break;
						}
						else if("BSC" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["ENGNAME"] ? statisticData[i]["ENGNAME"] : "") + "</td>";
							break;
						}
						else if("CELL" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["CELL"] ? statisticData[i]["CELL"] : "") + "</td>";
							break;
						}else
						{
							
						}
					}
				});
				tr += "</tr>";
			}
		}else
		{*/
			for(var i=0; i<statisticData.length; i++)
			{
				tr += "<tr bgcolor=\"" + colorarr[i % 2] + "\">";
				ths.each(function(k,v)
				{
					for(var j = 0; j < selectTitle.length; j++)
					{
						if($(v).html()==selectTitle[j].DISPLAY_NAME)
						{
							tr += "<td>" + (statisticData[i][selectTitle[j].TABLE_FIELDS] ? statisticData[i][selectTitle[j].TABLE_FIELDS] : "") + "</td>";
							break;
						}else if("DATE" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["STS_DATE"] ? statisticData[i]["STS_DATE"] : "") + "</td>";
							break;
						}
						else if("BSC" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["ENGNAME"] ? statisticData[i]["ENGNAME"] : "") + "</td>";
							break;
						}
						else if("CELL" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["CELL"] ? statisticData[i]["CELL"] : "") + "</td>";
							break;
						}else if("记录数" == $(v).html())
						{
							tr += "<td>" + (statisticData[i]["CNT"] ? statisticData[i]["CNT"] : "") + "</td>";
							break;
						}
					}
				});
				tr +="</tr>";
			}
		//}
	table.append(tr);
}
