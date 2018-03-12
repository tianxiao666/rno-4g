package com.iscreate.plat.workflow;

import java.util.*;

import com.iscreate.plat.workflow.constraint.impl.*;
import com.iscreate.plat.workflow.dataconfig.impl.*;
import com.iscreate.plat.workflow.datainput.impl.*;
import com.iscreate.plat.workflow.privileges.impl.*;
import com.iscreate.plat.workflow.processor.impl.*;
import com.iscreate.plat.workflow.serviceaccess.impl.*;




public class FlowBeanFactory {
	public static HashMap<String, FlowBean> flowBeanMap = new HashMap();


    /****
     * 
     * 通过bean工厂获取bean对象
     * 
     * @param beanType   bean的类型
     * @return
     */  
	public synchronized static FlowBean getFlowBean(BeanType beanType) {
		if (flowBeanMap.get(beanType.toString()) != null) {
			return flowBeanMap.get(beanType.toString());
		} else {
			if (beanType.toString().equals(Constant.BeanType.Bean_Type_DataInputService)) {
				// 非单列
				return new DataInputServiceBeanImpl();
			} else if (beanType.toString()
					.equals(Constant.BeanType.Bean_Type_FlowProcessor)) {
				FlowBean bean = FlowProcessorBeanImpl.getInstance();
				flowBeanMap.put(Constant.BeanType.Bean_Type_FlowProcessor, bean);
				return bean;
			} else if (beanType.toString().equals(Constant.BeanType.Bean_Type_Service)) {
				FlowBean bean = new ServiceBeanImpl();
				flowBeanMap.put(Constant.BeanType.Bean_Type_Service, bean);
				return  bean;
			} else if (beanType.toString().equals(Constant.BeanType.Bean_Type_RelAnalyzer)) {
				FlowBean bean = new  RelAnalyzerBeanImpl();
				flowBeanMap.put(Constant.BeanType.Bean_Type_RelAnalyzer, bean);
				return  bean;
			} else if (beanType.toString().equals(Constant.BeanType.Bean_Type_Manager)) {
				FlowBean bean = new  ManagerBeanImpl();
				flowBeanMap.put(Constant.BeanType.Bean_Type_Manager, bean);
				return  bean;
			} else if (beanType.toString()
					.equals(Constant.BeanType.Bean_Type_ExtDataConfig)) {
				FlowBean bean = new  ExtDataConfigBeanImpl();
				flowBeanMap.put(Constant.BeanType.Bean_Type_ExtDataConfig, bean);
				return  bean;

			} else {
                //throw new Exception("没有所要的Bean对象");
			}
		}
		return null;
	}

}
