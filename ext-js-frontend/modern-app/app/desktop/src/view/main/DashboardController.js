Ext.define('ModernApp.view.main.DashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.dashboard',

    showSection: function (key) {
        var docarea = this.lookupReference('docarea');
        //caut copilul cu itemId egal cu key
        var target = docarea.down('#' + key);
        if (target) {
            docarea.setActiveItem(target);
        } else {
            console.warn('Nu exista card cu itemId:', key);
        }
    }
});