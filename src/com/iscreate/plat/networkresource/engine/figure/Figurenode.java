package com.iscreate.plat.networkresource.engine.figure;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByIdExecution;

public class Figurenode extends BasicEntity {
	private static final long serialVersionUID = 6327313320504622022L;

	public static final String MY_TYPE = Figurenode.class.getSimpleName();

	public static final String ID_KEY = "id";
	public static final String ENTYPE_KEY = "entityType";
	public static final String ENID_KEY = "entityId";
	public static final String FIGUREID_KEY = "figureId";
	public static final String DATE_KEY = "birthdate";
	//yuan.yw 2013-06-28
	public static final String PATH = "path";  //上下级路径path
//	public static final String AREA_PATH = "area_path";//所属区域路径path
	public static final String PARENT_FIGURENODE_ID = "parent_figurenode_id";//figurenode 父id
	private ApplicationEntity entity = null;
	Figure figure = null;

	String loadInfo = "INIT;";

	public Figurenode() {

	}
	public Figurenode(long figureId, ApplicationEntity entity, Figure figure,long id) {
		super.set(DefaultParam.typeKey, Figurenode.MY_TYPE);
		super.set(ID_KEY, id);
		super.set(FIGUREID_KEY, figureId);
		setApplicationEntity(entity);
		this.figure = figure;
	}
	/**
	 * @desc  Figurenode 构造函数
	 * @param figureId
	 * @param entity
	 * @param figure
	 * @param id
	 * @param path
	 * @param parentId
	 * @updatedate 2013-06-28 14:04:00
	 * @updateauthor yuan.yw
	 * @updatereason 增加path，parentId参数设值
	 */
	public Figurenode(long figureId, ApplicationEntity entity, Figure figure,long id,String path,long parentId) {
		super.set(DefaultParam.typeKey, Figurenode.MY_TYPE);
		super.set(ID_KEY, id);
		super.set(FIGUREID_KEY, figureId);
		super.set(PATH, path);//yuan.yw 2013-06-28
		super.set(PARENT_FIGURENODE_ID, parentId);
		//super.set(AREA_PATH, areaPath);//yuan.yw 2013-06-28
		setApplicationEntity(entity);
		this.figure = figure;
	}
	//path areaPath getter and setter
	public String getPath() {
		
		return super.getValue(PATH);
	}
	public void setPath(String path) {
		super.set(PATH, path);
	}
	public long getParentFigurenodeId() {
		if(super.getValue(PARENT_FIGURENODE_ID)!=null){
			return 0;
		}
		return super.getValue(PARENT_FIGURENODE_ID);
	}
	public void setParentFigurenodeId(long parentId) {
		super.set(PARENT_FIGURENODE_ID, parentId);
	}
	/*public String getAreaPath() {
		
		return super.getValue(AREA_PATH);
	}
	public void setAreaPath(String areaPath) {
		super.set(AREA_PATH, areaPath);
	}*/
	public long getFigureId() {
		if(super.getValue(FIGUREID_KEY)==null){
			return 0;
		}
		return Long.parseLong(super.getValue(FIGUREID_KEY)+"");
	}

	public long getId() {
		long id = Long.parseLong(super.getValue(ID_KEY)+"");
		return id;
	}

	public void setId(long id) {
		super.set(ID_KEY, id);
	}

	public String getEntityType() {
		if (entity != null) {
			return entity.getType();
		}
		String entityType = super.getValue(ENTYPE_KEY);
		return entityType;
	}

	public void setEntityType(String entityType) {
		super.set(ENTYPE_KEY, entityType);
	}

	public long getEntityId() {
		if (entity != null) {
			return entity.getId();
		}
		long entityId = Long.parseLong(super.getValue(ENID_KEY)+"");
		return entityId;
	}

	public void setBirthdate(String birthdate) {
		super.set(DATE_KEY, birthdate);
	}

	public void setEntityId(long entityId) {
		super.set(ENID_KEY, entityId);
	}

	public ApplicationEntity getApplicationEntity() {
		if (this.entity == null) {
			String type = getEntityType();
			long id =getEntityId();
			QueryApplicationEntityByIdExecution qa = new QueryApplicationEntityByIdExecution(
					type, id);
			this.entity = figure.doExecution(qa);
		}
		return entity;
	}

	public void setApplicationEntity(ApplicationEntity e) {
		if (e != null) {
			setEntityType(e.getType());
			setEntityId(e.getId());
			this.entity = e;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		if (ENID_KEY.equals(key)) {
			if (entity != null) {
				return (T) Long.toString(entity.getId());
			}
		} else if (ENTYPE_KEY.equals(key)) {
			if (entity != null) {
				return (T) entity.getType();
			}
		}
		return super.getValue(key);
	}

	public static Figurenode changeFromEntity(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		Figurenode n = new Figurenode();
		for (String key : entity.keyset()) {
			n.set(key, entity.getValue(key));
		}
		return n;
	}
}
