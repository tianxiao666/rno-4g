function Scroll(obj, h, s){
    var h = h || 200;
    var s = s || 1.2;
    var obj = typeof(obj)=="string"?document.getElementById(obj):obj;
    if(obj == undefined){return false;}
    var status = obj.getAttribute("status")==null;
    var oh = parseInt(obj.offsetHeight);
    obj.style.height = oh;
    obj.style.display = "block";
	obj.style.overflow = "hidden";
    if(obj.getAttribute("oldHeight") == null){
        obj.setAttribute("oldHeight", oh);
    }else{
        var oldH = Math.ceil(obj.getAttribute("oldHeight"));
    }
    var reSet = function(){
        if(status){
            if(oh < h){
                oh = Math.ceil(h-(h-oh)/s);
                obj.style.height = oh+"px";
            }else{
                obj.setAttribute("status",false);
                window.clearInterval(IntervalId);
            }
        }else{
            obj.style.height = oldH+"px";
            obj.removeAttribute("status");
            window.clearInterval(IntervalId);
        }
    }
    var IntervalId = window.setInterval(reSet,10);
	return status;
}
window.onload= function(){
    var $ = function(id){return document.getElementById(id)};
    $('zkai').onclick = function(){
        Scroll('zkai',this.scrollHeight,1.2);
    }
}