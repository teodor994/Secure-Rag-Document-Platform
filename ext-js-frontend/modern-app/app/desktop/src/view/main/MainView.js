Ext.define('ModernApp.view.main.MainView', {
    extend: 'Ext.Container',
    xtype: 'mainview',

    requires: [
        'ModernApp.view.main.LoginView',
        'ModernApp.view.main.DashboardView',
        'ModernApp.view.main.MainViewModel'
    ],

    controller: 'main',
    viewModel: 'mainviewmodel',
    layout: 'card',

    items: [
        { xtype: 'login-page', itemId: 'login-page'},
        { xtype: 'dashboard', itemId: 'dashboard'},
    ]

});
