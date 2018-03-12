/*!
 * Ext JS Library 3.1.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
/**
 * 日期时间控件，可单独显示日期，或日期时间。格式可自己设置。和myDate97设置一样
 * @class Ext.form.DateTimeField
 * @extends Ext.form.TriggerField
 */
Ext.form.DateTimeField = Ext.extend(Ext.form.TriggerField, {
	/**
	 * @param {Array} date97配置列表
	 * @type 
	 */
	dateConfig:null,
	/**
	 * @param {Boolean} 是否显示时间,默认为不显示
	 */
	time:false,
	triggerClass : 'x-form-date-trigger',
	defaultAutoCreate : {tag : "input",type : "text",size : "10",autocomplete : "off"},
	initComponent : function() {
		Ext.form.DateField.superclass.initComponent.call(this);
		this.initDate97js();
		this.initDateConfig();
	},
	onTriggerClick : function(e) {// 点击查找按钮时
		if (this.disabled||this.readOnly) {
			return;
		}
		this.onFocus({});
		var bodyWidth = document.body.clientWidth;
		var xC = document.body.clientWidth - e.xy[0] - this.width;
		var yC = document.body.clientHeight - e.xy[1] - this.height; 
		var x=0;
		var y=0;
		if (xC > 0)
			x = e.xy[0];
		else
			x = document.body.clientWidth - this.width - 4;

		if (yC > 0)
			y = e.xy[1];
		else
			y= document.body.clientHeight - this.height - 4;
//		this.dateConfig['position']={left:e.xy[0],top:e.xy[1]};
		WdatePicker(this.dateConfig);
	},
	initDate97js:function(){
		var obj=this;
		if(!document.getElementById("$date97js")){
			 var  script  =  document.createElement("script");   
			  script.setAttribute("type",   "text/javascript");   
			  script.setAttribute("src",   "jslib/extjs/plugin/DateTimeField/date97/WdatePicker.js");
			  script.setAttribute("id","$date97js");
			  try   
			  {   
				  document.getElementsByTagName("head")[0].appendChild(script);
				  script.onload = script.onreadystatechange = function() {
				 	  if (script.readyState && script.readyState != 'loaded' && script.readyState != 'complete') 
			            return; 
			         script.onreadystatechange = script.onload = null; 
 			         WdatePicker(1);
				 }
			  }catch(e){}
		}
	},
	initDateConfig:function()
	{
		if(!this.dateConfig)
			this.dateConfig=new Array();
		if(!this.dateConfig['el'])
			this.dateConfig['el']=this.id;
		if(this.time)
			this.addDateConfig("dateFmt",'yyyy-MM-dd HH:mm:ss');
		else
			if(!this.dateConfig["dateFmt"])
				this.dateConfig["dateFmt"]='yyyy-MM-dd';
		if(!this.dateConfig["skin"])
			this.dateConfig["skin"]='ext';
	},
	addDateConfig:function(name,value)
	{
		this.removeDateConfig(name);
		this.dateConfig[name]=value;
	},
	removeDateConfig:function(name){
		for (var i = 0; i < this.dateConfig.length; i++) {
			var temp = this.dateConfig[i];
			if (temp && temp.split(':')[0] == name) {
				this.dateConfig.pop(i);
				return;
			}
		}
	},
	setDateConfig:function(config)
	{
		this.dateConfig=config;
		this.initDateConfig();
	}
	

});
 