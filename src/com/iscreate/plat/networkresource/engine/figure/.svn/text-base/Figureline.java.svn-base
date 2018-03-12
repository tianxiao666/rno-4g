package com.iscreate.plat.networkresource.engine.figure;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;

public class Figureline extends BasicEntity {
	private static final long serialVersionUID = 7118235928132374939L;

	public static final String MY_TYPE = Figureline.class.getSimpleName();

	public static final String ID_KEY = "id";
	public static final String LEFTID_KEY = "leftId";
	public static final String RIGHTID_KEY = "rightId";
	public static final String TYPE_KEY = "linkType";
	public static final String DATE_KEY = "birthdate";
	public static final String FIGUREID_KEY = "figureId";
	public static final String LEFTTYPE_KEY = "lefttype";// 以下变量 为递归 查询增加的两个字段  yuan.yw
	public static final String RIGHTTYPE_KEY = "righttype";
	private Figurenode left;
	private Figurenode right;

	private Figureline() {

	}

	public Figureline(long figureId, Figurenode left, Figurenode right,
			FigurelineType lineType,long id) {
		super.set(FIGUREID_KEY, figureId);
		super.set(DefaultParam.typeKey, MY_TYPE);
		super.set(ID_KEY, id);
		super.set(LEFTTYPE_KEY,left.getValue("entityType"));
		super.set(RIGHTTYPE_KEY,right.getValue("entityType"));
		setLeftnode(left);
		setRightnode(right);
		setLineType(lineType);
	}

	public long getFigureId() {
		return Long.parseLong(super.getValue(FIGUREID_KEY)+"");
	}

	public long getId() {
		long id = Long.parseLong(super.getValue(ID_KEY)+"");
		return id;
	}

	public void setId(long id) {
		super.set(ID_KEY, id);
	}

	public long getLeftId() {
		if (left != null) {
			return left.getId();
		}
		return super.getValue(LEFTID_KEY);
	}

	public void setLeftnode(Figurenode left) {
		super.set(LEFTID_KEY, left.getId());
		this.left = left;
	}

	public long getRightId() {
		if (right != null) {
			return right.getId();
		}
		return super.getValue(RIGHTID_KEY);
	}

	public void setRightnode(Figurenode right) {
		super.set(RIGHTID_KEY, right.getId());
		this.right = right;
	}

	public FigurelineType getLineType() {
		String type = super.getValue(TYPE_KEY);
		if (FigurelineType.CLAN.toString().equals(type)) {
			return FigurelineType.CLAN;
		} else {
			return FigurelineType.LINK;
		}
	}

	public void setBirthdate(String birthdate) {
		super.set(DATE_KEY, birthdate);
	}

	public void setLineType(FigurelineType lineType) {
		super.set(TYPE_KEY, lineType.toString());
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		if (key.equals(LEFTID_KEY)) {
			if (left != null) {
				return (T) Long.toString(left.getId());
			}
		} else if (key.equals(RIGHTID_KEY)) {
			if (right != null) {
				return (T) Long.toString(right.getId());
			}
		}
		return super.getValue(key);
	}

	public static Figureline changeFromEntity(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		Figureline l = new Figureline();
		for (String key : entity.keyset()) {
			l.set(key, entity.getValue(key));
		}
		return l;
	}
}
