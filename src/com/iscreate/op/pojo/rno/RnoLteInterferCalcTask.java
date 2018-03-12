package com.iscreate.op.pojo.rno;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.vo.Threshold;

public class RnoLteInterferCalcTask {
	
	private TaskInfo taskInfo=null;
	private Threshold thresholdDefault=null;//默认参数值
	private Threshold threshold=null;
	private HoInfo hoInfo=null;
	private MrInfo mrInfo=null;

	private List<Map<String,Object>> eriInfo;
	private List<Map<String,Object>> zteInfo;
	private List<RnoThreshold> rnoThresholds;
//	private List<List<RnoThreshold>> groupThresholds;
	private Map<Long,List<RnoThreshold>> groupThresholds;
	
	
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
	public void setZteInfo(List<Map<String, Object>> zteInfo) {
		this.zteInfo = zteInfo;
	}
	public List<Map<String, Object>> getZteInfo() {
		if(zteInfo==null){
			//如果为null，加锁初始化
			synchronized(this){
				if(zteInfo==null){
					zteInfo=new ArrayList<Map<String,Object>>();
				}
			}
		}
		//如果不为null，直接返回instance
		return zteInfo;
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
	public void setHoInfo(HoInfo hoInfo) {
		this.hoInfo = hoInfo;
	}
	public HoInfo getHoInfo() {
			if (hoInfo==null) {
				//如果为null，加锁初始化
				synchronized (this) {
					if (hoInfo==null) {
						hoInfo=new HoInfo();
					}
				}
			}
			//如果不为null，直接返回instance
			return hoInfo;
		
	}
	public void setMrInfo(MrInfo mrInfo) {
		this.mrInfo = mrInfo;
	}
	public MrInfo getMrInfo() {
		if (mrInfo==null) {
			//如果为null，加锁初始化
			synchronized (this) {
				if (mrInfo==null) {
					mrInfo=new MrInfo();
				}
			}
		}
		//如果不为null，直接返回instance
		return mrInfo;
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
		private String planType;
		private String converType;//收敛方式类型
		private String relaNumerType;//关联度分子
		private String lteCells;

		private TaskInfo(){
			
		}
		
		
		public String getLteCells() {
			return lteCells;
		}


		public void setLteCells(String lteCells) {
			this.lteCells = lteCells;
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
		
		public String getPlanType() {
			return planType;
		}

		public void setPlanType(String planType) {
			this.planType = planType;
		}

		public String getConverType() {
			return converType;
		}

		public void setConverType(String converType) {
			this.converType = converType;
		}
		public String getRelaNumerType() {
			return relaNumerType;
		}

		public void setRelaNumerType(String relaNumerType) {
			this.relaNumerType = relaNumerType;
		}
	}
	/**
	 * LTE信息内部类 HO
	 * @author chao.xj
	 *
	 */
	public static class HoInfo {
		private int recordNum;
		private HoInfo(){
			
		}
		public int getRecordNum() {
			return recordNum;
		}
		public void setRecordNum(int recordNum) {
			this.recordNum = recordNum;
		}
	}
	/**
	 * mr信息内部类
	 * @author chao.xj
	 *
	 */
	public static class MrInfo {
		private int recordNum;

		private MrInfo(){
			
		}
		public int getRecordNum() {
			return recordNum;
		}
		public void setRecordNum(int recordNum) {
			this.recordNum = recordNum;
		}
	
	}
	
	
}
