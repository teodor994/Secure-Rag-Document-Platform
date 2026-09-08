Ext.define("ModernApp.view.main.docarea.documents.DocumentsController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.documents',

    knownPathToId: {},
    objectUrl: null,

    requires: [
        'ModernApp.view.main.docarea.documents.AddFilePopUpView',
        'ModernApp.view.main.docarea.documents.AddFolderPopUpView'
    ],

    init: function () {
        // asculta orice schimbare de hash, inclusiv doar pe query string
        // la event de change
        // se declanseaza i=on history change
        // thos e contextul
        Ext.util.History.on('change', this.onHistoryChange, this);
    },

    destroy: function () {
        Ext.util.History.un('change', this.onHistoryChange, this);
        this.callParent();
    },

    /*
    * store -  instanta store ului care tocmai s a terminat de incarcat
    * records - array de inregistrari primitive(echivalentul content din Json)
    * succesful - true/ false (a mers req http)
    * */
    onStoreLoad: function (store, records, successful) {
        var pageLabel = this.lookupReference('pageInfoLabel');
        var prevBtn = this.lookupReference('prevBtn');
        var nextBtn = this.lookupReference('nextBtn');

        if (!successful) {
            console.log("buba eroare la on store load");
            prevBtn.setDisabled(true);
            nextBtn.setDisabled(true);
            return;
        }

        var currentPage = store.currentPage;
        var totalCount = store.getTotalCount();
        var pageSize = store.getPageSize();
        var totalPages = totalCount > 0 ? Math.ceil(totalCount / pageSize) : 1;

        pageLabel.setHtml('Page ' + currentPage + ' of ' + totalPages);
        prevBtn.setDisabled(currentPage <= 1);
        nextBtn.setDisabled(currentPage >= totalPages);
    },

    onDocumentsGridInit: function (dataview) {
        var store = dataview.getStore();
        // de cate ori store ul termina un load() se cheama fct on store load
        store.on('load', this.onStoreLoad, this);
    },

    navigateToPage: function (page) {
        var currentPath = this.parsePathFromToken(Ext.util.History.getToken() || '');
        var token = 'documents?path=' + encodeURIComponent(currentPath) + '&page=' + page;
        this.redirectTo(token);
    },

    onPrevPage: function () {
        var store = this.lookupReference('documentsGrid').getStore();
        if (store.currentPage > 1) {
            this.navigateToPage(store.currentPage - 1);
        }
    },

    onNextPage : function () {
        var store = this.lookupReference('documentsGrid').getStore();
        var totalPages = Math.ceil(store.getTotalCount() / store.getPageSize());
        if (store.currentPage < totalPages) {
            this.navigateToPage(store.currentPage + 1);
        }
    },

    onHistoryChange: function (token) {
        token = token || '';
        // reactionam doar daca suntem in sectiunea documents
        if (!/^\/?documents/.test(token)) {
            return;
        }
        var path = this.parsePathFromToken(token);
        this.lookup('currentPathBar').setHtml('<b>Path:</b> ' + this.formatPath(path));
        var page = this.parsePageFromToken(token);
        this.loadPath(path, page);
    },

    parsePageFromToken : function (token) {
        if (token.indexOf('page=') !== -1) {
            var pageStr = token.split('page=')[1].split('&')[0];
            var page = parseInt(pageStr, 10);
            return (page && page > 0) ? page : 1;
        }
        return 1;
    },

    parsePathFromToken: function (token) {
        var currentPath = '/';

        //verif daca exista path in token
        if (token.indexOf('path=') !== -1) {
            var encodedParam = token.split('path=')[1].split('&')[0];
            // avem url encoded
            currentPath = decodeURIComponent(encodedParam);
        }

        if (currentPath.endsWith('/') && currentPath.length > 1) {
            currentPath = currentPath.slice(0, -1);
        }

        return currentPath;
    },

    loadPath: function (path, page) {

        var me = this;

        if (!path || path === '/') {
            this.loadChildren(null, page);
            return;
        }

        var knownId = this.knownPathToId[path];
        if (knownId) {
            this.loadChildren(knownId, page);
            return;
        }

        Ext.Ajax.request({
            url: 'http://localhost:8080/api/nodes/resolve-path',
            method: 'GET',
            params: { path: path },
            success: function (response) {
                var chain = Ext.decode(response.responseText);
                var lastNode = chain[chain.length - 1];
                me.knownPathToId[path] = lastNode.id;
                me.loadChildren(lastNode.id, page);
            },
            failure: function () {
                console.error('Path invalid:', path);
                me.redirectTo('documents');
            }
        });
    },

    setCurrentFolderId: function (folderId) {
        var mainViewModel = this.getMainViewModel();
        if (mainViewModel) {
            mainViewModel.set('currentFolderId', folderId);
        } else {
            console.log("buba documents doc controller set curent folder id")
        }
    },

    getMainViewModel: function () {
        var mainView = Ext.ComponentQuery.query('mainview')[0];
        return mainView ? mainView.getViewModel() : null;
    },

    loadChildren: function (folderId, page) {
        this.setCurrentFolderId(folderId);

        var url = folderId
            ? 'http://localhost:8080/api/nodes/' + folderId + '/children'
            : 'http://localhost:8080/api/nodes/root';

        var store = this.lookupReference('documentsGrid').getStore();
        store.getProxy().setUrl(url);
        // pe nou folder mereu incep pe pagina 1
        // altfel raman la currentpage
        store.currentPage = page || 1;
        store.load();
    },

    onNodeTap: function (dataview, index, target, record) {
        var type = record.get('type');
        var id = record.get('id');
        var name = record.get('name');

        if (type === 'folder') {
            var currentPath = this.parsePathFromToken(Ext.util.History.getToken() || '');
            var newPath = (currentPath === '/' ? '' : currentPath) + '/' + name;
            this.knownPathToId[newPath] = id;
            // encodez url sa fie de forma %2F
            this.redirectTo('documents?path=' + encodeURIComponent(newPath));
        }
    },

    onAddFolderPress: function () {
        const dialog = Ext.create('ModernApp.view.main.docarea.documents.AddFolderPopUpView')
        this.getView().add(dialog);
        dialog.show();
    },

    onAddFilePress: function () {
        const dialog = Ext.create('ModernApp.view.main.docarea.documents.AddFilePopUpView')
        this.getView().add(dialog);
        dialog.show();
    },

    onSubmitFolder: async function(btn) {
        let vm = this.getViewModel();

        const dialog = btn.up('add-folder-pop-up');
        const textField = dialog.down('textfield');

        // If folder name contains spaces
        if (!textField.isValid()) {
            Ext.Msg.alert('Error', 'Invalid name!');
            return;
        }

        const folderName = textField.getValue().trim();
        let currentPath = '/';
        const hash = window.location.hash;

        if (hash.includes('path=')) {
            const encodedParam = hash.split('path=')[1].split('&')[0];
            currentPath = decodeURIComponent(encodedParam);
        }

        const newFolderPath = (currentPath === '/' ? '' : currentPath) + '/' + folderName;
        const parentId = vm.get('currentFolderId');
        const jsonData = {
            parentId: parentId,
            type: 'folder',
            name: folderName,
            createdBy: vm.get('userId'),
            path: newFolderPath
        };

        console.log(jsonData);

        try {
            let response = await fetch("http://localhost:8080/api/nodes",
                {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(jsonData)
                });
            const result = await response.json();
            if (!response.ok || !result.success) {
                throw new Error("Error creating new folder!");
            }
            var store = this.lookupReference('documentsGrid').getStore();
            this.loadChildren(parentId, store.currentPage);
            dialog.close();
        } catch (e) {
            Ext.Msg.alert('Error!', 'Could not create new folder');
        }
    },

    // When file gets dropped, it gets into the VM
    onDropFileUpload: async function (e) {
        e.preventDefault();
        const file = e.browserEvent.dataTransfer.files[0];
        this.getViewModel().set('pendingFile', file);
    },

    onSubmitFile: async function (btn) {
        const dialog = btn.up('add-file-pop-up');
        const file = this.getViewModel().get('pendingFile');
        if (file === null) {
            Ext.Msg.alert("Error!", "No file uploaded!");
            return;
        }

        const parentId = this.getViewModel().get('currentFolderId');
        let currentPath = '/';
        const hash = window.location.hash;
        if (hash.includes('path=')) {
            const encodedParam = hash.split('path=')[1];
            currentPath = decodeURIComponent(encodedParam);
        }
        const newFolderPath = (currentPath === '/' ? '' : currentPath) + '/' + file.name;

        let form = new FormData();
        form.append('parentId', parentId);
        form.append('path', newFolderPath)
        form.append('file', file);
        form.append('createdBy', this.getViewModel().get('userId'))

        // multipart/form-data
        const response = await fetch('http://localhost:8080/api/nodes/upload', {
            method: 'POST',
            body: form,
            credentials: 'include'
        });
        const data = await response.json();
        if (!response.ok) {
            Ext.Msg.alert('Error', 'Error uploading file');
        } else {
            Ext.Msg.alert('Succes', 'File uploaded');
            this.getViewModel().set('pendingFile', null);
            dialog.close();
            var store = this.lookupReference('documentsGrid').getStore();
            this.loadChildren(parentId, store.currentPage);
        }
    },

    loadDocument: function(documentId, targetDialog) {
        targetDialog.setMasked({ xtype: 'loadmask', message: 'Loading document...' });
        Ext.Ajax.request({
            url: 'http://localhost:8080/api/nodes/' + documentId + '/content',
            method: 'GET',
            success: function(response) {
                var data = Ext.decode(response.responseText);
                var mimeType = data.mimeType || 'application/octet-stream';

                switch (mimeType) {
                    case 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': // .xlsx
                        break;
                    case 'application/vnd.ms-excel': // .xls
                        break;

                    case 'text/markdown':
                    case 'text/plain':
                        targetDialog.down('#docContent').setHtml(
                            '<pre style="white-space:pre-wrap;word-wrap:break-word;padding:10px;">'
                            + Ext.String.htmlEncode(data.textContent) + '</pre>'
                        );
                        break;

                    default:
                        Ext.Msg.alert('Error', 'Unknown type of document: ' + mimeType);
                        break;
                }
                targetDialog.setMasked(false);
            },
            failure: function() {
                targetDialog.setMasked(false);
                Ext.Msg.alert('Error', 'An error occured, please try again.');
            },
            scope: this
        });
    },

    onCloseTap: function() {
        this.getView().destroy();
    },

    onDialogDestroy: function() {
        if (this.objectUrl) {
            URL.revokeObjectURL(this.objectUrl);
            this.objectUrl = null;
        }
    },

    onNodeDoubleTap: function (dataview, index, target, record) {
        if (record.get('type') === 'document') {
            var dialog = Ext.create('ModernApp.view.main.docarea.documents.DocumentPreview');
            dialog.show();
            this.loadDocument(record.get('id'), dialog);
        }
    },

    onNodeRightClick: function(grid, index, target, record, e) {
        e.preventDefault();
        const dialog = Ext.create('ModernApp.view.main.docarea.documents.RightClickPopUpView');
        this.getView().add(dialog);
        dialog.show();
        dialog.clickedFileId = record.get('id');
    },

    onDeleteFileClick: function(btn) {
        const dialog = btn.up('right-click-pop-up');
        const fileId = dialog.clickedFileId;
        const view = dialog.up();

        dialog.close();

        const deleteDialog = Ext.create('ModernApp.view.main.docarea.documents.DeletePopUpView', {
            clickedFileId: fileId
        });

        if (view) {
            view.add(deleteDialog);
        }
        deleteDialog.show();
    },

    submitDelete: async function (btn) {
        const dialog = btn.up('delete-pop-up');
        const store = this.lookupReference('documentsGrid').getStore();
        const deletedId = dialog.clickedFileId;
        const parentId = this.getViewModel().get('currentFolderId');
        const response = await fetch(`http://localhost:8080/api/nodes/${deletedId}`, {
            method: 'DELETE',
        });
        const data = await response.json();
        if (!response.ok) {
            Ext.Msg.alert('Error', 'Error deleting file');
        } else {
            Ext.Msg.alert('Succes', 'File Deleted');
            dialog.close();
            this.loadChildren(parentId, store.currentPage);
        }
    },

    onAddPermissionPress: function(btn) {
        const dialog = btn.up('right-click-pop-up');
        const fileId = dialog.clickedFileId;
        const view = dialog.up();
        const addPermDialog = Ext.create('ModernApp.view.main.docarea.documents.AddPermissionPopUpView',{
            clickedFileId: fileId
        });
        this.getView().add(addPermDialog);

        const gridStore = this.lookupReference('documentsGrid').getStore();

        const fileSelect = addPermDialog.lookup('fileSelect');
        if (fileSelect) {
            fileSelect.setStore(gridStore);
        }
        addPermDialog.show()
        dialog.close();
    },

    onRemovePermissionPress: function () {
        const dialog = Ext.create('ModernApp.view.main.docarea.documents.RemovePermissionPopUpView');
        this.getView().add(dialog);
        dialog.show()
    },

    onAddPermissionSave: async function (btn) {
        const dialog = btn.up('add-permission-pop-up');
        const subjectType = dialog.lookup('subjectTypeSelect').getValue();
        const grantedRole = dialog.lookup('grantedRoleSelect').getSelection() ?
                            dialog.lookup('grantedRoleSelect').getSelection().get('id') : null;


        const inherit = dialog.lookup('inheritSelect') ?  dialog.lookup('inheritSelect').getValue() : null;
        // const fileSelect = dialog.lookup('fileSelect').getSelection()
        const targetFile =  dialog.clickedFileId;

        let userId = null;
        let roleId = null;

        if (subjectType === 'BY_USER' &&  dialog.lookup('userSelect').getSelection()) {
            userId = dialog.lookup('userSelect').getSelection().get('id');
        } else if (subjectType === 'BY_ROLE' &&  dialog.lookup('roleSelect').getSelection()) {
            roleId = dialog.lookup('roleSelect').getSelection().get('id');
        }

        const jsonData = {
            'nodeId': targetFile,
            'userId': userId,
            'roleId': roleId,
            'grantedRoleId': grantedRole,
            'inherit': inherit,
            'createdBy': this.getViewModel().get('userId')
        };

        const response = await fetch('http://localhost:8080/api/permissions', {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(jsonData)
        });

        if (!response.ok) {
            Ext.Msg.alert('Error', 'Error adding permission');
        } else {
            Ext.Msg.alert('Succes', 'Permission Added');
            dialog.close();
        }
    },

    onRemovePermissionSave: async function (btn) {
        const dialog = btn.up('remove-permission-pop-up');
        const permissionSelect = dialog.lookup('permissionSelect').getSelection();
        const removedPermissionId = permissionSelect ? permissionSelect.get('id') : null;

        const response = await fetch(`http://localhost:8080/api/permissions/${removedPermissionId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            Ext.Msg.alert('Success', 'Permission Removed!');
            dialog.close();
        } else {
            Ext.Msg.alert('Error', 'Error removing permission..');
        }
    },

    formatPath: function (path) {
        var segments = path.split('/').filter(
            function (s) {
                return s.length > 0;
            });

        if (segments.length === 0) {
            return '/';
        }

        // num de segmente pe care le arat
        var maxSegments = 4;
        var sep = ' <span class="path-sep"> > </span> ';

        if (segments.length <= maxSegments) {
            return segments.join(sep);
        }

        var first = segments[0];
        var lastOnes = segments.slice(segments.length - (maxSegments - 1));

        return first + sep + '<span class="path-sep">...</span>' + sep + lastOnes.join(sep);
    },
});