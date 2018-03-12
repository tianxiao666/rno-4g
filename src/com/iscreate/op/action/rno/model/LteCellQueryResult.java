package com.iscreate.op.action.rno.model;

import java.util.List;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoLteCell;

public class LteCellQueryResult {
	private int totalCnt;
	private List<RnoLteCell> lteCells;
	private Page page;
	
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}


	public List<RnoLteCell> getLteCells() {
		return lteCells;
	}
	public void setLteCells(List<RnoLteCell> lteCells) {
		this.lteCells = lteCells;
	}
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
