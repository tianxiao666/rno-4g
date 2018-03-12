/**
 *扩展验证框架里面没有的方法
 */
 
 //修改默认的提示消息
  jQuery.extend(jQuery.validator.messages, {
  required: "必选字段",
  remote: "请修正该字段",
  email: "请输入正确格式的电子邮件",
  url: "请输入合法的网址",
  date: "请输入合法的日期",
  dateISO: "请输入合法的日期 (ISO).",
  number: "请输入合法的数字",
  digits: "只能输入整数",
  creditcard: "请输入合法的信用卡号",
  equalTo: "请再次输入相同的值",
  accept: "请输入拥有合法后缀名的字符串",
  maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
  minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
  rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
  max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
  min: jQuery.validator.format("请输入一个最小为 {0} 的值")
});
 

 //日期比较验证
jQuery.validator.addMethod("compareDate",
	function(value, element, param) {
	    
		var startDate = $(param).val();
		
        return startDate < value;
	},
	"结束时间必须大于开始时间!");

//练习结束时间与考试开始时间比较验证
jQuery.validator.addMethod("compareExamDate",
	function(value, element, param) {
		var endDate = $(param).val();
        return endDate < value;
	},
	"考试开始时间比须大于练习结束时间 !");

jQuery.validator.addMethod("compareWorkOrderDate",
	function(value, element, param) {//value  故障发生时间
	   var startDate = $(param).val();
	   var dateSelect = value.split("-");
	   var dateNow = startDate.split("-");
	   var flag
	   if(dateSelect[0]<dateNow[0]){
		   flag = true;
	   }else if(dateSelect[0]>dateNow[0]){
		   flag = false;
	   }else if(dateSelect[0]==dateNow[0]){
		   if(dateSelect[1]<dateNow[1]){
			   flag = true;
		   }else if(dateSelect[1]>dateNow[1]){
			   flag = false;
		   }else if(dateSelect[1]==dateNow[1]){
			   if(dateSelect[2].split(" ")[0]<=dateNow[2].split(" ")[0]){
				   flag = true;
			   }else if(dateSelect[2].split(" ")[0]>dateNow[2].split(" ")[0]){
				   flag = false;
			   }
		   }
	   }
        return flag;
	},
	"必须小于当前时间");

jQuery.validator.addMethod("compareDealDate",
	function(value, element, param) {//value  故障发生时间
	   var startDate = $(param).val();
        return startDate < value;
	},
	"不能小于当前时间");

jQuery.validator.addMethod("validateIsEmpty", function(value, element, param) {

	if(value==""){
		return false;
	}else{
		return true;
	}
}, "必选字段");

// 下拉框验证
jQuery.validator.addMethod("selectNone", function(value, element, param) {
	return value != "";
}, "必须"); 

// 整数验证
jQuery.validator.addMethod("validateDigit", function(value, element, param) {
	if(/^-*[0-9]+$/.test(value)||value==""){
		return true;
	}else{
		return false;
	}
}, "请填写：整数，如“12”"); 

