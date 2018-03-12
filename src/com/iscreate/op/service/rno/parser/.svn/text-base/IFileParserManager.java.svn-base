package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.util.Map;

import com.iscreate.op.service.rno.parser.FileParserManager.ParserToken;

public interface IFileParserManager {

	public String parserData(final File file, final String fileCode,
			final Boolean needPersist, final Boolean autoload,
			final Boolean update, final Long oldConfigId, final Long areaId, Map<String,Object> attachParams);
	
	public boolean updateTokenProgress(String token, float progress);
	
	public boolean updateTokenProgress(String token, float progress,String msg);
	
	public boolean updateTokenProgress(String token, String msg); 
	
	public boolean tokenFail(String token) ;
	
	public ParserToken getToken(String token);
	
	/**
	 * 销毁token
	 * 
	 * @param token
	 * @return Sep 9, 2013 12:40:53 PM gmh
	 */
	public boolean destroyToken(String token);
	
	/**
	 * 产生token
	 * 
	 * @param fileCode
	 * @return Sep 9, 2013 12:19:20 PM gmh
	 */
	public String assignToken(String fileCode);

	
}
