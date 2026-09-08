Ext.define('ModernApp.view.main.docarea.documents.AddFilePopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'add-file-pop-up',

    title: 'Drag and drop a new file to upload',
    modal: true,
    centered: true,
    width: 400,
    closeAction: 'destroy',

    items: [
        {
            xtype: 'component',
            height: 150,
            listeners: {
                element: 'element',
                dragover: function (e) {
                    e.preventDefault();
                    this.setStyle('backgroundColor', '#929ab4');
                },
                dragleave: function (e) {
                    e.preventDefault();
                    this.setStyle('backgroundColor', '#f5f5f5');
                },
                drop: 'onDropFileUpload'
            }

        }
    ],
    buttons: [
        {
            text: 'Close',
            handler: function (btn) {
                btn.up('add-file-pop-up').close();
                const vm = btn.lookupViewModel();
                vm.set('pendingFile', null);
            }
        },
        {
            text: 'Save',
            ui: 'action',
            handler: 'onSubmitFile'
        }
    ]
});