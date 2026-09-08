Ext.define('ModernApp.view.main.docarea.textsearch.TextSearchView', {
    extend: 'Ext.Container',
    xtype: 'search',
    controller: 'textsearch',

    requires: [
        'ModernApp.view.main.docarea.textsearch.TextSearchView'
    ],

    width: '100%',

    layout: {
        type: 'vbox',
        align: 'center',
        pack: 'start'
    },

    items: [
        {
            xtype: 'container',
            layout: 'vbox',
            width: '60%',
            margin: '30 0 0 0',
            items: [
                {
                    xtype: 'textareafield',
                    reference: 'searchInput',
                    flex: 1,
                    height: 40,
                    placeholder: 'Type here...'
                },
                {
                    xtype: 'button',
                    text: 'Search',
                    margin: '0 0 0 10',
                    handler: 'onSearchTap'
                }
            ]
            // xtype: 'textareafield',
            // width: '50%',
            // height: 40,
            // margin: '30 0 0 0',
            // placeholder: 'Tasteaza aici...'
        },
        {
            xtype: 'component',
            reference: 'searchStatus',
            width: '60%',
            margin: '10 0 0 0',
            html: ''
        },
        {
            xtype: 'dataview',
            reference: 'searchResultsGrid',
            width: '100%',
            flex: 1,
            margin: '10 10 10 10',
            layout: 'vbox',
            cls: 'documents-dataview',
            store: {
                fields: ['nodeId', 'name', 'path', 'snippet']
            },
            itemTpl: [
                '<div>',
                '<div>{name:htmlEncode}</div>',
                '<div>{path:htmlEncode}</div>',
                '<div>{snippet:htmlEncode}</div>',
                '</div>'
            ],
            itemCls: 'rag-result',
            listeners: {
                itemtap: 'onResultTap'
            }
        }
    ]
});