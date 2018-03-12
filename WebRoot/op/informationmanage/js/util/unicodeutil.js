//unicode转字符
function unicode2Chr(str) {
	if ("" != str) {
		var st, t, i;
		st = "";
		for (i = 1; i <= str.length / 4; i++) {
			t = str.slice(4 * i - 4, 4 * i - 2);
			t = str.slice(4 * i - 2, 4 * i).concat(t);
			st = st.concat("%u").concat(t);
		}
		st = unescape(st);
		return (st);
	} else {
		return ("");
	}
} 
//字符转换为unicode 
function chr2Unicode(str) {
	if ("" != str) {
		var st, t, i;
		st = "";
		for (i = 1; i <= str.length; i++) {
			t = str.charCodeAt(i - 1).toString(16);
			if (t.length < 4) {
				while (t.length < 4) {
					t = "0".concat(t);
				}
			}
			t = t.slice(2, 4).concat(t.slice(0, 2));
			st = st.concat(t);
		}
		return (st.toUpperCase());
	} else {
		return ("");
	}
}
var http_request = false;
function getRequest(url) {
	http_request = false;
	if (window.XMLHttpRequest) { // Mozilla, Safari,... 
		http_request = new XMLHttpRequest();
		if (http_request.overrideMimeType) {
			http_request.overrideMimeType("text/xml");
		}
	} else {
		if (window.ActiveXObject) { // IE 
			try {
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			}
			catch (e) {
				try {
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e) {
				}
			}
		}
	}
	if (!http_request) {
		return false;
	}
	http_request.onreadystatechange = showtlinfo;
	http_request.open("GET", url, true);
	http_request.send(null);
}

