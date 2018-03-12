package com.iscreate.op.service.rno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GFasQueryCond;
import com.iscreate.op.action.rno.model.Eri2GMrrQueryCond;
import com.iscreate.op.action.rno.model.LteKpiQueryCond;
import com.iscreate.op.action.rno.model.LteStsIndexQueryCond;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoIndexDisplayDao;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HadoopXml;

public class RnoIndexDisplayServiceImpl implements RnoIndexDisplayService {

	private static Log log = LogFactory
			.getLog(RnoIndexDisplayServiceImpl.class);
	private RnoIndexDisplayDao rnoIndexDisplayDao;
	
	private MemcachedClient memCached;
	
	private DateUtil dateUtil=new DateUtil();
	
	public RnoIndexDisplayDao getRnoIndexDisplayDao() {
		return rnoIndexDisplayDao;
	}
	public void setRnoIndexDisplayDao(RnoIndexDisplayDao rnoIndexDisplayDao) {
		this.rnoIndexDisplayDao = rnoIndexDisplayDao;
	}
	public MemcachedClient getMemCached() {
		return memCached;
	}
	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}
	/**
	 * 
	 * @title 查询爱立信2G小区MRR的各类指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-18上午10:22:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEri2GCellMrrIndex(
			final Eri2GMrrQueryCond cond){
		//HBase数据源
		return queryEri2GCellMrrIndexFromHBase(cond);
		
		//oracle数据源
		//return rnoIndexDisplayDao.queryEri2GCellMrrIndex(cond);
	}
	
	/**
	 * 从HBase中获取爱立信mrr的指标展现数据
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-12-9下午05:59:40
	 */
	private List<Map<String, Object>> queryEri2GCellMrrIndexFromHBase(
			Eri2GMrrQueryCond cond) {
				
		long cityId = cond.getCityId();
		String cell = cond.getCell();
		String meaBegTime = cond.getMeaBegTime()+" 00:00:00";
		String meaEndTime = cond.getMeaEndTime()+" 23:59:59";
		long meaBegMillis = dateUtil.parseDateArbitrary(meaBegTime).getTime();
		long meaEndMillis = dateUtil.parseDateArbitrary(meaEndTime).getTime();
		
		//-----2014-12 加入 start
		List<Long> dateList = new ArrayList<Long>();
		//HashSet<Integer> monSet = new HashSet<Integer>();

		DateUtil dateUtil = new DateUtil();
		Date meaBegDate = dateUtil.parseDateArbitrary(meaBegTime);
		Date meaEndDate = dateUtil.parseDateArbitrary(meaEndTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(meaBegDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int begYear = cal.get(Calendar.YEAR);
		int begMonth = cal.get(Calendar.MONTH)+1;
		cal.setTime(meaEndDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int endYear = cal.get(Calendar.YEAR);
		int endMonth = cal.get(Calendar.MONTH)+1;

		HTable idxTable = null;
		Result idxRes = null;
		NavigableMap<byte[], byte[]> idxNavMap = null;
		try {
			idxTable = new HTable(HadoopXml.getHbaseConfig(), "LTE4G_CELL_MEATIME");
			//起始日期
			Get begIdxGet = new Get(Bytes.toBytes(cityId+"_MRR_"+begYear+"_"+cell));
			idxRes = idxTable.get(begIdxGet);
			if(idxRes != null && !idxRes.isEmpty()) {
				idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(begMonth+""));
				for (byte[] d : idxNavMap.keySet()) {
					long da = Long.parseLong(new String(d));
					if(da>=meaBegMillis && da<=meaEndMillis) {
						dateList.add(Long.parseLong(new String(d)));
					}
				}
			}
			if(idxNavMap != null && !idxNavMap.isEmpty()) {
				idxNavMap.clear();
			}
			//判断是否在同一个月内 ， 是则不需要再获取结束日期的对应的索引，以免重复
			if(!(begYear == endYear && begMonth == endMonth)) {
				//结束日期
				Get endIdxGet = new Get(Bytes.toBytes(cityId+"_MRR_"+endYear+"_"+cell));
				idxRes = idxTable.get(endIdxGet);
				if(idxRes != null && !idxRes.isEmpty()) {
					idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(endMonth+""));
					for (byte[] d : idxNavMap.keySet()) {
						long da = Long.parseLong(new String(d));
						if(da>=meaBegMillis && da<=meaEndMillis) {
							dateList.add(Long.parseLong(new String(d)));
						}
					}		
				}
			}

			idxTable.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			try {
				idxTable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.error("爱立信MRR指标展现中，读取HBase的小区索引数据出错！");
			return Collections.emptyList();
		}
		if(dateList.size() == 0) {
			log.error("爱立信MRR指标展现中，该小区不存在数据！");
			return Collections.emptyList();
		}
		//获取信道组号
		String chgr = cond.getChgr();
		List<String> chgrList = new ArrayList<String>();
		if(!("ALL").equals(chgr)) {
			chgrList.add(chgr);
		} else {
			for (int i = 0; i < 16; i++) {
				chgrList.add(i+"");
			}
		}
		//整理需要获取的get列
		List<Get> getList = new ArrayList<Get>();
		for (Long d : dateList) {
			for (String chg : chgrList) {
				Get get = new Get(Bytes.toBytes(cityId+"_"+d+"_"+cell+"_"+chg));
				getList.add(get);
			}
		}
		
		String dataType = cond.getDataType();
		String tableName = "";
		
		if(("Rxlev").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_STRENGTH";
		} else if(("RxQual").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_QUALITY";
		} else if(("POWER").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_POWER";
		} else if(("PATHLOSS").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_PL";
		} else if(("PLDIFF").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_PLD";
		} else if(("TA").equals(dataType)) {
			tableName = "RNO_2G_ERI_MRR_TA";
		}
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		
		HTable table = null;
		NavigableMap<byte[], byte[]> naviMap=null;
		//System.out.println(getList.size());
		try {
			table = new HTable(HadoopXml.getHbaseConfig(), tableName);
			Result[] ress = table.get(getList);
			
			for (int i = 0; i < ress.length; i++) {
				
				Result res = ress[i];
				if(res.isEmpty()) {
					continue;
				}
				naviMap =  res.getFamilyMap(Bytes.toBytes("val"));
				String sKey = "";
				for(byte[] bKey : naviMap.keySet()) {
					sKey = new String(bKey,"UTF-8");
					if(("CELL").equals(sKey)) {
						map.put("CELL_NAME", new String(naviMap.get(bKey),"UTF-8"));
						continue;
					}
					else if(("CHGR").equals(sKey)) {
						if(!("ALL").equals(chgr)) {
							map.put("CHANNEL_GROUP_NUM", new String(naviMap.get(bKey),"UTF-8"));
						}
						continue;
					}
					else if(("CITY_ID").equals(sKey)) {
						continue;
					}
					else if(("MEA_DATE").equals(sKey)) {
						continue;
					}
					else if(("SUBCELL").equals(sKey)) {
						continue;
					}
					else{
						int v = map.get(sKey)==null?0:Integer.parseInt(map.get(sKey).toString());
						int totVal = v+Integer.parseInt(new String(naviMap.get(bKey),"UTF-8"));
						//累加
						map.put(sKey, totVal);
	
					}
				}
			}
			if(map.size() > 0) {
				result.add(map);
			}
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				table.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("爱立信MRR指标展现中，读取HBase数据出错！");
			return Collections.emptyList();
		}
		return result;
		//-----2014-12 加入 end
		
//		String chgr = cond.getChgr();
//		String dataType = cond.getDataType();
//		
//		String tableName = "";
//		if(("Rxlev").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_STRENGTH";
//		} else if(("RxQual").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_QUALITY";
//		} else if(("POWER").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_POWER";
//		} else if(("PATHLOSS").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_PL";
//		} else if(("PLDIFF").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_PLD";
//		} else if(("TA").equals(dataType)) {
//			tableName = "RNO_2G_ERI_MRR_TA";
//		}
//		
//		String startKey = cityId+"_"+meaBegMillis;
//		String endKey = cityId+"_"+meaEndMillis+"_~";
//		
//		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
//		HTable table = null;
//		ResultScanner rs = null;
//		
////		long s = System.currentTimeMillis();
//		try {
//			table = new HTable(HadoopXml.getHbaseConfig(), tableName);
//			Scan scan = new Scan();
//
//			scan.setCacheBlocks(false);
//			scan.setCaching(500); 
//			scan.setStartRow(Bytes.toBytes(startKey));
//			scan.setStopRow(Bytes.toBytes(endKey));
//			FilterList filterList = new FilterList();
//			//加入CELL_NAME过滤条件
//			Filter cellFilter = new SingleColumnValueFilter(Bytes
//					.toBytes("val"), Bytes.toBytes("CELL"),
//					CompareOp.EQUAL, Bytes.toBytes(cell));
//			filterList.addFilter(cellFilter);
//			//加入CHGR过滤条件
//			if(!("ALL").equals(chgr)) {
//				Filter chgrFilter = new SingleColumnValueFilter(Bytes
//						.toBytes("val"), Bytes.toBytes("CHGR"),
//						CompareOp.EQUAL, Bytes.toBytes(chgr)); 
//				filterList.addFilter(chgrFilter);
//			}
//			
//			scan.setFilter(filterList);
//			rs = table.getScanner(scan);
////			System.out.println("查询耗时："+(System.currentTimeMillis()-s)/1000);
////			s = System.currentTimeMillis();
//			NavigableMap<byte[], byte[]> naviMap=null;
//			Map<String,Object> map = new HashMap<String, Object>();
//
//		//	for(Result res=rs.next();res!=null;res=rs.next()){ 
//			Result res = rs.next();
////			System.out.println("获取第一次next耗时："+(System.currentTimeMillis()-s)/1000);
////			s = System.currentTimeMillis();
////			long s2 = System.currentTimeMillis();
//			while(res != null) {
////				System.out.println("获取map时间："+(System.currentTimeMillis()-s2)/1000);
////				s2 = System.currentTimeMillis();
//				naviMap =  res.getFamilyMap(Bytes.toBytes("val"));
//				String sKey = "";
//				for(byte[] bKey : naviMap.keySet()) {
//					sKey = new String(bKey,"UTF-8");
//					if(("CELL").equals(sKey)) {
//						map.put("CELL_NAME", new String(naviMap.get(bKey),"UTF-8"));
//						continue;
//					}
//					else if(("CHGR").equals(sKey)) {
//						if(!("ALL").equals(chgr)) {
//							map.put("CHANNEL_GROUP_NUM", new String(naviMap.get(bKey),"UTF-8"));
//						}
//						continue;
//					}
//					else if(("CITY_ID").equals(sKey)) {
//						continue;
//					}
//					else if(("MEA_DATE").equals(sKey)) {
//						continue;
//					}
//					else if(("SUBCELL").equals(sKey)) {
//						continue;
//					}
//					else{
//						int v = map.get(sKey)==null?0:Integer.parseInt(map.get(sKey).toString());
//						int totVal = v+Integer.parseInt(new String(naviMap.get(bKey),"UTF-8"));
//						//累加
//						map.put(sKey, totVal);
//
//					}
//				}
////				System.out.println("循环map时间："+(System.currentTimeMillis()-s2)/1000);
////				s2 = System.currentTimeMillis();
//				res=rs.next();
//			}
////			System.out.println("数据整理耗时："+(System.currentTimeMillis()-s)/1000);
////			s = System.currentTimeMillis();
//			if(map.size() > 0) {
//				result.add(map);
//			}
//			rs.close();
//			table.close();
////			System.out.println("关闭资源耗时："+(System.currentTimeMillis()-s)/1000);
////			int i=0;
////			for (Cell c : res.rawCells()) {
////				System.out.println( new String(CellUtil.cloneValue(c),"UTF-8"));
////				i++;
////				if(i>4){break;}
////			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			try {
//				rs.close();
//				table.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			log.error("爱立信MRR指标展现中，读取HBase数据出错！");
//			return Collections.emptyList();
//		}
//		return result;
	}
	/**
	 * 
	 * @title 查询爱立信2G小区FAS的各类指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-2-2上午10:14:55
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryEri2GCellFasIndex(
			final Eri2GFasQueryCond cond){
		//oracle数据源
		log.debug("进入queryEri2GCellFasIndex Eri2GFasQueryCond："+cond);
		List<Map<String, Object>> fasData= rnoIndexDisplayDao.queryEri2GCellFasIndex(cond);
		
		if(fasData==null || fasData.size()==0){
			return null;
		}
		String oriAvmedianArr[]=null;
		String oriAvpercentileArr[]=null;
		
		String oriArfcnArr[]=null;
		
		String sumAvmedian="";
		String sumAvpercentile="";
		String cell="";
		String bcch="";
		String tch="";
		String sumAvmedianArr[]=null;
		String sumAvpercentileArr[]=null;
		
		List<Map<String, Object>> indexMaps=new ArrayList<Map<String,Object>>();
		Map<String, Object> indexMap=new HashMap<String, Object>();
		
		for (Map<String, Object> map : fasData) {
			//逗号分割字符串 900－>94〉  1800->129长度
		/*	if("".equals(sumAvmedian)){
				sumAvmedian=map.get("AVMEDIAN_1_150").toString();
			}else{
				oriAvmedian=map.get("AVMEDIAN_1_150").toString().split(",");
			}
			if("".equals(sumAvpercentile)){
				sumAvpercentile=map.get("AVPERCENTILE_1_150").toString();
			}else{
				oriAvpercentile=map.get("AVPERCENTILE_1_150").toString().split(",");
			}*/
			if("".equals(sumAvmedian)||"".equals(sumAvpercentile)){
				sumAvmedian=map.get("AVMEDIAN_1_150").toString();
				sumAvpercentile=map.get("AVPERCENTILE_1_150").toString();
				//获取第一个ARFCN_1_150序列
				oriArfcnArr=map.get("ARFCN_1_150").toString().split(",");
				
				bcch=map.get("BCCH").toString();
				tch=map.get("TCH").toString();
				cell=map.get("CELL").toString();
				
				sumAvmedianArr=map.get("AVMEDIAN_1_150").toString().split(",");
				sumAvpercentileArr=map.get("AVPERCENTILE_1_150").toString().split(",");
				continue;  
			}else{
				oriAvmedianArr=map.get("AVMEDIAN_1_150").toString().split(",");
				oriAvpercentileArr=map.get("AVPERCENTILE_1_150").toString().split(",");
			}
			
			for (int i = 0; i < sumAvmedianArr.length; i++) {
				//汇总Avmedian
					sumAvmedianArr[i]=String.valueOf(Integer.parseInt(sumAvmedianArr[i])+Integer.parseInt(oriAvmedianArr[i]));
			}
			for (int j = 0; j < sumAvpercentileArr.length; j++) {
				//汇总Avpercentile
					sumAvpercentileArr[j]=String.valueOf(Integer.parseInt(sumAvpercentileArr[j])+Integer.parseInt(oriAvpercentileArr[j]));
				
			}
		}
		indexMap.put("CELL", cell);
		indexMap.put("BCCH", bcch);
		indexMap.put("TCH", tch);
		indexMap.put("ARFCN", oriArfcnArr);
		indexMap.put("AVMEDIAN", sumAvmedianArr);
		indexMap.put("AVPERCENTILE", sumAvpercentileArr);
		indexMaps.add(indexMap);
		log.debug("退出queryEri2GCellFasIndex indexMaps："+indexMaps);
		return indexMaps;
	}
	/**
	 * 
	 * @title 根据城市，日期范围，小区索引表，查询LTE 小区kpi的指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-9-15下午3:51:17
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryLteKpiIndexFromHBase(
			final LteKpiQueryCond cond){
		//HBase数据源
		return queryLteKpiIndexFromHBase("LTE4G_CELL_MEATIME", cond);
	}
	/**
	 * 
	 * @title 通过表索引从Hbase查询４g kpi指标数据  
	 * @param indexTabName
	 * LTE4G_CELL_MEATIME
	 * @param lteMrQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2015-9-17下午6:03:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryLteKpiIndexFromHBase(
			String indexTabName,LteKpiQueryCond lteMrQueryCond) {
		
		String cell = lteMrQueryCond.getCell();
		String startDate = lteMrQueryCond.getMeaBegTime();
		String endDate = lteMrQueryCond.getMeaEndTime();
		long cityId = lteMrQueryCond.getCityId();
		
		long meaBegMillis = dateUtil.parseDateArbitrary(startDate).getTime();
		long meaEndMillis = dateUtil.parseDateArbitrary(endDate).getTime();
		
		//在索引表中符合日期范围的日期集合
		List<String> cellRowKeyList = new ArrayList<String>();
		//HashSet<Integer> monSet = new HashSet<Integer>();

		DateUtil dateUtil = new DateUtil();
		Date meaBegDate = dateUtil.parseDateArbitrary(startDate);
		Date meaEndDate = dateUtil.parseDateArbitrary(endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(meaBegDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int begYear = cal.get(Calendar.YEAR);
		int begMonth = cal.get(Calendar.MONTH)+1;
		cal.setTime(meaEndDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int endYear = cal.get(Calendar.YEAR);
		int endMonth = cal.get(Calendar.MONTH)+1;

		HTable idxTable = null;
		Result idxRes = null;
		NavigableMap<byte[], byte[]> idxNavMap = null;
		try {
			idxTable = new HTable(HadoopXml.getHbaseConfig(), indexTabName);
			//起始日期
			Get begIdxGet = new Get(Bytes.toBytes(cityId+"_KPI_"+begYear+"_"+cell));
			idxRes = idxTable.get(begIdxGet);
			if(idxRes != null && !idxRes.isEmpty()) {
				idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(begMonth+""));
				for (byte[] d : idxNavMap.keySet()) {
					//103_1423398600000_657154_38100_6_38100_18
					long da = Long.parseLong(new String(d).split("_")[1]);
					if(da>=meaBegMillis && da<=meaEndMillis) {
						cellRowKeyList.add(new String(d));
					}
				}
			}
			if(idxNavMap != null && !idxNavMap.isEmpty()) {
				idxNavMap.clear();
			}
			//判断是否在同一个月内 ， 是则不需要再获取结束日期的对应的索引，以免重复
			if(!(begYear == endYear && begMonth == endMonth)) {
				//结束日期
				Get endIdxGet = new Get(Bytes.toBytes(cityId+"_KPI_"+endYear+"_"+cell));
				idxRes = idxTable.get(endIdxGet);
				if(idxRes != null && !idxRes.isEmpty()) {
					idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(endMonth+""));
					for (byte[] d : idxNavMap.keySet()) {
						//103_1423398600000_657154_38100_6_38100_18
						long da = Long.parseLong(new String(d).split("_")[1]);
						if(da>=meaBegMillis && da<=meaEndMillis) {
							cellRowKeyList.add(new String(d));
						}
					}		
				}
			}

			idxTable.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			try {
				idxTable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.error("LTE KPI 指标展现中，读取HBase的小区索引数据出错！");
			return Collections.emptyList();
		}
		if(cellRowKeyList.size() == 0) {
			log.error("LTE KPI 指标展现中，该小区不存在数据！");
			return Collections.emptyList();
		}
		
		//整理需要获取的get列
		List<Get> getList = new ArrayList<Get>();
		for (String d : cellRowKeyList) {
				Get get = new Get(Bytes.toBytes(d));
				getList.add(get);
		}
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		
		HTable dataTab = null;
		NavigableMap<byte[], byte[]> naviMap=null;
		//System.out.println(getList.size());
//		boolean flag=true;
		try {
			dataTab = new HTable(HadoopXml.getHbaseConfig(), "LTE4G_KPI_DATA");
			Result[] ress = dataTab.get(getList);
			if (ress != null && ress.length > 0) {
				for (int i = 0; i < ress.length; i++) {
					
					Result res = ress[i];
					if(res.isEmpty()) {
						continue;
					}
					naviMap =  res.getFamilyMap(Bytes.toBytes("KPIINFO"));
					String sKey = "";
					//新的　一行创建一个新的MAP对象
					map=new LinkedHashMap<String, String>();
					for(byte[] bKey : naviMap.keySet()) {
						
						sKey = new String(bKey,"UTF-8");
						byte[] value = naviMap.get(bKey);
						map.put(sKey, Bytes.toString(value));
					}
					result.add(map);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("LTE KPI 指标展现中，读取HBase数据出错！");
			return Collections.emptyList();
		}finally{
			try {
				dataTab.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:53:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteStsIndex(final LteStsIndexQueryCond cond){
		log.debug("进入方法：queryLteStsIndex。cond="+cond);		
		List<Map<String, Object>> list = null;
		//生成缓存key
		String areaId = String.valueOf(cond.getCityId());
		String column = cond.getColumnList();
		String time = cond.getMeaBegTime()+"_"+cond.getMeaEndTime();
		String cells = cond.getCellNameList();
		String cacheKey = RnoConstant.CacheConstant.CACHE_LTE_STS_INDEX_BY_AREA_COLUMN_TIME_CELLS_PRE
				+ areaId + "_" + column + "_" + time + "_" + cells;
		//防止key过长
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(cacheKey.getBytes("UTF-8"));
			cacheKey = new String(md.digest(),"UTF-8");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		try {
			log.debug("从缓存中获取->");
			list = (List<Map<String, Object>>) memCached.get(cacheKey);
			log.debug("缓存获取的结果数量为 ：" + ((list == null) ? 0 : list.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(list==null){
			log.info("从数据库获取->");
			List<Map<String, Object>> rows = rnoIndexDisplayDao.queryLteStsIndex(cond);
			list = new ArrayList<Map<String,Object>>();
			if(rows!=null&&rows.size()>0){
				for(Map<String, Object> m:rows){
					Map<String, Object> m1 = cond.getLteStsIndex(m);
					m1.put("MEABEGTIME",new Date(((Timestamp) m.get("BEGINTIME")).getTime()));
					m1.put("MEAENDTIME",new Date(((Timestamp) m.get("ENDTIME")).getTime()));
					m1.put("CELLNAME", m.get("CELLNAME"));
					list.add(m1);
				}
			}
			log.info("从数据库中获取结果数量为：" + list.size());
			log.info("放入缓存->");
			try {
				memCached.set(cacheKey,RnoConstant.TimeConstant.LTESTSINDEXTIME,list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("退出方法：queryLteStsIndex。result="+list);
		return list;
	}
	/**
	 * 根据城市，时间，数据列，小区列，分页查询LTE小区的STS指标
	 * @param cond，page
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:48:00
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteStsIndexByPage(final LteStsIndexQueryCond cond, Page page){
		log.debug("进入方法：queryLteStsIndexByPage。cond="+cond);
		List<Map<String, Object>> list = queryLteStsIndex(cond);
		List<Map<String, Object>> newlist = new ArrayList<Map<String,Object>>();
		int start = (page.getPageSize()* (page.getCurrentPage() - 1) + 1);
		int end = (page.getPageSize() * page.getCurrentPage());
		int size = list.size();
		if (start < 0) {
			start = 0;
		}
		if (start > size) {
			start = size;
		}
		if (end < 1) {
			end = 1;
		}
		if (end > size) {
			end = size;
		}
		//log.debug("start==" + start + ",end=" + end);
		// 截取
		newlist = list.subList(start, end);
		log.debug("退出方法：queryLteStsIndexByPage。result="+newlist);
		return newlist;
	}
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标总数
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015年9月21日 下午2:37:16
	 * @company 怡创科技
	 * @version V1.0
	 */
	public int queryLteStsIndexCnt(final LteStsIndexQueryCond cond){
		//return rnoIndexDisplayDao.queryLteStsIndexCnt(cond);
		return queryLteStsIndex(cond).size();
	}
	/**
	 * 导出4G话统指标文件
	 * @param lteStsIndexQueryCond
	 * @author chen.c10
	 * @date 2015年9月20日 下午7:35:50
	 * @company 怡创科技
	 * @version V1.0
	 */
	public String downloadLteStsCellIndexFile(LteStsIndexQueryCond cond){
		String result = "";
		List<Map<String, Object>> list = queryLteStsIndex(cond);
		ListOrderedMap columnTitles= cond.getColumnTitles();
		Date today=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String filePath = ServletActionContext.getServletContext().getRealPath("/op/rno/ana_result/");
		String FileName = sdf.format(today) + "_话统指标数据.xlsx";
		String FileRealPath = filePath+"/"+FileName;
		boolean flag = createExcelFile(list,columnTitles,FileRealPath);
		if(flag){
			result = FileRealPath;
		}else {
			result = "error";
		}
		return result;
	}
	private boolean createExcelFile(List<Map<String,Object>> res,ListOrderedMap columnTitles,String fileRealPath) {
		
		FileOutputStream fos = null;
		try {
			File file = new File(fileRealPath);
	        if(!file.getParentFile().exists()){
	            file.getParentFile().mkdirs();
				}
	        if(!file.exists()){
	            try {
	            	file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	                return false;
	            }
	        }
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		List<String> columns = new ArrayList<String>();
		List<String> columnNames = new ArrayList<String>();
		for(Object column:columnTitles.keySet()){
			columns.add((String)column);
			columnNames.add((String)columnTitles.get(column));
		}
		Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
		Row row;
		Cell cell;
		row = sheet.createRow((short)0);
		for(int i=0;i<columnNames.size();i++){
			cell = row.createCell(i);
			cell.setCellValue(columnNames.get(i));
		}
		for (int i = 0; i < res.size(); i++) {
			row = sheet.createRow((short)i + 1);
			for(int j=0;j<columns.size();j++){
				if(res.get(i).get(columns.get(j)) instanceof String){
					cell = row.createCell(j);
					cell.setCellValue(res.get(i).get(columns.get(j)).toString());
				}else if (res.get(i).get(columns.get(j)) instanceof Float) {
					cell = row.createCell(j);
					cell.setCellValue(Float.parseFloat(res.get(i).get(columns.get(j)).toString()));
				}else {
					cell = row.createCell(j);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					cell.setCellValue(sdf.format((Date)res.get(i).get(columns.get(j))));
				}
/*				if(j==0){
				cell = row.createCell(j);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cell.setCellValue(sdf.format((Date)res.get(i).get(columns.get(j))));
				}else if (j==1) {
					cell = row.createCell(j);
					cell.setCellValue(res.get(i).get(columns.get(j)).toString());
				}else {
					cell = row.createCell(j);
					cell.setCellValue((float)res.get(i).get(columns.get(j)));
				}*/
			}
		}	
        //最终写入文件
        try {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
}
