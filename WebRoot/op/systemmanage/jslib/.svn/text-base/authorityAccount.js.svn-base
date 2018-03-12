$(document).ready(function(){
	
	$("#account_add_form").validate( {
		errorPlacement:function(error,element){
		   error.appendTo(element.parent());	
		},
		
		submitHandler : function(form) {
//			var password1 = $.trim($("#accountPassword1").val());
//			var password2 = $.trim($("#accountPassword2").val());
//
//			//
//			if (password1 == "") {
//				alert("请输入账号密码");
//				return;
//			}
//			if (password1 != password2) {
//				alert("两次输入密码请保持一致");
//				return;
//			}

//			//存在中文的正则表达式
//			var re = /[\u4E00-\u9FA5]/g; 
//			
//			//正确的邮箱格式表达式
//			var reg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;
//				///^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
//					
//			//正确的手机号表达式
//			var parten=/^-?\d+$/;
//			//^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
//			
//			var account = $("#newaccount").val();
//			var name = $("#newname").val();
//			var email = $("#newemail").val();
//			var bakcemail = $("#newbakcemail").val();
//			var cellphone = $("#newcellphone").val();
//			var mobileemail = $("#newmobileemail").val();
//			
//			
//			//验证账号
//			if(re.test(account)){
//				errorStr = "账号不能有中文!";
//				alert(errorStr);
//				return false;
//			}
//			//验证名字
//			name = name.replace(/\s+/g,"");
//			if(name==""){
//				alert("名字不能为空!");
//				return false;
//			}
//			
//			//验证邮箱
//			email = email.replace(/\s+/g,"");
//			if(email==""){
//				alert("邮箱不能为空!");
//				return false;
//			}
//			if(!reg.test(email)){
//				alert("邮箱格式不正确!");
//				return false;
//			}
//				
//				
//			
//			//验证备用邮箱
//			bakcemail = bakcemail.replace(/\s+/g,"");
//			if(bakcemail==""){
//				
//			}else if(!reg.test(bakcemail)){
//				alert("备用邮箱格式不正确!");
//				return false;
//			}
//			
//			
//			//验证手机号
//			cellphone = cellphone.replace(/\s+/g,"");
//			if(cellphone==""){
//				alert("手机号码不能为空!");
//				return false;
//			}
//			if(!parten.test(cellphone)){
//				alert("手机号码非法!");
//				return false;
//			}
//					
//			
//			//验证手机邮箱
//			
//			mobileemail = mobileemail.replace(/\s+/g,"");
//			if(mobileemail==""){
//				alert("手机邮箱不能为空!");					
//				return false;
//			}
//			if(!reg.test(mobileemail)){
//				alert("手机邮箱格式不正确!");
//				return false;
//			}
//	
			
			//
			
			var form = $("#account_add_form");
			var status = 0;
			if ($("#accountStatus").attr("checked") == "checked") {
				status = 1;
			}
			form.ajaxSubmit( {
						url : 'saveNewAccountForAjaxAction',
						data : {
							'account.status' : status
						},
						type : 'post',
						success : function(d) {
							var data = eval("(" + d
									+ ")");
							if (data['flag'] == true) {
								alert("添加成功");
								
								//追加一条记录到账号表格
								var obj = data['msg'];
								
								var htmlStr = "<tr>";
								htmlStr += "<td>"
										+ obj['account']
										+ "</td>";
								htmlStr += "<td>"
										+ obj['name']
										+ "</td>";
								htmlStr += "<td>"
										+ obj['emailAddress']
										+ "</td>";
								htmlStr += "<td>"
										+ obj['cellPhoneNumber']
										+ "</td>";
								htmlStr += "<td>"
										+ obj['time_range_begin']
										+ "</td>";
								if (obj['status'] == "1") {
									htmlStr += "<td>有效</td>";
								} else {
									htmlStr += "<td>无效</td>";
								}
								htmlStr += "<td><a style=\"cursor:pointer\" onclick=\"viewAccountDetail('"
										+ obj['account']
										+ "');\">查看详情</a>&nbsp;<input type='button' onclick='regeneratorPassword(\""+obj['account']+"\") value=\"重置密码\"/></td>";
								htmlStr += "</tr>";
								
								$("#account_list_tb")
										.append($(htmlStr));

								
								//隐藏添加框
								$("#accountAdd_Dialog")
										.css("display",
												"none");
								$(".black").hide();
								//进行跳转
								viewAccountDetail(obj['account']);
							} else {
								alert("添加失败。原因："
										+ data['msg']);
							}
						}
					});
		}
	});
//
//	//确认添加
//	$("#confirmAddAccount").click(function() {
//		saveNewAccount();
//		//		return false;
//	});
	//取消增加按钮
		$("#cancelAddAccount").click(function(){
			$(".dialog_closeBtn").trigger("click");
			return false;
		});
});

function viewAccountDetail(userId){
//	alert("obj");
//    var form=$(obj).parent("form");
//    var userId=$(form).children("input:first").val();
//    alert("userId="+userId)
//	form.get(0).submit();
	window.open('viewAccountDetailAction?userId='+userId,'blank_');
//	window.location.href='viewAccountDetailAction?userId='+userId;
//	$.ajax({
//		url:'viewAccountDetailAction',
//		type:'post',
//		data:{'userId':userId},
//		success:function(d){
//			
//		}
//	});
}

///**
// * 保存新添加的账号信息
// */
//function saveNewAccount(){
//	
//
//	
//	//判断2次输入密码是否一致
//	var password1=$.trim($("#accountPassword1").val());
//	var password2=$.trim($("#accountPassword2").val());
//	
//	//
//	if(password1==""){
//		alert("请输入账号密码");
//		return ;
//	}
//	if(password1!=password2){
//		alert("两次输入密码请保持一致");
//		return ;
//	}
//	
//	
//	//准备数据
//	var form=$("#account_add_form");
//	var status=0;
//	if($("#accountStatus").attr("checked")=="checked"){
//		status=1;
//	}
//	
//	form.ajaxSubmit({
//		url:'saveNewAccountForAjaxAction',
//		data:{'account.status':status},
//		type:'post',
//		success:function(d){
//			var data=eval("("+d+")");
//			if(data['flag']==true){
//				alert("添加成功");
//				//追加一条记录到账号表格
//				var obj=data['msg'];
//				var htmlStr="<tr>";
//				htmlStr+="<td>"+obj['account']+"</td>";
//				htmlstr+="<td>"+obj['name']+"</td>";
//				htmlStr+="<td>"+obj['emailAddress']+"</td>";
//				htmlStr+="<td>"+obj['cellPhoneNumber']+"</td>";
//				htmlStr+="<td>"+obj['time_range_begin']+"</td>";
//				if(obj['status']=="1"){
//					htmlStr+="<td>有效</td>";
//				}else{
//					htmlStr+="<td>无效</td>";
//				}
//				htmlStr+="<td><a style=\"cursor:pointer\" onclick=\"viewAccountDetail('"+obj['account']+"');\">查看详情</a></td>";
//				htmlStr+="</tr>";
//				$("#account_list_tb").append($(htmlStr));
//				
//				//TODO 进行跳转
//				
//				//隐藏添加框
//				$("#accountAdd_Dialog").css("display","none");
//				$(".black").hide();
//			}else{
//				alert("添加失败。原因："+data['msg']);
//			}
//		}
//	});
//}

function cancelAddAccount(){
	$("#accountAdd_Dialog").css("display","none");
	$(".black").hide();
	return false;
}

/**
 * 重置密码
 */
function regeneratorPassword(userId){
	$.ajax({
		url:'reGeneratorOneUserPasswordForAjaxAction',
		type:'post',
		data:{'userId':userId},
		success:function(d){
		alert("修改成功");
		}
	})
}

/**
 * 重置所有未更改过的密码
 */
function resetAllNotModifyPassword(){
	$.ajax({
		url:'resetAllNotModifedPasswordForAjaxAction',
		type:'post',
		success:function(d){
		alert("修改成功");
		}
	})
}