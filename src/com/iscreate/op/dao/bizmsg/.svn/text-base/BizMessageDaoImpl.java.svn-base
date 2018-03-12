package com.iscreate.op.dao.bizmsg;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.bizmsg.BizmsgMessageType;

public class BizMessageDaoImpl implements BizMessageDao{

	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存消息
	 * @param bizMsg
	 */
	public void saveMessage(BizMessage bizMsg){
		this.hibernateTemplate.save(bizMsg);
	}
	
	/**
	 * 修改消息
	 * @param bizMsg
	 */
	public void updateMessage(BizMessage bizMsg){
		this.hibernateTemplate.update(bizMsg);
	}
	
	/**
	 * 删除消息
	 * @param bizMsg
	 */
	public void deleteMessage(BizMessage bizMsg){
		this.hibernateTemplate.delete(bizMsg);
	}
	
	/**
	 * 根据接收人获取消息
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getMessageByReceivePerson(String receivePerson){
		String hql = "from BizMessage b where b.receivePerson='"+receivePerson+"' order by b.sendTime desc";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByPage(final String receivePerson,final int indexPage,final int pageSize){
		return hibernateTemplate.executeFind(new HibernateCallback<List<BizMessage>>(){

			public List<BizMessage> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from BizMessage b where b.receivePerson='"+receivePerson+"' and b.state<>1 and b.message_type_id<>4 order by b.sendTime desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(indexPage);
		        query.setMaxResults(pageSize);
		        return query.list();
			}
		});
	}
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)[终端]
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByPageForMobile(final String receivePerson,final int indexPage,final int pageSize){
		return hibernateTemplate.executeFind(new HibernateCallback<List<BizMessage>>(){

			public List<BizMessage> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from BizMessage b where b.receivePerson='"+receivePerson+"' and b.state<>1 and b.message_type_id<>4 and b.linkForMobile NOT LIKE '%TOID=&%' order by b.sendTime desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(indexPage);
		        query.setMaxResults(pageSize);
		        return query.list();
			}
		});
	}
	
	/**
	 * 根据接收人、状态和类型分页获取信息[终端]
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getMessageToPageByReceivePersonAndStateAndType(final String receivePerson,final int state,final long typeId,final int indexPage,final int pageSize){
		return hibernateTemplate.executeFind(new HibernateCallback<List<BizMessage>>(){

			public List<BizMessage> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String stateHql = "b.state="+state;
				if(state==0){
					stateHql = "b.state<>1";
				}
				String hql = "from BizMessage b where "+stateHql+" and b.receivePerson='"+receivePerson+"' and b.message_type_id="+typeId+" and b.linkForMobile NOT LIKE '%TOID=&%' order by b.sendTime desc";
				Query query = session.createQuery(hql);
				query.setFirstResult(indexPage);
		        query.setMaxResults(pageSize);
		        return query.list();
			}
		});
	}

	/**
	 * 根据接收人获取未读信息（包括响铃）
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePerson(String receivePerson){
		String hql = "from BizMessage b where b.state<>1 and b.receivePerson='"+receivePerson+"' order by b.sendTime desc";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据接收人获取未读信息（包括响铃）[终端]
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePersonForMobile(final String receivePerson){
		return hibernateTemplate.executeFind(new HibernateCallback<List<BizMessage>>(){

			public List<BizMessage> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from BizMessage b where b.state<>1 and b.receivePerson='"+receivePerson+"' and b.linkForMobile NOT LIKE '%TOID=&%'";
				Query query = session.createQuery(hql);
				return query.list();
			}
		});
	}
	
	/**
	 * 根据接收人获取信息
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePerson(int state,String receivePerson){
		String hql = "from BizMessage b where b.state="+state+" and b.receivePerson='"+receivePerson+"'";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据接收人获取信息 [终端]
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePersonForMobile(final int state,final String receivePerson){
		return hibernateTemplate.executeFind(new HibernateCallback<List<BizMessage>>(){

			public List<BizMessage> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from BizMessage b where b.state="+state+" and b.receivePerson='"+receivePerson+"' and b.linkForMobile NOT LIKE '%TOID=&%'";
				Query query = session.createQuery(hql);
				return query.list();
			}
		});
	}
	
	/**
	 * 根据接收人和类型获取消息
	 * @param state
	 * @param receivePerson
	 * @param msgTypeId
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePersonAndMsgType(int state,String receivePerson,long msgTypeId){
		String hql = "from BizMessage b where b.state="+state+" and b.receivePerson='"+receivePerson+"' and b.message_type_id="+msgTypeId + " order by b.sendTime desc";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据id获取消息
	 * @param id
	 * @return
	 */
	public BizMessage getMessageById(String id){
		String hql = "from BizMessage b where b.id='"+id+"'";
		List<BizMessage> list = (List<BizMessage>)hibernateTemplate.find(hql);
		BizMessage bizMessage = null;
		if(list!=null && list.size()>0){
			bizMessage = list.get(0);
		}
		return bizMessage;
	}
	
	/**
	 * 根据接收人和单号获取消息
	 * @param receivePerson
	 * @param orderId
	 * @return
	 */
	public List<BizMessage> getMessageByReceivePersonAndOrderId(String receivePerson,String orderId){
		String hql = "from BizMessage b where b.title='"+orderId+"' and b.receivePerson='"+receivePerson+"' and b.state<>1";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	/**
	 * 根据标题获取未读和响铃信息
	 * @param title
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingMessageByTitle(String title){
		String hql = "from BizMessage b where b.title='"+title+"' and b.state<>1";
		return (List<BizMessage>)hibernateTemplate.find(hql);
	}
	
	//消息类型======================================================================================
	
	/**
	 * 根据类型获取消息类型对象
	 * @param typeKey
	 * @return
	 */
	public BizmsgMessageType getMessageTypeByTypeKey(String typeKey){
		String hql = "from BizmsgMessageType b where b.typeKey='"+typeKey+"' and b.status=1";
		List<BizmsgMessageType> list = hibernateTemplate.find(hql);
		BizmsgMessageType type = null;
		if(list!=null && list.size()>0){
			type = list.get(0);
		}
		return type; 
	}
	
	/**
	 * 根据id获取消息类型对象
	 * @param typeKey
	 * @return
	 */
	public BizmsgMessageType getMessageTypeById(long id){
		String hql = "from BizmsgMessageType b where b.id="+id+" and b.status=1";
		List<BizmsgMessageType> list = hibernateTemplate.find(hql);
		BizmsgMessageType type = null;
		if(list!=null && list.size()>0){
			type = list.get(0);
		}
		return type;
	}

	/**
	 * 获取所有的消息类型
	 * @return
	 */
	public List<BizmsgMessageType> getAllMessageType(){
		String hql = "from BizmsgMessageType b where b.status=1 order by b._order asc";
		return hibernateTemplate.find(hql);
	}
	/**
	 * 
	 * @description: 根据消息状态 功能类型获取消息列表
	 * @author：yuan.yw
	 * @param userId
	 * @param messageStateId
	 * 
	 * @param functionType
	 * @param messageType
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 12, 2013 10:30:10 AM
	 */
	public List<Map<String,Object>> getMessageListByStatusAndFunctionTypeAndMsgType(String userId, String messageStateId,String functionType,String messageType){
		if(userId==null || "".equals(userId) || messageStateId==null || "".equals(messageStateId)){
			return null;
		}
		String sql = " select m.id,m.FUNCTIONTYPE,m.STATE,m.MESSAGE_TYPE_ID,m.TOID,m.WOID,m.RECEIVEPERSON,m.CONTENT,m.TYPE,m.TITLE,t.typekey,t.typename,to_char(m.sendtime,'yyyy-MM-dd HH24:mi:ss') sendtime from msg_message m left join msg_message_type t on m.message_type_id=t.id  where m.receivePerson='"+userId+"' and m.state in ("+messageStateId+") ";
		if(messageType!=null  && !"".equals(messageType)){
			sql = " select m.id,m.FUNCTIONTYPE,m.STATE,m.MESSAGE_TYPE_ID,m.TOID,m.WOID,m.RECEIVEPERSON,m.CONTENT,m.TYPE,m.TITLE,t.typekey,t.typename,to_char(m.sendtime,'yyyy-MM-dd HH24:mi:ss') sendtime from msg_message m , msg_message_type t where m.message_type_id=t.id and t.typekey='"+messageType+"' and m.receivePerson='"+userId+"' and m.state in ("+messageStateId+") ";
		}
		if(functionType!=null && !"".equals(functionType)){
			sql +=" and  m.functionType in ("+functionType+")";
		}
		sql += " order by m.sendtime desc ";
		return (List<Map<String,Object>>)this.executeSqlForObject(sql);
	}
	/**
	 * 
	 * @description: 查询
	 * @author：yuan.yw
	 * @param sqlString
	 * @return     
	 * @return List     
	 * @date：Jul 12, 2013 10:42:18 AM
	 */
	public List executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list;
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
