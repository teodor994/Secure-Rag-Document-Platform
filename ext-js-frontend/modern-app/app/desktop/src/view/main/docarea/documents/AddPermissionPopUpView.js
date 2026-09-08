Ext.define('ModernApp.view.main.docarea.documents.AddPermissionPopUpView', {
    extend: 'Ext.Dialog',
    xtype: 'add-permission-pop-up',
    requires: [
        'ModernApp.store.UsersStore'
    ],

    title: 'Add a new permission',
    referenceHolder: true,
    modal: true,
    centered: true,
    width: 400,
    closeAction: 'destroy',
    viewModel: 'mainviewmodel',

    items: [
        {
            xtype: 'selectfield',
            reference: 'subjectTypeSelect',
            label: 'Subject Type',
            bind: '{subjectType}',
            options: [
                { text: 'By User', value: 'BY_USER' },
                { text: 'By Role', value: 'BY_ROLE' }
            ]
        },
        {
            xtype: 'container',
            bind: {
                hidden: '{subjectType !== "BY_USER"}'
            },
            items: [
                {
                    xtype: 'combobox',
                    store: { type: 'users' },
                    reference: 'userSelect',
                    displayField: 'email',
                    label: 'Select User',
                    placeholder: 'Type to search user...'
                }
            ]
        },
        {
            xtype: 'container',
            bind: {
                hidden: '{subjectType !== "BY_ROLE"}'
            },
            items: [
                {
                    xtype: 'selectfield',
                    label: 'Select Role',
                    reference: 'roleSelect',
                    store: {type : 'roles'},
                    displayField: 'code'
                }
            ]
        },
        {
            xtype: 'selectfield',
            label: 'Granted Access',
            reference: 'grantedRoleSelect',
            value: 'VIEWER',
            store: {type: 'roles'},
            displayField: 'code'
        },
        {
            xtype: 'selectfield',
            reference: 'inheritSelect',
            label: 'Inherit',
            options: [
                { text: 'True', value: 'true' },
                { text: 'False', value: 'false' }
            ]
        },
    ],
    buttons: [
        {
            text: 'Close',
            handler: function (btn) {
                btn.up('add-permission-pop-up').close();
            }
        },
        {
            text: 'Save',
            ui: 'action',
            handler: 'onAddPermissionSave'
        }
    ]
});