package com.iscreate.op.dao.common;

import java.io.Serializable;
import java.util.List;

public interface CommonDao {
	/**
	  * 保存对象
	  * @param object
	  * @return
	  */
	 public Serializable saveObject(Object object);
	 
	 /**
	  * 更新对象
	  * @param object
	  */
	 public void updateObject(Object object);
	 
	 /**
	  * 根据对象唯一标识获取对象
	  * @param <T>
	  * @param classz
	  * @param id
	  * @return
	  */
	 public <T> T getObjectById(Class<T> classz ,long id);
	 
	 /**
	  * 删除对象
	  * @param entity
	  */
	 public void deleteObject(String entityName,long id);
	 
	 /**
	  * 删除对象
	  * @param entity
	  */
	 public void deleteObject(Object entity);
	 
	 /**
	  * 根据类，字段名和字段值获取对象列表
	  * @param <T>
	  * @param classz 类
	  * @param properName 表字段名
	  * @param value 字段值
	  * @return
	  */
	 public <T> List<T> geObjectListByPropertyAndValue(final Class<T> classz,final String properName,final Object value);
	 
	 /**
	  * 根据类，字段名和字段值获取对象列表
	  * @param <T>
	  * @param classz 类
	  * @param properName 表字段名
	  * @param value 字段值
	  * @return
	  */
	 public <T> T geUniqueObjectByPropertyAndValue(final Class<T> classz,final String properName,final Object value);
}
