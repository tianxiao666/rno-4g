<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<input type="hidden" id="common_id" />
<input type="hidden" id="common_name" />
<input type="hidden" id="common_type" />
<input type="hidden" id="common_longitude" />
<input type="hidden" id="common_latitude" />
<input type="hidden" id="common_location" />
<input type="hidden" id="common_stationId" />
<input type="hidden" id="common_stationName" />
<input type="hidden" id="common_stationType" />
<input type="hidden" id="common_Area" />
<input type="hidden" id="common_importancegrade" />
<input type="hidden" id="common_orgId" />
<style type="text/css">
#stationList input[type="radio"]{left: 0; position: static; top: 0px;}
.selectStation-table tr td.rows1{white-space: normal;}
</style>
<script type="text/javascript">


$(function() {
	//选中单选按钮
	$(".selectStation-table tr td").each(function(){
		$(this).click(function(){
			$(this).find("input[name='stationRadio']").attr("checked","checked");
		});
	});
	
	//取消按钮
	$("#btnCancels").click( function() {
		$("#stationList").hide();
		$("#black").hide();
	});
	
	//确定按钮
	$("#btnSubmit").click(function(){
		var radio = $("input[name=stationRadio]:checked").val();
		if(radio==undefined){
			alert("请选中基站");
		}else{
			var info = radio.split(",");
			$("#common_id").val(info[0]);
			$("#common_name").val(info[1]);
			$("#common_type").val(info[5]);
			$("#common_longitude").val(info[3]);
			$("#common_latitude").val(info[4]);
			$("#common_location").val(info[2]);
			$("#common_stationId").val(info[6]);
			stationByIdAndType(info[0],info[5],"common");
			//bsSingleSubmit();
		}
	});
});
</script>
<div class="selectStation-title">站址列表
</div>
	 <div id="searchStation">	
           	<span>首字母索引：</span>
		   	<em class="selectStation-title-search">
		        <input type="text" id="rex"/>&nbsp;
				<input type="button" id="search" value="快速查询" onclick="baseStationSingleFuzzy();"/>
			</em>
               <ul>
               	   <li><a onclick="baseStationSinglePinyinQuery('a')">A</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('b')">B</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('c')">C</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('d')">D</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('e')">E</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('f')">F</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('g')">G</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('h')">H</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('i')">I</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('j')">J</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('k')">K</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('l')">L</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('m')">M</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('n')">N</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('o')">O</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('p')">P</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('q')">Q</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('r')">R</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('s')">S</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('t')">T</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('u')">U</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('v')">V</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('w')">W</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('x')">X</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('y')">Y</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('z')">Z</a></li>
                   <li style="width:96px;"><a onclick="baseStationSinglePinyinQuery('all')">显示全部</a></li>
                   
               </ul>
           </div>
           <span class="selectStation-main-title">搜索站址列表：</span>
	<div class="selectStation-main" id="stl">
	
		<table class="selectStation-table">	
			
		<s:iterator value="baseStationList" id="station" status="st">

			<s:if test="#st.getCount() % 4 ==1">
				<s:if test="#st.getCount() >1">
					</tr>
				</s:if>
				<tr>
			</s:if>
			<td class="rows1">
				<input id="" type="radio" name="stationRadio"
					value="<s:property value='id' />,<s:property value='name' />,<s:property value='address' />,<s:property value='longitude' />,<s:property value='latitude' />,<s:property value='_entityType' />,<s:property value='stationid' />" />
				<span><s:property value="name" /></span>
			</td>	
			<s:if test="#st.isLast()==true">
				<s:if test="#st.getCount() % 4 ==1">
					<td class="rows1"></td>
					<td class="rows1"></td>
					<td class="rows1"></td>
					
				</s:if>
				<s:elseif test="#st.getCount() % 4 ==2 ">
					<td class="rows1"></td>
					<td class="rows1"></td>
					
				</s:elseif>
				<s:elseif test="#st.getCount() % 4 ==3 ">
					<td class="rows1"></td>
					
				</s:elseif>
			</s:if>


		</s:iterator>
		</tr>
		</table>
	</div>
	<div class="selectStation-bottom">
		<input type="button" id="btnSubmit"  value="确定" />&nbsp;&nbsp;&nbsp;
		<input type="button" id="btnCancels" value="取消" />
	</div>