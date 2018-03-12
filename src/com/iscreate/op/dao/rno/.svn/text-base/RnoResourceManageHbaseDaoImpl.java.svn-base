package com.iscreate.op.dao.rno;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4HoDescQueryCond;
import com.iscreate.op.action.rno.model.G4KpiDescQueryCond;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;

public class RnoResourceManageHbaseDaoImpl implements RnoResourceManageHbaseDao {

	private static Log log=LogFactory.getLog(RnoResourceManageHbaseDaoImpl.class);
	

/**
 * 
 * @title 获取Hbase的table
 * @param tableName
 * @return
 * @author chao.xj
 * @date 2015-3-18下午12:00:33
 * @company 怡创科技
 * @version 2.0.1
 */
public HTableInterface getHbaseTable(String tableName) {
    if (StringUtils.isEmpty(tableName))
        return null;
    HTable table = null;
    try {
		Configuration conf = new Configuration();
		conf = HBaseConfiguration.create(conf);
		table = new HTable(conf,tableName);
	} catch (IOException e) {
		e.printStackTrace();
	}
    return table;
}
/**
 * 
 * @title 封装过滤查询条件
 * @param isPage
 * @param cond
 * @return
 * @author chao.xj
 * @date 2015-3-18下午5:22:18
 * @company 怡创科技
 * @version 2.0.1
 */
private FilterList getMrDescFilters(boolean isPage,G4MrDescQueryCond cond) {
    FilterList filterList = null;
    // MUST_PASS_ALL(条件 AND) MUST_PASS_ONE（条件OR）
    filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    SingleColumnValueFilter filter1 = null;
//    System.out.println(cond.getFactory());
    if(!cond.getFactory().equals("ALL")){
    	filter1 = new SingleColumnValueFilter(Bytes.toBytes("DESCINFO"), 
        		Bytes.toBytes("FACTORY"), CompareOp.EQUAL, 
        		Bytes.toBytes(cond.getFactory()));
    	filter1.setFilterIfMissing(true);
    	filterList.addFilter(filter1);
    }else{
    	if (isPage) {
            filterList.addFilter(new FirstKeyOnlyFilter());
        }
    }
    return filterList;
}

/**
 * 
 * @title 封装过滤查询条件
 * @param isPage
 * @param cond
 * @return
 */
private FilterList getHoDescFilters(boolean isPage,G4HoDescQueryCond cond) {
    FilterList filterList = null;
    filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    SingleColumnValueFilter filter1 = null;
//    System.out.println(cond.getFactory());
    if(!cond.getFactory().equals("ALL")){
    	filter1 = new SingleColumnValueFilter(Bytes.toBytes("DESCINFO"), 
        		Bytes.toBytes("FACTORY"), CompareOp.EQUAL, 
        		Bytes.toBytes(cond.getFactory()));
    	filter1.setFilterIfMissing(true);
    	filterList.addFilter(filter1);
    }else{
    	if (isPage) {
            filterList.addFilter(new FirstKeyOnlyFilter());
        }
    }
    return filterList;
}

/**
 * 
 * @title 分页获取Hbase的Mr数据描述表的数据
 * @param cond
 * @param newPage
 * @return
 * @author chao.xj
 * @date 2015-3-18下午1:52:28
 * @company 怡创科技
 * @version 2.0.1
 */
public List<Map<String, String>> queryMrDataFromHbaseByPage(
		G4MrDescQueryCond cond, Page newPage) {
	String tableName,  startRow,  stopRow;
	
	tableName=cond.buildTable();
	startRow=cond.buildStartRow();
	stopRow=cond.buildStopRow();
	
	List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
	ResultScanner scanner = null;
	// 为分页创建的封装类对象，下面有给出具体属性
	try {
		// 获取最大返回结果数量
		if (newPage.getPageSize() == 0)
			newPage.setPageSize(25);
		if (newPage.getCurrentPage() == 0)
			newPage.setCurrentPage(1);
		// 计算起始页和结束页
		Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
		Integer endPage = firstPage + newPage.getPageSize();
		// 从表池中取出HBASE表对象
		HTableInterface table = getHbaseTable(tableName);
		if(table == null) {
			return null;
		}
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
		if(getMrDescFilters(true,cond)!=null){
			scan.setFilter(getMrDescFilters(true,cond));
		}
		// 缓存1000条数据
		scan.setCaching(1000);
		scan.setCacheBlocks(false);
		scanner = table.getScanner(scan);
		int i = 0;
		List<byte[]> rowList = new LinkedList<byte[]>();
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出
		for (Result r : scanner) {
			String row = Bytes.toString(r.getRow());
			if (i >= firstPage && i < endPage) {
				rowList.add(Bytes.toBytes(row));
			}
			i++;
		}
		// 获取取出的row key的GET对象
		List<Get> getList = new LinkedList<Get>();
        for (byte[] row : rowList) {
            Get get = new Get(row);
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
            getList.add(get);
        }

		Result[] results = table.get(getList);
		// 遍历结果
		for (Result r : results) {
			Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
			fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
			
	        Map<String, String> rmap = new LinkedHashMap<String, String>();
	        for (byte[] key : fmap.keySet()) {
	            byte[] value = fmap.get(key);
	            rmap.put(Bytes.toString(key), Bytes.toString(value));
	        }
			mapList.add(rmap);
		}
		
		// 封装分页对象
		newPage.setTotalCnt(i);
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (scanner != null)
			scanner.close();
	}
	return mapList;
}

/**
 * 
 * @title 分页获取Hbase的Ho数据描述表的数据
 * @param cond
 * @param newPage
 * @return
 */
public List<Map<String, String>> queryHoDataFromHbaseByPage(
		G4HoDescQueryCond cond, Page newPage) {
	String tableName,  startRow,  stopRow;
	
	tableName=cond.buildTable();
	startRow=cond.buildStartRow();
	stopRow=cond.buildStopRow();
	
	List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
	ResultScanner scanner = null;
	// 为分页创建的封装类对象，下面有给出具体属性
	try {
		// 获取最大返回结果数量
		if (newPage.getPageSize() == 0)
			newPage.setPageSize(25);
		if (newPage.getCurrentPage() == 0)
			newPage.setCurrentPage(1);
		// 计算起始页和结束页
		Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
		Integer endPage = firstPage + newPage.getPageSize();
		// 从表池中取出HBASE表对象
		HTableInterface table = getHbaseTable(tableName);
		if(table == null) {
			return null;
		}
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
		if(getHoDescFilters(true,cond)!=null){
			scan.setFilter(getHoDescFilters(true,cond));
		}
		// 缓存1000条数据
		scan.setCaching(1000);
		scan.setCacheBlocks(false);
		scanner = table.getScanner(scan);
		int i = 0;
		List<byte[]> rowList = new LinkedList<byte[]>();
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出
		for (Result r : scanner) {
			String row = Bytes.toString(r.getRow());
			if (i >= firstPage && i < endPage) {
				rowList.add(Bytes.toBytes(row));
			}
			i++;
		}
		// 获取取出的row key的GET对象
		List<Get> getList = new LinkedList<Get>();
        for (byte[] row : rowList) {
            Get get = new Get(row);
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
            getList.add(get);
        }

		Result[] results = table.get(getList);
		// 遍历结果
		for (Result r : results) {
			Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
			fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
			
	        Map<String, String> rmap = new LinkedHashMap<String, String>();
	        for (byte[] key : fmap.keySet()) {
	            byte[] value = fmap.get(key);
	            rmap.put(Bytes.toString(key), Bytes.toString(value));
	        }
			mapList.add(rmap);
		}
		
		// 封装分页对象
		newPage.setTotalCnt(i);
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (scanner != null)
			scanner.close();
	}
	return mapList;
}
/**
 * 
 * @title 分页获取Hbase的4g kpi 数据描述表的数据
 * @param cond
 * @param newPage
 * @return
 * @author chao.xj
 * @date 2015-9-17下午4:50:17
 * @company 怡创科技
 * @version 2.0.1
 */
public List<Map<String, String>> queryKpiDataFromHbaseByPage(
		G4KpiDescQueryCond cond, Page newPage) {
	String tableName,  startRow,  stopRow;
	
	tableName=cond.buildTable();
	startRow=cond.buildStartRow();
	stopRow=cond.buildStopRow();
	
	List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
	ResultScanner scanner = null;
	// 为分页创建的封装类对象，下面有给出具体属性
	try {
		// 获取最大返回结果数量
		if (newPage.getPageSize() == 0)
			newPage.setPageSize(25);
		if (newPage.getCurrentPage() == 0)
			newPage.setCurrentPage(1);
		// 计算起始页和结束页
		Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
		Integer endPage = firstPage + newPage.getPageSize();
		// 从表池中取出HBASE表对象
		HTableInterface table = getHbaseTable(tableName);
		if(table == null) {
			return null;
		}
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
		/*if(getMrDescFilters(true,cond)!=null){
			scan.setFilter(getMrDescFilters(true,cond));
		}*/
		// 缓存1000条数据
		scan.setCaching(1000);
		scan.setCacheBlocks(false);
		scanner = table.getScanner(scan);
		int i = 0;
		List<byte[]> rowList = new LinkedList<byte[]>();
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出
		for (Result r : scanner) {
			String row = Bytes.toString(r.getRow());
			if (i >= firstPage && i < endPage) {
				rowList.add(Bytes.toBytes(row));
			}
			i++;
		}
		// 获取取出的row key的GET对象
		List<Get> getList = new LinkedList<Get>();
        for (byte[] row : rowList) {
            Get get = new Get(row);
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
            getList.add(get);
        }

		Result[] results = table.get(getList);
		// 遍历结果
		for (Result r : results) {
			Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
			fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
			
	        Map<String, String> rmap = new LinkedHashMap<String, String>();
	        for (byte[] key : fmap.keySet()) {
	            byte[] value = fmap.get(key);
	            rmap.put(Bytes.toString(key), Bytes.toString(value));
	        }
			mapList.add(rmap);
		}
		
		// 封装分页对象
		newPage.setTotalCnt(i);
		
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (scanner != null)
			scanner.close();
	}
	return mapList;
}
}
