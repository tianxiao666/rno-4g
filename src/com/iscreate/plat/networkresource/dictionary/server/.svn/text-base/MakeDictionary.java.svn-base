package com.iscreate.plat.networkresource.dictionary.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;

public class MakeDictionary {

	static Dictionary dic;

	/**
	 * @param args
	 * @throws EntryOperationException
	 */
	public static void main(String[] args) throws EntryOperationException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		dic = ctx.getBean(Dictionary.class);
//		Entity entry0 = new Entity();
//		dic.addEntry("operationService", entry0);
//		Entity entry1 = new Entity();
//		dic.addEntry("UrgencyRepair_ServerWorkOrder,operationService", entry1);
//		urgencyRepair_ServerWorkOrderDictionary();
//		addEntry("UrgencyRepair_Snag,operationService","snagReason","好吧");
		breakdown_reasonDictionary();
	}

	private static void addEntry(String parentdn, String type, String value) {
		long start = System.currentTimeMillis();
		String dn = type + "-" + value + "," + parentdn;
		Entity entry = new Entity();
		entry.setValue("type", type);
		entry.setValue("name", value);
		entry.setValue("value", value);
		try {
			dic.addEntry(dn, entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		//System.out.println(end - start);
	}
	
	private static void breakdown_reasonDictionary() throws EntryOperationException{
		//UrgencyRepair_BreakdownReason, breakdownClassify
		//UrgencyRepair_BreakdownReason, breakdownReason
		//UrgencyRepair_BreakdownReason, breakdownMeasureClassify
		//UrgencyRepair_BreakdownReason, breakdownMeasure
		//UrgencyRepair_BreakdownReason, delayReasonClassify
		//UrgencyRepair_BreakdownReason, delayReason
		dic.removeEntry("UrgencyRepair_BreakdownReason,operationService");
		
		dic.addEntry("UrgencyRepair_BreakdownReason,operationService", new Entity());
		
		addEntry("UrgencyRepair_BreakdownReason,operationService","breakdownClassify", "故障分类1");
		addEntry("UrgencyRepair_BreakdownReason,operationService","breakdownClassify", "故障分类2");

		addEntry("breakdownClassify-故障分类1,UrgencyRepair_BreakdownReason,operationService","breakdownReason", "故障分类1原因A");
		addEntry("breakdownClassify-故障分类1,UrgencyRepair_BreakdownReason,operationService","breakdownReason", "故障分类1原因B");
		
		addEntry("breakdownClassify-故障分类2,UrgencyRepair_BreakdownReason,operationService","breakdownReason", "故障分类2原因X");
		addEntry("breakdownClassify-故障分类2,UrgencyRepair_BreakdownReason,operationService","breakdownReason", "故障分类2原因Y");
		
		addEntry("breakdownReason-故障分类1原因A,breakdownClassify-故障分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因A的解决方法");
		addEntry("breakdownReason-故障分类1原因B,breakdownClassify-故障分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因B的解决方法");
		addEntry("breakdownReason-故障分类1原因B,breakdownClassify-故障分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因B的解决方法XXX");
		
		addEntry("breakdownReason-故障分类2原因X,breakdownClassify-故障分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因X的解决方法");
		addEntry("breakdownReason-故障分类2原因Y,breakdownClassify-故障分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因Y的解决方法");
		addEntry("breakdownReason-故障分类2原因Y,breakdownClassify-故障分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因Y的解决方法ZZZ");
	
		addEntry("UrgencyRepair_BreakdownReason,operationService","breakdownMeasureClassify", "解决方法分类1");
		addEntry("UrgencyRepair_BreakdownReason,operationService","breakdownMeasureClassify", "解决方法分类2");
		
		addEntry("breakdownMeasureClassify-解决方法分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因A的解决方法");
		addEntry("breakdownMeasureClassify-解决方法分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因B的解决方法");
		addEntry("breakdownMeasureClassify-解决方法分类1,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类1原因B的解决方法XXX");
		addEntry("breakdownMeasureClassify-解决方法分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因X的解决方法");
		addEntry("breakdownMeasureClassify-解决方法分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因Y的解决方法");
		addEntry("breakdownMeasureClassify-解决方法分类2,UrgencyRepair_BreakdownReason,operationService","breakdownMeasure", "故障分类2原因Y的解决方法ZZZ");
	
		addEntry("UrgencyRepair_BreakdownReason,operationService","delayReasonClassify", "延迟原因分类1");
		addEntry("UrgencyRepair_BreakdownReason,operationService","delayReasonClassify", "延迟原因分类2");
		addEntry("UrgencyRepair_BreakdownReason,operationService","delayReasonClassify", "延迟原因分类3");

		addEntry("delayReasonClassify-延迟原因分类1,UrgencyRepair_BreakdownReason,operationService","delayReason", "没时间");
		addEntry("delayReasonClassify-延迟原因分类2,UrgencyRepair_BreakdownReason,operationService","delayReason", "没钱");
		addEntry("delayReasonClassify-延迟原因分类3,UrgencyRepair_BreakdownReason,operationService","delayReason", "没空");
		
	}
	
	private static void urgencyRepair_ServerWorkOrderDictionary(){
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "安全告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "操作维护");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "处理器模块");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "动力电源");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "动力告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "动力环境");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "对外接口");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "话务处理");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "环境");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "计费处理");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "计费告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "计费系统");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "连通性告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "设备");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "设备故障");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "设备环境");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "设备性能告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "输入输出外部设备");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "数据库系统");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "数据配置");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "通信");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "网管");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "网管故障");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "维护");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "系统告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "信令");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "信令模块");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "信令与IP");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "信源故障");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "业务处理");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "业务性能告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "硬件告警");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "中继与传输");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "主机系统");
		addEntry("UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalClass", "主设备故障");
		
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","安全日志告警");
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","安全扫描告警");
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","安全事件告警");
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","登录事件");
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网管连通性告警");
		addEntry("alarmLogicalClass-安全告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网管数据采集异常告警");
		addEntry("alarmLogicalClass-操作维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","操作异常");
		addEntry("alarmLogicalClass-操作维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","日志告警");
		addEntry("alarmLogicalClass-操作维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网管断连");
		addEntry("alarmLogicalClass-操作维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网元告警传送");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","进程故障");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","局数据问题");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内部通信故障");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内存资源的管理");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","区域处理器故障");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统重启动");
		addEntry("alarmLogicalClass-处理器模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","中央处理器故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","UPS故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","变压器故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","低压故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","低压配电故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","过压故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","熔丝故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","市电故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","油机故障");
		addEntry("alarmLogicalClass-动力电源,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","整流电源故障");
		addEntry("alarmLogicalClass-动力告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","BSC电源告警");
		addEntry("alarmLogicalClass-动力告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","BTS电源告警");
		addEntry("alarmLogicalClass-动力告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","电源告警");
		addEntry("alarmLogicalClass-动力告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","门禁告警");
		addEntry("alarmLogicalClass-动力环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","电源告警");
		addEntry("alarmLogicalClass-动力环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","风扇告警");
		addEntry("alarmLogicalClass-动力环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","温度告警");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","BOSS接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","OCS接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCP接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SMP接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","充值接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","短信前置机接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","短信网关接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","短信中心接口故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网络连接故障");
		addEntry("alarmLogicalClass-对外接口,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","信令模块接口故障");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","BSC退出服务");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","交换机处理能力");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","交换机退服");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","交换模块故障");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","容量告警");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","设备占用监视");
		addEntry("alarmLogicalClass-话务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","选组级故障");
		addEntry("alarmLogicalClass-环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","电源");
		addEntry("alarmLogicalClass-环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","外告");
		addEntry("alarmLogicalClass-计费处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","话单文件产生故障");
		addEntry("alarmLogicalClass-计费处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","话单文件存储异常");
		addEntry("alarmLogicalClass-计费告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","话单产生异常告警");
		addEntry("alarmLogicalClass-计费告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","话单处理异常告警");
		addEntry("alarmLogicalClass-计费告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","话单传送异常告警");
		addEntry("alarmLogicalClass-计费告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内容计费告警");
		addEntry("alarmLogicalClass-计费系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","计费设备故障");
		addEntry("alarmLogicalClass-计费系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","计费数据存储异常");
		addEntry("alarmLogicalClass-计费系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","计费文件产生故障");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","PING不可达");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","传输端口告警");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","链路状态异常");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","路由协议告警");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","同步时钟告警");
		addEntry("alarmLogicalClass-连通性告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务链路全阻");
		addEntry("alarmLogicalClass-设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","电源");
		addEntry("alarmLogicalClass-设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","软件");
		addEntry("alarmLogicalClass-设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","硬件");
		addEntry("alarmLogicalClass-设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","电源类故障");
		addEntry("alarmLogicalClass-设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","功率类故障");
		addEntry("alarmLogicalClass-设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","设备类故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","火警故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","空调故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","雷击故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","门禁故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","湿度故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","水浸故障");
		addEntry("alarmLogicalClass-设备环境,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","温度故障");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","CPU性能告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","丢包率告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","连接数不足告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","链路负荷告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","流量异常告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内存占用率告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","容量告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","硬盘占用率告警");
		addEntry("alarmLogicalClass-设备性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","拥塞告警");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","BSC外部告警");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","I/O设备告警");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","操作异常");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","存储设备告警");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","进程故障");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内部通信故障");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","外部告警");
		addEntry("alarmLogicalClass-输入输出外部设备,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","文件操作");
		addEntry("alarmLogicalClass-数据库系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","CDR管理");
		addEntry("alarmLogicalClass-数据库系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","HDR管理");
		addEntry("alarmLogicalClass-数据库系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","数据库操作");
		addEntry("alarmLogicalClass-数据库系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","数据库宕机");
		addEntry("alarmLogicalClass-数据库系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","数据库性能管理");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","局数据问题");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","配置备份告警");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","软件版本告警");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","数据库配置");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统数据库异常告警");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统数据配置异常");
		addEntry("alarmLogicalClass-数据配置,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务数据制作异常");
		addEntry("alarmLogicalClass-通信,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","倒换");
		addEntry("alarmLogicalClass-通信,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","端口");
		addEntry("alarmLogicalClass-通信,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","通道");
		addEntry("alarmLogicalClass-通信,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","性能");
		addEntry("alarmLogicalClass-网管,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","通信问题");
		addEntry("alarmLogicalClass-网管,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网管软件");
		addEntry("alarmLogicalClass-网管,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","网管硬件");
		addEntry("alarmLogicalClass-网管故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","联机故障");
		addEntry("alarmLogicalClass-维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","操作告警");
		addEntry("alarmLogicalClass-维护,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","数据配置");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","板卡操作告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","辅助系统告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","进程异常告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统切换告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统日志告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统提示告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","系统重起告警");
		addEntry("alarmLogicalClass-系统告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","智能拨测告警");
		addEntry("alarmLogicalClass-信令,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCCP层故障");
		addEntry("alarmLogicalClass-信令,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","链路故障");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCP接口故障");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","处理出错");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","单板故障");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","链路故障");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","目的信令点不可达");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","通讯故障");
		addEntry("alarmLogicalClass-信令模块,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","同步时钟故障");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","IP信令告警");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCCP层故障");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","链路故障");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","目的信令点不可达");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","设备占用监视");
		addEntry("alarmLogicalClass-信令与IP,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","中继电路故障");
		addEntry("alarmLogicalClass-信源故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","参数设置问题");
		addEntry("alarmLogicalClass-信源故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","天馈线故障");
		addEntry("alarmLogicalClass-信源故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","信源小区故障");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCP处理能力");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SCP退出服务");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SMP处理能力");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","SMP退出服务");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","设备占用监视");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","同步数据");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务处理提示");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务逻辑故障");
		addEntry("alarmLogicalClass-业务处理,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","重要进程故障");
		addEntry("alarmLogicalClass-业务性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","Attach附着成功率告警");
		addEntry("alarmLogicalClass-业务性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","PDP激活成功率告警");
		addEntry("alarmLogicalClass-业务性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","RAU路由切换成功率告警");
		addEntry("alarmLogicalClass-业务性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务层连接告警");
		addEntry("alarmLogicalClass-业务性能告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","业务量异常告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","CPU硬件告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","单板告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","功能扩展模块告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","控制面板告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","扩展槽硬件告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","路由引擎告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内存硬件告警");
		addEntry("alarmLogicalClass-硬件告警,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","硬盘硬件告警");
		addEntry("alarmLogicalClass-中继与传输,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","光纤故障");
		addEntry("alarmLogicalClass-中继与传输,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","路由故障");
		addEntry("alarmLogicalClass-中继与传输,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","同步时钟故障");
		addEntry("alarmLogicalClass-中继与传输,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","中继电路故障");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","存储设备管理");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","内存资源的管理");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","时间同步");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","双机管理");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","外围设备管理");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","中央处理器管理");
		addEntry("alarmLogicalClass-主机系统,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","主机宕机");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","基站传输");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","基站退服");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","天馈线系统故障");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","小区退服");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","载波故障");
		addEntry("alarmLogicalClass-主设备故障,UrgencyRepair_ServerWorkOrder,operationService","alarmLogicalSubClass","主处理单元故障");

	}
}
