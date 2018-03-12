<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加新问题</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript">
$(function(){
	$("#cancelButton").click(function(){
		$("#questionType").val("请选择");
		$("#routineinspectionQuestion.description").html("");
	});
	
	//提交新增问题
	$("#addQuestion").validate({submitHandler: function(form) { 
		  form.submit();
	}});	
});
</script>
</head>

<body>
	<form id="addQuestion"  action="addRoutineinspectionQuestionAction" method="post" enctype="multipart/form-data">
	<input type="hidden" name="TOID" value="${questionInfo['TOID']}">
	<div class="container">
        <div class="content">
        	<div class="content_step">
            	<table class="thleft_table">
                    <tr>
                        <th colspan="4"><em class="edit_ico">添加新问题</em></th>
                    </tr>
                    <tr>
                        <td class="label_td">问题类型：</td>
                        <td>
                            <select validateIsEmpty="#questionType" name="routineinspectionQuestion.questionType" id="questionType">
                            	<option value="请选择">请选择</option>
                            	<option value="环境问题">环境问题</option>
                            	<option value="动力问题">动力问题</option>
                            	<option value="物业问题">物业问题</option>
                            	<option value="施工隐患">施工隐患</option>
                            	<option value="其它">其它</option>
                            </select><span class="red">*</span>
                        </td>
                        <td class="label_td">严重程度：</td>
                        <td>
                        	<input type="radio" name="routineinspectionQuestion.seriousLevel" value="严重" checked/>严重
                        	<input type="radio" name="routineinspectionQuestion.seriousLevel" value="一般"/>一般
                        	<input type="radio" name="routineinspectionQuestion.seriousLevel" value="次要"/>次要
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">问题描述：</td>
                        <td colspan="3">
                            <textarea class="{required:true,maxlength:1000}" style="width:606px;" name="routineinspectionQuestion.description" id="routineinspectionQuestion.description"></textarea><span class="red">*</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">问题图片：</td>
                        <td colspan="3">
                            <input type="file" id="filePath"  name="file"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">关联资源：</td>
                        <td colspan="3">
                            <input type="text" value="${questionInfo['resourceName']}" readonly="readonly" style="width: 125px;" name="routineinspectionQuestion.resourceName" id="resourceName"/>
                            <input type="hidden" value="${questionInfo['resourceId']}" name="routineinspectionQuestion.resourceId">
                            <input type="hidden" value="${questionInfo['resourceType']}" name="routineinspectionQuestion.resourceType">
                        </td>
                    </tr>
                </table>
                <div class="content_bottom">
                    <input type="submit" value="保 存" />
                    <input type="button" id="cancelButton" value="取 消" />
                </div>
            </div>
        </div>
    </div>
    </form>
</body>
</html>
