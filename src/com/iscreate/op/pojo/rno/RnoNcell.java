package com.iscreate.op.pojo.rno;

/**
 * RnoNcell entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoNcell implements java.io.Serializable {

	// Fields

	private Long ncellId;
	private String cell;
	private String ncell;
	private String cs;
	private String dir;
	private Long bscId;

	// Constructors

	/** default constructor */
	public RnoNcell() {
	}

	/** full constructor */
	public RnoNcell(String cell, String ncell, String cs, String dir,Long bscId) {
		this.cell = cell;
		this.ncell = ncell;
		this.cs = cs;
		this.dir = dir;
		this.bscId=bscId;
	}

	// Property accessors

	public Long getNcellId() {
		return this.ncellId;
	}

	public void setNcellId(Long ncellId) {
		this.ncellId = ncellId;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getNcell() {
		return this.ncell;
	}

	public void setNcell(String ncell) {
		this.ncell = ncell;
	}

	public String getCs() {
		return this.cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getDir() {
		return this.dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Long getBscId() {
		return bscId;
	}

	public void setBscId(Long bscId) {
		this.bscId = bscId;
	}

}