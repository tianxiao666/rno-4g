//------------------------------------------------------------//

function RuleSetting(setting, fromDB) {
	if (fromDB == true) {
		this.minValue = setting.MIN_VALUE ? setting.MIN_VALUE : 0;
		this.maxValue = setting.MAX_VALUE ? setting.MAX_VALUE : 0;
		this.style = setting.STYLE ? eval("(" + setting.STYLE + ")") : {};
		this.name = setting.NAME ? setting.NAME : "";
		this.params=setting.PARAMS?eval("(" + setting.PARAMS + ")") : {};
	} else {
		this.minValue = setting.minValue ? setting.minValue : 0;
		this.maxValue = setting.maxValue ? setting.maxValue : 0;
		this.style = setting.style ? eval("(" + setting.style + ")") : {};
		this.name = setting.name ? setting.name : "";
		this.params = setting.params ? eval("(" + setting.params + ")") : {};
	}
}

RuleSetting.prototype.print=function(){
	//console.log("rule settin:name="+this.name+",minValue="+this.minValue+",maxValue="+this.maxValue+",style="+this.style.color+",params="+this.params.cellnum);
}

RuleSetting.prototype.getStyle = function() {
	return this.style;
}

// ------------------------------------------------------------//
// 渲染所使用的数据结构
function RendererRule(ruleName, rawData) {
	this.ruleName = ruleName;
	this.ruleSettings = new Array();// 元素是 RuleSetting
	this.rawData = rawData;
}
//获取rule数组
RendererRule.prototype.getRuleSettings = function() {
	 //console.log("addRuleSetting . ruleSetting=" );
	return this.ruleSettings;
}
RendererRule.prototype.addRuleSetting = function(ruleSetting) {
	 //console.log("addRuleSetting . ruleSetting=" );
	 //ruleSetting.print();
	if (ruleSetting) {
		this.ruleSettings.push(ruleSetting);
	}
}

RendererRule.prototype.print=function(){
	//console.log("rule name="+this.ruleName+",rule setting details is as folows:length="+this.ruleSettings.length);
	for(var i=0;i<this.ruleSettings.length;i++){
		this.ruleSettings[i].print();
	}
	
}
RendererRule.prototype.findRuleSetting = function(value) {
	if (value == null || value == undefined) {
		//console.error("findRuleSetting的参数为空！");
		return null;
	}
	var fitForAll=null;
	var exactOne=null;
	for ( var i = 0; i < this.ruleSettings.length; i++) {
		if(!this.ruleSettings[i] ){
			continue;
		}
		// 2013-11-8 modify take null value into account
		if (this.ruleSettings[i].minValue && typeof this.ruleSettings[i].minValue == "number"
				&& this.ruleSettings[i].maxValue
				&& typeof this.ruleSettings[i].maxValue == "number") {
			// min_value ,max_value都有数据
			if (this.ruleSettings[i].minValue <= value
					&& this.ruleSettings[i].maxValue > value) {
				exactOne= this.ruleSettings[i];
				break;
			}
		} else {
			if (this.ruleSettings[i].minValue && typeof this.ruleSettings[i].minValue == "number") {
				// 只有min_value有数据
				if (this.ruleSettings[i].minValue <= value) {
					exactOne= this.ruleSettings[i];
					break;
				}
			} else if (this.ruleSettings[i].maxValue
					&& typeof this.ruleSettings[i].maxValue == "number") {
				// 只有max_value有数据
				if (this.ruleSettings[i].maxValue > value) {
					exactOne= this.ruleSettings[i];
					break;
				}
			} else {
				// min_value,max_value都没有数据.都是null，表示适合全部过程。但是优先级最低
				fitForAll= this.ruleSettings[i];
			}
		}
	}
	//console.log("find value="+value+", result ="+(exactOne==null?(fitForAll==null?"未找到":"找到模糊匹配的"):'精确找到'));
	if(exactOne!=null){
		//console.log("精确规则为：");
		//exactOne.print();
		return exactOne;
	}
	return fitForAll;
}

// ------------------------------------------------------------//
// 负责渲染
function RendererFactory(needCache) {
	this.rules = new Array();// 元素为 RendererRule
	if (needCache == true) {
		this.cache = true;
	} else {
		this.cache = false;
	}
}

RendererFactory.prototype.addRule = function(rule) {
	if (rule) {
		this.rules.push(rule);
	}
}

RendererFactory.prototype.clearAllRules = function() {
	this.rules.splice(0, this.rules.length);
}

RendererFactory.prototype.getRule=function(ruleName){
	if(!ruleName){
		return null;
	}
	for(var i=0;i<this.rules.length;i++){
		if(this.rules[i].ruleName===ruleName){
			return this.rules[i];
		}
	}
	return null;
}


/**
 * 
 * @param ruleCode
 * @param areaId
 * @param callback
 *            回调函数
 * @param needRawData
 *            在调用callback的时候，是否传递原始数据
 * @returns
 */
RendererFactory.prototype.findRule = function(ruleCode, areaId, callback,
		needRawData) {
//	console.log("in findRule.ruleCode=" + ruleCode + ", areaId=" + areaId
//			+ ",callback=" + callback + ",needRawData=" + needRawData);
	if (!ruleCode || ruleCode == "") {
		return null;
	}

	var me = this;

	// 访问服务器得到
	var find = false;
	$.ajax({
		url : 'getRnoTrafficRendererAction',
		data : {
			'trafficCode' : ruleCode,
			'areaId' : areaId
		},
		dataType : 'json',
		type : 'post',
		async : false,
		success : function(data) {
			//console.log("factory 返回数据。" + data);
			if (data) {
				var rule = new RendererRule(ruleCode, data);
				var list = data['returnList'];
				if (list instanceof Array) {
					//console.log("is array.");
					var setting;
					for ( var j = 0; j < list.length; j++) {
						//console.log(list[j].toSource());
						setting=new RuleSetting(list[j],true);
						if(setting){
							//console.log("添加一个setting：");
							//setting.print();
							rule.addRuleSetting(setting);
						}else{
							//console.log("不能创建setting！");
						}
					}
				} else {
					//console.log("not array.")
					rule.addRuleSetting(new RuleSetting(list,true));
				}
				me.addRule(rule);

				//console.log("in factory.rule ==");
				//rule.print();
				//console.log("in factory ,rule.rawData=" + rule.rawData);
				if (typeof callback == "function") {
					if (needRawData === true) {
						callback(data, ruleCode, areaId);
					} else {
						callback(rule, ruleCode, areaId);
					}
				}

			}
		},
		error : function(xhr, textstatus, e) {
			//console.log("返回渲染规则出错！" + textstatus);
		}
	});

}
