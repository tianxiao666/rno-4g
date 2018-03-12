package com.iscreate.op.pojo.rno;

import java.io.Serializable;

import com.iscreate.op.action.rno.StsCondition;

/**
 * 分析列表
 * 
 * @author brightming
 * 
 */
public class StsConfig implements Serializable {

	private long configId;
	private boolean isFromQuery;
	private boolean isSelected;
	private StsAnaItemDetail stsAnaItemDetail;
	private StsCondition stsCondition;

	public long getConfigId() {
		return configId;
	}

	public void setConfigId(long configId) {
		this.configId = configId;
	}

	public boolean isFromQuery() {
		return isFromQuery;
	}

	public void setFromQuery(boolean isFromQuery) {
		this.isFromQuery = isFromQuery;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public StsAnaItemDetail getStsAnaItemDetail() {
		return stsAnaItemDetail;
	}

	public void setStsAnaItemDetail(StsAnaItemDetail stsAnaItemDetail) {
		this.stsAnaItemDetail = stsAnaItemDetail;
	}

	public StsCondition getStsCondition() {
		return stsCondition;
	}

	public void setStsCondition(StsCondition stsCondition) {
		this.stsCondition = stsCondition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (configId ^ (configId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StsConfig other = (StsConfig) obj;
		if (configId != other.configId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StsConfig [configId=" + configId + ", isFromQuery="
				+ isFromQuery + ", isSelected=" + isSelected
				+ ", stsAnaItemDetail=" + stsAnaItemDetail + ", stsCondition="
				+ stsCondition + "]";
	}

}
