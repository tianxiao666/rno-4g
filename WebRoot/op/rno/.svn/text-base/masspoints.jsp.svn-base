<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>热区+自定义图层</title>
<%@include file="commonheader.jsp"%>
<style type="text/css">
.triangle_contianer {
	position: relative;
	background: #FFE4CA;
	color: #000;
	border: 4px solid #F90;
	padding: 5px;
}

.triangle_border {
	width: 0;
	position: absolute;
	left: 50%;
	bottom: -24px;
	margin-left: -12px;
	border: 12px solid transparent;
	border-top-color: #F90;
}

.triangle {
	left: 50%;
	bottom: -12px;
	margin-left: -6px;
	border: 6px solid transparent;
	border-top-color: #FFE4CA;
}

.arrow {
	left: 30px;
	top: 320px;
	width: 0;
	height: 0;
	font-size: 0;
	border-width: 16px;
	border-style: dashed dashed solid dashed;
	border-color: transparent transparent red transparent;
}

.circle {
	width: 100px;
	height: 100px;
	background: red;
	border-radius: 50px;
}

.smallele {
	width: 5px;
	height: 5px;
	border: 1px solid #A9A9F5;
	position: absolute;
}

.divwrap {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 1200px;
	height: 500px;
	border: 1px dashed;
	display: block;
	background-color: none;
	background-color: transparent;
	pointer-events: none;
	z-index: 2
}
</style>

</head>
<body>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div style="padding-bottom: 0px" class="map_hd">
					<div style="padding-bottom: 0px" class="head_box clearfix">
						<div class="search_result">
							<div style="padding: 5px">
								<form method="post" id="conditionForm">
									省：<select id="provinceId" class="required" name="provinceId">
										<%-- option value="-1">请选择</option --%>
										<option value="82">广东省</option>
									</select> 市：<select id="cityId" class="required" name="cityId">
									</select> 区：<select id="queryCellAreaId" class="required" name="areaId">
									</select>
								</form>
								<div style="display:none" id="hiddenAreaLngLatDiv"></div>
							</div>
						</div>
					</div>
				</div>

			</div>
			<%-- 地图--%>
			<div style="padding-top: 0px">
				<div class="map_bd">
					<div id="map" style="width:1000px;height:500px"></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var wholeX = 0;
		var wholeY = 0;
		var mapCont = document.getElementById("map");
		var offset=getElementAbsPos(mapCont);
		wholeX = offset.left;
		wholeY =offset.top;
		console.log("map cont:left=" + wholeX + ",top=" + wholeY);
		var doms = new Array();
		var domPoints = new Array();
		var visibles = new Array();
		var hiddens = new Array();
		var curzoom = 15;

		var point = new BMap.Point(113.278972, 23.130828);
		var map = new BMap.Map("map");
		map.centerAndZoom(point, 15); // 初始化地图,设置中心点坐标和地图级别。
		map.enableScrollWheelZoom();

		var onMoving = false;
		var oriE = null;
		$(document).ready(function() {

			$(".moveable1").on("mousedown", function(e) {
				onMoving = true;
				oriE = e;
				//console.log(e.pageX + "," + e.pageY);
			});
			//	$(document).on("mouseup", function() {
			//	onMoving = false;
			//		console.log("mouseup");
			//});
			$(".moveable1").on("mousemove", function(e) {
				if (onMoving == true) {
					var target = $(e.target);
					//console.log(e.pageX + "," + e.pageY);
					var dy = e.pageY - oriE.pageY;
					var dx = e.pageX - oriE.pageX;
					console.log("dx=" + dx + ",dy=" + dy);
					var ctop = target.css("top");
					var cleft = target.css("left");
					ctop = ctop.substring(0, ctop.length - 2);
					cleft = cleft.substring(0, cleft.length - 2);
					var newtop = (new Number(ctop) + dy) + "px";
					var newleft = (new Number(cleft) + dx) + "px";
					target.css("top", newtop);
					target.css("left", newleft);
					oriE = e;
				}
			});

			//createMapElement();

			$(".smallele").live("click", function(e) {

			});
		});

		map.addEventListener("click", function(e) {
			console.log(e.point.lng + "," + e.point.lat);
		});

		map.addEventListener("moveend", function(e) {
			console.log("moveend....");
			//$(".smallele").css("display","none");
			var bounds = map.getBounds();
			visibles.splice(0, visibles.length);
			hiddens.splice(0, hiddens.length);
			for ( var i = 0; i < doms.length; i++) {
				console.log("move : i=" + i);
				movetorightPlace(doms[i].dom, doms[i].point);
				if (!bounds.containsPoint(doms[i].point)) {
					doms[i].hide();
					hiddens.push(doms[i]);
				} else {
					doms[i].show();
					visibles.push(doms[i]);
				}
			}
		});

		map.addEventListener("zoomend", function(e) {
			console.log("zoomend....");
			//$(".smallele").css("display","none");
			var bounds = map.getBounds();

			var newzoom = map.getZoom();
			var arr = null;
			if (curzoom < newzoom) {
				arr = visibles;
				//放大,仅重新计算可见范围的
				for ( var i = 0; i < arr.length; i++) {
					console.log("move : i=" + i);
					movetorightPlace(arr[i].dom, arr[i].point);
					//$(".smallele").css("display","");
					if (!bounds.containsPoint(arr[i].point)) {
						arr[i].hide();
						arr.splice(i, 1);
						hiddens.push(arr[i]);
						i--;
					}
				}
			} else {
				//缩小
				arr = visibles;
				for ( var i = 0; i < arr.length; i++) {
					movetorightPlace(arr[i].dom, arr[i].point);
				}
				arr = hiddens;
				console.log("hiddens lenght=" + arr.length);
				for ( var i = 0; i < arr.length; i++) {
					if (bounds.containsPoint(arr[i].point)) {
						movetorightPlace(arr[i].dom, arr[i].point);
						arr[i].show();
						arr.splice(i, 1);
						visibles.push(arr[i]);
						i--;
					}

				}
			}
			curzoom = newzoom;
		});

		function createMapElement() {
			var startLng = 113.243399;
			var endLng = 113.314401;
			var startLat = 23.115141;
			var endLat = 23.147112;

			var curLng = startLng;
			var curLat = startLat;
			var dy = (endLat - startLat) / 4;
			var dx = (endLng - startLng) / 10;
			var bounds = map.getBounds();
			var row = 0;
			var col = 0;
			while (curLng < endLng) {
				row++;
				curLat = startLat;
				while (curLat < endLat) {
					col++;
					var dom = document.createElement("div");
					dom.setAttribute("class", "smallele");
					//document.getElementById("info").appendChild(dom);
					document.body.appendChild(dom);
					//doms.push(dom);
					var p = new BMap.Point(curLng, curLat);
					//domPoints.push(p);
					var wrap = new MapDomWrap(dom, p);
					wrap.addEventListener("click", domclick);
					doms.push(wrap);
					movetorightPlace(dom, p);
					map.addOverlay(new BMap.Marker(p));
					curLat += dy;

					if (!bounds.containsPoint(p)) {
						dom.style.display = "none";
						hiddens.push(wrap);
					} else {
						visibles.push(wrap);
					}

				}
				curLng += dx;
			}

			//var pl = new BMap.Polyline([ doms[0].point, doms[1].point ], {
			//	'strokeWeight' : 2
			//});
			//map.addOverlay(pl);

		}

		function domclick(obj, event) {
			var tar = event.target
			console.log("(" + obj.point.lng + "," + obj.point.lat + "),("
					+ tar.style.top + "," + tar.style.left + ")  background="
					+ tar.style.background);
			if (tar.style.background.indexOf("green") >= 0) {
				tar.style.background = "blue";
			} else {
				tar.style.background = "green";
			}
		}

		function movetorightPlace(dom, residePoint) {
			console.log("--------------begin ");
			//console.log("----b4 move....left:"+dom.style.left+",top="+dom.style.top);
			var position = map.pointToPixel(residePoint);
			var length = dom.clientWidth || dom.offsetWidth;
			//console.log("----point=("+residePoint.lng+","+residePoint.lat+"),position=("+position.x+","+position.y+"),length=" + length);
			dom.style.left = (wholeX + position.x) + "px";
			dom.style.top = (wholeY + position.y) + "px";

			//console.log("----aft move:left="+dom.style.left+",top="+dom.style.top);
			//console.log("--------------end");
		}

		function btnClick() {
			createMapElement();
		}

		function MapDomWrap(radius, domtag, domclass, point) {
			this.dom = document.createElement(domtag);
			this.dom.setAttribute("class", domclass);
			document.body.appendChild(this.dom);
			this.point = point;
		}

		function MapDomWrap(dom, point) {
			this.dom = dom;
			this.point = point;
		}
		MapDomWrap.prototype.show = function() {
			if (this.dom) {
				this.dom.style.display = "";
			}
		}
		MapDomWrap.prototype.hide = function() {
			if (this.dom) {
				this.dom.style.display = "none";
			}
		}
		MapDomWrap.prototype.addEventListener = function(eventName, callback) {
			var me = this;
			if (this.dom) {
				$(this.dom).on(eventName, function(event) {
					callback(me, event);
				});
			}
		}
		
		function getElementAbsPos(e)   
		{  
		    var t = e.offsetTop;  
		    var l = e.offsetLeft;  
		    while(e = e.offsetParent)  
		    {  
		        t += e.offsetTop;  
		    l += e.offsetLeft;  
		    }  
		  
		    console.log("t="+t+",l="+l);
		    return {left:l,top:t};  
		}  
	</script>



	<input type="button" value="创建" onclick="btnClick();" />

</body>
</html>