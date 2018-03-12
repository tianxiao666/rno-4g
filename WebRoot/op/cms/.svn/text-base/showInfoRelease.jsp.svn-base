<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*;" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>查看公告发布信息</title>
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/cms.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
<script type="text/javascript" src="js/showInfoRelease.js"/>

</head>
<body>
<input type="hidden" id="tabType" value="<s:property value='tabType' />" />
	<%--主体开始--%>
<div class="cms_top">信息列表</div>
	<div class="cms_infoList clearfix">
	<%--切换卡开始--%>
	<div id="infoList_tab" class="infoList_tab tab_menu">
			<ul>
				<li id="waitAudit" class="selected">待审核(<em>${requestMap.tabTotal.waitAudit }</em>)</li>
				<li id="latestRelease">最近发布(<em>${requestMap.tabTotal.latestRelease }</em>)</li>
				<li id="draftByMe">由我拟稿(<em>${requestMap.tabTotal.draftByMe }</em>)</li>
				<li id="allInfo">全部信息(<em>${requestMap.tabTotal.allInfo }</em>)</li>
				<li id="overReleaseTime">最近已过期(<em>${requestMap.tabTotal.overReleaseTime }</em>)</li>
			</ul>
			<div class="infoList_button clearfix">
			    <span style="display:none;"><input type="button" class="input_button" value="通过发布" /></span>
				<span style="display:none;"><input type="button" class="input_button" value="驳回发布" /></span>
				<span><input type="button" class="input_button" value="复制" onclick="copyInfoItem();"/></span>
				<span><input id="btnDelete" type="button" class="input_button" value="删除" /></span>
			</div>
		</div>
		<div class="infoList_content">
			<div class="infoList_tab_0 tab_content"  id="infoList_Div">
				<table class="main_table">
					<tr>
						<th style="width:45px;"><input id="ckall" type="checkbox" class=""/></th>
						<th>编号</th>
						<th>类型</th>
						<th>标题</th>
						<th>拟稿人</th>
						<th>重要级别</th>
						<th>紧急程度</th>
						<th>拟稿时间</th>
					</tr>
					<s:iterator id="cms" value="cmsReleaseList" status="st">
                   		<tr class="tab_tr0">
                   			<td><input type="checkbox" class="input_checkbox" value="<s:property value='#cms.infoId'/>" /><input type="hidden" value="<s:property value='#cms.infoId' />" /></td>
							<s:if test="#cms.infoId == null">
								<td><s:property value='#cms.id'/></td>
							</s:if>
							<s:else>
								<td><s:property value='#cms.infoId'/></td>
							</s:else>
							<td><s:property value='#cms.category'/></td>
							<td class="wb">
								<a title="
								<s:if test="#cms.title != null && #cms.title != ''">
								<s:property value='#cms.title'/>
								</s:if>
								<s:else>
								无标题
								</s:else>
								" onclick="loadApproveInfoReleasePage(<s:property value='#cms.id'/>,'<s:property value='#cms.infoType'/>','<s:property value='#cms.status'/>');" href="#">
								<s:if test="#cms.title != null && #cms.title != ''">
								<s:property value='#cms.title'/></a>
								</s:if>
								<s:else>
								无标题</a>
								</s:else>
							</td>
							<td><s:property value='#cms.name'/></td>
							<td class="tispTd"><span class='tips'>
							<s:if test="#cms.importancelevel == '普通'">
								<span class="tips_c3"></span>
							</s:if>
							<s:elseif test="#cms.importancelevel == '较高'">
								<span class="tips_c2"></span>
							</s:elseif>
							<s:elseif test="#cms.importancelevel == '最高'">
								<span class="tips_c1"></span>
							</s:elseif>
							<s:elseif test="#cms.importancelevel == '最高'">
								<span class="tips_c1"></span>
							</s:elseif>
							<s:elseif test="#cms.importancelevel == '' || #cms.importancelevel == null">
								<span></span>
							</s:elseif>
							<s:else>
								<span class="tips_c4"></span>
							</s:else>
							<s:property value='#cms.importancelevel'/></span></td>
							<td class="tispTd"><span class='tips'>
							<s:if test="#cms.timeSensibility == '无所谓'">
								<span class="tips_c4"></span>
							</s:if>
							<s:elseif test="#cms.timeSensibility == '普通'">
								<span class="tips_c3"></span>
							</s:elseif>
							<s:elseif test="#cms.timeSensibility == '较急'">
								<span class="tips_c2"></span>
							</s:elseif>
							<s:elseif test="#cms.timeSensibility == '紧急'">
								<span class="tips_c1"></span>
							</s:elseif>
							<s:else>
								<span ></span>
							</s:else>
							<s:property value='#cms.timeSensibility'/></span></td>
							<td><s:date name="#cms.drafttime" format="yyyy-MM-dd HH:mm:ss" /></td>
                         </tr>
                    </s:iterator>
					<%--  <tr>
						<td><input type="checkbox" class="input_checkbox" /></td>
						<td>190</td>
						<td>公告</td>
						<td>IOSM在广东、广西全面推广</td>
						<td>熊斌</td>
						<td><span class="tips_yellow"></span>较高</td>
						<td>[2012]005号</td>
						<td>待审核</td>
						<td><span class="tips_red"></span>紧急</td>
						<td></td>
						<td>公司发文公告</td>	
					</tr>
					<tr>
						<td><input type="checkbox" class="input_checkbox" /></td>
						<td>190</td>
						<td>公告</td>
						<td>IOSM在广东、广西全面推广</td>
						<td>熊斌</td>
						<td><span class="tips_yellow"></span>较高</td>
						<td>[2012]005号</td>
						<td>待审核</td>
						<td><span class="tips_red"></span>紧急</td>
						<td></td>
						<td>公司发文公告</td>	
					</tr>--%>
				</table>
				<div class="main_table_foot clearfix" style="display:none;">
					<p class="main_table_page">
						每页&nbsp;<select onchange="submitPage('changeRecPerPage')" id="_recPerPage" name="_recPerPage" style="width: 50px;">
										<option value="5">5</option>
										<option value="10">10</option>
										<option selected="selected" value="15">15</option>
										<option value="20">20</option>
										<option value="25">25</option>
										<option value="30">30</option>
										<option value="40">40</option>
										<option value="50">50</option>
										<option value="100">100</option>
										<option value="200">200</option>
									</select>&nbsp;条记录 | 第&nbsp;<input class="input_text" type="text" value="1"  style="width:32px;" />&nbsp;页 
						<input class="input_button" type="button" value="go" />&nbsp;
						<a href="#" onclick="showListViewByPage('first')">首页</a>&nbsp;
						<a href="#" onclick="showListViewByPage('back')">上一页</a>&nbsp;
						<a href="#" onclick="showListViewByPage('next')">下一页</a>&nbsp;
						<a href="#" onclick="showListViewByPage('last')">尾页</a>&nbsp;
					</p>
				</div>
			</div>
			<div class="tab_content" style="display:none;">
			</div>
			<div class="tab_content" style="display:none;">
			</div>
			<div class="tab_content" style="display:none;">
			</div>
			<div class="tab_content" style="display:none;">
			</div>
			<div id="span_paging">
			</div>
		</div>
	<%--切换卡结束--%>
	
<%--主体结束--%>
</body>
</html>