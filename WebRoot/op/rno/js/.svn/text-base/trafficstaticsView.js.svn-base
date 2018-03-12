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
	});

	/**
	 * 输入的数值必须为正数，如果是小数，最多只能输入两位小数 node是元素 引用方法，在需要验证的input中加入
	 * onkeyup="checkNumber(this)"onafterpaste="checkNumber(this)"
	 */
	function checkNumber(node){
		  var money  = node.value;
		  money=money.replace(/[^.1234567890]/g,'');
		  if(money.indexOf('0')==0 &&　money.indexOf('.') != 1 && money.length >= 2){// 小数点前0太多
		       money = money.replace('0','');
		  }
		  if(money.indexOf('0')==0 &&　money.indexOf('.') == -1 && money.length >= 2){// 小数点前0太多
		       money = money.replace('0','');
		  }
		  if(money.indexOf('.') != -1){// 包含小数点
		       var index = money.indexOf('.');
		      // money = money.substring(0,index+3);
		       var backString = money.substring(index+1);
		       // alert(backString);
		       if(backString.indexOf('.') != -1){// 字符串中有第二个小数点
		          money = money.substring(0,index+1)+money.substring(index+1,index+1+backString.indexOf('.'));
		       }else{
		          money = money.substring(0,index+3);
		       }
		  }
		  // ou.jh
		  if(money.indexOf('.') != -1){// 包含小数点
		  		money = money.substring(0,15);
		  }else{
		  		money = money.substring(0,12);
		  }
		  
		  
		  node.value=money;
	}

	
   /**
	 * 图形化显示某个渲染规则的颜色示例
	 * 
	 * @param data
	 * @param valueSuffix
	 * 值后面的后缀，如%，或空
	 * @param hideValue
	 * 是否需要隐藏值，如果为true，则不显示值，只显示名称
	 */
	
	var lastValueSuffix=null;
	var lastHideValue=null;
   function showRendererRuleColor(data,trafficCode,areaId,valueSuffix,hideValue){
	   //console.log("in showRendererruleColor .. data=="+data+",areaId="+areaId);
	   if(valueSuffix==undefined || valueSuffix==null){
		   if(lastValueSuffix!=null){
			   valueSuffix=lastValueSuffix;
		   }else{
			   valueSuffix="";
		   }
	   }
	   if(hideValue==undefined){
		   if(lastHideValue!=null){
			   hideValue=lastHideValue;
		   }
	   }
	   var colspan=3;
	   if(hideValue==true){
		   colspan=2;
	   }
	   if(lastValueSuffix==null){
		   lastValueSuffix=valueSuffix;
	   }
	   if(lastHideValue==null){
		   lastHideValue=hideValue;
	   }
    	if(data){
    		//console.log("data is valid");
    		var context = "";
    		if(data.trafficMap){
    			// console.log("data has trafficmap");
    		  context = context + "<tr class=\"tb-tr-bg\">"
				                +"    <td style=\"width:15px\">"
				                +"    </td>"
				                +"	<td colspan=\"3\" style=\"width:300px\">"
				                +"    	<span class=\"sp_title\">"+data.trafficMap.NAME+"  图例 </span>"
				                +"    </td>"
				                +"    <td style=\"width:15px\">"
				                +"    </td>"
				                +"</tr>";

			  context = context + "<tr class=\"tb-tr-bg-coldwhite\">"
					            +"    <td style=\"width:15px\">"
					            +"    </td>"
					            +"    <td style=\"width:30px\" align=\"center\"><span>图例</span>"
					            +"    </td>"
					            +"    <td align=\"center\"><span>范围</span>"
					            +"    </td>"
					            +"    <td align=\"center\"><span>指标描述</span>"
					            +"    </td>"
					            +"     <td style=\"width:15px\">"
					            +"    </td>"                         
					            +"</tr>";
    		}
    		if(data.returnList){
    			//console.log("data has returnList");
    			try{
    			$.each(data.returnList,function(k,v){
    					var backgroundcolor = "";
    					if(v.STYLE != null && v.STYLE != ""){
    						//console.log("style="+v.STYLE);
    						var da = eval("("+v.STYLE+")");
    						backgroundcolor = da.color;
    					}
    					context = context + "<tr class=\"tb-tr-bg\"> "
					                        +" 	<td style=\"width:15px\"> "
					                        +"     </td> "
					                        +" 	<td> "
					                        +"     	<table width=\"20\" height=\"20\"  style=\"background-color:"+backgroundcolor+"\"> "
					                        +"         	<tr > "
					                        +"             	<td style=\" border:1px solid #CCC; \"> "
					                        +"                 </td> "
					                        +"             </tr> "
					                        +"         </table> "
					                        +"     </td> ";
    					
    				  if(hideValue!=true){
    				  context+="<td align=\"center\">";
					  if((v.MIN_VALUE==null ||v.MIN_VALUE==undefined) && (v.MAX_VALUE==null || v.MAX_VALUE==undefined)){
						  context = context + "     	<span></span> ";
					  }
    				  else if(v.MIN_VALUE == null){
					  		context = context + "     	<span><"+v.MAX_VALUE+valueSuffix+"</span> ";
					  }else if(v.MAX_VALUE == null){
					  		context = context + "     	<span>>="+v.MIN_VALUE+valueSuffix+"</span> ";
					  }else{
						  	context = context + "     	<span>"+v.MIN_VALUE+valueSuffix+"-"+v.MAX_VALUE+valueSuffix+"</span> ";
					  }
					  context+="</td>";
    				  }
					  context +="     <td align=\"center\"> "
					                        +"     	<span><xmp>"+v.RENDERERNAME+"</xmp></span> "
					                        +"     </td> "
							                +"    <td style=\"width:15px\">"
							                +"    </td>"
					                        +" </tr>  ";
    			});
    			}catch(err){
    				//console.error(err);
    			}
    		}
    		 //console.log("1--context==="+context);
    		
    		var td="<input id='wire_edit'  name='' type='button'  value='修改' onclick='showUpdateRnoTrafficRendererPanel(\""+areaId+"\",\""+trafficCode+"\")'  style='width:60px' />";
    		//console.log("td=="+td);
    		context += " <tr class=\"tb-tr-bg\"> "
                        +"	<td style=\"width:15px\"> "
                        +"    </td> "
                        +"<td colspan=\"3\" align=\"right\">"
                        +td
                        +"</td>"
                        +"	<td style=\"width:15px\"> "
                        +"    </td> "
                        +" </tr>    ";

            try{
    		fillUpdatePanelWithData(areaId,trafficCode,data);
            }catch(err){
            	//console.log(err);
            }
    		$("#trafficTable").html(context);
    		$("#analyze_Dialog").css({
			"top" :(40) + "px",
			//"right" :(400) + "px",
			//"width":(300)+"px",
			"z-index": (30)
			});
			
			$("#analyze_Dialog").show();
			
			// 填充修改面板，但暂不显示
			
      }else{
    	  //console.warn("data invalid!");
      }

   }
	
   /**
	 * 填充用于修改的面板
	 * 
	 * @param areaId
	 * @param tafficCode
	 * @param data
	 */
   function fillUpdatePanelWithData(areaId,trafficCode,data){
       //console.log("fillUpdatePanelWithData. areaId="+areaId+",trafficCode="+trafficCode+",data="+data);
    	if(data){
    		var context = "";
    		var divContext = "";
    		if(data.trafficMap){
    		  context = context + "<tr class=\"tb-tr-bg\">"
             	  				+"	<td style=\"width:20px\">"
				                +"    </td>"
				                +"	<td colspan=\"5\" style=\"width:350px\">"
				                +"      <input type=\"hidden\" value=\""+areaId+"\" name=\"areaId\"/>"
				                +"      <input type=\"hidden\" value=\""+trafficCode+"\" id=\"trafficCode\" name=\"trafficCode\"/>"                                                 
				                +"      <span class=\"sp_title\">"+data.trafficMap.NAME+"</span>"
				                +"    </td>"
			                	+"</tr>";
				}
			  context = context +"<tr class=\"tb-tr-bg-coldwhite\">"
					            +"    <td style=\"width:15px\">"
					            +"    </td>"
					            +"    <td style=\"width:30px\" align=\"center\"><span>图例</span>"
					            +"    </td>"
					            +"    <td align=\"center\"><span>最小值</span>"
					            +"    </td>"
					            +"    <td align=\"center\"><span>最大值</span>"
					            +"    </td>"
					            +"    <td style=\"width:160px\" align=\"center\"><span>指标描述</span>"
					            +"    </td>"
					            +"     <td style=\"width:15px\">"
					            +"    </td>"                             
					            +"</tr>";
			//保存选项的disableFields选项
			var disabledFieldsArray = "[";
    		if(data.returnList){
    			$.each(data.returnList,function(k,v){
    					var backgroundcolor = "";
    					var style = "";
    					if(v.STYLE != null && v.STYLE != ""){
    						//var da = eval("("+v.STYLE+")");
    						style = v.STYLE;
    						backgroundcolor = eval("("+style+")").color;
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
    					var disabledFields="";
    					//console.log("v.DISABLED_FIELDS="+v.DISABLED_FIELDS);
    					v.DISABLED_FIELDS=getValidValue(v.DISABLED_FIELDS,{});
    					if(v.DISABLED_FIELDS!=null && v.DISABLED_FIELDS!=undefined && v.DISABLED_FIELDS!="undefined" && v.DISABLED_FIELDS!="null"){
    						try{
    							disabledFields=eval("("+v.DISABLED_FIELDS+")");
    						}catch(err){
    							//console.error(err);
    							disabledFields=eval("({'MIN_VALUE':false,'MAX_VALUE':false})");
    						}
    					}
    					var colorDisabled=(true == disabledFields['color']);
    					var minvalueDisabled=(true == disabledFields['MIN_VALUE']);
    					var maxvalueDisabled=(true == disabledFields['MAX_VALUE']);
    					var disabledFieldStr = "{'MIN_VALUE':"+disabledFields['MIN_VALUE']+",'MAX_VALUE':"+disabledFields['MAX_VALUE']+"}";
    					if(k<data.returnList.length-1)
    					{
    						disabledFieldsArray = disabledFieldsArray + disabledFieldStr + ",";
    					}
    					else
    					{
    						disabledFieldsArray += disabledFieldStr;
    					}
    					//params
    					var params = "";
    					if(v.PARAMS != null && v.PARAMS != ""){
    						//var da = eval("("+v.STYLE+")");
    						params = v.PARAMS
    					}
    					context = context +"<tr class=\"tb-tr-bg\">"
                                   	+"<td style=\"width:20px\">"
                                       +"</td>"
	                                    +"<td align=\"center\">"
	                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].style\" class=\"colorstyle color"+k+"\" value=\""+style+"\" />"
	                                    +"      <input type=\"hidden\" name=\"rnoTrafficRendererList["+k+"].params\" value=\""+params+"\" />"
	                                    +"    	<table id=\"colorTable"+k+"\" width=\"20\" height=\"20\" class=\"color"+k+"\""+(colorDisabled==true?"":" onclick=\"farbtasticColor('colorpickerDiv"+k+"','colorpicker"+k+"','colorTable"+k+"');\"")+" id=\"value"+k+"\" style=\"background-color:"+backgroundcolor+"\">"
	                                    +"        	<tr >"
	                                    +"            	<td style=\" border:1px solid #CCC; \">"
	                                    +"                </td>"
	                                    +"            </tr>"
	                                    +"        </table>"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].trafficRenId\" value=\""+v.TRAFFIC_REN_ID+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].trafficType\" value=\""+v.TRAFFIC_TYPE+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].areaId\" value=\""+areaId+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].configOrder\" value=\""+configOrder+"\" />"
	                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].disabledFields\" value=\""+disabledFieldStr+"\" />"
	                                    +"    </td>"
	                                    +"    <td align=\"center\">"
	                                    +"    	<input type="+(minvalueDisabled==true?"'hidden'":"\"text\"")+" class=\"min_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].minValue\"  onblur=\"checkNumber(this)\" value=\""+minValue+"\" />"
	                                    +"    </td>"
	                                    +"    <td align=\"center\">"
	                                    +"    <input type="+(maxvalueDisabled==true?"'hidden'":"\"text\"")+" class=\"max_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].maxValue\"  onblur=\"checkNumber(this)\" value=\""+maxValue+"\" />"
	                                    +"    </td>"
	                                    +"    <td align=\"center\">"
	                                   // +"    	<span><xmp>"+v.RENDERERNAME+"</xmp></span>"
	                                    +"    <input type=\"text\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].name\" value=\""+v.RENDERERNAME+"\" />"
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
    		disabledFieldsArray +="]";
    		//console.log("disabledFieldsArray: " +disabledFieldsArray);
/*    			context = context + "<tr class=\"tb-tr-bg\">"
                                 +"  	<td style=\"width:20px\">"
                                 +"      </td>"
                                 +"      <td>"
                                 +"      </td>"
                                 +"  	<td style=\" text-align:center\">"
								  +"		<input id=\"save\" name=\"\" type=\"button\"  value=\"保存\" onclick=\"updateRendererRule('rendererForm',"+disabledFieldsArray+");\" style=\"width:60px\" />	"                                            
                                 +"      </td>"
                                 +"      <td style=\" text-align:center\">"
								  +"		<input id=\"close\" name=\"\" type=\"button\"  value=\"取消\"  onclick=\"$('#analyzeedit_Dialog').hide();$('.black').hide();\" style=\"width:60px\" />"	
                                 +"      </td>"
                                 +"      <td>"
                                 +"      		"
                                 +"      </td>"
                                 +"  </tr>";*/
				context = context +"<tr class=\"tb-tr-bg\">"
								  +"  	<td align=\"center\" colspan=\"5\">"
								  +"		<input id=\"save\" name=\"\" type=\"button\"  value=\"保存\" onclick=\"updateRendererRule('rendererForm',"+disabledFieldsArray+");\" style=\"width:60px\" />	" 
								  +"        &nbsp;&nbsp;&nbsp;"
								  +"		<input id=\"close\" name=\"\" type=\"button\"  value=\"取消\"  onclick=\"$('#analyzeedit_Dialog').hide();$('.black').hide();\" style=\"width:60px\" />"	
								  +"    </td>"
								  +"</tr>";
    		$("#analyzeedit_trafficTable").html(context);
    		$("#divColorDiv").html(divContext);
    		
    		
    	}
   }
	
   /**
	 * 显示更新面板，面板内容已经在前一步填充
	 * 
	 * @param areaId
	 * @param trafficCode
	 */
	function showUpdateRnoTrafficRendererPanel(areaId,trafficCode){
		$("#analyzeedit_Dialog").css({
			"top" :(40) + "px",
			"right" :(700) + "px",
//			"width":(410)+"px",
			"z-index": (40)
		});		
		
		$("#analyzeedit_Dialog").show();
		$(".black").show();
		
	}
	
	function farbtasticColor(dialogDivId,farbtasticDivId,colorTable){
		$("#"+dialogDivId).css({
			"top" :(40) + "px",
			"right" :(486) + "px",
			"width":(200)+"px",
			"height":(242)+"px",
			"z-index": (40)
			});		
			$(".colordialog2").hide();
			$("#"+dialogDivId).show();
		// var color = $("#"+colorId).val();
		// alert(color);
		 $('#'+farbtasticDivId).farbtastic('#'+colorTable);
		 // $("#"+valueId).css("background-color",$("#"+colorId).val());
		 // $("#"+colorId).val(color);
	}
	
	function updateRendererRule(formId,disabledFieldsArray){
	
	//var minNULLCount = 0;
	//var maxNULLCount = 0;
	var isTrue = 0;
	$(".min_value").each(function(k,v)
	{
		var minFlag = $(this).val() == null || $(this).val() == "";
		var maxFlag = $(".max_value").eq(k).val() == null || $(".max_value").eq(k).val() == "";
		var minFlagDB = disabledFieldsArray[k]['MIN_VALUE'];
		var maxFlagDB = disabledFieldsArray[k]['MAX_VALUE'];
		//minFlagDB和maxFlagDB同时为false
		if((!minFlagDB)&&(!maxFlagDB))
		{
			//如果只有一行，则最大值和最小值不能同时为空
			if(disabledFieldsArray.length == 1)
			{
				if(minFlag && maxFlag)
				{
					isTrue++;
					alert("最大值和最小值不能同时为空");
					return false;
				}
			}
			else
			{
				//前一项的最小值不能小于后一行的最大值
				if(k < $(".min_value").length-1 && (parseFloat($(this).val()) < parseFloat($(".max_value").eq(k+1).val())))
				{
					isTrue++;
					alert("前一项的最小值不能小于后一行的最大值");
					$(this).focus();
					return false;
				}
				//只能允许整个数组中的最小值为null或""
				if(minFlag && !(k==($(".min_value").length-1)))
				{
					isTrue++;
					alert("中间值不能为空");
					return false;
				}
				//只能允许整个数组中的最大值为null或""
				if(maxFlag && !(k==0))
				{
					isTrue++;
					alert("中间值不能为空");
					return false;
				}
			}
			//如果同一行中最大值和最小值都不为空，且最小值>最大值，则不允许提交
			if((!minFlag) && (!maxFlag) && (parseFloat($(this).val())>parseFloat($(".max_value").eq(k).val())))
			{
				isTrue++;
				alert("最小值不能大于最大值");
				$(this).focus();
				return false;
			}
			
			
		}
	});
			
	if(isTrue)
	{
		return false;
	}
	
	
	$(".colorstyle").each(function(k,v){
		var text = $(this).val();
		var color = $("#colorTable"+k).css("background-color");
		color = rgbToHex(color);
		text = eval("("+text+")");
		text.color = color;
		text = objectToStyleStr(text);
		$(this).val(text);
	});
	
		$("#"+formId).ajaxSubmit({
			url : 'updateOrAddrnoTrafficRendererAction',
			dataType : 'json',
			success : function(data) {
			// console.log(data);
				//alert(data.message);
				if(data.flag == true){
					// 清空所有的缓存规则
				    rendererFactory.clearAllRules();
				    
					$("#analyzeedit_Dialog").hide();
					$(".black").hide();
					$(".colordialog2").hide();
					var trafficCode = $("#trafficCode").val();
					var areaId=$("#"+formId).find("input[name=areaId]").val();
					//console.log("areaid is ="+areaId);
					// 更新颜色图例面板
					rendererFactory.findRule(trafficCode, areaId, showRendererRuleColor,true);
				}
				// 清空所有的缓存规则
				rendererFactory.clearAllRules();
				isRuleChanged=true;
			},
			complete : function() {
			$(".loading_cover").css("display", "none");
			var trafficCode = $("#trafficCode").val();
			var areaId=$("#"+formId).find("input[name=areaId]").val();
			// 更新颜色图例面板
			rendererFactory.findRule(trafficCode, areaId, showRendererRuleColor,true);
			}
		});
	}
	
	/**
	 * @author Liang YJ
	 * @param obj
	 * @returns {String}
	 * @date 2013.12.26 15:06
	 * 描述：将输入的对象转换成json字符串
	 */
	function objectToStyleStr(obj)
	{
		var json = "{";
		for(var key in obj)
		{
				if(key=="color")
				{
					json = json + key + ":'" + obj[key] + "',";
				}
				else
				{					
					json = json + key + ":" + obj[key] + ",";
				}
		}
		json = json.slice(0,json.length-1);
		json += "}";
		return json;
	}
	
