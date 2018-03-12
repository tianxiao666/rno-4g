<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>errorpage</title>
<link rel="stylesheet" type="text/css" href="css/base.css" />
<link rel="stylesheet" type="text/css" href="css/public.css" />
<link rel="stylesheet" type="text/css" href="css/errorpage.css" />
<script>
/*show,hide tagDiv*/
function infoShow(me,tagDiv){
	var tagButton = me;
	var tagDiv = document.getElementById(tagDiv);
	if(tagDiv.style.display == "block"){
		tagDiv.style.display = "none";
		tagButton.setAttribute("class","");
	}else{
		tagDiv.style.display = "block";
		tagButton.setAttribute("class","showed");
	}
}
</script>
</head>

<body>

	<div class="error_container">
        <div class="error_image">
            <h4>对不起，您的访问出错了！</h4>
        </div>
        <div class="error_info">
        	<span id="error_info_title" onclick="infoShow(this,'error_info_main')">
            	进一步查看异常
            </span>
            <div id="error_info_main">
            	<s:property value="message" escape="false" />
            </div>
        </div>
    </div>
</body>
</html>

