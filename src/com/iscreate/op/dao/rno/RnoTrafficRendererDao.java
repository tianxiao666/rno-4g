package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public interface RnoTrafficRendererDao {
	/**
	 * 根据区域ID与话务指标类型获取默认话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 2:15:29 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getDefaultRnoTrafficRenderer(String trafficCode);
	
	/**
	 * 根据话务指标类型获取对应类型数据
	* @author ou.jh
	* @date Oct 28, 2013 3:01:40 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getRnoTraffic(String trafficCode);
	
	/**
	 * 添加话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:34:28 PM
	* @Description: TODO 
	* @param @param bno
	* @param @return        
	* @throws
	 */
	public Long insertBno(RnoTrafficRendererConfig bno);
	
	/**
	 * 添加话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:34:28 PM
	* @Description: TODO 
	* @param @param bno
	* @param @return        
	* @throws
	 */
	public void updateBno(RnoTrafficRendererConfig bno);
	
	/**
	 * 根据区域ID与话务指标类型获取话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 2:14:51 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRnoTrafficRendererByTrafficCodeAndAreaId(String trafficCode,long areaId);

}
