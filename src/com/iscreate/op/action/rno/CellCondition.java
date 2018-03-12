package com.iscreate.op.action.rno;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;

/**
 * 小区查询条件类
 * 
 * @author brightming
 * 
 */
public class CellCondition {

	private long areaId;// 区域id
	private long bscId;// bscid
	private String label;// 英文名
	private String name;// 中文名
	private String coverType;// 覆盖类型
	private String coverarea;// 覆盖区域
	private String importancegrade;// 重要级别
	private long cityId;//城市id

	@Override
	public String toString() {
		return "cityId="+cityId+",areaId=" + areaId + ",bscId=" + bscId + ",label=" + label
				+ ",name=" + name + ",coverType=" + coverType + ",coverarea="
				+ coverarea + ",importancegrade=" + importancegrade;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getBscId() {
		return bscId;
	}

	public void setBscId(long bscId) {
		this.bscId = bscId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	public String getCoverarea() {
		return coverarea;
	}

	public void setCoverarea(String coverarea) {
		this.coverarea = coverarea;
	}

	public String getImportancegrade() {
		return importancegrade;
	}

	public void setImportancegrade(String importancegrade) {
		this.importancegrade = importancegrade;
	}
	
	

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	/**
	 * 构建小区查询的条件
	 * 
	 * @param allAllowareaIds
	 *            用户允许查看的全部区域的集合
	 * @return Sep 16, 2013 2:29:30 PM gmh
	 */
	public String buildSqlWhere(List<Long> allAllowareaIds) {
		StringBuilder buf = new StringBuilder();
		if (allAllowareaIds == null || allAllowareaIds.isEmpty()) {
			// 没有查看任何一个区域的权限
			buf.append("1<>1");// 不查
			return buf.toString();
		}

		// 区域
		if (areaId > 0) {
			//
			buf.append(" AREA_ID=" + areaId);
		} 
		//2013-12-23 增加cityid---begin
		else if(cityId>0){
			List<Long> subIds=AuthDsDataDaoImpl.getSubAreaIdsByCityId(cityId);
//			String tmp="select AREA_ID FROM SYS_AREA WHERE PARENT_ID="+cityId;
			String tmp="";
			if(subIds!=null && subIds.size()>0){
				for(Long id:subIds){
					tmp+=id+",";
				}
				if(tmp.length()>0){
					tmp=tmp.substring(0, tmp.length()-1);
				}else{
					tmp=cityId+"";
				}
			}
			buf.append(" AREA_ID IN ( "+tmp+" ) ");
		}//--------end
		else {
			StringBuilder buf2 = new StringBuilder();
			for (Long aid : allAllowareaIds) {
				buf2.append(aid + ",");
			}
			buf2.deleteCharAt(buf2.length() - 1);
			buf.append(" AREA_ID IN ( " + buf2.toString() + " )");
		}

		// bsc
		if (bscId > 0) {
			buf.append(" and BSC_ID=" + bscId);
		}// 不选的话，不理会，全部都要

		// label
		if (label != null && !label.trim().equals("")) {
			//buf.append(" and LABEL='" + label.trim() + "'");
			buf.append(" and \"INSTR\"(LABEL,'"+label.trim()+"')>0");
		}

		// name
		if (name != null && !name.trim().equals("")) {
			//buf.append(" and NAME='" + name.trim() + "'");
			buf.append(" and \"INSTR\"(NAME,'"+name.trim()+"')>0");
		}

		// coverType
		if (coverType != null && !coverType.trim().equals("")) {
			buf.append(" and COVERTYPE='" + coverType.trim() + "'");
		}
		// coverarea
		if (coverarea != null && !coverarea.trim().equals("")) {
			buf.append(" and COVERAREA='" + coverarea.trim() + "'");
		}

		// importancegrade
		if (importancegrade != null && !importancegrade.trim().equals("")) {
			buf.append(" and IMPORTANCEGRADE='" + importancegrade.trim() + "'");
		}

		return buf.toString();
	}

	/**
	 * 构建邻区查询的条件
	 * 
	 * @param allAllowareaIds
	 *            用户能看到的区域id的集合
	 * @return Sep 17, 2013 3:15:59 PM gmh
	 */
	public String buildNcellQuerySqlWhere(List<Long> allAllowareaIds) {
		StringBuilder buf = new StringBuilder();

		// label
		if (label != null && !label.trim().equals("")) {
//			buf.append(" CELL='"
//					+ label.trim() + "'");
			buf.append(" \"INSTR\"(CELL,'"+label.trim()+"')>0");
		}

		// name
		if (name != null && !name.trim().equals("")) {
			//buf.append((buf.length()>0?" and ":"")+" CELL in(select LABEL from CELL where NAME='"+name+"') ");
			buf.append((buf.length()>0?" and ":"")+" CELL in(select LABEL from CELL where \"INSTR\"(NAME,'"+name+"')>0) ");
		}
		
		if (bscId > 0) {
			// 明确指定了bscid
			buf.append((buf.length()>0?" and ":"")+" BSC_ID=" + bscId);
		}else if(areaId<=0){
			// 未指定bscid，也未指定areaid
			/*StringBuilder buf2 = new StringBuilder();
			for (Long bid : allAllowareaIds) {
				buf2.append(bid + ",");
			}
			buf2.deleteCharAt(buf2.length() - 1);
			buf.append((buf.length()>0?" and ":"")+" BSC_ID IN ( select BSC_ID FROM RNO_BSC_RELA_AREA WHERE AREA_ID IN (" + buf2.toString() + " ))");
			*/
			
			List<Long> subIds=AuthDsDataDaoImpl.getSubAreaIdsByCityId(cityId);
			String tmp="";
			if(subIds!=null && subIds.size()>0){
				for(Long id:subIds){
					tmp+=id+",";
				}
				if(tmp.length()>0){
					tmp=tmp.substring(0, tmp.length()-1);
				}else{
					tmp=cityId+"";
				}
			}
			
//			String tmp="select AREA_ID FROM SYS_AREA WHERE PARENT_ID="+cityId;
			buf.append((buf.length()>0?" and ":"")+" BSC_ID IN ( select BSC_ID FROM RNO_BSC_RELA_AREA WHERE AREA_ID IN (" + tmp + " ))");
		}else{
			// 指定了区域id
			buf.append((buf.length()>0?" and ":"")+" BSC_ID in (select BSC_ID FROM RNO_BSC_RELA_AREA WHERE AREA_ID="+areaId+")");
		}
		
		

		return buf.toString();
	}

	public static void main(String[] args) {
		CellCondition cc = new CellCondition();
		// /cc.setAreaId(1);
		cc.setName("白藤三路1怎么了");

		List<Long> allAllowareaIds = new ArrayList<Long>();
		allAllowareaIds.add(190L);
		allAllowareaIds.add(2L);
		allAllowareaIds.add(3L);
		// System.out.println(cc.buildSqlWhere(allAllowareaIds));

		// cc.setBscId(1l);
		System.out.println(cc.buildNcellQuerySqlWhere(allAllowareaIds));
	}
}
