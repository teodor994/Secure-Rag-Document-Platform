Ext.define('ModernApp.view.main.sidebar.SidebarView', {
    extend: 'Ext.Panel',
    xtype: 'sidebar',

    requires: [
        'ModernApp.view.main.sidebar.SidebarController'
    ],

    controller: 'sidebar',

    title: 'Menu',

    cls: 'sidebar',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'button',
            text: 'Home',
            action: 'navigate',
            navigateTo: 'home'
        },
        {
            xtype: 'button',
            text: 'Documents',
            action: 'navigate',
            navigateTo: 'documents'
        },
        {
            xtype: 'button',
            text: 'Search',
            action: 'navigate',
            navigateTo: 'search'
        },
        {
            xtype: 'toolbar',
            docked: 'bottom',
            layout: {
                type: 'hbox',
                pack: 'center'
            },

            items: [
                {
                    xtype: 'button',
                    text: 'logout',
                    reference: 'logoutBtn',
                    handler: 'onLogout'
                }
            ]

        },
    ]

});