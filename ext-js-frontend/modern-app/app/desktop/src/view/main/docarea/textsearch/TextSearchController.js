Ext.define('ModernApp.view.main.docarea.textsearch.TextSearchController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.textsearch',

    onSearchTap: async function () {
        var queryField = this.lookupReference('searchInput');
        var statusCmp = this.lookupReference('searchStatus');
        var store = this.lookupReference('searchResultsGrid').getStore();

        var query = queryField.getValue() ? queryField.getValue().trim() : '';
        if (!query) {
            statusCmp.setHtml('Type a text to search.');
            store.removeAll();
            return;
        }

        statusCmp.setHtml('Marinating...');

        try {
            var response = await fetch('http://localhost:8080/api/nodes/search/files', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(query) // backend waits for json
            });

            if (!response.ok) {
                throw new Error('Search failed');
            }

            var results = await response.json();
            store.setData(results);

            statusCmp.setHtml(results.length
                ? results.length + ' results found.'
                : 'No result found.');
        } catch (e) {
            console.error(e);
            statusCmp.setHtml('Error searching results.');
            store.removeAll();
        }
    },

    onResultTap: function (dataview, index, target, record) {
        var me = this;
        var path = record.get('path');
        if (!path) {
            return;
        }
        // Ext.util.History.add('documents?path=' + encodeURIComponent(this.parentFolderPath(path)));
        this.redirectTo('documents?path=' + encodeURIComponent(this.parentFolderPath(path)));
        me.restoreCurrentPath();
    },

    parentFolderPath: function (path) {
        var segments = path.split('/').filter(function (s) { return s.length > 0; });
        segments.pop(); // scoate numele fisierului, ramane doar folderul
        return segments.length ? '/' + segments.join('/') : '/';
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