Ext.define('ModernApp.store.UsersStore', {
    extend: 'Ext.data.Store',
    alias: 'store.users',

    requires: [
        'ModernApp.model.UserModel'
    ],

    model: 'ModernApp.model.UserModel',
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/api/users',
        reader: {
            type: 'json',
        },
    },
    autoLoad: true
});