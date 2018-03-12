package com.iscreate.op.service.rno.parser.vo;

public class G4PciRec {

	private int ncell_id;//邻区标识
	private double RsrpTimes1;//主小区减去邻小区信号强度   可设置相对数值2(建议值：-3)
	private double cosi;//服务小区到某一邻区的关联度
	private int cellEnodebId;//小区归属站点
	private int cellPci;//小区PCI
	private int ncellPci;//邻区PCI
	private int cellBcch;//小区频点



	private String sameSiteOtherCell;//cell2,cell3
	public double getCosi() {
		return cosi;
	}

	public void setCosi(double cosi) {
		this.cosi = cosi;
	}

	public int getNcell_id() {
		return ncell_id;
	}

	public void setNcell_id(int ncell_id) {
		this.ncell_id = ncell_id;
	}
	public double getRsrpTimes1() {
		return RsrpTimes1;
	}

	public void setRsrpTimes1(double rsrpTimes1) {
		RsrpTimes1 = rsrpTimes1;
	}
	public int getCellEnodebId() {
		return cellEnodebId;
	}

	public void setCellEnodebId(int cellEnodebId) {
		this.cellEnodebId = cellEnodebId;
	}

	public int getCellPci() {
		return cellPci;
	}

	public void setCellPci(int cellPci) {
		this.cellPci = cellPci;
	}

	public String getSameSiteOtherCell() {
		return sameSiteOtherCell;
	}

	public void setSameSiteOtherCell(String sameSiteOtherCell) {
		this.sameSiteOtherCell = sameSiteOtherCell;
	}
	public int getNcellPci() {
		return ncellPci;
	}

	public void setNcellPci(int ncellPci) {
		this.ncellPci = ncellPci;
	}
	public int getCellBcch() {
		return cellBcch;
	}

	public void setCellBcch(int cellBcch) {
		this.cellBcch = cellBcch;
	}
/*	public G4PciRec(int ncell_id, double rsrpTimes1, double cosi,
			int cellEnodebId, int cellPci, int ncellPci,
			String sameSiteOtherCell) {
		super();
		this.ncell_id = ncell_id;
		RsrpTimes1 = rsrpTimes1;
		this.cosi = cosi;
		this.cellEnodebId = cellEnodebId;
		this.cellPci = cellPci;
		this.ncellPci = ncellPci;
		this.sameSiteOtherCell = sameSiteOtherCell;
	}*/
	
	public G4PciRec(int ncell_id, double rsrpTimes1, double cosi,
			int cellEnodebId, int cellPci, int ncellPci, int cellBcch,
			String sameSiteOtherCell) {
		super();
		this.ncell_id = ncell_id;
		RsrpTimes1 = rsrpTimes1;
		this.cosi = cosi;
		this.cellEnodebId = cellEnodebId;
		this.cellPci = cellPci;
		this.ncellPci = ncellPci;
		this.cellBcch = cellBcch;
		this.sameSiteOtherCell = sameSiteOtherCell;
	}

	/**
	 * @title 
	 * @param args
	 * @author chao.xj
	 * @date 2015-3-31下午2:07:09
	 * @company 怡创科技
	 * @version 2.0.1
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



}
