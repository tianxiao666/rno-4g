<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
			$.ajax({ //entity 资源类型列表
						url:'getResourceTypeAction',
						data:'',
						dataType:'json',
						async:false,
						type:'post',
						success:function(data){
							var content="";
							$.each(data,function(index,value){
								if(value.chineseName=="中文未定义"){
									content = content+"<option value='"+value.typeName+"'>"+value.typeName+"(<em style='color:red'>"+value.chineseName+"</em>)</option>";
								}else{
									content = content+"<option value='"+value.typeName+"'>"+value.typeName+"("+value.chineseName+")</option>";
								}
								
							
							})
							$("#entityNames").append(content);
						}
				})	
		$("#entityNames").change(function(){//下拉框取值变化
			var entityName= $("#entityNames").val();
			$("#attributeNames").val("all");
			if(entityName=="all"){
				$("#attributeNames").html("<option value='all'>全部</option>");
				$("tr").show();
			}else{
				$("tr[class='trContent']").hide();
				$("tr[id='"+entityName+"']").show();		
				$.ajax({//资源类型对应属性列表
						url:'getResourcAttributeAction',
						data:{entityType:entityName},
						dataType:'json',
						async:false,
						type:'post',
						success:function(data){
							var content="<option value='all'>全部</option>";
							$.each(data,function(index,value){
								content = content+"<option value='"+value.attrName+"'>"+value.attrName+"</option>";
							
							})
							$("#attributeNames").html(content);
						}
				})	
			}
			
		})
		
		$("#attributeNames").change(function(){//资源属性下拉框变化
			var entityName = $("#entityNames").val();
			var attributeName = $("#attributeNames").val();
			if(attributeName=="all"){
				$("tr[class='trContent']").hide();
				$("tr[id='"+entityName+"']").show();
			}else{
				$("tr[class='trContent']").hide();
				$("tr[id='"+entityName+"']").each(function(){
					 var r=$(this).find("td[id='"+attributeName+"']");
					 if(r.size()==1){
					 	$(this).show();	
					 }
				})
					
			}
		});
	
	$("#delete").click(function(){//删除字典
	    var check = $("input[id='choose']:checked");
		var size=check.size();
		if(size>0){
			if(confirm("确实要删除选中字典？")){
				check.each(function(){
				var parTr = $(this).parent();				
				var entityType=parTr.next().children().eq(0).text();
				var attributeType = parTr.next().next().children().eq(0).text();
				var originalMixName= parTr.parent().children().eq(4).children().eq(2).val();
				//alert(entityType);
				//alert(attributeType);
				//alert(originalMixName);
				var url= 'deleteResourceDictionaryAction';
				var data ={entityType:entityType,attributeType:attributeType,originalMixName:originalMixName,operateType:"attributeEntityDictionary"};
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
								alert(entityType+" "+attributeType+" Entity级别字典删除失败");
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
//修改按钮隐藏，保存，取消按钮显示
function displayButton(id){
			var inputs = $(id).parent().siblings().children("input[type='text']");
			var ems = $(id).parent().siblings().children("em[id='hideem']");
			inputs.show();
			ems.hide();
			$(id).prev().show();
			$(id).prev().prev().show();
			$(id).hide();
}
//取消字典修改
function cancelDictionary(id){
			var inputs = $(id).parent().siblings().children("input[type='text']");
			var ems = $(id).parent().siblings().children("em[id='hideem']");
			ems.each(function(){
				$(this).next().val($(this).text());
				$(this).show();
			})
			inputs.hide();
			$(id).prev().hide();
			$(id).next().show();
			$(id).hide();
}
//保存修改字典
function saveDictionary(id){
			var parTr=$(id).parent().parent();
			var entityType=parTr.children().eq(1).children().eq(1).val();
			var attributeType=parTr.children().eq(2).children().eq(1).val();
			var mixValue=parTr.children().eq(3).children().eq(1).val();
			var mixName=parTr.children().eq(4).children().eq(1).val();
			var originalMixName=parTr.children().eq(4).children().eq(2).val();		
			//alert(entityType);
			//alert(attributeType);
			//alert(mixValue);
			//alert(mixName);
			//alert(originalMixName);
			if($.trim(mixValue)==""){
				alert("下拉选项值不能为空");
			}else if($.trim(mixName)==""){
				alert("下拉选项显示值不能为空");
			}else{
				var data={entityType:entityType,attributeType:attributeType,mixValue:mixValue,mixName:mixName,originalMixName:originalMixName,operateType:"attributeEntityDictionary"};
				$.ajax({
						url:'updateResourceDictionaryAction',
						data:data,
						async:false,
						dataType:'json',
						type:'post',
						success:function(data){
							if(data=="success"){
								alert("修改成功！");
								var cldTd = parTr.children();
								cldTd.each(function(index){
									var tx=$(this).children();
									if(index==4){
										tx.eq(0).text(tx.eq(1).val());
										tx.eq(2).val(tx.eq(1).val());
									}else{
										tx.eq(0).text(tx.eq(1).val());
									}	
								})		
								var inputs = parTr.children().children("input[type='text']");
								var ems = parTr.children().children("em[id='hideem']");
								inputs.hide();
								ems.show();
								var buttonTd =parTr.children().eq(5).children();
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
	var entityName = $("#entityNames").val();
	var attributeName = $("#attributeNames").val();
	if(entityName=="all"){
		entityName="";
	}
	if(attributeName=="all"){
		attributeName="";
	}
	var content = "<tr id='resAdd'><td><input type='checkbox' id='choose'/></td>"
	  				+"<td><em></em><input type='text' name='entityType' style='display:inline' value='"+entityName+"'/></td>"
	  				+"<td><em></em><input type='text' name='attributeType' style='display:inline' value='"+attributeName+"'/></td>"
	  				+"<td><em id='hideem'></em><input type='text' name='mixValue' style='display:inline'/></td>"
	  				+"<td><em id='hideem'></em><input type='text' name='mixName' style='display:inline'/></td>"
	  				+"<td><input onclick='addDictionary(this);' type='button' value='增加' />&nbsp;<input type='button' onclick='cancelAdd(this);' value='取消' /></td></tr>";
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
	var attributeType = cldTd.eq(2).children().eq(1).val();
	var mixValue = cldTd.eq(3).children().eq(1).val();
	var mixName = cldTd.eq(4).children().eq(1).val();
	if($.trim(entityType)==""||$.trim(attributeType)==""||$.trim(mixValue)==""||$.trim(mixName)==""){
		alert("请填写完整信息");
	}else{
		var url="addResourceDictionaryAction";
		var data={entityType:entityType,attributeType:attributeType,mixValue:mixValue,mixName:mixName,operateType:"attributeEntityDictionary"};
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
								cldTd.eq(2).children().eq(0).text(attributeType);
								cldTd.eq(3).children().eq(0).text(mixValue);
								cldTd.eq(4).children().eq(0).text(mixName);
								
								cldTd.eq(1).children().eq(1).remove();
								cldTd.eq(2).children().eq(1).remove();
								cldTd.eq(1).append("<input type='hidden' name='entityType' value='"+entityType+"'>");
								cldTd.eq(2).append("<input type='hidden' name='attributeType' value='"+attributeType+"'>");
								cldTd.eq(4).append("<input type='hidden' name='orginalMixName' value='"+mixName+"' >");
								var inputs = $(id).parent().siblings().children("input[type='text']");
								var ems = $(id).parent().siblings().children("em[id='hideem']");
								//ems.next().val(ems.text());
								inputs.hide();
								ems.show();
								$(id).parent().html("<input id='save' class='nodisplay-button' type='button' value='保存' onclick='saveDictionary(this);'/>&nbsp;"
                    	+"<input id='cancel' class='nodisplay-button' type='button' value='取消' onclick='cancelDictionary(this);'/>&nbsp;"
                    	+ "<input class='display-button' type='button' value='修改' onclick='displayButton(this);' style='margin-left: 11px;'/>");
							$(id).parent().parent().removeAttr("id");
							}else{
								alert("增加失败！");
							}
						}
				})	
	}
}
</script>
<div class="table_top">
                	<span class="fl">
                    	Entity筛选：
                        <select id="entityNames">
                        	<option value="all">全部</option>
                        </select>
                        Attribute筛选：
                        <select id="attributeNames">
                        	<option value="all">全部</option>
                        </select>
                    </span>
                	<span class="fr mr10">维护操作：
                		<input type="button" value="增加" id="addDictionary" onclick="clickAddButton();"/>
                		&nbsp;&nbsp;&nbsp;&nbsp;<input id="delete" type="button" value="删除" />
                		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="导入" onclick="showimport();" id="import" />
                	</span>
                </div>
<table class="main_table2 tc" id="dictionaryTable">
	<thead>
	<tr id="head">
    	<th><input type="checkbox" id="chooseAll"/>选择</th>
    	<th>Entity英文</th>
    	<th>Attribute-属性英文</th>
        <th>value-下拉选项(属性值)</th>
        <th>Display-下拉选项(属性显示名称)</th>
        <th><img src="images/modify.png" style="margin-bottom: -7px;"/>编辑</th>
    </tr>
    </thead>
    <s:iterator value="resultList" id="map">
<s:iterator value="#map.attrList" id="map1">
<tr id="${entityName}" class="trContent">
<td><input type="checkbox" id="choose"/></td>
<td><em>${entityName}</em><input type="hidden" name="entityType" value="${entityName}"/></td>
<td id="${attributeName}"><em>${attributeName}</em><input type="hidden" name="attributeType" value="${attributeName}"/></td>
<td><em id="hideem">${attrValue}</em><input type="text" name="mixValue" value="${attrValue}"/></td>
<td><em id="hideem">${attrName }</em><input type="text" name="mixName" value="${attrName}"/><input type="hidden" name="originalMixName" value="${attrName}"/></td>
    
	<td><input id="save" class="nodisplay-button" type="button" value="保存" onclick="saveDictionary(this);"/>&nbsp;
	<input id="cancel" class="nodisplay-button" type="button" value="取消" onclick="cancelDictionary(this);"/>&nbsp;
	<input class="display-button" type="button" value="修改" onclick="displayButton(this);" /></td>
</tr>
	</s:iterator>
</s:iterator>
   
   
</table>
