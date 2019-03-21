var userid;
var password;
var role;
$(function () {
    $("#login-placeholder").load("../pages/Login.html", function () {
        //        initialize();
        $("#idBtnLogin").click(function () {
            userid = $('#userid').val();
            password = $('#pwd').val();
            //            alert(userid + ' ' + password);
            $(".loading").show();
            doLogin();
        });
        $("#idBtnLogout").click(function () {
            doLogout();
            //            initialize();
            //            setTimeout(location.reload.bind(location), 2000);
            //            setTimeout(function () {
            window.location.replace("/");
            //            }, 5000);
        });
    });
    $("#nav-placeholder").load("../pages/nav.html", function () {
        if (typeof $.cookie('userid') !== 'undefined' && $.cookie('userid') != 'null') {
            //            console.log($.cookie('userid') + ' ' + $.cookie('password'));
            //            console.log(typeof $.cookie('userid'));
            userid = $.cookie('userid');
            password = $.cookie('password');
            role = $.cookie('role');
            //            $(".loading").show();
            reLogin();
        }
    });
    $("#loading-placeholder").load("../pages/loading.html", function () {});
    $("#confirm-placeholder").load("../pages/confirm.html", function () {});
    //    $("#report-placeholder").load("Report");
});

function doLogin() {
    document.getElementById('idResult').innerHTML = "it will take times as first login, please wait";
    //    var blnUserExist = false;
    $.post(serverAddress + "login", {
        username: userid
        , password: password
    }, function (res) {
        //        console.log(res);
        switch (res) {
        case "Administrator":
            //            console.log("Admin");
            $.cookie('role', 'Administrator');
            $('.admin').css('display', 'block');
            $('.dcc').css('display', 'block');
            break;
        case "A01. CCB Admin":
            //            console.log("dcc");
            $.cookie('role', 'A01. CCB Admin');
            $('.dcc').css('display', 'block');
            break;
        default:
            //            console.log("default");
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
        //        alert("finished");
        $("#idSpinner").hide();
        $('#idLoader').delay(1000).fadeOut('fast')
    });
    //    if (blnUserExist == false) {
    //        document.getElementById('idSpanLoginResult').innerHTML = "incorrect username or password";
    //    }
}

function reLogin() {
    $(".login").hide();
    $("#idBtnLogout").show();
    $('#idDivLogin').hide();
    switch (role) {
    case "Administrator":
        $('.admin').css('display', 'block');
        $('.dcc').css('display', 'block');
        //        console.log('admin');
        break;
    case "A01. CCB Admin":
        //        console.log('dcc');
        $('.dcc').css('display', 'block');
        break;
    default:
    }
    $('#idPuserName').text(userid);
    $('#idPuserName1').text(userid);
    //    document.getElementById('idPuserName').innerHTML = userid;
    //    document.getElementById('idPuserName1').innerHTML = userid;
}

function doLogout() {
    $('#idDivLogin').hide();
    $.cookie("userid", null);
    $.cookie("password", null);
    $.cookie("role", null);
    window.location.reload();
}