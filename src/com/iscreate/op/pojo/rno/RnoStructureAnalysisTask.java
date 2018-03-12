package com.iscreate.op.pojo.rno;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.vo.Threshold;

public class RnoStructureAnalysisTask {
	
	private TaskInfo taskInfo=null;
	private Threshold thresholdDefault=null;//默认参数值
	private Threshold threshold=null;
	private NcsInfo ncsInfo=null;
	private MrrInfo mrrInfo=null;
	private List<Map<String,Object>> eriInfo;
	private List<Map<String,Object>> hwInfo;
	private List<RnoThreshold> rnoThresholds;
//	private List<List<RnoThreshold>> groupThresholds;
	private Map<Long,List<RnoThreshold>> groupThresholds;
	
/*	public List<List<RnoThreshold>> getGroupThresholds() {
		return groupThresholds;
	}
	public void setGroupThresholds(List<List<RnoThreshold>> groupThresholds) {
		this.groupThresholds = groupThresholds;
	}*/
	
	public Map<Long, List<RnoThreshold>> getGroupThresholds() {
		return groupThresholds;
	}
	public void setGroupThresholds(Map<Long, List<RnoThreshold>> groupThresholds) {
		this.groupThresholds = groupThresholds;
	}
	public List<RnoThreshold> getRnoThresholds() {
		return rnoThresholds;
	}
	public void setRnoThresholds(List<RnoThreshold> rnoThresholds) {
		this.rnoThresholds = rnoThresholds;
	}
	public List<Map<String, Object>> getEriInfo() {
		if(eriInfo==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(eriInfo==null){
					eriInfo=new ArrayList<Map<String,Object>>();
				}
			}
		}
		//如果不为null，直接返回instance
		return eriInfo;
	}
	public void setEriInfo(List<Map<String, Object>> eriInfo) {
		this.eriInfo = eriInfo;
	}
	public List<Map<String, Object>> getHwInfo() {
		if(hwInfo==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(hwInfo==null){
					hwInfo=new ArrayList<Map<String,Object>>();
				}
			}
		}
		//如果不为null，直接返回instance
		return hwInfo;
	}
	public void setHwInfo(List<Map<String, Object>> hwInfo) {
		this.hwInfo = hwInfo;
	}
	public Threshold getThresholdDefault() {
		if(thresholdDefault==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(thresholdDefault==null){
					thresholdDefault=new Threshold();
				}
			}
		}
		//如果不为null，直接返回instance
		return thresholdDefault;
	}
	public void setThresholdDefault(Threshold thresholdDefault) {
		this.thresholdDefault = thresholdDefault;
	}
	public Threshold getThreshold() {
		if(threshold==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(threshold==null){
					threshold=new Threshold();
				}
			}
		}
		//如果不为null，直接返回instance
		return threshold;
	}
	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}
	public TaskInfo getTaskInfo() {
		if(taskInfo==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(taskInfo==null){
					taskInfo=new TaskInfo();
				}
			}
		}
		//如果不为null，直接返回instance
		return taskInfo;
	}
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	public NcsInfo getNcsInfo() {
			if (ncsInfo==null) {
				//如果为null，加锁初始化
				synchronized (this) {
					if (ncsInfo==null) {
						ncsInfo=new NcsInfo();
					}
				}
			}
			//如果不为null，直接返回instance
			return ncsInfo;
		
	}
	public void setNcsInfo(NcsInfo ncsInfo) {
		this.ncsInfo = ncsInfo;
	}
	public MrrInfo getMrrInfo() {
		if (mrrInfo==null) {
			//如果为null，加锁初始化
			synchronized (this) {
				if (mrrInfo==null) {
					mrrInfo=new MrrInfo();
				}
			}
		}
		//如果不为null，直接返回instance
		return mrrInfo;
	}
	public void setMrrInfo(MrrInfo mrrInfo) {
		this.mrrInfo = mrrInfo;
	}
	
	/**
	 * 任务消息内部类
	 * @author chao.xj
	 *
	 */
	public static class TaskInfo {
		private String taskName;
		private String taskDesc;
		private long provinceId;
		private long cityId;
		private String cityName;
		private String provinceName;
		private String startTime;
		private String endTime;
		private Map<String, Boolean> busDataType;//业务数据类型
		private Map<String, Boolean> calProcedure;//计算过程
		
		private TaskInfo(){
			
		}
		
		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getProvinceName() {
			return provinceName;
		}

		public void setProvinceName(String provinceName) {
			this.provinceName = provinceName;
		}

		public long getProvinceId() {
			return provinceId;
		}

		public void setProvinceId(long provinceId) {
			this.provinceId = provinceId;
		}

		public long getCityId() {
			return cityId;
		}

		public void setCityId(long cityId) {
			this.cityId = cityId;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getTaskDesc() {
			return taskDesc;
		}
		public void setTaskDesc(String taskDesc) {
			this.taskDesc = taskDesc;
		}
		public Map<String, Boolean> getBusDataType() {
			return busDataType;
		}

		public void setBusDataType(Map<String, Boolean> busDataType) {
			this.busDataType = busDataType;
		}

		public Map<String, Boolean> getCalProcedure() {
			return calProcedure;
		}

		public void setCalProcedure(Map<String, Boolean> calProcedure) {
			this.calProcedure = calProcedure;
		}
	}
	/**
	 * NCS信息内部类
	 * @author chao.xj
	 *
	 */
	public static class NcsInfo {
		private String ncsIds;
		private int ncsFileNum;
		private String ncsAreaCoverage;
		private String ncsTimeSpan;
		
		private String ncsLevel;
		private long ncsAreaId;
		private long ncsCityId;
		
		
		private NcsInfo(){
			
		}
		
		public String getNcsLevel() {
			return ncsLevel;
		}
		public void setNcsLevel(String ncsLevel) {
			this.ncsLevel = ncsLevel;
		}
		public long getNcsAreaId() {
			return ncsAreaId;
		}
		public void setNcsAreaId(long ncsAreaId) {
			this.ncsAreaId = ncsAreaId;
		}
		public long getNcsCityId() {
			return ncsCityId;
		}
		public void setNcsCityId(long ncsCityId) {
			this.ncsCityId = ncsCityId;
		}
		public String getNcsIds() {
			return ncsIds;
		}
		public void setNcsIds(String ncsIds) {
			this.ncsIds = ncsIds;
		}
		public int getNcsFileNum() {
			return ncsFileNum;
		}
		public void setNcsFileNum(int ncsFileNum) {
			this.ncsFileNum = ncsFileNum;
		}
		public String getNcsAreaCoverage() {
			return ncsAreaCoverage;
		}
		public void setNcsAreaCoverage(String ncsAreaCoverage) {
			this.ncsAreaCoverage = ncsAreaCoverage;
		}
		public String getNcsTimeSpan() {
			return ncsTimeSpan;
		}
		public void setNcsTimeSpan(String ncsTimeSpan) {
			this.ncsTimeSpan = ncsTimeSpan;
		}
	
	}
	/**
	 * mrr信息内部类
	 * @author chao.xj
	 *
	 */
	public static class MrrInfo {
		private String mrrIds;
		private int mrrFileNum;
		private String mrrAreaCoverage;
		private String mrrTimeSpan;
		
		private String mrrLevel;
		private long mrrAreaId;
		private long mrrCityId;
		
		
		private MrrInfo(){
			
		}
		public String getMrrLevel() {
			return mrrLevel;
		}
		public void setMrrLevel(String mrrLevel) {
			this.mrrLevel = mrrLevel;
		}
		public long getMrrAreaId() {
			return mrrAreaId;
		}
		public void setMrrAreaId(long mrrAreaId) {
			this.mrrAreaId = mrrAreaId;
		}
		public long getMrrCityId() {
			return mrrCityId;
		}
		public void setMrrCityId(long mrrCityId) {
			this.mrrCityId = mrrCityId;
		}
		public String getMrrIds() {
			return mrrIds;
		}
		public void setMrrIds(String mrrIds) {
			this.mrrIds = mrrIds;
		}
		public int getMrrFileNum() {
			return mrrFileNum;
		}
		public void setMrrFileNum(int mrrFileNum) {
			this.mrrFileNum = mrrFileNum;
		}
		public String getMrrAreaCoverage() {
			return mrrAreaCoverage;
		}
		public void setMrrAreaCoverage(String mrrAreaCoverage) {
			this.mrrAreaCoverage = mrrAreaCoverage;
		}
		public String getMrrTimeSpan() {
			return mrrTimeSpan;
		}
		public void setMrrTimeSpan(String mrrTimeSpan) {
			this.mrrTimeSpan = mrrTimeSpan;
		}
	
	}
	
	
}
