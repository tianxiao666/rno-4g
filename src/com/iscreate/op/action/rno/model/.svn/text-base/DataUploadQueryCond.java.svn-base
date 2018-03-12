package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

/**
 * 数据上传查询条件
 * 
 * @author brightming
 * 
 */
public class DataUploadQueryCond {

	long cityId = -1;
	String begUploadDate = null;
	String endUploadDate = null;
	String fileStatus = null;
	int businessDataType = 0;

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getBegUploadDate() {
		return begUploadDate;
	}

	public void setBegUploadDate(String begUploadDate) {
		this.begUploadDate = begUploadDate;
	}

	public String getEndUploadDate() {
		return endUploadDate;
	}

	public void setEndUploadDate(String endUploadDate) {
		this.endUploadDate = endUploadDate;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public int getBusinessDataType() {
		return businessDataType;
	}

	public void setBusinessDataType(int businessDataType) {
		this.businessDataType = businessDataType;
	}

	public String buildWhereCont() {
		String where = "";
		
		DateUtil dateUtil=new DateUtil();
		if (!StringUtils.isBlank(fileStatus)
				&& !StringUtils.equals("全部", fileStatus)) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " FILE_STATUS ='" + fileStatus + "'";
		}
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		// 数据类型
		where += StringUtils.isBlank(where) ? "" : " and ";
		where += " BUSINESS_DATA_TYPE=" + businessDataType;

		if (!StringUtils.isBlank(begUploadDate)) {
			Date bd = RnoHelper.parseDateArbitrary(begUploadDate);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " UPLOAD_TIME>=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(endUploadDate)) {
			endUploadDate+=" 23:59:59";
			Date bd = RnoHelper.parseDateArbitrary(endUploadDate);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " UPLOAD_TIME<=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		
		return where;
	}

	@Override
	public String toString() {
		return "DataUploadQueryCond [cityId=" + cityId + ", begUploadDate="
				+ begUploadDate + ", endUploadDate=" + endUploadDate
				+ ", fileStatus=" + fileStatus + ", businessDataType="
				+ businessDataType + "]";
	}


}
