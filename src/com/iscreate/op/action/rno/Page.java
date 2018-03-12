package com.iscreate.op.action.rno;

import com.google.gson.Gson;

public class Page {

	private int totalPageCnt = 0;// 总页数
	private int pageSize = 25;// 每页记录数
	private int currentPage = 1;// 当前页数，从1开始
	private int totalCnt = -1;// 总记录数

	private int forcedStartIndex = -1;// 如果这个值大于0，说明直接使用这个值，而不是使用currentPage*pageSize这样计算得到

	public Page() {

	}

	public Page copy(){
		Page another=new Page();
		another.currentPage=currentPage;
		another.forcedStartIndex=forcedStartIndex;
		another.pageSize=pageSize;
		another.totalCnt=totalCnt;
		another.totalPageCnt=totalPageCnt;
		
		return another;
	}
	
	public int getTotalPageCnt() {
		return totalPageCnt;
	}

	public void setTotalPageCnt(int totalPageCnt) {
		this.totalPageCnt = totalPageCnt;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public int getForcedStartIndex() {
		return forcedStartIndex;
	}

	public void setForcedStartIndex(int forcedStartIndex) {
		this.forcedStartIndex = forcedStartIndex;
	}

	public int calculateStart(){
		if(forcedStartIndex>0){
			return forcedStartIndex;
		}else{
			return (this.getCurrentPage() - 1) * this.getPageSize();
		}
	}

	@Override
	public String toString() {
		return "Page [totalPageCnt=" + totalPageCnt + ", pageSize=" + pageSize
				+ ", currentPage=" + currentPage + ", totalCnt=" + totalCnt
				+ ", forcedStartIndex=" + forcedStartIndex + "]";
	}

	public static void main(String[] args) {
		Page page = new Page();
		Gson gson = new Gson();
		String json = gson.toJson(page);
		System.out.println(json);
	}

}
