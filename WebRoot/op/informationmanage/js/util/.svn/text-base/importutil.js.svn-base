
//自动匹配导入js或css
function importList(urlsetting) {
	for (var i = 0; i < urlsetting.length; i++) {
		var url = urlsetting[i];
		var expand = url.substring(url.lastIndexOf(".") + 1);
		if (expand == "css") {
			importCss(url);
		} else {
			if (expand == "js") {
				importJs(url);
			}
		}
	}
}

//导入js
function importJs(url) {
	if (typeof url == "string" || url instanceof String) {
        s = document.createElement("script");
        s.type = "text/javascript";
        s.src = url;
        var head = document.getElementsByTagName("head")[0];
    	head.appendChild(s);
	} else {
		if (url instanceof Array) {
			for (var i = 0; i < url.length; i++) {
				var u = url[i];
				importJs(u + "");
			}
		}
	}
}

//导入css
function importCss(url) {
	if (typeof url == "string" || url instanceof String) {
		var s = document.createElement("link");
		s.rel = "stylesheet";
		s.type = "text/css";
		s.href = url;
		var head = document.getElementsByTagName("head")[0];
    	head.appendChild(s);
	} else {
		if (url instanceof Array) {
			for (var i = 0; i < url.length; i++) {
				var u = url[i];
				importCss(u + "");
			}
		}
	}
	
}

