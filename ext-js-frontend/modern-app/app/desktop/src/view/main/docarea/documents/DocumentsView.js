Ext.define('ModernApp.view.main.docarea.documents.DocumentsView', {
    extend: 'Ext.Container',
    xtype: 'documents',

    controller: 'documents',

    requires: [
        'ModernApp.view.main.docarea.documents.DocumentsController',
        'ModernApp.store.DocumentsStore'
    ],

    layout: { type: 'vbox', align: 'stretch' },

    items: [
        {
            xtype: 'container',
            flex: 1,
            reference: 'currentPathBar',
            cls: 'current-path-label',
            html: '<b>Path:</b> /'
        },
        {
            xtype: 'dataview',
            flex: 9,
            reference: 'documentsGrid',
            cls: 'documents-dataview',
            store: { type: 'documents' },
            scrollable: true,
            itemTpl: [
                '<div class="doc-tile">',
                '<tpl if="type === \'folder\'">',
                '<img src="resources/images/folder.png" class="doc-icon" />',
                '<tpl else>',
                '<img src="resources/images/document.png" class="doc-icon" />',
                '</tpl>',
                '<div class="doc-name">{name:htmlEncode}</div>',
                '</div>'
            ],
            listeners: {
                itemtap: 'onNodeTap',
                itemdoubletap: 'onNodeDoubleTap',
                itemcontextmenu: 'onNodeRightClick',
                initialize: 'onDocumentsGridInit'
            }
        },
        {
            xtype: 'toolbar',
            docked: 'bottom',
            layout: {
                type: 'hbox',
                pack: 'center',
                align: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    text: '< Prev',
                    handler: 'onPrevPage',
                    reference: 'prevBtn',
                    disabled: true
                },
                {
                    xtype: 'label',
                    reference: 'pageInfoLabel',
                    html: 'Page X of Y',
                    style: 'margin: 0 12px; font-weight: 600;'
                },
                {
                    xtype: 'button',
                    text: 'Next >',
                    handler: 'onNextPage',
                    reference: 'nextBtn',
                    disabled: true
                }
            ]

        },
        {
            xtype: 'toolbar',
            layout: { type: 'hbox', pack: 'center' },
            bind: {
                hidden: '{!isAdmin}'
            },
            items: [
                {
                    xtype: 'button',
                    handler: 'onAddFilePress',
                    text: 'Add File'
                },
                {
                    xtype: 'button',
                    handler: 'onAddFolderPress',
                    text: 'Create New Folder'
                },
                // {
                //     xtype: 'button',
                //     // bind: {
                //     //     hidden: '{isRoot}'
                //     // },
                //     text: 'Add Permission',
                //     handler: 'onAddPermissionPress'
                // },
                {
                    xtype: 'button',
                    text: 'Remove Permission',
                    handler: 'onRemovePermissionPress'
                }
            ]
        }
    ]
});