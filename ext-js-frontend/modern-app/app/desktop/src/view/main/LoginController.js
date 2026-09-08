Ext.define('ModernApp.view.main.LoginController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.login',

    control: {
        'button[action=login]': {
            tap: 'onLoginTap'
        }
    },

    onLoginTap: function () {
        // La oidc modificare url catre 'http://localhost:8080/login/ouath2/authorization/{ssoId}'
        window.location.href = 'http://localhost:8080/login';
    }
});