Ext.define('ModernApp.store.DocumentsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.documents',

    requires: [
        'ModernApp.model.NodeModel'
    ],

    model: 'ModernApp.model.NodeModel',

    pageSize: 40,
    // indexarea se face automat de la 1
    currentPage: 1,

    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/api/nodes/root',
        reader: {
            type: 'json',
            rootProperty: 'content', // ce pun in grid e in interiorul campului content
            totalProperty: 'totalElements'  // numarul total de el se afla in campul totalElements
        },
        pageParam: 'page',  // numele parametrului pt numarul paginii sa fie page
        limitParam: 'size',    //trim explicit numarul parametrului
        // // ext js cand face cererea pune predefinit startParam (offset de la cxare sa porneasca)
        // // sa n am un parametru inutil in url
        startParam: undefined,
    },

    autoLoad: true
});