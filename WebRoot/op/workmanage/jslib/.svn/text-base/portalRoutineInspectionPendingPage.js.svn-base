Ext.onReady(function(){
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();
	
	var dataUrl = 'getRoutineInspectionPendingTaskOrderAction';
	var dataStore = new Ext.data.JsonStore({
	    url: dataUrl,
	    root: 'result',
	    totalProperty: 'totalCount',
	    remoteSort: true,
	    fields: ['id','woId', 'toId', 'toTitle', 'assignTime','requireCompleteTime','taskPlanBeginTime','taskPlanEndTime','statusName','formUrl']
	});
	dataStore.setDefaultSort('assignTime', 'desc');
	
	
	var grid = new Ext.grid.GridPanel({
        height:320,
        width:907,
        store: dataStore,
        padding: '5 5 5 5',
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        columns:[
        new Ext.grid.RowNumberer(), 
        {
            id: 'toId', 
            header: "任务单号",
            dataIndex: 'toId',
            width: 180,
            align: 'center'
        },{
            header: "任务单标题",
            dataIndex: 'toTitle',
            width: 270,
            align: 'center'
        },
        {
            id: 'taskPlanBeginTime',
            header: "计划开始时间",
            dataIndex: 'taskPlanBeginTime',
            width: 150,
            align: 'center'
        },
        {
            id: 'taskPlanEndTime',
            header: "计划结束时间",
            align: 'center',
            dataIndex: 'taskPlanEndTime',
            width: 150
        }
        ],
        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: 10,
            store: dataStore,
            displayInfo: true,
            displayMsg: '显示 {0} - {1} of {2}',
            emptyMsg: "没有待办工单记录"
           
        }),
        listeners : {  
            'rowdblclick':function(grid, rowIndex,e){
            	var woId = grid.getStore().getAt(rowIndex).data.woId;
                var toId = grid.getStore().getAt(rowIndex).data.toId;
                var formUrl = grid.getStore().getAt(rowIndex).data.formUrl;
                var url = formUrl+"?WOID="+woId+"&TOID="+toId;
		        window.open(url,"_blank");
            }
        }
    });
    
    dataStore.load({params:{start:0, limit:10}});

    
    var viewport = new Ext.Viewport({
        layout:'border',
        items:[
			{
                region: 'center',
                split: false,    
                border:false,            
                margins: '0 0 0 0',       
                items:[grid]
            }
        ]
    });
    
    
	

    
    //自动刷新
    setInterval(function(){
    	dataStore.reload();
    },1000*60*2);
    
    
    
});



