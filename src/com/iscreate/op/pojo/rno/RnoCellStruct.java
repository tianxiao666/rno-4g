package com.iscreate.op.pojo.rno;

/**
 * RnoCellStruct entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoCellStruct implements java.io.Serializable {

	// Fields

	private Long rnoCellStructId;
	private Long rnoCellStructDescId;
	private String cell;
	private Long cellLevel;
	private Double overlapClusterWeight;
	private Double interferenceCoefficient;
	private Double netStructIndex;
	private Double redundanceCoverIndex;
	private Double overlapCover;
	private Double interSourceCoefficient;
	private Double overshootingCoefficient;
	private Long cellDetectCnt;
	private Double expectedCoverDistance;
	private Double comprehensiveTraffic;
	private Double audioTraffic;
	private Double dataTraffic;
	private Double dwnBadTrafficRatio;
	private Double upBadTraffRatio;
	private Double dwnQuality;
	private Double upQuality;
	private Double upInterferenceCoefficient;
	private Double upBaseNoise;
	private Double coverLimitedRatio;
	private Double taMean;

	// Constructors

	/** default constructor */
	public RnoCellStruct() {
	}

	/** minimal constructor */
	public RnoCellStruct(Long rnoCellStructDescId) {
		this.rnoCellStructDescId = rnoCellStructDescId;
	}

	/** full constructor */
	public RnoCellStruct(Long rnoCellStructDescId, String cell, Long cellLevel,
			Double overlapClusterWeight, Double interferenceCoefficient,
			Double netStructIndex, Double redundanceCoverIndex, Double overlapCover,
			Double interSourceCoefficient, Double overshootingCoefficient,
			Long cellDetectCnt, Double expectedCoverDistance,
			Double comprehensiveTraffic, Double audioTraffic, Double dataTraffic,
			Double dwnBadTrafficRatio, Double upBadTraffRatio, Double dwnQuality,
			Double upQuality, Double upInterferenceCoefficient, Double upBaseNoise,
			Double coverLimitedRatio, Double taMean) {
		this.rnoCellStructDescId = rnoCellStructDescId;
		this.cell = cell;
		this.cellLevel = cellLevel;
		this.overlapClusterWeight = overlapClusterWeight;
		this.interferenceCoefficient = interferenceCoefficient;
		this.netStructIndex = netStructIndex;
		this.redundanceCoverIndex = redundanceCoverIndex;
		this.overlapCover = overlapCover;
		this.interSourceCoefficient = interSourceCoefficient;
		this.overshootingCoefficient = overshootingCoefficient;
		this.cellDetectCnt = cellDetectCnt;
		this.expectedCoverDistance = expectedCoverDistance;
		this.comprehensiveTraffic = comprehensiveTraffic;
		this.audioTraffic = audioTraffic;
		this.dataTraffic = dataTraffic;
		this.dwnBadTrafficRatio = dwnBadTrafficRatio;
		this.upBadTraffRatio = upBadTraffRatio;
		this.dwnQuality = dwnQuality;
		this.upQuality = upQuality;
		this.upInterferenceCoefficient = upInterferenceCoefficient;
		this.upBaseNoise = upBaseNoise;
		this.coverLimitedRatio = coverLimitedRatio;
		this.taMean = taMean;
	}

	// Property accessors

	public Long getRnoCellStructId() {
		return this.rnoCellStructId;
	}

	public void setRnoCellStructId(Long rnoCellStructId) {
		this.rnoCellStructId = rnoCellStructId;
	}

	public Long getRnoCellStructDescId() {
		return this.rnoCellStructDescId;
	}

	public void setRnoCellStructDescId(Long rnoCellStructDescId) {
		this.rnoCellStructDescId = rnoCellStructDescId;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public Long getCellLevel() {
		return this.cellLevel;
	}

	public void setCellLevel(Long cellLevel) {
		this.cellLevel = cellLevel;
	}

	public Double getOverlapClusterWeight() {
		return overlapClusterWeight;
	}

	public void setOverlapClusterWeight(Double overlapClusterWeight) {
		this.overlapClusterWeight = overlapClusterWeight;
	}

	public Double getInterferenceCoefficient() {
		return interferenceCoefficient;
	}

	public void setInterferenceCoefficient(Double interferenceCoefficient) {
		this.interferenceCoefficient = interferenceCoefficient;
	}

	public Double getNetStructIndex() {
		return netStructIndex;
	}

	public void setNetStructIndex(Double netStructIndex) {
		this.netStructIndex = netStructIndex;
	}

	public Double getRedundanceCoverIndex() {
		return redundanceCoverIndex;
	}

	public void setRedundanceCoverIndex(Double redundanceCoverIndex) {
		this.redundanceCoverIndex = redundanceCoverIndex;
	}

	public Double getOverlapCover() {
		return overlapCover;
	}

	public void setOverlapCover(Double overlapCover) {
		this.overlapCover = overlapCover;
	}

	public Double getInterSourceCoefficient() {
		return interSourceCoefficient;
	}

	public void setInterSourceCoefficient(Double interSourceCoefficient) {
		this.interSourceCoefficient = interSourceCoefficient;
	}

	public Double getOvershootingCoefficient() {
		return overshootingCoefficient;
	}

	public void setOvershootingCoefficient(Double overshootingCoefficient) {
		this.overshootingCoefficient = overshootingCoefficient;
	}

	public Long getCellDetectCnt() {
		return cellDetectCnt;
	}

	public void setCellDetectCnt(Long cellDetectCnt) {
		this.cellDetectCnt = cellDetectCnt;
	}

	public Double getExpectedCoverDistance() {
		return expectedCoverDistance;
	}

	public void setExpectedCoverDistance(Double expectedCoverDistance) {
		this.expectedCoverDistance = expectedCoverDistance;
	}

	public Double getComprehensiveTraffic() {
		return comprehensiveTraffic;
	}

	public void setComprehensiveTraffic(Double comprehensiveTraffic) {
		this.comprehensiveTraffic = comprehensiveTraffic;
	}

	public Double getAudioTraffic() {
		return audioTraffic;
	}

	public void setAudioTraffic(Double audioTraffic) {
		this.audioTraffic = audioTraffic;
	}

	public Double getDataTraffic() {
		return dataTraffic;
	}

	public void setDataTraffic(Double dataTraffic) {
		this.dataTraffic = dataTraffic;
	}

	public Double getDwnBadTrafficRatio() {
		return dwnBadTrafficRatio;
	}

	public void setDwnBadTrafficRatio(Double dwnBadTrafficRatio) {
		this.dwnBadTrafficRatio = dwnBadTrafficRatio;
	}

	public Double getUpBadTraffRatio() {
		return upBadTraffRatio;
	}

	public void setUpBadTraffRatio(Double upBadTraffRatio) {
		this.upBadTraffRatio = upBadTraffRatio;
	}

	public Double getDwnQuality() {
		return dwnQuality;
	}

	public void setDwnQuality(Double dwnQuality) {
		this.dwnQuality = dwnQuality;
	}

	public Double getUpQuality() {
		return upQuality;
	}

	public void setUpQuality(Double upQuality) {
		this.upQuality = upQuality;
	}

	public Double getUpInterferenceCoefficient() {
		return upInterferenceCoefficient;
	}

	public void setUpInterferenceCoefficient(Double upInterferenceCoefficient) {
		this.upInterferenceCoefficient = upInterferenceCoefficient;
	}

	public Double getUpBaseNoise() {
		return upBaseNoise;
	}

	public void setUpBaseNoise(Double upBaseNoise) {
		this.upBaseNoise = upBaseNoise;
	}

	public Double getCoverLimitedRatio() {
		return coverLimitedRatio;
	}

	public void setCoverLimitedRatio(Double coverLimitedRatio) {
		this.coverLimitedRatio = coverLimitedRatio;
	}

	public Double getTaMean() {
		return taMean;
	}

	public void setTaMean(Double taMean) {
		this.taMean = taMean;
	}

	
}