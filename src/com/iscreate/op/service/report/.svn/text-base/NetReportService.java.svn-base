package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

public interface NetReportService {

	public abstract List<Map<String, String>> getStationByArea(String userId);

	public abstract List<Map<String, String>> getWANByArea(String userId);

	public abstract List<Map<String, String>> getStationByOrg(String userId);

	public abstract List<Map<String, String>> getWANByOrg(String userId);

	public abstract List<Map<String, String>> getStationByAreaId(String areaId);

	public abstract List<Map<String, String>> getWANByAreaId(String areaId);

	public abstract List<Map<String, String>> getStationByOrgId(long orgId);

	public abstract List<Map<String, String>> getWANByOrgId(long orgId);
	
	public List<Map<String,String>> getStationByAreaIdAndTopOrg(String userId,String areaId);
	
	public List<Map<String,String>> getWANByAreaIdAndTopOrg(String userId,String areaId);
	
	public List<Map<String,String>> getStationByOrgIdAndTopOrg(long orgId);
	
	public List<Map<String,String>> getWANByOrgIdAndTopOrg(long orgId);
	
	public List<Map<String,String>> getStationByOrgIdProject(long orgId);
	
	public List<Map<String,String>> getWANByOrgIdProject(long orgId);
	

}