Ext.define('ModernApp.view.main.docarea.documents.AddFolderPopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'add-folder-pop-up',

    title: 'Add new folder',
    modal: true,
    centered: true,
    width: 400,
    closeAction: 'destroy',

    items: [
        {
            xtype: 'textfield',
            name: 'folderName',
            label: 'Nume Folder',
            placeholder: 'Ex: Document',
            required: true,
            clearable: true,
            allowBlank: false,
            // no space allowed
            validators: {
                type: 'format',
                matcher: /^\S+$/,
                message: 'No space allowed'
            },
        }
    ],
    buttons: [
        {
            text: 'Close',
            handler: function (btn) {
                btn.up('add-folder-pop-up').close();
            }
        },
        {
            text: 'Save',
            ui: 'action',
            handler: 'onSubmitFolder'
        }
    ]
});