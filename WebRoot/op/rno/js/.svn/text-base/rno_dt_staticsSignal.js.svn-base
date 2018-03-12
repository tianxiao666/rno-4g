/*
var dtopts={
"signalstrength":[
 { 'min':-60,'max':0,"style":[{"color": "#4EA04E"}]}
,{ 'min':-65,'max':-60,"style":[{"color": "#42FF42"}]}
,{ 'min':-70,'max':-65,"style":[{"color": "#4BFFFF"}]}
,{ 'min':-75,'max':-70,"style":[{"color": "#4040FF"}]}
,{ 'min':-80,'max':-75,"style":[{"color": "#9D9D48"}]}
,{ 'min':-85,'max':-80,"style":[{"color": "#FFFF41"}]}
,{ 'min':-90,'max':-85,"style":[{"color": "#FF49FF"}]}
,{ 'min':-100,'max':-90,"style":[{"color": "#FF4141"}]}
],
"signalquality":[
 { 'min':0,'max':1,"style":[{"color": "#4EA04E"}]}
,{ 'min':1,'max':2,"style":[{"color": "#42FF42"}]}
,{ 'min':2,'max':3,"style":[{"color": "#4BFFFF"}]}
,{ 'min':3,'max':4,"style":[{"color": "#4040FF"}]}
,{ 'min':4,'max':5,"style":[{"color": "#9D9D48"}]}
,{ 'min':5,'max':6,"style":[{"color": "#FFFF41"}]}
,{ 'min':6,'max':7,"style":[{"color": "#FF49FF"}]}
,{ 'min':7,'max':8,"style":[{"color": "#FF4141"}]}
],
"signalstructure": [
 { 'min':0,'max':4,"style":[{"color": "#47FF47"}]}
,{ 'min':4,'max':7,"style":[{"color": "#4343FF"}]}
,{ 'min':7,'max':10,"style":[{"color": "#FFFF4E"}]}
,{ 'min':10,'max':13,"style":[{"color": "#FF4343"}]}
],
"primaryservice":[
 {"style":[{"strokeColor": "#003399","strokeWeight": 6,"strokeOpacity": 0.5}]}
],
"weakcoverage": [
 { "style":[{"color": "#BCBCBC"}]}
,{ 'distance':100,'rxlevsub':-85,"style":[{"color": "#FFFF00"}]}
,{ "style":[{"color": "#00FEFF"}]}
,{ "style":[{"color": "#00FF00"}]}
,{ "style":[{"color": "#0033CC"}]}
],
"nomaincoverage": [
 { "style":[{"color": "#FFFF00"}]}
,{ 'cellnum':4,'dbcomparaval':6,"style":[{"color": "#00FEFF"}]}
,{ "style":[{"color": "#00FF00"}]}
,{ "style":[{"color": "#0033CC"}]}
,{ "style":[{"color": "#FF0000"}]}
],
"rapidattenuation": [
 { "style":[{"color": "#993366"}]}
,{ 'second':5,'dbattenuationval':15,"style":[{"color": "#FF0000"}]}
],
"overcoverage": [
 { "style":[{"color": "#993366"}]}
,{ 'cellnum':5,'multiple':2.5,"style":[{"color": "#FF0000","strokeWeight":1}]}
],
"signalandantennanotmatch": [
{ "state":"normal","style":[{"color": "#993366"}]}
,{ "state":"obviousbias","style":[{"color": "#FF9900"}]}
,{ "state":"backcover","style":[{"color": "#FF0000"}]}
]
};*/



function clearPreState(){
	dtSampleContainer.clearOnlyExtraOverlay();
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
	gisCellDisplayLib.releaseOptions();
	
	currentSampleOccupyMode=sampleOccupyMode_IDLE;
	currentCellCoverMode = cellCoverMode_WAIT_F_CELL;
}

//显示操作提示信息
function showOperTips(tip){
	$("#operInfo").css("display","");
	$("#operInfo").find("#operTip").html(tip);
}

function animateOperTips(tip){
	$("#operInfo").find("#operTip").html(tip);
	animateInAndOut("operInfo", 500, 500, 2000);
}

function hideOperTips(){
	$("#operInfo").css("display","none");
}

/**
 * 统计信号强度
 *  
 */
function staticsSignalStrength(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalStrength(rule);
}

/**
 * 统计信号质量
 *  
 */
function staticsSignalQuality(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalQuality(rule);
}

/**
 * 统计信号结构图
 *  
 */
function staticsSignalStructure(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalStructure(rule);
}

/**
 * 全路段主服务覆盖图
 *  
 */
function staticsPrimaryService(rule)
{
	clearPreState();
	dtSampleContainer.staticsPrimaryService(rule);
}


/**
 * 弱覆盖分析图
 *  
 */
function staticsWeakCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsWeakCoverage(rule);
}

/**
 * 无主覆盖分析
 *  
 */
function staticsNoMainCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsNoMainCoverage(rule);
}

/**
 * 过覆盖分析
 *  
 */
function staticsOverCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsOverCoverage(rule);
}

/**
 * 室分外泄
 */
function analysisIndoorSignalLeakOutside(rule){
	clearPreState();
	dtSampleContainer.staticsIndoorSignalLeakOutside(-80,12,rule);
}

/**
 * 天线方向与覆盖方向不符
 */
function staticsSignalAndAntennaNotMatch(rule){
	clearPreState();
	dtSampleContainer.staticsSignalAndAntennaNotMatch(rule);
}
/**
 * 信号快速衰减分析
 *  
 */
function staticsRapidAttenuation(rule){
　clearPreState();
  dtSampleContainer.staticsRapidAttenuation(rule);
}
function find(value)
{
	for(var i = 0; i < this.length; i++)
	{
		if(value == this[i])
			return true;
	}
	return false;
}

 /**
* 是否为快速衰减：是返回true,不是返回false
* */
/*
 function isNotRapidAttenuation(samplearray){

  var sampleobj=new Array();
  var startsampleTimeStr;
  var sampleTimeStr;
  var startrxLevSub;
  var rxLevSub;
  var initstart=true;
  var flag=true;
	for(var i=0;i<sampleArray.length;i++){
			rxLevSub=sampleArray[i]['rxLevSub'];

				sampleTimeStr=sampleArray[i]['sampleTimeStr'];
				sampleobj.push(sampleArray[i]);

			if(initstart==true){startsampleTimeStr=sampleTimeStr;startrxLevSub=rxLevSub;initstart=false;}
			if(sampleTimeStr>(startsampleTimeStr+dtopts.rapidattenuation[1].second)){
				for(var j=1;j<sampleobj.length;j++){
					if(Math.abs(startrxLevSub-sampleobj[j]['rxLevSub'])<15){
						flag=false;
						break;
					}
				}
				if(flag==true){return true;}
				if(flag==false){initstart=true;}
			}
			
		}
		return flag;

}
*/	
/**
*传入时间字串，和递增秒数获取新的时间
*/
function getNewDateTime(timestr,seconds){

		//var dd="2007-08-31 23:00:00";
		//替换所有-,否则转换时会出错
		var trantimestr=timestr.replace(/[-]/g, "/");
		//console.log(aa);
		var d1=new Date(trantimestr);
		var a=d1.valueOf();//毫秒为单位
		a=a+(seconds*1000);  //由秒转至毫秒可加可減
		a=new Date(a);
		var str=a.getFullYear()+'/'+(a.getMonth()+1)+'/'+a.getDate()+' '
		str+=a.getHours()+':'+a.getMinutes()+':'+a.getSeconds();
		return str;
}