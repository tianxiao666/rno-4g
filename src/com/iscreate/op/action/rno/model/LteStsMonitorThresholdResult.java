package com.iscreate.op.action.rno.model;

public class LteStsMonitorThresholdResult {
	String cellName;
	String startTime;
	String endTime;
	//通用阈值
	boolean erab_DropRate_Flag; //E-RAB掉线率
	boolean erab_EstabSuccRate_Flag; //E-RAB建立成功率
	boolean rrc_ConnEstabSuccRate_Flag; //RRC连接建立成功率
	boolean zeroFlow_ZeroTeltra_Flag; //零流量零话务
	boolean wireConnRate_Flag; //无线接通率
	//VIP和普通小区有区别
	boolean cellBar_rrc_Conn_FailTimes_Flag; //CellBar_RRC连接失败次数
	boolean cellBar_DropTimes_Flag; //CellBar_掉线次数
	boolean rrc_ConnFailTimes_Flag; //RRC连接失败次数
	boolean dropTimes_Flag; //掉线次数
	boolean connFailTimes_Flag; //接通失败次数
	boolean switchSuccRate_Flag; //切换成功率
	boolean switchFailTimes_Flag; //切换失败次数
	boolean wireDropRate_Flag; //无线掉线率
	
	
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
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
	public boolean isErab_DropRate_Flag() {
		return erab_DropRate_Flag;
	}
	public void setErab_DropRate_Flag(boolean erab_DropRate_Flag) {
		this.erab_DropRate_Flag = erab_DropRate_Flag;
	}
	public boolean isErab_EstabSuccRate_Flag() {
		return erab_EstabSuccRate_Flag;
	}
	public void setErab_EstabSuccRate_Flag(boolean erab_EstabSuccRate_Flag) {
		this.erab_EstabSuccRate_Flag = erab_EstabSuccRate_Flag;
	}
	public boolean isRrc_ConnEstabSuccRate_Flag() {
		return rrc_ConnEstabSuccRate_Flag;
	}
	public void setRrc_ConnEstabSuccRate_Flag(boolean rrc_ConnEstabSuccRate_Flag) {
		this.rrc_ConnEstabSuccRate_Flag = rrc_ConnEstabSuccRate_Flag;
	}
	public boolean isZeroFlow_ZeroTeltra_Flag() {
		return zeroFlow_ZeroTeltra_Flag;
	}
	public void setZeroFlow_ZeroTeltra_Flag(boolean zeroFlow_ZeroTeltra_Flag) {
		this.zeroFlow_ZeroTeltra_Flag = zeroFlow_ZeroTeltra_Flag;
	}
	public boolean isWireConnRate_Flag() {
		return wireConnRate_Flag;
	}
	public void setWireConnRate_Flag(boolean wireConnRate_Flag) {
		this.wireConnRate_Flag = wireConnRate_Flag;
	}
	public boolean isCellBar_rrc_Conn_FailTimes_Flag() {
		return cellBar_rrc_Conn_FailTimes_Flag;
	}
	public void setCellBar_rrc_Conn_FailTimes_Flag(
			boolean cellBar_rrc_Conn_FailTimes_Flag) {
		this.cellBar_rrc_Conn_FailTimes_Flag = cellBar_rrc_Conn_FailTimes_Flag;
	}
	public boolean isCellBar_DropTimes_Flag() {
		return cellBar_DropTimes_Flag;
	}
	public void setCellBar_DropTimes_Flag(boolean cellBar_DropTimes_Flag) {
		this.cellBar_DropTimes_Flag = cellBar_DropTimes_Flag;
	}
	public boolean isRrc_ConnFailTimes_Flag() {
		return rrc_ConnFailTimes_Flag;
	}
	public void setRrc_ConnFailTimes_Flag(boolean rrc_ConnFailTimes_Flag) {
		this.rrc_ConnFailTimes_Flag = rrc_ConnFailTimes_Flag;
	}
	public boolean isDropTimes_Flag() {
		return dropTimes_Flag;
	}
	public void setDropTimes_Flag(boolean dropTimes_Flag) {
		this.dropTimes_Flag = dropTimes_Flag;
	}
	public boolean isConnFailTimes_Flag() {
		return connFailTimes_Flag;
	}
	public void setConnFailTimes_Flag(boolean connFailTimes_Flag) {
		this.connFailTimes_Flag = connFailTimes_Flag;
	}
	public boolean isSwitchSuccRate_Flag() {
		return switchSuccRate_Flag;
	}
	public void setSwitchSuccRate_Flag(boolean switchSuccRate_Flag) {
		this.switchSuccRate_Flag = switchSuccRate_Flag;
	}
	public boolean isSwitchFailTimes_Flag() {
		return switchFailTimes_Flag;
	}
	public void setSwitchFailTimes_Flag(boolean switchFailTimes_Flag) {
		this.switchFailTimes_Flag = switchFailTimes_Flag;
	}
	public boolean isWireDropRate_Flag() {
		return wireDropRate_Flag;
	}
	public void setWireDropRate_Flag(boolean wireDropRate_Flag) {
		this.wireDropRate_Flag = wireDropRate_Flag;
	}
	}
