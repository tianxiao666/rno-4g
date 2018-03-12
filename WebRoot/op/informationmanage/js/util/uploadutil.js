
function UploadUtil ( opt ) {
	this.uploadUrl = null;
	this.uploaded = null;
	this.fileUrl = null;
	
	{
		var defOpt = {
			"uploadUrl" : "information_foreign_ajax!uploadFile.action" , 
			"uploaded" : function ( result) {} , 
			"fileUrl" : ""
		};
		
		opt = $.extend(defOpt, opt);
		
		this.uploadUrl = opt.uploadUrl;
		this.uploaded = opt.uploaded;
		this.fileUrl = opt.fileUrl;
		
	}
	
	
	this.uploadFile = function () {
		var uploadForm = $("<form style='display:none;' method='post'>").attr({"action":this.uploadUrl}).appendTo($("body"));
		var uploadInput = $("<input type='file' name='file' />").appendTo($(uploadForm));
		var uploadUrl = $("<input type='hidden' name='fileUrl' />").val(this.fileUrl).appendTo($(uploadForm));
		var ins = this;
		$(uploadInput).change(function(){
			$(uploadForm).ajaxSubmit(function (result){
				ins.uploaded(result);
				$(uploadForm).remove();
			});
		});
		$(uploadInput).click();
	}
	
	
}








