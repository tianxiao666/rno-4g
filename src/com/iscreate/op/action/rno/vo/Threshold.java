package com.iscreate.op.action.rno.vo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Threshold {

	private int id;
	private int orderNum;
	private String moduleType;
	private String code;
	private String descInfo;
	private String defaultVal;
	private String scopeDesc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescInfo() {
		return descInfo;
	}
	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
	}
	public String getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
	public String getScopeDesc() {
		return scopeDesc;
	}
	public void setScopeDesc(String scopeDesc) {
		this.scopeDesc = scopeDesc;
	}
	/*private String sameFreqInterThreshold;//强相关的同频干扰度门限
	private String overShootingIdealDisMultiple;//计算过覆盖系数时考虑小区理想覆盖距离的倍数
	private String betweenCellIdealDisMultiple;//计算小区间覆盖系数时考虑小区理想覆盖距离的倍数
	private String cellCheckTimesIdealDisMultiple;//计算小区检测次数时考虑小区理想覆盖距离的倍数
	private String cellCheckTimesSameFreqInterThreshold;//计算小区检测次数时考虑的同频干扰度门限
	private String cellIdealDisReferenceCellNum;//计算小区理想覆盖距离的时候参考强相关小区数
	private String gsm900CellFreqNum;//900小区频点数量
	private String gsm1800CellFreqNum;//1800小区频点数量
	private String gsm900CellIdealCapacity;//900小区理想容量
	private String gsm1800CellIdealCapacity;//1800小区理想容量
	private String dlCoverMinimumSignalStrengthThreshold;//计算下行覆盖率的最低信号强度门限
	private String ulCoverMinimumSignalStrengthThreshold;//计算上行覆盖率的最低信号强度门限
	private String interFactorMostDistant;//计算干扰系数时考虑的最远(优)距离
	private String interFactorSameAndAdjFreqMinimumThreshold;//计算干扰系数时，同频和邻频干扰系数的最低门限
	//描述信息
	private String sameFreqInterThresholdDescInfo;
	private String overShootingIdealDisMultipleDescInfo;
	private String betweenCellIdealDisMultipleDescInfo;
	private String cellCheckTimesIdealDisMultipleDescInfo;
	private String cellCheckTimesSameFreqInterThresholdDescInfo;
	private String cellIdealDisReferenceCellNumDescInfo;
	private String gsm900CellFreqNumDescInfo;
	private String gsm1800CellFreqNumDescInfo;
	private String gsm900CellIdealCapacityDescInfo;
	private String gsm1800CellIdealCapacityDescInfo;
	private String dlCoverMinimumSignalStrengthThresholdDescInfo;
	private String ulCoverMinimumSignalStrengthThresholdDescInfo;
	private String interFactorMostDistantDescInfo;
	private String interFactorSameAndAdjFreqMinimumThresholdDescInfo;
	
	public String getSameFreqInterThreshold() {
		return sameFreqInterThreshold;
	}
	public void setSameFreqInterThreshold(String sameFreqInterThreshold) {
		this.sameFreqInterThreshold = sameFreqInterThreshold;
	}
	public String getOverShootingIdealDisMultiple() {
		return overShootingIdealDisMultiple;
	}
	public void setOverShootingIdealDisMultiple(String overShootingIdealDisMultiple) {
		this.overShootingIdealDisMultiple = overShootingIdealDisMultiple;
	}
	public String getBetweenCellIdealDisMultiple() {
		return betweenCellIdealDisMultiple;
	}
	public void setBetweenCellIdealDisMultiple(String betweenCellIdealDisMultiple) {
		this.betweenCellIdealDisMultiple = betweenCellIdealDisMultiple;
	}
	public String getCellCheckTimesIdealDisMultiple() {
		return cellCheckTimesIdealDisMultiple;
	}
	public void setCellCheckTimesIdealDisMultiple(
			String cellCheckTimesIdealDisMultiple) {
		this.cellCheckTimesIdealDisMultiple = cellCheckTimesIdealDisMultiple;
	}
	public String getCellCheckTimesSameFreqInterThreshold() {
		return cellCheckTimesSameFreqInterThreshold;
	}
	public void setCellCheckTimesSameFreqInterThreshold(
			String cellCheckTimesSameFreqInterThreshold) {
		this.cellCheckTimesSameFreqInterThreshold = cellCheckTimesSameFreqInterThreshold;
	}
	public String getCellIdealDisReferenceCellNum() {
		return cellIdealDisReferenceCellNum;
	}
	public void setCellIdealDisReferenceCellNum(String cellIdealDisReferenceCellNum) {
		this.cellIdealDisReferenceCellNum = cellIdealDisReferenceCellNum;
	}
	public String getGsm900CellFreqNum() {
		return gsm900CellFreqNum;
	}
	public void setGsm900CellFreqNum(String gsm900CellFreqNum) {
		this.gsm900CellFreqNum = gsm900CellFreqNum;
	}
	public String getGsm1800CellFreqNum() {
		return gsm1800CellFreqNum;
	}
	public void setGsm1800CellFreqNum(String gsm1800CellFreqNum) {
		this.gsm1800CellFreqNum = gsm1800CellFreqNum;
	}
	public String getGsm900CellIdealCapacity() {
		return gsm900CellIdealCapacity;
	}
	public void setGsm900CellIdealCapacity(String gsm900CellIdealCapacity) {
		this.gsm900CellIdealCapacity = gsm900CellIdealCapacity;
	}
	public String getGsm1800CellIdealCapacity() {
		return gsm1800CellIdealCapacity;
	}
	public void setGsm1800CellIdealCapacity(String gsm1800CellIdealCapacity) {
		this.gsm1800CellIdealCapacity = gsm1800CellIdealCapacity;
	}
	public String getDlCoverMinimumSignalStrengthThreshold() {
		return dlCoverMinimumSignalStrengthThreshold;
	}
	public void setDlCoverMinimumSignalStrengthThreshold(
			String dlCoverMinimumSignalStrengthThreshold) {
		this.dlCoverMinimumSignalStrengthThreshold = dlCoverMinimumSignalStrengthThreshold;
	}
	public String getUlCoverMinimumSignalStrengthThreshold() {
		return ulCoverMinimumSignalStrengthThreshold;
	}
	public void setUlCoverMinimumSignalStrengthThreshold(
			String ulCoverMinimumSignalStrengthThreshold) {
		this.ulCoverMinimumSignalStrengthThreshold = ulCoverMinimumSignalStrengthThreshold;
	}
	public String getInterFactorMostDistant() {
		return interFactorMostDistant;
	}
	public void setInterFactorMostDistant(String interFactorMostDistant) {
		this.interFactorMostDistant = interFactorMostDistant;
	}
	public String getInterFactorSameAndAdjFreqMinimumThreshold() {
		return interFactorSameAndAdjFreqMinimumThreshold;
	}
	public void setInterFactorSameAndAdjFreqMinimumThreshold(
			String interFactorSameAndAdjFreqMinimumThreshold) {
		this.interFactorSameAndAdjFreqMinimumThreshold = interFactorSameAndAdjFreqMinimumThreshold;
	}

	public String getSameFreqInterThresholdDescInfo() {
		return sameFreqInterThresholdDescInfo;
	}
	public void setSameFreqInterThresholdDescInfo(
			String sameFreqInterThresholdDescInfo) {
		this.sameFreqInterThresholdDescInfo = sameFreqInterThresholdDescInfo;
	}
	public String getOverShootingIdealDisMultipleDescInfo() {
		return overShootingIdealDisMultipleDescInfo;
	}
	public void setOverShootingIdealDisMultipleDescInfo(
			String overShootingIdealDisMultipleDescInfo) {
		this.overShootingIdealDisMultipleDescInfo = overShootingIdealDisMultipleDescInfo;
	}
	public String getBetweenCellIdealDisMultipleDescInfo() {
		return betweenCellIdealDisMultipleDescInfo;
	}
	public void setBetweenCellIdealDisMultipleDescInfo(
			String betweenCellIdealDisMultipleDescInfo) {
		this.betweenCellIdealDisMultipleDescInfo = betweenCellIdealDisMultipleDescInfo;
	}
	public String getCellCheckTimesIdealDisMultipleDescInfo() {
		return cellCheckTimesIdealDisMultipleDescInfo;
	}
	public void setCellCheckTimesIdealDisMultipleDescInfo(
			String cellCheckTimesIdealDisMultipleDescInfo) {
		this.cellCheckTimesIdealDisMultipleDescInfo = cellCheckTimesIdealDisMultipleDescInfo;
	}
	public String getCellCheckTimesSameFreqInterThresholdDescInfo() {
		return cellCheckTimesSameFreqInterThresholdDescInfo;
	}
	public void setCellCheckTimesSameFreqInterThresholdDescInfo(
			String cellCheckTimesSameFreqInterThresholdDescInfo) {
		this.cellCheckTimesSameFreqInterThresholdDescInfo = cellCheckTimesSameFreqInterThresholdDescInfo;
	}
	public String getCellIdealDisReferenceCellNumDescInfo() {
		return cellIdealDisReferenceCellNumDescInfo;
	}
	public void setCellIdealDisReferenceCellNumDescInfo(
			String cellIdealDisReferenceCellNumDescInfo) {
		this.cellIdealDisReferenceCellNumDescInfo = cellIdealDisReferenceCellNumDescInfo;
	}
	public String getGsm900CellFreqNumDescInfo() {
		return gsm900CellFreqNumDescInfo;
	}
	public void setGsm900CellFreqNumDescInfo(String gsm900CellFreqNumDescInfo) {
		this.gsm900CellFreqNumDescInfo = gsm900CellFreqNumDescInfo;
	}
	public String getGsm1800CellFreqNumDescInfo() {
		return gsm1800CellFreqNumDescInfo;
	}
	public void setGsm1800CellFreqNumDescInfo(String gsm1800CellFreqNumDescInfo) {
		this.gsm1800CellFreqNumDescInfo = gsm1800CellFreqNumDescInfo;
	}
	public String getGsm900CellIdealCapacityDescInfo() {
		return gsm900CellIdealCapacityDescInfo;
	}
	public void setGsm900CellIdealCapacityDescInfo(
			String gsm900CellIdealCapacityDescInfo) {
		this.gsm900CellIdealCapacityDescInfo = gsm900CellIdealCapacityDescInfo;
	}
	public String getGsm1800CellIdealCapacityDescInfo() {
		return gsm1800CellIdealCapacityDescInfo;
	}
	public void setGsm1800CellIdealCapacityDescInfo(
			String gsm1800CellIdealCapacityDescInfo) {
		this.gsm1800CellIdealCapacityDescInfo = gsm1800CellIdealCapacityDescInfo;
	}
	public String getDlCoverMinimumSignalStrengthThresholdDescInfo() {
		return dlCoverMinimumSignalStrengthThresholdDescInfo;
	}
	public void setDlCoverMinimumSignalStrengthThresholdDescInfo(
			String dlCoverMinimumSignalStrengthThresholdDescInfo) {
		this.dlCoverMinimumSignalStrengthThresholdDescInfo = dlCoverMinimumSignalStrengthThresholdDescInfo;
	}
	public String getUlCoverMinimumSignalStrengthThresholdDescInfo() {
		return ulCoverMinimumSignalStrengthThresholdDescInfo;
	}
	public void setUlCoverMinimumSignalStrengthThresholdDescInfo(
			String ulCoverMinimumSignalStrengthThresholdDescInfo) {
		this.ulCoverMinimumSignalStrengthThresholdDescInfo = ulCoverMinimumSignalStrengthThresholdDescInfo;
	}
	public String getInterFactorMostDistantDescInfo() {
		return interFactorMostDistantDescInfo;
	}
	public void setInterFactorMostDistantDescInfo(
			String interFactorMostDistantDescInfo) {
		this.interFactorMostDistantDescInfo = interFactorMostDistantDescInfo;
	}
	public String getInterFactorSameAndAdjFreqMinimumThresholdDescInfo() {
		return interFactorSameAndAdjFreqMinimumThresholdDescInfo;
	}
	public void setInterFactorSameAndAdjFreqMinimumThresholdDescInfo(
			String interFactorSameAndAdjFreqMinimumThresholdDescInfo) {
		this.interFactorSameAndAdjFreqMinimumThresholdDescInfo = interFactorSameAndAdjFreqMinimumThresholdDescInfo;
	}
	@Override
	public String toString() {
		return "Threshold [sameFreqInterThreshold=" + sameFreqInterThreshold
				+ ", overShootingIdealDisMultiple="
				+ overShootingIdealDisMultiple
				+ ", betweenCellIdealDisMultiple="
				+ betweenCellIdealDisMultiple
				+ ", cellCheckTimesIdealDisMultiple="
				+ cellCheckTimesIdealDisMultiple
				+ ", cellCheckTimesSameFreqInterThreshold="
				+ cellCheckTimesSameFreqInterThreshold
				+ ", cellIdealDisReferenceCellNum="
				+ cellIdealDisReferenceCellNum + ", gsm900CellFreqNum="
				+ gsm900CellFreqNum + ", gsm1800CellFreqNum="
				+ gsm1800CellFreqNum + ", gsm900CellIdealCapacity="
				+ gsm900CellIdealCapacity + ", gsm1800CellIdealCapacity="
				+ gsm1800CellIdealCapacity
				+ ", dlCoverMinimumSignalStrengthThreshold="
				+ dlCoverMinimumSignalStrengthThreshold
				+ ", ulCoverMinimumSignalStrengthThreshold="
				+ ulCoverMinimumSignalStrengthThreshold
				+ ", interFactorMostDistant=" + interFactorMostDistant
				+ ", interFactorSameAndAdjFreqMinimumThreshold="
				+ interFactorSameAndAdjFreqMinimumThreshold + "]";
	}
	
	
	public static Map<String,String> getMap(Threshold obj){
		
		
		Map<String,String> result=new HashMap<String,String>();
		Class<?> classz=Threshold.class;
		Field[] fields=classz.getDeclaredFields();
		Method method=null;
		String name="",val="";
		String methodName="";
		for(Field f:fields){
			name=f.getName();
			if(name.endsWith("Info")){
				continue;
			}
			methodName="get"+name.substring(0,1).toUpperCase()+name.substring(1);
			try {
				method=classz.getDeclaredMethod(methodName, new Class[]{});
				Object objv=method.invoke(obj,new Object[]{});
				val=(String)objv;
				result.put(name.toUpperCase(), val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		Threshold th=new Threshold();
		Map<String,String> map=th.getMap(th);
		for(String k : map.keySet()) {
			System.out.println(map.get(k));
		}
		System.out.println(map);
	}*/
	
}
