var userid;
var password;
$(document).ready(function () {
    $("#login-placeholder").load("../pages/Login", function () {
        initialize();
        $("#idBtnLogin").click(function () {
            userid = $('#userid').val();
            password = $('#pwd').val();
            //            alert(userid + ' ' + password);
            $(".loading").show();
            doLogin();
        });
        $("#idBtnLogout").click(function () {
            doLogout();
            initialize();
            setTimeout(location.reload.bind(location), 0);
        });
        if (typeof $.cookie('userid') !== 'undefined') {
            console.log($.cookie('userid') + ' ' + $.cookie('password'));
            console.log($.cookie('userid') != 'undefined');
            console.log($.cookie('password') != 'undefined');
            userid = $.cookie('userid');
            password = $.cookie('password');
            $(".loading").show();
            doLogin();
        }
    });
    //    $("#report-placeholder").load("Report");
});

function doLogin() {
    document.getElementById('idResult').innerHTML = "it will take times as first login, please wait";
    //    var blnUserExist = false;
    $.post(serverAddress + "login", {
        username: userid
        , password: password
    }, function (res) {}).done(function (res) {
        document.getElementById('idSpanLoginResult').innerHTML = "welcome " + res;
        document.getElementById('idResult').innerHTML = "welcome " + res;
        document.getElementById('idPuserName').innerHTML = userid;
        document.getElementById('idPuserName1').innerHTML = userid;
        $(".login").hide();
        $("#idBtnLogout").show();
        $('#idDivLogin').hide();
        $.cookie('userid', userid);
        $.cookie('password', password);
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

function doLogout() {
    $('#idDivLogin').hide();
    $.cookie("userid", null);
    $.cookie("password", null);
}