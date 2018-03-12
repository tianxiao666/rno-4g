<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date"%>
<%@ page language="java" import="java.text.DateFormat"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>创建工单-创建故障抢修工单</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="jslib/public.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
</head>

<body>
	<div id="header960">
    	<div class="header-top960"><h2>快速创建EOMS故障抢修工单</h2></div>
        <div class="header-main">
        	<span class="text-l">工单号：</span>
            <span>创建时间：</span>
            <span class="text-r">状态：</span>
       	</div>
    </div>
    <%--头部结束--%>				
    <div id="container960">
    	<%--tab1开始--%>
    	<div class="container-tab1">
        	 <form id="form" action="loadCreateUrgentRepairWorkOrderPageForQuickAction" method="post" enctype="multipart/form-data">
        	<fieldset id="fieldset-1">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>工单信息</span>
                </legend>
                <div class="container-main">
                  
                    <table class="main-table1">
                    	<tr>
                        	<td colspan="2">EOMS工单类型：
                            	<input type="radio" name="moudleId" value="000" checked="checked" />无线
                            	<input type="radio" name="moudleId" value="001" />动力
                            	<input type="radio" name="moudleId" value="002" />TD
                            	<input type="radio" name="moudleId" value="003" />WLAN
                            	<input type="radio" name="moudleId" value="004" />室分-移动本地生产支撑
                            	<input type="radio" name="moudleId" value="005" />室分-网优支撑
                            </td>
                        </tr>
                    	<tr>
                        	<td colspan="2">EOMS工单基本信息：<br />
                            	<textarea style="width:99%;height:300px;" name="content"></textarea>
                            </td>
                        </tr>
                    </table>
                   
                </div>
            </fieldset>
            <div class="container-bottom">
            	<input type="submit" value="下一步" style="width:60px;" name="save" />
            	<input type="button" value="取消" style="width:60px;" name="cancel" />
            </div>
             </form>
        </div>
        	<%--tab1结束，可重写--%>
        </form>
    </div>
</body>
</html>
