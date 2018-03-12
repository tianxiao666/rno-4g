$(function() {

		$(".switch").click(function() {
			$(this).hide();
			$(".switch_hidden").show();
			$(".resource_list_icon").animate({
				right : '0px'
			}, 'fast');
			$(".resource_list300_box").hide("fast");
		})
		$(".switch_hidden").click(function() {
			$(this).hide();
			$(".switch").show();
			$(".resource_list_icon").animate({
				right : '380px'
			}, 'fast');
			$(".resource_list300_box").show("fast");
		})
		$(".zy_show").click(function() {
			$(".search_box_alert").slideToggle("fast");
		})
	})

	/**
	 * 输入的数值必须为正数，如果是小数，最多只能输入两位小数
	  node是元素
	  引用方法，在需要验证的input中加入
	  onkeyup="checkNumber(this)"onafterpaste="checkNumber(this)"
	*/
	function checkNumber(node){
		  var money  = node.value;
		  money=money.replace(/[^.1234567890]/g,'');
		  if(money.indexOf('0')==0 &&　money.indexOf('.') != 1 && money.length >= 2){//小数点前0太多
		       money = money.replace('0','');
		  }
		  if(money.indexOf('0')==0 &&　money.indexOf('.') == -1 && money.length >= 2){//小数点前0太多
		       money = money.replace('0','');
		  }
		  if(money.indexOf('.') != -1){//包含小数点
		       var index = money.indexOf('.');
		      // money = money.substring(0,index+3);
		       var backString = money.substring(index+1);
		       //alert(backString);
		       if(backString.indexOf('.') != -1){//字符串中有第二个小数点
		          money = money.substring(0,index+1)+money.substring(index+1,index+1+backString.indexOf('.'));
		       }else{
		          money = money.substring(0,index+3);
		       }
		  }
		  //ou.jh
		  if(money.indexOf('.') != -1){//包含小数点
		  		money = money.substring(0,15);
		  }else{
		  		money = money.substring(0,12);
		  }
		  
		  
		  node.value=money;
	}

	
	function getRnoTrafficRenderer(trafficCode){
	var areaId = $("#cityId").val();
		var url = "getRnoTrafficRendererAction";
	var params = {areaId:areaId,trafficCode:trafficCode};
	var returnData;
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
		  async: false,
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
     	if(data){
     		var context = "";
     		if(data.trafficMap){
     			context = context + "<tr class=\"tb-tr-bg\">"
				                +"    <td style=\"width:20px\">"
				                +"    </td>"
				                +"</tr>";
     		}
     		context = context + "<tr class=\"tb-tr-bg\"> "
					                        +" 	<td style=\"width:20px\"> "
					                        +"     </td> "
					                        +" 	<td> "
					                        +"     	<table width=\"20\" height=\"20\"  style=\"background-color:#FFFFFF\"> "
					                        +"         	<tr > "
					                        +"             	<td style=\" border:1px solid #CCC; \"> "
					                        +"                 </td> "
					                        +"             </tr> "
					                        +"         </table> "
					                        +"     </td> "
					                        +"     <td> "
					                        +"     	<span>无干扰</span> "
					                        +" </tr>  ";
     		if(data.returnList){
     			$.each(data.returnList,function(k,v){
     					var backgroundcolor = "";
     					if(v.STYLE != null && v.STYLE != ""){
     						var da = eval("("+v.STYLE+")");
     						backgroundcolor = da.color;
     					}
     					context = context + "<tr class=\"tb-tr-bg\"> "
					                        +" 	<td style=\"width:20px\"> "
					                        +"     </td> "
					                        +" 	<td> "
					                        +"     	<table width=\"20\" height=\"20\"  style=\"background-color:"+backgroundcolor+"\"> "
					                        +"         	<tr > "
					                        +"             	<td style=\" border:1px solid #CCC; \"> "
					                        +"                 </td> "
					                        +"             </tr> "
					                        +"         </table> "
					                        +"     </td> "
					                        +"     <td> ";
					  if(v.MIN_VALUE == null){
					  		context = context + "     	<span>≤"+v.MAX_VALUE+"</span> ";
					  }else if(v.MAX_VALUE == null){
					  		context = context + "     	<span>>"+v.MIN_VALUE+"</span> ";
					  }else{
						  	context = context + "     	<span>"+v.MIN_VALUE+"-"+v.MAX_VALUE+"</span> ";
					  }
					  context = context 
					                        +" </tr>  ";
     			});
     		}
     			context = context + " <tr class=\"tb-tr-bg\"> "
                                    +"	<td style=\"width:20px\"> "
                                    +"    </td> "
                                    +"	<td> "
                                    +"    </td> "
                                    +"    <td> " 
                                    
                                    +"    </td> "
                                    +"    <td> "
                                    +"    	<input id=\"wire_edit\" name=\"\" type=\"button\"  value=\"修改\" onclick=\"getUpdateRnoTrafficRendererView("+areaId+",'"+trafficCode+"')\"  style=\"width:60px\" />	 "	
                                    +"    </td> "
                                    +" </tr>    ";
     		
     		$("#trafficTable").html(context);
     		$("#analyze_Dialog").css({
			"top" :(250) + "px",
			"right" :(20) + "px",
			"width":(160)+"px",
			"z-index": (30)
			});		
			$("#analyze_Dialog").show();
			returnData = data.returnList;
     	}
     }
     });
		return returnData;
	}
	
	function getUpdateRnoTrafficRendererView(areaId,trafficCode){
		var url = "getRnoTrafficRendererAction";
	var params = {areaId:areaId,trafficCode:trafficCode};
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
     	if(data){
     		var context = "";
     		var divContext = "";
     		if(data.trafficMap){
     			context = context + "<tr class=\"tb-tr-bg\">"
                                +"	<td style=\"width:20px\">"
                                +"    </td>"
								+" "
                                +"	<td colspan=\"5\" style=\"width:300px\">"
                                +" <input type=\"hidden\" value=\""+areaId+"\" name=\"areaId\"/>"
                                +" <input type=\"hidden\" value=\""+trafficCode+"\" id=\"trafficCode\" name=\"trafficCode\"/>"     
                                +"    </td>"
                                +" </tr>";
     		}
			context = context + "<tr class=\"tb-tr-bg-coldwhite\">"
					            +"    <td style=\"width:20px\">"
					            +"    </td>"
					            +"    <td style=\"width:30px\"><span>图例</span>"
					            +"    </td>"
					            +"    <td><span>最小值</span>"
					            +"    </td>"
					            +"    <td><span>最大值</span>"
					            +"    </td>"
					            +"     <td style=\"width:10px\">"
					            +"    </td>      "                             
					            +""
					           +" </tr>";
     		if(data.returnList){
     			$.each(data.returnList,function(k,v){
     					var backgroundcolor = "";
     					if(v.STYLE != null && v.STYLE != ""){
     						var da = eval("("+v.STYLE+")");
     						backgroundcolor = da.color;
     					}
     					var aId = 0;
     					if(v.AREA_ID != null && v.AREA_ID != 0 && v.AREA_ID != ""){
     						aId = v.AREA_ID;
     					}else{
     						aId = areaId;
     					}
     					var minValue = "";
     					if(v.MIN_VALUE != null){
     						minValue = v.MIN_VALUE;
     					}
     					var maxValue = "";
     					if(v.MAX_VALUE != null){
     						maxValue = v.MAX_VALUE;
     					}
     					var configOrder ="";
     					if(v.CONFIG_ORDER != null){
     						configOrder = v.CONFIG_ORDER;
     					}
     					context = context +"<tr class=\"tb-tr-bg\">"
                                    	+"<td style=\"width:20px\">"
                                        +"</td>"
	                                    +"<td>"
	                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].style\" class=\"colorstyle color"+k+"\" value=\""+backgroundcolor+"\" />"
	                                    +"    	<table width=\"20\" height=\"20\" class=\"color"+k+"\" onclick=\"farbtasticColor('colorpickerDiv"+k+"','colorpicker"+k+"','color"+k+"');\" id=\"value"+k+"\" style=\"background-color:"+backgroundcolor+"\">"
	                                    +"        	<tr >"
	                                    +"            	<td style=\" border:1px solid #CCC; \">"
	                                    +"                </td>"
	                                    +"            </tr>"
	                                    +"        </table>"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].trafficRenId\" value=\""+v.TRAFFIC_REN_ID+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].trafficType\" value=\""+v.TRAFFIC_TYPE+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].areaId\" value=\""+areaId+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].configOrder\" value=\""+configOrder+"\" />"
	                                    
	                                    +"    </td>"
	                                    +"    <td>"
	                                    +"    	<input type=\"text\" class=\"min_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].minValue\" onfocus=\"checkNumber(this);\" onkeyup=\"checkNumber(this)\" value=\""+minValue+"\" />"
	                                    +"    </td>"
	                                    +"    <td>"
	                                    +"    <input type=\"text\" class=\"max_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].maxValue\" onfocus=\"checkNumber(this);\" onkeyup=\"checkNumber(this)\" value=\""+maxValue+"\" />"
	                                    +"    </td>"
	                                    +"    <td>"
	                                    +"    <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].name\" value=\""+v.RENDERERNAME+"\" />"
	                                    +"    </td>"
	                                    +"    <td style=\"width:10px\">"
	                                    +"    </td>"                                  
	                                    +"</tr> ";
	                 divContext = divContext+"<div id=\"colorpickerDiv"+k+"\" class=\"dialog2 colordialog2\" style=\"display:none; width:410px;\" > "
										+"<div class=\"dialog_header\">"
					                    +"	<div class=\"dialog_title\">图例颜色选择</div>"
					                    +"    <div class=\"dialog_tool\">"
					        	        +"    	<div class=\"dialog_tool_close dialog_closeBtn2\" onclick=\"$('#colorpickerDiv"+k+"').hide();\"></div>"
					    	            +"    </div>"
										+"</div>"
					                    +"        <div class=\"dialog_content\" style=\"width:200px; height:200px; background:#f9f9f9\">"
			                            +"		      <div id=\"colorpicker"+k+"\" style=\"position:absolute;\"></div>"
			                            +"        </div>        "                              
			            				+"</div>";
     			});
     		}
     			context = context + "<tr class=\"tb-tr-bg\">"
                                  +"  	<td style=\"width:20px\">"
                                  +"      </td>"
                                  +"      <td>"
                                  +"      </td>"
                                  +"  	<td style=\" text-align:center\">"
 								  +"		<input id=\"save\" name=\"\" type=\"button\"  value=\"保存\" onclick=\"ajaxSubmitPOST('rendererForm');\" style=\"width:60px\" />	"                                            
                                  +"      </td>"
                                  +"      <td style=\" text-align:center\">"
 								  +"		<input id=\"close\" name=\"\" type=\"button\"  value=\"取消\"  onclick=\"$('#analyzeedit_Dialog').hide();$('.black').hide();\" style=\"width:60px\" />"	
                                  +"      </td>"
                                  +"      <td>"
                                  +"      		"
                                  +"      </td>"
                                  +"  </tr>";
     		$("#analyzeedit_trafficTable").html(context);
     		$("#divColorDiv").html(divContext);
     		
     		$("#analyzeedit_Dialog").css({
			"top" :(250) + "px",
			"right" :(195) + "px",
			"width":(360)+"px",
			"z-index": (40)
			});		
			$("#analyzeedit_Dialog").show();
			$(".black").show();
     	}
     }
     });
		
	}
	
	function farbtasticColor(dialogDivId,farbtasticDivId,colorId){
		$("#"+dialogDivId).css({
			"top" :(250) + "px",
			"right" :(568) + "px",
			"width":(200)+"px",
			"height":(242)+"px",
			"z-index": (40)
			});		
			$(".colordialog2").hide();
			$("#"+dialogDivId).show();
		//var color = $("#"+colorId).val();
		//alert(color);
		 $('#'+farbtasticDivId).farbtastic('.'+colorId);
		 //$("#"+valueId).css("background-color",$("#"+colorId).val());
		 //$("#"+colorId).val(color);
	}
	
	function ajaxSubmitPOST(formId){
	
	var minNULLCount = 0;
	var maxNULLCount = 0;
	var isTrue = 0;
	$(".min_value").each(function(k,v){
		if($(this).val() == null || $(this).val() == ""){
			minNULLCount++;
		}else if($(".max_value").eq(k).val() == null || $(".max_value").eq(k).val() == ""){
			maxNULLCount++;
		}else if(($(".max_value").eq(k).val() == null || $(".max_value").eq(k).val() == "")
			&& ($(this).val() == null || $(this).val() == "")){
			alert("最小值与最大值不能同时为空");
			isTrue++;
			return false;
		}else{
			if(parseInt($(".max_value").eq(k).val()) < parseInt($(this).val())){
				alert("最小值不能大于最大值");
				isTrue++;
				$(this).focus();
				return false;
			}
		}
	});
	if(minNULLCount > 1){
		alert("空值最小值不能多于一个");
		return false;
	}
	
	if(maxNULLCount > 1){
		alert("空值最大值不能多于一个");
		return false;
	}
	if(isTrue > 0){
		return false;
	}
	
	
	$(".colorstyle").each(function(){
		var text = "{\'color\':\'" + $(this).val() + "\'}";
		$(this).val(text);
	});
		$("#"+formId).ajaxSubmit({
			url : 'updateOrAddrnoTrafficRendererAction',
			dataType : 'json',
			success : function(data) {
			// console.log(data);
				alert(data.message);
				if(data.flag == true){
					$("#analyzeedit_Dialog").hide();
					$(".black").hide();
					$(".colordialog2").hide();
					var trafficCode = $("#trafficCode").val();
					getRnoTrafficRenderer(trafficCode);
				}
			},
			complete : function() {
			$(".loading_cover").css("display", "none");
			}
		});
	}