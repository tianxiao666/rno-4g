package com.iscreate.plat.networkresource.graph;



/**
 * 经纬度
 * 
 * @author brightming
 * 
 */
public class LatLng {

	private double latitude;
	private double longitude;

	public LatLng() {
		// TODO Auto-generated constructor stub
	}

	public LatLng(double lat, double lng) {
		this.longitude = lng;
		this.latitude = lat;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 是否包含在某个经纬度范围内
	 * 
	 * @param latLngBounds
	 * @return
	 * 
	 *         $Author gmh
	 * 
	 *         2012-3-23 上午11:17:55
	 */
	public boolean containedIn(LatLngBounds latLngBounds) {
		if (latLngBounds == null || latLngBounds.getNe() == null
				|| latLngBounds.getSw() == null) {
			return false;
		}
		LatLng sw = latLngBounds.getSw();
		LatLng ne = latLngBounds.getNe();
		if (longitude >= sw.getLongitude() && latitude >= sw.getLatitude()
				&& longitude <= ne.getLongitude()
				&& latitude <= ne.getLatitude()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public String toString(){
		return "("+this.latitude+","+this.longitude+")";
	}
}
