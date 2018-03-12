package com.iscreate.op.pojo.rno;

import java.util.List;
import java.util.Map;

public class CellFreqInterfer {
	private String cell;
	private double sum;
	private List<Map<String, Object>> inter;

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public List<Map<String, Object>> getInter() {
		return inter;
	}

	public void setInter(List<Map<String, Object>> inter) {
		this.inter = inter;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "CellFreqInterfer [cell=" + cell + ", sum=" + sum + ", inter="
				+ inter + "]";
	}
	
	
}
