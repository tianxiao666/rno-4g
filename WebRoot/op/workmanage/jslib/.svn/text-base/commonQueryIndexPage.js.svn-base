
/*
*比较两个时间的大小 输入时间格式:yyyy-MM-dd HH:mm:ss
*若time2>=time1 则返回1 
*若tim2<time1 则回0
*/
function compareTime(time1,time2){
	var dA=new Date(time1.replace(/-/g,"/"));
	var dB=new Date(time2.replace(/-/g,"/"));
	if(Date.parse(dA)<=Date.parse(dB))
		return 1;
	else
		return 0;
}