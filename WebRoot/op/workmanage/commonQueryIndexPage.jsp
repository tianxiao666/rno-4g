<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.text.SimpleDateFormat,java.util.*,com.iscreate.op.pojo.workmanage.*"%>

<%

	WorkmanageMenu menuEntity = (WorkmanageMenu)request.getAttribute("menu");
	List<Map<String,Object>> input_config_list  = (List<Map<String,Object>>)request.getAttribute("input_config_list");
	List<Map<String,Object>> input_button_list  = (List<Map<String,Object>>)request.getAttribute("input_button_list");
	List<Map<String,Object>> result_config_list=(List<Map<String,Object>>)request.getAttribute("result_config_list");
	List<WorkmanageField> input_field_list = (List<WorkmanageField>)request.getAttribute("input_field_list");
	List<WorkmanageField> result_field_list = (List<WorkmanageField>)request.getAttribute("result_field_list");
	
	//获取菜单信息
	String menuId =menuEntity.getMenuId()==null?null:String.valueOf(menuEntity.getMenuId());
	String menuName =menuEntity.getMenuName()==null?null:String.valueOf(menuEntity.getMenuName());
	String menuType=menuEntity.getMenuType()==null?null:String.valueOf(menuEntity.getMenuType());
	String queryPage=menuEntity.getQueryPage()==null?null:String.valueOf(menuEntity.getQueryPage());
	
	
	String subPage_resultItemId="";
	
	//js引用str
	StringBuilder sb_js=new StringBuilder();

	//查询条件str
	StringBuilder sb_input_config = new StringBuilder();
	
	
	//查询条件中ComboBox的store定义
	StringBuilder inputComboxStoreString = new StringBuilder();
	
	
	//结果项str
	StringBuilder sb_result_config=new StringBuilder();
	
	//结果项tab
	StringBuilder sb_result_tab_config=new StringBuilder();
	
	StringBuilder sb_result_htmltab_config=new StringBuilder();
	StringBuilder sb_result_htmltabbody_config=new StringBuilder();
	StringBuilder sb_result_htmltabhidden_config=new StringBuilder();
	
	
	//结果对象名称，当为TAB时，取TAB对象名;当只为一个GRID时，取GRID对象名
	StringBuilder sb_tabObjectName = new StringBuilder(); ;
	
	StringBuilder sb_gridPanel=new StringBuilder();
	
	
	//结果项控件
	//StringBuilder sb_resultItem_widget=new StringBuilder();
	
	//StringBuilder inputFormObj  = new StringBuilder();
	
	
	StringBuilder sb_menuType=new StringBuilder();
	StringBuilder sb_queryDiv=new StringBuilder();
	StringBuilder sb_isShowOfTab=new StringBuilder();
	
	
	if("workDispatch".equals(menuType)){
		//查询条件config--------------begin-----------------
		
		if(queryPage!=null && !queryPage.isEmpty()){
			sb_queryDiv.append("var queryDivId='"+queryPage+"';");
			sb_menuType.append("var menuType='"+menuType+"';");
			
			//sb_queryDiv.append("$('#"+queryPage+"').show()");
			
		}else{
			
			//存在查询条件输入定义配置信息
			if(input_config_list!=null && input_config_list.size()>0){
				Map<String,Object> inputItem = input_config_list.get(0);
				String queryInputFormId = inputItem.get("queryInputFormId")==null?null:inputItem.get("queryInputFormId").toString();
				String queryLable = inputItem.get("queryLabel")==null?null:inputItem.get("queryLabel").toString();
				
				//组装查询条件输入控件---------- begin-----
				StringBuilder sb_col = new StringBuilder();
				StringBuilder sb_col2 = new StringBuilder();
				StringBuilder sb_col3 = new StringBuilder();
				
				if(input_field_list!=null && !input_field_list.isEmpty()){
					//遍历查询条件field
					for(int i=0;i<input_field_list.size();i++){
						WorkmanageField field=input_field_list.get(i);
						String fieldLabel=field.getFieldLabel();	//字段显示名称
						String fieldName=field.getFieldName();	//字段属性名称
						String fieldOperator=field.getOperator();	//字段操作符
						String fieldType=field.getFieldType();	//字段数据类型
						String nullable=field.getNullable()==null?"":field.getNullable()+"";	//是否为空
						if("<=".equals(fieldOperator)){
							fieldName = fieldName+"_";
						}
						StringBuilder fieldTemp = new StringBuilder();
						if("dateTime".equals(fieldType.trim())){	//时间选择框
							fieldTemp.append("new Ext.form.DateTimeField({\n");
							fieldTemp.append("fieldLabel: '"+fieldLabel+"',\n");
							fieldTemp.append("name: '"+fieldName+"',\n");
							fieldTemp.append("anchor:'85%',\n");
							//fieldTemp.append("width:135,\n");
							fieldTemp.append("time:true,\n");
							fieldTemp.append("value:''\n");
							fieldTemp.append("}\n");
							fieldTemp.append("),\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");	
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");
						}else if("select".equals(fieldType.trim())){	//下列列表
							String field_name_show = field.getFieldNameShow();
							String field_name_value = field.getFieldNameValue();
							String fieldLoadUrl = field.getFieldLoadUrl();
							
							fieldTemp.append("new Ext.form.ComboBox({\n");
							fieldTemp.append("id:'"+fieldName+"_combobox',\n");
							fieldTemp.append("fieldLabel:'"+fieldLabel+"',\n");
							fieldTemp.append("store:"+fieldName+"_store,\n");
							//fieldTemp.append("editable:false,\n");
							fieldTemp.append("anchor:'85%',\n");
							//fieldTemp.append("width:135,\n");
							fieldTemp.append("name:'"+fieldName+"',\n");
							fieldTemp.append("hiddenName:'"+fieldName+"',\n");
							//fieldTemp.append("hiddenName:'cat"+field_name+"',\n");
							//fieldTemp.append("hiddenName:'dog',\n");
							fieldTemp.append("valueField: '"+field_name_value+"',\n");
							fieldTemp.append("displayField: '"+field_name_show+"',\n");
							fieldTemp.append("typeAhead: true,\n");
							fieldTemp.append("mode: 'local',\n");
							fieldTemp.append("forceSelection: true,\n");
							fieldTemp.append("triggerAction: 'all',\n");
							fieldTemp.append("emptyText:'请选择...',\n");
							fieldTemp.append("selectOnFocus:true\n");
							fieldTemp.append("}),\n");
							
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");	
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");
							
							//定义store--------begin----
							inputComboxStoreString.append("var "+fieldName+"_store = new Ext.data.Store({\n");
							inputComboxStoreString.append("id: '"+fieldName+"_store',\n");
							inputComboxStoreString.append("proxy: new Ext.data.HttpProxy({\n");
							inputComboxStoreString.append("url: '"+fieldLoadUrl+"'\n");
							inputComboxStoreString.append("}),\n");
							inputComboxStoreString.append("reader: new Ext.data.JsonReader({\n");
							inputComboxStoreString.append("root: 'result'\n");
							inputComboxStoreString.append("}, [\n");
							inputComboxStoreString.append("{name: '"+field_name_show+"', mapping: '"+field_name_show+"'},\n");
							inputComboxStoreString.append("{name: '"+field_name_value+"', mapping: '"+field_name_value+"'}\n");
							inputComboxStoreString.append("])\n");
							inputComboxStoreString.append("});\n");
							inputComboxStoreString.append(fieldName+"_store.load();\n");
							
							//定义store---------end-----
							
						}else if("selecttree".equals(fieldType.trim())){
							
						}else {	//默认是文本输入框
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'textfield',\n");
							fieldTemp.append("fieldLabel: '"+fieldLabel+"',\n");
							fieldTemp.append("name: '"+fieldName+"',\n");
							fieldTemp.append("anchor:'85%',\n");
							fieldTemp.append("allowBlank:true\n");
							fieldTemp.append("},\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");	
						}
						
						//第1列
						if(i%3==0){
							sb_col.append(fieldTemp);
						}else if(i%3==1){	//第2列
							sb_col2.append(fieldTemp);
						}else if(i%3==2){	//第3列
							sb_col3.append(fieldTemp);
						}
					}
					if(sb_col.length()>0) sb_col.deleteCharAt(sb_col.length()-2);
					if(sb_col2.length()>0) sb_col2.deleteCharAt(sb_col2.length()-2);
					if(sb_col3.length()>0) sb_col3.deleteCharAt(sb_col3.length()-2);
				}
				//组装查询条件输入控件---------- end -----
				
				sb_input_config.append("var "+queryInputFormId+" = new Ext.FormPanel({\n");
				sb_input_config.append("id: '"+queryInputFormId+"',\n");
				sb_input_config.append("labelAlign: 'center',\n");
				//sb_input_config.append("layout:'fit',\n");
				sb_input_config.append("monitorResize:true,\n");
				sb_input_config.append("frame:true,\n");
				sb_input_config.append("labelWidth: 100,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("items: [{\n");
				sb_input_config.append("layout:'column',\n");
				sb_input_config.append("items:[\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col);
				sb_input_config.append("]\n");
				sb_input_config.append("},\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col2);
				sb_input_config.append("]\n");
				sb_input_config.append("},\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col3);
				sb_input_config.append("]\n");
				sb_input_config.append("}\n");
				
				sb_input_config.append("]\n");
				sb_input_config.append("}\n");
				sb_input_config.append("]\n");
				sb_input_config.append("});\n");
				
				sb_input_config.append("var inputForm = new Ext.Panel({\n");
				sb_input_config.append("labelWidth: 75,\n");
				sb_input_config.append("frame:false,\n");
				sb_input_config.append("border:false,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append("{\n");
				sb_input_config.append("xtype:'fieldset',\n");
				sb_input_config.append("checkboxToggle:true,\n");
				sb_input_config.append("title: '查询条件',\n");
				sb_input_config.append("autoHeight:true,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("items:["+queryInputFormId+"],\n");
				
				//查询条件按钮--------------begin--------
				sb_input_config.append("buttons: [\n");
				if(input_button_list!=null&&input_button_list.size()>0){
					for(Map buttonItem:input_button_list){
						String buttonLable = buttonItem.get("buttonLable")==null?null:buttonItem.get("buttonLable").toString();
						String buttionId = buttonItem.get("buttionId")==null?null:buttonItem.get("buttionId").toString();
						String buttonActionScript = buttonItem.get("buttonActionScript")==null?null:buttonItem.get("buttonActionScript").toString();
						String buttonActionFunction = buttonItem.get("buttonActionFunction")==null?null:buttonItem.get("buttonActionFunction").toString();
						String isDisplay = buttonItem.get("isDisplay")==null?null:buttonItem.get("isDisplay").toString();
						String isDisabled = buttonItem.get("isDisabled")==null?null:buttonItem.get("isDisabled").toString();
						
						if(sb_js.indexOf("src=\""+buttonActionScript+"\"")<0){		
							if(sb_js!=null&&!"".equals(buttonActionScript)){
								sb_js.append("<script src=\""+buttonActionScript+"\" type=\"text/javascript\"></script>\n");
							}		
						}
						
						sb_input_config.append("{\n");
						sb_input_config.append("id: '"+buttionId+"',\n");
						
						if("1".equalsIgnoreCase(isDisabled.trim())){
							sb_input_config.append("disabled:true,\n");
						}else{
							sb_input_config.append("disabled:false,\n");
						}
						
						if("1".equalsIgnoreCase(isDisplay.trim())){
							sb_input_config.append("hidden:false,\n");
						}else{
							sb_input_config.append("hidden:true,\n");
						}
						
						sb_input_config.append("text: '"+buttonLable+"',\n");
						sb_input_config.append("handler: function(){\n");
						sb_input_config.append(buttonActionFunction+"();\n");
						sb_input_config.append("}\n");
						sb_input_config.append("},\n");
					}
					if(sb_input_config.length()>0){
						sb_input_config.delete(sb_input_config.length()-2,sb_input_config.length());
					}
				}	
				sb_input_config.append("]\n");
				//查询条件按钮--------------end--------
				
				sb_input_config.append("}\n");
				sb_input_config.append("]\n");
				sb_input_config.append("});\n");
			}
		}
		
		
		
		//查询条件config--------------end-----------------
		
		
		//查询结果config-----------begin------
		//StringBuilder sb_result = new StringBuilder();
		if(result_config_list!=null&&result_config_list.size()>0){
			//如果结果项数量大于1，以tab显示结果项
			if(result_config_list.size()>1){
				sb_isShowOfTab.append("var isShowOfTab=true;");
			}else{
				String resultItemId = null;
				String resultItemType=null;
				String queryEntityName = null;
				String resultField=null;
				String queryAction=null;
				String queryCondition=null;
				String autoQueryTime=null;
				String sortName = null;
				String sortType = null;
				String resultLimit=null;
				String resultItemScript=null;
				String resultRowDbClickFunc=null;
				String resultRowViewFunc=null;
				String filterBiz=null;
				String filterProp=null;
				
				sb_isShowOfTab.append("var isShowOfTab=false;");
				
				for(int i=0;i<result_config_list.size();i++){
					Map<String,Object> resultItem = result_config_list.get(i);
					resultItemId=resultItem.get("resultItemId")==null?null:resultItem.get("resultItemId").toString();
					resultItemType=resultItem.get("resultItemType")==null?null:resultItem.get("resultItemType").toString();
					queryEntityName=resultItem.get("queryEntityName")==null?null:resultItem.get("queryEntityName").toString();
					resultField=resultItem.get("resultField")==null?null:resultItem.get("resultField").toString();
					queryAction=resultItem.get("queryAction")==null?null:resultItem.get("queryAction").toString();
					queryCondition=resultItem.get("queryCondition")==null?null:resultItem.get("queryCondition").toString();
					autoQueryTime=resultItem.get("autoQueryTime")==null?null:resultItem.get("autoQueryTime").toString();
					sortName=resultItem.get("sortName")==null?null:resultItem.get("sortName").toString();
					sortType=resultItem.get("sortType")==null?null:resultItem.get("sortType").toString();
					resultLimit=resultItem.get("resultLimit")==null?null:resultItem.get("resultLimit").toString();
					resultItemScript=resultItem.get("resultItemScript")==null?null:resultItem.get("resultItemScript").toString();
					resultRowDbClickFunc=resultItem.get("resultRowDbClickFunc")==null?null:resultItem.get("resultRowDbClickFunc").toString();
					resultRowViewFunc=resultItem.get("resultRowViewFunc")==null?null:resultItem.get("resultRowViewFunc").toString();
					filterBiz=resultItem.get("filterBiz")==null?null:resultItem.get("filterBiz").toString();
					filterProp=resultItem.get("filterProp")==null?null:resultItem.get("filterProp").toString();
					
					if(resultItemScript!=null && !resultItemScript.isEmpty()){
						sb_js.append("<script src=\""+resultItemScript+"\" type=\"text/javascript\"></script>\n");
					}
					
					if(resultItemType!=null && !resultItemType.isEmpty() && "grid".equals(resultItemType)){		//如果结果项为grid
						
						subPage_resultItemId=subPage_resultItemId+"\""+(resultItemId)+"\"";
						sb_result_config.append("var "+resultItemId+"_select = new Ext.grid.CheckboxSelectionModel();\n");
						sb_result_config.append("var "+resultItemId+"_dataUrl = '"+queryAction+"?queryCondition="+queryCondition+"&queryEntityName="+queryEntityName+"&filterBiz="+filterBiz+"&filterProp="+filterProp+"&menuType="+menuType+"';\n");
						sb_result_config.append("var "+resultItemId+"_store = new Ext.data.JsonStore({\n");
						sb_result_config.append("url: "+resultItemId+"_dataUrl,\n");
						sb_result_config.append("root: 'result',");
						sb_result_config.append("totalProperty: 'totalCount',\n");
						sb_result_config.append("remoteSort: true,");
						sb_result_config.append("fields: ['_NOWTIME','_HALFLATERTIME',"+resultField+"]\n");
						sb_result_config.append("});\n");
						
						//默认排序
						if(sortName!=null && sortType!=null){
							sb_result_config.append(resultItemId+"_store.setDefaultSort('"+sortName+"', '"+sortType.toLowerCase()+"');\n");
						}
						
						sb_gridPanel.append("var gridPanel_grid='"+resultItemId+"_grid';");
						sb_result_config.append("var "+resultItemId+"_grid = new Ext.grid.GridPanel({\n");
						sb_result_config.append("id:'"+resultItemId+"_grid',\n");
						sb_result_config.append("height:315,\n");//工单管理数据结果框的高度
						//sb_result_config.append("autoHeight:true,\n");
						//sb_result_config.append("width:width-20,\n");
						sb_result_config.append("width:width,\n");
						//sb_result_config.append("region:'center',\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("padding: '5 5 5 5',");
						sb_result_config.append("trackMouseOver:true,\n");
						sb_result_config.append("disableSelection:false,\n");
						sb_result_config.append("loadMask: true,\n");
						sb_result_config.append("sm:"+resultItemId+"_select,\n");
						sb_result_config.append("columns:[\n");
						//sb_result_config.append("new Ext.grid.RowNumberer(),"+resultItemId+"_select,\n");
						sb_result_config.append("new Ext.grid.RowNumberer(),\n");
						
						//构造gridPanel的列样式
						if(result_field_list!=null && result_field_list.size()>0){
							for(int k=0;k<result_field_list.size();k++){
								WorkmanageField field = result_field_list.get(k);				
								sb_result_config.append("{\n");
								sb_result_config.append("id: '"+field.getFieldName()+"',\n");
								sb_result_config.append("dataIndex: '"+field.getFieldName()+"',\n");
								sb_result_config.append("header: '"+field.getFieldLabel()+"',\n");
								if(field.getWidth()!=null && field.getWidth().intValue()!=0){
									sb_result_config.append("width: "+field.getWidth()+",\n");
								}else{
									sb_result_config.append("width: 150,\n");
								}	
								
								if(field.getRendererJs()!=null && !"".equals(field.getRendererJs())){
									sb_result_config.append("renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){\n");						
									sb_result_config.append(field.getRendererJs()+"(value,cellmeta,record,rowIndex,columnIndex,store);");
									sb_result_config.append("},\n");
								}
								
								sb_result_config.append("align: 'center',\n");
								sb_result_config.append("sortable: true\n");
								sb_result_config.append("},\n");
							}
						}
						
						
						if(sb_result_config.length()>0){
							sb_result_config.deleteCharAt(sb_result_config.length()-2);
						}
						
						sb_result_config.append("],\n");
						sb_result_config.append("bbar: new Ext.PagingToolbar({\n");
						sb_result_config.append("pageSize: 10,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("displayInfo: true,\n");
						sb_result_config.append("displayMsg: '显示 {0} - {1} of {2}',\n");
						sb_result_config.append("emptyMsg: '没有查询到记录'\n");
						sb_result_config.append("}),\n");
						
						//----row记录行样式控制----begin---------
						sb_result_config.append("viewConfig : {\n");
						sb_result_config.append("forceFit:true,\n");
						sb_result_config.append("enableRowBody : true,\n");
						sb_result_config.append("getRowClass :function(record, rowIndex, p, ds) {\n");
						if(resultRowViewFunc!=null&&!"".equals(resultRowViewFunc)){
							sb_result_config.append("return "+resultRowViewFunc+"(record, rowIndex, p, ds);");
						}
						sb_result_config.append("}");
						sb_result_config.append("},");		
						//----row记录行样式控制----end---------
						
						//监听事件
						sb_result_config.append("listeners : {\n");
						sb_result_config.append("'rowdblclick':function(grid, rowIndex,e){\n");
						if(resultRowDbClickFunc!=null && !"".equals(resultRowDbClickFunc)){
							sb_result_config.append(resultRowDbClickFunc+"(grid,rowIndex,e);");
						};
						sb_result_config.append("}\n");
						sb_result_config.append("}\n");
						sb_result_config.append("});\n");
						//配置分页
						if(resultLimit==null||"".equals(resultLimit)){
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\",1000*60*5);\n");
							}
						}else{
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");					
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");	
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\","+autoQueryTime+");\n");
							}				
						}
						
						//sb_resultItem_widget.append(resultItemId+"_grid");
					}
				}
				
				//构造form，装载控件
				sb_result_config.append("var viewPortForm = new Ext.Panel({\n");
				sb_result_config.append("labelWidth: 75,\n");
				sb_result_config.append("frame:false,\n");
				sb_result_config.append("border:false,\n");
				sb_result_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_result_config.append("items: [\n");
				sb_result_config.append("\n");
				//sb_result_config.append("{border:false,items:[{border:false,html:\"<iframe style='width: 100%; height:40px; border:0px;'  src='op/workmanage/orderTips.html' ></iframe>\"}]},\n");
				sb_result_config.append(resultItemId+"_grid\n");
				sb_result_config.append("]\n");
				sb_result_config.append("});\n");
			}
		}
		//查询结果config-----------end------
	}else if("carDispatch".equals(menuType)){
		//查询条件config--------------begin-----------------
		if(queryPage!=null && !queryPage.isEmpty()){
			sb_queryDiv.append("var queryDivId='"+queryPage+"';");
			sb_menuType.append("var menuType='"+menuType+"';");
			//sb_input_config.append("var inputForm = new Ext.Panel({\n");
			//sb_input_config.append("labelWidth: 75,\n");
			//sb_input_config.append("frame:false,\n");
			//sb_input_config.append("height:50,\n");
			//sb_input_config.append("border:false,\n");
			//sb_input_config.append("buttonAlign:'center',\n");
			//sb_input_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
			//sb_input_config.append("items: [\n");
			//sb_input_config.append("{border:false,html:\"<iframe style='width: 100%; border:0px;'  src='"+queryPage+"' ></iframe>\"}");
			//sb_input_config.append("]\n");
			//sb_input_config.append("});\n");
		}else{
			//存在查询条件输入定义配置信息
			if(input_config_list!=null && input_config_list.size()>0){
				Map<String,Object> inputItem = input_config_list.get(0);
				String queryInputFormId = inputItem.get("queryInputFormId")==null?null:inputItem.get("queryInputFormId").toString();
				String queryLable = inputItem.get("queryLabel")==null?null:inputItem.get("queryLabel").toString();
				
				//组装查询条件输入控件---------- begin-----
				StringBuilder sb_col = new StringBuilder();
				StringBuilder sb_col2 = new StringBuilder();
				StringBuilder sb_col3 = new StringBuilder();
				
				if(input_field_list!=null && !input_field_list.isEmpty()){
					//遍历查询条件field
					for(int i=0;i<input_field_list.size();i++){
						WorkmanageField field=input_field_list.get(i);
						String fieldLabel=field.getFieldLabel();	//字段显示名称
						String fieldName=field.getFieldName();	//字段属性名称
						String fieldOperator=field.getOperator();	//字段操作符
						String fieldType=field.getFieldType();	//字段数据类型
						String nullable=field.getNullable()==null?"":field.getNullable()+"";	//是否为空
						if("<=".equals(fieldOperator)){
							fieldName = fieldName+"_";
						}
						StringBuilder fieldTemp = new StringBuilder();
						if("dateTime".equals(fieldType.trim())){	//时间选择框
							fieldTemp.append("new Ext.form.DateTimeField({\n");
							fieldTemp.append("fieldLabel: '"+fieldLabel+"',\n");
							fieldTemp.append("name: '"+fieldName+"',\n");
							fieldTemp.append("anchor:'85%',\n");
							//fieldTemp.append("width:135,\n");
							fieldTemp.append("time:true,\n");
							fieldTemp.append("value:''\n");
							fieldTemp.append("}\n");
							fieldTemp.append("),\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");	
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");
						}else if("select".equals(fieldType.trim())){	//下列列表
							String field_name_show = field.getFieldNameShow();
							String field_name_value = field.getFieldNameValue();
							String fieldLoadUrl = field.getFieldLoadUrl();
							
							fieldTemp.append("new Ext.form.ComboBox({\n");
							fieldTemp.append("id:'"+fieldName+"_combobox',\n");
							fieldTemp.append("fieldLabel:'"+fieldLabel+"',\n");
							fieldTemp.append("store:"+fieldName+"_store,\n");
							//fieldTemp.append("editable:false,\n");
							fieldTemp.append("anchor:'85%',\n");
							//fieldTemp.append("width:135,\n");
							fieldTemp.append("name:'"+fieldName+"',\n");
							fieldTemp.append("hiddenName:'"+fieldName+"',\n");
							//fieldTemp.append("hiddenName:'cat"+field_name+"',\n");
							//fieldTemp.append("hiddenName:'dog',\n");
							fieldTemp.append("valueField: '"+field_name_value+"',\n");
							fieldTemp.append("displayField: '"+field_name_show+"',\n");
							fieldTemp.append("typeAhead: true,\n");
							fieldTemp.append("mode: 'local',\n");
							fieldTemp.append("forceSelection: true,\n");
							fieldTemp.append("triggerAction: 'all',\n");
							fieldTemp.append("emptyText:'请选择...',\n");
							fieldTemp.append("selectOnFocus:true\n");
							fieldTemp.append("}),\n");
							
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");	
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");
							
							//定义store--------begin----
							inputComboxStoreString.append("var "+fieldName+"_store = new Ext.data.Store({\n");
							inputComboxStoreString.append("id: '"+fieldName+"_store',\n");
							inputComboxStoreString.append("proxy: new Ext.data.HttpProxy({\n");
							inputComboxStoreString.append("url: '"+fieldLoadUrl+"'\n");
							inputComboxStoreString.append("}),\n");
							inputComboxStoreString.append("reader: new Ext.data.JsonReader({\n");
							inputComboxStoreString.append("root: 'result'\n");
							inputComboxStoreString.append("}, [\n");
							inputComboxStoreString.append("{name: '"+field_name_show+"', mapping: '"+field_name_show+"'},\n");
							inputComboxStoreString.append("{name: '"+field_name_value+"', mapping: '"+field_name_value+"'}\n");
							inputComboxStoreString.append("])\n");
							inputComboxStoreString.append("});\n");
							inputComboxStoreString.append(fieldName+"_store.load();\n");
							
							//定义store---------end-----
							
						}else if("selecttree".equals(fieldType.trim())){
							
						}else {	//默认是文本输入框
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'textfield',\n");
							fieldTemp.append("fieldLabel: '"+fieldLabel+"',\n");
							fieldTemp.append("name: '"+fieldName+"',\n");
							fieldTemp.append("anchor:'85%',\n");
							fieldTemp.append("allowBlank:true\n");
							fieldTemp.append("},\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_operator',\n");
							fieldTemp.append("value:'"+fieldOperator+"'\n");
							fieldTemp.append("},\n");
							fieldTemp.append("{\n");
							fieldTemp.append("xtype:'hidden',\n");
							fieldTemp.append("name: '"+fieldName+"_type',\n");
							fieldTemp.append("value:'"+fieldType+"'\n");
							fieldTemp.append("},\n");	
						}
						
						//第1列
						if(i%3==0){
							sb_col.append(fieldTemp);
						}else if(i%3==1){	//第2列
							sb_col2.append(fieldTemp);
						}else if(i%3==2){	//第3列
							sb_col3.append(fieldTemp);
						}
					}
					if(sb_col.length()>0) sb_col.deleteCharAt(sb_col.length()-2);
					if(sb_col2.length()>0) sb_col2.deleteCharAt(sb_col2.length()-2);
					if(sb_col3.length()>0) sb_col3.deleteCharAt(sb_col3.length()-2);
				}
				//组装查询条件输入控件---------- end -----
				
				sb_input_config.append("var "+queryInputFormId+" = new Ext.FormPanel({\n");
				sb_input_config.append("id: '"+queryInputFormId+"',\n");
				//sb_input_config.append("width: width,\n");
				sb_input_config.append("autoHeight:true,\n");
				sb_input_config.append("labelAlign: 'center',\n");
				//sb_input_config.append("layout:'fit',\n");
				sb_input_config.append("monitorResize:true,\n");
				sb_input_config.append("frame:true,\n");
				sb_input_config.append("labelWidth: 100,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("items: [{\n");
				sb_input_config.append("layout:'column',\n");
				sb_input_config.append("items:[\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col);
				sb_input_config.append("]\n");
				sb_input_config.append("},\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col2);
				sb_input_config.append("]\n");
				sb_input_config.append("},\n");
				
				sb_input_config.append("{\n");
				sb_input_config.append("columnWidth:.333,\n");
				sb_input_config.append("layout: 'form',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append(sb_col3);
				sb_input_config.append("]\n");
				sb_input_config.append("}\n");
				
				sb_input_config.append("]\n");
				sb_input_config.append("}\n");
				sb_input_config.append("]\n");
				sb_input_config.append("});\n");
				
				sb_input_config.append("var inputForm = new Ext.Panel({\n");
				sb_input_config.append("labelWidth: 75,\n");
				//sb_input_config.append("width: width,\n");
				//sb_input_config.append("autoWidth: true,\n");
				//sb_input_config.append("frame:true,\n");
				sb_input_config.append("border:false,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_input_config.append("items: [\n");
				sb_input_config.append("{\n");
				sb_input_config.append("xtype:'fieldset',\n");
				sb_input_config.append("checkboxToggle:true,\n");
				sb_input_config.append("title: '查询条件',\n");
				//sb_input_config.append("width: width,\n");
				sb_input_config.append("autoHeight:true,\n");
				sb_input_config.append("buttonAlign:'center',\n");
				sb_input_config.append("items:["+queryInputFormId+"],\n");
				
				//查询条件按钮--------------begin--------
				sb_input_config.append("buttons: [\n");
				if(input_button_list!=null&&input_button_list.size()>0){
					for(Map buttonItem:input_button_list){
						String buttonLable = buttonItem.get("buttonLable")==null?null:buttonItem.get("buttonLable").toString();
						String buttionId = buttonItem.get("buttionId")==null?null:buttonItem.get("buttionId").toString();
						String buttonActionScript = buttonItem.get("buttonActionScript")==null?null:buttonItem.get("buttonActionScript").toString();
						String buttonActionFunction = buttonItem.get("buttonActionFunction")==null?null:buttonItem.get("buttonActionFunction").toString();
						String isDisplay = buttonItem.get("isDisplay")==null?null:buttonItem.get("isDisplay").toString();
						String isDisabled = buttonItem.get("isDisabled")==null?null:buttonItem.get("isDisabled").toString();
						
						if(sb_js.indexOf("src=\""+buttonActionScript+"\"")<0){		
							if(sb_js!=null&&!"".equals(buttonActionScript)){
								sb_js.append("<script src=\""+buttonActionScript+"\" type=\"text/javascript\"></script>\n");
							}		
						}
						
						sb_input_config.append("{\n");
						sb_input_config.append("id: '"+buttionId+"',\n");
						
						if("1".equalsIgnoreCase(isDisabled.trim())){
							sb_input_config.append("disabled:true,\n");
						}else{
							sb_input_config.append("disabled:false,\n");
						}
						
						if("1".equalsIgnoreCase(isDisplay.trim())){
							sb_input_config.append("hidden:false,\n");
						}else{
							sb_input_config.append("hidden:true,\n");
						}
						
						sb_input_config.append("text: '"+buttonLable+"',\n");
						sb_input_config.append("handler: function(){\n");
						sb_input_config.append(buttonActionFunction+"();\n");
						sb_input_config.append("}\n");
						sb_input_config.append("},\n");
					}
					if(sb_input_config.length()>0){
						sb_input_config.delete(sb_input_config.length()-2,sb_input_config.length());
					}
				}	
				sb_input_config.append("]\n");
				//查询条件按钮--------------end--------
				
				sb_input_config.append("}\n");
				sb_input_config.append("]\n");
				sb_input_config.append("});\n");
			}
		}
		
		
		//查询条件config--------------end-----------------
		
		
		//查询结果config-----------begin------
		//StringBuilder sb_result = new StringBuilder();
		if(result_config_list!=null && result_config_list.size()>0){
			
			String resultItemId = null;
			String resultItemType=null;
			String resultItemTabLabel=null;
			String queryEntityName = null;
			String resultField=null;
			String queryAction=null;
			String queryCondition=null;
			String autoQueryTime=null;
			String sortName = null;
			String sortType = null;
			String resultLimit=null;
			String resultItemScript=null;
			String resultRowDbClickFunc=null;
			String resultRowViewFunc=null;
			String filterBiz=null;
			
			//如果结果项数量大于1，以tab显示结果项
			if(result_config_list.size()>1){
				sb_isShowOfTab.append("var isShowOfTab=true;");
				subPage_resultItemId=subPage_resultItemId+"\""+(resultItemId)+"\"";
				
				String resultId="result";	//hardcode
				String resultActiveTab="0";
				
				sb_tabObjectName=sb_tabObjectName.append(resultId+"_tab");
				
				//结果集TAB---------begin---------
				//sb_gridPanel.append("var gridPanel_grid='"+resultId+"_tab';");
				sb_result_tab_config.append("var "+resultId+"_tab = new Ext.TabPanel({\n");
				sb_result_tab_config.append("id:'"+resultId+"_tab',\n");
				//sb_result_tab_config.append("height:390,\n");
				sb_result_tab_config.append("resizeTabs:true,\n");
				sb_result_tab_config.append("enableTabScroll:true,\n");
				//sb_result_tab_config.append("defaults: {autoScroll:true},\n");
				sb_result_tab_config.append("activeTab: "+resultActiveTab+",\n");
				sb_result_tab_config.append("listeners:{\n");
				sb_result_tab_config.append("'tabchange':function(tabPanel){\n");
				sb_result_tab_config.append("changeGridWidth(tabPanel);\n");
				sb_result_tab_config.append("}\n");
				sb_result_tab_config.append("},\n");
				sb_result_tab_config.append("items:[\n");
				
				//sb_result_htmltab_config.append("var tabTitle=$(\"<div id='tab' class='tab_menu'></div>\")");
				
				sb_result_htmltab_config.append("\"");
				sb_result_htmltabbody_config.append("\"");
				//结果集TAB---------end---------
				
				for(int i=0;i<result_config_list.size();i++){
					Map<String,Object> resultItem = result_config_list.get(i);
					resultItemId=resultItem.get("resultItemId")==null?null:resultItem.get("resultItemId").toString();
					resultItemType=resultItem.get("resultItemType")==null?null:resultItem.get("resultItemType").toString();
					resultItemTabLabel=resultItem.get("resultItemTabLabel")==null?null:resultItem.get("resultItemTabLabel").toString();
					queryEntityName=resultItem.get("queryEntityName")==null?null:resultItem.get("queryEntityName").toString();
					resultField=resultItem.get("resultField")==null?null:resultItem.get("resultField").toString();
					queryAction=resultItem.get("queryAction")==null?null:resultItem.get("queryAction").toString();
					queryCondition=resultItem.get("queryCondition")==null?null:resultItem.get("queryCondition").toString();
					autoQueryTime=resultItem.get("autoQueryTime")==null?null:resultItem.get("autoQueryTime").toString();
					sortName=resultItem.get("sortName")==null?null:resultItem.get("sortName").toString();
					sortType=resultItem.get("sortType")==null?null:resultItem.get("sortType").toString();
					resultLimit=resultItem.get("resultLimit")==null?null:resultItem.get("resultLimit").toString();
					resultItemScript=resultItem.get("resultItemScript")==null?null:resultItem.get("resultItemScript").toString();
					resultRowDbClickFunc=resultItem.get("resultRowDbClickFunc")==null?null:resultItem.get("resultRowDbClickFunc").toString();
					resultRowViewFunc=resultItem.get("resultRowViewFunc")==null?null:resultItem.get("resultRowViewFunc").toString();
					filterBiz=resultItem.get("filterBiz")==null?null:resultItem.get("filterBiz").toString();
					
					
					if(resultItemScript!=null && !resultItemScript.isEmpty()){
						sb_js.append("<script src=\""+resultItemScript+"\" type=\"text/javascript\"></script>\n");
					}
					
					if(resultItemType!=null && !resultItemType.isEmpty() && "grid".equals(resultItemType)){		//如果结果项为grid
						sb_result_config.append("var "+resultItemId+"_select = new Ext.grid.CheckboxSelectionModel();\n");
						sb_result_config.append("var "+resultItemId+"_dataUrl = '"+queryAction+"?queryCondition="+queryCondition+"&queryEntityName="+queryEntityName+"&filterBiz=true&filterProp=creatorOrgId&menuType="+menuType+"';\n");
						sb_result_config.append("var "+resultItemId+"_store = new Ext.data.JsonStore({\n");
						sb_result_config.append("url: "+resultItemId+"_dataUrl,\n");
						sb_result_config.append("root: 'result',");
						sb_result_config.append("totalProperty: 'totalCount',\n");
						sb_result_config.append("remoteSort: true,");
						sb_result_config.append("fields: ['_NOWTIME','_HALFLATERTIME',"+resultField+"]\n");
						sb_result_config.append("});\n");
						
						//默认排序
						if(sortName!=null && sortType!=null){
							sb_result_config.append(resultItemId+"_store.setDefaultSort('"+sortName+"', '"+sortType.toLowerCase()+"');\n");
						}
						//sb_gridPanel.append("var gridPanel_grid='"+resultItemId+"_grid';");
						sb_result_config.append("var "+resultItemId+"_grid = new Ext.grid.GridPanel({\n");
						sb_result_config.append("id:'"+resultItemId+"_grid',\n");
						sb_result_config.append("renderTo:'tab_"+i+"',\n");
						//sb_result_config.append("renderTo:'tab"+i+"',\n");
						//sb_result_config.append("width:width-20,\n");
						//sb_result_config.append("width:width+15,\n");
						sb_result_config.append("width:width,\n");
						sb_result_config.append("height:333,\n");//车辆管理数据结果框的高度
						//sb_result_config.append("autoHeight:true,\n");
						
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("padding: '5 5 5 5',");
						sb_result_config.append("trackMouseOver:true,\n");
						sb_result_config.append("disableSelection:false,\n");
						sb_result_config.append("loadMask: true,\n");
						sb_result_config.append("sm:"+resultItemId+"_select,\n");
						sb_result_config.append("columns:[\n");
						//sb_result_config.append("new Ext.grid.RowNumberer(),"+resultItemId+"_select,\n");
						sb_result_config.append("new Ext.grid.RowNumberer(),\n");
						
						
						
						//构造gridPanel的列样式
						if(result_field_list!=null && result_field_list.size()>0){
							for(int k=0;k<result_field_list.size();k++){
								WorkmanageField field = result_field_list.get(k);				
								sb_result_config.append("{\n");
								sb_result_config.append("id: '"+field.getFieldName()+"',\n");
								sb_result_config.append("dataIndex: '"+field.getFieldName()+"',\n");
								sb_result_config.append("header: '"+field.getFieldLabel()+"',\n");
								if(field.getWidth()!=null && field.getWidth().intValue()!=0){
									sb_result_config.append("width: "+field.getWidth()+",\n");
								}else{
									sb_result_config.append("width: 150,\n");
								}	
								
								if(field.getRendererJs()!=null && !"".equals(field.getRendererJs())){
									sb_result_config.append("renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){\n");						
									sb_result_config.append(field.getRendererJs()+"(value,cellmeta,record,rowIndex,columnIndex,store);");
									sb_result_config.append("},\n");
								}
								
								sb_result_config.append("align: 'center',\n");
								sb_result_config.append("sortable: true\n");
								sb_result_config.append("},\n");
							}
						}
						
						
						if(sb_result_config.length()>0){
							sb_result_config.deleteCharAt(sb_result_config.length()-2);
						}
						
						sb_result_config.append("],\n");
						sb_result_config.append("bbar: new Ext.PagingToolbar({\n");
						sb_result_config.append("pageSize: 10,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("displayInfo: true,\n");
						sb_result_config.append("displayMsg: '显示 {0} - {1} of {2}',\n");
						sb_result_config.append("emptyMsg: '没有查询到记录'\n");
						sb_result_config.append("}),\n");
						
						//----row记录行样式控制----begin---------
						sb_result_config.append("viewConfig : {\n");
						sb_result_config.append("forceFit : false,\n");
						sb_result_config.append("enableRowBody : true,\n");
						sb_result_config.append("getRowClass :function(record, rowIndex, p, ds) {\n");
						if(resultRowViewFunc!=null&&!"".equals(resultRowViewFunc)){
							sb_result_config.append("return "+resultRowViewFunc+"(record, rowIndex, p, ds);");
						}
						sb_result_config.append("}");
						sb_result_config.append("},");		
						//----row记录行样式控制----end---------
						
						//监听事件
						sb_result_config.append("listeners : {\n");
						sb_result_config.append("'rowdblclick':function(grid, rowIndex,e){\n");
						if(resultRowDbClickFunc!=null && !"".equals(resultRowDbClickFunc)){
							sb_result_config.append(resultRowDbClickFunc+"(grid,rowIndex,e);");
						};
						sb_result_config.append("}\n");
						sb_result_config.append("}\n");
						sb_result_config.append("});\n");
						//配置分页
						if(resultLimit==null||"".equals(resultLimit)){
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\",1000*60*5);\n");
							}
						}else{
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								//sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
							}else{
								//sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\","+autoQueryTime+");\n");
							}				
						}
						
						//String resultItemLable="ccc";	//hardcode
						
						sb_result_tab_config.append("{\n");
						sb_result_tab_config.append("title: '"+resultItemTabLabel+"',\n");
						sb_result_tab_config.append("id:'"+resultItemId+"_tab',\n");
						sb_result_tab_config.append("items:["+resultItemId+"_grid]\n");
						sb_result_tab_config.append("},\n");
						
						
						//组装htmlTab
						if(i==0){
							sb_result_htmltab_config.append("<li class='selected' style='border-left: 1px solid #99BBE8;'>"+resultItemTabLabel+"</li>");
							//sb_result_htmltabbody_config.append("<div id='tab_"+i+"'>"+resultItemId+"</div>");
						}else{
							sb_result_htmltab_config.append("<li>"+resultItemTabLabel+"</li>");
							//sb_result_htmltabbody_config.append("<div id='tab_"+i+"' style='display:none;'>"+resultItemId+"</div>");
						}
						sb_result_htmltabhidden_config.append("$(\"#tab_"+i+" input\").val(\""+resultItemId+"\");\n");
					}
				}
				
				sb_result_htmltab_config.append("\"");
				sb_result_htmltabbody_config.append("\"");
				
				if(sb_result_tab_config.length()>0){
					sb_result_tab_config.deleteCharAt(sb_result_tab_config.length()-2);
				}
				sb_result_tab_config.append("]\n");
				sb_result_tab_config.append("});");
				
				
				sb_result_config.append(sb_result_tab_config);
				
				
				//构造form，装载控件
				sb_result_config.append("var viewPortForm = new Ext.Panel({\n");
				sb_result_config.append("labelWidth: 75,\n");
				//sb_result_config.append("frame:true,\n");
				sb_result_config.append("border:false,\n");
				sb_result_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_result_config.append("items: [\n");
				//sb_result_config.append("\n");
				//sb_result_config.append(sb_tabObjectName+"\n");	//渲染tabPanel
				sb_result_config.append("]\n");
				sb_result_config.append("});\n");
			}else{
				sb_isShowOfTab.append("var isShowOfTab=false;");
				for(int i=0;i<result_config_list.size();i++){
					Map<String,Object> resultItem = result_config_list.get(i);
					resultItemId=resultItem.get("resultItemId")==null?null:resultItem.get("resultItemId").toString();
					resultItemType=resultItem.get("resultItemType")==null?null:resultItem.get("resultItemType").toString();
					queryEntityName=resultItem.get("queryEntityName")==null?null:resultItem.get("queryEntityName").toString();
					resultField=resultItem.get("resultField")==null?null:resultItem.get("resultField").toString();
					queryAction=resultItem.get("queryAction")==null?null:resultItem.get("queryAction").toString();
					queryCondition=resultItem.get("queryCondition")==null?null:resultItem.get("queryCondition").toString();
					autoQueryTime=resultItem.get("autoQueryTime")==null?null:resultItem.get("autoQueryTime").toString();
					sortName=resultItem.get("sortName")==null?null:resultItem.get("sortName").toString();
					sortType=resultItem.get("sortType")==null?null:resultItem.get("sortType").toString();
					resultLimit=resultItem.get("resultLimit")==null?null:resultItem.get("resultLimit").toString();
					resultItemScript=resultItem.get("resultItemScript")==null?null:resultItem.get("resultItemScript").toString();
					resultRowDbClickFunc=resultItem.get("resultRowDbClickFunc")==null?null:resultItem.get("resultRowDbClickFunc").toString();
					resultRowViewFunc=resultItem.get("resultRowViewFunc")==null?null:resultItem.get("resultRowViewFunc").toString();
					filterBiz=resultItem.get("filterBiz")==null?null:resultItem.get("filterBiz").toString();
					
					
					if(resultItemScript!=null && !resultItemScript.isEmpty()){
						sb_js.append("<script src=\""+resultItemScript+"\" type=\"text/javascript\"></script>\n");
					}
					
					if(resultItemType!=null && !resultItemType.isEmpty() && "grid".equals(resultItemType)){		//如果结果项为grid
						
						subPage_resultItemId=subPage_resultItemId+"\""+(resultItemId)+"\"";
						sb_result_config.append("var "+resultItemId+"_select = new Ext.grid.CheckboxSelectionModel();\n");
						sb_result_config.append("var "+resultItemId+"_dataUrl = '"+queryAction+"?queryCondition="+queryCondition+"&queryEntityName="+queryEntityName+"&filterBiz=true&filterProp=creatorOrgId&menuType="+menuType+"';\n");
						sb_result_config.append("var "+resultItemId+"_store = new Ext.data.JsonStore({\n");
						sb_result_config.append("url: "+resultItemId+"_dataUrl,\n");
						sb_result_config.append("root: 'result',");
						sb_result_config.append("totalProperty: 'totalCount',\n");
						sb_result_config.append("remoteSort: true,");
						sb_result_config.append("fields: ['_NOWTIME','_HALFLATERTIME',"+resultField+"]\n");
						sb_result_config.append("});\n");
						
						//默认排序
						if(sortName!=null && sortType!=null){
							sb_result_config.append(resultItemId+"_store.setDefaultSort('"+sortName+"', '"+sortType.toLowerCase()+"');\n");
						}
						sb_gridPanel.append("var gridPanel_grid='"+resultItemId+"_grid';");
						sb_result_config.append("var "+resultItemId+"_grid = new Ext.grid.GridPanel({\n");
						sb_result_config.append("id:'"+resultItemId+"_grid',\n");
						sb_result_config.append("height:333,\n");//车辆管理数据结果框的高度
						//sb_result_config.append("width:width-20,\n");
						//sb_result_config.append("width:width+15,\n");
						sb_result_config.append("width:width,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("padding: '5 5 5 5',");
						sb_result_config.append("trackMouseOver:true,\n");
						sb_result_config.append("disableSelection:false,\n");
						sb_result_config.append("loadMask: true,\n");
						sb_result_config.append("sm:"+resultItemId+"_select,\n");
						sb_result_config.append("columns:[\n");
						//sb_result_config.append("new Ext.grid.RowNumberer(),"+resultItemId+"_select,\n");
						sb_result_config.append("new Ext.grid.RowNumberer(),\n");
						
						//构造gridPanel的列样式
						if(result_field_list!=null && result_field_list.size()>0){
							for(int k=0;k<result_field_list.size();k++){
								WorkmanageField field = result_field_list.get(k);				
								sb_result_config.append("{\n");
								sb_result_config.append("id: '"+field.getFieldName()+"',\n");
								sb_result_config.append("dataIndex: '"+field.getFieldName()+"',\n");
								sb_result_config.append("header: '"+field.getFieldLabel()+"',\n");
								if(field.getWidth()!=null && field.getWidth().intValue()!=0){
									sb_result_config.append("width: "+field.getWidth()+",\n");
								}else{
									sb_result_config.append("width: 150,\n");
								}	
								
								if(field.getRendererJs()!=null && !"".equals(field.getRendererJs())){
									sb_result_config.append("renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){\n");						
									sb_result_config.append(field.getRendererJs()+"(value,cellmeta,record,rowIndex,columnIndex,store);");
									sb_result_config.append("},\n");
								}
								
								sb_result_config.append("align: 'center',\n");
								sb_result_config.append("sortable: true\n");
								sb_result_config.append("},\n");
							}
						}
						
						
						if(sb_result_config.length()>0){
							sb_result_config.deleteCharAt(sb_result_config.length()-2);
						}
						
						sb_result_config.append("],\n");
						sb_result_config.append("bbar: new Ext.PagingToolbar({\n");
						sb_result_config.append("pageSize: 10,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("displayInfo: true,\n");
						sb_result_config.append("displayMsg: '显示 {0} - {1} of {2}',\n");
						sb_result_config.append("emptyMsg: '没有查询到记录'\n");
						sb_result_config.append("}),\n");
						
						//----row记录行样式控制----begin---------
						sb_result_config.append("viewConfig : {\n");
						sb_result_config.append("forceFit : false,\n");
						sb_result_config.append("enableRowBody : true,\n");
						sb_result_config.append("getRowClass :function(record, rowIndex, p, ds) {\n");
						if(resultRowViewFunc!=null&&!"".equals(resultRowViewFunc)){
							sb_result_config.append("return "+resultRowViewFunc+"(record, rowIndex, p, ds);");
						}
						sb_result_config.append("}");
						sb_result_config.append("},");		
						//----row记录行样式控制----end---------
						
						//监听事件
						sb_result_config.append("listeners : {\n");
						sb_result_config.append("'rowdblclick':function(grid, rowIndex,e){\n");
						if(resultRowDbClickFunc!=null && !"".equals(resultRowDbClickFunc)){
							sb_result_config.append(resultRowDbClickFunc+"(grid,rowIndex,e);");
						};
						sb_result_config.append("}\n");
						sb_result_config.append("}\n");
						sb_result_config.append("});\n");
						//配置分页
						if(resultLimit==null||"".equals(resultLimit)){
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\",1000*60*5);\n");
							}
						}else{
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								//sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
							}else{
								//sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\","+autoQueryTime+");\n");
							}
						}
						
						//sb_resultItem_widget.append(resultItemId+"_grid");
					}
				}
				
				//构造form，装载控件
				sb_result_config.append("var viewPortForm = new Ext.Panel({\n");
				sb_result_config.append("labelWidth: 75,\n");
				sb_result_config.append("frame:false,\n");
				sb_result_config.append("border:false,\n");
				sb_result_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_result_config.append("items: [\n");
				sb_result_config.append("\n");
				//sb_result_config.append("items:[{border:false,items:[{border:false,html:\"<iframe style='width: 100%; height:40px; border:0px;'  src='op/workmanage/orderTips.html' ></iframe>\"}]},");
				//sb_result_config.append(resultItemId+"_grid]\n");
				sb_result_config.append(resultItemId+"_grid"+"\n");
				sb_result_config.append("]\n");
				sb_result_config.append("});\n");
			}
		}
		//查询结果config-----------end------
		
	}else {
		if(queryPage!=null && !queryPage.isEmpty()){
			sb_queryDiv.append("var queryDivId='"+queryPage+"';");
			sb_menuType.append("var menuType='"+menuType+"';");
			//sb_queryDiv.append("$('#"+queryPage+"').show()");
		}else{
			
		}
		
//查询条件config--------------end-----------------
		
		
		//查询结果config-----------begin------
		//StringBuilder sb_result = new StringBuilder();
		if(result_config_list!=null&&result_config_list.size()>0){
			//如果结果项数量大于1，以tab显示结果项
			if(result_config_list.size()>1){
				sb_isShowOfTab.append("var isShowOfTab=true;");
			}else{
				String resultItemId = null;
				String resultItemType=null;
				String queryEntityName = null;
				String resultField=null;
				String queryAction=null;
				String queryCondition=null;
				String autoQueryTime=null;
				String sortName = null;
				String sortType = null;
				String resultLimit=null;
				String resultItemScript=null;
				String resultRowDbClickFunc=null;
				String resultRowViewFunc=null;
				String filterBiz=null;
				String filterProp=null;
				
				sb_isShowOfTab.append("var isShowOfTab=false;");
				
				for(int i=0;i<result_config_list.size();i++){
					Map<String,Object> resultItem = result_config_list.get(i);
					resultItemId=resultItem.get("resultItemId")==null?null:resultItem.get("resultItemId").toString();
					resultItemType=resultItem.get("resultItemType")==null?null:resultItem.get("resultItemType").toString();
					queryEntityName=resultItem.get("queryEntityName")==null?null:resultItem.get("queryEntityName").toString();
					resultField=resultItem.get("resultField")==null?null:resultItem.get("resultField").toString();
					queryAction=resultItem.get("queryAction")==null?null:resultItem.get("queryAction").toString();
					queryCondition=resultItem.get("queryCondition")==null?null:resultItem.get("queryCondition").toString();
					autoQueryTime=resultItem.get("autoQueryTime")==null?null:resultItem.get("autoQueryTime").toString();
					sortName=resultItem.get("sortName")==null?null:resultItem.get("sortName").toString();
					sortType=resultItem.get("sortType")==null?null:resultItem.get("sortType").toString();
					resultLimit=resultItem.get("resultLimit")==null?null:resultItem.get("resultLimit").toString();
					resultItemScript=resultItem.get("resultItemScript")==null?null:resultItem.get("resultItemScript").toString();
					resultRowDbClickFunc=resultItem.get("resultRowDbClickFunc")==null?null:resultItem.get("resultRowDbClickFunc").toString();
					resultRowViewFunc=resultItem.get("resultRowViewFunc")==null?null:resultItem.get("resultRowViewFunc").toString();
					filterBiz=resultItem.get("filterBiz")==null?null:resultItem.get("filterBiz").toString();
					filterProp=resultItem.get("filterProp")==null?null:resultItem.get("filterProp").toString();
					
					if(resultItemScript!=null && !resultItemScript.isEmpty()){
						sb_js.append("<script src=\""+resultItemScript+"\" type=\"text/javascript\"></script>\n");
					}
					
					if(resultItemType!=null && !resultItemType.isEmpty() && "grid".equals(resultItemType)){		//如果结果项为grid
						
						subPage_resultItemId=subPage_resultItemId+"\""+(resultItemId)+"\"";
						sb_result_config.append("var "+resultItemId+"_select = new Ext.grid.CheckboxSelectionModel();\n");
						sb_result_config.append("var "+resultItemId+"_dataUrl = '"+queryAction+"?queryCondition="+queryCondition+"&queryEntityName="+queryEntityName+"&filterBiz="+filterBiz+"&filterProp="+filterProp+"';\n");
						sb_result_config.append("var "+resultItemId+"_store = new Ext.data.JsonStore({\n");
						sb_result_config.append("url: "+resultItemId+"_dataUrl,\n");
						sb_result_config.append("root: 'result',");
						sb_result_config.append("totalProperty: 'totalCount',\n");
						sb_result_config.append("remoteSort: true,");
						sb_result_config.append("fields: ['_NOWTIME','_HALFLATERTIME',"+resultField+"]\n");
						sb_result_config.append("});\n");
						
						//默认排序
						if(sortName!=null && sortType!=null){
							sb_result_config.append(resultItemId+"_store.setDefaultSort('"+sortName+"', '"+sortType.toLowerCase()+"');\n");
						}
						
						sb_gridPanel.append("var gridPanel_grid='"+resultItemId+"_grid';");
						sb_result_config.append("var "+resultItemId+"_grid = new Ext.grid.GridPanel({\n");
						sb_result_config.append("id:'"+resultItemId+"_grid',\n");
						sb_result_config.append("height:300,\n");
						//sb_result_config.append("width:width-20,\n");
						sb_result_config.append("width:width,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("padding: '5 5 5 5',");
						sb_result_config.append("trackMouseOver:true,\n");
						sb_result_config.append("disableSelection:false,\n");
						sb_result_config.append("loadMask: true,\n");
						sb_result_config.append("sm:"+resultItemId+"_select,\n");
						sb_result_config.append("columns:[\n");
						//sb_result_config.append("new Ext.grid.RowNumberer(),"+resultItemId+"_select,\n");
						sb_result_config.append("new Ext.grid.RowNumberer(),\n");
						
						//构造gridPanel的列样式
						if(result_field_list!=null && result_field_list.size()>0){
							for(int k=0;k<result_field_list.size();k++){
								WorkmanageField field = result_field_list.get(k);				
								sb_result_config.append("{\n");
								sb_result_config.append("id: '"+field.getFieldName()+"',\n");
								sb_result_config.append("dataIndex: '"+field.getFieldName()+"',\n");
								sb_result_config.append("header: '"+field.getFieldLabel()+"',\n");
								if(field.getWidth()!=null && field.getWidth().intValue()!=0){
									sb_result_config.append("width: "+field.getWidth()+",\n");
								}else{
									sb_result_config.append("width: 150,\n");
								}	
								
								if(field.getRendererJs()!=null && !"".equals(field.getRendererJs())){
									sb_result_config.append("renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){\n");						
									sb_result_config.append(field.getRendererJs()+"(value,cellmeta,record,rowIndex,columnIndex,store);");
									sb_result_config.append("},\n");
								}
								
								sb_result_config.append("align: 'center',\n");
								sb_result_config.append("sortable: true\n");
								sb_result_config.append("},\n");
							}
						}
						
						
						if(sb_result_config.length()>0){
							sb_result_config.deleteCharAt(sb_result_config.length()-2);
						}
						
						sb_result_config.append("],\n");
						sb_result_config.append("bbar: new Ext.PagingToolbar({\n");
						sb_result_config.append("pageSize: 10,\n");
						sb_result_config.append("store: "+resultItemId+"_store,\n");
						sb_result_config.append("displayInfo: true,\n");
						sb_result_config.append("displayMsg: '显示 {0} - {1} of {2}',\n");
						sb_result_config.append("emptyMsg: '没有查询到记录'\n");
						sb_result_config.append("}),\n");
						
						//----row记录行样式控制----begin---------
						sb_result_config.append("viewConfig : {\n");
						sb_result_config.append("forceFit : false,\n");
						sb_result_config.append("enableRowBody : true,\n");
						sb_result_config.append("getRowClass :function(record, rowIndex, p, ds) {\n");
						if(resultRowViewFunc!=null&&!"".equals(resultRowViewFunc)){
							sb_result_config.append("return "+resultRowViewFunc+"(record, rowIndex, p, ds);");
						}
						sb_result_config.append("}");
						sb_result_config.append("},");		
						//----row记录行样式控制----end---------
						
						//监听事件
						sb_result_config.append("listeners : {\n");
						sb_result_config.append("'rowdblclick':function(grid, rowIndex,e){\n");
						if(resultRowDbClickFunc!=null && !"".equals(resultRowDbClickFunc)){
							sb_result_config.append(resultRowDbClickFunc+"(grid,rowIndex,e);");
						};
						sb_result_config.append("}\n");
						sb_result_config.append("}\n");
						sb_result_config.append("});\n");
						//配置分页
						if(resultLimit==null||"".equals(resultLimit)){
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:10}});\n");
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\",1000*60*5);\n");
							}
						}else{
							if(autoQueryTime==null||"".equals(autoQueryTime)){
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");					
							}else{
								sb_result_config.append(resultItemId+"_store.load({params:{start:0, limit:"+resultLimit+"}});\n");	
								sb_result_config.append("setInterval(\"Ext.getCmp('"+resultItemId+"_grid').getStore().reload()\","+autoQueryTime+");\n");
							}				
						}
						
						//sb_resultItem_widget.append(resultItemId+"_grid");
					}
				}
				
				//构造form，装载控件
				sb_result_config.append("var viewPortForm = new Ext.Panel({\n");
				sb_result_config.append("labelWidth: 75,\n");
				sb_result_config.append("frame:false,\n");
				sb_result_config.append("border:false,\n");
				sb_result_config.append("bodyStyle:'padding:2px 2px 2px 2px',\n");
				sb_result_config.append("items: [\n");
				sb_result_config.append("\n");
				//sb_result_config.append("{border:false,items:[{border:false,html:\"<iframe style='width: 100%; height:40px; border:0px;'  src='op/workmanage/orderTips.html' ></iframe>\"}]},\n");
				sb_result_config.append(resultItemId+"_grid\n");
				sb_result_config.append("]\n");
				sb_result_config.append("});\n");
			}
		}
		//查询结果config-----------end------
		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>您好!通用首页</title>
<%@include file="common.jsp"%>
<meta http-equiv=Content-Type content=text/html;charset=utf-8 >
<%-- <meta http-equiv="X-UA-Compatible" content="IE=edge" /> --%>
<style type="text/css">

.red-row{ background-color: red ;}

.x-grid-record-green table{  
    color: blue;      
} 

.x-grid-record-red table{  
    color: red;      
}

.x-grid-record-font-normal table td{  
    font-weight:normal;    
} 

.x-grid-record-font-bold table td{  
    font-weight:bold;    
}

.x-selectable, .x-selectable * {
-moz-user-select: text! important ;
-khtml-user-select: text! important ;
}

/*解决gridPanel兼容IE，高度以内容作界限的bug*/
.x-grid3-scroller{
	height:330px;
}

.tab_menu{clear: both;height: 25px; position:relative; z-index:1;}
.tab_menu ul{ position: absolute; z-index: 1;}
.tab_menu ul li{background: none repeat scroll 0 0 #EEEEEE;border-right: 1px solid #99BBE8;border-top: 1px solid #99BBE8;cursor: pointer; float: left;line-height: 24px; padding: 0 3px; position: relative; text-align: center; width: 100px;}
.tab_menu ul li.selected{background: none repeat scroll 0 0 #FFFFFF; border-top: 3px solid #99BCE8; height: 23px; line-height: 22px;}
.tab_middle{}
.checked_search .w_tree{display:none; left:90px; left:87px\0; top:64px; border-top:1px solid #ccc;}

</style>
<link rel="stylesheet" type="text/css" href="../ops/css/base.css"/>
<link rel="stylesheet" type="text/css" href="../ops/css/public.css"/>
<link rel="stylesheet" type="text/css" href="op/workmanage/css/car_search.css" />
<link rel="stylesheet" type="text/css" href="op/workmanage/css/search_bar.css" />
<link rel="stylesheet" type="text/css" href="../ops/jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../ops/jslib/dialog/dialog.css" />
<link rel="stylesheet" type="text/css" href="op/workmanage/css/qxgd_zd.css" />




<script src="op/workmanage/jslib/commonQueryIndexPage.js" type="text/javascript"></script>
<script type="text/javascript" src="../ops/jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript"" src="../ops/jslib/date/date.js"></script>
<script type="text/javascript" src="../ops/jslib/common.js"></script>
<script type="text/javascript" src="../ops/jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../ops/op/jslib/generateTree.js"></script>
<script type="text/javascript" src="op/workmanage/jslib/commonUtil.js"></script>

<script type="text/javascript" src="../ops/jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="../ops/op/js/tab.js"></script>
<script type="text/javascript">
var pageToolBarId="";


var subPage_resultItemId=<%=subPage_resultItemId%>;
Ext.onReady(function(){
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();
	
	<%=inputComboxStoreString%>
	
	<%=sb_input_config%>
	<%=sb_result_config%>
	
  	<%
  		//System.out.println("要输出的str：\n"+sb_result_config);
  	%>
     var viewport = new Ext.Viewport({
        layout:'border',
        //renderTo:'result_div',
        applyTo:'result_div',
        items:[
			{
                region: 'center',
                id: 'center-panel',
                split: false,
                border:false,
                margins: '0 0 0 0',
                items:[viewPortForm]
            }
         ]
    });
    
    //窗口大小变动事件
    function windowsResize(){
		//var inputFormHeight = inputForm.getHeight();
    	//var bodyHeight = document.body.clientHeight;
    	//alert(bodyHeight-inputFormHeight);
    	//resultObj.setHeight(bodyHeight-inputFormHeight-50);
    	//resultObj.resize();
    }
    
  	window.onresize=function(){
  		var bodyWidth = Ext.get(document.body).getWidth();
  		var gridHeight=350;
  		var gridPanel;
		if(isShowOfTab){
			//var gridItems=activeTab.items.itemAt(0);
			//var tabPanel = Ext.getCmp('result_tab');
			//var allTab=tabPanel.items;
			//var activeTab=tabPanel.getActiveTab();
			//gridPanel=activeTab.items.itemAt(0);
			//gridPanel.setWidth(bodyWidth);
			
		}else{
			gridPanel=Ext.getCmp(gridPanel_grid);
			gridPanel.setWidth(bodyWidth);
		}
	}
});


function changeGridWidth(tabPanel){
	var bodyWidth = Ext.get(document.body).getWidth();
	var activeTab=tabPanel.getActiveTab();
	gridPanel=activeTab.items.itemAt(0);
	gridPanel.setWidth(bodyWidth);
}

//GRID行记录数据可复制扩展----begin----
if (!Ext.grid.GridView.prototype.templates) {
	Ext.grid.GridView.prototype.templates = {};
}
Ext.grid.GridView.prototype.templates.cell = new Ext.Template(
'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,
'<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,
'</td>'
);
//GRID行记录数据可复制扩展----end----

//################ query ######

<%=sb_queryDiv%>
<%=sb_menuType%>

<%=sb_isShowOfTab%>
<%=sb_gridPanel%>

$(function(){


	//sb_result_htmltab_config
	//alert("dd=="+dd.attr("id"));
	
	<%=sb_result_htmltabhidden_config%>
	$("#tab ul").append(<%=sb_result_htmltab_config%>);
	
	$("#tabBody").append(<%=sb_result_htmltabbody_config%>);

	//显示当前菜单对应的查询条件div
	$("#"+queryDivId).show();
	
	$(":button,:submit").mousedown(function(){
		$(this).addClass("input_button_down");
	})
	$(":button,:submit").mouseup(function(){
		$(this).removeClass("input_button_down");
	})
	
	$(".gd_select").change(function(){
		var index = $(this).get(0).selectedIndex;
		if(index == 1){
			$(".gd_select_toggle").show();
			$(".gd_search_toggle").show();
		}else{
			$(".gd_select_toggle").hide();
			$(".gd_search_toggle").hide();
		}
	});
	$(".rwd_select").change(function(){
		var index = $(this).get(0).selectedIndex;
		if(index == 1){
				$(".rwd_select_toggle").show();
			}else{
				$(".rwd_select_toggle").hide();
			}
	});
	$(".gd_search").change(function(){
		var index = $(this).get(0).selectedIndex;
		if(index == 1){
			$(".gd_search_toggle").show();
		}else{
			$(".gd_search_toggle").hide();
		}
	})
	
	/*高级搜索*/
	$(".checked_search").hide();
	
	$("#gj_search").click(function(){
		if($("#gj_search").attr("checked")){
			$(".checked_search").show();
		}else{
			$(".checked_search").hide();
		}
	});
	$("#gj_search2").click(function(){
		if($("#gj_search2").attr("checked")){
			$(".checked_search").show();
		}else{
			$(".checked_search").hide();
		}
	});
	
	
	searchProviderOrgTree();
	
	/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
	
	$("#urgentRepair_queryWorkOrder_chooseAreaButton").click(function(){
		$("#treeDiv2").toggle("fast");
	});
	
	$("#urgentRepair_queryTaskOrder_chooseAreaButton").click(function(){
		$("#treeDiv3").toggle("fast");
	});
	
	
	//获取受理专业数据字典
	var acceptProfessionalData=getDictionaryDataByTreeId(6);
	var targetId=$("#"+queryDivId+" :input[name=acceptProfessional]").attr("id");
	constructSelectData(targetId,acceptProfessionalData);
	
	//获取基站等级数据字典
	var baseStationLevelData=getDictionaryDataByTreeId(3);
	targetId=$("#"+queryDivId+" :input[name=baseStationLevel]").attr("id");
	constructSelectData(targetId,baseStationLevelData);
	
	
	var id_btnSubmit=$("#"+queryDivId+" :input[name=btnSubmit]").attr("id");
	var id_form=$("#"+queryDivId+" form").attr("id");
	
	//点击查询按钮
	$("#"+id_btnSubmit).click(function(){
		submitQuery(id_form);
	});
	
	//字段选择对话框js
	$(".dialog_tab li").each(function(index){
		$(this).click(function(){
			$(".dialog_tab li").removeClass("on").eq(index).addClass("on");
			$(".dialog_tab_content").hide().eq(index).show();
		})
	});
	
	//父复选框选中，子复选框选中
	$(".dialog_tab :checkbox").each(function(index){
		$(this).click(function(){
			if($(this).attr("checked") == "checked"){
				$(".dialog_tab_content").eq(index).find($(":checkbox")).attr("checked","checked");
			}else{
				$(".dialog_tab_content").eq(index).find($(":checkbox")).removeAttr("checked");
			}
		})
	});
	
	//子复选框选中，父复选框选中
	$(".dialog_tab_content").each(function(index){
		$(this).find($(":checkbox")).each(function(){
			$(this).click(function(){
				var hasBrotherSelect=false;
				if($(this).attr("checked") == "checked"){
					$(".dialog_tab :checkbox").eq(index).attr("checked","checked");
				}else if($(".dialog_tab_content").eq(index).find($(":checkbox")).not($(this)).each(function(){
						if($(this).attr("checked")== "checked"){
							hasBrotherSelect=true;
							return false;
						}
					})){
					if(!hasBrotherSelect){
						$(".dialog_tab :checkbox").eq(index).removeAttr("checked");
					}else{
						$(".dialog_tab :checkbox").eq(index).attr("checked","checked");
					}
				}else{
					$(".dialog_tab :checkbox").eq(index).removeAttr("checked");
				}
			})
		})
	});
	
	
	$("#urgentRepair_pendingWorkOrder_exportResult").click(function(){
		//判断是否选择工单类型
		var select_woType_val=$("#select_woType").val();
		if(select_woType_val==""){
			alert("请选择工单类型")
		}else{
			$("#copy_dialog").show();
		}
	});
	
	
	//监听对话框关闭事件--------- begin ----
	$("#btnRightTopClose").click(function(){
		$("#copy_dialog").hide();
	});
	
	$("#btnCancelDialog").click(function(){
		$("#copy_dialog").hide();
	});
	//监听对话框关闭事件--------- end ----
	
	
	
	//监听对话框提交
	var exportUrl="exportQueryResultToExcelAction";
	$("#btnSubmitDialog").click(function(){
		var isCanSubmit=false;
		
		//校验是否可以提交导出
		var exportTypeTab=$(".dialog_tab li input[type=checkbox]");
		for ( var i = 0; i < exportTypeTab.length; i++) {
			if(exportTypeTab[i].checked){
				isCanSubmit= true;
				break;
			}
		}
		if(isCanSubmit){
			$("#dialogForm").submit();
		}else{
			alert("请至少选择一项导出");
		}
	});
	
	
	if(!+[1,] || window.XMLHttpRequest){
		$(".placeholder_text").each(function(){
			$(this).val($(this).attr("placeholder"));
			$(this).focusin(function(){
				if($(this).val() == $(this).attr("placeholder")){
					$(this).val("");
				}
			});
			$(this).focusout(function(){
				if($(this).val() == ""){
					$(this).val($(this).attr("placeholder"));
				}
			});
		})
	};
	
	
	tab("tab","li","onclick");
	
	
	
	addEnterListener();
	
});

//生成组织架构树
function searchProviderOrgTree(){
	var orgId = "16";
	var orgName="";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.orgId;
			orgName=data.name;
			if(orgId==null||orgId==""){
				orgId="16";
			}
			if(!orgName || orgName=="" || orgName=="undefined"){
				orgName="";
			}
			
			$("#searchBizunitIdText").val(orgId);
			$("#searchBizunitNameText").val(orgName);
			
			$("#urgentRepair_queryWorkOrder_searchOrgName").val(orgName);
			$("#urgentRepair_queryWorkOrder_searchOrgId").val(orgId);
			
			$("#urgentRepair_queryTaskOrder_searchOrgName").val(orgName);
			$("#urgentRepair_queryTaskOrder_searchOrgId").val(orgId);
			
			var values = {"orgId":orgId}
			var myUrl = "op/organization/getProviderOrgTreeByOrgIdAction";
			
			if(menuType=="workDispatch"){
				$.post(myUrl,values,function(data){
					createOrgTreeOpenFirstNode(data,"treeDiv2","search_org_div2","a","urgentRepair_queryWorkOrder_searchOrgTreeClick");
				},"json");
				$.post(myUrl,values,function(data){
					createOrgTreeOpenFirstNode(data,"treeDiv3","search_org_div3","a","urgentRepair_queryTaskOrder_searchOrgTreeClick");
				},"json");
			}else{
				$.post(myUrl,values,function(data){
					createOrgTreeOpenFirstNode(data,"treeDiv","search_org_div","a","searchOrgTreeClick");
				},"json");
			}
		}
	});
}
function a(){}

//选择组织
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#searchBizunitNameText").val(data.name);
	$("#searchBizunitIdText").val(data.orgId);
	$("#treeDiv").slideUp("fast");
}

//选择组织
function urgentRepair_queryWorkOrder_searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#urgentRepair_queryWorkOrder_searchOrgName").val(data.name);
	$("#urgentRepair_queryWorkOrder_searchOrgId").val(data.orgId);
	$("#treeDiv2").slideUp("fast");
}

function urgentRepair_queryTaskOrder_searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#urgentRepair_queryTaskOrder_searchOrgName").val(data.name);
	$("#urgentRepair_queryTaskOrder_searchOrgId").val(data.orgId);
	$("#treeDiv3").slideUp("fast");
}


//提交查询
function submitQuery(formId){
	var formValue="";
	var queryData=getInputsByForm(formId);
	formValue=jsonToStr(queryData);
	
	var resultItemId=subPage_resultItemId;
	var gridStore;
	if(isShowOfTab){
		var flag=0;
		var children_li=$("#tab li");
		$.each(children_li,function(i,n){
			if($(n).attr("class")=="selected"){
				flag=i;
			}
		});
		
		//var tabPanel = Ext.getCmp('result_tab');
		//var activeTab=tabPanel.getActiveTab();
		//var grid=activeTab.items.itemAt(0);
		//gridStore = grid.getStore();
		var gridId=$("#tab_"+flag+" input").val();
		gridId=gridId+"_grid";
		var grid=Ext.getCmp(gridId);
		gridStore = grid.getStore();
	}else{
		gridStore=Ext.getCmp(resultItemId+"_grid").getStore();
	}
	gridStore.baseParams= {queryParams:formValue,start:0,limit:10};
	gridStore.load();
}

//监听页面查询按钮的键盘事件
function addEnterListener(){
	$("body").keyup(function(event){
		var myEvent = event || window.event;
		var keyCode = myEvent.keyCode;
		if(keyCode==13){
			//window.alert("提交查询");
			
			var id_btnSubmit=$("#"+queryDivId+" :input[name=btnSubmit]").attr("id");
			var id_form=$("#"+queryDivId+" form").attr("id");
			
			submitQuery(id_form);
		}
	});
}


</script>
<%=sb_js.toString()%>


</head>
<body>
<%-- 抢修待办工单 ############## begin --%>
<div class="container" id="urgentRepair_pendingWorkOrder" style="display:none;margin-bottom:30px;">
       <div class="search_bar">
           <h4>查询条件</h4>
           <form id="form1" action="exportQueryResultToExcelAction">
            <table class="search_table">
                <tr>
                	<td>
                    	<span class="td_m">工单类型：</span>
                    	<select id="urgentRepair_pendingWorkOrder_woType" name="woType" class="gd_select">
                        	<option value="">请选择工单类型</option>
                        	<option value="抢修">抢修工单</option>
                        	<option value="修缮">修缮工单</option>
                        </select>
                        <input name="woType_operator" type="hidden" value="=" />
                        <input name="woType_type" type="hidden" value="select" />
                    </td>
                	<td>
                    	<span class="td_m">工单状态：</span>
                    	<select id="urgentRepair_pendingWorkOrder_status" name="status">
                        	<option value="">请选择工单状态</option>
                        	<option value="2">待受理</option>
                        	<option value="3">处理中</option>
                        	<option value="6">已派发</option>
                        	<%-- <option value="7">已结束</option> --%>
                        </select>
                        <input name="status_operator" type="hidden" value="=" />
                        <input name="status_type" type="hidden" value="text" />
                    </td>
                	<td>
                    	<span class="td_m">工单主题：</span>
                    	<input name="woTitle" id="urgentRepair_pendingWorkOrder_woTitle" type="text" class="placeholder_text" placeholder="请输入工单主题" />
                    	<input name="woTitle_operator" type="hidden" value="like" />
                        <input name="woTitle_type" type="hidden" value="text" />
                    </td>
                </tr>
                <tr class="gd_select_toggle" style="display:none;">
                	<%--<td>
                    	 <span class="td_m">基站类型：</span>
                    	<select id="select_baseStationType" name="baseStationType">
                        	<option value="">请选择基站类型</option>
                        	<option>2G主设备</option>
                        	<option>2G直放站</option>
                            <option>TD主设备</option>
                            <option>TD直放站</option>
                        </select>
                        <input name="baseStationType_operator" type="hidden" value="=" />
                        <input name="baseStationType_type" type="hidden" value="select" /> 
                    </td>--%>
                	<td>
                    	<span class="td_m">受理专业：</span>
                    	<select id="urgentRepair_pendingWorkOrder_acceptProfessional" name="acceptProfessional">
                        	<option value="">请选择受理专业</option>
                        	<%-- <option value="无线">无线</option>
                       	<option value="传输">传输</option>
                       	<option value="主设备">主设备</option> --%>
                       </select>
                       <input name="acceptProfessional_operator" type="hidden" value="=" />
                       <input name="acceptProfessional_type" type="hidden" value="select" />
                   </td>
               	<td>
                   	<span class="td_m">基站名称：</span>
                   	<input name="faultStationName" type="text" class="placeholder_text" placeholder="请输入基站名称" />
                   	<input name="faultStationName_operator" type="hidden" value="like" />
                       <input name="faultStationName_type" type="hidden" value="text" />
                   </td>
               </tr>
               <tr>
               		<td class="tc" colspan="3">
                   		<input name="btnSubmit" id="urgentRepair_pendingWorkOrder_btnSubmit" type="button" value="查询" />
                   	</td>
               </tr>
           </table>
          </form>
      </div>
      <div><iframe width="100%" height="40" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" src='op/workmanage/orderTips.html' ></iframe></div>
</div>
<%-- 抢修待办工单 ############## end --%>


<%-- 抢修工单查询 ############## begin --%>
<div class="container" id="urgentRepair_queryWorkOrder" style="display:none;margin-bottom:30px;">
    <div class="search_bar">
        <h4>查询条件
        	<div style="position:absolute; right:8px; top:2px;">
                <input type="checkbox" id="gj_search" /><label for="gj_search">高级</label>
            </div>
        </h4>
         <form id="form2">
         <table class="search_table">
             <tr>
             	<td>
                 	<span class="td_m">工单类型：</span>
                 	<select id="select_woType" name="woType" class="gd_select">
                     	<option value="">请选择工单类型</option>
                     	<option value="抢修">抢修工单</option>
                     	<%-- <option>修缮工单</option> --%>
                     </select>
                     <input name="woType_operator" type="hidden" value="=" />
                     <input name="woType_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<span class="td_m">工单状态：</span>
                 	<select name="status">
                     	<option value="">请选择工单状态</option>
                     	<option value="2">待受理</option>
                     	<option value="3">处理中</option>
                     	<option value="6">已派发</option>
                     	<option value="7">已结束</option>
                     </select>
                     <input name="status_operator" type="hidden" value="=" />
                     <input name="status_type" type="hidden" value="text" />
                 </td>
             	<td>
                 	<span class="td_m">工单主题：</span>
                 	<input name="woTitle" type="text" class="placeholder_text" placeholder="请输入工单主题" />
                 	<input name="woTitle_operator" type="hidden" value="like" />
                     <input name="woTitle_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr class="checked_search">
             	<td>
             		<span class="td_m">组织架构：</span>
	               	<input id="urgentRepair_queryWorkOrder_searchOrgId" name="orderOwnerOrgId" type="hidden"  />
	               	<input name="orderOwnerOrgId_operator" type="hidden" value="in"  />
	               	<input name="orderOwnerOrgId_type" type="hidden" value="dataScope"  />
	               	<input type="text" class="search_zuzhi" disabled="disabled" id="urgentRepair_queryWorkOrder_searchOrgName" onfocus="$('#wrap_treeDiv').toggle('fast');" style="*+margin-left:-12px;"/>
	               	<a href="javascript:void(0);" style="margin-top:2px; margin-left:-22px; margin-left:-26px\0;" class="select_button selectWorkPlaceButton" id="urgentRepair_queryWorkOrder_chooseAreaButton" title="选择组织"></a>
             		<div id="treeDiv2" class="select_tree_div w_tree">
	    					<%-- 放置组织架构树 --%>
    				</div>
             	</td>
             	<td>
                 	<span class="td_m">是否超时：</span>
                    <select name="isOverTime">
                     	<option value="">全部</option>
                     	<option value="yes">是</option>
                     	<option value="no">否</option>
                    </select>
                    <input name="isOverTime_operator" type="hidden" value="in" />
                    <input name="isOverTime_type" type="hidden" value="dataScope" />
                 </td>
             	<td>
                 	<span class="td_m">工单编号：</span>
                     <input name="woId" type="text" class="placeholder_text" placeholder="请输入工单编号" />
                 	<input name="woId_operator" type="hidden" value="like" />
                     <input name="woId_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr class="checked_search">
             	<td colspan="3">
	                <span class="td_m">创建时间：</span>
	                <input name="createTime" type="text" id="date_text1" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text1'),document.getElementById('date_text1'),true)"></a> - 
	                    <input name="createTime_" type="text" id="date_text2" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text2'),document.getElementById('date_text2'),true)"></a>
	                 	<%--
	                      <input name="createTime" type="text" id="date_text1" style="width:60px;" onclick="fPopCalendar(event,document.getElementById('date_text1'),document.getElementById('date_text1'),true)" /> - 
	                     <input name="createTime_" type="text" id="date_text2" style="width:60px;" onclick="fPopCalendar(event,document.getElementById('date_text2'),document.getElementById('date_text2'),true)"  />
	                      
	                     <input type="text" id="date_text1" name="createTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" /> -
	                    <input type="text" id="date_text2" name="createTime_" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" />
	                    --%>
	                     
	                     
	                     
	                     
	                     
	                     <input name="createTime_operator" type="hidden" value=">=" />
	                     <input name="createTime_type" type="hidden" value="dateTime" />
	                     <input name="createTime__operator" type="hidden" value="&lt;=" />
	                     <input name="createTime__type" type="hidden" value="dateTime" />
	                     
	                     
	               	 <span class="td_m">关闭时间：</span>
	               	<input name="finalCompleteTime" type="text"  id="date_text3" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text3'),document.getElementById('date_text3'),true)"></a> - 
	                    <input name="finalCompleteTime_" type="text"  id="date_text4" readonly="readonly"  /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text4'),document.getElementById('date_text4'),true)"></a>
						
					<%-- 	 <input type="text" id="date_text3" name="finalCompleteTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" /> -
	                    <input type="text" id="date_text4" name="finalCompleteTime_" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" />
	                    
						--%>
						
						
						<input name="finalCompleteTime_operator" type="hidden" value=">=" />
	                    <input name="finalCompleteTime_type" type="hidden" value="dateTime" />
	                    <input name="finalCompleteTime__operator" type="hidden" value="&lt;=" />
	                    <input name="finalCompleteTime__type" type="hidden" value="dateTime" />
	                   
                 </td>
             </tr>
             <tr class="gd_search_toggle" style="display:none; border-top:1px solid #99BBE8;">
             	<td>
                 	<span class="td_m">基站等级：</span>
                     <select id="urgentRepair_queryWorkOrder_select_baseStationLevel" name="baseStationLevel">
                     	<option value="">请选择基站等级</option>
                     </select>
                     <input name="baseStationLevel_operator" type="hidden" value="=" />
                     <input name="baseStationLevel_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<%--  <span class="td_m">基站类型：</span>
                 	<select name="baseStationType">
                     	<option value="">请选择基站类型</option>
                     	<option>2G主设备</option>
                     	<option>2G直放站</option>
                        <option>TD主设备</option>
                        <option>TD直放站</option>
                     </select>
                     <input name="baseStationType_operator" type="hidden" value="=" />
                     <input name="baseStationType_type" type="hidden" value="select" />  --%>
                     <span class="td_m">基站名称：</span>
	                 	<input name="faultStationName" type="text" class="placeholder_text" placeholder="请输入基站名称" />
	                 	<input name="faultStationName_operator" type="hidden" value="like" />
	                    <input name="faultStationName_type" type="hidden" value="text" />
                 </td>
             	<td>
                 	<span class="td_m">受理专业：</span>
                 	<select id="urgentRepair_queryWorkOrder_acceptProfessional" name="acceptProfessional">
                     	<option value="">请选择受理专业</option>
                     </select>
                     <input name="acceptProfessional_operator" type="hidden" value="=" />
                     <input name="acceptProfessional_type" type="hidden" value="select" />
                 </td>
             </tr>
	            <%-- <tr class="gd_search_toggle" style="display:none;">
	            	<td>
	                 	<span class="td_m">基站名称：</span>
	                 	<input name="faultStationName" type="text" class="placeholder_text" placeholder="请输入基站名称" />
	                 	<input name="faultStationName_operator" type="hidden" value="like" />
	                    <input name="faultStationName_type" type="hidden" value="text" />
	                 </td>
	            </tr> --%>
             <tr>
             	<td class="tc" colspan="3">
                 	<input id="urgentRepair_queryWorkOrder_btnSubmit" name="btnSubmit" type="button" value="查询" style="*+line-height:18px; *+height:26px;"/>
                 	<a class="excel_button" id="urgentRepair_pendingWorkOrder_exportResult" href="javascript:void(0);" value="工单批量导出" style="*+display:inline-block;">工单批量导出</a>
                 </td>
             </tr>
         </table>
        </form>
    </div>
</div>
<%-- 抢修工单查询 ############## end --%>


<%-- 抢修待办任务单 ############### begin --%>
<div class="container" id="urgentRepair_pendingTaskOrder" style="display:none;margin-bottom:30px;">
    <div class="search_bar">
        <h4>查询条件</h4>
        <form id="form3">
         <table class="search_table">
             <tr>
             	<td>
                 	<span class="td_m">任务单类型：</span>
                 	<select name="toType" class="rwd_select">
                     	<option value="">请选择任务单类型</option>
                     	<option value="抢修">抢修任务单</option>
                     	<option>修缮任务单</option>
                     </select>
                     <input name="toType_operator" type="hidden" value="=" />
                     <input name="toType_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<span class="td_m">任务单状态：</span>
                 	<select name="status">
                     	<option value="">请选择任务单状态</option>
                     	<option value="8">待受理</option>
                     	<option value="9">处理中</option>
                     	<option value="10">已派发</option>
                     	<option value="18">升级中</option>
                     	<%-- <option value="11">已结束</option> --%>
                     </select>
                     <input name="status_operator" type="hidden" value="=" />
                     <input name="status_type" type="hidden" value="text" />
                 </td>
             	<td>
                 	<span class="td_m">任务单主题：</span>
                 	<input name="toTitle" type="text" class="placeholder_text" placeholder="请输入任务单主题" />
                 	<input name="toTitle_operator" type="hidden" value="like" />
                     <input name="toTitle_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr class="rwd_select_toggle" style="display:none;">
             	<%-- <td>
                 	<span class="td_m">基站类型：</span>
                 	<select name="baseStationType">
                     	<option value="">请选择基站类型</option>
                     	<option>2G主设备</option>
                     	<option>2G直放站</option>
                         <option>TD主设备</option>
                         <option>TD直放站</option>
                     </select>
                     <input name="baseStationType_operator" type="hidden" value="=" />
                     <input name="baseStationType_type" type="hidden" value="select" />
                 </td> --%>
             	<td>
                 	<span class="td_m">受理专业：</span>
                 	<select id="urgentRepair_pendingTaskOrder_acceptProfessional" name="acceptProfessional">
                     	<option value="">请选择受理专业</option>
                     	<%-- <option value="无线">无线</option>
                     	<option value="传输">传输</option>
                     	<option value="主设备">主设备</option> --%>
                     </select>
                     <input name="acceptProfessional_operator" type="hidden" value="=" />
                     <input name="acceptProfessional_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<span class="td_m">基站名称：</span>
                 	<input name="faultStationName" type="text" class="placeholder_text" placeholder="请输入基站名称" />
                 	<input name="faultStationName_operator" type="hidden" value="like" />
                     <input name="faultStationName_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr>
             	<td class="tc" colspan="3">
                 	<input name="btnSubmit" id="urgentRepair_pendingTaskOrder_btnSubmit" type="button" value="查询" />
                 </td>
             </tr>
         </table>
        </form>
    </div>
</div>
<%-- 抢修待办任务单 ############### end --%>

<%-- 抢修任务单查询 ############### begin --%>
<div class="container" id="urgentRepair_queryTaskOrder" style="display:none;margin-bottom:30px;">
    <div class="search_bar">
        <h4>查询条件
        	<div style="position:absolute; right:8px; top:2px;">
                <input type="checkbox" id="gj_search2" /><label for="gj_search">高级</label>
            </div>
        </h4>
       	<form id="form4">
         <table class="search_table">
             <tr>
             	<td>
                 	<span class="td_m">任务单类型：</span>
                 	<select class="gd_search">
                     	<option value="">请选择任务单类型</option>
                     	<option value="抢修">抢修任务单</option>
                     	<option>修缮任务单</option>
                     </select>
                     <input name="toType_operator" type="hidden" value="=" />
                     <input name="toType_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<span class="td_m">任务单状态：</span>
                 	<select name="status">
                 		<option value="">请选择任务单状态</option>
                     	<option value="8">待受理</option>
                     	<option value="9">处理中</option>
                     	<option value="10">已派发</option>
                     	<option value="18">升级中</option>
                     	<option value="11">已结束</option>
                     </select>
                     <input name="status_operator" type="hidden" value="=" />
                     <input name="status_type" type="hidden" value="text" />
                 </td>
             	<td>
                 	<span class="td_m">任务单主题：</span>
                 	<input name="toTitle" type="text" class="placeholder_text" placeholder="请输入任务单主题" />
                 	<input name="toTitle_operator" type="hidden" value="like" />
                     <input name="toTitle_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr class="checked_search">
             	<td>
             		<span class="td_m">组织架构：</span>
	               	<input id="urgentRepair_queryTaskOrder_searchOrgId" name="currentHandlerOrgId" type="hidden"  />
	               	<input name="currentHandlerOrgId_operator" type="hidden" value="in"  />
	               	<input name="currentHandlerOrgId_type" type="hidden" value="dataScope"  />
	               	<input type="text" class="search_zuzhi" disabled="disabled" id="urgentRepair_queryTaskOrder_searchOrgName" onfocus="$('#wrap_treeDiv').toggle('fast');" style="*+margin-left:-12px;"/>
	               	<a href="javascript:void(0);" style="margin-top:2px; margin-left:-22px; margin-left:-26px\0;" class="select_button selectWorkPlaceButton" id="urgentRepair_queryTaskOrder_chooseAreaButton" title="选择组织"></a>
             		<div id="treeDiv3" class="select_tree_div w_tree ">
	    					<%-- 放置组织架构树 --%>
    				</div>
             	</td>
             	<td>
                 	<span class="td_m">是否超时：</span>
                    <select name="isOverTime">
                     	<option value="">全部</option>
                     	<option value="yes">是</option>
                     	<option value="no">否</option>
                    </select>
                    <input name="isOverTime_operator" type="hidden" value="in" />
                    <input name="isOverTime_type" type="hidden" value="dataScope" />
                 </td>
             	<td>
                 	<span class="td_m">任务单编号：</span>
                     <input name="toId" type="text" class="placeholder_text" placeholder="请输入任务单编号" />
                 	<input name="toId_operator" type="hidden" value="like" />
                     <input name="toId_type" type="hidden" value="text" />
                 </td>
             </tr>
             <tr class="checked_search">
             	<td colspan="3">
                 	<span class="td_m">创建时间：</span>
	             <input name="assignTime" type="text" id="date_text5" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text5'),document.getElementById('date_text5'),true)"></a> -
	                    <input name="assignTime_" type="text" id="date_text6" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text6'),document.getElementById('date_text6'),true)"></a>
	                    
	                <%--      <input type="text" id="date_text5" name="assignTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" /> -
	                    <input type="text" id="date_text6" name="assignTime_" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" />
	                    
	                  --%>    
	                    
	                    
	                    <input name="assignTime_operator" type="hidden" value=">=" />
	                    <input name="assignTime_type" type="hidden" value="dateTime" />
	                    <input name="assignTime__operator" type="hidden" value="&lt;=" />
	                    <input name="assignTime__type" type="hidden" value="dateTime" />

                	<span class="td_m">关闭时间：</span>
	             <input name="finalCompleteTime" type="text" id="date_text7" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text7'),document.getElementById('date_text7'),true)"></a> -
	                    <input name="finalCompleteTime_" type="text" id="date_text8" readonly="readonly" /><a href="#" class="dateButton" onclick="fPopCalendar(event,document.getElementById('date_text8'),document.getElementById('date_text8'),true)"></a>
	                    
	                     
	               <%--        <input type="text" id="date_text7" name="finalCompleteTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" /> -
	                    <input type="text" id="date_text8" name="finalCompleteTime_" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" />
	                 --%>     
	                    
	                     
	                     
	                     
	                    <input name="finalCompleteTime_operator" type="hidden" value=">=" />
	                    <input name="finalCompleteTime_type" type="hidden" value="dateTime" />
	                    <input name="finalCompleteTime__operator" type="hidden" value="&lt;=" />
	                    <input name="finalCompleteTime__type" type="hidden" value="dateTime" />
                 </td>
             </tr>
             <tr class="gd_search_toggle" style="display:none; border-top:1px solid #99BBE8;">
             	<td>
                 	<span class="td_m">基站等级：</span>
                     <select id="urgentRepair_queryTaskOrder_baseStationLevel" name="baseStationLevel">
                     	<option value="">请选择基站等级</option>
                     </select>
                     <input name="baseStationLevel_operator" type="hidden" value="=" />
                     <input name="baseStationLevel_type" type="hidden" value="select" />
                 </td>
             	<td>
                 	<%-- <span class="td_m">基站类型：</span>
                 	<select name="baseStationType">
                     	<option value="">请选择基站类型</option>
                     	<option>2G主设备</option>
                     	<option>2G直放站</option>
                         <option>TD主设备</option>
                         <option>TD直放站</option>
                     </select>
                     <input name="baseStationType_operator" type="hidden" value="=" />
                     <input name="baseStationType_type" type="hidden" value="select" /> --%>
                     
                     <span class="td_m">基站名称：</span>
                 	<input name="faultStationName" type="text" class="placeholder_text" placeholder="请输入基站名称" />
                 	<input name="faultStationName_operator" type="hidden" value="like" />
                     <input name="faultStationName_type" type="hidden" value="text" />
                 </td>
             	<td>
                 	<span class="td_m">受理专业：</span>
                 	<select id="urgentRepair_queryTaskOrder_acceptProfessional" name="acceptProfessional">
                     	<option value="">请选择受理专业</option>
                     	<%-- <option value="无线">无线</option>
                     	<option value="传输">传输</option>
                     	<option value="主设备">主设备</option> --%>
                     </select>
                     <input name="acceptProfessional_operator" type="hidden" value="=" />
                     <input name="acceptProfessional_type" type="hidden" value="select" />
                 </td>
             </tr>

             <tr>
             	<td class="tc" colspan="3">
                 	<input name="btnSubmit" id="urgentRepair_queryTaskOrder_btnSubmit" type="button" value="查询" />
                 </td>
             </tr>
         </table>
        </form>
    </div>
</div>
<%-- 抢修任务单查询 ############### end --%>




<%-- 车辆调度查询 ############## begin --%>
<div id="carDispatch_query" style="display:none;margin-bottom:30px;">
	<form id="form5">
		<div class="container-tab1">
	   		<div class="container-main">
	           <h4 class="search_bar clearfix">
	               <input type="hidden" id="searchBizunitIdText" name="creatorOrgId" />
	               <input type="hidden" name="creatorOrgId_operator" value="in" />
	               <input type="hidden" name="creatorOrgId_type" value="dataScope" />
	               
	               <input type="text" class="search_zuzhi" id="searchBizunitNameText" onfocus="$('#wrap_treeDiv').toggle('fast');" />
	               <a href="javascript:void(0);" style="margin-top:2px; margin-left:-29px;" class="select_button selectWorkPlaceButton" id="chooseAreaButton" title="选择组织"></a>
	               
	               <input name="useCarPersonName" type="text" class="placeholder_text" placeholder="请输入用车人姓名" />
	               <input name="useCarPersonName_operator" type="hidden" value="like" />
	               <input name="useCarPersonName_type" type="hidden" value="text" />
	               
	               <input name="woId" type="text" class="placeholder_text" placeholder="请输入工单号" />
	               <input name="woId_operator" type="hidden" value="like" />
	               <input name="woId_type" type="hidden" value="text" />
	               
	               
	               <input name="planUseCarTime" id="useCarTime" type="text" readonly="readonly" class="placeholder_text" placeholder="请输入用车时间" onclick="fPopCalendar(event,document.getElementById('useCarTime'),document.getElementById('useCarTime'),false)" />
	             
	            <%--  <input type="text" id="useCarTime" name="planUseCarTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text" />--%>
	             
	               <input name="planUseCarTime_operator" type="hidden" value="like" />
	               <input name="planUseCarTime_type" type="hidden" value="date" />
	               
	               <input name="btnSubmit" id="carDispatch_query_btnSubmit" type="button" value="查询" />
	               <input name="useCarPersonId_operator" type="hidden" value="like" />
	             	<input name="useCarPersonId_type" type="hidden" value="text" />
	               		<div id="treeDiv" class="select_tree_div" style="display:none;">
	    					<%-- 放置组织架构树 --%>
	    				</div>
	           </h4>
	       </div>
	   	</div>
   	</form>
   	<div id="carDispatchTabDiv">
   		<div id="tab" class="tab_menu">
			<ul></ul>
		</div>
   		<div id="tabBody" class="tab_middle">
			<div id="tab_0">
				<input type="hidden" value=""/>
			</div>
			<div id="tab_1" style="display:none;">
				<input type="hidden" value=""/>
			</div>
			<div id="tab_2" style="display:none;">
				<input type="hidden" value=""/>
			</div>
		</div>
   	</div>
</div>


<%-- 巡检待办任务查询 --%>
<div class="container" id="routineInspection_pendingTask" style="display:none;margin-bottom:30px;">
    
</div>



	
<div  style="margin-top:-30px;" id="result_div" ></div>


<%-- 字段选择导出对话框 --%>
<div id="copy_dialog" style="display:none;">
	<form id="dialogForm" action="exportQueryResultToExcelAction">
      <div class="black" style="display:block;"></div>
      <div class="dialog" style="width:650px; margin-left:-325px; margin-top:0px;">
          <div class="dialog_header">
              <div class="dialog_title">选择抢修工单导出字段</div>
              <div class="dialog_tool">
                 <div id="btnRightTopClose" class="dialog_tool_close copy_dialog_close"></div>
              </div>
          </div>
          <div class="dialog_content" style=" padding:0px;height:300px;">
          	<div class="dialog_left">
              	<ul class="dialog_tab">
                  	<li class="on">
                  		<input type="checkbox" />IOSM工单基本信息
                  		<input type="hidden" name="workOrderBasicInfo" value="workOrderBasicInfo" />
                  	</li>
                  	<li>
                  		<input type="checkbox" />IOSM工单回复信息
                  		<input type="hidden" name="workOrderReplyInfo" value="workOrderReplyInfo" />
                  	</li>
                  	<li><input type="checkbox" name="taskOrderBasicInfo" />IOSM任务单信息</li>
                  	<li><input type="checkbox" name="customerWorkOrderInfo" />客户工单信息</li>
                </ul>
              </div>
              <div class="dialog_right">
              	<div class="dialog_tab_content">
                  	<table class="zd_table">
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.woId" />工单号</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.woTitle" />工单主题</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultStationName" />告警基站</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.netElementName" />告警网元名称</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultArea" />区域</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.stationName" />站址名称</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultStationAddress" />站址地址</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.baseStationLevel" />基站等级</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.acceptProfessional" />受理专业</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultType" />故障类型</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultLevel" />故障级别</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultOccuredTime" />故障发生时间</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.latestAllowedTime" />故障处理时限</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultDescription" />故障描述</td>
                          	<td></td>
                          	<td></td>
                          </tr>
                      </table>
                  </div>
              	<div class="dialog_tab_content" style="display:none;">
                  	<table class="zd_table">
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.lastReplyPeopleName" />回复人</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.finalReplyTime" />回复时间</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultGenera" />故障大类</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultCause" />故障原因</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.sideeffectService" />是否影响业务</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.affectedServiceName" />受影响业务</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultDealResult" />故障处理结果</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.foreseeResolveTime" />预计解决时间</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.resonForDelayApply" />延迟解决原因</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.isAlarmClear" />告警是否清除</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.faultSolveTime" />告警清除时间</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.isOverTime" />工单是否超时</td>
                          </tr>
                      </table>
                  </div>
              	<div class="dialog_tab_content" style="display:none;">
                  	<table class="zd_table">
                      	  <%-- <tr>
                          	<td><input type="checkbox" />是否派发任务单</td>
                          	<td><input type="checkbox" />是否使用手持终端回单</td>
                          </tr> --%>
                          <tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.orderOwnerOrgId" />处理组织</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.orderOwner" />处理人</td>
                          </tr>
                      </table>
                  </div>
              	<div class="dialog_tab_content" style="display:none;">
                  	<table class="zd_table">
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerWoId" />客户工单编号</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerWoTitle" />客户工单主题</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerSenderDepartment" />派单人部门</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerWoSender" />派单人</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerWoCurrentDepartment" />处理人部门</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerWoCurrentHandler" />处理人</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.isEmergencyFault" />是否紧急故障</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.sendWoWay" />派单方式</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerSideeffectService" />是否影响业务</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.customerAffectedServiceName" />影响业务</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmFormalName" />告警标准名</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmNetManageSource" />告警网管来源</td>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmLogicalClass" />告警逻辑分类</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmLogicalSubClass" />告警逻辑子类</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmClass" />告警类别</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.netmanageAlarmLevel" />网管告警级别</td>
                          	
                          	<%-- <td><input type="checkbox" name="urgentRepairWorkOrderMap.orderOwner" />网元名称</td> --%>
                          </tr>
                      	<tr>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.alarmAssociatedId" />告警关联标识</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.subAlarmNumber" />子告警数量</td>
                          	<td><input type="checkbox" name="urgentRepairWorkOrderMap.subAlarmInfo" />子告警信息</td>
                          </tr>
                      </table>
                  </div>
              </div>
              <div class="dialog_but" style="position:absolute; bottom:12px; width:100%; *+margin-left:-150px;">
                  <button type="button" class="aui_state_highlight copy_dialog_close" id="btnSubmitDialog">确定</button>
                  <button type="button" class="aui_state_highlight copy_dialog_close" id="btnCancelDialog">取消</button>
              </div>
          </div>
      </div>
      </form>
  </div>

</body>
</html>