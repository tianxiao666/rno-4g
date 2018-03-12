$(document).ready(function(){
	$("#changePasswordForm").validate({
		rules:{
		   oldPassword:"required",
		   newPassword:"required",
		   again:{
		         required:true,
		         equalTo:'#pwd1'
		   }
		},
		submitHandler : function(form) {
				 $(form).ajaxSubmit({
					 success:function(d){
					    var data=eval("("+d+")");
					    form.reset();
					    if(data['flag']==true){
					    	alert("修改成功");
					    }else{
					    	alert("修改失败！原因："+data['msg']);
					    }
					 }
				 });
			}
	});
});