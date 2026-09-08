Ext.define('ModernApp.store.RolesStore', {
    extend: 'Ext.data.Store',
    alias: 'store.roles',

    requires: [
        'ModernApp.model.RoleModel'
    ],

    model: 'ModernApp.model.RoleModel',
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/api/roles',
        reader: {
            type: 'json',
        },
    },
    autoLoad: true
});