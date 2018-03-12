//步骤与进度提示
	function showProgress(){
		var hidImportEntityType = $("#hidImportEntityType").val();
		var fileContent = $("#importFile").val();
		var importModel = $("input[name='importModel']:checked").val();
		var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
		var isChecked = $("#chooseCheckBox").attr("checked");
		var selResChosenType = $("#selResChosenType").val();
		var linkAttributes = $("#linkAttributes").val();
		if(hidImportEntityType!="" && fileContent!="" && importModel!=""){
			if(importModel=="updateAdd" || importModel=="deleteAdd"){
				if(linkAttributes!=""){
					$("#progressEm1").css("color","blue").text("已完成");
				}else{
					$("#progressEm1").css("color","red").text("未完成");
				}
			}else{
					$("#progressEm1").css("color","blue").text("已完成");
			}
			
		 	if(isChecked=="checked"){
		 		if(selResChosenType!="请选择"){
		 			if(rdoAssModel=="exactContent"){
		 				if($("#txtExactContent").val()!=""){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
								}else{
									$("#progressDiv").addClass("progress_bar_color2");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
							}
					 		$("#progressEm2").css("color","blue").text("已完成");
					 		
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color1");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}else{
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#daoru").css("background","none").attr("disabled","disabled");
							}
					 		$("#progressEm2").css("color","red").text("未完成");
					 		
		 				}
		 			}else if(rdoAssModel=="exactMatch"){
		 				if($("#selAttrExactMatch").val()!="请选择"){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
								}else{
									$("#progressDiv").addClass("progress_bar_color2");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
							}
					 		$("#progressEm2").css("color","blue").text("已完成");
					 		
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color1");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}else{
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#progressDiv").addClass("progress_bar_color1");
									$("#daoru").css("background","none").attr("disabled","disabled");
							}
					 		$("#progressEm2").css("color","red").text("未完成");
					 		
		 				}
		 			}else if(rdoAssModel=="indistinctMatch"){
		 				if($("#selAttrIndistinctMatch").val()!="请选择"){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
								}else{
									$("#progressDiv").addClass("progress_bar_color2");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#progressDiv").addClass("progress_bar_color3");
									$("#daoru").css("background","powderblue").removeAttr("disabled");
							}
					 		$("#progressEm2").css("color","blue").text("已完成");
					 		
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		
					 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
								if(linkAttributes!=""){
									$("#progressDiv").addClass("progress_bar_color1");
									$("#daoru").css("background","none").attr("disabled","disabled");
								}else{
									$("#daoru").css("background","none").attr("disabled","disabled");
								}
							}else{
									$("#progressDiv").addClass("progress_bar_color1");
									$("#daoru").css("background","none").attr("disabled","disabled");
							}
					 		$("#progressEm2").css("color","red").text("未完成");
					 		
		 				}
		 			}else{
		 				$("#progressDiv").removeClass("progress_bar_color1");
				 		$("#progressDiv").removeClass("progress_bar_color2");
				 		$("#progressDiv").removeClass("progress_bar_color3");
				 		
				 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
							if(linkAttributes!=""){
								$("#progressDiv").addClass("progress_bar_color1");
								$("#daoru").css("background","none").attr("disabled","disabled");
							}else{
								$("#daoru").css("background","none").attr("disabled","disabled");
							}
						}else{
								$("#progressDiv").addClass("progress_bar_color1");
								$("#daoru").css("background","none").attr("disabled","disabled");
						}
				 		$("#progressEm2").css("color","red").text("未完成");	
				 		
		 			}
		 		}else{
		 			$("#progressDiv").removeClass("progress_bar_color1");
			 		$("#progressDiv").removeClass("progress_bar_color2");
			 		$("#progressDiv").removeClass("progress_bar_color3");
			 		//$("#progressDiv").addClass("progress_bar_color1");
			 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
						if(linkAttributes!=""){
							$("#progressDiv").addClass("progress_bar_color1");
							$("#daoru").css("background","none").attr("disabled","disabled");
						}else{
								$("#daoru").css("background","none").attr("disabled","disabled");
						}
					}else{
							$("#progressDiv").addClass("progress_bar_color1");
							$("#daoru").css("background","none").attr("disabled","disabled");
					}
			 		$("#progressEm2").css("color","red").text("未完成");
			 		
		 		}
		 	}else{
		 		$("#progressDiv").removeClass("progress_bar_color1");
		 		$("#progressDiv").removeClass("progress_bar_color2");
		 		$("#progressDiv").removeClass("progress_bar_color3");
		 	//	$("#progressDiv").addClass("progress_bar_color3");
		 		if(importModel=="updateAdd" || importModel=="deleteAdd"){
					if(linkAttributes!=""){
						$("#progressDiv").addClass("progress_bar_color3");
						$("#daoru").css("background","powderblue").removeAttr("disabled");
					}else{
						$("#progressDiv").addClass("progress_bar_color2");
						$("#daoru").css("background","none").attr("disabled","disabled");
					}
				}else{
						$("#progressDiv").addClass("progress_bar_color3");
						$("#daoru").css("background","powderblue").removeAttr("disabled");
				}
		 		$("#progressEm2").css("color","blue").text("不关联");
		 	}
		}else{
			$("#progressEm1").css("color","red").text("未完成");
			$("#daoru").css("background","none").attr("disabled","disabled");
			if(isChecked=="checked"){
		 		if(selResChosenType!="请选择"){
		 			if(rdoAssModel=="exactContent"){
		 				if($("#txtExactContent").val()!=""){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressDiv").addClass("progress_bar_color2");
					 		$("#progressEm2").css("color","blue").text("已完成");
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressEm2").css("color","red").text("未完成");
		 				}
		 			}else if(rdoAssModel=="exactMatch"){
		 				if($("#selAttrExactMatch").val()!="请选择"){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressDiv").addClass("progress_bar_color2");
					 		$("#progressEm2").css("color","blue").text("已完成");
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressEm2").css("color","red").text("未完成");
		 				}
		 			}else if(rdoAssModel=="indistinctMatch"){
		 				if($("#selAttrIndistinctMatch").val()!="请选择"){
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressDiv").addClass("progress_bar_color2");
					 		$("#progressEm2").css("color","blue").text("已完成");
		 				}else{
		 					$("#progressDiv").removeClass("progress_bar_color1");
					 		$("#progressDiv").removeClass("progress_bar_color2");
					 		$("#progressDiv").removeClass("progress_bar_color3");
					 		$("#progressEm2").css("color","red").text("未完成");
		 				}
		 			}else{
		 				$("#progressDiv").removeClass("progress_bar_color1");
				 		$("#progressDiv").removeClass("progress_bar_color2");
				 		$("#progressDiv").removeClass("progress_bar_color3");
				 		$("#progressEm2").css("color","red").text("未完成");
		 			}
		 		}else{
		 			$("#progressDiv").removeClass("progress_bar_color1");
			 		$("#progressDiv").removeClass("progress_bar_color2");
			 		$("#progressDiv").removeClass("progress_bar_color3");
			 		$("#progressEm2").css("color","red").text("未完成");
		 		}
		 	}else{
		 		$("#progressDiv").removeClass("progress_bar_color1");
		 		$("#progressDiv").removeClass("progress_bar_color2");
		 		$("#progressDiv").removeClass("progress_bar_color3");
		 		$("#progressDiv").addClass("progress_bar_color2");
		 		$("#progressEm2").css("color","blue").text("不关联");
		 	}
		 	
		}
	}