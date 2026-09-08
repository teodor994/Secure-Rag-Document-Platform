Ext.define('ModernApp.view.main.docarea.documents.DeletePopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'delete-pop-up',

    title: 'Do you want to delete this file?',
    modal: true,
    centered: true,
    width: 400,
    closeAction: 'destroy',
    buttons: [
        {
            text: 'Yes',
            handler: 'submitDelete'
        },
        {
            text: 'No',
            ui: 'action',
            handler: function (btn) {
                btn.up('delete-pop-up').close();
            }
        }
    ]
});
