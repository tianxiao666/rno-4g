package com.iscreate.op.action.rno;

public class LteCellQueryCondition {

	private long provinceId = -1;
	private long cityId = -1;
	private String enodebName = null;
	private String cellName = null;
	private int pci = -1;
	
	//lte邻区查询的条件
	private String lteCellName = null;
	private String ncellName = null;
	private String cellSite = null;
	private String ncellSite = null;
	
	public String getNcellName() {
		return ncellName;
	}

	public void setNcellName(String ncellName) {
		this.ncellName = ncellName;
	}

	public String getNcellSite() {
		return ncellSite;
	}

	public void setNcellSite(String ncellSite) {
		this.ncellSite = ncellSite;
	}
	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getEnodebName() {
		return enodebName;
	}

	public void setEnodebName(String enodebName) {
		this.enodebName = enodebName;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public int getPci() {
		return pci;
	}

	public void setPci(int pci) {
		this.pci = pci;
	}
	public String getLteCellName() {
		return lteCellName;
	}

	public void setLteCellName(String lteCellName) {
		this.lteCellName = lteCellName;
	}
	public String getCellSite() {
		return cellSite;
	}

	public void setCellSite(String cellSite) {
		this.cellSite = cellSite;
	}
	/**
	 * 构建WHERE,enodebName的部分需要单独处理
	 * 
	 * @return
	 * @author brightming 2014-5-19 下午1:48:33
	 */
	public String buildCellQueryCond(String prx) {
		String where = "";

		if (pci != -1) {
			where += prx + ".PCI=" + pci;
		}

		//----默认加上status-------//
		where+=(where.length()==0?" ":" AND ")+ " STATUS='N' ";
		
		if (cellName != null) {
			cellName = cellName.trim();
			if (!"".equals(cellName)) {
				where += (where.length() == 0 ? " " : " and ") + prx
						+ ".CELL_NAME like '%" + cellName + "%'";
			}
		}

		if (cityId != -1) {
			where += (where.length() == 0 ? " " : " and ") + prx + ".AREA_ID="
					+ cityId;
		} else {
			if (provinceId != -1) {
				where += (where.length() == 0 ? " " : " and ")
						+ prx
						+ ".AREA_ID in ( SELECT AREA_ID FROM SYS_AREA WHERE PARENT_ID="
						+ provinceId + ")";
			}
		}

		if (enodebName != null) {
			enodebName = enodebName.trim();
			if (!"".equals(enodebName)) {
				where += (where.length() == 0 ? " " : " and ")
						+ prx
						+ ".ENODEB_ID IN (SELECT ENODEB_ID FROM RNO_LTE_ENODEB WHERE ENODEB_NAME LIKE '%"
						+ enodebName + "%')";
			}
		}
		return where;
	}
	/**
	 * 
	 * @title 构建邻区查询的条件
	 * @param prx
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:03:40
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String buildNCellQueryCond(String prx) {
		
		String where = " 1=1 ";
		if (cellName != null) {
			cellName = cellName.trim();
			if (!"".equals(cellName)) {
				where += " and " + prx
						+ ".LTE_CELL_NAME like '%" + cellName + "%'";
			}
		}
		
		if (ncellName != null) {
			ncellName = ncellName.trim();
			if (!"".equals(ncellName)) {
				where += " and " + prx
						+ ".LTE_NCELL_NAME like '%" + ncellName + "%'";
			}
		}
		
		if (cellSite != null) {
			cellSite = cellSite.trim();
			if (!"".equals(cellSite)) {
				where += " and " + prx
						+ ".LTE_CELL_SITE_ID like '%" + cellSite + "%'";
			}
		}
		
		if (ncellSite != null) {
			ncellSite = ncellSite.trim();
			if (!"".equals(ncellSite)) {
				where += " and " + prx
						+ ".LTE_NCELL_SITE_ID like '%" + ncellSite + "%'";
			}
		}
		
		if (cityId != -1) {
			where += " and " + prx + ".LTE_CELL_ENODEB_ID in " +
					"(select BUSINESS_ENODEB_ID from RNO_LTE_ENODEB " +
					" where AREA_ID =" + cityId + ")";
			
		} else if (provinceId != -1) {
			where += " and " + prx + ".LTE_CELL_ENODEB_ID in " +
			"(select BUSINESS_ENODEB_ID from RNO_LTE_ENODEB " +
			" where AREA_ID in ( SELECT AREA_ID FROM SYS_AREA WHERE PARENT_ID="
						+ provinceId + "))";				
			
		} else {
			
			
		}
		
		return where;
	}
}
