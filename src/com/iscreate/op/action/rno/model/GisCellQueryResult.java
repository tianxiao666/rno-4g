package com.iscreate.op.action.rno.model;

import java.util.List;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoGisCell;

public class GisCellQueryResult {

	transient private int totalCnt;
	private List<RnoGisCell> gisCells;
	private Page page;
	
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public List<RnoGisCell> getGisCells() {
		return gisCells;
	}
	public void setGisCells(List<RnoGisCell> gisCells) {
		this.gisCells = gisCells;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
