var allCheckType = [ 'checknull', 'checklength', 'checkajax', 'checkregex',
		'checkmail' , 'checkdatecompare' , 'checkopera' , 'checkchinese' , 'checkphone' , 'checkidcard' , 'checknumber' ];
var allKeyCheckType = [ 'number', 'dnumber', 'snumber' , 'peculiar' ];
var checkmailRegex = /^([A-z]{3}[.])?\w+@\w+[.]\w{2,3}([.]\w{2,3})?$/;

function formcheck(opt) {
	/*参数*/
	var defaultOpt = { //默认选项		
		"notNullEm" : "*",
		"notNullEmColor" : "red",
		"isAjax" : false,
		"ajaxSuccess" : function(data) {} ,
		"formSubmiting" : function(){} , 
		"showLoading" : false
	};

	var formId; //表单Id
	var formName; //表单名
	var subBtn; //提交按钮
	var isAjax; //是否ajax提交
	var ajaxMethod = ""; //ajax提交后的方法
	var notNullEm; //非空标记
	var notNullEmColor; //标记颜色
	var isShowLoading;
	var formSubmiting;
	
	{
		defaultOpt = $.extend(defaultOpt, opt);
		formId = opt.form;
		formName = $(formId).attr("id");
		isAjax = opt.isAjax;
		subBtn = opt.subButton;
		notNullEm = defaultOpt.notNullEm;
		notNullEmColor = defaultOpt.notNullEmColor;
		formSubmiting = defaultOpt.formSubmiting;
		isShowLoading = defaultOpt.showLoading;
	}
	//添加标记
	addStar();

	
	//添加提示消息
	addPromptInfo();

	//提交按钮点击事件
	$(subBtn).click(function($event) {
		var pros = $(formId).find("[promtInfo]");
		$(pros).each(function(){
			var ip = this;
			var v = $(ip).val().replace(" ","");
			var msg = $(ip).attr("promtInfo");
			if ( v == "" || v == msg ) {
				$(ip).val( "" );
			}
		});
		$(formId).find("." + formName + "_span").remove();
		var flag = true; //验证结果
		var typeFlag = true; //验证类型，验证结果
		var inputsSelector = list2AttrSelector(allCheckType);
		var allCheckInput = $(formId).find(inputsSelector);
		$(allCheckInput).each(function() {
			var input = this;
			$(allCheckType).each(function(index) {
				var type = allCheckType[index];
				var typeInfo = $(input).attr(type);
				if (typeInfo) {
					var pd = checkTheWidget(input, type);
					if (!pd) {
						flag = false;
						return false;
					}
				}
			});
		});
		//判断是否验证成功
		if (flag) {
			//判断是否ajax提交
			if (formSubmiting) {
				var submiting_flag = formSubmiting();
				var flag = Boolean(submiting_flag);
				if ( typeof(submiting_flag) ==  "undefind" || submiting_flag == null || flag == true ) {
				} else if ( submiting_flag == false ) {
					return false;
				} else {
					return false;
				}
			}
			//显示loading画面
			if ( isShowLoading ) {
				showLoading();
			}
			if (isAjax) {
				$(formId).ajaxSubmit(function(data) {
					defaultOpt.ajaxSuccess(data);
					deleteLoading();
				});
			} else {
				$(formId).submit();
				if ( isShowLoading ) {
					deleteLoading();
				}
			}
		} else {
			$event.stopPropagation();
			$(pros).blur();
		}
	});

	//删除提示span
	$(formId).find("[removeMsgSpan]").click(function() {
		$(formId).find("." + formName + "_span").remove();
	});

	/******************* check method start ********************/

	/**
	 * 控件验证
	 * widget 要验证的控件
	 * checkType 要验证的类型
	 */
	function checkTheWidget(widget, checkType) {
		var msg = $(widget).attr(checkType);
		var checkInput = widget;
		var checkTarget = widget;
		if ( $(checkInput).attr("checktarget") && $(checkInput).attr("checktarget") != "" ) {
			checkTarget = $(formId).find($(checkInput).attr("checktarget"));
		}
		//判断验证类型，并进行相应验证
		var flag = false;

		if (checkType == 'checknull') {
			flag = checkNull( checkInput , checkTarget );
		} else if (checkType == 'checkajax') {
			flag = checkAjax( checkInput , checkTarget );
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
		} else if (checkType == 'checkregex') {
			flag = checkRegex( checkInput , checkTarget );
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
		} else if (checkType == 'checkmail') {
			flag = checkMail(checkInput);
		} else if (checkType == 'checklength') {
			flag = checkLength( checkInput , checkTarget );
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
			msg = replaceAll(msg, "[min]", obj.minLength);
			msg = replaceAll(msg, "[max]", obj.maxLength);
		} else if ( checkType == 'checkdatecompare' ) {
			flag = checkDateCompare( checkInput , checkTarget );
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
		} else if ( checkType == 'checkopera' ) {
			flag = checkOpera( checkInput , checkTarget );
			var msg = $(widget).attr(checkType);
		} else if ( checkType == 'checkchinese' ) {
			flag = checkChinese( checkInput , checkTarget );
			var msg = $(widget).attr(checkType);
		} else if ( checkType == 'checkphone' ) {
			flag = checkPhone( checkInput , checkTarget );
			var msg = $(widget).attr(checkType);
		} else if ( checkType == 'checkidcard' ) {
			flag = checkIdentify( checkInput , checkTarget );
			var msg = $(widget).attr(checkType);
		} else if ( checkType == 'checknumber' ) {
			flag = checkNumber( checkInput , checkTarget );
			var msg = $(widget).attr(checkType);
		}

		//弹出提示
		if (!flag) {
			fadeInSpan(widget, msg);
		}
		inputClickRemoveSpan(checkInput, widget);

		return flag;
	}
	
	
	/**
	 * 验证数字
	 * @param {Object} widget - 控件
	 * @param {Object} checkTarget - 控件
	 */
	function checkNumber ( widget , checkTarget ) {
		var value = $(checkTarget).val();
		var flag = true;
		var reg = /^\d+$/;
		if ( reg.test(value) ) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
	
	
	
	/**
	 * 验证电话
	 * @param {Object} widget - 控件
	 * @param {Object} checkTarget - 控件
	 */
	function checkPhone ( widget , checkTarget ) {
		var value = $(checkTarget).val();
		var flag = true;
		var reg = /^(\d{4}-\d{7}|\d{3}-\d{8}|\d{11}|\d{8})$/;
		if ( reg.test(value) ) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 验证身份证
	 * @param {Object} widget - 控件
	 * @param {Object} checkTarget - 控件
	 */
	function checkIdentify ( widget , checkTarget ) {
		var value = $(checkTarget).val();
		var flag = true;
		var reg = /^\d{17}[\d|X|x]|\d{14}[\d|X|x]$/;
		if ( reg.test(value) ) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 验证中文
	 * @param {Object} widget - 控件
	 * @param {Object} checkTarget - 控件
	 */
	function checkChinese ( widget , checkTarget ) {
		var value = $(checkTarget).val();
		var flag = true;
		var reg = /[^\u4e00-\u9fa5]/;
		if ( reg.test(value) ) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 验证是否存在符号
	 * @param {Object} widget - 控件
	 * @param {Object} checkTarget - 控件
	 * @return {TypeName} boolean - 是否存在符号
	 */
	function checkOpera ( widget , checkTarget ) {
		var value = $(checkTarget).val();
		var flag = true;
		for( var i = 0 ; i < value.length ; i++ ) {
			var c = value.charAt(i);
			if ( c == "%" || 
				 c == "|" || 
				 c == "*" || 
				 c == "/" || 
				 c == "-" || 
				 c == "+" ||
				 c == "#" ||
				 c == "￥" || 
				 c == "&" || 
				 c == "!" || 
				 c == "！" || 
				 c == "=" || 
				 c == "^" || 
				 c == "(" || 
				 c == ")" || 
				 c == "—" || 
				 c == "、" || 
				 c == "‘" || 
				 c == "’" || 
				 c == "：" || 
				 c == ":" || 
				 c == ";" || 
				 c == "?" || 
				 c == "<" || 
				 c == ">" || 
				 c == "《" || 
				 c == "》" || 
				 c == "?" || 
				 c == "？" || 
				 c == "." || 
				 c == "。" || 
				 c == "," || 
				 c == "\\" || 
				 c == "~" || 
				 c == "@" ) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	
	/**
	 * 验证日期比较
	 * widget 需要验证的控件
	 */
	function checkDateCompare ( widget , checkTarget ) {
		var obj = $(widget).attr("checkdatecompare");
		obj = eval( "(" + obj + ")" );
		var target = $(obj.target);
		var comp = obj.compare;
		var targetTxt1 = "";
		if ( $(target).is("input") ) {
			targetTxt1 = $(target).val();
		} else {
			targetTxt1 = $(target).text();
		}
		var comparetarget = $(obj.comparetarget);
		var targetTxt2 = "";
		if ( $(comparetarget).is("input") ) {
			targetTxt2 = $(comparetarget).val();
		} else {
			targetTxt2 = $(comparetarget).text();
		}
		var flag = false;
		if ( comp == "moreThan" ) {
			flag = targetTxt1 > targetTxt2;
		} else if ( comp == "lessThan") {
			flag = targetTxt1 < targetTxt2;
		}
		return flag;
	}


	/**
	 * 验证邮箱
	 * widget 需要验证的控件
	 */
	function checkLength( widget , checkTarget ) {
		var len = $(checkTarget).attr("checklength");
		var obj = eval("(" + len + ")");
		var minLen = obj.minLength;
		var maxLen = obj.maxLength;
		var v = $(checkTarget).val().replace(" ","");
		return v.length <= maxLen && v.length >= minLen;
	}

	/**
	 * 验证邮箱
	 * widget 需要验证的控件
	 */
	function checkMail( widget , checkTarget ) {
		var v = $(checkTarget).val().replace(" ","");
		return checkmailRegex.test(v);
	}

	/**
	 * 验证非空
	 * widget 需要验证的控件
	 */
	function checkNull( widget , checkTarget ) {
		var v = $(checkTarget).val();
		v = replaceAll( v , " " , "" );
		var promtInfo = $(widget).attr("checknull");
		if (v == "" || v == promtInfo ) {
			return false;
		}
		return true;
	}

	/**
	 * 验证ajax
	 * widget 需要验证的控件
	 */
	function checkAjax( widget , checkTarget ) {
		var flag = true;
		var obj = $(widget).attr("checkajax");
		obj = eval("(" + obj + ")");
		var paramName = $(checkTarget).attr("name");
		var paramValue = $(checkTarget).val().replace(" ","");
		var opt = new Object();
		opt[paramName] = paramValue;
		if ( !obj ){
			return false;
		}
		var urlString = obj.url;
		var paramString = obj.param;
		if (paramString) {
			var param = eval(paramString);
			if (param != null) {
				for ( var key in param) {
					opt[key] = $(param[key]).val();
				}
			}
		}
		if ( urlString && urlString != null && urlString != "" ) {
			//ajax操作
			$.ajax({
				type : 'POST',
				async : false,
				url : urlString,
				data : opt , 
				dataType : "text",
				success : function(data) {
					flag = !(data == "true");
				}
			});
		} else if ( obj.method ) {
			var m = eval(obj.method);
			flag = m();
		} else {
			return false;
		}
		return flag;
	}

	/**
	 * 验证正则表达式
	 * widget 需要验证的控件
	 */
	function checkRegex( widget , checkTarget ) {
		var r = $(widget).attr("checkregex");
		var obj = stringToObj(r);
		var reg = eval(obj.reg);
		var v = $(checkTarget).val().replace(" ","");
		var flag = reg.test(v);
		return flag;
	}

	/******************* check method end ********************/

	/**
	 * 弹出提示
	 * widget 需要验证的控件
	 * msg  错误提示语句
	 */
	function fadeInSpan(widget, msg) {
		var span = $("<span/>").attr("class", formName + "_span").text(msg)
				.css( {
					"display" : "none",
					"color" : "red"
				});
		var wid = $(widget).next(".formcheckspan");
		if (wid != null && wid.length > 0) {
			$(widget).next(".formcheckspan").after(span);
		} else {
			$(widget).after(span);
		}
		$(span).fadeIn(1000);
	}

	/**
	 * 为控件，添加删除提示的click事件
	 * clickInputSelector 促发点击事件控件
	 * selectInputSelector 选择控件
	 */
	function inputClickRemoveSpan(clickInputSelector, selectInputSelector) {
		$(clickInputSelector).focus(
				function() {
					var wid = $(selectInputSelector).next(".formcheckspan");
					if (wid != null && wid.length > 0) {
						$(selectInputSelector).next(".formcheckspan").next(
								"." + formName + "_span").remove();
					} else {
						$(selectInputSelector).next("." + formName + "_span")
								.remove();
					}
				});
	}

	/**
	 * 添加必填标记
	 */
	function addStar() {
		$(formId).find("input[checknull]").after(
				"<span class='formcheckspan' style='color:" + notNullEmColor
						+ ";'>" + notNullEm + "</span>");
	}
	
	//添加提示
	function addPromptInfo () {
		var pros = $(formId).find("[promtInfo]");
		$(pros).each(function( index ){
			var input = this;
			var color = "black";
			if ( $(input).css("color") ) {
				color = $(input).css("color");
			}
			var msg = $(input).attr("promtInfo");
			$(input).blur(function(){
				var v = $(this).val().replace(" ","");
				if ( v == "" || v == msg ) {
					$(input).val( msg );
					$(input).css({"color":"#cccccc"})
				} else {
					$(input).css({"color":color})
				}
			});
			
			$(input).focus(function(){
				var v = $(this).val().replace(" ","");
				if ( v == "" || v == msg ) {
					$(input).val( "" );
					$(input).css({"color":color})
				}
			});
			$(input).blur();
		});
	}

	//显示loading画面
	function showLoading () {
		var div = $("body");
		//删除已存在loading
		$(div).find(".new_formcheck_loading").remove();
		//创建loading
		var new_formcheck_loading = $("<div class='new_formcheck_loading'/>");
		var top = $(div).offset().top;
		var left = $(div).offset().left;
		var width = $(div).width();
		var height = $(div).height();
		//创建图片
		var img = $("<img/>").attr({"src":"images/formcheck_loading.gif"}).css({
			"width" : "50px" , 
			"height" : "50px"
		}).appendTo($(new_formcheck_loading));
		var imgWidth = $(img).width();
		var imgHeight = $(img).height();
		var paddingLeft = (width / 2 ) - ( imgWidth / 2 );
		var paddingTop = (height / 2 ) - ( imgHeight / 2 );
		$(new_formcheck_loading).css({
			"z-index" : 999999 , 
			"position" : "absolute" , 
			"top" : top+"px" , 
			"left" : left+"px" , 
			"width" : width + "px" , 
			"height" : height + "px" , 
			"background-color" : "white" , 
			"opacity" : "0.5" , 
			"padding-left" : paddingLeft+"px" , 
			"padding-top" : paddingTop+"px"
		});
		$("body").append($(new_formcheck_loading));
	}
	
	//删除loading
	function deleteLoading () {
		var div = $("body");
		$(div).find(".new_formcheck_loading").remove();
	}

	/*****************工具*****************/
	function replaceAll(str, ov, nv) {
		var txt = str + "";
		for (; txt.indexOf(ov) != -1;) {
			txt = txt.replace(ov, nv);
		}
		return txt;
	}

	function stringToObj(str) {
		var obj = eval("(" + str + ")");
		return obj;
	}

	function list2AttrSelector(list) {
		var str = "";
		for ( var i = 0; i < list.length; i++) {
			var type = list[i];
			if (i != 0) {
				str += ",";
			}
			str += "input[" + type + "]";
		}
		return str;
	}

	//获取剪切板
	function getClipboard() {
		if (window.clipboardData) {
			return (window.clipboardData.getData('text'));
		} else {
			if (window.netscape) {
				try {
					netscape.security.PrivilegeManager
							.enablePrivilege("UniversalXPConnect");
					var clip = Components.classes["@mozilla.org/widget/clipboard;1"]
							.createInstance(Components.interfaces.nsIClipboard);
					if (!clip) {
						return;
					}
					var trans = Components.classes["@mozilla.org/widget/transferable;1"]
							.createInstance(Components.interfaces.nsITransferable);
					if (!trans) {
						return;
					}
					trans.addDataFlavor("text/unicode");
					clip.getData(trans, clip.kGlobalClipboard);
					var str = new Object();
					var len = new Object();
					trans.getTransferData("text/unicode", str, len);
				} catch (e) {
					alert("您的firefox安全限制限制您进行剪贴板操作，请打开'about:config'将signed.applets.codebase_principal_support'设置为true'之后重试，相对路径为firefox根目录/greprefs/all.js");
					return null;
				}
				if (str) {
					if (Components.interfaces.nsISupportsWString) {
						str = str.value
								.QueryInterface(Components.interfaces.nsISupportsWString);
					} else {
						if (Components.interfaces.nsISupportsString) {
							str = str.value
									.QueryInterface(Components.interfaces.nsISupportsString);
						} else {
							str = null;
						}
					}
				}
				if (str) {
					return (str.data.substring(0, len.value / 2));
				}
			}
		}
		return null;
	}
	
	function setClipboard(maintext) {
	    if (window.clipboardData) 
	     {
	        return (window.clipboardData.setData("Text", maintext));
	     } 
	    else 
	     {
	        if (window.netscape) 
	         {
	            try{
	             netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
	            var clip = Components.classes["@mozilla.org/widget/clipboard;1"].createInstance(Components.interfaces.nsIClipboard);
	            if (!clip) 
	             {
	                return;
	             }
	            var trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable);
	            if (!trans) 
	             {
	                return;
	             }
	             trans.addDataFlavor("text/unicode");
	            var str = new Object();
	            var len = new Object();
	            var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
	            var copytext = maintext;
	             str.data = copytext;
	             trans.setTransferData("text/unicode", str, copytext.length * 2);
	            var clipid = Components.interfaces.nsIClipboard;
	            if (!clip) 
	             {
	                return false;
	             }
	             clip.setData(trans, null, clipid.kGlobalClipboard);
	            return true;
	             }
	            catch(e)
	             {
	                 alert("您的firefox安全限制限制您进行剪贴板操作，请打开'about:config'将signed.applets.codebase_principal_support'设置为true'之后重试，相对路径为firefox根目录/greprefs/all.js");
	                return false;
	             }
	         }
	     }
	    return false;
	}

}




/***************************************** checkValue 验证 begin *********************************************/



function checkValue ( opt ) {
	/*参数*/
	var defaultOpt = { //默认选项		
		"notNullEm" : "*",
		"notNullEmColor" : "red",
		"isAjax" : false,
		"ajaxSuccess" : function(data) {}
	};

	var formId; //表单Id
	var formName; //表单名
	var subBtn; //提交按钮
	var isAjax; //是否ajax提交
	var ajaxMethod = ""; //ajax提交后的方法
	var notNullEm; //非空标记
	var notNullEmColor; //标记颜色

	{
		defaultOpt = $.extend(defaultOpt, opt);
		formId = opt.form;
		if ( !opt.formName ) {
			formName = $(formId).attr("id");
		} else {
			formName = opt.formName;
		}
		
		isAjax = opt.isAjax;
		subBtn = opt.subButton;
		notNullEm = defaultOpt.notNullEm;
		notNullEmColor = defaultOpt.notNullEmColor;
		var formSubmiting = defaultOpt.formSubmiting;
	}
	
	//提交按钮点击事件
	$(subBtn).click(function($event) {
		var pros = $(formId).find("[promtInfo]");
		$(pros).each(function(){
			var ip = this;
			var v = $(ip).val().replace(" ","");
			var msg = $(ip).attr("promtInfo");
			if ( v == "" || v == msg ) {
				$(ip).val( "" );
			}
		});
		
		$(formId).find("." + formName + "_span").remove();
		var flag = true; //验证结果
		var typeFlag = true; //验证类型，验证结果
		var inputsSelector = list2AttrSelector(allCheckType);
		var allCheckInput = $(formId).find(inputsSelector);
		$(allCheckInput).each(function() {
			var input = this;
			$(allCheckType).each(function(index) {
				var type = allCheckType[index];
				var typeInfo = $(input).attr(type);
				if (typeInfo) {
					var pd = checkTheWidget(input, type);
					if (!pd) {
						flag = false;
						return false;
					}
				}
			});
		});
		/*
		//判断是否验证成功
		if (flag) {
			//判断是否ajax提交
			if (formSubmiting) {
				formSubmiting();
			}
			if (isAjax) {
				$(formId).ajaxSubmit(function(data) {
					defaultOpt.ajaxSuccess(data);
				});
			} else {
				//$(formId).submit();
			}
		} else {
			$event.stopPropagation();
			$(pros).blur();
		}*/
	});
	
	
	/**
	 * 为控件，添加删除提示的click事件
	 * clickInputSelector 促发点击事件控件
	 * selectInputSelector 选择控件
	 */
	function inputClickRemoveSpan(clickInputSelector, selectInputSelector) {
		$(clickInputSelector).focus(
				function() {
					var wid = $(selectInputSelector).next(".formcheckspan");
					if (wid != null && wid.length > 0) {
						$(selectInputSelector).next(".formcheckspan").next(
								"." + formName + "_span").remove();
					} else {
						$(selectInputSelector).next("." + formName + "_span")
								.remove();
					}
				});
	}
	
	/**
	 * 控件验证
	 * widget 要验证的控件
	 * checkType 要验证的类型
	 */
	function checkTheWidget(widget, checkType) {
		var msg = $(widget).attr(checkType);
		var checkInput = widget;
		var checkAjaxInput = widget;
		var promtInfo = $(checkInput).attr("checknull");
		if ($(checkInput).attr("checktarget")
				&& $(checkInput).attr("checktarget") != "") {
			promtInfo = $(checkInput).attr("checknull");
			checkInput = $(formId).find($(checkInput).attr("checktarget"));
		}
		//判断验证类型，并进行相应验证
		var flag = false;

		if (checkType == 'checknull') {
			flag = checkNull(checkInput,promtInfo);
		} else if (checkType == 'checkajax') {
			flag = checkAjax(checkAjaxInput);
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			if ( flag ) {
				if ( obj.smsg ) {
					msg = obj.smsg;
				} else {
					msg = "";
				}
			} else {
				msg = obj.msg;
			}
		} else if (checkType == 'checkregex') {
			flag = checkRegex(checkInput);
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
		} else if (checkType == 'checkmail') {
			flag = checkMail(checkInput);
		} else if (checkType == 'checklength') {
			flag = checkLength(checkInput);
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
			msg = replaceAll(msg, "[min]", obj.minLength);
			msg = replaceAll(msg, "[max]", obj.maxLength);
		} else if ( checkType == 'checkdatecompare' ) {
			flag = checkDateCompare(checkInput);
			var obj = eval("(" + $(widget).attr(checkType) + ")");
			msg = obj.msg;
		}

		//弹出提示
		if (!flag) {
			fadeInSpan(widget, msg);
		} else if ( checkType == 'checkajax' && msg != "" ) {
			fadeInSpanS(widget, msg);
		}
		inputClickRemoveSpan(checkInput, widget);

		return flag;
	}

	/**
	 * 验证日期比较
	 * widget 需要验证的控件
	 */
	function checkDateCompare ( widget ) {
		var obj = $(widget).attr("checkdatecompare");
		obj = eval( "(" + obj + ")" );
		var target = $(obj.target);
		var comp = obj.compare;
		var targetTxt1 = "";
		if ( $(target).is("input") ) {
			targetTxt1 = $(target).val();
		} else {
			targetTxt1 = $(target).text();
		}
		var comparetarget = $(obj.comparetarget);
		var targetTxt2 = "";
		if ( $(comparetarget).is("input") ) {
			targetTxt2 = $(comparetarget).val();
		} else {
			targetTxt2 = $(comparetarget).text();
		}
		var flag = false;
		if ( comp == "moreThan" ) {
			flag = targetTxt1 > targetTxt2;
		} else if ( comp == "lessThan") {
			flag = targetTxt1 < targetTxt2;
		}
		return flag;
	}


	/**
	 * 验证邮箱
	 * widget 需要验证的控件
	 */
	function checkLength(widget) {
		var len = $(widget).attr("checklength");
		var obj = eval("(" + len + ")");
		var minLen = obj.minLength;
		var maxLen = obj.maxLength;
		var v = $(widget).val().replace(" ","");
		return v.length <= maxLen && v.length >= minLen;
	}

	/**
	 * 验证邮箱
	 * widget 需要验证的控件
	 */
	function checkMail(widget) {
		var v = $(widget).val().replace(" ","");
		return checkmailRegex.test(v);
	}

	/**
	 * 验证非空
	 * widget 需要验证的控件
	 */
	function checkNull(widget,promtInfo) {
		var v = $(widget).val().replace(" ","");
		if (v == "" || v == promtInfo ) {
			return false;
		}
		return true;
	}

	/**
	 * 验证ajax
	 * widget 需要验证的控件
	 */
	function checkAjax(widget) {
		var flag = true;
		var obj = $(widget).attr("checkajax");
		obj = eval("(" + obj + ")");
		var paramName = $(widget).attr("name");
		var paramValue = $(widget).val().replace(" ","");
		var opt = new Object();
		opt[paramName] = paramValue;
		var urlString = obj.url;
		var paramString = obj.param;
		if (paramString) {
			var param = eval(paramString);
			if (param != null) {
				for ( var key in param) {
					opt[key] = $(param[key]).val();
				}
			}
		}
		//ajax操作
		$.ajax( {
			type : 'POST',
			async : false,
			url : urlString,
			data : opt,
			dataType : "text",
			success : function(data) {
				flag = !(data == "true");
			}
		});
		return flag;
	}

	/**
	 * 验证正则表达式
	 * widget 需要验证的控件
	 */
	function checkRegex(widget) {
		var r = $(widget).attr("checkregex");
		var obj = stringToObj(r);
		var reg = eval(obj.reg);
		var v = $(widget).val().replace(" ","");
		var flag = reg.test(v);
		return flag;
	}
	
	
	/**
	 * 弹出提示
	 * widget 需要验证的控件
	 * msg  错误提示语句
	 */
	function fadeInSpan(widget, msg) {
		var span = $("<span/>").attr("class", formName + "_span").text(msg)
				.css( {
					"display" : "none",
					"color" : "red"
				});
		var wid = $(widget).next(".formcheckspan");
		if (wid != null && wid.length > 0) {
			$(widget).next(".formcheckspan").after(span);
		} else {
			$(widget).after(span);
		}
		$(span).fadeIn(1000);
	}
	
	/**
	 * 弹出成功提示
	 * widget 需要验证的控件
	 * msg  错误提示语句
	 */
	function fadeInSpanS(widget, msg) {
		var span = $("<span/>").attr("class", formName + "_span").text(msg)
				.css( {
					"display" : "none",
					"color" : "green"
				});
		var wid = $(widget).next(".formcheckspan");
		if (wid != null && wid.length > 0) {
			$(widget).next(".formcheckspan").after(span);
		} else {
			$(widget).after(span);
		}
		$(span).fadeIn(1000);
	}
	
	/*****************工具*****************/
	function replaceAll(str, ov, nv) {
		var txt = str + "";
		for (; txt.indexOf(ov) != -1;) {
			txt = txt.replace(ov, nv);
		}
		return txt;
	}

	function stringToObj(str) {
		var obj = eval("(" + str + ")");
		return obj;
	}

	function list2AttrSelector(list) {
		var str = "";
		for ( var i = 0; i < list.length; i++) {
			var type = list[i];
			if (i != 0) {
				str += ",";
			}
			str += "input[" + type + "]";
		}
		return str;
	}
}




/***************************************** checkValue 验证 end *********************************************/




$(document).ready(function(){
	//添加键盘事件
	addKeyEvent();
})



/******************** keybroad event start ********************/

	function addFocus () {
		$("input,select,textarea").focusin(function(){
			$(this).css({"box-shadow":"1px 1px 2px 2px #ccc","border-radius":"3px"});
		});
		$("input,select,textarea").focusout(function(){
			$(this).css({"box-shadow":"0px 0px 0px 0px #ccc"});
		});
	}


	/**
	 * 添加键盘事件
	 * formSelector 表单选择器
	 */
	function addKeyEvent() {
		$(allKeyCheckType).each(function(index) {
			var type = this;
			var eventWidget = $("body").find("[" + type + "]");
			$(eventWidget).each(function(index) {
				if (type == "number") {
					numberControl(this);
				} else if (type == "dnumber") {
					dnumberControl(this);
				} else if (type == "snumber") {
					snumberControl(this);
				} else if ( type == "peculiar" ) {
					peculiarControl(this);
				}

			});
		});
	}

	/**
	 * 添加数字控制事件
	 */
	function numberControl(widget) {
		var ev = $(widget).attr("number");
		$(widget).bind(ev, function($event) {
				var v = $(this).val().replace(" ","");
				if ( ( $event.which == 118 && $event.ctrlKey ) || ($event.which == 120 && $event.ctrlKey)
						|| ($event.which == 99 && $event.ctrlKey)) {
					return true;
				}
				if (($event.which == 8 || $event.which == 0)
						|| ($event.which == 58 && v.length > 0)) {
					return true;
				}
				if ($event.which > 58 || $event.which < 48) {
					return false;
				}
			});
		$(widget).bind("blur",function($event) {
							var v = $(this).val().replace(" ","");
							if (v.length > 1) {
								var first = v.substring(0, 1);
								if (first == "0") {
									for ( var first = v.substring(0, 1); first == "0"; first = v.substring(0, 1)) {
										v = v.substring(1);
									}
									$(this).val(v);
								}
							}
							v = isNaN(parseInt($(this).val().replace(" ",""))) ?"":parseInt($(this).val().replace(" ",""));
							$(this).val( v );
						});
	}

	/**
	 * 添加数字控制事件
	 */
	function dnumberControl(widget) {
		var ev = $(widget).attr("dnumber");
		$(widget).bind(ev,function($event) {
							var v = $(this).val().replace(" ","");
							var pointIndex = v.indexOf('.');
							//组合键
							if (($event.which == 118 && $event.ctrlKey)
									|| ($event.which == 120 && $event.ctrlKey)
									|| ($event.which == 99 && $event.ctrlKey)) {
								return true;
							}
							if (($event.which == 8 || $event.which == 0)
									|| ($event.which == 46 && pointIndex == -1 && v.length > 0)) {
								return true;
							}
							if ($event.which > 58 || $event.which < 48) {
								return false;
							}
						});

		$(widget).bind("blur", function($event) {
			var v = $(this).val().replace(" ","");
			if (v == "") {
				return;
			}
			v = isNaN(parseFloat($(this).val().replace(" ",""))) ?"":parseFloat($(this).val().replace(" ",""));
			$(this).val(v);
		});
	}

	/**
	 * 添加数字控制事件
	 */
	function snumberControl(widget) {
		var ev = $(widget).attr("snumber");
		$(widget).bind(
				ev,
				function($event) {
					var v = $(this).val().replace(" ","");
					var pointIndex = v.indexOf('.');
					//组合键
					if (($event.which == 118 && $event.ctrlKey) 
						|| ($event.which == 120 && $event.ctrlKey) 
						|| ($event.which == 99 && $event.ctrlKey)) {
						return true;
					}
					if ($event.which == 8 || $event.which == 0) {
						return true;
					}
					if ($event.which > 58 || $event.which < 48) {
						return false;
					}
				});
	}
	
	/**
	 * 添加特殊符号控制事件
	 */
	function peculiarControl ( widget ) {
		var ev = $(widget).attr("peculiar");
		$(widget).bind(
				ev,
				function($event) {
					var v = $(this).val().replace(" ","");
					if ($event.which == 95 || $event.which == 37 || $event.which == 32 ) {
						return false;
					}
				});
		$(widget).bind(
				"blur",
				function($event) {
					var v = replaceAll($(this).val()," ","");
					v = replaceAll_form(v,"%","");
					v = replaceAll_form(v,"_","");
					$(this).val(v);
				});
	}

	
	
	//提示层
	function promptCueDiv ( widget , cueMsg ) {
		//获取id
		var widget_id = $(widget).attr("id");
		if ( !widget_id || widget_id == "" || widget_id == null ) {
			alert("promptCueDiv--> 控件id不能为空");
			return;
		}
		//创建div
		var cue_div = $("<div/>").attr({ "id" : widget_id + "_div" });
		$(widget).after($(cue_div));
		$(cue_div).text(cueMsg);
		//设置div样式
		var left = $(widget).width() * (-1) - 5 ;
		var top = $(widget).offset().top+5;
		var height = $(widget).height();
		var displaystring = "none";
		
		var txt = $(widget).val();
		txt = replaceAll(txt," ","");
		txt = replaceAll(txt,"	","");
		if ( txt != "" ) {
			displaystring = "none";
		} else {
			displaystring = "inline-block";
		}
		$(cue_div).css({
			"margin-left" : left , 
			"position" : "absolute" , 
			"color" : "#cccccc" , 
			"width" : "auto" , 
			"height" : height+"px" , 
			"z-index" : 999 , 
			"display" : displaystring , 
			"font-size" : (height-1)+"px" , 
			"margin-top" : "5px"
		});
		
			
		
		//记录颜色
		$(widget).focus( { "d" : $(cue_div) , "msg" : cueMsg , "w" : widget } , function(_ev){
			var msg = _ev.data.msg;
			var d = _ev.data.d;
			var c = _ev.data.c;
			var w = _ev.data.w;
			var v = $(w).val();
			if ( v == "" || v == msg ) {
				$(d).css({"display":"none"});
				$(this).val("");
			}
		});
		$(widget).blur( { "d" : $(cue_div) , "msg" : cueMsg } , function(_ev){
			var msg = _ev.data.msg;
			var d = _ev.data.d;
			var c = _ev.data.c;
			var v = $(this).val().replace(" ","");
			if ( v == "" || v == msg ) {
				$(d).css({"display":"inline-block"});
				$(this).val("");
			} else {
				$(d).css({"display":"none"});
			}
		});
		$(cue_div).click( {"d" : $(cue_div) , "msg" : cueMsg , "w" : widget } , function(_ev){
			var msg = _ev.data.cueMsg;
			var c = _ev.data.c;
			var w = _ev.data.w;
			var d = _ev.data.d;
			var v = $(w).val().replace(" ","");
			if ( v == "" || v == msg ) {
				$(w).val("");
				$(d).css({"display":"none"});
				$(w).focus();
			}
		});
	}
	
	function replaceAll ( str , b , c ) {
		while ( str.indexOf(b) != -1 ) {
			str = str.replace(b,c);
		}
		return str;
	}
	function replaceAll_form ( str , reg , replacement ) {
		var subCount = reg.length;
		for ( var i = 0 ; (i+subCount-1) < str.length ; i+= subCount ) {
			var s = str.substring( i , (i + subCount) );
			if ( reg == s ) {
				str = str.substring(0,i) + replacement + str.substring( ( i + subCount ) );
				i = i + replacement.length - 1 ;
			}
		}
		return str;
	}
	