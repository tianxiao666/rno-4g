<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>log</title>
  </head>
  <script type="text/javascript" src="jslib/jquery/jquery-1.6.2.min.js" ></script>
  <script type="text/javascript">
		function moreLog(){
			var size = $("#size").val();
			var totalSize = $("#totalSize").val();
			var values={"size":size,"totalSize":totalSize};
			$.post("loadLogAction",values,function(data){
				var str = "";
				$.each(data.list,function(k,v){
					str += v +"</br>";
				});
				$("#showLog").html(str);
				$("#totalSize").val(data.totalSize);
			},"json");
		}
		
		function cleanLog(){
			$("#totalSize").val(0);
			$("#showLog").html("");
			$.post("cleanLogAction",function(data){
			},"json");
		}
  </script>
  <body>
  	<input type="text" id="size" value="500" />&nbsp;<input type="button" value="更多" onclick="moreLog();" />&nbsp;
  	<input type="button" value="清空" onclick="cleanLog();" />
  	<input type="hidden" id="totalSize" value="0" />
  	<div id="showLog">
    	
    </div>
  </body>
</html>
