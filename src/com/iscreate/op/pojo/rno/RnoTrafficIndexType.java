package com.iscreate.op.pojo.rno;

/**
 * RnoTrafficIndexType entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoTrafficIndexType implements java.io.Serializable {

	// Fields

	private Long trafficIndexId;
	private String type;
	private String code;
	private String name;
	private String description;

	// Constructors

	/** default constructor */
	public RnoTrafficIndexType() {
	}

	/** minimal constructor */
	public RnoTrafficIndexType(String type, String code, String name) {

		this.type = type;
		this.code = code;
		this.name = name;

	}

	public RnoTrafficIndexType(Long trafficIndexId, String type, String code,
			String name, String description) {
		super();
		this.trafficIndexId = trafficIndexId;
		this.type = type;
		this.code = code;
		this.name = name;
		this.description = description;
	}

	// Property accessors

	public Long getTrafficIndexId() {
		return this.trafficIndexId;
	}

	public void setTrafficIndexId(Long trafficIndexId) {
		this.trafficIndexId = trafficIndexId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}