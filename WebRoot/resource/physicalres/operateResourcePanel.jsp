<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1"
       width="800px" height="800px" viewBox="-253 0 800 800" onload="loadSVGContent(evt);" >
  <defs>
  	<style type="text/css" >
  	rect{cursor:pointer}
  	text{cursor:pointer}
  	</style>
  	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
    <script type="text/javascript">
      <![CDATA[
      	var models = ["FCU","CDU-F9","ASU","HCU","CXU","dTRU","IDM","PSU","DXU","TMA-CM"];
      	var array;
      	var svgDoc,newElement,panelContent,textNode,textLength,BBox,trace,pathNode,x=0,y=0,textHeight,textWidth,content,rectNode,gElement;
      	var svgns = "http://www.w3.org/2000/svg";
      	var modelType ='';	
      	function loadSVGContent(evt){
      		svgDoc = evt.target.ownerDocument;
      		if(evt.target.parentNode.getAttribute("id")=="contentDiv"){
      			modelType="manage";
      		}else{
      			modelType="view";
      		}
      		if(modelType=="view"){
      			$("#boardPanel svg").attr("viewBox","-226 0 800 800")
      			$("#viewinfoDiv").css("position","fixed");
      		}
      		panelContent = svgDoc.getElementById(modelType+"panelContent");
      		
      		newElement = svgDoc.createElementNS(svgns, "rect");
      		newElement.setAttributeNS(null, "x", "20");
      		newElement.setAttributeNS(null, "y", "62");
	        newElement.setAttributeNS(null, "width", "40");
	        newElement.setAttributeNS(null, "height", "23");
	        newElement.setAttributeNS(null, "fill", "white");
	        newElement.setAttributeNS(null, "stroke", "black");
	        panelContent.appendChild(newElement);
      		
      		newElement = svgDoc.createElementNS(svgns, "g");//创建path
	        newElement.setAttributeNS(null, 'id',modelType+'FCUG');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"FCUG");
      		 
      		
      		newElement = svgDoc.createElementNS(svgns, "path");//创建path
	        newElement.setAttributeNS(null, 'id',modelType+'FCUPATH');
	        newElement.setAttributeNS(null, 'fill','#CCCCCC');
	        gElement.appendChild(newElement);
	        
	        newElement = svgDoc.createElementNS(svgns, "text");//创建text
	       	newElement.setAttributeNS(null, "x", "70");
	        newElement.setAttributeNS(null, "y", "80");
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'FCUTEXT');
	        text=svgDoc.createTextNode('FCU');
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	        
	        textNode = svgDoc.getElementById(modelType+"FCUTEXT"); //path包围text
	        BBox=textNode.getBBox();
	        trace='M'+BBox.x+' '+BBox.y+' l'+BBox.width+' 0 0 '+BBox.height+' -'+BBox.width+' 0z';
	       	pathNode = svgDoc.getElementById(modelType+"FCUPATH");
	       	pathNode.setAttribute('d',trace);
			
	      	
	      	newElement = svgDoc.createElementNS(svgns, "rect");
	      	newElement.setAttributeNS(null, "x", "123");
      		newElement.setAttributeNS(null, "y", "62");
	        newElement.setAttributeNS(null, "width", "40");
	        newElement.setAttributeNS(null, "height", "23");
	        newElement.setAttributeNS(null, "fill", "white");
	        newElement.setAttributeNS(null, "stroke", "black");
	        panelContent.appendChild(newElement);
	      
	        for(var i=0;i<3;i++){
	        	newElement = svgDoc.createElementNS(svgns, "g");//创建G
		        newElement.setAttributeNS(null, 'id',modelType+'CDU-FG'+i);
		        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        	newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        	newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
		        panelContent.appendChild(newElement);
	      		gElement = svgDoc.getElementById(modelType+"CDU-FG"+i);
		        newElement = svgDoc.createElementNS(svgns, "rect");//创建RECT
		        newElement.setAttributeNS(null, 'id',modelType+'CDU-FRECT'+i);
		        newElement.setAttributeNS(null, 'name','element');
		        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
		        gElement.appendChild(newElement);
	        	y = 108;
		        x = 33+i*50;
		        array=["C","D","U","-","F"];
		        textHeight=0;
		        for(var j=0;j<5;j++){
	
		        	newElement = svgDoc.createElementNS(svgns, "text");//创建text
			       	newElement.setAttributeNS(null, "x", x);
			        newElement.setAttributeNS(null, "y",y);
			        y = y+23;
			        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
			        newElement.setAttributeNS(null, 'id',modelType+'CDU-FTEXT'+i+j);
			        newElement.setAttributeNS(null, 'name','element');
			        text=svgDoc.createTextNode(array[j]);
			        newElement.appendChild(text);
			        gElement.appendChild(newElement);
		        }
		       
	        }
	        for(var j=0;j<3;j++){
	        	for(var k=0;k<5;k++){
		        	textNode = svgDoc.getElementById(modelType+"CDU-FTEXT"+j+k); //rect包围text
		        	BBox=textNode.getBBox();
		        	if(k==0){
		        		textHeight =5*23;
		        		textWidth = 50;
			        	x=BBox.x-18;
			        	y=BBox.y;
		        	}
		        }
		       	rectNode = svgDoc.getElementById(modelType+"CDU-FRECT"+j);
		       	rectNode.setAttributeNS(null, "x", x);
	      		rectNode.setAttributeNS(null, "y", y);
		        rectNode.setAttributeNS(null, "width",textWidth);
		        rectNode.setAttributeNS(null, "height",textHeight);
	        }
	        
	        newElement = svgDoc.createElementNS(svgns, "g");//创建g
	        newElement.setAttributeNS(null, 'id',modelType+'ASUG');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"ASUG"); 
	        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
	        newElement.setAttributeNS(null, 'name','element');
	        newElement.setAttributeNS(null, 'id',modelType+'ASURECT');
	        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
	        newElement.setAttributeNS(null, "x", "14");
      		newElement.setAttributeNS(null, "y", "205");
	        newElement.setAttributeNS(null, "width", "150");
	        newElement.setAttributeNS(null, "height", "23");
	        gElement.appendChild(newElement);
            newElement = svgDoc.createElementNS(svgns, "text");//创建text
            newElement.setAttributeNS(null, 'name','element');
	       	newElement.setAttributeNS(null, "x", 70);
	        newElement.setAttributeNS(null, "y",223);	
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'ASUTEXT');
	        text=svgDoc.createTextNode("ASU");
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	     	
	     	newElement = svgDoc.createElementNS(svgns, "g");//创建g
	        newElement.setAttributeNS(null, 'id',modelType+'HCUG');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"HCUG"); 
	        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
	        newElement.setAttributeNS(null, 'name','element');
	        newElement.setAttributeNS(null, 'id',modelType+'HCURECT');
	        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
	        newElement.setAttributeNS(null, "x", "14");
      		newElement.setAttributeNS(null, "y", "228");
	        newElement.setAttributeNS(null, "width", "150");
	        newElement.setAttributeNS(null, "height", "23");
	        gElement.appendChild(newElement);
            newElement = svgDoc.createElementNS(svgns, "text");//创建text
            newElement.setAttributeNS(null, 'name','element');
	       	newElement.setAttributeNS(null, "x", 70);
	        newElement.setAttributeNS(null, "y",246);	
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'HCUTEXT');
	        text=svgDoc.createTextNode("HCU");
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	        
	        newElement = svgDoc.createElementNS(svgns, "g");//创建g
	        newElement.setAttributeNS(null, 'id',modelType+'CXUG0');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"CXUG0");
	        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
	        newElement.setAttributeNS(null, 'id',modelType+'CXURECT0');
	        newElement.setAttributeNS(null, 'name','element');
	        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
	        newElement.setAttributeNS(null, "x", "14");
      		newElement.setAttributeNS(null, "y", "251");
	        newElement.setAttributeNS(null, "width", "150");
	        newElement.setAttributeNS(null, "height", "23");
	        gElement.appendChild(newElement);
            newElement = svgDoc.createElementNS(svgns, "text");//创建text
            newElement.setAttributeNS(null, 'name','element');
	       	newElement.setAttributeNS(null, "x", 70);
	        newElement.setAttributeNS(null, "y",269);	
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'CXUTEXT0');
	        text=svgDoc.createTextNode("CXU");
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	        
	         newElement = svgDoc.createElementNS(svgns, "g");//创建g
	        newElement.setAttributeNS(null, 'id',modelType+'CXUG1');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"CXUG1");
	        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
	        newElement.setAttributeNS(null, 'id',modelType+'CXURECT1');
	        newElement.setAttributeNS(null, 'name','element');
	        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
	        newElement.setAttributeNS(null, "x", "14");
      		newElement.setAttributeNS(null, "y", "274");
	        newElement.setAttributeNS(null, "width", "150");
	        newElement.setAttributeNS(null, "height", "23");
	        gElement.appendChild(newElement);
            newElement = svgDoc.createElementNS(svgns, "text");//创建text
            newElement.setAttributeNS(null, 'name','element');
	       	newElement.setAttributeNS(null, "x", 70);
	        newElement.setAttributeNS(null, "y",292);	
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'CXUTEXT1');
	        text=svgDoc.createTextNode("CXU");
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	        
	        for(var i=0;i<6;i++){
	        	newElement = svgDoc.createElementNS(svgns, "g");//创建g
		        newElement.setAttributeNS(null, 'id',modelType+'dTRUG'+i);
		        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	       	    newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	       	    newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
		        panelContent.appendChild(newElement);
	      		gElement = svgDoc.getElementById(modelType+"dTRUG"+i);
		        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
		        newElement.setAttributeNS(null, 'name','element');
		        newElement.setAttributeNS(null, 'id',modelType+'dTRURECT'+i);
		        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
		        gElement.appendChild(newElement);
	        	y = 320;
		        x = 19+i*25;
		        array=["d","T","R","U"];
		        textHeight=0;
		        for(var j=0;j<4;j++){
	
		        	newElement = svgDoc.createElementNS(svgns, "text");//创建text
		        	newElement.setAttributeNS(null, 'name','element');
			       	newElement.setAttributeNS(null, "x", x);
			        newElement.setAttributeNS(null, "y",y);
			        y = y+23;
			        newElement.setAttributeNS(null, 'style','font-size:15;font-family:Arial;fill:#8C8C8C');
			        newElement.setAttributeNS(null, 'id',modelType+'dTRUTEXT'+i+j);
			        text=svgDoc.createTextNode(array[j]);
			        newElement.appendChild(text);
			        gElement.appendChild(newElement);
		        }
		       
	        }
	        for(var j=0;j<6;j++){
	        	for(var k=0;k<4;k++){
		        	textNode = svgDoc.getElementById(modelType+"dTRUTEXT"+j+k); //rect包围text
		        	BBox=textNode.getBBox();
		        	if(k==0){
		        		textHeight =4*23;
		        		textWidth = 25;
			        	x=BBox.x-4;
			        	y=BBox.y-7;
		        	}
		        }
		        
		       	rectNode = svgDoc.getElementById(modelType+"dTRURECT"+j);
		       	rectNode.setAttributeNS(null, "x", x);
	      		rectNode.setAttributeNS(null, "y", y);
		        rectNode.setAttributeNS(null, "width",textWidth);
		        rectNode.setAttributeNS(null, "height",textHeight);
	        }
	        
	        newElement = svgDoc.createElementNS(svgns, "g");//创建path
	        newElement.setAttributeNS(null, 'id',modelType+'IDMG');
	        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	        newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	        newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
	        panelContent.appendChild(newElement);
      		gElement = svgDoc.getElementById(modelType+"IDMG");
	        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
	        newElement.setAttributeNS(null, 'name','element');
	        newElement.setAttributeNS(null, 'id',modelType+'IDMRECT');
	        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
	        newElement.setAttributeNS(null, "x", "14");
      		newElement.setAttributeNS(null, "y", "393");
	        newElement.setAttributeNS(null, "width", "150");
	        newElement.setAttributeNS(null, "height", "23");
	        gElement.appendChild(newElement);
            newElement = svgDoc.createElementNS(svgns, "text");//创建text
            newElement.setAttributeNS(null, 'name','element');
	       	newElement.setAttributeNS(null, "x", 70);
	        newElement.setAttributeNS(null, "y",412);	
	        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
	        newElement.setAttributeNS(null, 'id',modelType+'IDMTEXT');
	        text=svgDoc.createTextNode("IDM");
	        newElement.appendChild(text);
	        gElement.appendChild(newElement);
	        
	        var indexFlag=3;
	        for(var i=0;i<7;i++){
	        	y = 435;
		        x = 17+i*20;
		        array=["P","S","U"];
		        content="PSU";
		        if(i==4){
		        	array=["D","X","U"];
		        	content="DXU";
		        	indexFlag=3;
		        }else if(i>4){
		        	array=["T","M","A","-","C","M"];
		        	content="TMA-CM";
		        	indexFlag=6;
		        }
		  		newElement = svgDoc.createElementNS(svgns, "g");//创建g
		        newElement.setAttributeNS(null, 'id',modelType+content+'G'+i);
		        newElement.setAttributeNS(null, 'onmouseover','elementOver(evt);');
	       	    newElement.setAttributeNS(null, 'onmousedown','elementDown(evt);');
	       	    newElement.setAttributeNS(null, 'onmouseout','elementOut(evt);');
		        panelContent.appendChild(newElement);
	      		gElement = svgDoc.getElementById(modelType+content+"G"+i);
		        newElement = svgDoc.createElementNS(svgns, "rect");//创建rect
		        newElement.setAttributeNS(null, 'name','element');
		        newElement.setAttributeNS(null, 'id',modelType+content+'RECT'+i);
		        newElement.setAttributeNS(null, 'style','fill:#CCCCCC;stroke:black;stroke-width:1');
		        gElement.appendChild(newElement);
		        textHeight=0;
		        for(var j=0;j<indexFlag;j++){
		        	newElement = svgDoc.createElementNS(svgns, "text");//创建text
		        	newElement.setAttributeNS(null, 'name','element');
			       	newElement.setAttributeNS(null, "x", x);
			        newElement.setAttributeNS(null, "y",y);
			        y = y+23;
			        newElement.setAttributeNS(null, 'style','font-size:10;font-family:Arial;fill:#8C8C8C');
			        newElement.setAttributeNS(null, 'id',modelType+content+'TEXT'+i+j);
			        text=svgDoc.createTextNode(array[j]);
			        newElement.appendChild(text);
			        gElement.appendChild(newElement);
		        }
		       
	        }
	        for(var j=0;j<7;j++){
	        	indexFlag=3;
	        	content="PSU";
		        if(j==4){
		        	content="DXU";
		        }else if(j>4){
		        	content="TMA-CM";
		        	indexFlag=6;
		        }
	        	for(var k=0;k<indexFlag;k++){
		        	textNode = svgDoc.getElementById(modelType+content+"TEXT"+j+k); //rect包围text
		        	BBox=textNode.getBBox();
		        	if(k==0){
		        		textHeight =6*23;
		        		textWidth = 18;
			        	x=BBox.x-2;
			        	y=BBox.y-7;
		        	}
		        }
		        rectNode = svgDoc.getElementById(modelType+content+"RECT"+j);
		       	rectNode.setAttributeNS(null, "x", x);
	      		rectNode.setAttributeNS(null, "y", y);
		        rectNode.setAttributeNS(null, "width",textWidth);
		        rectNode.setAttributeNS(null, "height",textHeight);
	        }
	        var url="getEquipBoardForShowPanelAction";
	        var currentEntityType="PrimaryEquipFrame_GSM";
	        var currentEntityId=$("#"+modelType+"addedParentEntityId").val();
	        var params ={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	        $.post(url,params,function(data){
	        	if(data!=null && data!="" && data!=undefined){
	        		var model="";
	        		var paneltext="";
	        		var aeId="";
	        		var aeType="";
	        		var setupslot=0;
	        		if(currentEntityType=="PrimaryEquipFrame_GSM"){
	        			var cduIndex=0;
		        		var dtrIndex=0;
		        		var psuIndex=0;
		        		var tmaIndex=0;
		        		var cxuIndex=0;
	        			$.each(data,function(index,value){
		        			model = value.model;paneltext = value.paneltext;
		        			aeId = value.id;aeType = value.type;
		        			setupslot = value.setupslot;
		        			if(setupslot==""){
		        				setupslot=0;
		        			}else{
		        			    setupslot = parseInt(setupslot);
		        			}
		        			if(model!=""){
		        				if(model.indexOf("FCU")>-1){
		        					if(model.length>3){
		        						$("#"+modelType+"FCUTEXT").text(model).css("font-size","15").css("fill","black");
		        						$("#"+modelType+"FCUPATH").attr("fill","#CFDCF0");
		        						$("#"+modelType+"FCUG").attr("class",aeType+"*"+aeId);
		        					}else{
		        						$("#"+modelType+"FCUTEXT").text(model).css("fill","black");
		        						$("#"+modelType+"FCUPATH").attr("fill","#CFDCF0");
		        						$("#"+modelType+"FCUG").attr("class",aeType+"*"+aeId);
		        					}
			        			}else if(model.indexOf("CDU")>-1){
			        				if(model.length>5){
			        					for(var i=0;i<model.length;i++){
			        						if(i>=5){
			        							x = $("#"+modelType+"CDU-FTEXT"+setupslot+(i-1)).attr("x");
			        							y = $("#"+modelType+"CDU-FTEXT"+setupslot+(i-1)).attr("y");
			        							y = parseInt(y)+23;
			        							newElement = svgDoc.createElementNS(svgns, "text");//创建text
										       	newElement.setAttributeNS(null, "x", x);
										        newElement.setAttributeNS(null, "y",y);
										        newElement.setAttributeNS(null, 'style','font-size:20;font-family:Arial;fill:#8C8C8C');
										        newElement.setAttributeNS(null, 'id',modelType+'CDU-FTEXT'+setupslot+i);
										        text=svgDoc.createTextNode(model.substring(i,i+1));
										        newElement.appendChild(text);
										        gElement=svgDoc.getElementById(modelType+"CDU-FG"+setupslot);
										        gElement.appendChild(newElement);
			        						}else{
			        							$("#"+modelType+"CDU-FTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
        								textHeight =(model.length)*23;
        								var height0 = $("#"+modelType+"CDU-FRECT0").attr("height");
        								var height1 = $("#"+modelType+"CDU-FRECT1").attr("height");
        								var height2 = $("#"+modelType+"CDU-FRECT2").attr("height");
        								if(textHeight>height0 && textHeight>height1 && textHeight>height2){
	        								$("#"+modelType+"CDU-FRECT0").attr("height",textHeight);
				        					$("#"+modelType+"CDU-FRECT1").attr("height",textHeight);
				        					$("#"+modelType+"CDU-FRECT2").attr("height",textHeight);
				        					$("rect[name='"+modelType+"element']").each(function(){
				        						var idString = $(this).attr("id");
				        						if(idString.indexOf("CDU")<0){
				        							$(this).attr("y",parseInt($(this).attr("y"))+(model.length-6)*23);
				        						}
				        					})
				        					$("text[name='"+modelType+"element']").each(function(){
				        						var idString = $(this).attr("id");
				        						if(idString.indexOf("CDU")<0){
				        							$(this).attr("y",parseInt($(this).attr("y"))+(model.length-6)*23);
				        						}
				        					})
				        					$("#"+modelType+"firstRect").attr("height",parseInt($("#"+modelType+"firstRect").attr("height"))+(model.length-6)*23);
				        					$("#"+modelType+"secondRect").attr("height",parseInt($("#"+modelType+"secondRect").attr("height"))+(model.length-6)*23);
        								}			        					
			        				}else{
			        					for(var i=0;i<5;i++){
			        						if(model.length<6){
			        							if(i>=model.length){
			        								$("#"+modelType+"CDU-FTEXT"+setupslot+i).text("");
			        							}else{
			        								$("#"+modelType+"CDU-FTEXT"+setupslot+i).text(model.substring(i,i+1));
			        							}
			        						}else{
			        							$("#"+modelType+"CDU-FTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					
			        				}
			        				$("#"+modelType+"CDU-FRECT"+setupslot).css("fill","#CFDCF0");
			        				$("#"+modelType+"CDU-FG"+setupslot).attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"CDU-FG"+setupslot+" text").each(function(){
			        					$(this).css("fill","black");
			        				})
			        				cduIndex++;
			        			}else if(model.indexOf("ASU")>-1){
			        				textNode = svgDoc.getElementById(modelType+"ASUTEXT");
			        				var everyLength = textNode.getComputedTextLength()/3;
			        				x = parseInt($("#"+modelType+"ASURECT").attr("x"));
			        				var rectWidth = parseInt($("#"+modelType+"ASURECT").attr("width"));
			        				if(rectWidth-model.length*everyLength>0){
			        					x=x+(rectWidth-model.length*everyLength)/2;
			        				}
			        				$("#"+modelType+"ASUTEXT").text(model).attr("x",x).css("fill","black");
			        				$("#"+modelType+"ASURECT").css("fill","#CFDCF0");
			        				$("#"+modelType+"ASUG").attr("class",aeType+"*"+aeId);
			        			}else if(model.indexOf("HCU")>-1){
			        				textNode = svgDoc.getElementById(modelType+"HCUTEXT");
			        				var everyLength = textNode.getComputedTextLength()/3;
			        				x = parseInt($("#"+modelType+"HCURECT").attr("x"));
			        				var rectWidth = parseInt($("#"+modelType+"HCURECT").attr("width"));
			        				if(rectWidth-model.length*everyLength>0){
			        					x=x+(rectWidth-model.length*everyLength)/2;
			        				}
			        				$("#"+modelType+"HCUTEXT").text(model).attr("x",x).css("fill","black");
			        				$("#"+modelType+"HCURECT").css("fill","#CFDCF0");
			        				$("#"+modelType+"HCUG").attr("class",aeType+"*"+aeId);
			        			}else if(model.indexOf("CXU")>-1){
			        				textNode = svgDoc.getElementById(modelType+"CXUTEXT"+setupslot);
			        				var everyLength = textNode.getComputedTextLength()/3;
			        				x = parseInt($("#"+modelType+"CXURECT"+setupslot).attr("x"));
			        				var rectWidth = parseInt($("#"+modelType+"CXURECT"+setupslot).attr("width"));
			        				if(rectWidth-model.length*everyLength>0){
			        					x=x+(rectWidth-model.length*everyLength)/2;
			        				}
			        				$("#"+modelType+"CXUTEXT"+setupslot).text(model).attr("x",x).css("fill","black");
			        				$("#"+modelType+"CXURECT"+setupslot).css("fill","#CFDCF0");
			        				$("#"+modelType+"CXUG"+setupslot).attr("class",aeType+"*"+aeId);
			        				cxuIndex++;
			        			}else if(model.indexOf("dTRU")>-1){
			        				if(model.length>4){
			        					for(var i=0;i<model.length;i++){
			        						if(i>=4){
			        							x = $("#"+modelType+"dTRUTEXT"+setupslot+(i-1)).attr("x");
			        							y = $("#"+modelType+"dTRUTEXT"+setupslot+(i-1)).attr("y");
			        							y = parseInt(y)+23;
			        							newElement = svgDoc.createElementNS(svgns, "text");//创建text
										       	newElement.setAttributeNS(null, "x", x);
										        newElement.setAttributeNS(null, "y",y);
										        newElement.setAttributeNS(null, 'style','font-size:15;font-family:Arial;fill:#8C8C8C');
										        newElement.setAttributeNS(null, 'id',modelType+'dTRUTEXT'+setupslot+i);
										        text=svgDoc.createTextNode(model.substring(i,i+1));
										        newElement.appendChild(text);
										        gElement=svgDoc.getElementById(modelType+"dTRUG"+setupslot);
										        gElement.appendChild(newElement);
			        						}else{
			        							$("#"+modelType+"dTRUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
        								textHeight =(model.length)*23;
        								var height0 = $("#"+modelType+"dTRURECT0").attr("height");
        								var height1 = $("#"+modelType+"dTRURECT1").attr("height");
        								var height2 = $("#"+modelType+"dTRURECT2").attr("height");
        								var height3 = $("#"+modelType+"dTRURECT3").attr("height");
        								var height4 = $("#"+modelType+"dTRURECT4").attr("height");
        								var height5 = $("#"+modelType+"dTRURECT5").attr("height");
        								if(textHeight>height0 && textHeight>height1 && textHeight>height2){
	        								$("#"+modelType+"dTRURECT0").attr("height",textHeight);
				        					$("#"+modelType+"dTRURECT1").attr("height",textHeight);
				        					$("#"+modelType+"dTRURECT2").attr("height",textHeight);
				        					$("#"+modelType+"dTRURECT3").attr("height",textHeight);
	        								$("#"+modelType+"dTRURECT4").attr("height",textHeight);
	        								$("#"+modelType+"dTRURECT5").attr("height",textHeight);
				        					$("rect[name='"+modelType+"element']").each(function(){
				        						var idString = $(this).attr("id");
				        						if(idString.indexOf("dTRU")<0&&idString.indexOf("CDU")<0&&idString.indexOf("ASU")<0&&idString.indexOf("HCU")<0&&idString.indexOf("CXU")<0){
				        							$(this).attr("y",parseInt($(this).attr("y"))+(model.length-5)*23);
				        						}
				        					})
				        					$("text[name='"+modelType+"element']").each(function(){
				        						var idString = $(this).attr("id");
				        						if(idString.indexOf("dTRU")<0&&idString.indexOf("CDU")<0&&idString.indexOf("ASU")<0&&idString.indexOf("HCU")<0&&idString.indexOf("CXU")<0){
				        							$(this).attr("y",parseInt($(this).attr("y"))+(model.length-5)*23);
				        						}
				        					})
				        					$("#"+modelType+"firstRect").attr("height",parseInt($("#"+modelType+"firstRect").attr("height"))+(model.length-5)*23);
				        					$("#"+modelType+"secondRect").attr("height",parseInt($("#"+modelType+"secondRect").attr("height"))+(model.length-5)*23);
        								}			        					
			        				}else{
			        					for(var i=0;i<4;i++){
			        						if(model.length<4){
			        							if(i>=model.length){
			        								$("#"+modelType+"dTRUTEXT"+setupslot+i).text("");
			        							}else{
			        								$("#"+modelType+"dTRUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        							}
			        						}else{
			        							$("#"+modelType+"dTRUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					
			        				}
			        				$("#"+modelType+"dTRURECT"+setupslot).css("fill","#CFDCF0");
			        				$("#"+modelType+"dTRUG"+setupslot).attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"dTRUG"+setupslot+" text").each(function(){
			        					$(this).css("fill","black");
			        				})
			        				dtrIndex++;
			        			}else if(model.indexOf("IDM")>-1){
			        				textNode = svgDoc.getElementById(modelType+"IDMTEXT");
			        				var everyLength = textNode.getComputedTextLength()/3;
			        				x = parseInt($("#"+modelType+"IDMRECT").attr("x"));
			        				var rectWidth = parseInt($("#"+modelType+"IDMRECT").attr("width"));
			        				if(rectWidth-model.length*everyLength>0){
			        					x=x+(rectWidth-model.length*everyLength)/2;
			        				}
			        				$("#"+modelType+"IDMTEXT").text(model).attr("x",x).css("fill","black");
			        				$("#"+modelType+"IDMG").attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"IDMRECT").css("fill","#CFDCF0");
			        			}else if(model.indexOf("PSU")>-1){
			        				if(model.length>3){
			        					for(var i=0;i<model.length;i++){
			        						if(i>=3){
			        							x = $("#"+modelType+"PSUTEXT"+setupslot+(i-1)).attr("x");
			        							y = $("#"+modelType+"PSUTEXT"+setupslot+(i-1)).attr("y");
			        							y = parseInt(y)+23;
			        							newElement = svgDoc.createElementNS(svgns, "text");//创建text
										       	newElement.setAttributeNS(null, "x", x);
										        newElement.setAttributeNS(null, "y",y);
										        newElement.setAttributeNS(null, 'style','font-size:10;font-family:Arial;fill:#8C8C8C');
										        newElement.setAttributeNS(null, 'id',modelType+'PSUTEXT'+setupslot+i);
										        text=svgDoc.createTextNode(model.substring(i,i+1));
										        newElement.appendChild(text);
										        gElement=svgDoc.getElementById(modelType+"PSUG"+setupslot);
										        gElement.appendChild(newElement);
			        						}else{
			        							$("#"+modelType+"PSUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					if(i>5){
			        						textHeight =(model.length)*23;
	        								var height0 = $("#"+modelType+"PSURECT0").attr("height");
	        								var height1 = $("#"+modelType+"PSURECT1").attr("height");
	        								var height2 = $("#"+modelType+"PSURECT2").attr("height");
	        								var height3 = $("#"+modelType+"PSURECT3").attr("height");
	        								var height4 = $("#"+modelType+"DXURECT4").attr("height");
	        								var height5 = $("#"+modelType+"TMA-CMRECT5").attr("height");
	        								var height6 = $("#"+modelType+"TMA-CMRECT6").attr("height");
	        								if(textHeight>height0 && textHeight>height1 && textHeight>height2){
		        								$("#"+modelType+"PSURECT0").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT1").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT2").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT3").attr("height",textHeight);
		        								$("#"+modelType+"DXURECT").attr("height",textHeight);
		        								$("#"+modelType+"TMA-CMRECT0").attr("height",textHeight);
		        								$("#"+modelType+"TMA-CMRECT1").attr("height",textHeight);
					        					$("#"+modelType+"firstRect").attr("height",parseInt($("#"+modelType+"firstRect").attr("height"))+(model.length-6)*23);
					        					$("#"+modelType+"secondRect").attr("height",parseInt($("#"+modelType+"secondRect").attr("height"))+(model.length-6)*23);
	        								}
			        					}
        											        					
			        				}else{
			        					for(var i=0;i<3;i++){
			        						if(model.length<3){
			        							if(i>=model.length){
			        								$("#"+modelType+"PSUTEXT"+setupslot+i).text("");
			        							}else{
			        								$("#"+modelType+"PSUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        							}
			        						}else{
			        							$("#"+modelType+"PSUTEXT"+setupslot+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					
			        				}
			        				$("#"+modelType+"PSURECT"+setupslot).css("fill","#CFDCF0");
			        				$("#"+modelType+"PSUG"+setupslot).attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"PSUG"+setupslot+" text").each(function(){
			        					$(this).css("fill","black");
			        				})
			        				psuIndex++;
			        			}else if(model.indexOf("DXU")>-1){
			        				if(model.length>3){
			        					for(var i=0;i<model.length;i++){
			        						if(i>=3){
			        							x = $("#"+modelType+"DXUTEXT"+4+(i-1)).attr("x");
			        							y = $("#"+modelType+"DXUTEXT"+4+(i-1)).attr("y");
			        							y = parseInt(y)+23;
			        							newElement = svgDoc.createElementNS(svgns, "text");//创建text
										       	newElement.setAttributeNS(null, "x", x);
										        newElement.setAttributeNS(null, "y",y);
										        newElement.setAttributeNS(null, 'style','font-size:10;font-family:Arial;fill:#8C8C8C');
										        newElement.setAttributeNS(null, 'id',modelType+'DXUTEXT'+4+i);
										        text=svgDoc.createTextNode(model.substring(i,i+1));
										        newElement.appendChild(text);
										        gElement=svgDoc.getElementById(modelType+"DXUG4");
										        gElement.appendChild(newElement);
			        						}else{
			        							$("#"+modelType+"DXUTEXT"+4+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					if(i>5){
			        						textHeight =(model.length)*23;
	        								var height0 = $("#"+modelType+"PSURECT0").attr("height");
	        								var height1 = $("#"+modelType+"PSURECT1").attr("height");
	        								var height2 = $("#"+modelType+"PSURECT2").attr("height");
	        								var height3 = $("#"+modelType+"PSURECT3").attr("height");
	        								var height4 = $("#"+modelType+"DXURECT4").attr("height");
	        								var height5 = $("#"+modelType+"TMA-CMRECT5").attr("height");
	        								var height6 = $("#"+modelType+"TMA-CMRECT6").attr("height");
	        								if(textHeight>height0 && textHeight>height1 && textHeight>height2){
		        								$("#"+modelType+"PSURECT0").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT1").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT2").attr("height",textHeight);
		        								$("#"+modelType+"PSURECT3").attr("height",textHeight);
		        								$("#"+modelType+"DXURECT4").attr("height",textHeight);
		        								$("#"+modelType+"TMA-CMRECT5").attr("height",textHeight);
		        								$("#"+modelType+"TMA-CMRECT6").attr("height",textHeight);
					        					$("#"+modelType+"firstRect").attr("height",parseInt($("#firstRect").attr("height"))+(model.length-6)*23);
					        					$("#"+modelType+"secondRect").attr("height",parseInt($("#secondRect").attr("height"))+(model.length-6)*23);
	        								}
			        					}
        											        					
			        				}else{
			        					for(var i=0;i<3;i++){
			        						if(model.length<3){
			        							if(i>=model.length){
			        								$("#"+modelType+"DXUTEXT"+4+i).text("");
			        							}else{
			        								$("#"+modelType+"DXUTEXT"+4+i).text(model.substring(i,i+1));
			        							}
			        						}else{
			        							$("#"+modelType+"DXUTEXT"+4+i).text(model.substring(i,i+1));
			        						}
			        					}
			        					
			        				}
			        				$("#"+modelType+"DXURECT4").css("fill","#CFDCF0");
			        				$("#"+modelType+"DXUG4").attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"DXUG4 text").each(function(){
			        					$(this).css("fill","black");
			        				})
			        			}else if(model.indexOf("TMA")>-1){
			        				if(model.length>6){
			        					for(var i=0;i<model.length;i++){
			        						if(i>=6){
			        							x = $("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+(i-1)).attr("x");
			        							y = $("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+(i-1)).attr("y");
			        							y = parseInt(y)+23;
			        							newElement = svgDoc.createElementNS(svgns, "text");//创建text
										       	newElement.setAttributeNS(null, "x", x);
										        newElement.setAttributeNS(null, "y",y);
										        newElement.setAttributeNS(null, 'style','font-size:10;font-family:Arial;fill:#8C8C8C');
										        newElement.setAttributeNS(null, 'id',modelType+'TMA-CMTEXT'+(setupslot+5)+i);
										        text=svgDoc.createTextNode(model.substring(i,i+1));
										        newElement.appendChild(text);
										        gElement=svgDoc.getElementById(modelType+"TMA-CMG"+(setupslot+5));
										        gElement.appendChild(newElement);
			        						}else{
			        							$("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+i).text(model.substring(i,i+1));
			        						}
			        					}

		        						textHeight =(model.length)*23;
        								var height0 = $("#"+modelType+"PSURECT0").attr("height");
        								var height1 = $("#"+modelType+"PSURECT1").attr("height");
        								var height2 = $("#"+modelType+"PSURECT2").attr("height");
        								var height3 = $("#"+modelType+"PSURECT3").attr("height");
        								var height4 = $("#"+modelType+"DXURECT4").attr("height");
        								var height5 = $("#"+modelType+"TMA-CMRECT5").attr("height");
        								var height6 = $("#"+modelType+"TMA-CMRECT6").attr("height");
        								if(textHeight>height0 && textHeight>height1 && textHeight>height2){
	        								$("#"+modelType+"PSURECT0").attr("height",textHeight);
	        								$("#"+modelType+"PSURECT1").attr("height",textHeight);
	        								$("#"+modelType+"PSURECT2").attr("height",textHeight);
	        								$("#"+modelType+"PSURECT3").attr("height",textHeight);
	        								$("#"+modelType+"DXURECT4").attr("height",textHeight);
	        								$("#"+modelType+"TMA-CMRECT5").attr("height",textHeight);
	        								$("#"+modelType+"TMA-CMRECT6").attr("height",textHeight);
				        					$("#"+modelType+"firstRect").attr("height",parseInt($("#"+modelType+"firstRect").attr("height"))+(model.length-6)*23);
				        					$("#"+modelType+"secondRect").attr("height",parseInt($("#"+modelType+"secondRect").attr("height"))+(model.length-6)*23);
        								}        											        					
			        				}else{
			        					for(var i=0;i<6;i++){
			        						if(model.length<6){
			        							if(i>=model.length){
			        								$("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+i).text("");
			        							}else{
			        								$("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+i).text(model.substring(i,i+1));
			        							}
			        						}else{
			        							$("#"+modelType+"TMA-CMTEXT"+(setupslot+5)+i).text(model.substring(i,i+1));
			        						}
			        					}	
			        				}
			        				$("#"+modelType+"TMA-CMRECT"+(setupslot+5)).css("fill","#CFDCF0");
			        				$("#"+modelType+"TMA-CMG"+(setupslot+5)).attr("class",aeType+"*"+aeId);
			        				$("#"+modelType+"TMA-CMG"+(setupslot+5)+" text").each(function(){
			        					$(this).css("fill","black");
			        				})
			        				tmaIndex++;
			        			}
		        			}
		        			
		        		})
		        	}	
	        	}
	        	
	        },'json');
	        
      	}
      	/*鼠标点击*/
      	function elementDown(evt){
      		var cur = evt.target.parentNode;
      		var curClass = cur.getAttribute("class");
      		if(evt.target.parentNode.parentNode.parentNode.parentNode.getAttribute("id")=="contentDiv"){
      			modelType="manage";
      		}else{
      			modelType="view";
      		}
      		if(curClass!=null && curClass!="" && curClass!=undefined){
      			var array = curClass.split("*");
      			var currentEntityType=array[0];
      			var currentEntityId=array[1];
      			var areaId = $("#areaId").val();
      			//打开某个页面
				open("../physicalres/getPhysicalresForOperaAction?currentEntityType=" 
					+ currentEntityType + "&currentEntityId=" + currentEntityId+"&modelType="+modelType);
      		}else{
      			addElement(evt);
      		}
      		
      		
      	}
      	var st;//定时器
      	/**鼠标mouse over*/
      	function elementOver(evt){
      		if(evt.target.parentNode.parentNode.parentNode.parentNode.getAttribute("id")=="contentDiv"){
      			modelType="manage";
      		}else{
      			modelType="view";
      		}
      		var cur = evt.target.parentNode;
      		var curClass = cur.getAttribute("class");
      		if(curClass!=null && curClass!="" && curClass!=undefined){
      			var array = curClass.split("*");
      			var currentEntityType=array[0];
      			var currentEntityId=array[1];
      			var areaId = $("#areaId").val();
				var params={"currentEntityType":currentEntityType,"currentEntityId":currentEntityId,"loadBasicPage":"loadBasicPage"};
				$.post("../physicalres/getPhysicalresAction",params,function(data){
					clearTimeout(st);
					$("#"+modelType+"rInfo").html(data);
					$("#"+modelType+"infoDiv").css("left",evt.clientX).css("top",evt.clientY).show();
					$("#"+modelType+"infoDiv").show();
					st=setTimeout("$('#"+modelType+"infoDiv').hide()",2000); 
				});
				
      		}
      		
      	}
      	
      	function elementOut(evt){
      		if(evt.target.parentNode.parentNode.parentNode.parentNode.getAttribute("id")=="contentDiv"){
      			modelType="manage";
      		}else{
      			modelType="view";
      		}
      		$("#"+modelType+"infoDiv").hide();
      	}
      	/*增加板件*/
      	function addElement(evt){
      		if(evt.target.parentNode.parentNode.parentNode.parentNode.getAttribute("id")=="contentDiv"){
      			modelType="manage";
      		}else{
      			modelType="view";
      		}
      		var addedResEntityType = "EquipBoard_GSM";
			var addedResParentEntityType = "PrimaryEquipFrame_GSM";
			var addedResParentEntityId = $("#"+modelType+"addedParentEntityId").val();
			var areaId =$("#areaId").val();
			if(addedResEntityType == "" || addedResParentEntityType == "" || addedResParentEntityId == "") {
				alert("请选择要增加的物理资源的类型");
				return false;
			}
			var url = "../physicalres/loadAddPhysicalresPageAction";
			var loadBigPage = "loadBigPage";
			var params = {addedResEntityType:addedResEntityType,addedResParentEntityType:addedResParentEntityType,addedResParentEntityId:addedResParentEntityId,loadBigPage:loadBigPage,areaId:areaId}
			
			//加载新的编辑页面前，先将选中节点设置class
			//var chosenObj = $("#treeDiv span.chosenClass");
			$("#"+modelType+"module").val($("#bizModule").val());
			$("#"+modelType+"processCode").val($("#bizProcessCode").val());
			$("#"+modelType+"processId").val($("#bizProcessId").val());
			$("#"+modelType+"addedEntityType").val(addedResEntityType);
			$("#"+modelType+"addedParentEntityType").val(addedResParentEntityType);
			$("#"+modelType+"addedParentEntityId").val(addedResParentEntityId);
			//获取要被添加的enitty信息
			$.post("../physicalres/loadAddPhysicalresPageAction", params, function(data){
				$("#"+modelType+"addcontent").html(data);
				$("#editDiv").html("");
				var cx = evt.clientX;
				cx = parseInt(cx) - 500;
				$("#"+modelType+"fbox").css("left",cx).css("top",evt.clientY);
				$("#"+modelType+"fbox").show();
				$("#"+modelType+"addcontent").show();
				$("#"+modelType+"infoDiv").show();
				$("#physicalresAttribute tbody").append("<tr style='text-align:center'><td colspan='2'><input type='submit' value='保存'/><input type='button' value='取消' onclick='cancelOperate();'/></td></tr>");
				var cur = evt.target.parentNode;
				var gId = cur.getAttribute("id");
      			$("#"+modelType+"gId").val(gId);
				var setupslot = gId.substring(gId.lastIndexOf("G")+1,gId.length);
				
				gId=gId.replace(modelType,"");
				var model = gId.substring(0,gId.lastIndexOf("G"));
				
				$("input[name='EquipBoard_GSM#model'][type='text']").val(model);
      			if(setupslot==""||setupslot==null){
					setupslot=0;
      				$("input[name='EquipBoard_GSM#setupslot'][type='text']").val("0");
      			}else{
      				if(model=="DXU"){
      				    setupslot = parseInt(setupslot)-4;
      					$("input[name='EquipBoard_GSM#setupslot'][type='text']").val(setupslot);
      				}else if(model=="TMA-CM"){
      					setupslot = parseInt(setupslot)-5;
      					$("input[name='EquipBoard_GSM#setupslot'][type='text']").val(setupslot);
      				}else{
      					$("input[name='EquipBoard_GSM#setupslot'][type='text']").val(setupslot);
      				}	
      			}
      			$("input[name='EquipBoard_GSM#name'][type='text']").val(model+setupslot);
				$("#"+modelType+"addcontent td[class='main-table1-title']").html("<div>填写信息,增加型号为<span style='color:red'>"+model+"</span>,所在槽位是<span style='color:red'>"+setupslot+"</span>的板件<span><a onclick='closeAddInfoBox();' href='javascript:void(0);' style='margin-left: 185px;'>关闭</a></span></div>")
			});
			
      	}
      	//关闭板件增加div
      	function closeAddInfoBox(){
      		$("#"+modelType+"fbox").hide();
      	}
      	 //将一个点从屏幕坐标转换到SVG用户坐标。
        //需要转换的点的坐标从事件参数event中获得。
        function toUserCor(evt){
            //getScreenCTM方法获得的是将一个点从SVG用户坐标转换到屏幕坐标所需的转换矩阵。
            //背后是一些有趣的关于平面坐标变换和矩阵的数学。
            var svg=evt.target.ownerSVGElement;
            var m = svg.getScreenCTM();
            var p = svg.createSVGPoint();
            p.x = evt.clientX;
            p.y = evt.clientY;
            //matrixTransform将一个点按照参数中的矩阵做坐标变换。
            //因为m是将一个点从SVG用户坐标转换到屏幕坐标所需的转换矩阵，所以需要先用inverse方法获得此矩阵的逆阵。
            p = p.matrixTransform(m.inverse());
            return p;
        }
        
        
        
        $(document).keydown(function(event){ //按ESC键退出弹出窗口
			if(event.keyCode==27){
				modelType="manage";
				$("#"+modelType+"fbox").hide();
				$("#"+modelType+"infoDiv").hide();
				modelType="view";
				$("#"+modelType+"fbox").hide();
				$("#"+modelType+"infoDiv").hide();
			}
		}); 
		
		//表单提交验证
	$("#panelForm").validate({
				submitHandler: function(form) {
					//	alert("请选择一个需要保存的节点");
					//	return false;
					$("#panelForm").ajaxSubmit({
						success:function(data){
							if(data!=null && data!=""){
					      		modelType="view";
								var gId = $("#"+modelType+"gId").val();
								if(gId.indexOf("FCU")>=0){
									$("#"+gId+" path").attr("fill","#CFDCF0");
								}else{
									$("#"+gId+" rect").css("fill","#CFDCF0");
								}
								$("#"+gId+" text").css("fill","black");
								$("#"+gId).attr("class","EquipBoard_GSM*"+data);
								$("#"+modelType+"fbox").hide();
							}
						},
						dataType:'json'
					});
				}
			});
	
	//表单AJAX提交
	$("#panelForm").ajaxForm({
		success:function(data){
			if(data!=null && data!=""){
	      		modelType="view";
				var gId = $("#"+modelType+"gId").val();
				if(gId.indexOf("FCU")>=0){
					$("#"+gId+" path").attr("fill","#CFDCF0");
				}else{
					$("#"+gId+" rect").css("fill","#CFDCF0");
				}
				$("#"+gId+" text").css("fill","black");
				$("#"+gId).attr("class","EquipBoard_GSM*"+data);
				$("#"+modelType+"fbox").hide();
			}
		},
		dataType:'json'
	});
	
		
	function cancelOperate(){
		$("#"+modelType+"addcontent input[type='text']").val("");
	}
		
      ]]>
    </script>
  </defs>
  <g id="${param.modelType}panelContent">
  <text style="font-size:20;" y="43" x="30">RBS2206面板</text>
<%--<g id="addPanelElement" onmousedown="addElement(evt);" title="点击增加板件">
<rect style="fill:white;stroke:black;stroke-width:1" height="15" width="16" y="31" x="117"></rect>
<text style="font-size:15;" y="43" x="119">+</text>
<text style="font-size:10;" y="43" x="133">增加板件</text>
</g>  --%>
  <rect id="${param.modelType}firstRect" x="5" y="50" width="169" height="523"  style="fill:white;stroke:black;stroke-width:1"></rect>
  <rect id="${param.modelType}secondRect" x="12" y="50" width="154" height="507" style="fill:white;stroke:black;stroke-width:1"></rect>   
  </g>
</svg>
<div id="${param.modelType}fbox" style='position: absolute;z-index: 1000;display:none; left: 578px; top: 271px;'>
<input type="hidden" id="${param.modelType}gId" value="" />
	<form id="panelForm" action="addPhysicalresForAjaxAction" method="post">
		<input id="${param.modelType}addedParentEntityType" type="hidden" name="addedResParentEntityType" value='${param.currentEntityType}' >
		<input id="${param.modelType}addedParentEntityId" type="hidden" name="addedResParentEntityId" value='${param.currentEntityId}'>
		<input id="${param.modelType}addedEntityType" type="hidden" name="addedResEntityType" >
		<input id="${param.modelType}module" type="hidden" value="" name="bizModule">
		<input id="${param.modelType}processCode" type="hidden" value="" name="bizProcessCode">
		<input id="${param.modelType}processId" type="hidden" value="" name="bizProcessId">
		<div id="${param.modelType}addcontent" style="width:327px"></div>
	</form>
	
</div>
<div id="${param.modelType}infoDiv" style='position: absolute;z-index: 1000;display:none;background: none repeat scroll 0 0 lightsteelblue;'>
	<div class="flattening_mian" style="border: 2px solid #99BBF0;margin-top:0px;">
		<div id='${param.modelType}rInfo'></div>
	</div>
</div>



