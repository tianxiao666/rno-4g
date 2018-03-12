package com.iscreate.plat.location.service;

import java.util.Date;
import java.util.List;

import com.iscreate.plat.location.dao.PdaGpsDao;
import com.iscreate.plat.location.pojo.PdaClient;
import com.iscreate.plat.location.pojo.PdaGpsLocation;

public class PdaGpsServiceImpl implements PdaGpsService{

	private PdaGpsDao pdaGpsDao;

	/**
	 * 保存GPS
	 * @param gpsLocation
	 */
	public void saveGpsLocationService(String gpsLocation){
		String[] gpsArray = gpsLocation.split("\\|");
		String imei = gpsArray[0];
		String userId = gpsArray[1];
		String longitude = gpsArray[2];
		String latitude = gpsArray[3];
//		String pickTime = gpsArray[4];
		String address = gpsArray[5];
		String coorType = gpsArray[6];
		String locType = gpsArray[7];
		String poi = gpsArray[8];
		String radius = gpsArray[9];
		String speed = gpsArray[10];
		String derect = gpsArray[11];
		String satelliteNumber = gpsArray[12];
		if(userId !=null && !"null".equals(userId) && !"".equals(imei) && !"0".equals(address) && !"".equals(userId)){
			PdaGpsLocation pgl = new PdaGpsLocation();
			pgl.setImei(imei);
			pgl.setUserId(userId);
			pgl.setLongitude(this.strToDouble(longitude));
			pgl.setLatitude(this.strToDouble(latitude));
			pgl.setPickTime(new Date());
			pgl.setAddress(address);
			pgl.setCoorType(coorType);
			pgl.setLocType(this.strToInteger(locType));
			pgl.setPoi(poi);
			pgl.setRadius(this.strToFloat(radius));
			pgl.setSpeed(this.strToFloat(speed));
			pgl.setDerect(this.strToFloat(derect));
			pgl.setSatelliteNumber(this.strToInteger(satelliteNumber));
			this.pdaGpsDao.saveGpsLocation(pgl);
		}
	}
	
	/**
	 * 根据用户Id获取GPS
	 * @param userId
	 * @return
	 */
	public List<PdaGpsLocation> getGpsLocationByUserIdService(String userId){
		return this.pdaGpsDao.getGpsLocationByUserId(userId);
	}
	
	/**
	 * 保存客户端登陆信息
	 * @param gpsLocation
	 */
	public void saveClientLoginService(String gpsLocation){
		this.saveClient(gpsLocation,0);
	}
	
	/**
	 * 保存客户端登出信息
	 * @param gpsLocation
	 */
	public void saveClientExitService(String gpsLocation){
		this.saveClient(gpsLocation, 1);
	}
	
	/**
	 * 根据用户Id获取最近的一个GPS位置信息
	 * @param userId
	 * @return
	 */
	public PdaGpsLocation getLastGpsLocationByUserIdService(String userId){
		return this.pdaGpsDao.getLastGpsLocationByUserId(userId);
	}
	
	private void saveClient(String gpsLocation,int state){
		String[] gpsArray = gpsLocation.split("\\|");
		String userId = gpsArray[0];
		String imei = gpsArray[1];
//		String time = gpsArray[2];
		if(!"".equals(imei)){
			PdaClient pc = new PdaClient();
			pc.setUserId(userId);
			pc.setImei(imei);
			pc.setState(state);
			pc.setTime(new Date());
			this.pdaGpsDao.saveClient(pc);
		}
	}
	
	private Double strToDouble(String str){
		Double dou = new Double(0);
		if(str!=null && !"".equals(str)){
			dou = Double.parseDouble(str);
		}
		return dou;
	}
	
	private Integer strToInteger(String str){
		Integer i = new Integer(0);
		if(str!=null && !"".equals(str)){
			i = Integer.parseInt(str);
		}
		return i;
	}
	
	private Float strToFloat(String str){
		Float f = new Float(0);
		if(str!=null && !"".equals(str)){
			f = Float.parseFloat(str);
		}
		return f;
	}
	
	public PdaGpsDao getPdaGpsDao() {
		return pdaGpsDao;
	}

	public void setPdaGpsDao(PdaGpsDao pdaGpsDao) {
		this.pdaGpsDao = pdaGpsDao;
	}
	
}