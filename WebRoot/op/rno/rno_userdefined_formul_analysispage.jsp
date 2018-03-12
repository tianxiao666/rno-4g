<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    
    <title>用户自定义话统公式</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<%@include file="commonheader.jsp"%>
	<link href="css/userdefinedanalyse.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css"
		type="text/css" />
	<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
	<script type="text/javascript" src="js/rno_userdefined_formul_analysispage.js"></script>
  
  </head>
  
  <body>
  <div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在进行 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<%-- 以上为加载状态 --%>
    <div class="userdefinedhead" id="userdefinedhead">
  <div align="center" style=" background-color:#99FF99">自定义指标分析项</div>
</div>
<div class="availableinfo" id="availableinfo">
  <table width="100%" id="stsinfotab">
  <div style="border:thin dashed  #CCCCCC; text-align:center">
   
      话统可用信息
    
    </div>
    <tr>
      <th width="98" rowspan="9" scope="col">话务统计表</th>
      <th width="140" scope="col">英文字段名</th>
      <th width="141" scope="col">中文字段名</th>
    </tr>
    <tr>
      <td align="center">TCH_INTACT_RATE</td>
      <td align="center"><a href="#" onclick="getIndex(this)">TCH完好率</a></td>
    </tr>
    <tr>
      <td align="center">DECLARE_CHANNEL_NUMBER</td>
      <td align="center"><a href="#" onclick="getIndex(this)">申报通道数</a></td>
    </tr>
    <tr>
      <td align="center">AVAILABLE_CHANNEL_NUMBER</td>
      <td align="center"><a href="#" onclick="getIndex(this)">可用频道数</a></td>
    </tr>
    <tr>
      <td align="center">RESOURCE_USE_RATE</td>
      <td align="center"><a href="#" onclick="getIndex(this)">资源利用率</a></td>
    </tr>
    <tr>
      <td align="center">ACCESS_OK_RATE</td>
      <td align="center"><a href="#" onclick="getIndex(this)">访问成功率</a></td>
    </tr>
    <tr>
      <td align="center">RADIO_DROP_RATE_NO_HV</td>
      <td align="center"><a href="#" onclick="getIndex(this)">无线丢包率</a></td>
    </tr>
    <tr>
      <td align="center">HANDOVER_SUC_RATE</td>
      <td align="center"><a href="#" onclick="getIndex(this)">切换成功率</a></td>
    </tr>
    <tr>
      <td align="center">PS_RADIO_USE_RATE</td>
      <td align="center"><a href="#" onclick="getIndex(this)">PS无线利用率</a></td>
    </tr>
  </table>
</div>
<div class="userdefinedcondition" id="userdefinedcondition">
  <form id="conditionForm" name="conditionForm" method="post">
    <%-- 从session中取出当前用户名及ID --%>
    <table width="99%" id="colorTable0" class="color0" >
      <tr style="line-height: 3">
        <td width="161" align="right">指标分析名称：</td>
        <td colspan="2"><label>
          <input type="text" name="rnoUserDefinedFormul.name" id="formulname" class="required" />
        </label></td>
        <td width="68">&nbsp;</td>
      </tr>
      <tr style="line-height: 3">
        <td align="right">自定义指标条件：</td>
        <td colspan="2"><label>
          <textarea name="rnoUserDefinedFormul.condition" id="formulcondition" cols="45" rows="5" style="resize:none" class="required"></textarea>
        </label></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td align="right">渲染颜色：</td>
        
        <td  width="15" id="colortd" style="border:2px solid #CCC; background-color: #6633FF;" onclick="farbtasticColor('colorpickerDiv0','colorpicker0','colortd');"></td>
        <td width="475"><input type="text" name="rnoUserDefinedFormul.style" id="style" readonly="readonly" style="background-color:#6633FF" value="#6633FF"/></td>
        <td width="68">&nbsp;</td>
      </tr>
      <tr style="line-height: 3">
        <td align="right">应用范围：</td>
        <td colspan="2"><span style="display:block; float:left">
          <input type="radio" name="rnoUserDefinedFormul.applyScope" id="perscope" checked="checked" value="perscope" />个人　　
          <input type="radio" name="rnoUserDefinedFormul.applyScope" id="areascope" value="areascope"  />区/县　　  
        <input type="radio" name="rnoUserDefinedFormul.applyScope" id="cityscope" value="cityscope"  />市
        </span><div id="selectarea" style="display:none; float:left; margin-left:16px;">省：<select
										name="provinceId" class="required" id="provinceId">
										<%-- option value="-1">请选择</option --%>
										<s:iterator value="provinceAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> 市：<select name="cityId" class="required" id="cityId">
										<s:iterator value="cityAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> 区：<select name="areaId" class="required" id="areaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select></div></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td colspan="2"><label>
          <input type="button" name="button" id="conditionLoadBtn" value="提　交" />
          <input type="reset" name="button2" id="button2" value="重　置" />
          <input type="button" name="test" id="test" value="测　试"  onclick="filterCondition();"/>
        </label></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td colspan="2">
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>
  </form>
</div>
        <div id="colorpickerDiv0" class="dialog2 colordialog2" style="width: 200px; top: 40px; right: 486px; height: 242px; z-index: 40; display: none;">
        <div class="dialog_header">
        <div class="dialog_title">颜色选择</div>
        <div class="dialog_tool">
        <div class="dialog_tool_close dialog_closeBtn2" onclick="$('#colorpickerDiv0').hide();"></div>
        </div>
        </div>
        <div class="dialog_content" style="width:200px; height:200px; background:#f9f9f9">
        <div id="colorpicker0" onmouseup="mouseupcolor()" style="position:absolute;">
        <div class="farbtastic">
        <div class="color" style="background-color: rgb(9, 255, 0);"></div>
        <div class="wheel"></div>
        <div class="overlay"></div>
        <div class="h-marker marker" style="left: 171px; top: 136px;"></div>
        <div class="sl-marker marker" style="left: 74px; top: 70px;"></div>
        </div>
        </div>
        </div>
        </div>
        <%-- 提示信息 --%>
    	<div id="operInfo" style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
            	<table height="100%" width="100%" style="text-align:center">
                	<tr>
                    	<td>
                        	<span id="operTip"></span>
                        </td>
                    </tr>
                </table>
             
            </div>
  </body>
</html>
