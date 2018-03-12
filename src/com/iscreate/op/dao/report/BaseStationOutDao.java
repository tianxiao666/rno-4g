package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;

public interface BaseStationOutDao {

	public abstract List<Map<String, Object>> getbaseStationOut(
			final String orgId, final String beginTime, final String endTime);

	public abstract List<Map<String, Object>> getAreaBaseStationCount(
			final String areaId);

}