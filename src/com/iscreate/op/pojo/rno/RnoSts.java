package com.iscreate.op.pojo.rno;

/**
 * RnoSts entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoSts implements java.io.Serializable {

	// Fields

	private Long stsId;
	private Long descriptorId;
	private Long bscId;
	private String cell;
	private String cellChineseName;
	private Float tchIntactRate;
	private Float declareChannelNumber;
	private Float availableChannelNumber;
	private Float carrierNumber;
	private Float resourceUseRate;
	private Float traffic;
	private Float trafficPerLine;
	private Float accessOkRate;
	private Float radioAccess;
	private Float dropCallNumTogether;
	private Float radioDropRateNoHv;
	private Float icm;
	private Float handoutSucRate;
	private Float handinSucRate;
	private Float handoverSucRate;
	private Float psRadioUseRate;
	private Float pdchCarryingEfficiency;
	private Float dataTraffic;
	private Float downlinkBpdchReuse;
	private Float downlinkEpdchReuse;
	private Float downlinkTbfCongRate;

	// Constructors

	/** default constructor */
	public RnoSts() {
	}

	/** minimal constructor */
	public RnoSts(Long descriptorId, Long bscId, String cell) {
		this.descriptorId = descriptorId;
		this.bscId = bscId;
		this.cell = cell;
	}

	/** full constructor */
	public RnoSts(Long descriptorId, Long bscId, String cell,
			String cellChineseName, Float tchIntactRate,
			Float declareChannelNumber, Float availableChannelNumber,
			Float carrierNumber, Float resourceUseRate, Float traffic,
			Float trafficPerLine, Float accessOkRate, Float radioAccess,
			Float dropCallNumTogether, Float radioDropRateNoHv, Float icm,
			Float handoutSucRate, Float handinSucRate, Float handoverSucRate,
			Float psRadioUseRate, Float pdchCarryingEfficiency,
			Float dataTraffic, Float downlinkBpdchReuse,
			Float downlinkEpdchReuse, Float downlinkTbfCongRate) {
		this.descriptorId = descriptorId;
		this.bscId = bscId;
		this.cell = cell;
		this.cellChineseName = cellChineseName;
		this.tchIntactRate = tchIntactRate;
		this.declareChannelNumber = declareChannelNumber;
		this.availableChannelNumber = availableChannelNumber;
		this.carrierNumber = carrierNumber;
		this.resourceUseRate = resourceUseRate;
		this.traffic = traffic;
		this.trafficPerLine = trafficPerLine;
		this.accessOkRate = accessOkRate;
		this.radioAccess = radioAccess;
		this.dropCallNumTogether = dropCallNumTogether;
		this.radioDropRateNoHv = radioDropRateNoHv;
		this.icm = icm;
		this.handoutSucRate = handoutSucRate;
		this.handinSucRate = handinSucRate;
		this.handoverSucRate = handoverSucRate;
		this.psRadioUseRate = psRadioUseRate;
		this.pdchCarryingEfficiency = pdchCarryingEfficiency;
		this.dataTraffic = dataTraffic;
		this.downlinkBpdchReuse = downlinkBpdchReuse;
		this.downlinkEpdchReuse = downlinkEpdchReuse;
		this.downlinkTbfCongRate = downlinkTbfCongRate;
	}

	// Property accessors

	public Long getStsId() {
		return this.stsId;
	}

	public void setStsId(Long stsId) {
		this.stsId = stsId;
	}

	public Long getDescriptorId() {
		return this.descriptorId;
	}

	public void setDescriptorId(Long descriptorId) {
		this.descriptorId = descriptorId;
	}

	public Long getBscId() {
		return bscId;
	}

	public void setBscId(Long bscId) {
		this.bscId = bscId;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getCellChineseName() {
		return this.cellChineseName;
	}

	public void setCellChineseName(String cellChineseName) {
		this.cellChineseName = cellChineseName;
	}

	public Float getTchIntactRate() {
		return tchIntactRate;
	}

	public void setTchIntactRate(Float tchIntactRate) {
		this.tchIntactRate = tchIntactRate;
	}

	public Float getDeclareChannelNumber() {
		return declareChannelNumber;
	}

	public void setDeclareChannelNumber(Float declareChannelNumber) {
		this.declareChannelNumber = declareChannelNumber;
	}

	public Float getAvailableChannelNumber() {
		return availableChannelNumber;
	}

	public void setAvailableChannelNumber(Float availableChannelNumber) {
		this.availableChannelNumber = availableChannelNumber;
	}

	public Float getCarrierNumber() {
		return carrierNumber;
	}

	public void setCarrierNumber(Float carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public Float getResourceUseRate() {
		return resourceUseRate;
	}

	public void setResourceUseRate(Float resourceUseRate) {
		this.resourceUseRate = resourceUseRate;
	}

	public Float getTraffic() {
		return traffic;
	}

	public void setTraffic(Float traffic) {
		this.traffic = traffic;
	}

	public Float getTrafficPerLine() {
		return trafficPerLine;
	}

	public void setTrafficPerLine(Float trafficPerLine) {
		this.trafficPerLine = trafficPerLine;
	}

	public Float getAccessOkRate() {
		return accessOkRate;
	}

	public void setAccessOkRate(Float accessOkRate) {
		this.accessOkRate = accessOkRate;
	}

	public Float getRadioAccess() {
		return radioAccess;
	}

	public void setRadioAccess(Float radioAccess) {
		this.radioAccess = radioAccess;
	}

	public Float getDropCallNumTogether() {
		return dropCallNumTogether;
	}

	public void setDropCallNumTogether(Float dropCallNumTogether) {
		this.dropCallNumTogether = dropCallNumTogether;
	}

	public Float getRadioDropRateNoHv() {
		return radioDropRateNoHv;
	}

	public void setRadioDropRateNoHv(Float radioDropRateNoHv) {
		this.radioDropRateNoHv = radioDropRateNoHv;
	}

	public Float getIcm() {
		return icm;
	}

	public void setIcm(Float icm) {
		this.icm = icm;
	}

	public Float getHandoutSucRate() {
		return handoutSucRate;
	}

	public void setHandoutSucRate(Float handoutSucRate) {
		this.handoutSucRate = handoutSucRate;
	}

	public Float getHandinSucRate() {
		return handinSucRate;
	}

	public void setHandinSucRate(Float handinSucRate) {
		this.handinSucRate = handinSucRate;
	}

	public Float getHandoverSucRate() {
		return handoverSucRate;
	}

	public void setHandoverSucRate(Float handoverSucRate) {
		this.handoverSucRate = handoverSucRate;
	}

	public Float getPsRadioUseRate() {
		return psRadioUseRate;
	}

	public void setPsRadioUseRate(Float psRadioUseRate) {
		this.psRadioUseRate = psRadioUseRate;
	}

	public Float getPdchCarryingEfficiency() {
		return pdchCarryingEfficiency;
	}

	public void setPdchCarryingEfficiency(Float pdchCarryingEfficiency) {
		this.pdchCarryingEfficiency = pdchCarryingEfficiency;
	}

	public Float getDataTraffic() {
		return dataTraffic;
	}

	public void setDataTraffic(Float dataTraffic) {
		this.dataTraffic = dataTraffic;
	}

	public Float getDownlinkBpdchReuse() {
		return downlinkBpdchReuse;
	}

	public void setDownlinkBpdchReuse(Float downlinkBpdchReuse) {
		this.downlinkBpdchReuse = downlinkBpdchReuse;
	}

	public Float getDownlinkEpdchReuse() {
		return downlinkEpdchReuse;
	}

	public void setDownlinkEpdchReuse(Float downlinkEpdchReuse) {
		this.downlinkEpdchReuse = downlinkEpdchReuse;
	}

	public Float getDownlinkTbfCongRate() {
		return downlinkTbfCongRate;
	}

	public void setDownlinkTbfCongRate(Float downlinkTbfCongRate) {
		this.downlinkTbfCongRate = downlinkTbfCongRate;
	}

}