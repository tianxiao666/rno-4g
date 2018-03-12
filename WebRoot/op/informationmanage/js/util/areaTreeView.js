

function IscreateTreeView ( opt ) {
	
	var div;			//divid
	var data;			//数据
	var ulId;			//ulId
	var liEventType;	//节点事件类型
	var liClick;		//节点函数
	var liDbclick;		//节点函数
	var liKeyup;		//节点函数
	var liMouseover;		//节点函数
	var spanText;		//li字符
	var liEventFun;
	var liEventClass;
	var showCheckbox;
	var creatingMethod;
	
	var ul;
	
	{
		var defaultOpt = {
							"evFn" : function( lv , data ){} , 
							"spanText" : function( data ){ return "";} , 
							"child" : function(){} , 
							"showCheckbox" : false , 
							"creatingMethod" : function (li,data){}
						};
						
		defaultOpt = $.extend(defaultOpt,opt);
		this.div = defaultOpt.div;
		this.data = defaultOpt.data;
		this.ulId = defaultOpt.ulId;
		this.liEventType = defaultOpt.evType;
		this.liEventFunction = defaultOpt.evFn;
		this.spanText = defaultOpt.spanText;
		this.child = defaultOpt.child;
		this.liClick = defaultOpt.click;
		this.liDbclick = defaultOpt.dbclick;
		this.liKeyup = defaultOpt.keyup;
		this.liMouseover = defaultOpt.mouseover;
		this.showCheckbox = defaultOpt.showCheckbox;
		this.creatingMethod = defaultOpt.creatingMethod;
		
		this.liEventFun = {
			"click" : this.liClick , 
			"dbclick" : this.liDbclick, 
			"keyup" : this.liKeyup, 
			"mouseover" : this.liMouseover
		};   
		
		this.liEventClass = {
			"click" : "treeViewClick" , 
			"dbclick" : "treeViewDbclick" , 
			"keyup" : "treeViewKeyup" , 
			"mouseover" : "treeViewMouseover"
		}; 
	}
	
	
	
	this.createUl = function(){
		var ins = this;
		this.ul = $("<ul/>").attr({"id":ins.ulId,"class":"filetree"});
		$(this.ul).appendTo($(this.div));
		$(this.div).append($(this.ul));
		if ( !this.data ) {
			return;
		}
		createLi( this , $(this.ul) , this.data , 1 );
		
		//treeview事件
		$(this.ul).treeview({
			collapsed: true,
			animated: "fast",
			control:"#sidetreecontrol"
		});
	}
	
	function createLi ( ins , ul , data , lv ) {
		for ( var i = 0 ; data != null && i < data.length ; i++ ) {
			var info = data[i];
			if ( !info ) {
				continue;
			}
			var li = $("<li />");
			var liSpan = $("<span/>").attr({"class":"folder"});
			var sp = ins.spanText(lv,info.data);
			$(liSpan).text(sp);
			$(li).appendTo($(ul));
			if ( ins.showCheckbox ) {
				var checkbox = $("<input type='checkbox' c='" + sp + "' >").appendTo($(li));
			}
			$(liSpan).appendTo($(li));
			if ( ins.creatingMethod ) {
				ins.creatingMethod( $(li) , info );
			}
			for ( var key in ins.liEventFun ) {
				var fn = ins.liEventFun[key];
				var clz = ins.liEventClass[key];
				if ( fn ) {
					$(liSpan).bind( key , { "clz" : clz , "d" : info , "fn" : fn } , function( _ev ){
						var dd = _ev.data.d.data;
						var fn = _ev.data.fn;
						var clz = _ev.data.clz;
						$(ins.div).find("."+clz).removeClass(clz);
						$(this).addClass(clz);
						fn( lv , dd );
					});
				}
			}
			var childData = ins.child( lv , info );
			var childUl = $("<ul/>").appendTo($(li));
			var childLv = lv - 1;
			createLi( ins , childUl , childData , childLv );
		}
		
	}
	this.createUl();
}

function createAreaTree ( div , data , ulId , click ) {
	new IscreateTreeView({
		"div" : div , 
		"data" : data , 
		"ulId" : ulId , 
		"evType" : "click" ,
		"click" : click , 
		"spanText" : function( lv , data ){
					return data.name;
				} , 
		"child" : function ( lv , data ) {
					return data.child;
				}
	});
}

function createAreaTreeWithCreating ( div , data , ulId , click , creating , showCheckbox ) {
	new IscreateTreeView({
		"div" : div , 
		"data" : data , 
		"ulId" : ulId , 
		"evType" : "click" ,
		"click" : click , 
		"spanText" : function( lv , data ){
					return data.name;
				} , 
		"child" : function ( lv , data ) {
					return data.child;
				} , 
		"creatingMethod" : creating	, 
		"showCheckbox" : showCheckbox
	});
}

function createAreaTreeWithCheckbox ( div , data , ulId , click ) {
	new IscreateTreeView({
		"div" : div , 
		"data" : data , 
		"ulId" : ulId , 
		"evType" : "click" ,
		"click" : click , 
		"showCheckbox" : true , 
		"spanText" : function( lv , data ){
					return data.name;
				} , 
		"child" : function ( lv , data ) {
					return data.child;
				}
	});
}

