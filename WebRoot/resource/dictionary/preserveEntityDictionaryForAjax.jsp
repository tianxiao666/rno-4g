<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
.nodisplay-button{
display:none
}
.display-button{
display:inline
}
input[type="text"]{
display:none
}
td{text-align:center}
</style>
<script type="text/javascript">
$(function(){
			$("#dictionaryTable").chromatable({
				width:"100%",
				height:"430px",
				scrolling: "yes"
			});	
	/*var prevTableTr = $("#dictionaryTable").prev().children("thead").children("tr").children();
	$("#dictionaryTable thead tr th").each(function(index){
		$(this).css("width",prevTableTr.eq(index).css("width"));
	})*/
	$("#delete").click(function(){//删除字典
	    var check = $("input[id='choose']:checked");
		var size=check.size();
		if(size>0){
			if(confirm("确实要删除选中字典？")){
				check.each(function(){
					var parTr = $(this).parent();				
					var entityType=parTr.next().children().eq(0).text();
					//alert(entityType);
					var url= 'deleteResourceDictionaryAction';
					var data ={entityType:entityType,operateType:"entityChinese"};
					$.ajax({
						url:url,
						data:data,
						async:false,
						dataType:'json',
						type:'post',
						success:function(data){
							if(data=="success"){
								parTr.parent().remove();
							}else{
								alert(entityType+" 字典删除失败");
							}			
						}
					})	
				})
			}
			
		}else{
			alert("请选择要删除的字典");
		}
	
	})
	
	$("#chooseAll").click(function(){//全选
			if($(this).attr("checked")=="checked"){
				$("input[type='checkbox']:visible").attr("checked","checked");
			}else{
				$("input[type='checkbox']:visible").removeAttr("checked");
			}
		})
		

	
	
	
})


//修改按钮隐藏 保存，取消按钮显示
function displayButton(id){
			var inputs = $(id).parent().siblings().children("input[type='text']");
			var ems = $(id).parent().siblings().children("em[id='hideem']")
			inputs.show();
			ems.hide();
			$(id).prev().show();
			$(id).prev().prev().show();
			$(id).hide();
}
//修改字典取消
function cancelDictionary(id){
			var inputs = $(id).parent().siblings().children("input[type='text']");
			var ems = $(id).parent().siblings().children("em[id='hideem']");
			ems.next().val(ems.text());
			inputs.hide();
			ems.show();
			$(id).prev().hide();
			$(id).next().show();
			$(id).hide();
}
//保存修改的字典
function saveDictionary(id){
			var parTr=$(id).parent().parent();
			var entityType=parTr.children().eq(1).children().eq(1).val();
			var entityChineseType=parTr.children().eq(2).children().eq(1).val();
			if($.trim(entityChineseType)==""){
				alert("Entity 中文不能为空");
			}else{
				$.ajax({
						url:'updateResourceDictionaryAction',
						data:{entityType:entityType,entityChineseType:entityChineseType,operateType:"entityChinese"},
						async:false,
						dataType:'json',
						type:'post',
						success:function(data){
							if(data=="success"){
								alert("修改成功！");
								parTr.children().eq(2).children().eq(0).text(entityChineseType);
								var inputs = parTr.children().children("input[type='text']");
								var ems = parTr.children().children("em[id='hideem']");
								inputs.hide();
								ems.show();
								var buttonTd =parTr.children().eq(3).children();
								buttonTd.hide();
								buttonTd.eq(2).show();
							}else{
								alert("修改失败！");
							}
						}
				})	
			}
			


}
//点击增加按钮
function clickAddButton(){
	var content = "<tr id='resAdd'><td><input type='checkbox' id='choose'/></td><td><em></em>"
	  				+"<input type='text' name='entityType' style='display:inline'/>"
	  				+"</td><td><em id='hideem'></em><input type='text' name='entityChineseType' style='display:inline'/>"
	  				+"</td><td><input onclick='addDictionary(this);' type='button' value='增加' />&nbsp;<input type='button' onclick='cancelAdd(this);' value='取消' /></td></tr>";
	  $(content).insertAfter("#dictionaryTable tr[id='head']");
}
//取消增加
function cancelAdd(id){
	$(id).parent().parent().remove();
}
//保存增加的字典
function addDictionary(id){
	var cldTd=$(id).parent().parent().children();
	var entityType = cldTd.eq(1).children().eq(1).val();
	var entityChineseType = cldTd.eq(2).children().eq(1).val();
	if($.trim(entityType)==""||$.trim(entityChineseType)==""){
		alert("请填写完整信息");
	}else{
		var url="addResourceDictionaryAction";
		var data={entityType:entityType,entityChineseType:entityChineseType,operateType:"entityChinese"};
		$.ajax({
						url:url,
						data:data,
						async:false,
						dataType:'json',
						type:'post',
						success:function(data){
							if(data=="success"){
								alert("增加成功！");
								cldTd.eq(1).children().eq(0).text(entityType);
								cldTd.eq(2).children().eq(0).text(entityChineseType);
								cldTd.eq(1).children().eq(1).remove();
								cldTd.eq(1).append("<input type='hidden' name='entityType' value='"+entityType+"'>");
								var inputs = $(id).parent().siblings().children("input[type='text']");
								var ems = $(id).parent().siblings().children("em[id='hideem']");
								ems.next().val(ems.text());
								inputs.hide();
								ems.show();
								$(id).parent().html("<input id='save' class='nodisplay-button' type='button' value='保存' onclick='saveDictionary(this);'/>&nbsp;"
                    	+"<input id='cancel' class='nodisplay-button' type='button' value='取消' onclick='cancelDictionary(this);'/>&nbsp;"
                    	+ "<input class='display-button' type='button' value='修改' onclick='displayButton(this);' style='margin-left: 11px;'/>");
							$(id).parent().parent().removeAttr("id");
							}else{
								alert("增加失败，该字典可能已存在！");
							}
						}
				})	
	}
}
</script>
<div class="table_top">
                	<span class="fr mr10">维护操作：
                		<input type="button" value="增加" id="addDictionary" onclick="clickAddButton();"/>
                		&nbsp;&nbsp;&nbsp;&nbsp;<input id="delete" type="button" value="删除" />
                		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导入" onclick="showimport();" id="import" />
                	</span>
                	
                </div>
<table class="main_table2 tc" id="dictionaryTable">
<thead>
	<tr id="head">
    	<th><input type="checkbox" id="chooseAll" />选择</th>
    	<th>Entity英文</th>
    	<th>Entity中文</th>
    	<th><img src="images/modify.png" style="margin-bottom: -7px;"/>编辑</th>
    </tr>
</thead>
    <s:iterator value="resultList" id="map">
 <tr>
<td><input type="checkbox" id="choose"/></td>
<td><em>${entityName}</em><input type="hidden" name="entityType" value="${entityName}"/></td>
<td><em id="hideem">${entityChineseName}</em><input type="text" name="entityChineseType" value="${entityChineseName}"/></td>
<td><input id="save" class="nodisplay-button" type="button" value="保存" onclick="saveDictionary(this);"/>&nbsp;
<input id="cancel" class="nodisplay-button" type="button" value="取消" onclick="cancelDictionary(this);"/>&nbsp;
<input class="display-button" type="button" value="修改" onclick="displayButton(this);" /></td>
</tr>
</s:iterator>
</table>