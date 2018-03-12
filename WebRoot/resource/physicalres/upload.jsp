<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>文件上传</title>
    <link href="css/uploadify.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
    <script type="text/javascript" src="../js/swfobject.js"></script>
    <script type="text/javascript" src="../js/jquery.uploadify.v2.0.3.js"></script>
    <script type="text/javascript">
    	$(function(){
			$("#bgImage").hide();
        	
			$("#uploadify").uploadify({
				'uploader':'swf/uploadify.swf',
				'script':'uploadAction?parentEntityTypeAndId=Station_2',
				//'auto':true,
				'cancelImg':'image/cancel.png',
				//'folder':'/upload',
				'fileDataName':'file',
				'queueID':'fileQueue',
				'queueSizeLimit':8,
				'fileDesc':'jpg,png,gif,bmp',
				'fileExt':'*.jpg;*.gif;*.png;*.bmp',
				'method':'post',
				'multi':true,
				'buttonText':'浏览文件',
				'onComplete':function(event,queueID,fileObj,serverData,data){
					//$('#bgImage').attr("src",serverData);
					var content = "<dl style='width:350px;height:120px;float:left;text-align:center;'><dt style='width:390px;height:110px;'><img style='width:100px;height:100px;' src='"+serverData+"' /></dt><dd>"+fileObj.name+"</dd></dl>";
					$("#imgPreviewDiv").append(content);
					alert("文件：" + fileObj.name + " 上传成功！");
				},
				'onError':function(event,queueID,fileObj){
					alert("文件：" + fileObj.name + " 上传失败！");
				}
			});
        });
    </script>
    <style type="text/css">
    	#bgImage {
    		margin:5px;
    	}
    	#operaFileUploadDiv{
    		width:100%;
    		padding-left:500px;
    	}
    	#fileQueue{
    		margin-bottom:20px;
    	}
    	/*flash按钮"浏览文件"*/
    	#uploadifyUploader {
    		margin-left:42px;
    	}
    </style>
  </head>
  
  <body>
  	<%--<span id="imgSpan">图片：</span>
    <form action="uploadAction" method="post" enctype="multipart/form-data">
   		文件上传：<input id="imgFile" type="file" name="file" onchange="fileContentChange();" /><br />
   		<input type="submit" value="提交" />
    </form>--%>
    <div id="imgPreviewDiv"></div>
    <div style="height:30px;clear:both;"></div>
    <div id="operaFileUploadDiv">
    	<div id="fileQueue"></div>
	    <input type="file" name=file id="uploadify" />
	    <p>
	    	<input type="button" value="开始上传" onclick="javascript:$('#uploadify').uploadifyUpload()" />&nbsp;
	    	<input type="button" value="取消所有上传" onclick="javascript:$('#uploadify').uploadifyClearQueue()" />
	    </p>
    </div>
  </body>
</html>
