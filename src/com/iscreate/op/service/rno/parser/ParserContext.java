package com.iscreate.op.service.rno.parser;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;

import com.iscreate.op.service.rno.parser.vo.NcsAdmRecord;
import com.iscreate.op.service.rno.parser.vo.NcsCellData;
import com.iscreate.op.service.rno.parser.vo.NcsFreqNotReport;
import com.iscreate.op.service.rno.parser.vo.NcsNcellData;
import com.iscreate.op.service.rno.parser.vo.NcsUmfiNotReport;
import com.iscreate.op.service.rno.parser.vo.NcsUmtsCellData;
import com.iscreate.op.service.rno.parser.vo.NcsUmtsNcellData;
import com.iscreate.op.service.rno.tool.DateUtil;

public class ParserContext {


	private NcsAdmRecord ncsAdmRecord;
	private NcsCellData ncsCellData;
	private NcsNcellData ncsNcellData;
	private NcsFreqNotReport ncsFreqNotReport;
	private NcsUmtsCellData ncsUmtsCellData;
	private NcsUmtsNcellData ncsUmtsNcellData;
	private NcsUmfiNotReport ncsUmfiNotReport;
	
	private long areaId;
	private String bscName;
	private String freqSection;
	private long cityId;

	//每个ncs文件对应的adm record列表，
	private Map<Long,List<NcsAdmRecord>> ncsAdminRecs=new HashMap<Long,List<NcsAdmRecord>>();
	
	private StringBuilder errMsg=new StringBuilder();
	
	//相关的preparedstatment
	private Map<String,PreparedStatement> preparedStmts=new HashMap<String,PreparedStatement>();
	//hbase puts集合
	private Map<String,List<Put>> hbasePuts=new HashMap<String,List<Put>>();
	
	private Date meaTime;
	
	private DateUtil dateUtil=new DateUtil();
	private void init(){
		ncsAdmRecord =new NcsAdmRecord();
		ncsCellData=new NcsCellData();
		ncsNcellData=new NcsNcellData();
		ncsFreqNotReport=new NcsFreqNotReport();
		ncsUmtsCellData=new NcsUmtsCellData();
		ncsUmtsNcellData=new NcsUmtsNcellData();
		ncsUmfiNotReport=new NcsUmfiNotReport();
	}
	
	public ParserContext() {
		init();
	}

	public ParserContext(FileOutputStream fos) {
		init();
		
	}
	
	public NcsAdmRecord getNcsAdmRecord() {
		return ncsAdmRecord;
	}

	public void setNcsAdmRecord(NcsAdmRecord ncsAdmRecord) {
		this.ncsAdmRecord = ncsAdmRecord;
	}

	public NcsCellData getNcsCellData() {
		return ncsCellData;
	}

	public void setNcsCellData(NcsCellData ncsCellData) {
		this.ncsCellData = ncsCellData;
	}

	public NcsNcellData getNcsNcellData() {
		return ncsNcellData;
	}

	public void setNcsNcellData(NcsNcellData ncsNcellData) {
		this.ncsNcellData = ncsNcellData;
	}

	public NcsFreqNotReport getNcsFreqNotReport() {
		return ncsFreqNotReport;
	}

	public void setNcsFreqNotReport(NcsFreqNotReport ncsFreqNotReport) {
		this.ncsFreqNotReport = ncsFreqNotReport;
	}

	public NcsUmtsCellData getNcsUmtsCellData() {
		return ncsUmtsCellData;
	}

	public void setNcsUmtsCellData(NcsUmtsCellData ncsUmtsCellData) {
		this.ncsUmtsCellData = ncsUmtsCellData;
	}

	public NcsUmtsNcellData getNcsUmtsNcellData() {
		return ncsUmtsNcellData;
	}

	public void setNcsUmtsNcellData(NcsUmtsNcellData ncsUmtsNcellData) {
		this.ncsUmtsNcellData = ncsUmtsNcellData;
	}

	public NcsUmfiNotReport getNcsUmfiNotReport() {
		return ncsUmfiNotReport;
	}

	public void setNcsUmfiNotReport(NcsUmfiNotReport ncsUmfiNotReport) {
		this.ncsUmfiNotReport = ncsUmfiNotReport;
	}
	
	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getBscName() {
		return bscName;
	}

	public void setBscName(String bscName) {
		this.bscName = bscName;
	}

	public String getFreqSection() {
		return freqSection;
	}

	public void setFreqSection(String freqSection) {
		this.freqSection = freqSection;
	}

	public void appedErrorMsg(String msg){
		errMsg.append(msg);
	}
	
	public String getErrorMsg(){
		return errMsg.toString();
	}
	
	public void clearErrorMsg(){
		errMsg.setLength(0);
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public DateUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	//增加管理头部记录
	public void addNcsAdmRecord(long ncsId, NcsAdmRecord rec) {
		List<NcsAdmRecord> recs=ncsAdminRecs.get(ncsId);
		if(recs==null){
			recs=new ArrayList<NcsAdmRecord>();
			ncsAdminRecs.put(ncsId, recs);
		}
		recs.add(rec);
	}
	
	public List<Long> getAllNcsId(){
		return new ArrayList<Long>(ncsAdminRecs.keySet());
	}
	
	/**
	 * 获取最后一个头部信息
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-1-13 下午3:26:33
	 */
	public NcsAdmRecord getLastNcsAdmRecord(long ncsId){
		if(ncsAdminRecs.containsKey(ncsId)){
			List<NcsAdmRecord> recs=ncsAdminRecs.get(ncsId);
			if(recs.size()>0){
				return recs.get(recs.size()-1);
			}
		}
		
		return null;
	}
	
	
	public boolean setPreparedStatment(String name,PreparedStatement stmt){
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

	public Date getMeaTime() {
		return meaTime;
	}

	public void setMeaTime(Date meaTime) {
		this.meaTime = meaTime;
	}
	public List<Put> getHbasePuts(String name) {
		if(name==null || "".equals(name.trim())){
			return null;
		}
		return hbasePuts.get(name);
	}

	public boolean setHbasePuts(String name, List<Put> puts) {
		if(name==null || "".equals(name.trim()) || puts==null){
			return false;
		}
		hbasePuts.put(name, puts);
		return true;
	}
	
}
