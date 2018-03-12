package com.iscreate.op.action.rno.model;

import java.util.List;
import java.util.Map;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoLteCell;


public class LteCellPciCollAndConfQueryResult {

	private Page page;
	private List<Long> pciList;
	private List<List<RnoLteCell>> collisionList;   //pci冲突的小区
	private List<List<ConfusionCellResult>> confusionList;    //pci混淆的小区
	private List<List<CellPairs>> collPairsList; //pci冲突小区对列表
	private List<List<CellPairs>> confPairsList; //pci冲突小区对列表
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<Long> getPciList() {
		return pciList;
	}
	public void setPciList(List<Long> pciList) {
		this.pciList = pciList;
	}

	public List<List<RnoLteCell>> getCollisionList() {
		return collisionList;
	}
	public void setCollisionList(List<List<RnoLteCell>> collisionList) {
		this.collisionList = collisionList;
	}
	public List<List<ConfusionCellResult>> getConfusionList() {
		return confusionList;
	}
	public void setConfusionList(List<List<ConfusionCellResult>> confusionList) {
		this.confusionList = confusionList;
	}
	public List<List<CellPairs>> getCollPairsList() {
		return collPairsList;
	}
	public void setCollPairsList(List<List<CellPairs>> collPairsList) {
		this.collPairsList = collPairsList;
	}
	public List<List<CellPairs>> getConfPairsList() {
		return confPairsList;
	}
	public void setConfPairsList(List<List<CellPairs>> confPairsList) {
		this.confPairsList = confPairsList;
	}



	
}
