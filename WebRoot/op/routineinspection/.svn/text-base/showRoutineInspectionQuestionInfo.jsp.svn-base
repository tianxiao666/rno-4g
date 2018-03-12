<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看问题详细信</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>

<script>
$(function(){
	$(".jd_reply_show").click(function(){
		$(".jd_reply").toggle();
	})
	$(".solve_reply_show").click(function(){
		$(".solve_reply").toggle();
	});
	
	var isOver = $("#isOver").val();
	if(isOver == "0"){
		$("#handle_table").hide();
		$("#handleQuestionButton").show();
	}
	
	$("#cancelButton").click(function(){
		$("#handleResult").val("");
		$("#handleQuestionDiv").hide();
	});
	
	//提交阶段回复现场任务
	$("#handleQuestion").validate({submitHandler: function(form) { 
		$("#handleQuestion").ajaxSubmit({
			dataType:'text',
			success:function(result){
				if(result=="success"){
					alert("处理成功！");
					window.location.reload();
				}else if(result=="fail"){
					alert("处理失败！");
				}	
				$("#handleQuestionDiv").hide();
			}
			
		});
		return false;
	}});
	
	var handlePicture = $("#handlePicture").val();
	var questionPicture = $("#questionPicture").val();
	if(questionPicture!=null&&questionPicture!=""){
		var fileStr = " <img style='width:800px;' src='"+questionPicture+"' />"
		$("#questionPictureSrc").append(fileStr);
	}
	if(handlePicture!=null&&handlePicture!=""){
		var fileStr = " <img style='width:800px;' src='"+handlePicture+"' />"
		$("#handlePictureSrc").append(fileStr);
	}
	
});
</script>
</head>

<body>
	<input type="hidden" id="isOver" value="${questionInfo['isOver']}"/>
	<input type="hidden"  id="handlePicture"  value="${questionInfo['handlePicture']}"/>
	<input type="hidden"  id="questionPicture"  value="${questionInfo['questionPicture']}"/>
    <div class="container960">
        <div class="header">
            <h4>查看问题详细信息</h4>
        </div>
        <div class="content">
            <table class="thleft_table" id="add_table">
                <tr>
                    <th colspan="4">
                        <em class="edit_ico">问题描述</em>
                        <em class="f-normal">${questionInfo['createTime']}</em>
                        <span class="th_right">
                        	<input id="handleQuestionButton" type="button" class="solve_reply_show" style="display:none;" value="解决问题" />
                        </span>
                        
                        
                        <%---------------------- 处理结果弹出div --------------------------%>
                        <form id="handleQuestion"  action="handleRoutineinspectionQuestionAction" method="post" enctype="multipart/form-data">
                        <input type="hidden"  name="TOID"  value="${questionInfo['TOID']}"/>
                        <input type="hidden"  name="QID"  value="${questionInfo['QID']}"/>
                        <div class="reply_div solve_reply" id="handleQuestionDiv">
                        	<em class="reply_tip"></em>
                        	<p>
                            	<em class="vt">处理结果：</em>
                                <textarea class="{maxlength:1000}" name="routineinspectionQuestion.handleResult" id="handleResult"></textarea>
                            </p>
                        	<p>
                            	<em class="vt">图片附件：</em>
                                <input type="file" name="file"/>
                            </p>
                            <div class="content_bottom">
                            	<input type="submit" value="保存" />
                            	<input type="button" id="cancelButton" value="取消" />
                            </div>
                        </div>
                        </form>
                    </th>
                </tr>
                <tr>
                    <td class="label_td">问题类型：</td>
                    <td>${questionInfo['questionType']}</td>
                    <td class="label_td">严重程度：</td>
                    <td>${questionInfo['seriousLevel']}</td>
                </tr>
                <tr>
                    <td class="label_td">关联资源：</td>
                    <td><a href="#">【${questionInfo['resourceType']}】${questionInfo['resourceName']}</a></td>
                    <td class="label_td">关联任务：</td>
                    <td>【日常巡检】${questionInfo['toId']}</td>
                </tr>
                <tr>
                    <td class="label_td">所属组织：</td>
                    <td>${questionInfo['creatorOrgName']}</td>
                    <td class="label_td">创建人：</td>
                    <td>${questionInfo['creatorName']}（${questionInfo['creatorPhone']}）</td>
                </tr>
                <tr>
                    <td class="label_td">问题描述：</td>
                    <td colspan="3">
                    	<p>${questionInfo['description']}</p>
                    	<div id="questionPictureSrc"></div>
                    </td>
                </tr>
            </table>
            
            <table class="thleft_table" id="handle_table">
                <tr>
                    <th colspan="4">
                        <em class="edit_ico">问题处理结果</em>
                        <em class="f-normal">（${questionInfo['handleTime']}）</em>
                    	<em class="edit_name">${questionInfo['handlerName']}</em>
                    </th>
                </tr>
                <tr>
                    <td class="label_td">处理结果：</td>
                    <td colspan="3">
                    	<p>${questionInfo['handleResult']}</p>
                        <div id="handlePictureSrc"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
	
</body>
</html>
