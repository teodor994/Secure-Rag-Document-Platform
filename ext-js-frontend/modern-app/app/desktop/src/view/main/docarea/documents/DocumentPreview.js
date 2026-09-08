Ext.define('ModernApp.view.main.docarea.documents.DocumentPreview', {
    extend: 'Ext.Dialog',
    xtype: 'documentPreview',
    controller: 'documents',
    config: {
        title: 'Document Preview',
        width: '90%',
        height: '90%',
        modal: true,
        maximizable: true,
        resizable: true,
        scrollable: true,
        destroyOnHide: true,
        // masked: {
        //     xtype: 'loadmask',
        //     message: 'Loading document...'
        // }

    },

    items: [
        {
            xtype: 'component',
            reference: 'docContent',
            itemId: 'docContent'
        }
    ],

    buttons: [
        {
            text: 'Close',
            handler: 'onCloseTap'
        }
    ],

    listeners: {
        destroy: 'onDialogDestroy'
    }
})