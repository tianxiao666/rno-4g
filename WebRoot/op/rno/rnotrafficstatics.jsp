<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<title>话务专题分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp" %>
<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css" type="text/css" />
<script type="text/javascript" src="js/trafficstatics.js"></script>
<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<script type="text/javascript" src="js/trafficstaticsView.js"></script>
<script type="text/javascript" language="javascript">
		//百度地图和谷歌地图切换，默认是百度地图
	var glomapid='<%=session.getAttribute("mapId") %>';
	
</script>
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	background: #EEEEEE;
}

#map_canvas {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}
;
</style>

</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在进行 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">话务专题分析</div> --%>
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div class="map_hd" style="padding-bottom: 0px">
					<div class="head_box clearfix" style="padding-bottom: 0px">
						<div class="dialog2 draggable ui-draggable">
							<div style="padding: 5px">

								<form id="conditionForm" method="post">
									 省：<select
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
									</select> 区：<select name="areaId" class="required" id="queryCellAreaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select>
									<input  type="button" id="trigger" name="trigger" value=""/>
									<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
									<input type="hidden" id="hiddenLng"
										value="<s:property value='centerPoint.lng' />" /> <input
										type="hidden" id="hiddenLat"
										value="<s:property value='centerPoint.lat' />" /> <input
										type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="100" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> 
										<input type="hidden" name="page.forcedStartIndex" id="hiddenForcedStartIndex" />
										<input type="hidden"
										id="hiddenTotalPageCnt" /> <input type="hidden"
										id="hiddenTotalCnt" />
										
								</form>
								<div id="hiddenAreaLngLatDiv" style="display:none">
								<s:iterator value="countryAreas" id="onearea">
												<input type="hidden" id="areaid_<s:property value='#onearea.area_id' />" value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
									</s:iterator>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

			<%-- 地图--%>
			<div style="padding-top: 0px">
				<div class="map_bd">
					<div id="map_canvas"></div>
					<div class="resource_list_icon" style="right:380px">
						<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
						<div class="shad_v"></div>
					</div>			
					<div class="resource_list300_box" style="height:100%;">
						<div class="resource_list300">
		                	<div id="div_tab" class="div_tab divtab_menu">
		                    	 <ul>
		                         	<li class="selected">话务统计</li>
		                         </ul>
		                    </div>					
						</div>
					<div class="divtab_content" >
		                    	<div id="div_tab_0">
		                        	
		                            <%--查询结果  --%>
		                            <%-- 标题--%>
		                            <div class="div_title_24px_blue">
		                            	<span class="sp_title"> 小区性能指标</span>                            
		                            </div>
		                            <div class="div_transparentstandard_nopaddming" id="analysisListDiv">
		                            	<table class="tb-transparent-standard" style="width:100%" id="analysisListTable">
		                                	                                                                                                                                    
										</table>
										<div id="analysisBtnDiv" style="display:none"><input type="button" value="确定" id="confirmSelectionAnalysisBtn" /></div>
		                            </div>
		                            <%-- --%>
		                            <div class ="div-m-split-10px">
		                            
		                            </div>
		                        	<%--分析应用 --%>
		                            <div class="div_transparent_standard_border">
		                            	<div class="div_title_24px_grey sp_title_left">
		                                	<span> 分析应用</span>
		                                </div>
		                            	
										<div id="analysisDiv">
										<input type="button" id="radioresourceutilizationbtn" onclick="javascript:commonstatics('radioresourcerate','staticsResourceUtilizationRateForAjaxAction');" value='无线资源利用率%'/>
										<input type="button" id="accsucratebtn" onclick="javascript:commonstatics('accsucrate','staticsResourceUtilizationRateForAjaxAction')" value='接通率%'/>
										<input type="button" id="dropratebtn" onclick="javascript:commonstatics('droprate','staticsResourceUtilizationRateForAjaxAction')" value='掉话率%'/>
										<input type="button" id="dropnumbtn" onclick="javascript:commonstatics('dropnum','staticsResourceUtilizationRateForAjaxAction')" value='掉话数'/>
										<input type="button" id="handoversucratebtn" onclick="javascript:commonstatics('handoversucrate','staticsResourceUtilizationRateForAjaxAction')" value='切换成功率'/>
										<br/><p></hr>
										<input type="button" id="veryidlecellbtn" onclick="javascript:commonstatics('veryidlecell','staticsSpecialCellForAjaxAction')" value='超闲小区'/>
										<input type="button" id="overloadcellbtn" onclick="javascript:commonstatics('overloadcell','staticsSpecialCellForAjaxAction')" value='超忙小区'/>
										<input type="button" id="highuseradiocellbtn" onclick="javascript:commonstatics('highuseradiocell','staticsSpecialCellForAjaxAction')" value='高无线利用率小区'/>
										<input type="button" id="highcongindatacellbtn" onclick="javascript:commonstatics('highcongindatacell','staticsSpecialCellForAjaxAction')" value='数据高拥塞率小区'/>
										<input type="button" id="badlyicmcellbtn" onclick="javascript:commonstatics('badlyicmcell','staticsSpecialCellForAjaxAction')" value='高干扰小区'/>
										</div>
		                            </div>
		                        </div>
		                    </div>                
					</div>
					<%-- 地图高宽--%>
					<div class="htl_map_move">
						<div>
							<iframe frameborder="none" src=""
								style="border: medium none; width: 1600px; height: 650px;">
							</iframe>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	
	 <%--分析弹出窗 --%>
    <div id="analyze_Dialog" class="dialog2 draggable" style="display:none; " >
                            <div class="dialog_header">
                                <div class="dialog_title">话备性能专题分析</div>
                                <div class="dialog_tool">
        	                        <div class="dialog_tool_close dialog_closeBtn" onclick="$('#analyze_Dialog').hide();$('.colordialog2').hide();"></div>
    	                        </div>
							</div>
                            <div class="dialog_content" style="width:300px; background:#f9f9f9">
                            	<table class="tb-transparent-standard" style="width:300px"  id="trafficTable">
                               		                                                                                                                                                                                  
                                </table>
                                
                            </div>                       
        	</div>
             <%--分析修改弹出窗 --%>
            <div >
            	<div class="black"></div>
            	<div id="analyzeedit_Dialog" class="dialog2 draggable" style="display:none; width:410px;" > 
					<div class="dialog_header">
                    	<div class="dialog_title">话备性能专题分析修改</div>
                        <div class="dialog_tool">
        	            	<div class="dialog_tool_close dialog_closeBtn2" onclick="$('#analyzeedit_Dialog').hide();$('.black').hide();$('.colordialog2').hide();"></div>
    	                </div>
					</div>
                            <div class="dialog_content" style="width:410px; background:#f9f9f9">
                            	<form action="updateOrAddrnoTrafficRendererAction" id="rendererForm" method="post">
	                            	<table class="tb-transparent-standard" style="width:350px "  id="analyzeedit_trafficTable">
	                               		                                                                                                                                                                                  
	                                </table>
                                </form>
                            </div>                                      
            	</div>
            </div>
            <div id="divColorDiv">
            </div>
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
