package com.iscreate.op.action.rno.model;

import java.math.BigDecimal;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoLteCell;


public class ConfusionCellResult {

	private RnoLteCell cellOne = null;
	private RnoLteCell cellTwo = null;
	private RnoLteCell mainCell = null;
	
	public RnoLteCell getCellOne() {
		return cellOne;
	}
	public void setCellOne(RnoLteCell cellOne) {
		this.cellOne = cellOne;
	}
	public RnoLteCell getCellTwo() {
		return cellTwo;
	}
	public void setCellTwo(RnoLteCell cellTwo) {
		this.cellTwo = cellTwo;
	}
	public RnoLteCell getMainCell() {
		return mainCell;
	}
	public void setMainCell(RnoLteCell mainCell) {
		this.mainCell = mainCell;
	}
	

}
