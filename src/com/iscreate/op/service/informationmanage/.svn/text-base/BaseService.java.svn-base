package com.iscreate.op.service.informationmanage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T> {


	/**
	 * 根据参数,查询泛型类的单个数据信息
	 * @param param_map - 参数集合
	 * @return 数据信息
	 */
	public T findSingleByParam(Map param_map);

	/**
	 * 根据参数,查询是否存在
	 * @param param_map
	 * @return 数据信息集合
	 */
	public boolean exists(Map param_map);

	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam(Map param_map, final boolean isLazy);

	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam(Map param_map);

	/**
	 * 根据id编号和类对象,查询出数据信息
	 * @param clazz - 类对象
	 * @param id - 编号
	 * @return 数据信息
	 */
	public <E> E get(Class<E> clazz, Serializable id);

	/**
	 * 根据id编号,查询泛型类的数据信息
	 * @param id - 编号
	 * @return 数据信息
	 */
	public T get(Serializable id);

	/**
	 * 根据id编号,查询泛型类的数据信息
	 * @param id - 编号
	 * @return 数据信息
	 */
	public T get(final Serializable id, final boolean isLazy);

	/**
	 * 根据参数实例,查询跟该实例条件相符的数据信息
	 * @param <E> 泛型类
	 * @param e 参数实例
	 * @return 数据信息集合
	 */
	public <E> List<E> findByExample(E e);

	/**
	 * 根据泛型类名,查询该类所有数据信息集合
	 * @return 数据信息集合
	 */
	public List<T> findAllList();

	/**
	 * 保存对象
	 * @param <T> 泛型
	 * @param t - 保存的对象
	 * @return 保存对象的id
	 */
	public <T> Long txinsert(T t);

	/**
	 * 更新对象
	 * @param <T> 泛型
	 * @param t - 更新的对象
	 * @return 	0 : 更新不成功
	 * 			1 : 更新成功  
	 */
	public <T> int txupdate(T t);

	/**
	 * 删除对象
	 * @param <T> 泛型
	 * @param t - 删除的对象
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public <T> int txremove(T t);

	/**
	 * 根据id,删除数据
	 * @param id - id
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public int txremove(Long id);

	public <T> int txdelete(Map<String,String> param_map);

	public <T> int txupdate(Map<String,String> param_map, Map<String,String> set_map);

	/********************* sql begin ************************/
	public List<Map<String,String>> executeFindList(String sql);

	public List<Map<String,Object>> executeFindListMapObject(String sql);

	public <T> Long txsave( Map map );

	public <T> Long txsave(Map map, Class clazz);

	public <T> int txdelete(Map param_map, final Class clazz);

	public abstract Boolean executeSql(final String sql);
	
}
