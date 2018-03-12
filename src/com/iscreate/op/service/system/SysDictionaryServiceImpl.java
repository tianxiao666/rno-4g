package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.system.SysDictionaryDao;
import com.iscreate.op.pojo.system.SysDictionary;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.plat.tools.TreeListHelper;

public class SysDictionaryServiceImpl implements SysDictionaryService{
	private static Log log = LogFactory.getLog(SysDictionaryServiceImpl.class);
	private SysDictionaryDao sysDictionaryDao;
	
	/**
	 * 
	 * @description: 保存或更新字典
	 * @author：yuan.yw
	 * @param sysDictionary
	 * @return     
	 * @return Long     
	 * @date：Aug 21, 2013 10:24:21 AM
	 */
	public Long saveOrUpdateSysDictionary(SysDictionary sysDictionary){
		log.info("进入saveOrUpdateSysDictionary(SysDictionary sysDictionary)，保存或更新字典");
		Long dataTypeId =  sysDictionary.getDataTypeId();
		long orgUserId =  (Long)SessionService.getInstance().getValueByKey("org_user_id");
		if(dataTypeId!=null){//更新
			long parId=0;
			long newParId=0;
			SysDictionary dic = this.sysDictionaryDao.getSysDictionaryById(dataTypeId);
			if(dic!=null){
				parId = dic.getParentId();
			}
			SysDictionary parDic = this.sysDictionaryDao.getSysDictionaryById(sysDictionary.getParentId());
			if(parDic!=null){
				newParId = parDic.getDataTypeId();
			}
			if(parId!=newParId){
				dic.setParentId(newParId);//更新上级
				dic.setPath(parDic.getPath()+dataTypeId+"/");
			}
			if(sysDictionary.getCode()!=null){
				dic.setCode(sysDictionary.getCode());
			}
			if(sysDictionary.getName()!=null){
				dic.setName(sysDictionary.getName());
			}
			if(sysDictionary.getOrderNum()!=null){
				dic.setOrderNum(sysDictionary.getOrderNum());
			}
			if(sysDictionary.getDescription()!=null){
				dic.setDescription(sysDictionary.getDescription());
			}
			String dicIds="";
			if(sysDictionary.getStatus()!=null){
				String curStatus = dic.getStatus()+"";
				if(!curStatus.equals(sysDictionary.getStatus()+"")){//下级字典状态也要同步修改
					List<Map<String,Object>> rList = this.sysDictionaryDao.getSelfAndChildDictionaryListByIds(dataTypeId+"");
					if(rList!=null && !rList.isEmpty()){
						StringBuffer sf = new StringBuffer();
						for(Map<String,Object> mp:rList){
							sf.append(","+mp.get("DATA_TYPE_ID"));
						}
						dicIds = sf.substring(1);
					}
				}
			}
			dic.setModUserId(orgUserId);
			dic.setModTime(new Date());
			this.sysDictionaryDao.updateSysDictionary(dic);
			if(!"".equals(dicIds)){
				this.sysDictionaryDao.updateSysDictionaryStatusByIds(dicIds, sysDictionary.getStatus(), orgUserId);//更新自身及下级状态
			}
		}else{//新添
			sysDictionary.setCreateUserId(orgUserId);
			sysDictionary.setCreateTime(new Date());
			Serializable s = this.sysDictionaryDao.saveSysDictionary(sysDictionary);
			if(s!=null){
				dataTypeId = Long.valueOf(s+"");
				String path = dataTypeId+"/";
				SysDictionary dic = this.sysDictionaryDao.getSysDictionaryById(dataTypeId);
				if(dic.getParentId()!=null){//上级
					SysDictionary parDic = this.sysDictionaryDao.getSysDictionaryById(dic.getParentId());
					if(parDic!=null){
						path = parDic.getPath()+path;
					}
				}	
				dic.setPath(path);
				this.sysDictionaryDao.updateSysDictionary(dic);
			}
		}
		log.info("退出saveOrUpdateSysDictionary(SysDictionary sysDictionary)，返回dataTypeId="+dataTypeId);
		return dataTypeId;
	}
	/**
	 * 
	 * @description: 根据id或名称或编码验证数据字典是否存在
	 * @author：yuan.yw
	 * @param id
	 * @param parentId
	 * @param code
	 * @param name
	 * @return     
	 * @return boolean     
	 * @date：Aug 21, 2013 11:33:54 AM
	 */
	public boolean checkDictionaryIsExists(String id,String parentId,String code,String name){
		log.info("进入checkDictionaryIsExists(String id,String code,String name)，根据名称或编码验证数据字典是否存在");
		log.info("参数id="+id+",parentId="+parentId+",code="+code+",name="+name);
		boolean flag = false;
		List<Map<String,Object>> list=this.sysDictionaryDao.getDictionaryByNameOrCode(id,parentId,name, code);
		if(list!=null && !list.isEmpty()){
			flag=true;
		}
		log.info("退出checkDictionaryIsExists(String id,String code,String name)，返回结果"+flag);
		return flag;
	}
	/**
	 * 
	 * @description: 根据上级id获取数据字典list
	 * @author：yuan.yw
	 * @param dicId
	 * @param statusFlag
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 21, 2013 2:18:49 PM
	 */
	public List<Map<String,Object>> getDictionaryListByParId(long dicId,boolean statusFlag){
		log.info("进入getDictionaryListByParId(long dicId)，根据上级id获取数据字典list");
		log.info("参数dicId="+dicId);
		List<Map<String,Object>> list=this.sysDictionaryDao.getDictionaryByParId(dicId,statusFlag);
		log.info("退出getDictionaryListByParId(long dicId)，返回结果"+list);
		return list;
	}
	/**
	 * 
	 * @description:根据Id获取字典信息
	 * @author：yuan.yw
	 * @param dicId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 21, 2013 2:36:06 PM
	 */
	public Map<String,Object> getDictionaryDetailById(long dicId){
		log.info("进入getDictionaryById(long dicId)，根据上级id获取数据字典list");
		log.info("参数dicId="+dicId);
		Map<String,Object> result=this.sysDictionaryDao.getSysDictionaryDetailById(dicId);
		log.info("退出getDictionaryById(long dicId)，返回结果"+result);
		return result;
	}
	/**
	 * 
	 * @description: 更新字典状态
	 * @author：yuan.yw
	 * @param dicIds
	 * @param status
	 * @return     
	 * @return boolean     
	 * @date：Aug 22, 2013 11:09:59 AM
	 */
	public boolean updateDictionaryStatus(String dicIds,String status){
		log.info("进入updateDictionaryStatus(String dicIds,String status)，更新字典状态");
		log.info("参数dicIds="+dicIds+",status="+status);
		boolean flag = false;
		List<Map<String,Object>> list = this.sysDictionaryDao.getSelfAndChildDictionaryListByIds(dicIds);
		StringBuffer sf = new StringBuffer();
		if(list!=null && !list.isEmpty()){
			for(Map<String,Object> map:list){
				sf.append(","+map.get("DATA_TYPE_ID"));
			}
			dicIds = sf.substring(1,sf.length());
		}
		long orgUserId =  (Long)SessionService.getInstance().getValueByKey("org_user_id");
		flag=this.sysDictionaryDao.updateSysDictionaryStatusByIds(dicIds, status,orgUserId);
		log.info("退出updateDictionaryStatus(String dicIds,String status)，返回结果"+flag);
		return flag;
	}
	/**
	 * 
	 * @description: 分页获取第一级字典
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param currentPage
	 * @param pageSize
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Aug 22, 2013 3:00:23 PM
	 */
	public Map<String,Object> getRootDictionaryList(Map<String,Object> conditionMap,int currentPage,int pageSize){
		log.info("进入getRootDictionaryList(Map<String,Object> conditionMap,int currentPage,int pageSize)，分页获取第一级字典");
		log.info("参数conditionMap="+conditionMap+",currentPage="+currentPage+",pageSize="+pageSize);
		int indexStart = 1;
		int indexEnd = 1;
		if(currentPage>0){
			indexStart = 1+(currentPage-1)*pageSize;
			indexEnd = currentPage*pageSize;
		}
		Map<String,Object> result=this.sysDictionaryDao.getFirstLevelDictionaryByConditionForPage(conditionMap, indexStart, indexEnd);
		log.info("退出getRootDictionaryList(Map<String,Object> conditionMap,int currentPage,int pageSize)，返回结果"+result);
		return result;
	}
	/**
	 * 
	 * @description: 根据编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCode(String code){
		log.info("进入getChildDictionaryListByCode(String code)，code="+code+",根据编码获取下（子）级数据字典");
		List<Map<String,Object>> list = this.sysDictionaryDao.getChildDictionaryListByCode(code);
		log.info("退出getChildDictionaryListByCode(String code),返回"+list);
		return list;
	}
	/**
	 * 
	 * @description: 根据编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param selfFlag 是否获取自身
	 * @param status   为空值获取全部状态的数据字典
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:10:43 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCode(String code,boolean selfFlag,String status){
		log.info("进入getAllChildDictionaryListByCode(String code,boolean selfFlag)，code="+code+",selfFlag="+selfFlag+",status="+status+",根据编码获取其下全部下级字典（或者包括自身 selfFlag判断）");
		List<Map<String,Object>> list = this.sysDictionaryDao.getAllChildDictionaryListByCode(code, selfFlag,status);
		log.info("退出getAllChildDictionaryListByCode(String code,boolean selfFlag),返回"+list);
		return list;
	}
	/**
	 * 
	 * @description: 根据自身编码及父编码获取下（子）级数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public List<Map<String,Object>> getChildDictionaryListByCodeAndParCode(String code,String parentCode){
		log.info("进入getChildDictionaryListByCodeAndParCode(String code,String parentCode)，code="+code+",parentCode="+parentCode+",根据自身编码及父编码获取下（子）级数据字典");
		List<Map<String,Object>> list = this.sysDictionaryDao.getChildDictionaryListByCodeAndParCode(code, parentCode);
		log.info("退出getChildDictionaryListByCodeAndParCode(String code,String parentCode),返回"+list);
		return list;
	}
	/**
	 * 
	 * @description: 根据自身编码及父编码获取其下全部下级字典（或者包括自身 selfFlag判断）
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @param selfFlag 是否获取自身
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:20:08 AM
	 */
	public List<Map<String,Object>> getAllChildDictionaryListByCodeAndParCode(String code,String parentCode,boolean selfFlag){
		log.info("进入getAllChildDictionaryListByCodeAndParCode(String code,String parentCode,boolean selfFlag)，code="+code+",parentCode="+parentCode+",selfFlag="+selfFlag+",根据自身编码及父编码获取其下全部下级字典（或者包括自身 selfFlag判断）");
		List<Map<String,Object>> list = this.sysDictionaryDao.getAllChildDictionaryListByCodeAndParCode(code, parentCode, selfFlag);
		log.info("退出getAllChildDictionaryListByCodeAndParCode(String code,String parentCode,boolean selfFlag),返回"+list);
		return list;
	}
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCodeAndParCode(String code,String parentCode){
		log.info("进入getDictionaryMapByCodeAndParCode(String code,String parentCode)，code="+code+",parentCode="+parentCode+",根据自身编码及父编码获取数据字典");
		Map<String,Object> map = this.sysDictionaryDao.getDictionaryMapByCodeAndParCode(code, parentCode);
		log.info("退出getChildDictionaryListByCodeAndParCode(String code,String parentCode),返回"+map);
		return map;
	}
	
	/**
	 * 
	 * @description: 获取自身及子下级字典列表
	 * @author：yuan.yw
	 * @param dicIds
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 22, 2013 11:13:31 AM
	 */
	public List<Map<String,Object>> getSelfAndChildDictionaryListByCode(String code){
		log.info("进入getSelfAndChildDictionaryListByCode(String code)，code="+code+",获取自身及子下级字典列表");
		List<Map<String, Object>> list = this.sysDictionaryDao.getSelfAndChildDictionaryListByCode(code);
		log.info("退出getSelfAndChildDictionaryListByCode(String code),返回"+list);
		return list;
	}
	
	/**
	 * 
	 * @description: 根据自身编码及父编码获取数据字典
	 * @author：yuan.yw
	 * @param code
	 * @param parentCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Aug 26, 2013 10:19:37 AM
	 */
	public Map<String,Object> getDictionaryMapByCode(String code){
		return this.sysDictionaryDao.getDictionaryMapByCode(code);
	}
	
	/**
	 * 根据code集合其孙子级数据字典(in)
	* @author ou.jh
	* @date Jan 10, 2014 2:27:24 PM
	* @Description: TODO 
	* @param @param code
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSecondSysDictionaryListByCode(String code){
		return this.sysDictionaryDao.getSecondSysDictionaryListByCode(code);
	}
	

	public SysDictionaryDao getSysDictionaryDao() {
		return sysDictionaryDao;
	}

	public void setSysDictionaryDao(SysDictionaryDao sysDictionaryDao) {
		this.sysDictionaryDao = sysDictionaryDao;
	}
	
	
}
