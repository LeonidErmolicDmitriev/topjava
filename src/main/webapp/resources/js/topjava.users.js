const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function (){
        $.get(ctx.ajaxUrl, function (data) {
            updateTableByData(data);
        });
    },
    enableUser: function (checkbox){
        let id = checkbox.closest('tr').attr("id");
        let enabled = checkbox.is(':checked');
        $.ajax({
            type: "POST",
            url: userAjaxUrl + id,
            data: "enabled=" + enabled
        }).done(function () {
            checkbox.closest('tr').attr("data-user-enabled", enabled);
        });
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});