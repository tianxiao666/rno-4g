var date_btn = 1;
var dateTimeBtn = "";

$(document).ready(function(){
	
	/*********** 默认 **********/
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserInfo.action" , 
		"type" : "post" , 
		"async" : true , 
		"success" : function( data ){
			data = eval( "(" + data + ")" );
			$("#useCarPersonName").val(data.name);
			$("#useCarPersonAccountId").val(data.account);
		}
	});
	$("#bg_time").val(new Date().toString("yyyy-MM-dd HH:mm"));
	$("#ed_time").val(new Date().toString("yyyy-MM-dd HH:mm"));
	
	/********** 注册事件 ***********/
	$("#criticalClass").change(function(){
		getReturnCarTime();
	});
	
	$("#calendardiv :button,#calendardiv table td").click(function(){
		if ( date_btn == 1 ) {
			getReturnCarTime();
		}
		var bgTime = $(dateTimeBtn).val();
		var d = bgTime.toDate("yyyy-MM-dd HH:mm");
		bgTime = d.toString("yyyy-MM-dd HH:mm");
		$(dateTimeBtn).val(bgTime);
	});
	
	
	
	
	
	//设置默认用车时间
	setUseCarTime();
})


function bg_date_buttonClick( event ) {
	date_btn = 1;
	fPopCalendar(event,document.getElementById('bg_time'),document.getElementById('bg_time'),true);
	dateTimeBtn = "#bg_time";
}

function ed_date_buttonClick( event ) {
	date_btn = 2;
	fPopCalendar(event,document.getElementById('ed_time'),document.getElementById('ed_time'),true);
	dateTimeBtn = "#ed_time";
}

/*********** 区域 select begin *************/

function createAreaSelect () {
	$.ajax({
		"url" : "" , 
		"type" : "post" , 
		"async" : true , 
		"success" : function( data ){
			data = eval( "(" + data + ")" );
			$("#useCarPeopleName").val(data.name);
			$("#useCarPeopleAccountId").val(data.account);
		}
	});
}

/*********** 区域 select end *************/








	//设置默认用车时间
	function setUseCarTime () {
	  	var dateNow = new Date();
		dateNow = dateNow.addMinutes(30);
		var dateString = dateNow.toString("yyyy-MM-dd HH:mm");
		$("#bg_time").val(dateString);
		getReturnCarTime();
	}

	
	/**
	 * 根据用车紧急程序、用车时间，计算还车时间
	 */
	function getReturnCarTime() {
		var useCarTime = $("#bg_time").val();
		if ( useCarTime == "" ) {
			setUseCarTime();
			useCarTime = $("#bg_time").val();
		}
		
		try{
			useCarTime = useCarTime.toDate("yyyy-MM-dd HH:mm");
		} catch ( ex ) {
			setUseCarTime();
			useCarTime = $("#bg_time").val();
			useCarTime = useCarTime.toDate("yyyy-MM-dd HH:mm");
		}
		var now = new Date();
		var returnCarDate = useCarTime.clone();
		var hour = useCarTime.getHours();
		if ( hour >= 18 ) {
			returnCarDate.setHours(returnCarDate.getHours()+2);
		} else if ( hour < 18 ) {
			returnCarDate.setHours(18);
		}
		returnCarDate.setMinutes(0);
		returnCarDate.setSeconds(0);
		$("#ed_time").val(returnCarDate.toString("yyyy-MM-dd HH:mm"));
	}
	
	
	
	