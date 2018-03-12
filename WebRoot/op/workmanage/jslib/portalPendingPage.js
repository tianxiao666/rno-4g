Ext.onReady(function(){
	var height = Ext.get(document.body).getHeight();
	var width = Ext.get(document.body).getWidth();
	
	var data;
	Ext.Ajax.request({
        url:"loadPortalUrgentRepairWorkOrderTabResourceAction.action",
        method:'post',
        success: function (response, options) {
            //alert("response=="+response);
            //alert("response.responseText=="+response.responseText);
            data=eval("("+response.responseText+")");
            
            var tabs = new Ext.TabPanel({
            	id:"portalTab",
		        resizeTabs:true, 
		        tabWidth:100,
		        enableTabScroll:true,
		        border:false, 
		        height:350,
		        defaults: {autoScroll:true},
		        activeTab:0,
		        listeners:{
					'tabchange':function(tabPanel){
						/*
							remove
						if(tabPanel.activeTab.id=="urgentRepair_pendingWorkOrderTab"){
							pending_store.load({params:{start:0, limit:7}});
						}else if(tabPanel.activeTab.id=="urgentRepair_traceWorkOrderTab"){
							track_store.load({params:{start:0, limit:7}});
						}else if(tabPanel.activeTab.id=="urgentRepair_superviseWorkOrderTab"){
							supervise_store.load({params:{start:0, limit:7}});
						}*/
						var activeTab=tabPanel.getActiveTab();
						var grid=activeTab.items.itemAt(0);
						var store = grid.getStore();
						store.load({params:{start:0, limit:10}});
					}
				}
		    });
            
            constructPortalTab(data,tabs);	//为tabPanel构建Tab对象
            
            var viewport = new Ext.Viewport({
		        layout:'border',
		        items:[
					{
		                region: 'center',
		                id: 'center-panel', 
		                //title:'待办工单',
		                split: false,    
		                border:false,            
		                margins: '0 0 0 0',       
		                items:[tabs]         
		            }
		         ]
		    });
        }, 
        failure: function (response, options) { 
            Ext.MessageBox.alert('失败', '请求超时或网络故障,错误编号：' + response.status); 
        }
    })

    
    //che.yd---------begin-------
    //自动刷新
    setInterval(function(){
    	//pending_store.reload();
    	//track_store.reload();

    	var tabPanel = Ext.getCmp('portalTab');
    	var tabSize=tabPanel.items.length;
		for(var i=0;i<tabSize;i++){
			var tab=tabPanel.items.get(i)
			var grid=tab.items.itemAt(0);
			var store = grid.getStore();
			store.reload();
		}
    },1000*60*2);
    //che.yd----------end--------------
    
    
    
});


function constructPortalTab(data,tabs){
	if(data && data!=""){
		for(var key in data){
			var tempData=data[key];
			var tempObj=tempData[0];
			var menuName=tempObj.menuName;
			var menuCode=tempObj.menuCode;
			//alert("menuName=="+menuName+",menuCode=="+menuCode);
			var tabObj={};
			
			//构建store
			var check_select = new Ext.grid.CheckboxSelectionModel();	//多选
			var dataUrl=tempObj.queryAction+"?queryCondition="+tempObj.queryCondition+"&queryEntityName="+tempObj.queryEntityName+"&filterBiz="+tempObj.filterBiz+"&filterProp="+tempObj.filterProp+"";
			var dataStore=new Ext.data.JsonStore({
				url:dataUrl,
				root:"result",
				totalProperty:"totalCount",
				remoteSort:true,
				//fields:["_NOWTIME","_HALFLATERTIME",tempObj.resultField]
				fields:["_NOWTIME","_HALFLATERTIME",'id','woId', 'woTitle', 'creator', 'creatorOrgId','createTime','requireCompleteTime','statusName','formUrl']
			});
			dataStore.setDefaultSort(tempObj.sortName,tempObj.sortType);
			
			//构建gridPanel
			var grid=new Ext.grid.GridPanel({
				id:menuCode+"_grid",
				height:320,
				store: dataStore,
				padding: '5 5 5 5',
		        trackMouseOver:true,
		        disableSelection:false,
		        loadMask: true,
		        //sm:check_select,
		        columns:[
		        	new Ext.grid.RowNumberer(),
		        	//check_select,
		        	{
			            id: 'woId', 
			            header: "工单号",
			            dataIndex: 'woId',
			            width: 180,
			            align: 'center'
			        },{
			            header: "工单主题",
			            dataIndex: 'woTitle',
			            width: 300,
			            align: 'center'
			        },
			        {
			            id: 'requireCompleteTime',
			            header: "要求完成时间",
			            dataIndex: 'requireCompleteTime',
			            width: 150,
			            align: 'center'
			        },
			        {
			            id: 'statusName',
			            header: "工单状态",
			            align: 'center',
			            dataIndex: 'statusName',
			            width: 100
			        }
		        ],
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
		                var formUrl = grid.getStore().getAt(rowIndex).data.formUrl;
		                var creatorOrgId = grid.getStore().getAt(rowIndex).data.creatorOrgId;
		                var url = formUrl+"?WOID="+woId;
		                window.open(url,"_blank");
		            }
		        }
			});
			//tabObj.title=menuName;
			//tabObj.id=menuCode+"Tab";
			//tabObj.items=grid;
			//tabObj.closable=true;
			tabs.add({
				title:menuName,
				id:menuCode+"Tab",
				items:[
					grid
				]
			});
		}
	}
}

