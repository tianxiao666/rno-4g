/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.BLANK_IMAGE_URL="jslib/extjs/resources/images/default/s.gif";
Ext.onReady(function(){

    // NOTE: This is an example showing simple state management. During development,
    // it is generally best to disable state management as dynamically-generated ids
    // can change across page loads, leading to unpredictable results.  The developer
    // should ensure that stable state ids are set for stateful components in real apps.
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    // create some portlet tools using built in Ext tool ids
    var tools = [{
        id:'gear',
        handler: function(){
            Ext.Msg.alert('Message', 'The Settings tool was clicked.');
        }
    },{
        id:'close',
        handler: function(e, target, panel){
            panel.ownerCt.remove(panel, true);
        }
    }];

    var viewport = new Ext.Viewport({
        layout:'border',
        items:[{
            xtype:'portal',
            frame:false,
            region:'center',
            margins:'0 0 0 0',
            items:[{
                columnWidth:.4,
                style:'padding:5px 0 5px 5px',
                items:[{
                    title: 'Grid in a Portlet',
                    layout:'fit',
                    tools: tools,
                    items: new SampleGrid([0, 2, 3])
                },{
                    title: 'Another Panel 1',
                    tools: tools,
                    html: "<iframe width='100%' height='100%' name='baidu' src='http://www.baidu.com'/>"
                }]
            },{
                columnWidth:.4,
                style:'padding:5px 0 5px 5px',
                items:[{
                    title: 'Panel 2',
                    tools: tools,
                    html: Ext.example.shortBogusMarkup
                },{
                    title: 'Another Panel 2',
                    tools: tools,
                    html: Ext.example.shortBogusMarkup
                }]
            },{
                columnWidth:.2,
                style:'padding:5px',
                items:[{
                    title: 'Panel 3',
                    tools: tools,
                    html: Ext.example.shortBogusMarkup
                },{
                    title: 'Another Panel 3',
                    tools: tools,
                    html: Ext.example.shortBogusMarkup
                }]
            }]
            
            /*
             * Uncomment this block to test handling of the drop event. You could use this
             * to save portlet position state for example. The event arg e is the custom 
             * event defined in Ext.ux.Portal.DropZone.
             */
//            ,listeners: {
//                'drop': function(e){
//                    Ext.Msg.alert('Portlet Dropped', e.panel.title + '<br />Column: ' + 
//                        e.columnIndex + '<br />Position: ' + e.position);
//                }
//            }
        }]
    });
});

