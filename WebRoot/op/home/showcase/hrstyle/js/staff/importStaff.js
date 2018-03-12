$(document).ready(function(){
	$('#daoru').click(function(){
		$('#demo1_table3').html("");
		if($('#file').val() == null || $('#file').val() == ''){
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
				url:'importStaffAction',
				secureuri:false,
				fileElementId:'file',
				dataType: 'json',
				success: function (data, status)
				{
					$('#tishi').show();
					$('#tishi').html();
					var member = eval(data);
					$('#tishi').html(decodeURI(member.message));
				}
				
			}
		)
	});
});