
$(document).ready(function(){

	//验证
	formcheck({
		"form" : $("#useCar_form") , 
		"subButton" : $("#useCarBtn") , 
		"isAjax" : false , 
		"formSubmiting" : function () {
			var mileage = $("#mileage").val().replace(" ","");
			if ( mileage == "" ) {
				alert("请填写里程数!");
				return ;
			}
			//$("#useCar_form").submit();
		}
	});
})
