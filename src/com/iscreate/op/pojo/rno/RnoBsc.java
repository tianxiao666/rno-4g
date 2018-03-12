package com.iscreate.op.pojo.rno;

/**
 * RnoBsc entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoBsc implements java.io.Serializable {

	// Fields

	private Long bscId;
	private String chinesename;
	private String engname;

	// Constructors

	/** default constructor */
	public RnoBsc() {
	}

	/** full constructor */
	public RnoBsc(String chinesename, String engname) {
		this.chinesename = chinesename;
		this.engname = engname;
	}

	// Property accessors

	public Long getBscId() {
		return this.bscId;
	}

	public void setBscId(Long bscId) {
		this.bscId = bscId;
	}

	public String getChinesename() {
		return this.chinesename;
	}

	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	public String getEngname() {
		return this.engname;
	}

	public void setEngname(String engname) {
		this.engname = engname;
	}

}