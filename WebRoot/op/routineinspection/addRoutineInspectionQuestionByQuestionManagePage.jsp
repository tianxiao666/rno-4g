<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加新问题</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />

</head>

<body>

	<div class="container">
        <div class="content">
        	<div class="content_step">
            	<table class="thleft_table">
                    <tr>
                        <th colspan="4"><em class="edit_ico">添加新问题</em></th>
                    </tr>
                    <tr>
                        <td class="label_td">问题类型：</td>
                        <td>
                            <select>
                            	<option>请选择</option>
                            </select>
                        </td>
                        <td class="label_td">严重程度：</td>
                        <td>
                        	<input type="radio" />严重
                        	<input type="radio" />一般
                        	<input type="radio" />次要
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">问题描述：</td>
                        <td colspan="3">
                            <textarea style="width:606px;"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">问题图片：</td>
                        <td colspan="3">
                            <input type="file" />
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">关联资源：</td>
                        <td colspan="3">
                            <input type="text" value="金山大厦" readonly="readonly" style="width: 125px;"/>
                        </td>
                    </tr>
                </table>
                <div class="content_bottom">
                    <input type="button" value="保 存" />
                    <input type="button" value="取 消" />
                </div>
            </div>
        </div>
    </div>
</body>
</html>
