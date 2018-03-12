<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Mp3播放页面</title>
    <script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="jslib/jquery.jmp3.js"></script>
    <script type="text/javascript">
    	$(function(){
    		//音乐播放器
			$("#sounddl").jmp3({
				filepath: "music/",
				flashpath: "swf/",
				showfilename: "false",
				backcolor: "ffffff",
				forecolor: "ffffff",
				width: 1,
				showdownload: "true",
				repeat:"false",
				autoplay:"true"
			});
    	});
    </script>
  </head>
  
  <body>
  	<%-- 装载音乐播放器 --%>
  	<span id="sounddl" style="visibility:hidden;position:absolute;display:block;">message.mp3</span>
  </body>
</html>
