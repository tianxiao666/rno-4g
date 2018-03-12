function windowHeight() {
	var de = document.documentElement;
	return self.innerHeight || (de && de.clientHeight)
			|| document.body.clientHeight;
}
window.onload = window.onresize = function() {
	var wh = windowHeight();
	document.getElementById("map_c").style.height = (wh - 45) + "px";
}
$(document).ready(function() {
	$("#treeTd").click(function() {
		var obj = $("#switchPoint").attr("title");
		if (obj == "关闭") {
			$("#switchPoint").attr("title", "展开");
			$("#switchPoint").attr("src", "../images/pic24.gif");
			$("#hideTree").hide();
		} else {
			$("#switchPoint").attr("title", "关闭");
			$("#switchPoint").attr("src", "../images/pic23.gif");
			$("#hideTree").show();
		}
	});
	
	$("#zkai").click(function() {
		var obj = $("#zkai").attr("title");
		if (obj == "close") {
			$("#zkai").attr("title", "open");
			$("#demo1_table3").hide();
		} else {
			$("#zkai").attr("title", "close");
			$("#demo1_table3").show();
		}
	});
});     