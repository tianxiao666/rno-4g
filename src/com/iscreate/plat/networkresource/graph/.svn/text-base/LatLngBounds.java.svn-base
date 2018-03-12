package com.iscreate.plat.networkresource.graph;


/**
 * 经纬度窗口范围
 * @author gmh
 *
 * 2012-3-20下午04:00:25
 */
public class LatLngBounds {

	//西南
	private LatLng sw;
	//东北
	private LatLng ne;
	
	public LatLngBounds() {
		// TODO Auto-generated constructor stub
	}
	public LatLngBounds(LatLng sw,LatLng ne){
		this.sw = sw;
		this.ne = ne;
	}
	public LatLngBounds(double swLat,double swLng,double neLat,double neLng) {
		LatLng sw = new LatLng(swLat,swLng);
		LatLng ne = new LatLng(neLat, neLng);
		this.sw = sw;
		this.ne = ne;
	}
	
	
	public LatLng getSw() {
		return sw;
	}
	public void setSw(LatLng sw) {
		this.sw = sw;
	}
	public LatLng getNe() {
		return ne;
	}
	public void setNe(LatLng ne) {
		this.ne = ne;
	}
	/**
	 * 是否包含在某个经纬度窗口范围内
	 * @param other
	 * @return
	 * 2012-3-20 下午04:03:52
	 */
	public boolean containedInOther(LatLngBounds other){
		if(this.sw.containedIn(other) && this.ne.containedIn(other)){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 是否包含某个经纬度窗口
	 * @param other
	 * @return
	 * 2012-3-20 下午04:05:46
	 */
	public boolean containsOther(LatLngBounds other){
		if(other.getNe().containedIn(this) && other.getSw().containedIn(this)){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		return "[sw:"+this.getSw().toString()+",ne:"+this.getNe().toString()+"]";
	}
	
}
