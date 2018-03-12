


function createButtonAction(actionURL){
	window.open(actionURL);
}




Ext.onReady(function(){
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();
		
	//多选
 	var check_select = new Ext.grid.CheckboxSelectionModel();

	var track_dataUrl = 'getDraftWorkOrdersByUserId.action';
	var track_store = new Ext.data.JsonStore({
	    url: track_dataUrl,
	    root: 'result',
	    totalProperty: 'totalCount',
	    remoteSort: true,
	    fields: ['woId', 'woTitle', 'creator', 'createTime','requireCompleteTime','statusName']
	    //sortInfo: {field:'_order', direction:'ASC'}
	});
	track_store.setDefaultSort('CREATETIME', 'desc');


    var track_grid = new Ext.grid.GridPanel({
        //width:700,
        height:300,
        //autoHeight:true,
        monitorResize:true,
        //title:'草稿工单列表',
        store: track_store,
        padding: '5 5 5 5',
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
		sm:check_select,
        // grid columns
        columns:[
	        new Ext.grid.RowNumberer(),check_select, 
	        {
	            id: 'woId', 
	            header: "工单号",
	            dataIndex: 'woId',
	            width: 250,
	            align: 'center',
	            //renderer: renderTopic,
	            sortable: true
	        },{
	            header: "工单主题",
	            dataIndex: 'woTitle',
	            width: 250,
	            align: 'center',
	            sortable: true
	        },{
	            header: "创建人",
	            dataIndex: 'creator',
	            width: 150,
	            align: 'center',
	            sortable: true
	        },{
	            id: 'createTime',
	            header: "创建时间",
	            dataIndex: 'createTime',
	            width: 200,
	            align: 'center',
	            //renderer: renderLast,
	            sortable: true
	        },
	        {
	            id: 'requireCompleteTime',
	            header: "要求完成时间",
	            dataIndex: 'requireCompleteTime',
	            width: 200,
	            align: 'center',
	            //renderer: renderLast,
	            sortable: true
	        },
	        {
	            id: 'statusName',
	            header: "工单状态",
	            align: 'center',
	            dataIndex: 'statusName',
	            width: 100,
	            sortable: true
	        }
        ],
        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: 25,
            store: track_store,
            displayInfo: true,
            displayMsg: '显示 {0} - {1} of {2}',
            emptyMsg: "没有草稿记录"
        }),        
        listeners : {  
            'rowdblclick':function(grid, rowIndex,e){  
            	var record=grid.getSelectionModel().getSelected();
                //alert("你双击了第" + rowIndex + 1 + "行!");  
                //alert(record.get('WOID'));
            }  
        }
    });

    track_store.load({params:{start:0, limit:25}});
    
    
    
	var fsf = new Ext.FormPanel({
        labelWidth: 75, // label settings here cascade unless overridden
        monitorResize:true,
        frame:false,
        border:false,
        bodyStyle:'padding:5px 5px 5px 5px',
        applyTo:"draftWorkOrder-Grid",
        items: [{
            xtype:'fieldset',
            width:width-10,
            checkboxToggle:true,
            title: '草稿工单列表',
            autoHeight:true,            
            collapsed: false,
            items :[track_grid]
        }
        ]
    });
	
});