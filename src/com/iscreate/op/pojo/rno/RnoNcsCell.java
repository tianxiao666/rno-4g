/**
* author:Liang YJ 
* 2014-2-18 下午5:08:47
* RnoNcsCell.java 
*/
package com.iscreate.op.pojo.rno;

/**
 * @author Liang YJ
 * @date 2014-2-18 17:09
 */
public class RnoNcsCell
{
	private Long ncsDescId;
	private String cell;
	private String chineseName; 
	private Double lng;
	private Double lat; 
	private String allLngLats;
	private Integer freqCnt;
	private Double beInterfer;
	private Double srcInterfer;
	private Double netStructFactor; 
	private Double reduntCoverFact;
	private Double overlapCover;
	private Double expectedCoverDis;
	private Double overshootingFact;
	private Double detectCnt;
	private Double cellCover;
	private Double expectedCapacity;
	private Double capacityDestroyFact;
	private Double capacityDestroy;
	
	public RnoNcsCell()
	{
		super();
	}
	public RnoNcsCell(Long ncsDescId, String cell, String chineseName,
			Double lng, Double lat, String allLngLats, Integer freqCnt,
			Double beInterfer, Double srcInterfer, Double netStructFactor,
			Double reduntCoverFact, Double overlapCover,
			Double expectedCoverDis, Double overshootingFact, Double detectCnt,
			Double cellCover, Double expectedCapacity,
			Double capacityDestroyFact, Double capacityDestroy) {
		super();
		this.ncsDescId = ncsDescId;
		this.cell = cell;
		this.chineseName = chineseName;
		this.lng = lng;
		this.lat = lat;
		this.allLngLats = allLngLats;
		this.freqCnt = freqCnt;
		this.beInterfer = beInterfer;
		this.srcInterfer = srcInterfer;
		this.netStructFactor = netStructFactor;
		this.reduntCoverFact = reduntCoverFact;
		this.overlapCover = overlapCover;
		this.expectedCoverDis = expectedCoverDis;
		this.overshootingFact = overshootingFact;
		this.detectCnt = detectCnt;
		this.cellCover = cellCover;
		this.expectedCapacity = expectedCapacity;
		this.capacityDestroyFact = capacityDestroyFact;
		this.capacityDestroy = capacityDestroy;
	}
	public String getAllLngLats() {
		return allLngLats;
	}
	public void setAllLngLats(String allLngLats) {
		this.allLngLats = allLngLats;
	}
	public Integer getFreqCnt() {
		return freqCnt;
	}
	public void setFreqCnt(Integer freqCnt) {
		this.freqCnt = freqCnt;
	}
	public Double getSrcInterfer() {
		return srcInterfer;
	}
	public void setSrcInterfer(Double srcInterfer) {
		this.srcInterfer = srcInterfer;
	}
	public Double getReduntCoverFact() {
		return reduntCoverFact;
	}
	public void setReduntCoverFact(Double reduntCoverFact) {
		this.reduntCoverFact = reduntCoverFact;
	}
	public Double getOverlapCover() {
		return overlapCover;
	}
	public void setOverlapCover(Double overlapCover) {
		this.overlapCover = overlapCover;
	}
	public Double getExpectedCoverDis() {
		return expectedCoverDis;
	}
	public void setExpectedCoverDis(Double expectedCoverDis) {
		this.expectedCoverDis = expectedCoverDis;
	}
	public Double getOvershootingFact() {
		return overshootingFact;
	}
	public void setOvershootingFact(Double overshootingFact) {
		this.overshootingFact = overshootingFact;
	}
	public Double getDetectCnt() {
		return detectCnt;
	}
	public void setDetectCnt(Double detectCnt) {
		this.detectCnt = detectCnt;
	}
	public Double getCellCover() {
		return cellCover;
	}
	public void setCellCover(Double cellCover) {
		this.cellCover = cellCover;
	}
	public Double getExpectedCapacity() {
		return expectedCapacity;
	}
	public void setExpectedCapacity(Double expectedCapacity) {
		this.expectedCapacity = expectedCapacity;
	}
	public Double getCapacityDestroyFact() {
		return capacityDestroyFact;
	}
	public void setCapacityDestroyFact(Double capacityDestroyFact) {
		this.capacityDestroyFact = capacityDestroyFact;
	}
	public Double getCapacityDestroy() {
		return capacityDestroy;
	}
	public void setCapacityDestroy(Double capacityDestroy) {
		this.capacityDestroy = capacityDestroy;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	
	
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getAllLnglats() {
		return allLngLats;
	}
	public void setAllLnglats(String allLngLats) {
		this.allLngLats = allLngLats;
	}
	public Double getBeInterfer() {
		return beInterfer;
	}
	public void setBeInterfer(Double beInterfer) {
		this.beInterfer = beInterfer;
	}
	public Double getNetStructFactor() {
		return netStructFactor;
	}
	public void setNetStructFactor(Double netStructFactor) {
		this.netStructFactor = netStructFactor;
	}
	public Long getNcsDescId() {
		return ncsDescId;
	}
	public void setNcsDescId(Long rnoNcsDescId) {
		ncsDescId = rnoNcsDescId;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("{ncsDescId:").append(ncsDescId).append(",cell:").append(cell).append(",chineseName:").append(chineseName).append(",lng").append(lng);
		buf.append(",lat").append(lat).append(",allLngLat").append(allLngLats).append(",beInterfer").append(beInterfer).append(",netStructFactor").append(netStructFactor).append("}");
		return buf.toString();
	}
}
