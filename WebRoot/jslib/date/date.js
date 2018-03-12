var gMonths=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
var WeekDay=new Array("日","一","二","三","四","五","六");
var strToday="当前时间";
var strFinishInput="完成";
var clearDate = "清除"
var strYear="年";
var strMonth="月";
var strDay="日";

var splitChar="-";
var startYear=2000;
var endYear=2050;
var dayTdHeight=12;
var dayTdTextSize=12;
var gcNotCurMonth="#999999";
var gcRestDay="#FF0000";
var gcWorkDay="#000000";
var gcMouseOver="#a4cbfa";
var gcMouseOut="#ffffff";
var gcToday="#000000";
var gcTodayMouseOver="#a4cbfa";
var gcTodayMouseOut="#c3daf9";
var gdCtrl=new Object();
var goSelectTag=new Array();
var gdCurDate=new Date();
var giYear=gdCurDate.getFullYear();
var giMonth=gdCurDate.getMonth()+1;
var giDay=gdCurDate.getDate();


//时分秒的扩展
var currentDataString ="2011:00:00";    //当前选择的日期
var currentTimeString ="00:00:00";    //当前选择的时分秒


var withDataTime = false;    //是否包含时分秒
var strHour="时";
var strMinute="分";
var strSecond="秒";
var conneceChar =" ";    //日期和时间之间的分隔符
var dataTimeSplitChar=":";
var giHour = gdCurDate.getHours();
var giMinute = gdCurDate.getMinutes();
var giSecond = gdCurDate.getSeconds();



//  //当前时间
//  currentTimeString=giHour+dataTimeSplitChar+giMinute + dataTimeSplitChar + giSecond;


/**
 * 获取对象集合
 * @return
 */
function getObj_() {
	var elements = new Array();
	for (var i = 0; i < arguments.length; i++) {
		var element = arguments[i];
		if (typeof(arguments[i]) == 'string') {
			element = document.getElementById(arguments[i]);
		}
		if (arguments.length == 1) {
			return element;
		}
		elements.Push(element);
	}
	return elements;
}
Array.prototype.Push = function() {
	var startLength = this.length;
	for (var i = 0; i < arguments.length; i++) {
		this[startLength + i] = arguments[i];
	}
	return this.length;
}
String.prototype.HexToDec = function() {
	return parseInt(this, 16);
}
String.prototype.cleanBlank = function() {
	return this.isEmpty() ? "" : this.replace(/\s/g, "");
}
function checkColor() {
	var color_tmp = (arguments[0] + "").replace(/\s/g, "").toUpperCase();
	var model_tmp1 = arguments[1].toUpperCase();
	var model_tmp2 = "rgb(" + arguments[1].substring(1, 3).HexToDec() + ","
			+ arguments[1].substring(1, 3).HexToDec() + ","
			+ arguments[1].substring(5).HexToDec() + ")";
	model_tmp2 = model_tmp2.toUpperCase();
	if (color_tmp == model_tmp1 || color_tmp == model_tmp2) {
		return true;
	}
	return false;
}
function getFirstObj_() {
	return getObj_(arguments[0]).value;
}

/**
 * 日期控件的触发函数
 * @param evt 事件
 * @param popCtrl 日期控件的绑定到哪个对象旁边
 * @param dateCtrl 日期控件输出的对象
 * @return
 */
function fPopCalendar(evt, popCtrl, dateCtrl,isWithTime) {
	if(!isWithTime){
		withDataTime=false
		}else{
		if(isWithTime==true){
			withDataTime=true;
		}else{
			withDataTime=false;
		}
		}
	evt.cancelBubble = true;
	gdCtrl = dateCtrl;
	fSetYearMon(giYear, giMonth);
	var point = fGetXY(popCtrl);
	with (getObj_("calendardiv").style) {
		left = point.x + "px";
		top = (point.y + popCtrl.offsetHeight + 1) + "px";
		visibility = 'visible';
		zindex = '99';
		position = 'absolute';
	}
	getObj_("calendardiv").focus();
	
	fShowAndHideDataTimeCtrl();
}

/**
 * 设置时间日期
 * @param iYear 年
 * @param iMonth 月
 * @param iDay 日
 * @return
 */
function fSetDate(iYear, iMonth, iDay) {

	var iMonthNew = new String(iMonth);
	var iDayNew = new String(iDay);
	if (iMonthNew.length < 2) {
		iMonthNew = "0" + iMonthNew;
	}
	if (iDayNew.length < 2) {
		iDayNew = "0" + iDayNew;
	}
//	gdCtrl.value = iYear + splitChar + iMonthNew + splitChar + iDayNew;
//	fHideCalendar();
	currentDataString = iYear + splitChar + iMonthNew + splitChar + iDayNew;
	updataDate();
}

/**
 * 更新控件输出
 * @return
 */
function fUpdateCtrl(){
	if(currentDataString==""){
		currentDataString=""+giYear+splitChar+giMonth+splitChar+giDay;
	}
	if(currentTimeString==""){
		currentTimeString=""+giHour+dataTimeSplitChar+giMinute+dataTimeSplitChar+giSecond;
	}
	
	if(withDataTime&&(withDataTime==true)){
		gdCtrl.value = currentDataString+conneceChar+currentTimeString;
		gdCtrl.focus();
	}else{
		gdCtrl.value = currentDataString;
		gdCtrl.focus();
	}
}



/**
 * 清除输出框控件并隐藏
 * @return
 */
function fClearDate() {
	gdCtrl.value = "";
	fHideCalendar();
}

/**
 * 隐藏日期控件
 * @return
 */
function fHideCalendar() {
	getObj_("calendardiv").style.visibility = "hidden";
	for (var i = 0; i < goSelectTag.length; i++) {
		goSelectTag[i].style.visibility = "visible";
	}
	goSelectTag.length = 0;
}

/**
 * 获取当前选择了的日期并绑定到输出框
 * @return
 */
function fSetSelected() {
	var iOffset = 0;
	var iYear = parseInt(getObj_("tbSelYear").value);
	var iMonth = parseInt(getObj_("tbSelMonth").value);
	var aCell = getObj_("cellText" + arguments[0]);
	aCell.bgColor = gcMouseOut;
	with (aCell) {
		var iDay = parseInt(innerHTML);
		if (checkColor(style.color, gcNotCurMonth)) {
			iOffset = (innerHTML > 10) ? -1 : 1;
		}
		iMonth += iOffset;
		if (iMonth < 1) {
			iYear--;
			iMonth = 12;
		} else if (iMonth > 12) {
			iYear++;
			iMonth = 1;
		}
	}
	fSetDate(iYear, iMonth, iDay);
	
	fUpdateCtrl();
}



/**
 * 位置
 * @param iX
 * @param iY
 * @return
 */
function Point(iX, iY) {
	this.x = iX;
	this.y = iY;
}

/**
 * 按指定年份和月份生成月份的二位数组
 * @param iYear
 * @param iMonth
 * @return
 */
function fBuildCal(iYear, iMonth) {
	var aMonth = new Array();
	for (var i = 1; i < 7; i++) {
		aMonth[i] = new Array(i);
	}
	var dCalDate = new Date(iYear, iMonth - 1, 1);
	var iDayOfFirst = dCalDate.getDay();
	var iDaysInMonth = new Date(iYear, iMonth, 0).getDate();
	var iOffsetLast = new Date(iYear, iMonth - 1, 0).getDate() - iDayOfFirst
			+ 1;
	var iDate = 1;
	var iNext = 1;
	for (var d = 0; d < 7; d++) {
		aMonth[1][d] = (d < iDayOfFirst) ? (iOffsetLast + d) * (-1) : iDate++;
	}
	for (var w = 2; w < 7; w++) {
		for (var d = 0; d < 7; d++) {
			aMonth[w][d] = (iDate <= iDaysInMonth) ? iDate++ : (iNext++) * (-1);
		}
	}
	return aMonth;
}

/**
 * 按指定年份和月份生成月份的显示界面
 * @param iYear
 * @param iMonth
 * @param iCellHeight
 * @param iDateTextSize
 * @return
 */
function fDrawCal(iYear, iMonth, iCellHeight, iDateTextSize) {
	var colorTD = " bgcolor='" + gcMouseOut + "' bordercolor='" + gcMouseOut
			+ "'";
	var styleTD = " valign='middle' align='center' style='height:"
			+ iCellHeight + "px;font-weight:none;font-size:" + iDateTextSize
			+ "px;";
	var dateCal = "";
	dateCal += "<tr>";
	for (var i = 0; i < 7; i++) {
		dateCal += "<td" + colorTD + styleTD + "color:#233d6d'>" + WeekDay[i]
				+ "</td>";
	}
	dateCal += "</tr>";
	for (var w = 1; w < 7; w++) {
		dateCal += "<tr>";
		for (var d = 0; d < 7; d++) {
			var tmpid = w + "" + d;
			dateCal += "<td" + styleTD
					+ "cursor:pointer;' onclick='fSetSelected(" + tmpid + ")'>";
			dateCal += "<span id='cellText" + tmpid + "'></span>";
			dateCal += "</td>";
		}
		dateCal += "</tr>";
	}
	return dateCal;
}

/**
 * 更新月份显示的界面
 * @param iYear
 * @param iMonth
 * @return
 */
function fUpdateCal(iYear, iMonth) {
	var myMonth = fBuildCal(iYear, iMonth);
	var i = 0;
	for (var w = 1; w < 7; w++) {
		for (var d = 0; d < 7; d++) {
			with (getObj_("cellText" + w + "" + d)) {
				parentNode.bgColor = gcMouseOut;
				parentNode.borderColor = gcMouseOut;
				parentNode.onmouseover = function() {
					this.bgColor = gcMouseOver;
				};
				parentNode.onmouseout = function() {
					this.bgColor = gcMouseOut;
				};
				if (myMonth[w][d] < 0) {
					style.color = gcNotCurMonth;
					innerHTML = Math.abs(myMonth[w][d]);
				} else {
					style.color = ((d == 0) || (d == 6))
							? gcRestDay
							: gcWorkDay;
					innerHTML = myMonth[w][d];
					if (iYear == giYear && iMonth == giMonth
							&& myMonth[w][d] == giDay) {
						style.color = gcToday;
						parentNode.bgColor = gcTodayMouseOut;
						parentNode.onmouseover = function() {
							this.bgColor = gcTodayMouseOver;
						};
						parentNode.onmouseout = function() {
							this.bgColor = gcTodayMouseOut;
						};
					}
				}
			}
		}
	}
}

/**
 * 根据年份和月份更新月份显示界面
 * @param iYear
 * @param iMon
 * @return
 */
function fSetYearMon(iYear, iMon) {
	getObj_("tbSelMonth").options[iMon - 1].selected = true;
	for (var i = 0; i < getObj_("tbSelYear").length; i++) {
		if (getObj_("tbSelYear").options[i].value == iYear) {
			getObj_("tbSelYear").options[i].selected = true;
		}
	}
	fUpdateCal(iYear, iMon);
}

/**
 * 设置上一个月
 * @return
 */
function fPrevMonth() {
	var iMon = getObj_("tbSelMonth").value;
	var iYear = getObj_("tbSelYear").value;
	if (--iMon < 1) {
		iMon = 12;
		iYear--;
	}
	fSetYearMon(iYear, iMon);
}

/**
 * 设置下一个月
 * @return
 */
function fNextMonth() {
	var iMon = getObj_("tbSelMonth").value;
	var iYear = getObj_("tbSelYear").value;
	if (++iMon > 12) {
		iMon = 1;
		iYear++;
	}
	fSetYearMon(iYear, iMon);
}

/**
 * 获取控件所在的坐标
 * @param aTag
 * @return
 */
function fGetXY(aTag) {
	var oTmp = aTag;
	var pt = new Point(0, 0);
		do {
			pt.x += oTmp.offsetLeft;
			pt.y += oTmp.offsetTop;
			oTmp = oTmp.offsetParent;
			if(oTmp == null || oTmp.tagName == null || oTmp.tagName.toUpperCase() ==null){
				return pt;
			}
		} while (oTmp.tagName.toUpperCase() != "BODY");
		return pt;
}

/**
 * 生成日期选择器的界面
 * @return {}
 */
function getDateDiv() {
	var noSelectForIE = "";
	var noSelectForFireFox = "";
	if (document.all) {
		noSelectForIE = "onselectstart='return false;'";
	} else {
		noSelectForFireFox = "-moz-user-select:none;";
	}
	var dateDiv = "";
	dateDiv += "<div id='calendardiv' onclick='event.cancelBubble=true' "
			+ noSelectForIE
			+ " style='"
			+ noSelectForFireFox
			+ "position:absolute;z-index:99;visibility:hidden;border:1px solid #ccc;padding:3px; background:#eee;'>";
	dateDiv += "<table border='0' width='192px' cellpadding='1' cellspacing='1' >";
	dateDiv += "<tr>";
	dateDiv += "<td style='padding:2px 0'><input type='button' id='PrevMonth' value='&lt;' style='height:19px;width:19px;font-weight:bolder;' onclick='fPrevMonth()'>";
	dateDiv += "</td><td><select id='tbSelYear' style='border:1px solid #999;height:19px;width:68px;' onchange='fUpdateCal(getFirstObj_(\"tbSelYear\"),getFirstObj_(\"tbSelMonth\"))'>";
	for (var i = startYear; i < endYear; i++) {
		dateDiv += "<option value='" + i + "'>" + i + strYear + "</option>";
	}
	dateDiv += "</select></td><td>";
	dateDiv += "<select id='tbSelMonth' style='border:1px solid #999;height:19px;width:68px;' onchange='fUpdateCal(getFirstObj_(\"tbSelYear\"),getFirstObj_(\"tbSelMonth\"))'>";
	for (var i = 0; i < 12; i++) {
		dateDiv += "<option value='" + (i + 1) + "'>" + gMonths[i]
				+ "</option>";
	}
	dateDiv += "</select></td><td>";
	dateDiv += "<input type='button' id='NextMonth' value='&gt;' style='height:19px;width:19px;font-weight:bolder;' onclick='fNextMonth()'>";
	dateDiv += "</td>";
	dateDiv += "</tr><tr>";
	dateDiv += "<td align='center' colspan='4'>";
	dateDiv += "<div style='background-color:#ffffff;border:1px solid #a3bad9'><table style='line-height:19px' width='100%' border='0' cellpadding='3' cellspacing='1'>";
	dateDiv += fDrawCal(giYear, giMonth, dayTdHeight, dayTdTextSize);
	dateDiv += "</table></div>";
	dateDiv += "</td>";
	dateDiv += "</tr>";
	dateDiv += makeTimeSelect();
	dateDiv += "<tr><td align='center' colspan='4' nowrap>";
	dateDiv += "<input type='button' id='ct' value='"
			+ strToday
			+ "'    style='height:22px' onclick='fSetDate(giYear,giMonth,giDay)'/>";
			
	dateDiv += "<input type='button' value='"
			+ strFinishInput
			+ "'    style='height:22px' onclick='fHideCalendar()'/>";
			
	dateDiv += "<input type='button' value='" + clearDate
			+ "' style='height:22px' onclick='fClearDate()'/>";
	dateDiv += "</tr></tr>";
	dateDiv += "</table></div>";
	return dateDiv;
}
with (document) {
	onclick = fHideCalendar;
	write(getDateDiv());
}



/**
 * 生成时分秒的html代码
 * @return {}
 */
function makeTimeSelect(){
var str="";
    str +="<tr id=\"timeTrHtml\" style=\"display:none;\" ><td align=\"center\" colspan=\"4\">";
	str += "<select id='tbSelHour' style='border:1px solid #999;height:19px;width:40px;' onchange='fSetTime(getFirstObj_(\"tbSelHour\"),getFirstObj_(\"tbSelMinute\"),getFirstObj_(\"tbSelSecond\"))'>";
	for (var i = 0; i < 24; i++) {
		str += "<option value='" + i + "'>"	+i+ "</option>";
	}
	str += "</select>";
	str +=strHour;
	
	str += "<select id='tbSelMinute' style='border:1px solid #999;height:19px;width:40px;' onchange='fSetTime(getFirstObj_(\"tbSelHour\"),getFirstObj_(\"tbSelMinute\"),getFirstObj_(\"tbSelSecond\"))'>";
	for (var i = 0; i < 60; i++) {
		str += "<option value='" + i + "'>"	+i+ "</option>";
	}
	str += "</select>";
	str +=strMinute;
	
	str += "<select id='tbSelSecond' style='border:1px solid #999;height:19px;width:40px;' onchange='fSetTime(getFirstObj_(\"tbSelHour\"),getFirstObj_(\"tbSelMinute\"),getFirstObj_(\"tbSelSecond\"))'>";
	for (var i = 0; i < 60; i++) {
		str += "<option value='" + i + "'>"	+i+ "</option>";
	}
	str += "</select>";
	str +=strSecond;
	str +="</td></tr>";
	return str;
}

function fShowAndHideDataTimeCtrl(){
	if(withDataTime==true){
		var tr = document.getElementById("timeTrHtml").style.display="";
	}else{
		var tr = document.getElementById("timeTrHtml").style.display="none";
	}
}

/**
 * 设置时分秒
 * @param {} ihh
 * @param {} imm
 * @param {} iss
 */
function fSetTime(ihh,imm,iss){
	var ihhNew = new String(ihh);
	var immNew = new String(imm);
	var issNew = new String(iss);
	if (ihhNew.length < 2) {
		ihhNew = "0" + ihhNew;
	}
	if (immNew.length < 2) {
		immNew = "0" + immNew;
	}
	if (issNew.length < 2) {
		issNew = "0" + issNew;
	}
	currentTimeString = ""+ihhNew+dataTimeSplitChar+immNew+dataTimeSplitChar+issNew;
	fUpdateCtrl()
//	return currentTimeString;
//	gdCtrl.value = dataString+conneceChar+currentTimeString;
}


/**
 * 根据当前设置更新日期和时间
 * @return
 */
function updataDate(){
	if(withDataTime == true){
		var currentDate=new Date();
		var curHour = currentDate.getHours();
		var curMinute = currentDate.getMinutes();
		var curSecond = currentDate.getSeconds();
		
		var timeStr=formatTime(curHour,curMinute,curSecond);
		//gdCtrl.value = currentDataString+conneceChar+currentTimeString;
		gdCtrl.value = currentDataString+conneceChar+timeStr;
	}else{
		gdCtrl.value = currentDataString;
	}
}

/**
*格式化时间的显示
*/
function formatTime(intHours,intMinutes,intSeconds){
	var hours="";
	var minutes="";
	var seconds="";
	
	if (intHours == 0) { 
    	hours = "12:"; 
    }else if (intHours < 12) { 
    	hours = intHours+":"; 
    } else if (intHours == 12) { 
    	hours = "12:"; 
    } else {
    	hours = intHours + ":"; 
    }
    if (intMinutes < 10) { 
    	minutes = "0"+intMinutes+":"; 
    } else { 
   		minutes = intMinutes+":"; 
    } 
    if (intSeconds < 10) { 
    	seconds = "0"+intSeconds+" "; 
    } else { 
    	seconds = intSeconds+" "; 
    }
    
    var timeString = hours + minutes + seconds;
    return timeString;
}
