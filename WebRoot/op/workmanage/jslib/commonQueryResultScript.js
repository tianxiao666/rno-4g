
//工单记录行双击事件
function workOrderRowDbClickAction(grid, rowIndex,e){
   // alert("你双击了第" + rowIndex  + "行!");  
    //alert(grid.getStore().getAt(rowIndex).data.woId); 
    
    var woId = grid.getStore().getAt(rowIndex).data.woId;
    var formUrl = grid.getStore().getAt(rowIndex).data.formUrl;
    
    
    //更新成已读
	 Ext.Ajax.request({
	   url: 'updateWorkOrderReadStatusAction',
	   params: { WOID: woId },
	   method: 'post',
	   async: false,
	   success: function(request){
		   //var message = request.responseText;		 
		   //var json = Ext.util.JSON.decode(message);
	   },
	   failure:function(){

	   }
	 });
    
    
    //window.location.href=FORMURL+"?WOID="+WOID+"&TOID="+TOID;
    var url = formUrl+"?WOID="+woId;
    window.open(url,"_blank");
    
    
}


//任务单记录行双击事件
function taskOrderRowDbClickAction(grid, rowIndex,e){
     //alert("你双击了第" + rowIndex + "行!");  
     //alert(grid.getStore().getAt(rowIndex).data.woId);
     
     var woId = grid.getStore().getAt(rowIndex).data.woId;
     var toId = grid.getStore().getAt(rowIndex).data.toId;
     var formUrl = grid.getStore().getAt(rowIndex).data.formUrl;
     
     
     //更新成已读
	 Ext.Ajax.request({
	   url: 'updateTaskOrderReadStatusAction.action',
	   params: { TOID: toId },
	   method: 'post',
	   async: false,
	   success: function(request){
		   //var message = request.responseText;		 
		   //var json = Ext.util.JSON.decode(message);
	   },
	   failure:function(){
	   	
	   }
	 });
	 
	 var url = formUrl+"?WOID="+woId+"&TOID="+toId;
     window.open(url,"_blank");
}


//工单行样式控制
function workOrderRowResultView(record, rowIndex, p, ds){
	var requireCompleteTime = record.data.requireCompleteTime;
	var nowTime = record.data._NOWTIME;
	var halfLaterTime = record.data._HALFLATERTIME;
	var isRead = record.data.isRead;
	
	//alert("requireCompleteTime=="+requireCompleteTime+",nowTime=="+nowTime+",halfLaterTime=="+halfLaterTime);
	
	//0:未读
	//1:已读
	//2：未读，状态改变
	//3：已读，状态改变
	
	
	var returnCls="";
	
	//工单未读
	if((isRead && isRead=="0") || (isRead && isRead=="2")){
		returnCls+=" x-grid-record-font-bold ";
	}
	//工单已读
	else if((isRead && isRead=="1") || (isRead && isRead=="3")){
		returnCls+=" x-grid-record-font-normal ";
	}
	
	//alert("isRead=="+isRead+",returnCls==="+returnCls);
	
	return returnCls;	
}




//任务单行样式控制
function taskOrderRowResultView(record, rowIndex, p, ds){
	var requireCompleteTime = record.data.requireCompleteTime;
	var nowTime = record.data._NOWTIME;
	var halfLaterTime = record.data._HALFLATERTIME;
	var isRead = record.data.isRead;
	
	//alert("completeTime=="+completeTime+",nowTime=="+nowTime+",halfLaterTime=="+halfLaterTime);
	
	//0:未读
	//1:已读
	//2：未读，状态改变
	//3：已读，状态改变
	
	
	var returnCls="";
	
	//工单未读
	if((isRead && isRead=="0") || (isRead && isRead=="2")){
		returnCls+=" x-grid-record-font-bold ";
	}
	//工单已读
	else if((isRead && isRead=="1") || (isRead && isRead=="3")){
		returnCls+=" x-grid-record-font-normal ";
	}
	
	//alert("isRead=="+isRead+",returnCls==="+returnCls);
	
	return returnCls;	
}