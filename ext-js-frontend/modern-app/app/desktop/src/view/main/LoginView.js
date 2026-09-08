Ext.define('ModernApp.view.main.LoginView', {
    extend: 'Ext.Container',
    xtype: 'login-page',

    requires: [
        'ModernApp.view.main.LoginController'
    ],

    controller: 'login',

    layout:{
        type: 'vbox',
        pack: 'center',
        align: 'center'
    },

    items: [
        {
            xtype: 'button',
            text: 'Login',
            action: 'login'
        }
    ]
});