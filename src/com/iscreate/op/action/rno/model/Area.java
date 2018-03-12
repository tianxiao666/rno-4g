package com.iscreate.op.action.rno.model;

import java.util.Map;

public class Area {
	// id=190, _entityType=Sys_Area, area_id=190, level=区/县, name=海珠区,
	// path=/82/88/190/,
	// longitude=113.317451, area_level=区/县, latitude=23.084153,
	// _entityId=179063, parent_id=88
	private long area_id;
	private String name;
	private String area_level;
	private String path;
	private double longitude;
	private double latitude;
	private long parent_id;
	
	//[{ENTITY_ID=168055,
	//ENTITY_TYPE=Sys_Area, NAME=天河区, PATH=/82/88/187/, 
	//CREATETIME=2013-06-27 18:25:14.0, LONGITUDE=113.361397, 
	//AREA_LEVEL=区/县, AREA_ID=187, PARENT_ID=88, UPDATETIME=2013-06-27 18:25:14.0, LATITUDE=23.12489}]
	public static Area fromMap(Map<String, Object> map) {
		//System.out.println("map=="+map);
		if (map == null) {
			return null;
		}
		Area area = new Area();
		if (map.containsKey("AREA_ID") && map.get("AREA_ID")!=null) {
			try {
				area.setArea_id(Long.valueOf(map.get("AREA_ID").toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (map.containsKey("NAME")) {
			area.setName((String)map.get("NAME"));
		}
		
		if (map.containsKey("AREA_LEVEL")) {
			area.setArea_level(map.get("AREA_LEVEL").toString());
		}
		
		if(map.containsKey("PATH")){
			area.setPath(map.get("PATH").toString());
		}
		
		if(map.containsKey("LONGITUDE") && map.get("LONGITUDE")!=null){
			try {
				if (map.get("LONGITUDE")!=null) {
					area.setLongitude(Double.valueOf(map.get("LONGITUDE").toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(map.containsKey("LATITUDE") && map.get("LATITUDE") !=null){
			try {
				if (map.get("LATITUDE")!=null) {
			area.setLatitude(Double.valueOf(map.get("LATITUDE").toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (map.containsKey("PARENT_ID") && map.get("PARENT_ID")!=null) {
			try {
				if (map.get("PARENT_ID")!=null) {
				area.setParent_id(Long.valueOf(map.get("PARENT_ID").toString()));
				}
				} catch (Exception e) {
				e.printStackTrace();
				System.out.println("map.get(\"PARENT_ID\")=="+map.get("PARENT_ID"));
			}
		}
		
		return area;
	}

	public long getArea_id() {
		return area_id;
	}

	public void setArea_id(long area_id) {
		this.area_id = area_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getArea_level() {
		return area_level;
	}

	public void setArea_level(String area_level) {
		this.area_level = area_level;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	
}
