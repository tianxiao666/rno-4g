package com.iscreate.op.service.routineinspection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.routineinspection.RoutineinspectionQuestionDao;
import com.iscreate.op.dao.routineinspection.RoutineinspectionTaskorderQuestionDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorderQuestion;

public class RoutineinspectionTaskorderQuestionServiceImpl implements RoutineinspectionTaskorderQuestionService{
	private RoutineinspectionTaskorderQuestionDao routineinspectionTaskorderQuestionDao;
	private  static final  Log log = LogFactory.getLog(RoutineinspectionTaskorderQuestionServiceImpl.class);
	
	/**
	 * 保存巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public long saveRoutineinspectionTaskorderQuestion(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion){
		log.info("进入 saveRoutineinspectionTaskorderQuestion");
		log.info("saveRoutineinspectionTaskorderQuestion Service层 保存巡检问题。");
		if(routineinspectionTaskorderQuestion!=null){
			long qid = routineinspectionTaskorderQuestionDao.saveRoutineinspectionTaskorderQuestionDao(routineinspectionTaskorderQuestion);
			log.info("退出 saveRoutineinspectionTaskorderQuestion");
			return qid;
		}else{
			log.error("参数 巡检问题类 routineinspectionTaskorderQuestion 为空");
			log.error("执行 saveRoutineinspectionTaskorderQuestion 失败");
			return -1;
		}
		
	}
	
	/**
	 * 更新巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public void updateRoutineinspectionTaskorderQuestion(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion){
		log.info("进入 updateRoutineinspectionTaskorderQuestion");
		log.info("updateRoutineinspectionTaskorderQuestion Service层 更新巡检问题。");
		if(routineinspectionTaskorderQuestion!=null){
			routineinspectionTaskorderQuestionDao.updateRoutineinspectionTaskorderQuestionDao(routineinspectionTaskorderQuestion);
			log.info("退出 updateRoutineinspectionTaskorderQuestion");
		}else{
			log.error("参数 巡检问题类 routineinspectionTaskorderQuestion 为空");
			log.error("执行 updateRoutineinspectionTaskorderQuestion 失败");
		}
		
	}
	
	/**
	 * 根据Id获取巡检问题
	 * @param id
	 * @return
	 */
	public Map getRoutineinspectionTaskorderQuestion(String id){
		log.info("进入 getRoutineinspectionTaskorderQuestion");
		log.info("getRoutineinspectionTaskorderQuestion Service层 获取问题。");
		List<Map> list = routineinspectionTaskorderQuestionDao.getRoutineinspectionTaskorderQuestionDao(id);
		if(list!=null&&!list.isEmpty()){
			log.info("退出 getRoutineinspectionTaskorderQuestion");
			return list.get(0);
		}else{
			log.error("巡检问题为空");
			log.info("退出 updateRoutineinspectionTaskorderQuestion");
			return null;
		}
	}
	


	public RoutineinspectionTaskorderQuestionDao getRoutineinspectionTaskorderQuestionDao() {
		return routineinspectionTaskorderQuestionDao;
	}

	public void setRoutineinspectionTaskorderQuestionDao(
			RoutineinspectionTaskorderQuestionDao routineinspectionTaskorderQuestionDao) {
		this.routineinspectionTaskorderQuestionDao = routineinspectionTaskorderQuestionDao;
	}
	
	

}
