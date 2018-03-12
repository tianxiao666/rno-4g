package com.iscreate.op.constant;

import java.util.ArrayList;
import java.util.List;

public interface RnoConstant {

	/**
	 * session常量
	 * 
	 * @author brightming
	 * 
	 */
	public interface SessionConstant {
		// 被加载的分析列表
		public static final String STS_LOAD_CONFIG_ID = "STS_LOAD_CONFIG_ID";

		// 被加载的小区配置分析列表
		public static final String CELL_LOAD_CONFIG_ID = "PLAN_LOAD_CELL_CONFIG_ID";

		// 被加载的干扰分析列表
		public static final String INTERFERENCE_LOAD_CONFIG_ID = "PLAN_LOAD_INTERFERENCE_CONFIG_ID";

		// 被加载的ncs分析列表
		public static final String NCS_LOAD_CONFIG_ID = "PLAN_LOAD_NCS_CONFIG_ID";
		// 加载的小区结构分析列表
		public static final String CELL_STRUCT_CONFIG_ID = "PLAN_LOAD_CELL_STRUCT_CONFIG_ID";
		// 加载的小区切换分析列表
		public static final String CELL_HANDOVER_CONFIG_ID = "PLAN_LOAD_CELL_HANDOVER_CONFIG_ID";

		// 加载的dt分析列表
		public static final String DT_CONFIG_ID = "DT_LOAD_CONFIG_ID";

		// 文件上传进度的信息
		public static final String uploadStatusSessionKey = "currentUploadStatusToken";
	}

	public interface ApplicationConstant {
		// 导入lte时，锁定的区域
		public static final String LockAreaForImportList = "LOCKAREAFORIMPORTLIST";

		// 导入mrr时，锁定的区域
		public static final String LockAreaForImportEriMrr = "LOCKAREAFORIMPORTERIMRR";

		// 导入2g hw cell时，锁定的区域
		public static final String LockAreaForImport2GHWCell = "LOCKAREAFORIMPORT2GHWCELL";
		// 导入2g hw mrr时，锁定的区域
		public static final String LockAreaForImport2GHWMrr = "LOCKAREAFORIMPORT2GHWMRR";
		// 导入2g eri cell时，锁定的区域
		public static final String LockAreaForImport2GEriCell = "LOCKAREAFORIMPORT2GERICELL";
	}

	public interface DataType {
		// 干扰剧矩阵数据
		public static final String INTERFERENCE_DATA = "INTERFERENCEDATA";

		// 小区数据
		public static final String CELL_DATA = "CELLDATA";
		// NCS data
		public static final String NCS_DATA = "NCSDATA";
		// 小区结构数据
		public static final String CELLSTRUCT_DATA = "CELLSTRUCTDATA";
		// 小区切换数据
		public static final String HAND_OVER_DATA = "HAND_OVER_DATA";
		// dt数据
		public static final String DT_DATA = "DT_DATA";

		// 冗余邻区
		public static final String RedundantNcellType = "REDUNDANT";
		// 漏定邻区
		public static final String OmitNcellType = "OMIT";
	}

	public interface CacheConstant {
		// 小区配置列表在缓存的索引前缀，后面跟着配置方案id。如CACHE_CELL_CONFIG_3的key对应的value就是配置方案为3的小区列表
		public static final String CACHE_CELL_CONFIG_PRE = "CACHE_CELL_CONFIG_";

		// 记录了upload的结果情况
		public static final String UPLOADTOKENMSG = "UPLOADTOKENMSG";

		// 某区域下的小区列表前缀
		// 区域的级别为"区/县"
		public static final String CACHE_GISCELL_IN_AREA_PRE = "CACHE_GISCELL_IN_AREA_";
		
		//某区域下LTE小区列表前缀
		public static final String CACHE_LTE_GISCELL_IN_AREA_PRE = "CACHE_LTE_GISCELL_IN_AREA_";

		// 干扰矩阵描述数据总干扰分析
		public static final String CACHE_ANALYSIS_GISCELL_IN_AREA_ = "CACHE_ANALYSIS_GISCELL_IN_AREA_";

		// 某个小区的邻区
		public static final String CACHE_NCELL_OF_CELL_PRE = "CACHE_NCELL_OF_CELL_";

		// gps经纬度的key前缀。key后面为区域id，value为map<String,String[]>.
		// map里的key格式为lng+","+lat
		public static final String GPSPOINT_EACH_AREA_PRE = "GPS_AREA_LNG_LAT_";

		// 单个经纬度的
		public static final String GPSPOINT_POINT_PRE = "GPS_POINT_LNG_LAT_";

		// -------话务统计相关------//
		// 查询话务性能，并且，把查询结果加载到分析列表.对应的值就是查询出来的小区名称列表
		public static final String CACHE_STATICS_QUERY_COND_PRE = "CACHE_STATICS_QUERY_COND_PRE_";
		// 某分析列表组合的小区的gis信息,后缀为configid的串联：configid1_configid2
		public static final String STATICS_GISCELL_IN_SELECTION_LIST_PRE = "STATICS_GISCELL_IN_SELECTION_LIST_PRE_";
		// 分析列表对应的某类型资源的统计情况
		public static final String STATICS_STSCODE_PRE = "STATICS_STSCODE_";
		// 缓存指标符合某类型的小区的情况的key的前缀，后面有configid补充。value为对应的cell情况
		public static final String STATICS_SPECIALINDEXCELLTYPE_PRE = "STATICS_SPECIALINDEXCELLTYPE_";

		// 某区域的忙小区统计情况
		public static final String STATICS_HEAVYLOAD_CELL_IN_AREA_PRE = "STATICS_SPECIALINDEXCELLTYPE_";
		// ----------频率复用相关--------//
		// 频率复用统计
		public static final String STATICS_REPORT_FREQREUSE_IN_LIST_PRE = "STATICS_REPORT_FREQREUSE_IN_LIST_PRE";
		// 某分析列表组合的小区的gis信息,后缀为configid
		public static final String STATICS_FREQREUSE_GISCELL_IN_SELECTION_LIST_PRE = "STATICS_FREQREUSE_GISCELL_IN_SELECTION_LIST_PRE";
		
		//-----------4g话统部分------------//
		//缓存4g话统指标，根据区域指标列起止时间和所选小区保存
		public static final String CACHE_LTE_STS_INDEX_BY_AREA_COLUMN_TIME_CELLS_PRE = "CACHE_LTE_STS_INDEX_BY_AREA_COLUMN_TIME_CELLS_PRE_";
	}

	/**
	 * 状态常量
	 * 
	 * @author brightming
	 * 
	 */
	public interface StatusConstant {
		public static final String Normal = "Y";// 正常
		public static final String Delete = "X";// 删除

		public static final String Yes = "Y";
		public static final String No = "N";
	}

	/**
	 * 时间相关的常量
	 * 
	 * @author brightming
	 * 
	 */
	public interface TimeConstant {
		
		// token相关的信息在缓存的时间长度
		public static final int TokenTime = 60 * 60;// 1h,以秒为单位
		
		// 某区域下的小区在缓存的持续时间
		public static final int GisCellInAreaTime = 60 * 60;// 1h

		// 某查询结果数量的结果缓存时间
		public static final int CellQueryResultTotalCountTime = 2 * 60;

		// 某个小区的邻区的缓存时间
		public static final int NcellOfSingleCellTime = 5 * 60;

		// GPS到百度的坐标转换结果的保存时限，12h
		public static final int GPSTOBSIDUPOINTTIME = 60 * 60 * 12;

		// 话务统计中，用于存储所选择的分析列表对应的小区gis信息的时长
		public static final int TRAFFICSTSSELCONFIGURESTSCELLTIME = 60 * 20;// 20分钟

		// 区域的忙小区缓存时长
		public static final int TAFFFICSTATICSHEAVYLOADCELLINAREATIME = 60 * 10;// 10
																				// 分钟

		// 某区域下的总干扰分析小区诗句缓存时长
		public static final int ANALYSISCELLINAREATIME = 60 * 10;// 10 分钟
		
		//4g话统指标保存时间
		public static final int LTESTSINDEXTIME = 10 * 60;// 10 分钟

	}

	public interface DBConstant {
		// 网络制式
		public static final String NET_TYPE_GSM = "GSM";
		public static final String NET_TYPE_TD = "TD";

		// 指标类型
		public static final String STS_SPEC_TYPE_GSM_AUDIO = "CELLAUDIOINDEX";// gsm语音指标
		public static final String STS_SPEC_TYPE_GSM_DATA = "CELLDATAINDEX";// gsm数据指标
		public static final String STS_SPEC_TYPE_GSM_CITYQUL = "CITYQUALITYINDEX";// 城市网络质量指标

		// 地图类型
		public static final String MAPTYPE_BAIDU = "BAIDU";
		public static final String MAPTYPE_GOOGLE = "GOOGLE";

		// 指标渲染type
		public static final String RENDERER_TYPE_STSINDEX = "STSINDEX";// 指标值
		public static final String RENDERER_TYPE_STSSPECIALCELL = "STSSPECIALCELL";// 特定小区
		public static final String RENDERER_TYPE_PLANDESIGN = "PLANDESIGN";// 优化规划类型

		// 配置类型：
		public static final String CELLDATA = "CELLDATA";// CELLDATA：表示小区配置
		public static final String INTERFERENCEDATA = "INTERFERENCEDATA";// INTERFERENCEDATA：表示干扰配置

	}

	public enum ApplyScope {
		SYSTEM("SYSTEM", "for system default"), PERSONAL("PERSONAL",
				"only for single person"), CITY("CITY", "for city range"), DISTRICT(
				"DISTRICT", "for district range");

		private String code;
		private String desc;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private ApplyScope(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
	}

	/**
	 * 
	 * @title 业务常量
	 * @author chao.xj
	 * @date 2014-1-20下午06:57:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	public interface BusinessConstant {
		// 计算簇约束因子
		public static final int GSM900_FEQ_CNT = 95;// gsm900可用tch频点数
		public static final int GSM1800_FEQ_CNT = 124;// gsm1800可用tch频点数

	}

	/**
	 * 小区类型
	 * 
	 * @author brightming
	 * 
	 */
	public enum CellType {
		INDOOR_CELL("室内覆盖"), OUTSIDE_CELL("室外覆盖"), MIXED_CELL("混合覆盖");

		private String name;

		private CellType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static List<String> getNameArray() {
			List<String> names = new ArrayList<String>();
			CellType[] vls = CellType.values();
			for (CellType vl : vls) {
				names.add(vl.getName());
			}
			return names;
		}

	}

	public interface ReportConstant {
		// ncs分析结果中小区结果所在sheet的名称
		public static final String NCS_REPORT_CELL_RES_SHEET = "结构指标";
		public static final String NCS_REPORT_CLUSTER_SHEET = "所有连通簇列表";// 连通簇
		public static final String NCS_REPORT_CLUSTER_CELL_SHEET = "最大连通簇列表";// 簇内小区
		public static final String NCS_REPORT_CLUSTER_CELL_RELA_SHEET = "簇相关小区列表";// 簇内小区的干扰关系
		public static final String NCS_REPORT_OPT_SUGGESTION_SHEET = "自动优化建议";
		// ncs小区结果标题和数据库key
		// @author chao.xj 2014-7-24 上午9:53:28 增加mrr字段
		public static final String[] NCS_REPORT_CELL_RES_SHEET_TITLES = { "小区",
				"小区频点数", "被干扰系数", "网络结构指数", "冗余覆盖指数", "重叠覆盖度", "干扰源系数",
				"过覆盖系数", "小区检测次数", "小区检测次数（不含室分）", "理想覆盖距离", "关联邻小区数",
				"关联邻小区数（不含室分）", "所属连通簇数量", "问题连通簇数量", "小区中文名", "经度", "纬度",
				"上行质量", "下行质量", "上行质差", "下行质差", "上行覆盖率", "下行覆盖率", "上行弱覆盖",
				"下行弱覆盖", "平均TA" };
		public static final String[] NCS_REPORT_CELL_RES_SHEET_KEYS = { "cell",
				"FREQ_CNT", "BE_INTERFER", "NET_STRUCT_FACTOR",
				"REDUNT_COVER_FACT", "OVERLAP_COVER", "SRC_INTERFER",
				"OVERSHOOTING_FACT", "DETECT_CNT", "DETECT_CNT_EXINDR",
				"EXPECTED_COVER_DIS", "INTERFER_NCELL_CNT",
				"INTERFER_NCE_CNT_EXINDR", "IN_CLUSTER_CNT", "IN_BAD_CLUSTER_CNT",
				"chineseName", "lng", "lat" , "UL_QUALITY",
				"DL_QUALITY", "UL_POOR_QUALITY", "DL_POOR_QUALITY", "UL_COVERAGE",
				"DL_COVERAGE", "UL_POOR_COVERAGE", "DL_POOR_COVERAGE", "TA_AVERAGE" };
		// ncs连通簇结果标题和数据库key
		public static final String[] NCS_REPORT_CLUSTER_SHEET_TITLES = {
				"ClusterID（簇ID）", "Count（小区数）", "Trxs（频点数/载波数）", "簇约束因子",
				"簇权重", "Sectors（小区列表）" };
		// key
		public static final String[] NCS_REPORT_CLUSTER_SHEET_KEYS = {
				"CLUSTER_ID", "CELLCNT", "TOTAL_FREQ_CNT", "CONTROL_FACTOR",
				"WEIGHT", "SECTORS" };

		// 簇内小区结果标题和数据库key
		public static final String[] NCS_REPORT_CLUSTER_CELL_SHEET_TITLES = {
				"簇ID", "小区名", "小区中文名", "小区载频", "簇TCH载频数" };
		// key
		public static final String[] NCS_REPORT_CLUSTER_CELL_SHEET_KEYS = {
				"CLUSTER_ID", "CELL", "NAME", "FREQ_CNT", "TOTAL_FREQ_CNT" };

		// 簇内干扰关系结果标题和数据库key
		public static final String[] NCS_REPORT_CLUSTER_CELL_RELA_SHEET_TITLES = {
				"主小区", "簇编号", "簇内小区载波数", "干扰小区" };
		public static final String[] NCS_REPORT_CLUSTER_CELL_RELA_SHEET_KEYS = {
				"CELL", "CLUSTER_ID", "TOTAL_FREQ_CNT", "NCELL" };

		// 自动优化建议标题和key
		public static final String[] NCS_REPORT_OPT_SUGGESTION_TITLES = {
				"小区英文名", "小区中文名", "理想覆盖距离", "过覆盖系数", "关联邻小区", "关联邻小区（不含室分）",
				"覆盖分量", "容量分量", "小区经度", "小区纬度", "天线挂高", "天线下倾", "理想下倾", "是否室分",
				"定义信道数", "可用信道数", "载波配置", "最忙时资源利用率", "话务量", "问题标识", "优化建议" };// ,"话统参考时间"};
		public static final String[] NCS_REPORT_OPT_SUGGESTION_KEYS = { "CELL",
				"CELLNAME", "EXPECTED_COVER_DIS", "OVERSHOOTING_FACT",
				"INTERFER_NCELL_CNT", "INTERFER_NCE_CNT_EXINDR", "CELL_COVER",
				"CAPACITY_DESTROY", "LONGITUDE", "LATITUDE", "ANT_HEIGHT",
				"DOWNTILT", "IDEAL_DOWNTILT", "INDOOR_CELL_TYPE",
				"DECLARE_CHANNEL_NUMBER", "AVAILABLE_CHANNEL_NUMBER",
				"CARRIER_NUMBER", "RESOURCE_USE_RATE", "TRAFFIC",
				"PROBLEM_DESC", "MESSAGE" };// ,"STS_TIME"};

		// 报告对应的缓存的datatype
		public static final String CELLRES = "cellres";// 小区结构指标
		public static final String CLUSTER = "cluster";// 连通簇
		public static final String CLUSTERCELL = "clustercell";// 簇内小区
		public static final String CLUSTERCELLRELA = "clustercellrela";// 簇内小区的干扰关系
		public static final String OPTSUGGESTION = "optsuggestion";// 自动优化

		// 存储结构分析文件的命名
		public static final String SINGLE_NCS_FILE = "ncs_cluster_res_";// 单个ncs的结果excel的前缀，后面跟"{ncsId}.xls"共同构成excel的文件名
		public static final String SINGLE_NCS_DOING_FILE = "ncs_cluster_res_doing_";// 用于标识单个ncs正在进行分析的标识后跟"{ncsId}"共同构成文件名称
		public static final String NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX = ".ro";// 输出的供程序读取的ncs报告的后缀。在excel的名称后面加上这个后缀
		public static final String INTERMATRIXIDXSUFFIX = ".interferMatrix.Idx";// 干扰矩阵数据的索引文件
		public static final String INTERMATRIXDATASUFFIX = ".interferMatrix.Data";// 干扰矩阵数据的数据文件

	}

	
	/**
	 * 数据类型
	 * @author brightming
	 *
	 */
	public enum BusinessDataType {
		G2EriNcsData(1, "ERICSSONNCSFILE", "爱立信ncs文件"),
		G2HwNcsData(2, "HUAWEINCSFILE", "华为ncs文件"),
		G2EriMrrData(3, "ERICSSONMRRFILE", "爱立信mrr文件"),
		G2HwMrrData(4,"2GHWMRRFILE","2G华为Mrr文件"),
		G2EriCellData(5,"2GERICELLFILE","2G爱立信小区文件"),
		G2EriCellEnginData(6,"2GERICELLENGINFILE","2G爱立信小区工参文件"),
		G2HwCellData(7,"2GHWCELLFILE","2G华为小区文件"),
		EriFasData(8,"ERICSSONFASFILE","爱立信FAS文件"),
		Eri4GHoData(9,"LTE_ERI_HO_FILE","4G爱立信切换数据文件"),
		Zte4GHoData(10,"LTE_ZTE_HO_FILE","4G中兴切换数据文件"),
		G4EriMrData(11,"ERI4GMRFILE","4G爱立信MR数据文件"),
		G4ZteMrData(12,"ZTE4GMRFILE","4G中兴MR数据文件"),
		G4PciMatrixata(13,"RNO_PCI_PLAN_IMPORT","PCI干扰矩阵数据文件"),
		G4GridData(14,"LTEGRIDFILE","4g网格数据文件"),
		G4DtDataService(15,"4GDTDATASERVICEFILE","4g路测数据业务文件"),
		G4DtSweepFreqData(16,"4GDTSWEEPFREQFILE","4g路测扫频数据文件"),
		G4StsData(17,"4GSTSDATAFILE","4g话统数据文件"),
		G4KpiData(18,"G4KPIFILE","4gkpi数据文件"),
		OTHER(-100, "OTHER",
				"其他");
		int type;
		String code;
		String desc;

		private BusinessDataType(int type, String code, String desc) {
			this.type = type;
			this.code = code;
			this.desc = desc;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public static BusinessDataType getByType(int type) {
			BusinessDataType[] bdts = BusinessDataType.values();
			for (BusinessDataType bdt : bdts) {
				if (bdt.type == type) {
					return bdt;
				}
			}
			return OTHER;
		}

		public static BusinessDataType getByCode(String code) {
			BusinessDataType[] bdts = BusinessDataType.values();
			for (BusinessDataType bdt : bdts) {
				if (bdt.code.equals(code)) {
					return bdt;
				}
			}
			return OTHER;
		}
	}

}
