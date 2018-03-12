$(document).ready(function(){
	
		$("#perscope").click(function(){
			//$("#selectarea").html("");
			$("#selectarea").css("display","none");
		});
		$("#areascope").click(function(){
		    //$("#selectarea").html("省<select name=\"select\" id=\"select\"><option>广东省</option></select>市<select name=\"select\" id=\"select\"><option>广州市</option></select>区<select name=\"select\" id=\"select\"><option>天河区</option></select>");
		    $("#selectarea").css("display","block");
		});
		$("#cityscope").click(function(){
			 //$("#selectarea").html("省<select name=\"select\" id=\"select\"><option>广东省</option></select>市<select name=\"select\" id=\"select\"><option>广州市</option></select>");
			 $("#selectarea").css("display","block");
		});
		//$('#colorpicker0').farbtastic('#colortd');
		$('#colorpicker0').farbtastic('#style');
		
		//console.log($("#style").css("background-color"));
		//$("#style").css("background-color","+$('#colortd').css('background-color')+");
		// 联动----区域
		$("#provinceId").change(function() {
			getSubAreas("provinceId", "cityId", "市");
		});
		$("#cityId").change(function() {
			getSubAreas("cityId", "areaId", "区/县");
		});
		$("#conditionLoadBtn").click(function(){
			$("#conditionForm").submit();
		return true;
		});
		//自定义条件的校验
	$("#conditionForm").validate({
		
		submitHandler:function(form){
			
			storageFormulaCondition();//存储自定义查询条件
		}
	});
	});
/**
 * 获取色彩拾取对象
 * @param {} dialogDivId
 * @param {} farbtasticDivId
 * @param {} colorTable
 */
function farbtasticColor(dialogDivId,farbtasticDivId,colorTable){
		$("#"+dialogDivId).css({
			"top" :(40) + "px",
			"right" :(486) + "px",
			"width":(200)+"px",
			"height":(242)+"px",
			"z-index": (40)
			});		
			$(".colordialog2").hide();
			$("#"+dialogDivId).show();
		 $('#'+farbtasticDivId).farbtastic('#'+colorTable);
		 //console.log($('#'+farbtasticDivId).farbtastic());
		 $("#style").css("background-color",$('#colortd').css('background-color'));
	}
/**
 * 获取话统中文字段信息追加至自定义指标条件文本域
 * @param {} a
 */
function getIndex(a){
		
		//console.log("\""+$(a).text()+"\"");
		var indexval="\""+$(a).text()+"\"";
		//$("#formulcondition").text(indexval);
		$("#formulcondition").append(indexval);
		
	}
/**
 * 通过在色彩拾对象上鼠标弹起触发此函数：解决farbtastic无法同时触发的问题
 */
function mouseupcolor(){
		//console.log("进入mousemovecolor函数");
		//console.log($('#style').css('background-color'));
		$("#colortd").css("background-color",$('#color').css('background-color'));
		//$("#style").css("color",$('#colortd').css('background-color'));
	}
/**
 * 条件过滤
 */
function filterCondition(){
		$("#stsinfotab a").each(function(){
												   
				var b=$(this).text();
				//console.log(b);
				alert(b);
			});
		
	}
/**
 * 存储自定义公式条件
 */
function storageFormulaCondition(){
	
 /*var formulcondition=$("#formulcondition").text();
 var result = formulcondition.replace(/"([^"]*)"/g, function(word){
  //console.log('>',word);
  return word.replace(/'/g, '');
});
 console.log(formulcondition);
 return;
 $("#").text(result);
 var sendDate={"rnoUserDefinedFormul['condition']":result};*/
$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
				url : 'reciveStsUserDefinedConditionAndStorageAction',
				dataType : 'text',
				//data:sendDate,
				type : 'post',
				success : function(data) {
				   var mes_obj=eval("("+data+")");
//					console.log(mes_obj);
					 animateInAndOut("operInfo", 500, 500, 1000,
									"operTip", mes_obj);
				},
				error : function(err, status) {
//					console.error(status);
					$(".loading_cover").css("display", "none");
					animateInAndOut("operInfo", 500, 500, 1000,
									"operTip", status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}