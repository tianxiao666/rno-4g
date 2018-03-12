
function loginAction() {
	var username = document.getElementById("username").value;
	var passwd = document.getElementById("passwd").value;
	var checkcode = document.getElementById("checkcode").value;
	var alt = "";
	if (username == "" ) {
		alt += "用户名不能为空!\n";		
	}
	if (passwd == "" ) {
		alt += "密码不能为空!\n";		
	}
	if (checkcode == "" ) {
		alt += "验证码不能为空!\n";		
	}
	if(alt!=""){
		alert(alt);
		return false;
	}
	document.getElementById("loginForm").submit();
}
$(document).keydown(function (event) { 
	var username = document.getElementById("username").value;
	var passwd = document.getElementById("passwd").value;
		//alert(event.keyCode);
		//回车键事件邦定
	if (event.keyCode == 13) {
		if (username != "" && passwd != "") {
			loginAction();
		}
	}
});
$(function () {
	$("#username").focus();
});

