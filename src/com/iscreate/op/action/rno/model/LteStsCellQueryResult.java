package com.iscreate.op.action.rno.model;

import java.util.List;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoLteCellForPm;

public class LteStsCellQueryResult {
	private int totalCnt;
	private int faultCnt;
	private List<RnoLteCellForPm> lteCells;
	private Page page;
			
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}


	public int getFaultCnt() {
		return faultCnt;
	}
	public void setFaultCnt(int faultCnt) {
		this.faultCnt = faultCnt;
	}
	public List<RnoLteCellForPm> getLteCells() {
		return lteCells;
	}
	public void setLteCells(List<RnoLteCellForPm> lteCells) {
		this.lteCells = lteCells;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
}
