Ext.define('ModernApp.view.main.docarea.documents.RightClickPopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'right-click-pop-up',

    title: 'Select which action to do:',
    modal: true,
    centered: true,
    width: 400,
    closeAction: 'destroy',
    buttons: [
        {
            text: 'Add Permission',
            handler: 'onAddPermissionPress'
        },
        {
            text: 'Delete File',
            handler: 'onDeleteFileClick'
        },
        {
            text: 'Close',
            handler: function (btn) {
                btn.up('right-click-pop-up').close();
            }
        }
    ]
});
