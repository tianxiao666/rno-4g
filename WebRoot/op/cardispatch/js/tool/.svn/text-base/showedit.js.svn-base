
var reData = "";
function showedit ( divSelector ) {

		var instance = this;

		var allShowSpan = $("*[showSpan]");
		var allEditInput = $("*[editInput]");
		var editBtn = $("*[editBtn]");
		var saveBtn = $("*[saveBtn]");
		var cancelBtn = $("*[cancelBtn]");
		var showBtn = $("*[showBtn]");
		var editButton = $("*[editButton]");
		var img = $("*[showImg]");
		var editDiv = $("*[editDiv]");
		
		changeBtn("cancel");
		
		$(editBtn).live("click",function(){
			$(allShowSpan).each(function(){
				var ss = this;
				var edit = $(this).next("[editDiv]");
				if ( edit && $(edit).length > 0 ) {
					if ( $(ss).is("img") ) {
						$(edit).find("[editImg]").attr({"src":$(ss).attr("src")});
					} else  {
						edit = $(edit).find("[editInput]");
						if ( $(edit).is("select") ) {
							var opt = $(edit).find("option[value='" + replaceAll($(this).text() , " " , "" ) + "']");
							if ( $(opt).length == 0 ) {
								opt = $(edit).find("option:contains('" + replaceAll($(this).text() , " " , "" ) + "')");
							}
							$(opt).attr({"selected":"selected"});
						} else if ( $(edit).is("textarea") ) {
							$(edit).val($(this).text().replace(" ",""));
						} else if ( !$(edit).is(":file") ) {
							$(edit).val($(this).text().replace(" ",""));
						}
					}
				} else {
					edit = $(this).next("[editInput]");
					if ( $(edit).is("select") ) {
						var v = replaceAll($(this).val() , " " , "" );
						v = replaceAll(v , "	" , "" );
						var opt = $(edit).find("option[value='" + v + "']");
						if ( $(opt).length == 0 ) {
							opt = $(edit).find("option:contains('" + replaceAll($(this).text() , " " , "" ) + "')");
						}
						$(opt).attr({"selected":"selected"});
					} else if ( $(edit).is("textarea") ) {
						$(edit).val($(this).text().replace(" ",""));
					} else if ( !$(edit).is(":file") ) {
						$(edit).val($(this).text());
					} else {
						
					}
				}
			});			
			changeBtn("edit");
		});
		
		$(saveBtn).live("click",function(){
			$(allEditInput).each(function(){
				var txt = "";	
				if ( $(this).is(":file") ) {
					return true;
				} else if ( $(this).is("select") ) {
					txt = $(this).find("option:selected").text().replace(" ","");
					txt = replaceAll(txt , " " , "" )
				} else {
					txt = $(this).val();
					//txt = replaceAll(txt , " " , "" )
				}
				var show = $(this).prev("[showSpan]");
				$(show).text(txt);
			});	
			$(divSelector + " form").ajaxSubmit(function($data){
				reData = $data;
				$("[editDiv]").each(function(index){
					var editImg = $(this).prev();
					var div_img = $(this).find("[editImg]");
					if ( $(editImg).attr("showImg") && $(div_img).length > 0 ) {
						var src = div_img.attr("src");
						$(editImg).attr("src",src);
					}
				});
				
			});
			changeBtn("save");
		});
		
		
		$(cancelBtn).live("click",function(){
			$(allEditInput).each(function(){	
				var show = $(this).prev("[showSpan]");
			});		
			changeBtn("cancel");
		});
		
		
		function changeBtn ( opera ) {
			if ( opera == "edit" ) {
				$(allShowSpan).css("display","none");
				$(showBtn).css("display","none");
				//$(allEditInput).css("display","inline");
				$(allEditInput).slideDown(300);
				$(editDiv).fadeIn(800);
				
				$(editBtn).css("display","none");
				//$(saveBtn).css("display","inline");
				//$(cancelBtn).css("display","inline");
				$(saveBtn).fadeIn(500);
				$(cancelBtn).fadeIn(500);
				
				//$(editButton).css("display","inline");
				$(editButton).fadeIn(500);
				$(allEditInput).blur();
			} else if ( opera == "save" || opera == "cancel" ) {
				$(allEditInput).css("display","none");
				
				$(editDiv).css("display","none");
				$(allShowSpan).css("display","inline");
				$(showBtn).css("display","inline");
				
				$(editBtn).css("display","inline");
				$(saveBtn).css("display","none");
				$(cancelBtn).css("display","none");
				
				//$(editButton).css("display","none");
				$(editButton).css("display","none");
			}
		}
		
		
		function replaceAll ( str , reg , replacement ) {
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
		
		
		return instance;
	}