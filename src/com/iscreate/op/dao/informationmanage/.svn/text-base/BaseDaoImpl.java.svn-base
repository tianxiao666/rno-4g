package com.iscreate.op.dao.informationmanage;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class BaseDaoImpl<T> extends DBUtil implements BaseDao<T> {

	/*********** 依赖注入 ***********/
	protected HibernateTemplate hibernateTemplate;
	public Class<T> clazz;
	
	public BaseDaoImpl() {
		
	}
	
	
	/***********************  hibernate begin ***************************/
	
	
	/**
	 * 根据参数,查询泛型类的单个数据信息
	 * @param param_map - 参数集合
	 * @return 数据信息
	 */
	public T findSingleByParam ( Map param_map ) {
		T t = null;
		List<T> list = findByParam(param_map);
		if ( list != null && list.size() > 0 ) {
			t = list.get(0);
		}
		return t;
	}
	
	/**
	 * 根据参数,查询是否存在
	 * @param param_map
	 * @return 数据信息集合
	 */
	public boolean exists ( Map param_map ) {
		List<T> list = findByParam(param_map);
		boolean flag = list != null && list.size() > 0 ;
		return flag;
	}
	
	
	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam ( Map param_map , final boolean isLazy ) {
		final String whereString = DBUtil.getWhereString(param_map, null);
		List<T> list = hibernateTemplate.execute( new HibernateCallback<List<T>>(){
			public List<T> doInHibernate( Session session ) throws HibernateException, SQLException {
				String sqlString = "FROM " + clazz.getSimpleName() + whereString;
				Query query = session.createQuery(sqlString);
				List<T> find = query.list();
				for (int i = 0; !isLazy && i < find.size(); i++) {
					strikeLazy(find.get(i));
				}
				return find;
			}
		});
		return list;
	}
	
	/**
	 * 根据参数,查询泛型类的数据信息集合
	 * @param param_map - 参数集合
	 * @return 数据信息集合
	 */
	public List<T> findByParam ( Map param_map ) {
		List<T> list = findByParam( param_map , true );
		return list;
	}
	
	/**
	 * 根据id编号和类对象,查询出数据信息
	 * @param clazz - 类对象
	 * @param id - 编号
	 * @return 数据信息
	 */
	public <E> E get( Class<E> clazz , Serializable id ){
		E e = hibernateTemplate.get(clazz, id);
		return e;
	}
	
	/**
	 * 根据id编号,查询泛型类的数据信息
	 * @param id - 编号
	 * @return 数据信息
	 */
	public T get( Serializable id ){
		T t = hibernateTemplate.get(clazz, id);
		return t;
	}
	
	/**
	 * 根据id编号,查询泛型类的数据信息
	 * @param id - 编号
	 * @return 数据信息
	 */
	public T get( final Serializable id , final boolean isLazy ){
		T t = hibernateTemplate.execute( new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				T object = (T) session.get(clazz, id);
				if ( !isLazy ) {
					strikeLazy(object);
				}
				return object;
			}
		});
		return t;
	}
	
	/**
	 * 根据参数实例,查询跟该实例条件相符的数据信息
	 * @param <E> 泛型类
	 * @param e 参数实例
	 * @return 数据信息集合
	 */
	public <E> List<E> findByExample ( E e ) {
		List<E> list = hibernateTemplate.findByExample(e);
		return list;
	}
	
	/**
	 * 根据泛型类名,查询该类所有数据信息集合
	 * @return 数据信息集合
	 */
	public List<T> findAllList () {
		List<T> list = null;
		String hql = "FROM " + clazz.getSimpleName();
		list = hibernateTemplate.find(hql);
		return list;
	}
	
	/**
	 * 保存对象
	 * @param <T> 泛型
	 * @param t - 保存的对象
	 * @return 保存对象的id
	 */
	public <T> Long txinsert(T t) {
		Long id = null;
		id = (Long)hibernateTemplate.save(t);
		return id;
	}

	/**
	 * 保存对象
	 * @param <T> 泛型
	 * @param t - 保存的对象
	 * @return 保存对象的id
	 */
	public <T> Long txsave( Map map ) {
		String intoString = "";
		String valueString = "";
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = map.get(key);
			intoString += "," + key;
			if ( value instanceof String ) {
				valueString += ", '" + value+"' ";
			} else{
				valueString += ", " + value+"";
			}
		}
		intoString = intoString.replaceFirst(",", "");
		valueString = valueString.replaceFirst(",", "");
		final String sql = "INSERT INTO " + getTableName(getClazz()) + " (" + intoString + ") VALUES ( " + valueString + " )";
		Long execute = hibernateTemplate.execute(new HibernateCallback<Long>(){
			public Long doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int executeUpdate = query.executeUpdate();
				return 1l;
			}
		} );
		return execute;
	}
	
	/**
	 * 保存对象
	 * @param <T> 泛型
	 * @param t - 保存的对象
	 * @return 保存对象的id
	 */
	public <T> Long txsave( Map map , Class clazz ) {
		String intoString = "";
		String valueString = "";
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = map.get(key);
			intoString += "," + key;
			if ( value instanceof String ) {
				valueString += ", '" + value+"' ";
			} else {
				valueString += ", " + value+" ";
			}
			
		}
		intoString = intoString.replaceFirst(",", "");
		valueString = valueString.replaceFirst(",", "");
		final String sql = "INSERT INTO " + getTableName(clazz) + " (" + intoString + ") VALUES ( " + valueString + " )";
		Long execute = hibernateTemplate.execute(new HibernateCallback<Long>(){
			public Long doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int executeUpdate = query.executeUpdate();
				return 1l;
			}
		} );
		return execute;
	}
	
	/**
	 * 更新对象
	 * @param <T> 泛型
	 * @param t - 更新的对象
	 * @return 	0 : 更新不成功
	 * 			1 : 更新成功  
	 */
	public <T> int txupdate(T t) {
		int num = 0 ;
		try {
			hibernateTemplate.update(t);
			num = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	public <T> int txdelete( Map param_map ) {
		final String whereString = DBUtil.getWhereString(param_map, null);
		Integer execute = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate( Session session ) throws HibernateException, SQLException {
				String sql = "DELETE FROM " + getClazz().getSimpleName() + whereString;
				Query query = session.createQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return execute;
	}
	public <T> int txdelete( Map param_map , final Class clazz) {
		final String whereString = DBUtil.getWhereString(param_map, null);
		Integer execute = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate( Session session ) throws HibernateException, SQLException {
				String sql = "DELETE FROM " + clazz.getSimpleName() + whereString;
				Query query = session.createQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return execute;
	}
	
	public <T> int txupdate( Map<String,String> param_map , Map<String,String> set_map ) {
		final String whereString = DBUtil.getWhereString(param_map, null);
		final String updateString = DBUtil.getUpdateSetString(set_map, null);
		Integer execute = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate( Session session ) throws HibernateException, SQLException {
				String sql = "UPDATE " + getClazz().getSimpleName() + " SET " + updateString + whereString;
				Query query = session.createQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return execute;
	}
	
	
	public int txupdateMapObj( Map param_map , Map set_map ) {
		final String whereString = DBUtil.getWhereString(param_map, null);
		final String updateString = DBUtil.getUpdateSetString(set_map, null);
		Integer execute = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate( Session session ) throws HibernateException, SQLException {
				String sql = "UPDATE " + getClazz().getSimpleName() + " SET " + updateString + whereString;
				Query query = session.createQuery(sql);
				int num = query.executeUpdate();
				return num;
			}
		});
		return execute;
	}
	
	/**
	 * 删除对象
	 * @param <T> 泛型
	 * @param t - 删除的对象
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public <T> int txremove(T t) {
		int num = 0 ;
		try {
			hibernateTemplate.delete(t);
			num = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 根据id,删除数据
	 * @param id - id
	 * @return 	0 ： 删除不成功
	 * 			1 ： 删除成功
	 */
	public int txremove( Long id ){
		int num = 0;
		try {
			T t = clazz.newInstance();
			String setterMethodName = "setId";
			Method[] methods = clazz.getMethods();
			Method method = null;
			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();
				if ( methodName.equals(setterMethodName) ){
					method = methods[i];
				}
			}
			if ( method == null ) {
				return 0;
			}
			method.invoke(t, id);
			num = txremove(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	/***********************  hibernate end ***************************/
	
	/***********************  sql begin ***************************/
	
	public List<Map<String,String>> executeFindList ( String sql ) {
		final String sqlString = sql;
		List<Map<String,String>> list = hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,String>>>() {
			public List<Map<String, String>> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,String>> find = query.list();
				for (int i = 0; i < find.size(); i++) {
					Map map = find.get(i);
					for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
						String key = it.next();
						Object value = map.get(key);
						if ( value instanceof Date || value instanceof Timestamp ) {
							String[] strings = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd HH","yyyy-MM-dd","yyyy-MM"};
							for (int j = 0; j < strings.length; j++) {
								SimpleDateFormat sdf = new SimpleDateFormat(strings[j]);
								try {
									String format = sdf.format(value);
									map.put(key, format);
									break;
								} catch (Exception e) {
									continue;
								}
							}
						} else if ( !(value instanceof String) ) {
							map.put(key, value+"");
						}
					}
				}
				return find;
			}
		});
		return list;
	}
	
	
	public List<Map<String,String>> executeFindList ( String sql , final Object... objs ) {
		final String sqlString = sql;
		List<Map<String,String>> list = hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,String>>>() {
			public List<Map<String, String>> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				if( objs != null ) {
					for (int i = 0; i < objs.length; i++) {
						Object object = objs[i];
						if ( object instanceof String ) {
							query.setString( i , object+"");
						} else {
							query.setParameter( i , object );
						}
					}
				}
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,String>> find = query.list();
				for (int i = 0; i < find.size(); i++) {
					Map map = find.get(i);
					for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
						String key = it.next();
						Object value = map.get(key);
						if ( value instanceof Date || value instanceof Timestamp ) {
							String[] strings = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd HH","yyyy-MM-dd","yyyy-MM"};
							for (int j = 0; j < strings.length; j++) {
								SimpleDateFormat sdf = new SimpleDateFormat(strings[j]);
								try {
									String format = sdf.format(value);
									map.put(key, format);
									break;
								} catch (Exception e) {
									continue;
								}
							}
						} else if ( !(value instanceof String) ) {
							map.put(key, value+"");
						}
					}
				}
				return find;
			}
		});
		return list;
	}
	
	public List<Map<String,Object>> executeFindListMapObject ( String sql ) {
		final String sqlString = sql;
		List<Map<String,Object>> list = hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
	
	public List<Map<String,String>> executeFindForClassList ( String sql , final Class... clazzes ) {
		final String sqlString = sql;
		List<Map<String,String>> list = hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,String>>>() {
			public List<Map<String, String>> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < clazzes.length; i++) {
					Class clazz = clazzes[i];
					query.addEntity(clazz);
				}
				List<Object> find = query.list();
				List<Map<String,String>> ll = new ArrayList<Map<String,String>>();
				for (int i = 0; i < find.size(); i++) {
					Object[] objects = (Object[]) find.get(i);
					for (int j = 0; j < objects.length; j++) {
						Object obj = objects[j];
						Map<String, String> map = null;
						try {
							map = ObjectUtil.object2MapString(obj, false);
						} catch (SecurityException e1) {
							e1.printStackTrace();
						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (NoSuchMethodException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						}
						for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
							String key = it.next();
							Object value = map.get(key);
							if ( value instanceof Date || value instanceof Timestamp ) {
								String[] strings = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd HH","yyyy-MM-dd","yyyy-MM"};
								for (int k = 0; k < strings.length; k++) {
									SimpleDateFormat sdf = new SimpleDateFormat(strings[k]);
									try {
										String format = sdf.format(value);
										map.put(key, format);
										break;
									} catch (Exception e) {
										continue;
									}
								}
							} else if ( !(value instanceof String) ) {
								map.put(key, value+"");
							}
						}
					}
				}
				return ll;
			}
		});
		return list;
	}
	
	public List<Map<String,Object>> executeFindListForClassMapObject ( String sql , final Class... clazzes ) {
		final String sqlString = sql;
		List<Map<String,Object>> list = hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				for (int i = 0; i < clazzes.length; i++) {
					Class clazz = clazzes[i];
					query.addEntity(clazz);
				}
				List find = query.list();
				List<Map<String,Object>> ll = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < find.size(); i++) {
					if ( clazzes.length > 1 ) {
						Object[] objects = (Object[]) find.get(i);
						for (int j = 0; j < objects.length; j++) {
							Object obj = objects[j];
							Map<String, Object> map = null;
							try {
								map = ObjectUtil.object2Map(obj, false);
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
							ll.add(map);
						}
					} else {
						Object obj =  find.get(i);
						Map<String, Object> map = null;
						try {
							map = ObjectUtil.object2Map(obj, false);
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				return ll;
			}
		});
		return list;
	}
	
	
	public Boolean executeSql ( final String sql ) {
		Boolean execute = hibernateTemplate.execute( new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int executeUpdate = query.executeUpdate();
				boolean flag = executeUpdate > 0 ;
				return flag;
			}
		});
		return execute;
	} 
	
	/***********************  sql end ***************************/
	
	
	/******* 工具 ******/
	
	/**  
     * 通过反射,获得指定类的父类的泛型参数的实际类型. 如BuyerServiceBean extends DaoSupport<Buyer>  
     *  
     * @param clazz clazz 需要反射的类,该类必须继承范型父类
     * @param index 泛型参数所在索引,从0开始.  
     * @return 范型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回<code>Object.class</code>
     */  
    @SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index) {    
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
	
	
	
	
	
	
	
	/***************** getter setter ********************/
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public Class<T> getClazz() {
		if ( this.clazz == null ) {
			this.clazz = getSuperClassGenricType(getClass(),0);
		}
		return clazz;
	}
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	
}
