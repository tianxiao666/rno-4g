package com.iscreate.op.service.rno.task;

public enum TaskStatus {

	NOT_START("未开始"),//未开始
	DOING("运行中"),//正在运行
	FAIL("失败"),//失败
	ABORT("终止"),//终止
	FINISH("成功完成"),//成功完成
	CANCEL("取消");//取消
	
	private String code;
	public String getCode(){
		return this.code;
	}
	private TaskStatus(String code){
		this.code=code;
	}
}
