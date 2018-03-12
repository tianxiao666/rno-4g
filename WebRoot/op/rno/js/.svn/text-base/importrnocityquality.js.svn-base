var failCnt = 0;// 连续获取进度失败的次数
var maxFailCnt = 5;// 最大允许失败的次数，超过将放弃获取
var interval=2000;//周期性获取进度情况的

$(document).ready(function() {
	$("#importBtn2").click(function() {
				//console.log("来来来");
				//console.log($("#cityindex").val());
				if($("#cityindex").val()==""){alert("文件不能为空");return;}
				if($(":radio:checked").val()==""){alert("单选按钮不能为空");return;}
				$("#table1").append("<tr><td class='menuTd' style='width: 20%'>结果：</td><td align='left'><div id='importResultDiv'></div></td></tr>");
				$("#conditionForm").ajaxSubmit({
							type : 'post',
							url : "uploadFileAjaxAction",
							dataType : 'text',
							success : function(data) {
								//console.log("success");
								$("#importBtn2").attr("disabled", "disabled");// 先禁用
								var obj ;
								try{
								   obj=eval("(" + data + ")");
								}catch(err){
									$('#formImportCell .canclear').clearFields();	
									$("#importResultDiv").html("上传文件失败！");
									window.location.href=window.location.href;
									return;
								}
								//alert(obj['token']);
								if (obj['token'] && obj['token']!='null') {
									$("#importResultDiv").html("正在解析文件...");
									queryProgress(obj['token']);
								}else{
									//token分配失败
									$("#importResultDiv").html("文件解析失败！");
									$("#importBtn").removeAttr("disabled");
								}
							},
							error : function(XmlHttpRequest, textStatus,
									errorThrown) {
//								alert("error" + textStatus + "------"
//										+ errorThrown);
									alert("error");
									$("#importResultDiv").html("文件解析失败！");
									$("#importBtn").removeAttr("disabled");
							},complete:function(){
							      $('#formImportCell .canclear').clearFields();	
							}
						});
				return true;
			});
});

function queryProgress(token) {
	$.ajax({
				url : 'queryUploadStatusAjaxAction',
				data : {
					'token' : token
				},
				type : 'post',
				dataType : 'text',
				success : function(data) {
					var obj;
					try{
					  obj=eval("(" + data + ")");
					}catch(err){
						$("#importResultDiv").html("服务器返回失败！");
						return;
					}
					//alert("data=="+data);
					if (obj['flag']) {
						$("#importResultDiv").html("文件正在解析，当前进度:"
								+ obj['progress'] + "%");
						var pronum = new Number(obj['progress']);
						//alert("progress:"+pronum);
						if (pronum >= 100) {
							//alert("get final result");
							// 文件解析完成
							getImportResult(token);// 获取结果
						} else {
							//alert("get progress again");
							//setTimeout("queryProgress(" + token + ")", 5000);
							window.setTimeout(function(){ queryProgress(token);},interval); 
						}
					} else {
						$("#importResultDiv").html("文件解析失败！原因:" + obj['msg']);
						getImportResult(token);// 获取结果
					}
				},
				error : function(err, status) {
					//alert("获取进度失败！");
					// 获取失败
					failCnt++;
					if (failCnt > maxFailCnt) {
						$("#importResultDiv").html("无法获取文件解析进度！");
						$("#importBtn").removeAttr("disabled");// 启用
					} else {
						window.setTimeout(function(){ queryProgress(token);},interval); 
					}
				}
			});
}

// 获取详细结果
function getImportResult(token) {
	$.ajax({
				url : 'getUploadResultAjaxAction',
				data : {
					'token' : token
				},
				type : 'post',
				dataType : 'text',
				success : function(data) {
					//$("#importResultDiv").html(data);
					//console.log(data);
					mes_obj=eval("(" + data + ")");
					
					result();
					// 上传按钮可用
				},
				error : function(err, status) {
					$("#importResultDiv").html("获取结果失败！");
				},
				complete : function() {
					$("#importBtn").removeAttr("disabled");// 启用
				}
			});
}
//增加标注信息[闭包函数]
var result=function addMymarkers(){

			for(var i=0;i<mes_obj.length;i++){ 
				(function(){
					//console.log("addMymarkers来人了");
					var marker1 = new BMap.Marker(new BMap.Point(mes_obj[i].btslng,mes_obj[i].btslat));  // 创建标注
					map.addOverlay(marker1);              // 将标注添加到地图中
					//创建信息窗口
					var infoWindow1 = new BMap.InfoWindow("基站名："+mes_obj[i].btsname+"</br>站点编号："+mes_obj[i].siteno+"</br>小区ID："+mes_obj[i].cellid+"</br>归属BSC："+mes_obj[i].bsc+"</br>经度："+mes_obj[i].btslng+"　纬度："+mes_obj[i].btslat+"</br>天线方向："+mes_obj[i].antbearing+"</br>颜色类型："+mes_obj[i].colortype+"</br>描述："+mes_obj[i].description);
					marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});
				})(i);
			}	
}