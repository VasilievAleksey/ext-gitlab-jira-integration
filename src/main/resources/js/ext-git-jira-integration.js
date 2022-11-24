AJS.toInit(function () {
    AJS.log("Your message here.");

    var url = AJS.contextPath() + "/rest/extjiragit/1.0/";
    AJS.log(url);

    // wait for the DOM (i.e., document "skeleton") to load. This likely isn't necessary for the current case,
    // but may be helpful for AJAX that provides secondary content.
    AJS.$.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) { // when the configuration is returned...
            // ...populate the form.
            $("#name").val(config.name);
            $("#time").val(config.time);

            AJS.$("#admin").submit(function(e) {
                e.preventDefault();
                updateConfig();
        });
    });

    function updateConfig() {
        AJS.$.ajax({
            url: url,
            type: "PUT",
            contentType: "application/json",
            data: '{ "name": "' + AJS.$("#name").attr("value") + '", "time": ' +  AJS.$("#time").attr("value") + ' }',
            processData: false
        });
    }
});