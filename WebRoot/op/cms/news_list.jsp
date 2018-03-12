<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新闻列表</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/cms.css"/>
<link type="text/css" rel="stylesheet" href="css/news.css" />
<link type="text/css" rel="stylesheet" href="css/news_list.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="js/news_list.js"></script>
</head>

<body>
<br><%-- 总数 --%>
<input type="hidden" value="${totalCount}" name="totalCount" id="totalCount" />
<input type="hidden" value="${pageSize}" name="pageSize" id="pageSize" />
<input type="hidden" value="${pageIndex}" name="pageIndex" id="pageIndex" />
<input type="hidden" value="${totalPageCount}" name="totalPageCount" id="totalPageCount" />

    <div class="news_list_container" style="height: 150px; overflow: hidden;">
     <s:if test="announcements != null && announcements.size > 0">
  		<div id="news_list_container">
	    	<ul class="news_list_ul">
		    	<s:iterator value="announcements" var="announcement">
		        	<li onmousemove="stop_news_list_container();" onmouseout="start_news_list_container();" >
		            	<a target="_blank" href="getAnnouncementDetailAction?infoId=<s:property value='#announcement.infoId' />"><s:property value="#announcement.title" /></a>             
		            	<%-- <div class="news_list_right">
		            		<em class="news_name"><s:property value="#announcement.name" /></em>
		            		<em class="news_time"><s:date name="#announcement.lastModifiedTime" format="yyyy-MM-dd HH:mm"/></em>
		            	</div> --%>
		            </li>
		         </s:iterator>
	         </ul>
         </div>
        </s:if>
       <s:else>
       		暂无公告
       </s:else>
    </div>
    
</body>
</html>