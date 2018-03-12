<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<script type="text/javascript" src="js/gerneral_pay.js"></script>

<h4 class="content_m_tit">
                            <span class="right_tool_bar">
                            	<select id="feeType">
                                	<option value="null">费用类型</option>
                                	<option value="路桥费">路桥费</option>
                                	<option value="油费">油费</option>
                                	<option value="停车费">停车费</option>
                                	<option value="其他">其他</option>
                                </select>
                                <input type="text" id="search_pay_fromDate" value="" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('search_pay_fromDate'),document.getElementById('search_pay_fromDate'),false)"></a>
                                <input type="text" id="search_pay_endDate" value="" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('search_pay_endDate'),document.getElementById('search_pay_endDate'),false)"></a>
                                <select id="feeAmount">
                                	<option value="null">费用范围</option>
                                	<option value="0~20">0~20</option>
                                	<option value="20~50">20~50</option>
                                	<option value="50~100">50~100</option>
                                	<option value="100">100以上</option>
                                </select>
                                <a href="#" class="search_button" id="carPay_search_btn" ></a>
                            </span>
                        </h4>
                        <div class="table_div">
                            <table class="pi_table2 hover_tr tc" id="pay_table">
                            	<thead>
                            		<tr>
	                                    <th>用车类型</th>
	                                    <th>行车费用</th>
	                                    <th>费用说明</th>
	                                    <th>任务编号</th>
	                                    <th>费用产生时间</th>
	                                    <th>负责人</th>
	                                </tr>
                            	</thead>
                            	<tbody>
                            	</tbody>
                            </table>
        				</div>