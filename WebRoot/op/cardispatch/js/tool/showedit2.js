
var reData = "";
function showedit ( divSelector ) {

		var instance = this;

		var allShowSpan = $(divSelector + " *[showSpan]");
		var allEditInput = $(divSelector + " *[editInput]");
		var editBtn = $(divSelector + " *[editBtn]");
		var saveBtn = $(divSelector + " *[saveBtn]");
		var cancelBtn = $(divSelector + " *[cancelBtn]");
		var showBtn = $(divSelector + " *[showBtn]");
		var editButton = $(divSelector + " *[editButton]");
		var img = $(divSelector + " *[showImg]");
		var editDiv = $(divSelector + " *[editDiv]");
		
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
							var opt = $(edit).find("option[value='" + $(this).text().replace(" ","") + "']");
							if ( $(opt).length == 0 ) {
								opt = $(edit).find("option:contains('" + $(this).text().replace(" ","") + "')");
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
						var opt = $(edit).find("option[value='" + replaceAll($(this).text() , " " , "" ) + "']");
						if ( $(opt).length == 0 ) {
							opt = $(edit).find("option:contains('" + replaceAll($(this).text() , " " , "" ) + "')");
						}
						$(opt).attr({"selected":"selected"});
					} else if ( $(edit).is("textarea") ) {
						$(edit).val($(this).text().replace(" ",""));
					} else if ( !$(edit).is(":file") ) {
						$(edit).val($(this).text());
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
					txt = $(this).find("option:selected").text();
				} else {
					txt = $(this).val();
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
		
		
		function replaceAll ( str , b , c ) {
			while ( str.indexOf(b) != -1 ) {
				str = str.replace(b,c);
			}
		}
		
		
		return instance;
	}