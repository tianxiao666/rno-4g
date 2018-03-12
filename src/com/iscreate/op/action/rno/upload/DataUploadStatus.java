package com.iscreate.op.action.rno.upload;

public class DataUploadStatus {

	private long readedBytes;
	private long totalBytes;
	private int currentItem;

	
	public long getReadedBytes() {
		return readedBytes;
	}
	public void setReadedBytes(long readedBytes) {
		this.readedBytes = readedBytes;
	}
	public long getTotalBytes() {
		return totalBytes;
	}
	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
	public int getCurrentItem() {
		return currentItem;
	}
	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}
	@Override
	public String toString() {
		return "DataUploadStatus [readedBytes=" + readedBytes + ", totalBytes="
				+ totalBytes + "]";
	}
	
	
}
