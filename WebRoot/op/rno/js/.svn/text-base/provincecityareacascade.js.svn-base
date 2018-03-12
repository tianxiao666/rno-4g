		    //通过传省市区实现三级级联
		    function getSubAreas(provinceId,cityId,areaId){
		    //console.log("getSubArea进来了");
            //通用型  
			//console.log($(provinceId).val());
			var province;
			var cityarea;
			var area;
			
			/*if($(provinceId).length>0){
			province = $(provinceId); 
			//console.log("火狐");
			}
			if($(cityId).length>0){
			cityarea = $(cityId);
			}
			if(areaId!=null && $(areaId).length>0){
			//console.log("hahah");
			area=$(areaId);
			}*/
			if($("#"+provinceId+"").length>0){
			province = $("#"+provinceId+""); 
			//console.log("IE");
//			console.log("province:"+province);
			}
			if($("#"+cityId+"").length>0){
			cityarea = $("#"+cityId+"");
//			console.log("cityarea:"+cityarea);
			}
			if(areaId!=null && $("#"+areaId+"").length>0){
			//console.log("hahah");
			area=$("#"+areaId+"");
			}
			//选择变动省份
            province.change(function() {
            	var provinceId=province.val();
            	//console.log("省份进来了");
            	//if(areaId==-1){$("#cityid").empty();$("#regionid").empty();return;}
            	var senddata={provinceId:provinceId};
            	cityarea.empty();
            	if(areaId!=null){
            	area.empty();
            	}
            	//$("#cityid").append("<option value='-1'>请选择</option>");
            	//$("#regionid").append("<option value='-1'>请选择</option>");
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyCityAreaAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //alert("hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	cityarea.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                cityarea.trigger("change");	
                //area.trigger("change");
            });
            //自动加载触发省份变动事件
            province.trigger("change");
            //选择变动市区
               cityarea.change(function() {
            	var cityId=cityarea.val();
            	var senddata={cityId:cityId};
            	if(areaId!=null){
            	area.empty();
            	}
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyRegionAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					 if(typeof area!="undefined"){
					for(var i=0;i<mes_obj.length;i++){ 
					 //console.log("formtrafficimport　hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	area.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	//console.log(area);
			         	j++;
			      			} 
                      	 }
					 }
                       
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                if(areaId!=null){
                area.trigger("change");
                }
            });
            //function loadregion(){cityarea.trigger("change");}
            //setTimeout(loadregion,200);		
            cityarea.trigger("change");	
            
            }
/*
		$(document).ready(function() {
		    //通用型  
			if(!$("#formSysLoad")[0] || !$("#formtrafficimport")[0]){
			        
	        var province = $("#provinceId"); 
			var cityarea = $("#cityId");
			var area=$("#areaId");
		//选择变动省份
            province.change(function() {
            	var provinceId=province.val();
            	//console.log("进来了");
            	//if(areaId==-1){$("#cityid").empty();$("#regionid").empty();return;}
            	var senddata={provinceId:provinceId};
            	cityarea.empty();
            	area.empty();
            	//$("#cityid").append("<option value='-1'>请选择</option>");
            	//$("#regionid").append("<option value='-1'>请选择</option>");
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyCityAreaAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //alert("hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	cityarea.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                cityarea.trigger("change");	
            });
            //自动加载触发省份变动事件
            province.trigger("change");
            //选择变动市区
               cityarea.change(function() {
            	var cityId=cityarea.val();
            	var senddata={cityId:cityId};
            	area.empty();
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyRegionAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //console.log("formtrafficimport　hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	area.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	//console.log(area);
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
            });
            //function loadregion(){cityarea.trigger("change");}
            //setTimeout(loadregion,200);		
            cityarea.trigger("change");
			}
			
		});
		//==============================formtrafficimport表单==================//
		$(document).ready(function() {       
        //formtrafficimport表单
        //var aa="#formtrafficimport #provinceId";
        var province = $("#formtrafficimport #provinceId"); 
        console.log("hahahaaa");
        //console.log("province:"+province[0]);
		var cityarea = $("#formtrafficimport #cityId");
		var area=$("#formtrafficimport #areaId");
		
		//选择变动省份
            province.change(function() {
            	var provinceId=province.val();
            	//if(areaId==-1){$("#cityid").empty();$("#regionid").empty();return;}
            	var senddata={provinceId:provinceId};
            	cityarea.empty();
            	area.empty();
            	//$("#cityid").append("<option value='-1'>请选择</option>");
            	//$("#regionid").append("<option value='-1'>请选择</option>");
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyCityAreaAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //alert("hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	cityarea.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                cityarea.trigger("change");	
            });
            //自动加载触发省份变动事件
            province.trigger("change");
            //选择变动市区
               cityarea.change(function() {
            	var cityId=cityarea.val();
            	var senddata={cityId:cityId};
            	area.empty();
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyRegionAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //console.log("formtrafficimport　hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	area.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
            });
            //function loadregion(){cityarea.trigger("change");}
            //setTimeout(loadregion,200);		
            cityarea.trigger("change");	
            
     
        }); 
        
        
        //===============================formSysLoad表单====================//
        $(document).ready(function() {
		//formSysLoad表单
		var province = $("#formSysLoad #provinceId"); 
		//$("#formSysLoad");
		//console.log("province:"+province[0]);
		var cityarea = $("#formSysLoad #cityId");
		//console.log("cityarea:"+cityarea[0]);
		var area=$("#formSysLoad #areaId");
		//选择变动省份
            province.change(function() {
            	var provinceId=province.val();
            	//if(areaId==-1){$("#cityid").empty();$("#regionid").empty();return;}
            	var senddata={provinceId:provinceId};
            	cityarea.empty();
            	area.empty();
            	//$("#cityid").append("<option value='-1'>请选择</option>");
            	//$("#regionid").append("<option value='-1'>请选择</option>");
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyCityAreaAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //alert("hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	cityarea.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                cityarea.trigger("change");	
            });
            //自动加载触发省份变动事件
            province.trigger("change");
            //选择变动市区
               cityarea.change(function() {
            	var cityId=cityarea.val();
            	var senddata={cityId:cityId};
            	area.empty();
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyRegionAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //console.log("formSysLoad　hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	area.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
            });
            //function loadregion(){cityarea.trigger("change");}
            //setTimeout(loadregion,200);		
            cityarea.trigger("change");	
            });

            
            //方法三级联动 
            //function getSubArea(parentId,subareatype,childid){
            function getSubArea(provinceId,cityId,areaId){
            //通用型  
	        var province = $("#"+provinceId); 
			var cityarea = $("#"+cityId);
			var area=$("#"+areaId);
			//选择变动省份
            province.change(function() {
            	var provinceId=province.val();
            	//console.log("进来了");
            	//if(areaId==-1){$("#cityid").empty();$("#regionid").empty();return;}
            	var senddata={provinceId:provinceId};
            	cityarea.empty();
            	area.empty();
            	//$("#cityid").append("<option value='-1'>请选择</option>");
            	//$("#regionid").append("<option value='-1'>请选择</option>");
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyCityAreaAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //alert("hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	cityarea.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                cityarea.trigger("change");	
            });
            //自动加载触发省份变动事件
            province.trigger("change");
            //选择变动市区
               cityarea.change(function() {
            	var cityId=cityarea.val();
            	var senddata={cityId:cityId};
            	area.empty();
                $.ajax({
                    type: "POST", 
                    url: "getSpecifyRegionAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
					 //console.log("formtrafficimport　hello");
					 var j=0;
			 　　			　for(var key in mes_obj[i]){
			         	//alert("key："+key+",value："+mes_obj[i][key]); 
			         	area.append("<option value="+key+">"+mes_obj[i][key]+"</option>");
			         	//console.log(area);
			         	j++;
			      		} 
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
            });
            //function loadregion(){cityarea.trigger("change");}
            //setTimeout(loadregion,200);		
            cityarea.trigger("change");	
            }
            */