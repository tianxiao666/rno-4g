function Table2Table ( opt ) {
	
	var arr;
	var show;
	var del;
	var add;
	var prepare;
	var attrName;
	
	var delClass;
	var addClass;
	var delMethod;
	var addMethod;
	var instance;
	var checkDataId;
	var addSame;
	var different;
	
	{
		this.show = new Object();
		this.del = new Object();
		this.add = new Object();
		this.prepare = new Array();
		this.delClass = opt.delClass;
		this.addClass = opt.addClass;
		this.delMethod = opt.delMethod;
		this.addMethod = opt.addMethod;
		this.attrName = opt.attrName;
		this.addSame = opt.addSame;
		this.checkDataId = opt.checkDataId;
		this.different = opt.different;
		instance = this;
		
		//删除
		$(instance.delClass).live("click" , function(){
			var data = $(this).attr(instance.attrName);
			data = eval("(" + data + ")");
			var d = instance.delMethod( data , this );
			
			instance.del[d] = data;
			delete instance.add[d];
			delete instance.show[d];
			instance.refreshTd();
			//instance.showKey();
		});
		
		//添加
		$(instance.addClass).live("click" , function(){
			var data = $(this).attr(instance.attrName);
			data = eval("(" + data + ")");
			var d = instance.addMethod( data , this );
			
			instance.add[d] = data;
			instance.show[d] = data;
			delete instance.del[d];
			instance.refreshTd();
		});
	}
	
	//添加显示数据
	this.addShow = function( keyMethod , arr ){
		for ( var i = 0 ; i < arr.length ; i++ ) {
			var key = keyMethod( i , arr[i] );
			this.show[key] = arr[i];
		}
	}
	
	//清除显示数据
	this.clearShow = function () {
		this.show = {};
	}
	
	this.clearData = function() {
		this.add = {};
		this.del = {};
	}
	
	this.showKey = function () {
		var str = "";
		for( var key in this.show ) {
			str += "," + key;
		}
		console.log(str);
	}
	
	
	this.refreshTd = function() {
		$(instance.addClass).each(function(){
			var res = eval( "(" + $(this).attr(instance.attrName) + ")" );
			var ch = instance.checkDataId( res );
			if ( instance.show[ch] ) {
				instance.addSame( this );
			} else {
				instance.different( this );
			}
		});
	}
	
	this.getDelArray = function(){
		return this.del;
	}
	
	this.getAddArray = function(){
		return this.add;
	}
	
}