Ext.define('ModernApp.view.main.sidebar.SidebarController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.sidebar',

    control: {
        'button[action=navigate]' : {
            tap: 'onNavigate'
        }
    },

    onNavigate: function (button) {
        var targetRoute = button.navigateTo;
        if (targetRoute) {
            if (!targetRoute.startsWith('/')) {
                targetRoute = '/' + targetRoute;
            }
            this.redirectTo(targetRoute);
        }
    },

    onLogout: function () {
        console.log("s a delogat");
        var me = this;
        Ext.Msg.confirm(
            'Delogare',
            'Sigur vrei sa te deloghezi?',
            function (choice) {
                if (choice === 'yes') {
                    Ext.Ajax.request({
                        url: 'http://localhost:8080/auth/logout',
                        method: 'GET',

                        success: function (response) {
                           me.redirectTo('/login')
                        },
                        failure: function () {
                            console.error('Path invalid:', path);
                        }
                    });
                }
            }
        );
    }
});