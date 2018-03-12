package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.mockejb.jndi.java.javaURLContextFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoPlanDesignDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoHandover;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;

public class GsmChannelSwitchStatisticsFileParser extends ExcelFileParserBase {

	
	private static Log log = LogFactory.getLog(GsmChannelSwitchStatisticsFileParser.class);
	//spring注入
	private SysAreaDao sysAreaDao;
	public ExcelService excelService;
	public RnoPlanDesignDao rnoPlanDesignDao;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}
	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}
	public ExcelService getExcelService() {
		return excelService;
	}
	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}
	public RnoPlanDesignDao getRnoPlanDesignDao() {
		return rnoPlanDesignDao;
	}
	public void setRnoPlanDesignDao(RnoPlanDesignDao rnoPlanDesignDao) {
		this.rnoPlanDesignDao = rnoPlanDesignDao;
	}
	private static List<String> expectTitles = Arrays.asList("地区", "时间",
			"切换关系", "网元名", "方向", "切出尝试总次数", "切出成功总次数", "切出返回总次数", "切出尝试成功率",
			"K切换次数", "L切换次数", "超TA切换次数", "上行质差切换次数", "下行质差切换次数", "HCS切换次数", "乒乓切换次数");

	private static int titleSize = expectTitles.size();
	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		// TODO Auto-generated method stub
		log.debug("进入CellFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);

		long begTime = new Date().getTime();
		// 导入到的目标区域（区/县级别）
		SysArea country = sysAreaDao.getAreaById(areaId);
		if(country==null){
			log.error("不存在id="+areaId+"的区域！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"指定的导入区域不存在！");
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			return false;
		}else{
			log.info("准备导入小区数据到："+country.getName());
		}
		// 获取全部数据
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);
		
		if (allDatas == null || allDatas.size() < 1) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！因为文件是空的");
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			return false;
		}
		/*// 检查文件标题
		boolean titleok = super.checkTitles(expectTitles, allDatas.get(0));
		if (!titleok) {
			log.error("上传的小区excel文件的格式不符合格式要求！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！确保文件的格式为：" + expectTitles);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}*/

		int total = allDatas.size() - 1;// excel有效记录数
		//根据区域和指标时间删除切换统计描述中的所有
		String insertHandDescSql = "INSERT INTO RNO_HANDOVER_DESCRIPTOR VALUES(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
		String insertHandSql ="INSERT INTO RNO_HANDOVER VALUES(SEQ_RNO_HANDOVER.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// 数据连接
		Connection connection = DataSourceConn.initInstance().getConnection();
		String chareaname=attachParams.get("chareaname").toString();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement insertHandSqlpstmt = null;
		PreparedStatement insertHandDescSqlstmt=null;
		if (needPersist) {
			try {
				insertHandDescSqlstmt=connection.prepareStatement(insertHandDescSql);
				insertHandSqlpstmt = connection.prepareStatement(insertHandSql);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("在准备插入的statment时出错！");
				return false;
			}
			
		}
		
		List<String> oneData;
		int index;
		int insertSuccessCnt = 0, updateSuccessCnt = 0, failCnt = 0;
		boolean hasError = false;// 是否有错
		RnoHandover oneRnoHandover;
		StringBuilder buf = new StringBuilder();
		
		long descId = -1;
		String vsql = "select SEQ_RNO_HANDOVER_DESCRIPTOR.NEXTVAL as id from dual";
		PreparedStatement pstmt =null;
		try {
			pstmt=(PreparedStatement) connection
					.prepareStatement(vsql);
		} catch (SQLException e3) {
			e3.printStackTrace();
			fail(connection, token, "数据处理出错！code=505");
			return false;
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			rs.next();
			descId= rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=506");
			return false;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		RnoHandoverDescriptor rnoHandoverDescriptor=new RnoHandoverDescriptor();
		for (int i = 1; i < allDatas.size(); i++) {
			int cellColumn=1;
			oneData = allDatas.get(i);
			String staticTime="";
			staticTime=oneData.get(1);
			if (i==1) {
					try {
						int k=1;
						insertHandDescSqlstmt.setLong(k++, descId);
						insertHandDescSqlstmt.setString(k++, staticTime);
						insertHandDescSqlstmt.setLong(k++, areaId);
						insertHandDescSqlstmt.setString(k++, df.format(new Date()));
						insertHandDescSqlstmt.setString(k++, df.format(new Date()));
						//insertHandDescSqlstmt.setDate(k++,  new java.sql.Date(df.parse(df.format(new Date())).getTime()));
						//insertHandDescSqlstmt.setDate(k++,  new java.sql.Date(df.parse(df.format(new Date())).getTime()));
						insertHandDescSqlstmt.setString(k++, "Y");
						insertHandDescSqlstmt.setString(k++, chareaname);
						rnoHandoverDescriptor.setRnoHandoverDescId(descId);
						rnoHandoverDescriptor.setStaticsTime(df.parse(staticTime));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			}
			
			// 产生切换统计对象
			oneRnoHandover = createRnoHandoverFromExcelLine(oneData, i, buf, rnoHandoverDescriptor,descId);
			//System.out.println("oneRnoHandover="+oneRnoHandover);
			if (oneRnoHandover == null) {
				// 数据不符合要求
				failCnt++;
				continue;
			}
			// 绑定参数
			// cell,ncell,cs,dir,bsc_id
			index = 1;
			try {
				insertSuccessCnt++;
				insertHandSqlpstmt.setLong(index++, descId);
				insertHandSqlpstmt.setString(index++, oneRnoHandover.getServerCell());
				insertHandSqlpstmt.setString(index++, oneRnoHandover.getTargetCell());
				insertHandSqlpstmt.setString(index++, oneRnoHandover.getNetElementName());
				insertHandSqlpstmt.setString(index++, oneRnoHandover.getDirection());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHovercnt());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHoversuc());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHorttoch());
				insertHandSqlpstmt.setDouble(index++, oneRnoHandover.getHoTrySucRate());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHotokcl());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHotolcl());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHoexcta());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHouplqa());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHodwnqa());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHotohcs());
				insertHandSqlpstmt.setLong(index++, oneRnoHandover.getHodupft());
				insertHandSqlpstmt.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				hasError = true;
				break;
			}

			// 更新进度
			if(i!=total){
			fileParserManager.updateTokenProgress(token, i * 1.0f / total);
			}
		}

		// 执行
		if (insertSuccessCnt > 0) {
			// 真正插入
			try {
				boolean hodesc=insertHandDescSqlstmt.execute();
				//System.out.println(hodesc);
				//System.out.println("descid="+descId);
				int[] res1 = insertHandSqlpstmt.executeBatch();
				 //System.out.println("insert.len="+res1.length);
				 for(int i=0;i<res1.length;i++){
				 //System.out.println("insert "+i+" --- 结果： "+res1[i]);
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// commit
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if (insertHandSqlpstmt != null) {
				try {
					insertHandDescSqlstmt.close();
					insertHandSqlpstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				connection.setAutoCommit(true);
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String result = "导入完成。共 [" + total + "] 条记录，成功 [" + insertSuccessCnt
				+ "]条记录，出错 [" + failCnt
				+ "] 条记录。<br/>";
		try {
			 memCached.set(token, RnoConstant.TimeConstant.TokenTime,
					result + buf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if (autoload) {
			HttpSession session=(HttpSession)attachParams.get("session");
			//List<RnoHandoverDescriptor> lists=(List<RnoHandoverDescriptor>)session.getAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
			List<PlanConfig> lists=(List<PlanConfig>)session.getAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
			PlanConfig planConfig=null;
			
			if (lists!=null) {
					if(!lists.contains(rnoHandoverDescriptor)){
						boolean isTemp=false;
						planConfig=new PlanConfig();
						//System.out.println("planconfig=="+planConfig);
						planConfig.setSelected(false);
						planConfig.setType(RnoConstant.DataType.HAND_OVER_DATA);
						planConfig.setTemp(isTemp);
						//System.out.println(rnoHandoverDescriptor.getRnoHandoverDescId());
						planConfig.setConfigId(rnoHandoverDescriptor.getRnoHandoverDescId());
						//planConfig.setConfigId(descId);
						String dateString=df.format(rnoHandoverDescriptor.getStaticsTime());
						planConfig.setCollectTime(dateString);
						planConfig.setName(chareaname+dateString);
						planConfig.setAreaName(chareaname);
						planConfig.setTitle(chareaname+dateString);
						lists.add(planConfig);
						//session.setAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID, lists);
					}
			}else {
				List<PlanConfig> pList=new ArrayList<PlanConfig>();
				boolean isTemp=false;
				planConfig=new PlanConfig();
				//System.out.println("planconfig=="+planConfig);
				planConfig.setSelected(false);
				planConfig.setType(RnoConstant.DataType.HAND_OVER_DATA);
				planConfig.setTemp(isTemp);
				System.out.println(rnoHandoverDescriptor.getRnoHandoverDescId());
				planConfig.setConfigId(rnoHandoverDescriptor.getRnoHandoverDescId());
				//planConfig.setConfigId(descId);
				String dateString=df.format(rnoHandoverDescriptor.getStaticsTime());
				planConfig.setCollectTime(dateString);
				planConfig.setName(chareaname+dateString);
				planConfig.setAreaName(chareaname);
				planConfig.setTitle(chareaname+dateString);
				pList.add(planConfig);
				session.setAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID, pList);
			}
		}
		return true;
	}


	/**
	 * 创建小区切换统计对象
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 14, 2013 1:07:28 PM chao.xj
	 */
	private RnoHandover createRnoHandoverFromExcelLine(List<String> oneData, int i,
			StringBuilder buf,RnoHandoverDescriptor rnoHandoverDescriptor,long descId) {
		
		String msg = "";
		if (oneData == null) {
			msg = "第[" + (i + 1) + "]行错误！数据为空！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		if (oneData.size() < titleSize) {
			msg = "第[" + (i + 1) + "]行错误！数据不齐全！";
			buf.append(msg + "<br/>");
			return null;
		}
		
		
		
		Long rnoHandoverId,rnoHandoverDescId,hovercnt,hoversuc,horttoch,hotokcl,hotolcl,hoexcta,houplqa,hodwnqa,hotohcs,hodupft;
		String serverCell,targetCell,netElementName,direction;
		Double hoTrySucRate;
		
		
		//rnoHandoverDescId=rnoHandoverDescriptor.getRnoHandoverDescId();
		rnoHandoverDescId=descId;
		if (rnoHandoverDescId == null || Long.toString(rnoHandoverDescId).trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含切换描述ID数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		String handrelation[]=oneData.get(2).split("-");
		serverCell=handrelation[0];
		targetCell=handrelation[1];
		if (handrelation == null || handrelation[0].trim().isEmpty() || handrelation[1].trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含切换关系数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		int index=3;
		//System.out.println("index++="+index++);
		//System.out.println("index="+index);
		netElementName=oneData.get(index++);
		//System.out.println("netElementName="+netElementName);
		if (netElementName == null || netElementName.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含网元名数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		direction=oneData.get(index++);
		//System.out.println("direction="+direction);
		if (direction == null || direction.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含方向数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hovercnt=Long.parseLong(oneData.get(index++));
			//System.out.println("hovercnt="+hovercnt);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含切出尝试总次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hoversuc=Long.parseLong(oneData.get(index++));
			//System.out.println("hoversuc="+hoversuc);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含切出成功总次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			horttoch=Long.parseLong(oneData.get(index++));
			//System.out.println("horttoch="+horttoch);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含切出返回总次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hoTrySucRate=Double.parseDouble(oneData.get(index++));
			//System.out.println("hoTrySucRate="+hoTrySucRate);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含切出尝试成功率数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hotokcl=Long.parseLong(oneData.get(index++));
			//System.out.println("hotokcl="+hotokcl);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含K切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hotolcl=Long.parseLong(oneData.get(index++));
			//System.out.println("hotolcl="+hotolcl);
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含L切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hoexcta=Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含超TA切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			houplqa=Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含上行质差切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hodwnqa=Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含下行质差切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hotohcs=Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含HCS切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		try {
			hodupft=Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "第[" + (i + 1) + "]行错误！数据未包含乒乓切换次数数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		RnoHandover rnoHandover=new RnoHandover();
		rnoHandover.setRnoHandoverDescId(rnoHandoverDescId);
		rnoHandover.setServerCell(serverCell);
		rnoHandover.setTargetCell(targetCell);
		rnoHandover.setNetElementName(netElementName);
		rnoHandover.setDirection(direction);
		rnoHandover.setHovercnt(hovercnt);
		rnoHandover.setHoversuc(hoversuc);
		rnoHandover.setHorttoch(horttoch);
		rnoHandover.setHoTrySucRate(hoTrySucRate);
		rnoHandover.setHotokcl(hotokcl);
		rnoHandover.setHotolcl(hotolcl);
		rnoHandover.setHoexcta(hoexcta);
		rnoHandover.setHouplqa(houplqa);
		rnoHandover.setHodwnqa(hodwnqa);
		rnoHandover.setHotohcs(hotohcs);
		rnoHandover.setHodupft(hodupft);
		return rnoHandover;
	}

}
