package com.iscreate.op.pojo.cardispatch;

/**
 * CardispatchCar entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CardispatchCar implements java.io.Serializable {

	// Fields

	private Long carId;
	private String carModel;
	private Long carBizId;
	private String carNumber;
	private String carPic;
	private Long status;
	private String carBrand;
	private String carType;
	private Double loadWeight;
	private Integer passengerNumber;
	private Double leasePay;
	private Double custodyFee;
	private Integer carAge;
	private String carMarker;
	private Integer seatCount;
	//当前车最新经纬度
	private Double longitude;
	private Double latitude;
	
	
	// Constructors

	/** default constructor */
	public CardispatchCar() {
	}

	/** full constructor */
	public CardispatchCar(String carModel, Long carBizId, String carNumber,
			String carPic, Long status, String carBrand, String carType,
			Double loadWeight, Integer passengerNumber, Double leasePay,
			Double custodyFee, Integer carAge, String carMarker,
			Integer seatCount) {
		this.carModel = carModel;
		this.carBizId = carBizId;
		this.carNumber = carNumber;
		this.carPic = carPic;
		this.status = status;
		this.carBrand = carBrand;
		this.carType = carType;
		this.loadWeight = loadWeight;
		this.passengerNumber = passengerNumber;
		this.leasePay = leasePay;
		this.custodyFee = custodyFee;
		this.carAge = carAge;
		this.carMarker = carMarker;
		this.seatCount = seatCount;
	}

	// Property accessors

	

	public String getCarModel() {
		return this.carModel;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public Long getCarBizId() {
		return this.carBizId;
	}

	public void setCarBizId(Long carBizId) {
		this.carBizId = carBizId;
	}

	public String getCarNumber() {
		return this.carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getCarPic() {
		return this.carPic;
	}

	public void setCarPic(String carPic) {
		this.carPic = carPic;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCarBrand() {
		return this.carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarType() {
		return this.carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public Double getLoadWeight() {
		return this.loadWeight;
	}

	public void setLoadWeight(Double loadWeight) {
		this.loadWeight = loadWeight;
	}

	public Integer getPassengerNumber() {
		return this.passengerNumber;
	}

	public void setPassengerNumber(Integer passengerNumber) {
		this.passengerNumber = passengerNumber;
	}

	public Double getLeasePay() {
		return this.leasePay;
	}

	public void setLeasePay(Double leasePay) {
		this.leasePay = leasePay;
	}

	public Double getCustodyFee() {
		return this.custodyFee;
	}

	public void setCustodyFee(Double custodyFee) {
		this.custodyFee = custodyFee;
	}

	public Integer getCarAge() {
		return this.carAge;
	}

	public void setCarAge(Integer carAge) {
		this.carAge = carAge;
	}

	public String getCarMarker() {
		return this.carMarker;
	}

	public void setCarMarker(String carMarker) {
		this.carMarker = carMarker;
	}

	public Integer getSeatCount() {
		return this.seatCount;
	}

	public void setSeatCount(Integer seatCount) {
		this.seatCount = seatCount;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	
}