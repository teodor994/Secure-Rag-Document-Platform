Ext.define('ModernApp.view.main.docarea.documents.RemovePermissionPopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'remove-permission-pop-up',

    title: 'Remove a permission',
    modal: true,
    centered: true,
    referenceHolder: true,
    width: 400,
    closeAction: 'destroy',

    items: [
        {
            xtype: 'selectfield',
            label: 'Select Target Permission to Delete',
            reference: 'permissionSelect',
            store: { type: 'permissions' },
            displayField: 'displayLabel',
        }
    ],
    buttons: [
        {
            text: 'Close',
            handler: function (btn) {
                btn.up('remove-permission-pop-up').close();
            }
        },
        {
            text: 'Save',
            ui: 'action',
            handler: 'onRemovePermissionSave'
        }
    ]
});