//企业信息类
function Enterprise(datas) {
	this.id = null; //编号
	this.fullName = null; //企业全称
	this.registerNumber = null; //注册号
	this.ownership = null; //所有制
	this.legalRepresentative = null; //法定代表人
	this.businessSphere = null; //商业范围
	this.registerAddress = null; //注册地址
	this.businessAddress = null; //商业地址
	this.taxBearer = null; //纳税人 
	this.taxRegistrationNumber = null; //纳税代号
	this.zipCode = null; //邮政编码
	this.registerMoney = null; //注册资金
	this.industryType = null; //商业类型
	this.state = null; //企业状态
	this.phone = null; //电话
	this.telautogram = null; //传真
	this.mailbox = null; //邮箱地址
	this.internetUrl = null; //公司网址
	this.cooperative = null; //合作关系
	this.createDate = null; //成立日期
	this.loginAuthority = null; //登录机关
	this.enterpriseSuffix = null; //企业标识后缀

	var span_list = {};

	this.init = function() {
		var instance = this;
		for ( var key in instance) {
			if (!instance.hasOwnProperty(key)) {
				continue;
			}
			span_list[key] = "[column='enterprise#" + key + "']";
			if (datas[key] && datas[key] != "null") {
				this[key] = datas[key];
			} else {
				this[key] = " ";
			}
		}
	}

	{
		this.init();
	}

	//数据对号入座
	this.putInfo = function(div) {
		var ins = this;
		for ( var key in span_list) {
			var widget = $(div).find(span_list[key]);
			if (!widget || $(widget).length <= 0)
				continue;
			$(widget).each(function(index) {
				if ($(this).is("span") || $(this).is("textarea")) {
					$(this).text(ins[key]);
				} else if ($(this).is("input")) {
					$(this).val(ins[key]);
				} else if ($(this).is("img")) {
					$(this).attr( {
						"src" : ins[key]
					});
				} else if ($(this).is("select")) {
					$(this).find("option[value='" + ins[key] + "']").attr( {
						"selected" : "selected"
					});
				} else {
					$(this).text(ins[key]);
				}
			});
		}
	}

}