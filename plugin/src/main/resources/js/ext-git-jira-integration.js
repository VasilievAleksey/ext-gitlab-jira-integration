AJS.toInit(function () {

    const basUrl = AJS.contextPath() + "/rest/gitplugin/1.0/";

    AJS.$("#add-repo").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#add-repo-dialog").show();
    });

    AJS.$("#add-repo-submit-button").click(function (e) {
        e.preventDefault();
        AJS.$.ajax({
            url: basUrl + "repository",
            type: 'post',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                    url: AJS.$('#repo-url').val(),
                    username: AJS.$('#username').val(),
                    password: AJS.$('#password').val(),
                    accessToken: AJS.$('#accessToken').val()
                }
            ),
            dataType: "text",
            success: function () {
                AJS.dialog2("#add-repo-dialog").hide();
            }
        })
    });
});