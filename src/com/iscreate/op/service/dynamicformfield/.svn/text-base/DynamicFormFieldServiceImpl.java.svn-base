package com.iscreate.op.service.dynamicformfield;

import javax.servlet.http.HttpServletRequest;
import com.iscreate.op.dao.dynamicformfield.*;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.iscreate.op.pojo.dynamicformfield.*;
import java.util.*;
import java.sql.*;

public class DynamicFormFieldServiceImpl implements DynamicFormFieldService {

	private DynamicFormFieldDao dynamicFormFieldDao;

	private HibernateTemplate hibernateTemplate;

	/***************************************************************************
	 * 获取动态表单区域的html文本
	 * 
	 * @param areaCode
	 *            业务区
	 * @param formCode
	 *            动态表单的编码
	 * @return
	 */
	public String getDynamicFormFieldDivHtml(String areaCode, String formCode,
			String rowId) {
		String devHtml = "     ";// 
		List<FormControl> listC = this.dynamicFormFieldDao
				.queryFormControl(formCode);

		List listFV = this.dynamicFormFieldDao.queryFormValue(rowId, formCode);// 提取原有设置值

		int col = 2;
		int count = 0;
		String tr = "";
		if (listC != null && listC.size() > 0) {
			// 提取控件内容
			for (FormControl formControl : listC) {
				// 从数据库中提取控件值
				String value = "";
				for (int i = 0; i < listFV.size() && listFV != null; i++) {
					FormValue fv = (FormValue) listFV.get(i);
					if (fv.getName().equals(formControl.getName())) {
						value = fv.getValue();
					}
				}
				// //////////////////////////////////////////////
				if (count % col == 0 && count != 0)
					tr += "</tr><tr>";
				if (count == 0)
					tr += "<tr>";
				count++;
				// 控件拼装成页面要显示的内容
				if (formControl.getControlTag().toLowerCase().equals("select")) {
					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";
					// 控件的属性值
					SelectTag tag = new SelectTag();
					tag.init(String.valueOf(formControl.getControlId()));
					td += "<td >" + "<select  name='" + formControl.getName()
							+ "' id='" + formControl.getName() + "'   >"
							+ tag.showPart_2(value) + "</select>" + "</td>";
					tr += td;
				} else if (formControl.getControlTag().toLowerCase().equals(
						"text")) {
					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";
					// TODO 控件的属性值
					td += "<td>" + "<input type='text' name='"
							+ formControl.getName() + "'   id='"
							+ formControl.getName() + "'   value=" + value
							+ "  >" + "</td>";
					tr += td;
				} else if (formControl.getControlTag().toLowerCase().equals(
						"textarea")) {
					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";
					td += "<td>" + "<textarea  name='" + formControl.getName()
							+ "'   id='" + formControl.getName() + "'   >"
							+ value + "</textarea  >" + "</td>";
					tr += td;

				} else if (formControl.getControlTag().toLowerCase().equals(
						"date")) {

					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";

					td += "<td>"
							+ "<input type='text' name='"
							+ formControl.getName()
							+ "'   id='"
							+ formControl.getName()
							+ "'   value="
							+ value
							+ "  >"
							+ " <span > </span> "
							+ "  <input type='button' value='时间' "
							+ "  onclick='fPopCalendar(event,document.getElementById(\""
							+ formControl.getName()
							+ "\"),document.getElementById(\""
							+ formControl.getName() + "\"),true)' />"
							+ " </td> ";
					tr += td;

				} else if (formControl.getControlTag().toLowerCase().equals(
						"checkbox")) {
					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";
					// TODO 控件的属性值
					td += "<td>" + "<input type='checkbox' name='"
							+ formControl.getName() + "'   id='"
							+ formControl.getName() + "'   value=" + value
							+ "  >" + "</td>";
					tr += td;

				} else if (formControl.getControlTag().toLowerCase().equals(
						"number")) {
					String td = "<td  class='menuTd'>"
							+ formControl.getCnName() + ": </td>";
					// TODO 控件的属性值
					td += "<td>" + "<input type='number' name='"
							+ formControl.getName() + "' id='"
							+ formControl.getName() + "' >" + "</td>";
					tr += td;
				} else if (formControl.getControlTag().toLowerCase().equals(
						"redio")) {
					RedioTag tag = new RedioTag();
					tag.init(String.valueOf(formControl.getControlId()));
					String td = "<td class='menuTd'>" + formControl.getCnName()
							+ ": </td>";
					// TODO 控件的属性值
					td += "<td>" + tag.showPart_2(value, formControl.getName())
							+ "</td>";
					tr += td;
				} else {

				}
			}
		}
		devHtml += tr + "</tr>" + " ";
		return devHtml;
	}

	class RedioTag {

		public List attributeList = new ArrayList();

		/**
		 * 控件id
		 * 
		 * @param id
		 */
		public void init(String controlId) {
			// 提取属性
			List<FormControlAttribute> listA = dynamicFormFieldDao
					.queryFormControlAttribute(controlId);
			if (listA != null && listA.size() > 0) {
				for (FormControlAttribute attribute : listA) {
					String[] strA = { attribute.getAttributeId(),
							attribute.getAttributeValue() };
					attributeList.add(strA);
				}
			}
		}

		/***********************************************************************
		 * 对 sql , valueField 、 displayField 属性的处理
		 * 
		 * @return
		 */
		public String showPart_2(String selectedValue, String enName) {
			String htmlStr = "", sql = "", id = "", name = "";

			for (int i = 0; i < attributeList.size(); i++) {
				String[] strA = (String[]) attributeList.get(i);
				if (strA[0].toLowerCase().equals("sql")) {
					sql = strA[1];// TODO 参数处理
				} else if (strA[0].toLowerCase().equals("valuefield")) {
					id = strA[1];
				} else if (strA[0].toLowerCase().equals("displayfield")) {
					name = strA[1];
				} else {
					continue;
				}
			}
			// 查询出selected的内容
			Connection connection = hibernateTemplate.getSessionFactory()
					.openSession().connection();
			Statement state = null;
			try {
				state = connection.createStatement();

				ResultSet rst = state.executeQuery(sql);
				while (rst.next()) {
					if (selectedValue.equals(rst.getString(id))) {
						htmlStr += "<input type='radio'  name='" + enName
								+ "'  value = '" + rst.getString(id)
								+ "' checked >" + rst.getString(name) + "";
					} else {
						htmlStr += "<input type='radio'  name='" + enName
								+ "'  value = '" + rst.getString(id) + "'  >"
								+ rst.getString(name) + "";
					}
				}
				rst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					state.close();
					connection.close();
				} catch (Exception ex) {
				}

			}
			return htmlStr;
		}
	}

	class SelectTag {
		public List attributeList = new ArrayList();

		/**
		 * 控件id
		 * 
		 * @param id
		 */
		public void init(String controlId) {
			// 提取属性
			List<FormControlAttribute> listA = dynamicFormFieldDao
					.queryFormControlAttribute(controlId);
			if (listA != null && listA.size() > 0) {
				for (FormControlAttribute attribute : listA) {
					String[] strA = { attribute.getAttributeId(),
							attribute.getAttributeValue() };
					attributeList.add(strA);
				}
			}
		}

		/***********************************************************************
		 * 除 sql , valueField 、 displayField 属性外的所有属性转换为html的处理
		 * 
		 * 
		 * @return
		 */
		public String showPart_1() {
			String htmlStr = "";
			for (int i = 0; i < attributeList.size(); i++) {
				String[] strA = (String[]) attributeList.get(i);
				if (strA[0].toLowerCase().equals("sql")) {
					continue;
				} else if (strA[0].toLowerCase().equals("valuefield")) {
					continue;
				} else if (strA[0].toLowerCase().equals("displayfield")) {
					continue;
				} else {

				}
			}
			return htmlStr;
		}

		/***********************************************************************
		 * 对 sql , valueField 、 displayField 属性的处理
		 * 
		 * @return
		 */
		public String showPart_2(String selectedValue) {
			String htmlStr = "", sql = "", id = "", name = "";

			for (int i = 0; i < attributeList.size(); i++) {
				String[] strA = (String[]) attributeList.get(i);
				if (strA[0].toLowerCase().equals("sql")) {
					sql = strA[1];// TODO 参数处理
				} else if (strA[0].toLowerCase().equals("valuefield")) {
					id = strA[1];
				} else if (strA[0].toLowerCase().equals("displayfield")) {
					name = strA[1];
				} else {
					continue;
				}
			}
			// 查询出selected的内容
			Connection connection = hibernateTemplate.getSessionFactory()
					.openSession().connection();
			Statement state = null;
			try {
				state = connection.createStatement();

				ResultSet rst = state.executeQuery(sql);
				while (rst.next()) {
					if (selectedValue.equals(rst.getString(id))) {
						htmlStr += "<option value='" + rst.getString(id)
								+ "'  selected  >" + rst.getString(name)
								+ "</option>";
					} else {
						htmlStr += "<option value='" + rst.getString(id) + "'>"
								+ rst.getString(name) + "</option>";
					}
				}
				rst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					state.close();
					connection.close();
				} catch (Exception ex) {
				}

			}
			return htmlStr;
		}

	}

	/***************************************************************************
	 * 
	 * 
	 * 
	 * 
	 * @param areaCode
	 * @param formCode
	 * @param request
	 * @return
	 */
	public String txDynamicFormFieldSubmit(String rowId, String areaCode,
			String formCode, HttpServletRequest request) {

		List listfv = new ArrayList();// 所需入库的FormValue的集合

		List listFV = this.dynamicFormFieldDao.queryFormValue(rowId, formCode);// 提取一条主记录的FormValue集合

		List<FormControl> listC = this.dynamicFormFieldDao
				.queryFormControl(formCode);// 提取定义好的控件

		if (listFV == null || listFV.size() == 0) {
			if (listC != null && listC.size() > 0) {
				// 提取控件内容
				for (FormControl formControl : listC) {
					FormValue fv = new FormValue();
					fv.setFormCode(formCode);
					fv.setAreacode(areaCode);
					fv.setRowId(rowId);
					fv.setName(formControl.getName());
					fv.setValue(request.getParameter(formControl.getName()));
					listfv.add(fv);
				}
			}
		} else {
			for (int i = 0; i < listFV.size(); i++) {
				FormValue oldfv = (FormValue) listFV.get(i);
				oldfv.setValue(request.getParameter(oldfv.getName()));
				listfv.add(oldfv);

			}
		}
		// 循环入库
		for (int i = 0; i < listfv.size(); i++) {
			this.dynamicFormFieldDao.submitFormValue((FormValue) listfv.get(i));
		}
		return "seccuss";
	}

	public DynamicFormFieldDao getDynamicFormFieldDao() {
		return dynamicFormFieldDao;
	}

	public void setDynamicFormFieldDao(DynamicFormFieldDao dynamicFormFieldDao) {
		this.dynamicFormFieldDao = dynamicFormFieldDao;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
