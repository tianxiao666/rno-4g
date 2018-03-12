	
	/**
	 * 自动补全插件
	 * @author andy 2012.5.22
	 * @version 1.0
	 * Copy Right Information : iscreate
	 * Project : operationservice
	 * Modification history :2012.5.22
 	 */
	
	/**
	 * 根据当前登录用户，ajax查询车辆列表(自动补全)
	 * @author andy
	 * opt -- 参数
	 */
	function customAutoComplete ( opt ) {
		var selectIndex = 0;			//当前选中索引
	
		//获取参数
		var divId = opt.divId;
		var inputSelector = opt.inputSelector;
		var optUrl = opt.url;
		var showMethodName = opt.showMethodName;
		var clickMethodName = opt.clickMethodName
		var eventName = opt.eventName;
		var params = opt.params;
		var width;
		if ( !opt.width || opt.width == null || opt.width == "" ) {
			width = $(inputSelector).innerWidth() ;
		} else {
			width = opt.width;
		}
		
		
		var liCount = 0;							//显示总数
		
		$( ":not(#"+divId+"_outer)" ).click(function(){
			$("#"+divId).remove();
		})
		
		
		$(inputSelector).live(eventName,function ( $event ) {
		
			if ( eventName == "keyup" ) {
				var keyCode = $event.keyCode;
				if ( keyCode == 40 || keyCode == 38 ) {
					var divLi = $(divId + " li");
					if (divLi.length > 0) {
						if ( keyCode == 38 ) {
							selectIndexUpOrDown("up");
						} else if ( keyCode == 40 ) {
							selectIndexUpOrDown("down");
						}
					}
					$event.preventDefault();
					return;
				}
			}
		
			var div = $("#"+divId+"_outer");
			if ( div.length == 0 ) {
				div = $("<div/>").css({"display":"inline", "position":"relative","overflow":"hidden","background-color":"red"}).attr("id",divId+"_outer");
				//var nextLabel = $(inputSelector).next();
				$(inputSelector).wrap($(div));
			}
			$("#"+divId).remove();
			var innerDiv = $("<div/>").attr("id",divId).css({"z-index":"500" , "position":"absolute" , "left":"0px" , "width" : width + "px" , "top": "32px",  "display" : "none" ,"max-height": "200px" , "overflow-y" : "auto"});
			$(innerDiv).appendTo($(div));
			$(innerDiv).insertAfter($(this));
			$(innerDiv).appendTo($(this).parent());
			$(innerDiv).css({"cursor":"pointer"});
			var dataObj = {};
			for ( var key in params ) {
				var paramSelector = params[key];
				dataObj[key] = $(paramSelector).val();
			}
			$.ajax({
				url : optUrl ,
				type : "post" , 
				data : dataObj , 
				success : function ( data ) {
					data = eval (data);
					$(innerDiv).css(  "display" , "none" );
					liCount = data.length;
					$(data).each(function(index){
						var info = this;
						var li = $("<li/>").css({"list-style-type":"none" , "magin" : "0px" , "padding" : "0px" , "width" : "100%" , "background-color":"white" });
						$(li).appendTo($(innerDiv));
						
						
						$(li).bind( "mouseover" , info , function ($event) {
							selectIndex = index;
							selectChange();
							eval(clickMethodName + "(" + obj2String($event.data) + ")");
						});
						
						var showText = eval ( showMethodName + "(" + obj2String(info) + ")" );
						$(li).text(showText);
						
						$(li).bind( "click" , info , function ($event) {
							eval(clickMethodName + "(" + obj2String($event.data) + ")");
							$(innerDiv).empty();
						});
						$(inputSelector).focus();
					});
					selectIndex = 0;
					selectChange();
					$(innerDiv).slideDown("fast");
				}
			});
		});
		
		/**
		 * 选择改变
		 */
		function selectChange () {
			var selectLi = $("#" + divId + " li" )[selectIndex];
			$("#" + divId + " li" ).css({"color":"black" , "background-color" : "white"});
			$(selectLi).css({"color":"white" , "background-color" : "red"});
		}
		
		/**
		 * 键盘上下操作
		 */
		function selectIndexUpOrDown ( opera ) {
			if ( opera == "up" ) {
				selectIndex--;
			} else if ( opera == "up") {
				selectIndex++;
			}
			if ( selectIndex >= liCount ) {
				selectIndex = 0 ;
				$("#" + divId + " li" )[0].focus();
			} else if ( selectIndex < 0 ) {
				selectIndex = liCount - 1 ;
				$("#" + divId + " li" )[liCount - 1].focus();
			}
			selectChange();
		}
		
		
	}