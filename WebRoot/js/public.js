// JavaScript Document
	$(document).ready(function(){
		
		$(":button,:submit").mousedown(function(){
			$(this).addClass("input_button_down");
			})
		$(":button,:submit").mouseup(function(){
			$(this).removeClass("input_button_down");
			})
		/* checkbox点击隐藏 */
		$("#fieldset-1 .container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");//为tab的第一个li加上左边框
		$("#fieldset-2 .container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");
		$("#fieldset-3 .container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");

		$(".container-tab1 fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".container-main").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".container-main").hide();
						}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".container-main").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".container-main").hide();
						}
				})
			})
			
			
		/* tab门 */
		$("#fieldset-1 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-1 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-1 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-1 .container-main-table1-tab").hide();
					$("#fieldset-1 .container-main-table1-tab").eq(index).show();
				})
			})
			
		$("#fieldset-2 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-2 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-2 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-2 .container-main-table1-tab").hide();
					$("#fieldset-2 .container-main-table1-tab").eq(index).show();
				})
			})
			
		$("#fieldset-3 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-3 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-3 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-3 .container-main-table1-tab").hide();
					$("#fieldset-3 .container-main-table1-tab").eq(index).show();
				})
			})

		/* 点击加减按钮收起 */
		
		
		$(".main-table1 .hide-show-img").click(function(){
			if($(this).attr("src") == "../../images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				$(this).attr("src","../../images/ico_hide.gif");
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
				$(this).attr("src","../../images/ico_show.gif");
				}
			})
		

			
						
			$(".hide_show_checkbox").click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
					}else{
						$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
						}
				})
		})

			
		
		$(document).ready(function(){
			$(".search-button").click(function(){
				$("#search-table").hide();
				$("#black").hide();
				})
			$(".search-no").click(function(){
			$("#search-table").hide();
			$("#black").hide();
			})
			$("#tree").treeview({
				collapsed: true,
				animated: "medium",
				control:"#sidetreecontrol",
				persist: "location"
			});
			$("#tree2").treeview({
				collapsed: true,
				animated: "medium",
				control:"#sidetreecontrol",
				persist: "location"
			});
			
			$(".main-table1 #gaojisousuo").click(function(){
				$("#black").show();
				$("#search-table").show();
			})

				
			
		})
		
		
