Ext.define("ModernApp.model.NodeModel", {
    extend: 'Ext.data.Model',

    fields: [
        'createdAt',
        'createdBy',
        'id',
        'name',
        'parentId',
        'path',
        'type',
    ]
});