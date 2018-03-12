package com.iscreate.plat.tools.paginghelper;

import java.util.HashMap;
import java.util.Map;

public class PagingHelper {

	/**
	 * 计算分页
	 * @param totalCount （总行数）
	 * @param currentPage （当前页）
	 * @param pageSize （每页的行数）
	 * @return Map<String,Object> pageSize,currentPage,totalPage
	 */
	public Map<String,Object> calculatePagingParamService(int totalCount,int currentPage,int pageSize){
		Map<String,Object> map = new HashMap<String, Object>();
		int totalPage = (totalCount % pageSize > 0) ? (totalCount / pageSize)+1 : totalCount / pageSize;
		if(totalPage == 0){
			totalPage = 1;
		}
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		map.put("pageSize", pageSize);
		map.put("currentPage", currentPage);
		map.put("totalPage", totalPage);
		return map;
	}
}
