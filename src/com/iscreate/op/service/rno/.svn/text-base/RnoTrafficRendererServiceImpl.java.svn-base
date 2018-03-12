package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.rno.RnoTrafficRendererDao;
import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;

public class RnoTrafficRendererServiceImpl implements RnoTrafficRendererService {
	public RnoTrafficRendererDao rnoTrafficRendererDao;
	
	/**
	 * 根据区域ID与话务指标类型获取默认话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 2:15:29 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getDefaultRnoTrafficRenderer(String trafficCode){
		return rnoTrafficRendererDao.getDefaultRnoTrafficRenderer(trafficCode);
	}
	
	/**
	 * 根据话务指标类型获取对应类型数据
	* @author ou.jh
	* @date Oct 28, 2013 3:01:40 PM
	* @Description: TODO 
	* @param @param trafficCode
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getRnoTraffic(String trafficCode){
		return rnoTrafficRendererDao.getRnoTraffic(trafficCode);
	}
	
	/**
	 * 批量添加话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:39:19 PM
	* @Description: TODO 
	* @param @param bnoList        
	* @throws
	 */
	public void insertRnoTrafficRendererList(List<RnoTrafficRendererConfig> bnoList){
		if(bnoList != null && !bnoList.isEmpty()){
			for(RnoTrafficRendererConfig bno:bnoList){
				this.rnoTrafficRendererDao.insertBno(bno);
			}
		}
	}
	
	/**
	 * 批量更新话务性能渲染配置
	* @author ou.jh
	* @date Oct 28, 2013 4:39:29 PM
	* @Description: TODO 
	* @param @param bnoList        
	* @throws
	 */
	public void updateRnoTrafficRendererList(List<RnoTrafficRendererConfig> bnoList){
		if(bnoList != null && !bnoList.isEmpty()){
			for(RnoTrafficRendererConfig bno:bnoList){
				this.rnoTrafficRendererDao.updateBno(bno);
			}
		}
	}
	
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
	public List<Map<String, Object>> getRnoTrafficRendererByTrafficCodeAndAreaId(String trafficCode,long areaId){
		return rnoTrafficRendererDao.getRnoTrafficRendererByTrafficCodeAndAreaId(trafficCode,areaId);
	}

	public RnoTrafficRendererDao getRnoTrafficRendererDao() {
		return rnoTrafficRendererDao;
	}

	public void setRnoTrafficRendererDao(RnoTrafficRendererDao rnoTrafficRendererDao) {
		this.rnoTrafficRendererDao = rnoTrafficRendererDao;
	}
}
