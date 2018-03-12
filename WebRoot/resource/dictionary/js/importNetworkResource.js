$(document).ready(function(){
	$('#importOne').click(function(){
	$("input[type='button']").attr("disabled",true);
		$('#demo1_table3').html("");
		if($('#file1').val() == null || $('#file1').val() == ''){
		$("input[type='button']").removeAttr("disabled");
			return ;
		}
		$("#loading")
		.ajaxStart(function(){
			$('#tishi').hide();
			$(this).show();
		})
		.ajaxComplete(function(){
			$(this).hide();
		});
		$.ajaxFileUpload
		(
			{
				url:'importNetworkResourceExcelTextOneAction',
				secureuri:false,
				fileElementId:'file1',
				dataType: 'json',
				success: function (data, status)
				{
					$('#tishi').show();
					$('#tishi').html();
					var member = eval(data);
					$('#tishi').html(decodeURI(member.message));
					$("input[type='button']").removeAttr("disabled");
					$("#tab_ul .ontab").click();
				}
				
			}
		)
	});
	
	$('#importSec').click(function(){
	$("input[type='button']").attr("disabled",true);
		$('#demo1_table3').html("");
		if($('#file2').val() == null || $('#file2').val() == ''){
		$("input[type='button']").removeAttr("disabled");
			return ;
		}
		$("#loading")
		.ajaxStart(function(){
			$('#tishi').hide();
			$(this).show();
		})
		.ajaxComplete(function(){
			$(this).hide();
		});
		$.ajaxFileUpload
		(
			{
				url:'importNetworkResourceExcelTextTwoAction',
				secureuri:false,
				fileElementId:'file2',
				dataType: 'json',
				success: function (data, status)
				{
					$('#tishi').show();
					$('#tishi').html();
					var member = eval(data);
					$('#tishi').html(decodeURI(member.message));
					$("input[type='button']").removeAttr("disabled");
					$("#tab_ul .ontab").click();
				}
				
			}
		)
	});
	
	$('#importThr').click(function(){
	$("input[type='button']").attr("disabled",true);
		$('#demo1_table3').html("");
		if($('#file3').val() == null || $('#file3').val() == ''){
		$("input[type='button']").removeAttr("disabled");
			return ;
		}
		$("#loading")
		.ajaxStart(function(){
			$('#tishi').hide();
			$(this).show();
		})
		.ajaxComplete(function(){
			$(this).hide();
		});
		$.ajaxFileUpload
		(
			{
				url:'importNetworkResourceExcelTextThrAction',
				secureuri:false,
				fileElementId:'file3',
				dataType: 'json',
				success: function (data, status)
				{
					$('#tishi').show();
					$('#tishi').html();
					var member = eval(data);
					$('#tishi').html(decodeURI(member.message));
					$("input[type='button']").removeAttr("disabled");
					$("#tab_ul .ontab").click();
				}
				
			}
		)
	});
	
	$('#importFou').click(function(){
	$("input[type='button']").attr("disabled",true);
		$('#demo1_table3').html("");
		if($('#file4').val() == null || $('#file4').val() == ''){
		$("input[type='button']").removeAttr("disabled");
			return ;
		}
		$("#loading")
		.ajaxStart(function(){
			$('#tishi').hide();
			$(this).show();
		})
		.ajaxComplete(function(){
			$(this).hide();
		});
		$.ajaxFileUpload
		(
			{
				url:'importNetworkResourceExcelTextFouAction',
				secureuri:false,
				fileElementId:'file4',
				dataType: 'json',
				success: function (data, status)
				{
					$('#tishi').show();
					$('#tishi').html();
					var member = eval(data);
					$('#tishi').html(decodeURI(member.message));
					$("input[type='button']").removeAttr("disabled");
					$("#tab_ul .ontab").click();
				}
				
			}
		)
	});
});