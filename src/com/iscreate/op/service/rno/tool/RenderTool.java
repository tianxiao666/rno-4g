package com.iscreate.op.service.rno.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.service.rno.vo.ColorRange;
import java.util.Collections;

public class RenderTool {

	public static int PIC_WIDTH = 1024; //渲染图的宽度
	public static int GRID_UNIT = 150; //渲染图的网格地理长度初始值，单位米
	public static int GRID_LIMIT_NUM = 1000000; //网格数量限制
	public static int SEARCH_DISTANCE = 960000;//最大搜索距离
	public static int POWER = -3;//幂指数
	public static int SPLIT_BLOCK_CNT = 40;//过渡颜色份数
	public static String BaseStationColor="#BD760C"; //基站图标样式
	//渲染进度结果
	private	static List<Map<String, Object>> renderProgress = new ArrayList<Map<String,Object>>();
	
	//渲染颜色规则
	public static Map<String,Double> renderRule = new HashMap<String, Double>();
	static{
		renderRule.put("Blue", 0.0);
		renderRule.put("WBlue", 0.23);
		renderRule.put("Green", 0.4);
		renderRule.put("Yellow", 0.7);
		renderRule.put("Red", 0.9);
	}

	/**
	 * 获取两个颜色的过渡颜色集合
	 * @param startColor 
	 * @param endColor
	 * @param startValue
	 * @param endValue
	 * @param splitBlockCnt
	 * @return  颜色集合
	 * @author peng.jm
	 * 2014年7月10日9:25:57
	 */
	public static List<ColorRange> getGradientColor(String startColor,
			String endColor, double startValue, double endValue,int splitBlockCnt) {
		
		int startR = Integer.parseInt(startColor.substring(1, 3), 16);
		int startG = Integer.parseInt( startColor.substring(3, 5), 16);
		int startB = Integer.parseInt( startColor.substring(5, 7), 16);
//		System.out.println("startColor:  R="+startR+",G="+startG+",B="+startB);
		
		int endR = Integer.parseInt( endColor.substring(1, 3), 16);
		int endG = Integer.parseInt( endColor.substring(3, 5), 16);
		int endB = Integer.parseInt( endColor.substring(5, 7), 16);
//		System.out.println("endColor:  R="+endR+",G="+endG+",B="+endB);
		
		int sR = (endR-startR)/splitBlockCnt;//总差值
	    int sG = (endG-startG)/splitBlockCnt;
	    int sB = (endB-startB)/splitBlockCnt;
	    
	    double sV = (endValue-startValue)/splitBlockCnt;
		
	    List<ColorRange> result = new ArrayList<ColorRange>();
	    ColorRange cr = null;
	    for (int i = 0; i < splitBlockCnt; i++) {
	    	String R = Integer.toHexString(sR*i+startR);
			String G = Integer.toHexString(sG*i+startG);
			String B = Integer.toHexString(sB*i+startB);
			//补位
			if(Integer.parseInt(R, 16) < 16) {
				R = "0"+R;
			}
			if(Integer.parseInt(G, 16) < 16) {
				G = "0"+G;
			}
			if(Integer.parseInt(B, 16) < 16) {
				B = "0"+B;
			}
			String colorStr = R + G + B;
			//int color = Integer.parseInt(colorStr, 16);
			double eachMinValue = sV*i+startValue;
			double eachMaxValue=eachMinValue+sV;
			
			cr = new ColorRange(); 
			cr.setColor(colorStr);

			cr.setMinValue(eachMinValue);
			cr.setMaxValue(eachMaxValue);
			
			//System.out.println("grid" + i + ": color=" + cr.getColor()
			//		+ ",value=" + cr);
			result.add(cr);
		}
		return result;	
	}

	/**
	 * 通过任务id和渲染类型获取渲染进度
	 * @param taskId
	 * @param rendererType
	 * @return
	 */
	public static Map<String, Object> getRenderProgressFromTaskIdAndType(long taskId, String rendererType) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(renderProgress.size() > 0 
				&& renderProgress != null) {
			for (int i = 0; i < renderProgress.size(); i++) {
				long taskIdFromRecord = Long.parseLong(renderProgress.get(i).get("taskId").toString());
				String renderTypeFromRecord = renderProgress.get(i).get("rendererType").toString();
				if(taskId == taskIdFromRecord 
						&& rendererType.equals(renderTypeFromRecord)) {
					result = renderProgress.get(i);
				}
			}
		}
		return result;
	}

	/**
	 * 保存一个渲染任务的进度
	 * 通过taskId与renderType判断是否已存在
	 * @param progress
	 */
	public static void setProgress(Map<String, Object> progress) {
		
		long taskId = Long.parseLong(progress.get("taskId").toString());
		String rendererType = progress.get("rendererType").toString();
		
		if(renderProgress.size() > 0) {
			for (int i = 0; i < renderProgress.size(); i++) {
				long taskIdFromRecord = Long.parseLong(renderProgress.get(i).get("taskId").toString());
				String renderTypeFromRecord = renderProgress.get(i).get("rendererType").toString();
				if(taskId == taskIdFromRecord 
						&& rendererType.equals(renderTypeFromRecord)) {
					renderProgress.set(i, progress);
				}
			}
		} else {
			renderProgress.add(progress);
		}
	}
	
	/**
	 * 清除渲染进度缓存
	 * @param taskId
	 * @param rendererType
	 */
	public static void removeProgress(long taskId, String rendererType) {
		if(renderProgress.size() > 0) {
			for (int i = 0; i < renderProgress.size(); i++) {
				long taskIdFromRecord = Long.parseLong(renderProgress.get(i).get("taskId").toString());
				String renderTypeFromRecord = renderProgress.get(i).get("rendererType").toString();
				if(taskIdFromRecord == taskId
						&& rendererType.equals(renderTypeFromRecord)) {
					renderProgress.remove(i);
				}
			}
		}
		
	}

	
	/**
	 * 删除idw计算结果文件
	 * @param filePath
	 * @param taskId
	 * @param ncsRendererType
	 * @author peng.jm
	 */
	public static void deleteIdwFile(String filePath, Long taskId,
			String ncsRendererType) {
		String idwFile1 = filePath + "/idw_data/idw_data_" + taskId + "_" + ncsRendererType + "_1.txt";
		String idwFile2 = filePath + "/idw_data/idw_data_" + taskId + "_" + ncsRendererType + "_2.txt";
		List<String> idwFilePaths = Arrays.asList(idwFile1, idwFile2);
		for (String path : idwFilePaths) {
			File file = new File(path);
			FileTool.delete(file);
		}
	}

}
