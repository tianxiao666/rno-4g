package com.iscreate.plat.tools.map;
/**
 * 
 * @filename: MapReturnResult.java
 * @classpath: com.iscreate.plat.tools.map
 * @description: 调用经纬度转换实际地址的百度地图api【geocoder】 转换json格式数据用到
 * @author：yuan.yw
 * @date：Jul 22, 2013 3:52:54 PM
 * @version：
 */
public class MapReturnResult {
	private String status;  
    private MapAddressResult result;  
  
    public String getStatus() {  
        return status;  
    }  
  
    public void setStatus(String status) {  
        this.status = status;  
    }  
  
   
  
    public MapAddressResult getResult() {
		return result;
	}

	public void setResult(MapAddressResult result) {
		this.result = result;
	}

	/*数据格式
	 * {"status":0,
		"result":
		{
		"location":{"lng":116.322987,"lat":39.983424071404},
		"formatted_address":"北京市海淀区中关村大街27号1101-08室",
		"business":"人民大学,中关村,苏州街",
		"addressComponent":
		{"city":"北京市",
		"district":"海淀区",
		"province":"北京市",
		"street":"中关村大街",
		"street_number":"27号1101-08室"},
		"cityCode":131
		}
		}-------------*/
/*	status返回码 定义 
	0             正常 
	1             服务器内部错误 
	2             请求参数非法 
	3             权限校验失败 
	4             配额校验失败 
	5           ak不存在或者非法 
	101          服务禁用 
	102          不通过白名单或者安全码不对 
	2xx         无权限 
	3xx         配额错误 */

}
