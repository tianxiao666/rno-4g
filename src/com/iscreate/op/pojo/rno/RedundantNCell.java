package com.iscreate.op.pojo.rno;

/**
 * 冗余邻区
 * 
 * @author brightming
 * 
 */
public class RedundantNCell {

	private String ncellType;// 冗余还是漏定
	private String cell;
	private String ncell;
	private Double detectRatio;
	private Double navss;
	private Double distance;
	private Double hovercnt;
	private Double expectedCoverDis;

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getNcell() {
		return ncell;
	}

	public void setNcell(String ncell) {
		this.ncell = ncell;
	}

	public Double getNavss() {
		return navss;
	}

	public void setNavss(Double navss) {
		this.navss = navss;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getHovercnt() {
		return hovercnt;
	}

	public void setHovercnt(Double hovercnt) {
		this.hovercnt = hovercnt;
	}

	public String getNcellType() {
		return ncellType;
	}

	public void setNcellType(String ncellType) {
		this.ncellType = ncellType;
	}

	public Double getExpectedCoverDis() {
		return expectedCoverDis;
	}

	public void setExpectedCoverDis(Double expectedCoverDis) {
		this.expectedCoverDis = expectedCoverDis;
	}
	
	public Double getDetectRatio()
	{
		return detectRatio;
	}

	public void setDetectRatio(Double detectRatio)
	{
		this.detectRatio = detectRatio;
	}

	@Override
	public String toString() {
		return "RedundantNCell [ncellType=" + ncellType + ", cell=" + cell
				+ ", ncell=" + ncell + ", detectRatio=" + detectRatio + ", navss=" + navss + ", distance=" + distance
				+ ", hovercnt=" + hovercnt + ", expectedCoverDis="
				+ expectedCoverDis + "]";
	}




}
