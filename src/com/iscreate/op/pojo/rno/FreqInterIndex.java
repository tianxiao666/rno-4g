package com.iscreate.op.pojo.rno;

import java.util.List;

public class FreqInterIndex {

	private String label;
	private String cellTch;
	private List<AdjFreqInterDetailed> adjFreqInterDetaList;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCellTch() {
		return cellTch;
	}
	public void setCellTch(String cellTch) {
		this.cellTch = cellTch;
	}
	public List<AdjFreqInterDetailed> getAdjFreqInterDetaList() {
		return adjFreqInterDetaList;
	}
	public void setAdjFreqInterDetaList(
			List<AdjFreqInterDetailed> adjFreqInterDetaList) {
		this.adjFreqInterDetaList = adjFreqInterDetaList;
	}
}
