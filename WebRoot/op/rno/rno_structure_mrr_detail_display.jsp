<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>MRR结果查看</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript"
	src="js/rno_structure_mrr_detail_display.js"></script>
<style type="text/css">
.greystyle-standard-noborder tr td {
	padding: 0
}

.dict_left {
	width: 130px;
	float: left;
	left: 0;
	z-index: 1;
	border: 1px;
	margin: 5px
}

.dict_right {
	width: 100%;
	left: 200px;
	float: left;
	border: 1px;
	margin: 5px
}

/* 覆盖分页面板的对齐样式 */
.paging_div{
text-align:left;
}
</style>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab1", "li", "onclick");//项目服务范围类别切换

	})
</script>

<body>
<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<div style="padding: 10px">
				<div id="frame" class="loadframe"
					style="border: 1px solid #ddd;width:100%;float:left">
					<div id="div_tab1" class="div_tab divtab_menu">
						<ul style="width:100%">
							<li class="selected" style="width:15%">MRR记录查询</li>
							<li id="mrrDetailLi" style="width:14%">MRR记录详情</li>
						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab1_0" >
							<form id="mrrDescDataForm" method="post">
								<input type="hidden" id="hiddenPageSize" name="page.pageSize"
									value="25" /> <input type="hidden" id="hiddenCurrentPage"
									name="page.currentPage" value="1" /> <input type="hidden"
									id="hiddenTotalPageCnt" /> <input type="hidden"
									id="hiddenTotalCnt" />
								<div>
									<table class="main-table1 half-width"
										style="padding-top: 10px;">
										<tr>
											<td class="menuTd" style="text-align: center"><span
												style="padding-top: 0px">区域</span></td>
											<td class="menuTd" style="text-align: center ">BSC名称</td>
											<td class="menuTd" style="text-align: center ">测试文件名称</td>
											<td class="menuTd" style="text-align: center">测试时间</td>
										</tr>

										<tr>
											<td style="text-align: left">省：<select name="provinceId"
												class="required" id="provinceId2">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />市：<select name="cond['cityId']" class="required" id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />区：<select name="cond['AREAID']" class="required"
												id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
											<td style="text-align: left"><input type="text"
												name="cond['bsc']" / style="width:100%"></td>
											<td style="text-align: left"><input type="text"
												name="cond['fileNAME']" / style="width:100%"></td>
											<td style="text-align: left">从 <s:set name="begtime"
													value="new java.util.Date()" /> <input
												name="cond['begTime']"
												value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" />
												<br />至 <s:set name="endtime" value="new java.util.Date()" />
												<input name="cond['endTime']"
												value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" />

											</td>

										</tr>
										<tr>

											<td style=" text-align: right" colspan="4"><input
												type="button" onclick="" value="查  询" style="width: 90px;"
												id="searchMrrDescBtn" name="search" /></td>
										</tr>
									</table>
								</div>
							</form>
							<%--查询结果  --%>
							<div style="padding-top: 10px">
								<table width="100%">
									<tr>
										<td style="width: 20%">
											<p>
												<font style="font-weight: bold">mrr数据列表</font>
											</p>
										</td>
									</tr>
									<tr>
										<td style="width: 20%">
											<p>
											<%--
												<input type="button" onclick="analysisAreaStructure()"
													value="分析 选择的ncs计算得到的区域结构信息" />
													 --%>
											    
											    <%-- >input type="button" onclick="gatherNcsInfo()"
													value="汇总分析ncs信息" / --%>
											</p>
										</td>
									</tr>

								</table>

							</div>
							<div style="padding-top: 10px;overflow: auto;">
								<table id="queryMrrResultTab" class="greystyle-standard"
									width="100%">

								</table>
							</div>
							<div class="paging_div" id='mrrResultPageDiv'
								style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
									条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first',doQueryMrrDesc,'mrrDescDataForm')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back',doQueryMrrDesc,'mrrDescDataForm')"></a>
								第 <input type="text" id="showCurrentPage"
									class="paging_input_text" value="1" /> 页/<em
									id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
									title="GO"
									onclick="showListViewByPage('num',doQueryMrrDesc,'mrrDescDataForm')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next',doQueryMrrDesc,'mrrDescDataForm')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last',doQueryMrrDesc,'mrrDescDataForm')"></a>
							</div>

							<div id="areaStructDiv" style="display:none">
								<p>
									<font style="font-weight: bold">区结构相关信息</font>
								</p>
								<table id="queryAreaStructureResultTab"
									class="greystyle-standard" width="100%">

								</table>
							</div>
						</div>

						<%--详情查看 --%>
						<div id="div_tab1_1" style="display: none;">
							<div id="mrr_detail_tab" class="div_tab divtab_menu">
								<ul>
									<li id="mrrAdmDataLi">管理记录</li>
									<li>信号强度记录</li>
									<li>信号质量记录</li>
									<li>传输功率记录</li>
									<li>时间提前量记录</li>
									<li>路径损耗记录</li>
									<li>路径损耗差异记录</li>
									<li>测量结果记录</li>
									<li>帧消除速率记录</li>
								</ul>
							</div>
							<div style="overflow:auto; ">
								<div id="mrr_detail_tab_0" class="ncsDetailDivCls" data="mrrAdm">
									<%@ include
										file="rno_structure_mrr_adm_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_1" class="ncsDetailDivCls" data="strength" style="display:none">
									<%@ include
										file="rno_structure_mrr_strength_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_2" class="ncsDetailDivCls" data="quality" style="display:none">
									<%@ include
										file="rno_structure_mrr_quality_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_3" class="ncsDetailDivCls" data="power" style="display:none">
									<%@ include
										file="rno_structure_mrr_power_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_4" class="ncsDetailDivCls" data="ta" style="display:none">
									<%@ include
										file="rno_structure_mrr_ta_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_5" class="ncsDetailDivCls" data="pl" style="display:none">
									<%@ include
										file="rno_structure_mrr_pl_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_6" class="ncsDetailDivCls" data="pld" style="display:none">
									<%@ include
										file="rno_structure_mrr_pld_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_7" class="ncsDetailDivCls" data="meaResult" style="display:none">
									<%@ include
										file="rno_structure_mrr_mea_data.jsp"%>
								</div>
								<div id="mrr_detail_tab_8" class="ncsDetailDivCls" data="fer" style="display:none">
									<%@ include
										file="rno_structure_mrr_fer_data.jsp"%>
								</div>
						</div>
					</div>
				</div>



			</div>



		</div>


	</div>

	<div id="operInfo"
		style="display:none; top:40px;left:400px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>


</body>


</html>
