package com.iscreate.op.service.rno.parser;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HadoopXml;

public class MrrParserContext {

	private long areaId;
	private long cityId;
	
	//相关的preparedstatment
	private Map<String,PreparedStatement> preparedStmts=new HashMap<String,PreparedStatement>();
	
	private StringBuilder errMsg=new StringBuilder();

	DateUtil dateUtil=new DateUtil();
	
	//记录爱立信mrr文件中的adm类型数据数量，用于rowkey
	private int admNum = 0;
	//记录爱立信mrr文件测量日期
	private String meaDate = "";

	private Map<String,HTable> hTables = new HashMap<String,HTable>();
	//每个HTable对应的put队列
	private Map<String,List<Put>> putsList = new HashMap<String, List<Put>>();
	
	//华为mrr的速率类型
	private String rateType;
	private boolean isValidateCity = false;
	
	
	public boolean isValidateCity() {
		return isValidateCity;
	}

	public void setValidateCity(boolean isValidateCity) {
		this.isValidateCity = isValidateCity;
	}

	public String getMeaDate() {
		return meaDate;
	}

	public void setMeaDate(String meaDate) {
		this.meaDate = meaDate;
	}

	public int getAdmNum(){
		admNum++;
		return admNum;
	}
	public void initAdmNum() {
		this.admNum = 0;
	}
	
	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	
	public void appedErrorMsg(String msg){
		errMsg.append(msg);
	}
	
	public String getErrorMsg(){
		return errMsg.toString();
	}
	
	public DateUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	public void closeAllStatement() {
		for(PreparedStatement s:preparedStmts.values()){
			if(s!=null){
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public boolean setPreparedStatment(String name,
			PreparedStatement stmt) {
		if(name==null || "".equals(name.trim()) || stmt==null){
			return false;
		}
		preparedStmts.put(name, stmt);
		
		return true;
	}

	public PreparedStatement getPreparedStatement(String name){
		if(name==null || "".equals(name.trim())){
			return null;
		}
		return preparedStmts.get(name);
	}

	
	public void clearBatchAllStatement() {
		for(PreparedStatement s:preparedStmts.values()){
			if(s!=null){
				try {
					s.clearBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 增加需要使用的HTable表
	 * @param key 获取table的key
	 * @param tableName  表名
	 * @author peng.jm
	 * @throws IOException 
	 * @date 2014-12-5上午11:18:20
	 */
	public boolean addHtable(String key, String tableName, Configuration conf) throws IOException {
		if(key==null || "".equals(key.trim())){
			return false;
		}
		HTable t = new HTable(conf,tableName);
		t.setAutoFlushTo(false);
		hTables.put(key, t);
		return true;
	}

	/**
	 * 获取需要使用的HTable表
	 * @param key
	 * @return
	 * @author peng.jm
	 * @date 2014-12-5上午11:29:30
	 */
	public HTable getHtable(String key) {
		if(key==null || "".equals(key.trim())){
			return null;
		}
		return hTables.get(key);
	}

	/**
	 * 增加对应key的put队列
	 * @param key
	 * @author peng.jm
	 * @date 2014-12-5上午11:33:41
	 */
	public boolean addPuts(String key) {
		if(key==null || "".equals(key.trim())){
			return false;
		}
		putsList.put(key, new ArrayList<Put>());
		return true;
	}
	
	/**
	 * 获取对应key的put队列
	 * @param key
	 * @return
	 * @author peng.jm
	 * @date 2014-12-5上午11:39:27
	 */
	public List<Put> getPuts(String key) {
		if(key==null || "".equals(key.trim())){
			return null;
		}
		return putsList.get(key);
	}

	/**
	 * 清除put队列
	 * @author peng.jm
	 * @date 2014-12-5上午11:36:37
	 */
	public void clearPuts() {
		for(List<Put> list : putsList.values()){
			if(list.size() > 0){
				list.clear();
			}
		}
	}

	/**
	 * 关闭对象内的HTable对象
	 * @author peng.jm
	 * @throws  
	 * @date 2014-12-5上午11:58:08
	 */
	public void closeHTables() {
		for(HTable t : hTables.values()){
			if(t != null){
				try {
					t.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 提交htable数据
	 * @author peng.jm
	 * @throws InterruptedIOException 
	 * @throws RetriesExhaustedWithDetailsException 
	 * @date 2014-12-5下午02:13:55
	 */
	public void tableFlushCommits() throws RetriesExhaustedWithDetailsException, InterruptedIOException {
		for(HTable t : hTables.values()){
			if(t != null){
				t.flushCommits();
			}
		}
	}

	/**
	 * 获取华为的速率类型
	 * @param rateType
	 * @author peng.jm
	 * @date 2014-12-6下午05:21:49
	 */
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public String getRateType() {
		return rateType;
	}
	
}
