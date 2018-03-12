
//工单查询操作
function workOrderInputQuery(){

	var form1 = Ext.getCmp("queryInputForm");
	var formValue = Ext.encode(form1.form.getValues());
	
	var resultGrid = Ext.getCmp('workorder_grid_grid');
	var store = resultGrid.getStore();	
	store.baseParams = {queryParams:formValue}  
	//store.removeAll();
	store.reload();
	
}

//工单重置操作
function workOrderInputReset(){
	var form1 = Ext.getCmp("queryInputForm");
	form1.form.reset();
}

//任务单查询操作
function taskOrderInputQuery(){
	var form1 = Ext.getCmp("queryInputForm");
	var formValue = Ext.encode(form1.form.getValues());
	//alert(formValue);
	
	var resultGrid = Ext.getCmp('taskorder_grid_grid');
	var store = resultGrid.getStore();	
	store.baseParams = {queryParams:formValue}  
	store.reload();
	
}

//任务单重置操作
function taskOrderInputReset(){
	var form1 = Ext.getCmp("queryInputForm");
	form1.form.reset();
}





//带tab的查询
function workOrderInputQueryInTab(){

	var form1 = Ext.getCmp("queryInputForm");
	var formValue = Ext.encode(form1.form.getValues());
	
	var tabPanel = Ext.getCmp('result_tab');
	var activeTab=tabPanel.getActiveTab();
	var grid=activeTab.items.itemAt(0);
	
	var store = grid.getStore();
	store.baseParams = {queryParams:formValue};
	store.reload();
	
	//alert("grid==="+grid.id);
}



