var userid;
var password;
var role;
$(function () {
    $("#login-placeholder").load("../pages/Login.html", function () { //load html page in specific div
        $("#idBtnLogin").click(function () {
            userid = $('#userid').val();
            password = $('#pwd').val();
            $(".loading").show();
            doLogin();
        });
        $("#idBtnLogout").click(function () {
            doLogout();
            window.location.replace("/"); //redirect to home page
        });
    });
    $("#nav-placeholder").load("../pages/nav.html", function () {
        if (typeof $.cookie('userid') !== 'undefined' && $.cookie('userid') != 'null') { //check if user already logged in
            userid = $.cookie('userid');
            password = $.cookie('password');
            role = $.cookie('role');
            reLogin();
        }
    });
    $("#loading-placeholder").load("../pages/loading.html", function () {});
    $("#confirmUpdate-placeholder").load("../pages/confirmUpdate.html", function () {});
    $("#confirmDelete-placeholder").load("../pages/confirmDelete.html", function () {});
});

function doLogin() {
    document.getElementById('idResult').innerHTML = "it will take times as first login, please wait";
    $.post(serverAddress + "login", {
        username: userid
        , password: password
    }, function (res) { // check user role to determine which function user can use
        switch (res) {
        case "Administrator":
            $.cookie('role', 'Administrator');
            $('.admin').css('display', 'block');
            $('.dcc').css('display', 'block');
            break;
        case "A01. CCB Admin":
            $.cookie('role', 'A01. CCB Admin');
            $('.dcc').css('display', 'block');
            break;
        default:
        }
    }).done(function (res) {
        document.getElementById('idSpanLoginResult').innerHTML = "welcome " + res;
        document.getElementById('idResult').innerHTML = "welcome " + res;
        $('#idPuserName').text(userid);
        $('#idPuserName1').text(userid);
        $(".login").hide();
        $("#idBtnLogout").show();
        $('#idDivLogin').hide();
        $.cookie('userid', userid);
        $.cookie('password', password);
        window.location.reload();
    }).fail(function () {
        document.getElementById('idSpanLoginResult').innerHTML = "incorrect user or password";
        document.getElementById('idResult').innerHTML = "incorrect user or password";
    }).always(function () {
        $("#idSpinner").hide();
        $('#idLoader').delay(1000).fadeOut('fast')
    });
}

function reLogin() { // for the user who already logged in
    $(".login").hide();
    $("#idBtnLogout").show();
    $('#idDivLogin').hide();
    switch (role) {
    case "Administrator":
        $('.admin').css('display', 'block');
        $('.dcc').css('display', 'block');
        break;
    case "A01. CCB Admin":
        $('.dcc').css('display', 'block');
        break;
    default:
    }
    $('#idPuserName').text(userid);
    $('#idPuserName1').text(userid);
}

function doLogout() {
    $('#idDivLogin').hide();
    $.cookie("userid", null);
    $.cookie("password", null);
    $.cookie("role", null);
    window.location.reload();
}