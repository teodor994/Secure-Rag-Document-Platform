Ext.define('ModernApp.view.main.docarea.DocAreaView', {
    extend: 'Ext.Container',
    xtype: 'docarea',

    requires: [
        'ModernApp.view.main.docarea.HomeView',
        'ModernApp.view.main.docarea.textsearch.TextSearchView',
        'ModernApp.view.main.docarea.documents.DocumentsView'
    ],

    layout: 'card',

    items: [
        {xtype: 'home', itemId: 'home'},
        {xtype: 'search', itemId: 'search'},
        {xtype: 'documents', itemId: 'documents'}
    ]
});