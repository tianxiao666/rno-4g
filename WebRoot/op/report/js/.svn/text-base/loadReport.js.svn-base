$(function(){
	//加载下拉框
	var value = $("#hiddenbaseStationLevel").val();
		var va = value.split(",");
		var context = "";
		$.each(va,function(k,v){
			if(v != null && v != ""){
				context = context +"<option class='faultGenera'  value='"+v+"'>"+v+"</option>";
			}
		});
		$("#rowValue").html(context);
});