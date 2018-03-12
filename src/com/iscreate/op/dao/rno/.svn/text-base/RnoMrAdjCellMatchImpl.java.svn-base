package com.iscreate.op.dao.rno;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.rno.tool.DbValInject;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoMrAdjCellMatchImpl implements RnoMrAdjCellMatch {

	private static Log log = LogFactory.getLog(RnoMrAdjCellMatchImpl.class);

	private ThreadLocal CellMainInfo = new ThreadLocal(); // 线程本地局部变量

	public static class CellForMatch {
		int cellCnt = -1;
		Map<String, List<CellLonLat>> bcchBsicToInfo = null;
		Map<String, CellLonLat> cells = new HashMap<String, CellLonLat>();
		// 缓存的数据
		Map<String, List<String>> quickCached = new HashMap<String, List<String>>();// key为cell+"_bcch_bsic"，value为:0:ncell,1:ncells,2:dis
		//区配小区名：通过enodebId与earfcn、pci为key存储小区名
		Map<String, CellLonLat> cellMatch=new HashMap<String, CellLonLat>();//key为enodebId+"_"+earfcn+"_"+pci ,val为：CellLonLat
		
		public CellLonLat getCellInfo(String cell) {
			return cells == null ? null : cells.get(cell);
		}

		public List<String> getCachedMatchNcell(String key) {
			return quickCached.get(key);
		}

		public void putCachedMatchNcell(String key, List<String> ncellsInfo) {
			quickCached.put(key, ncellsInfo);
		}
		public CellLonLat getMatchCell(String key) {
			return cellMatch.get(key);
		}

		public void putMatchCell(String key, CellLonLat cellLonLat) {
			cellMatch.put(key, cellLonLat);
		}
		public List<CellLonLat> getBcchBsicCells(String key) {
			return bcchBsicToInfo == null ? null : bcchBsicToInfo.get(key);
		}

		public int getCellCnt() {
			return cellCnt;
		}

		public void setCellCnt(int cellCnt) {
			this.cellCnt = cellCnt;
		}

		public Map<String, CellLonLat> getCells() {
			return cells;
		}

		public void setCells(Map<String, CellLonLat> cells) {
			this.cells = cells;
		}
	}

	public static class CellLonLat {
		@DbValInject(dbField = "LABEL", type = "String")
		String cell;
		String name;
		@DbValInject(dbField = "LONGITUDE", type = "Double")
		double lon;
		@DbValInject(dbField = "LATITUDE", type = "Double")
		double lat;
		int bcch;
		int pci;
		int enodebId;
		
		// 一开始就要准备好
		List<Integer> freqSet = null;

		public CellLonLat() {
		}

		public CellLonLat(String cell, String name, double lon, double lat,
				int bcch, int pci,int enodebId) {
			this.cell = cell;
			this.name = name;
			this.lon = lon;
			this.lat = lat;
			this.bcch = bcch;
			this.pci = pci;
			this.enodebId = enodebId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public int getBcch() {
			return bcch;
		}

		public void setBcch(int bcch) {
			this.bcch = bcch;
		}
		public int getPci() {
			return pci;
		}

		public void setPci(int pci) {
			this.pci = pci;
		}

		public int getEnodebId() {
			return enodebId;
		}

		public void setEnodebId(int enodebId) {
			this.enodebId = enodebId;
		}

		public List<Integer> getFreqSet() {
			return freqSet;
		}

		public void setFreqSet(List<Integer> freqSet) {
			this.freqSet = freqSet;
		}

		@Override
		public String toString() {
			return "CellLonLat [cell=" + cell + ", name=" + name + ", lon="
					+ lon + ", lat=" + lat + ", bcch=" + bcch + ", pci=" + pci
					+ ", enodebId=" + enodebId + ", freqSet=" + freqSet + "]";
		}
		
		}

	

	public void clearMatchCellContext() {
		CellMainInfo.remove();
	}

	/**
	 * 获取邻区匹配需要的数据
	 * 通过线程本地局部变量存取邻区匹配数据，避免并发的干扰
	 */
	public CellForMatch getMatchCellContext(Statement stmt, long city_id) {
		log.debug("进行内存邻区匹配");
		CellForMatch cfm = (CellForMatch) CellMainInfo.get();
		if (cfm == null) {
			cfm = new CellForMatch();
			CellMainInfo.set(cfm);
		}
		if (cfm.cellCnt < 0) {
			prepareMatchCellInfo(stmt, cfm,city_id);
		}
		log.debug("CellForMatch中缓存小区数据量大小为：" + cfm.cellCnt);
		return cfm;
	}

	/**
	 * 
	 * @title 准备邻区匹配需要的数据
	 * @param stmt
	 * @param cfm
	 * @param city_id
	 * @author chao.xj
	 * @date 2015-3-19上午10:46:20
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private void prepareMatchCellInfo(Statement stmt, CellForMatch cfm,long city_id) {
		
		String sql="";
//		sql="select BUSINESS_CELL_ID cell,CELL_NAME name,PCI,EARFCN,LONGITUDE,LATITUDE,ENODEB_ID from rno_lte_cell where area_id="+city_id;
		sql="select t1.business_cell_id cell,			"
				+"       t1.cell_name name,                 "
				+"       t1.pci,                            "
				+"       t1.earfcn,                         "
				+"       t1.longitude,                      "
				+"       t1.latitude,                       "
				+"       t2.business_enodeb_id ENODEB_ID    "
				+"  from rno_lte_cell t1                    "
				+"  left join rno_lte_enodeb t2             "
				+"    on t1.enodeb_id = t2.enodeb_id        "
				+" where t1.area_id =                     "+city_id;
		List<Map<String, Object>> cells = RnoHelper
				.commonQuery(
						stmt,
						sql);
		int cnt = 0;
		cfm.cellCnt = 0;
		cfm.bcchBsicToInfo = new HashMap<String, List<CellLonLat>>();
		if (cells != null && cells.size() > 0) {
			String key;
			String cell, name;
			int pci = 0,earfcn=0,enodebId;
			Double lon, lat, downtilt, bearing;
			for (Map<String, Object> one : cells) {
				//小区名
				name = one.get("NAME") == null ? "" : one.get("NAME")
						.toString();
				if (name == null) {
					continue;
				}
				//小区标识
				cell = one.get("CELL") == null ? null : one.get("CELL")
						.toString();
				
				if (one.get("PCI") == null) {
					continue;
				}
				pci = Integer.parseInt(one.get("PCI").toString());
				if (one.get("EARFCN") == null || !isNum(one.get("EARFCN").toString())) {
					continue;
				}
				try {
					earfcn = Integer.parseInt(one.get("EARFCN").toString());
				} catch (Exception e) {
					// TODO: handle exception
					log.error("EARFCN:"+one.get("EARFCN").toString());
					e.printStackTrace();
				}
				if (one.get("ENODEB_ID") == null) {
					continue;
				}
				enodebId = Integer.parseInt(one.get("ENODEB_ID").toString());
				
				lon = one.get("LONGITUDE") == null ? -1l : Double
						.parseDouble(one.get("LONGITUDE").toString());
				lat = one.get("LATITUDE") == null ? -1l : Double
						.parseDouble(one.get("LATITUDE").toString());

				
				

				cnt++;
				//
				key = earfcn + "_" + pci;
				List<CellLonLat> clls = cfm.bcchBsicToInfo.get(key);
				if (clls == null) {
					clls = new ArrayList<CellLonLat>();
					cfm.bcchBsicToInfo.put(key, clls);
				}
				// bcch+bsic与小区对应关系
				CellLonLat oneCell = new CellLonLat(cell, name, lon, lat, earfcn,
						pci, enodebId);
				clls.add(oneCell);
				// 小区
				//cfm.cells.put(name, oneCell);
				//区配小区名：通过key为enodebId+"_"+earfcn+"_"+pci 为key,val为：cellname存储小区名
				cfm.putMatchCell(enodebId+"_"+earfcn+"_"+pci, oneCell);
				//
			}
		}
		cfm.cellCnt = cnt;
	}

	private void printTemp1(Statement stmt, String ncsTabName, String pre) {
		String sql = "select * from " + ncsTabName;
		List<Map<String, Object>> tempdatas = RnoHelper.commonQuery(stmt, sql);
		if (tempdatas != null && tempdatas.size() > 0) {
			// for(Map<String,Object> one:tempdatas){
			// System.out.println(pre+"--temptable data ="+one);
			// }
			System.out.println(pre + "--temptable data size ="
					+ tempdatas.size());
		} else {
			System.err.println(pre + "------temptable contains no data!");
		}
	}
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}


	private void printTmpTable1(String sql, Statement stmt) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			log.debug("\r\n---数据如下：");
			for (Map<String, Object> one : res) {
				log.debug(one);
			}
			log.debug("---数据展示完毕\r\n");
		} else {
			log.debug("---数据为空！");
		}
	}

	public static void main(String[] args) {
		
	}

}
