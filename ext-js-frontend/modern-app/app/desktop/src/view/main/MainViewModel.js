Ext.define('ModernApp.view.main.MainViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.mainviewmodel',
    data: {
        currentUrl: null,
        userId: null,
        currentFolderId: null,
        pendingFile: null,
        subjectType: 'BY_USER',
        userRole: null
    },

    formulas: {
        isAdmin: function(get) {
            return get('userRole') === 'ADMIN';
        }
    }
});