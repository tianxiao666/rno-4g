<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新闻</title>
<link type="text/css" rel="stylesheet" href="../../css/base.css" />
<link type="text/css" rel="stylesheet" href="../../css/public.css" />
<link type="text/css" rel="stylesheet" href="css/news.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="js/news_detail.js" ></script>
</head>

<body>
	<div class="news_container">
    	<div class="news_title">
    		<s:if test="announcement.category == '安全生产'">
	        	<h1><s:property value="announcement.title" /></h1>
    		</s:if>
    		<s:else>
    			<h1 style="color: #000;"><s:property value="announcement.title" /></h1>
    		</s:else>
        	<div class="news_info">
            	<span>
                	<%-- <em class="info_m">撰稿人：</em><em><s:property value="announcement.author" /></em> --%>
                    <em class="info_m">发布人：</em><em><s:property value="announcement.releaser" /></em>
                    <em class="info_m">审核人：</em><em><s:property value="announcement.auditor" /></em>
                    <em class="info_m">签发时间：</em><em><s:property value="announcement.lastModifiedTime" /></em>
                </span>
                <span class="news_lev">
                	<em class="info_m">级别：</em><s:property value="announcement.importancelevel" />
                </span>
            </div>
        </div>
        <hr color="red" />
        <hr color="red" />
        <div class="news_main">
        		<h2 style="font-size: 20px; text-align: center; width: 100%;">${ announcement.label}</h2>
        		<input type="hidden" id="picName" name="picName" value="${announcement.pictures }"/>
				<input type="hidden" id="picUrl" name="picUrl" value="${announcement.picture_url }"/>
        		<input type="hidden" id="attaName" name="attaName" value="${announcement.attachments }"/>
				<input type="hidden" id="attaUrl" name="attaUrl" value="${announcement.url } "/>
        	<div  class="news_main_image" id="imgDiv">
        	</div>
                <%--  <div class="news_main_image_title"><s:property value="announcement.title" /></div>--%>
        	<textarea style="background:#f7f7f7; border:none; padding:10px 40px; width:100%; min-height:600px; font-family: '宋体'; font-size: 14px;" readonly="readonly"><s:property value="announcement.content" />
            </textarea>
            
            <div class="img_list" id="attaDiv" style="display: none;">
        	</div>
        </div>
    </div>
</body>
</html>