Ext.define('ModernApp.Application', {
	extend: 'Ext.app.Application',
	name: 'ModernApp',
	requires: ['ModernApp.*'],

	routes: {
		'/login': 'onLoginRoute',
		'/home' : 'onHomeRoute',
		'/documents' : 'onDocumentsRoute',
		'/search' : 'onSearchRoute',
	},

	removeSplash: function () {
		Ext.getBody().removeCls('launching')
		var elem = document.getElementById("splash")
		elem.parentNode.removeChild(elem)
	},

	launch: function () {
		this.removeSplash()
		var whichView = 'mainview'
		Ext.Viewport.add([{xtype: whichView}])
	},

	getMainView: function () {
		return Ext.ComponentQuery.query('mainview')[0];
	},

	onLoginRoute: function () {
		var mainView = this.getMainView();

		if (mainView) {
			mainView.getController().showSection('login-page');
		}
	},


	navigateDashboard: function (key) {
		var mainView = this.getMainView();
		if (!mainView) return;

		mainView.getController().showSection('dashboard');

		var dashboardView = mainView.down('#dashboard');
		dashboardView.getController().showSection(key);
	},

	onHomeRoute: function () {
		this.navigateDashboard('home');
	},

	onDocumentsRoute: function () {
		this.navigateDashboard('documents');
	},

	onSearchRoute: function () {
		this.navigateDashboard('search');
	},

	onAppUpdate: function () {
		Ext.Msg.confirm('Application Update', 'This application has an update, reload?',
			function (choice) {
				if (choice === 'yes') {
					window.location.reload();
				}
			}
		);
	}
});
