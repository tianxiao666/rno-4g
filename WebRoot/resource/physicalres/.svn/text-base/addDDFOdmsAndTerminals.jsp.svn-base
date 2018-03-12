<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" href="css/base.css"/>
<link rel="stylesheet" type="text/css" href="css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/equipmentBox.css"/>
<style>
.main-table1 .menuTd{width:20%;}
.main-table1 .highLighte{ background:#FFF; width:30%;}
.main-table1 tr td {
   
    padding: 10px;
   
}
</style>
<script type="text/javascript">
/*单回路名称*/
function showEmValue(me){
var curId = $(me).attr("id");
$("."+curId).html($(me).val());
}
$(function(){
	//专项功能框隐藏或显示
			$(".container-tab1 fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
				})
			})
	//保存取消按钮去除		
	$("#editDiv").empty();
	
	
	
	
	
})
/**
 * 控制全选模块的端子
 *
 */
function selectAllTerminals(id){
	var idClass=$(id).attr("class");
	if($(id).attr("checked")=="checked"){
		$("."+idClass).attr("checked","checked");
	}else{
		$("."+idClass).removeAttr("checked");
	}
}
/**
 * 清空input
 * @param {Object} id
 */
function elonfocus(id){
				if($(id).css("color") == "#000" || $(id).css("color") == "rgb(0, 0, 0)") {
					return false;
				}
				$(id).val("");
				//focus后，字体更改为黑色
				$(id).css("color","#000");
}
function elonblur(id){
	var patterm =/[A-Za-z]/;
	var value =$(id).val();
	if(value!=""){
		if(isNaN(value)){
			if(!(patterm.test(value)&&value.toString().length==1)){
				alert("请输入一个字母！");
			}
		}
	}
} 

 //批量增加模块及端子
function saveOdmsAndTerminals(){
		var startOdmNumber = $("#startOdmNumber").val();
		var endOdmNumber =$("#endOdmNumber").val();
		var startTerminalNumber =$("#startTerminalNumber").val();
		var endTerminalNumber = $("#endTerminalNumber").val();
		var currentEntityType =$("#currentResourceType").val();
		var currentEntityId =$("#currentResourceId").val();
		if(startOdmNumber!=undefined||endOdmNumber!=undefined){
			if(/^[0-9]*$/.test(endOdmNumber)){
				if(!/^[0-9]*$/.test(startOdmNumber)){
					alert("模块开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			}else if(!/^[0-9]*$/.test(endOdmNumber)){
				if(/^[0-9]*$/.test(startOdmNumber)){
					alert("模块开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			}
			if(/^[0-9]*$/.test(endTerminalNumber)){
				if(!/^[0-9]*$/.test(startTerminalNumber)){
					alert("端子开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			}else if(!/^[0-9]*$/.test(endTerminalNumber)){
				if(/^[0-9]*$/.test(startTerminalNumber)){
					alert("端子开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			}
			if(startOdmNumber.toString()>=endOdmNumber.toString() || startTerminalNumber.toString()>=endTerminalNumber ){
				alert("开始编号不能大于或等于结束编号!");
				return false;
			}
				if(confirm("确实要批量增加吗?")){
					if(startOdmNumber=="开始编号"||endOdmNumber=="结束编号"){
						startOdmNumber="";
						endOdmNumber="";
					}
					if(startTerminalNumber=="开始编号"||endTerminalNumber=="结束编号"){
						startTerminalNumber="";
						endTerminalNumber="";
					}
					var useOdmName = "";
					var useLinkLabel="";
					var linklabel="";
					if($("#useOdmName").attr("checked")=="checked"){
						useOdmName="true";
						if($("#useLinkLabel").attr("checked")=="checked"){
							useLinkLabel="true";
							linklabel=$("#linklabel").val();
						}
					}
					var leftUpTerminalNumber = $("#leftuptNum").val();
					var rightUpTerminalNumber = $("#rightuptNum").val();
					var leftDownTerminalNumber=$("#leftdowntNum").val();
					var rightDownTerminalNumber=$("#rightdowntNum").val();
					if(leftUpTerminalNumber==""||rightUpTerminalNumber==""||rightUpTerminalNumber==""||rightUpTerminalNumber==""){
						alert("单回路端子名不能为空");
						return false;
					}
					var tLinkLabel="";//2M回路端子与单回路端子连接符
					if($("#tUseLinkLabel").attr("checked")=="checked"){
						tLinkLabel=$("#tLinkLabel").val();
					}
					var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId,startOdmNumber:startOdmNumber,endOdmNumber:endOdmNumber,startTerminalNumber:startTerminalNumber,endTerminalNumber:endTerminalNumber,useOdmName:useOdmName,useLinklabel:useLinkLabel,linklabel:linklabel,tLinkLabel:tLinkLabel,leftUpTerminalNumber:leftUpTerminalNumber,rightUpTerminalNumber:rightUpTerminalNumber,leftDownTerminalNumber:leftDownTerminalNumber,rightDownTerminalNumber:rightDownTerminalNumber};
					var url="addDDFOdmTerminalLotAction";
					$.post(url, params, function(data){	
						if(data=="success"){
							var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
							var url="getTerminalsAction";
							$.post(url, params, function(data){
									$("#contentDiv").html(data);			
							});
						}
					});
				}
				
			
		}else{

			if(startTerminalNumber=="开始编号"||endTerminalNumber=="结束编号"||startTerminalNumber==""||endTerminalNumber==""){
				alert("请完整输入信息!");
				return false;
			}
			
			if(/^[0-9]*$/.test(endTerminalNumber)){
				if(!/^[0-9]*$/.test(startTerminalNumber)){
					alert("端子开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			}else if(!/^[0-9]*$/.test(endTerminalNumber)){
				if(/^[0-9]*$/.test(startTerminalNumber)){
					alert("端子开始编号与结束编号类型应一致（字母或数字）");
					return false;
				}
			} 
			if(startTerminalNumber.toString()>= endTerminalNumber.toString()){
				alert("开始编号不能大于或等于结束编号!");
				return false;
			}

				var leftUpTerminalNumber = $("#leftuptNum").val();
				var rightUpTerminalNumber = $("#rightuptNum").val();
				var leftDownTerminalNumber=$("#leftdowntNum").val();
				var rightDownTerminalNumber=$("#rightdowntNum").val();
				if(leftUpTerminalNumber==""||rightUpTerminalNumber==""||rightUpTerminalNumber==""||rightUpTerminalNumber==""){
					alert("单回路端子名不能为空");
					return false;
				}
				var tLinkLabel="";//2M回路端子与单回路端子连接符
				if($("#tUseLinkLabel").attr("checked")=="checked"){
					tLinkLabel=$("#tLinkLabel").val();
				}

				if(confirm("确实要批量增加吗?")){
					var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId,startOdmNumber:"",endOdmNumber:"",startTerminalNumber:startTerminalNumber,endTerminalNumber:endTerminalNumber,useOdmName:"",useLinklabel:"",linklabel:"",tLinkLabel:tLinkLabel,leftUpTerminalNumber:leftUpTerminalNumber,rightUpTerminalNumber:rightUpTerminalNumber,leftDownTerminalNumber:leftDownTerminalNumber,rightDownTerminalNumber:rightDownTerminalNumber};
					var url="addOdmDDFTerminalLotAction";
					$.post(url, params, function(data){	
						if(data=="success"){
							var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
							var url="getTerminalsAction";
							$.post(url, params, function(data){
									$("#contentDiv").html(data);			
							});
						}
					});
				}
			
		}
	}
	/**
	 * 批量删除模块
	 */
	function deleteOdms(){
		var checksize=$("input[id='odms']:checked").size();
		if(checksize>0){
			if(confirm("确认要批量删除模块及端子？")){
				$("input[id='odms']:checked").each(function(){
					if($(this).next().next().text()==""){
						$("input[id='terminals']:checked").each(function(){
							var chooseResEntityId=$(this).next().val();
							var chooseResEntityType="DDFTerminal";
							var params={chooseResEntityType:chooseResEntityType,chooseResEntityId:chooseResEntityId};
							var url = "delPhysicalresByRecursionAction";
							$.ajax({
								url:url,
								data:params,
								async:false,
								dataType:'json',
								type:'post',
								success:function(data){
									if(data!="success"){
										alert("删除失败");
										return false;
									}
								}
							})	
						})
					}else{
						var chooseResEntityId=$(this).next().val();
						var chooseResEntityType="DDM";
						var params={chooseResEntityType:chooseResEntityType,chooseResEntityId:chooseResEntityId};
						var url = "delPhysicalresByRecursionAction";
						$.ajax({
							url:url,
							data:params,
							async:false,
							dataType:'json',
							type:'post',
							success:function(data){
								if(data!="success"){
									alert("删除失败");
									return false;
								}
							}
						})	
					}
					
				})
				var params={currentEntityType:$("#currentResourceType").val(),currentEntityId:$("#currentResourceId").val()};
				var url="getTerminalsAction";
				$.post(url, params, function(data){
					$("#contentDiv").html(data);			
				});		
			}
			
		}else{
			alert("请选择要删除的模块！")
		}
	}
 	/**
 	 * 批量删除端子
 	 */
 	function deleteTerminals(){
 		var checksize=$("input[id='terminals']:checked").size();
		if(checksize>0){
			if(confirm("确认要批量删除端子吗？")){
				$("input[id='terminals']:checked").each(function(){
					var chooseResEntityId=$(this).next().val();
					var chooseResEntityType="DDFTerminal";
					var params={chooseResEntityType:chooseResEntityType,chooseResEntityId:chooseResEntityId};
					var url = "delPhysicalresByRecursionAction";
					$.ajax({
						url:url,
						data:params,
						async:false,
						dataType:'json',
						type:'post',
						success:function(data){
							if(data!="success"){
								alert("删除失败");
								return false;
							}
						}
					})	
				})
				var params={currentEntityType:$("#currentResourceType").val(),currentEntityId:$("#currentResourceId").val()};
				var url="getTerminalsAction";
				$.post(url, params, function(data){
					$("#contentDiv").html(data);			
				});	
			}	
		}else{
			alert("请选择要删除的端子！")
		}
 	}
</script>
			<div class="container-tab1">
                <fieldset style="width:96%;">
                    <legend> <input type="checkbox" checked="checked"/> 专项功能</legend>
                    
                    <div class="fieldset_div">
                    	<input type="hidden" id="currentResourceType" value="${request.currentEntityType}"/>
			            <input type="hidden" id="currentResourceId" value="${request.currentEntityId}"/>
                    	<input type="button" value="模块/端子批量维护" onclick="addOdmsAndTerminals();" />&nbsp;
                        <input type="button" value="面板图" onclick="showOdmsAndTerminalsPanel();"/>
                    </div>
                </fieldset>
            </div>
             <table class="main-table1">
                    <tr class="main-table1-tr"><td colspan="2" class="main-table1-title">模块/端子批量维护</td></tr>
            	   <s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='DDF'"> 
                   <tr>
                        <td class="menuTd"> 模块设置：<div style="color:#999;">(数字单元)&nbsp;</div></td>
                        <td>模块名：<input type="text" id="startOdmNumber" style="color:#999;" value="开始编号" onfocus="elonfocus(this);" onblur="elonblur(this)"/>
                        &nbsp;到&nbsp;
                        <input style="color:#999;" type="text" id="endOdmNumber" value="结束编号" onfocus="elonfocus(this);" onblur="elonblur(this)"/>
                        <span style="color:#999;">◀一个模块即一个数字单元,为模块命名可以更好地区分不同模块、E1回路、端子</span>
                        </td>
                    </tr>
                   </s:if> 
                    <tr >
                        <td class="menuTd" rowspan="2">端子设置：<div style="color:#999;">(2M回路)&nbsp;&nbsp;</div></td>
                        <td>2M回路名：<input type="text" id="startTerminalNumber" style="color:#999;" value="开始编号" onfocus="elonfocus(this);" onblur="elonblur(this)"/>&nbsp;到&nbsp;<input style="color:#999;" type="text" id="endTerminalNumber" value="结束编号" onfocus="elonfocus(this);" onblur="elonblur(this)"/>&nbsp;
                        <s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='DDF'">
                        <input type="checkbox" id="useOdmName" checked/>使用模块名作为前缀&nbsp;<input type="checkbox" id="useLinkLabel" />使用连接字符
                        &nbsp;<select id="linklabel">
                            <option value="straight">-</option>
                            <option value="curve">~</option>
                            <option value="slant">/</option>
                             </select>
                         </s:if>
                        </td>
                      </tr>
                      <tr>
                        <td>
                        	<div class="addDDF">
								<span class="addDDF_title">单回路端子名：</span>
								<ul class="addTerminal" title="请输入字母+数字，如X1">
									<li class="c1"><input type="text" value="X1" id='leftuptNum' style='color:blue' onkeyup="showEmValue(this);"/><span style="color:#999;">左上</span></li>
									<li class="c2"><span style="color:#999;">右上</span><input type="text" value="X2" id='rightuptNum' style='color:blue' onkeyup="showEmValue(this);"/></li>
									<li class="c3"><input type="text" value="Y1" id='leftdowntNum' style='color:blue' onkeyup="showEmValue(this);"/><span style="color:#999;">左下</span></li>
									<li class="c4"><span style="color:#999;">右下</span><input type="text" value="Y2" id='rightdowntNum' style='color:blue' onkeyup="showEmValue(this);"/></li>
								</ul>
								<ul class="showTerminal">
									<li class="c1"><em class='leftuptNum'>X1</em></li>
									<li class="c2"><em class='rightuptNum'>X2</em></li>
									<li class="c3"><em class='leftdowntNum'>Y1</em></li>
									<li class="c4"><em class='rightdowntNum'>Y2</em></li>
								</ul>
							<span class="addDDF_title">	<input type="checkbox" id="tUseLinkLabel" checked />使用连接字符
	                        &nbsp;<select id="tLinkLabel">
	                            <option value="straight">-</option>
	                            <option value="curve">~</option>
	                            <option value="slant">/</option>
	                             </select>
	                         </span>
							</div>
                        </td>
                    </tr>
                    <tr>
                    	<td colspan="2"><input type="button" value="批量增加" onclick="saveOdmsAndTerminals()" /></td>
                    </tr>
                    
             </table>
             <div>
             	<span><h4 style="margin-left:10px;line-height:25px;">现有模块/端子</h4></span>
                
                    <s:iterator id="mp" value="odmTerminalList" status="st">
                     <s:if test="#st.index == 0 || #st.index == 1 || #st.index == 2||#st.index == 3 ">
                     	<table class="main-table1 tc" style="width:25%;float:left;text-align:center">
                     	<s:if test="#st.index ==0"><tr><th colspan="2"> 1</th></tr></s:if>
                     	<s:elseif test ="#st.index==1"><tr><th colspan="2"> 2</th></tr></s:elseif>
                     	<s:elseif test ="#st.index==2"><tr><th colspan="2"> 3</th></tr></s:elseif>
                     	<s:elseif test ="#st.index==3"><tr><th colspan="2"> 4</th></tr></s:elseif>
                	<tr>
                    	<th>模块</th>
                        <th>端子</th>
                    </tr>
                     </s:if>
                     <s:else>
                     	<table class="main-table1 tc " style="width:25%;float:left;text-align:center;margin-top:-10">
                     </s:else>
							  			<tr>
								  			<td rowspan="${mp.terminalcount}"><input id="odms" class="module${st.index}" type="checkbox" onclick="selectAllTerminals(this)"/><input type="hidden" id="odmId" value="${mp.odmId}"/><span>${mp.name}</span></td>
								  			<s:iterator id="cp" value="#mp.TerminalList" status="st2">
								  				<s:if test="#st2.index == 0">
								  						<td>
								  							<input id="terminals" class="module${st.index}"  type="checkbox" />
								  							<input type="hidden" id="terminalId" value="${cp.id}"/>
								  							<span>${cp.name}</span>
								  						</td>
								  					</tr>
								  				</s:if>
								  				<s:else>
								  					<tr>
								  						<td>
								  							<input id="terminals" class="module${st.index}"  type="checkbox" />
								  							<input type="hidden" id="terminalId" value="${cp.id}"/>
								  							<span>${cp.name}</span>					  		
								  						</td>
								  					</tr>
								  				</s:else>
								  			</s:iterator>

                     	</table>
                     
							  		</s:iterator>
				   
             </div>
             <table style="width:100%;text-align:center">
                  
                    	<tr>
                        	<td><input type="button" value="删除模块与端子" onclick="deleteOdms()"/>&nbsp;
                        	<input type="button" value="删除端子" onclick="deleteTerminals()"/>
                      </td> </tr>
                
                </table>