AJS.toInit(function () {
    new AJS.RestfulTable({
        el: jQuery("#project-config-versions-table"),
        autoFocus: true,
        resources: {
            all: AJS.contextPath() + "/rest/gitplugin/1.0/repository/all",
            self: AJS.contextPath() + "/rest/gitplugin/1.0/repository"
        },
        deleteConfirmationCallback: function(model) {
            AJS.$("#restful-table-model")[0].innerHTML = "<b>URL:</b> " + model.url + " <b>status:</b> " + model.status + " <b>description:</b> " + model.description;
            AJS.dialog2("#delete-confirmation-dialog").show();
            return new Promise(function(resolve, reject) {
                AJS.$("#dialog-submit-button").on('click', function (e) {
                    resolve();
                    e.preventDefault();
                    AJS.dialog2("#delete-confirmation-dialog").hide();
                });
                AJS.$(".aui-dialog2-header-close, #warning-dialog-cancel").on('click', function (e) {
                    reject();
                    e.preventDefault();
                    AJS.dialog2("#delete-confirmation-dialog").hide();
                });
            });
        },
        columns: [
            {
                id: "name",
                header: "Repository name",
                allowEdit: false
            },
            {
                id: "url",
                header: "Repository URL"
            },
            {
                id: "accessToken",
                header: "Access token"
            },
            {
                id: "description",
                header: "Description",
                allowEdit: false
            },
            {
                id: "status",
                header: "Status",
                allowEdit: false,
                readView: AJS.RestfulTable.CustomReadView.extend({
                    render: function (self) {
                        return '<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-success">' + self.value + '</span>';

                    }
                })
            }
        ]
    });

    var restfulTableEvents = ["ROW_ADDED", "ROW_REMOVED", "EDIT_ROW", "REORDER_SUCCESS", "SERVER_ERROR"];
    restfulTableEvents.forEach(function(eventName) {
        jQuery(document).on(AJS.RestfulTable.Events[eventName], function() {
            console && console.log("RestfulTable event", eventName, "- callback arguments: ", arguments);
            AJS.flag({
                body: "<strong>" + eventName + "</strong> fired on RestfulTable. (Check devtools for more info).",
                close: "auto"
            });
        });
    });
});