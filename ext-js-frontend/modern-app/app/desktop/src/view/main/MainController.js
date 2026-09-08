Ext.define('ModernApp.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',

    init: function () {
        this.checkAuth();
    },

    checkAuth: function () {
        var me = this;
        let vm = this.getViewModel();

        Ext.Ajax.request({
            url: 'http://localhost:8080/auth/me',
            method: 'GET',
            withCredentials: true,

            success: function (response) {
                var data = {};

                try {
                    data = Ext.JSON.decode(response.responseText);
                } catch (e) {
                    console.warn('Raspuns invalid de la /auth/me:', e);
                }

                console.log('Auth response:', data);

                if (data.authenticated === true) {
                    vm.set('userId', data.userId);
                    vm.set('userRole', data.role);
                    console.log('user role', vm.get('userRole'));
                    me.showSection('dashboard');
                    //to do
                    // SA MI SE INTOARCA P EURL CURR si sa se intoarca pe folderul de documente
                    me.restoreCurrentPath();
                } else {
                    me.showSection('login-page');
                }
            },

            failure: function (response) {
                console.warn('Cerere esuata catre /auth/me:', response.status, response.statusText);
                me.showSection('login-page');
            }
        });
    },

    showSection: function (key) {
        var target = this.getView().down('#' + key);

        if (target) {
            this.getView().setActiveItem(target);
        } else {
            console.warn('Nu exista card cu itemId:', key);
        }
    },

    restoreCurrentPath: function () {
        var token = Ext.util.History.getToken() || '';
        var app = Ext.getApplication();

        if (/^\/?documents/.test(token)) {
            app.navigateDashboard('documents');
            var documentsView = Ext.ComponentQuery.query('documents')[0];
            var controller = documentsView && documentsView.getController();
            if (controller) {
                controller.onHistoryChange(token);
            } else {
                console.log("buba n am gasit controller la documentsview")
            }
        } else if (/^\/?search/.test(token)) {
            app.navigateDashboard('search')
        } else {
            app.navigateDashboard('home')
        }
    }
});