var number_of_failures=0;//失败次数
var times;
$(document).ready(function(){
	times = setInterval('commonTimedRefreshTask()',300*1000);//5分钟轮询:300*1000
});

function commonTimedRefreshTask(){

	$.ajax({
                    type: "POST", 
                    url: "commonTimedRefreshTaskAction",
                    //data: senddata, 
                    dataType: "text",
                    //async:false,
                    success: function(data, textStatus) {
                    //console.log("刷新了!");
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                 number_of_failures++;
	                 if(number_of_failures>=5){clearInterval(times);}
	              }
                });
}
