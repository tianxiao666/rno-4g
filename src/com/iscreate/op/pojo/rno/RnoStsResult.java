package com.iscreate.op.pojo.rno;

import java.io.Serializable;

/**
 * 统计结果
 * 
 * @author brightming
 * 
 */
public class RnoStsResult implements Serializable{

	private String cell;
	private Float maxValue;
	private Float avgValue;
	private Float minValue;
	private Integer cnt;
	private String code;

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	
	public Float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}

	public Float getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Float avgValue) {
		this.avgValue = avgValue;
	}

	public Float getMinValue() {
		return minValue;
	}

	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "RnoStsResult [cell=" + cell + ", maxValue=" + maxValue
				+ ", avgValue=" + avgValue + ", minValue=" + minValue
				+ ", cnt=" + cnt + ", code=" + code + "]";
	}

}
