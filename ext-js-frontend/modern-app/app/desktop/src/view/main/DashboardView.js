Ext.define('ModernApp.view.main.DashboardView', {
    extend: 'Ext.Container',
    xtype: 'dashboard',

    requires: [
        'ModernApp.view.main.sidebar.SidebarView',
        'ModernApp.view.main.DashboardController',
        'ModernApp.view.main.docarea.DocAreaView'
    ],

    controller: 'dashboard',

    layout:{
        type: 'hbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'sidebar',
            flex: 1,
        },
        {
            xtype: 'docarea',
            flex: 4,
            reference: 'docarea'
        }
    ]
});