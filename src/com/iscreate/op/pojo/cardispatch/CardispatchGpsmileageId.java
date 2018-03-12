package com.iscreate.op.pojo.cardispatch;

import java.sql.Timestamp;
import java.util.Date;

/**
 * CardispatchGpsmileageId entity. @author MyEclipse Persistence Tools
 */

public class CardispatchGpsmileageId implements java.io.Serializable {

	// Fields

	private Long id;
	private Date gpsDate;

	// Constructors

	/** default constructor */
	public CardispatchGpsmileageId() {
	}


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Date getGpsDate() {
		return gpsDate;
	}


	public void setGpsDate(Date gpsDate) {
		this.gpsDate = gpsDate;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CardispatchGpsmileageId))
			return false;
		CardispatchGpsmileageId castOther = (CardispatchGpsmileageId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getGpsDate() == castOther.getGpsDate()) || (this
						.getGpsDate() != null && castOther.getGpsDate() != null && this
						.getGpsDate().equals(castOther.getGpsDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getGpsDate() == null ? 0 : this.getGpsDate().hashCode());
		return result;
	}

}