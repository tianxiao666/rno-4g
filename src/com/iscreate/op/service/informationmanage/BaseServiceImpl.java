package com.iscreate.op.service.informationmanage;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.dao.informationmanage.BaseDao;


@SuppressWarnings({"unchecked","hiding"})
public class BaseServiceImpl<T> implements BaseService<T> {
	
	
	
	/************ 依赖注入 **************/
	protected BaseDao<T> baseDao;
	/************ 属性 **************/

	protected Class<T> clazz;
	
	public BaseServiceImpl() {
		clazz = getSuperClassGenricType(getClass(),0);
	}
	
	/******************** hibernate begin **********************/
	
	
	public <T> int txupdate( Map<String,String> param_map , Map<String,String> set_map ){
		int txupdate = baseDao.txupdate(param_map, set_map);
		return txupdate;
	}
	
	/**
	 * 根据参数,查询泛型类的单个数据信息
	 * @param param_map - 参数集合
	 * @return 数据信息
	 */
	public T findSingleByParam ( Map param_map ){
		T t = baseDao.findSingleByParam(param_map);
		return t;
	}
	
	public <T> int txdelete( Map<String,String> param_map ){
		int txOpera = baseDao.txdelete(param_map);
		return txOpera;
	}
	
	/**
	 * 根据参数,查询是否存在
	 * @param param_map
	 * @return 数据信息集合
	 */
	public boolean exists ( Map param_map ) {
		boolean exists = baseDao.exists(param_map);
		return exists;
	}
	
	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam ( Map param_map , final boolean isLazy ) {
		List<T> list = baseDao.findByParam(param_map,isLazy);
		return list;
	}
	
	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam ( Map param_map ) {
		List<T> list = baseDao.findByParam(param_map);
		return list;
	}
	
	/**
	 * 根据id编号和类对象,查询出数据信息
	 * @param clazz - 类对象
	 * @param id - 编号
	 * @return 数据信息
	 */
	public <E> E get( Class<E> clazz , Serializable id ){
		E e = baseDao.get(clazz,id);
		return e;
	}
	
	/**
	 * 根据id编号,查询泛型类的数据信息
	 * @param id - 编号
	 * @return 数据信息
	 */
	public T get( Serializable id ){
		T t = baseDao.get(id);
		return t;
	}
	
	
	public T get( final Serializable id , final boolean isLazy ){
		T t = baseDao.get(id, isLazy);
		return t;
	}
	
	
	/**
	 * 根据参数实例,查询跟该实例条件相符的数据信息
	 * @param <E> 泛型类
	 * @param e 参数实例
	 * @return 数据信息集合
	 */
	public <E> List<E> findByExample ( E e ) {
		List<E> list = baseDao.findByExample(e);
		return list;
	}
	
	/**
	 * 根据泛型类名,查询该类所有数据信息集合
	 * @return 数据信息集合
	 */
	public List<T> findAllList () {
		List<T> list = baseDao.findAllList();
		return list;
	}
	
	/**
	 * 保存对象
	 * @param <T> 泛型
	 * @param t - 保存的对象
	 * @return 保存对象的id
	 */
	public <T> Long txinsert(T t) {
		Long id = baseDao.txinsert(t);
		return id;
	}

	/**
	 * 更新对象
	 * @param <T> 泛型
	 * @param t - 更新的对象
	 * @return 	0 : 更新不成功
	 * 			1 : 更新成功  
	 */
	public <T> int txupdate(T t) {
		int num = baseDao.txupdate(t);
		return num;
	}

	/**
	 * 删除对象
	 * @param <T> 泛型
	 * @param t - 删除的对象
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public <T> int txremove(T t) {
		int num = baseDao.txremove(t);
		return num;
	}
	
	/**
	 * 根据id,删除数据
	 * @param id - id
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public int txremove( Long id ){
		int num = baseDao.txremove(id);
		return num;
	}
	
	
	public <T> Long txsave(Map map) {
		Long txsave = baseDao.txsave(map);
		return txsave;
	}
	/********************* hibernate end ************************/
	
	/********************* sql begin ************************/
	
	public List<Map<String,String>> executeFindList ( String sql ) {
		List<Map<String, String>> list = baseDao.executeFindList(sql);
		return list;
	}
	
	public <T> Long txsave( Map map , Class clazz ){
		Long txsave = baseDao.txsave(map, clazz);
		return txsave;
	}
	
	public <T> int txdelete( Map param_map , final Class clazz) {
		int txdelete = baseDao.txdelete(param_map , clazz);
		return txdelete;
	}
	
	public List<Map<String,Object>> executeFindListMapObject ( String sql ) {
		List<Map<String, Object>> executeFindListMapObject = baseDao.executeFindListMapObject(sql);
		return executeFindListMapObject;
	}
	
	/********************* sql end ************************/
	
	/**  
     * 通过反射,获得指定类的父类的泛型参数的实际类型. 如BuyerServiceBean extends DaoSupport<Buyer>  
     * @param clazz clazz 需要反射的类,该类必须继承范型父类
     * @param index 泛型参数所在索引,从0开始.  
     * @return 范型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回<code>Object.class</code>
     */  
	public Class getSuperClassGenricType(Class clazz, int index) {    
        Type genType = clazz.getGenericSuperclass();//得到泛型父类  
        //如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class   
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;   
        }  
        //返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends DaoSupport<Buyer,Contact>就返回Buyer和Contact类型   
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();                   
        if (index >= params.length || index < 0) { 
        	 throw new RuntimeException("你输入的索引"+ (index<0 ? "不能小于0" : "超出了参数的总数"));
        }      
        if (!(params[index] instanceof Class)) {
            return Object.class;   
        }   
        return (Class) params[index];
    }
	
	
	public Boolean executeSql ( final String sql ) {
		Boolean executeSql = baseDao.executeSql(sql);
		return executeSql;
	}
	
	
	/*************** getter setter ****************/
	public BaseDao<T> getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
		baseDao.setClazz(clazz);
	}
	public Class<T> getClazz() {
		return clazz;
	}
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	
	
}
