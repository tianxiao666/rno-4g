package com.iscreate.op.service.rno.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;

public class GsmDataTrafficStaticsFileParser extends
		GsmAudioTrafficStaticsFileParser {
	private static Log log = LogFactory
			.getLog(GsmDataTrafficStaticsFileParser.class);
	public GsmDataTrafficStaticsFileParser(){
		log.info("初始化GsmDataTrafficStaticsFileParser对象。。。");
		super.NET_TYPE=RnoConstant.DBConstant.NET_TYPE_GSM;
		super.SPEC_TYPE=RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_DATA;
	}
}
