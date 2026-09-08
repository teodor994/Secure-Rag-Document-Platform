Ext.define('ModernApp.store.PermissionsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.permissions',

    requires: [
        'ModernApp.model.PermissionModel'
    ],

    model: 'ModernApp.model.PermissionModel',
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/api/permissions',
        reader: {
            type: 'json',
        },
    },
    autoLoad: true
});