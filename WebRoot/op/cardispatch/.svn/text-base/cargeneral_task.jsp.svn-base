<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<script type="text/javascript" src="js/gerneral_task.js"></script>
<script type="text/javascript" src="js/util/numberutil.js"></script>

<style>
	.task_f_em{color:red;}
</style>

<h4 class="content_m_tit">
	<input type="checkbox" checked="checked" id="task_unfinish" name="task_isFinish" />
	<label  class="task_isFinish_label" >未完成</label>&nbsp;(<em id="task_unfinish_em" class="task_f_em"></em>)&nbsp;
	<input type="checkbox"  id="task_finish" name="task_isFinish"/>
	<label  class="task_isFinish_label" >已完成</label>&nbsp;(<em id="task_finish_em" class="task_f_em"></em>)&nbsp;
	<span class="right_tool_bar"> 
		<select id="associateWorkType" >
			<option value="">用车类型</option>
			<option value="urgentrepair">抢修用车</option>
			<option value="routineinspection">巡检用车</option>
			<option value="car">申请用车</option>
		</select> 
		<input type="text" readonly="readonly" id="task_beginTime" value="" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('task_beginTime'),document.getElementById('task_beginTime'),false)"></a>
		<input type="text" readonly="readonly" id="task_endTime" value="" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('task_endTime'),document.getElementById('task_endTime'),false)"></a>
		<input type="text" value="" id="woTitle" /><a href="#" class="search_button" id="carTask_search_btn"></a>
	</span>
</h4>
<div class="table_div" id="task_div">
	<table class="pi_table2 hover_tr tc" id="task_table">
		<thead>
			<tr>
				<th>
					用车类型
				</th>
				<th>
					任务编号
				</th>
				<th>
					实际用车时间
				</th>
				<th>
					实际还车时间
				</th>
				<th>
					历时（小时）
				</th>
				<th>
					GPS里程（KM）
				</th>
				<th>
					实际里程（KM）
				</th>
				<th>
					行车费用
				</th>
				<th>
					行车轨迹
				</th>
			</tr>
		</thead>
		<tbody></tbody>
		<tfoot>
			<tr>
				<td colspan="9"></td>
			</tr>
		</tfoot>
	</table>
</div>