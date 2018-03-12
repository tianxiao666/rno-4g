<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
   
<% 
String roller = (String)request.getAttribute("roller");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公司重要公告</title>
<link rel="stylesheet" type="text/css" href="home/showcase/news_roller/javascript/roller.css" />
<script type="text/javascript" src="home/resource/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="home/showcase/news_roller/javascript/jquery.roller.js"></script>
<style type="text/css">
<%--
.word {
font-family: "宋体";/*字体*/
color: #000000;/*字体颜色*/
text-decoration: none;
border-top-width: 1px;/*这几个1PX为虚线粗线,单产像素*/
border-right-width: 1px;
border-bottom-width: 1px;
border-left-width: 1px;
border-top-style: none;
border-right-style: none;
border-bottom-style: dashed;
border-left-style: none;
border-top-color: #b3dcfd;/*这些值为虚线颜色*/
border-right-color: #b3dcfd;
border-bottom-color: #b3dcfd;
border-left-color: #b3dcfd;
cursor: pointer;
}

.ellipsis_row{
width:450px;
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
}
--%>
</style>


<script type="text/javascript">
$(function() {
	<%=roller%>
});


function windowsResize(){
	window.location.reload();
}


$(window).resize(function () {
	if (window.timerLayout) clearTimeout(window.timerLayout);
		window.timerLayout = null;
	if ($.browser.msie) {
			window.timerLayout = setTimeout(windowsResize, 100);
	}else{
			window.timerLayout = setTimeout(windowsResize, 100);
	}
});




</script>




</head>
<body style="margin:0px auto;padding:0px 0 0 0px;">
<div id="container" style="margin:0 0 0 10px;font-size:12px;">
</div>
</body>
</html>