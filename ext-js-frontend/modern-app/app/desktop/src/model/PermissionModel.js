Ext.define("ModernApp.model.PermissionModel", {
    extend: 'Ext.data.Model',

    fields: [
        'id',
        'nodeId',
        'nodePath',
        {name: 'targetUserName', defaultValue: null},
        {name: 'targetRoleName', defaultValue: null},
        'grantedRole',
        'inherit',
        'createdBy',
        'createdAt',
        {
            name: 'displayLabel',
            convert: function (value, record) {
                const userName = record.get('targetUserName');
                const roleName = record.get('targetRoleName');
                const grantedRole = record.get('grantedRole');
                const inherit = record.get('inherit');

                const target = userName ? 'User: ' + userName : (roleName ? 'Role: ' + roleName : 'N/A');

                return target + ' - Granted: ' + grantedRole + ' - Inherit: ' + inherit + ' - On: ' + record.get('nodePath');
            }
        }
    ]
});
