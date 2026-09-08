Ext.define("ModernApp.model.UserModel", {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'email',
        'password_hash',
        'created_at'
    ]
});