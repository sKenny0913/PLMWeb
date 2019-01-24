//var serverAddress = 'http://localhost:8080/PLMWeb/';
var serverAddress = 'http://plmweb.tech-man.com.cn:8080/PLMWeb/';
//var serverAddress = 'http://192.168.99.100:8080/PLMWeb/'; //docker IP
var oListValue = null;
//----------------------main function---------------------
$(function () {
    initialize();
    $("#nav-placeholder").load("../pages/nav.html", function () {});
    $("#loading-placeholder").load("../pages/loading.html", function () {});
    $("#confirm-placeholder").load("../pages/confirm.html", function () {});
    $("#btnCompare").click(function () {
        if ($('#idForm').valid()) {
            Compare();
        }
    });
    $("#btnInactivate").click(function () {
        if ($('#idForm').valid()) {
            if (textIsValid()) {
                alert('please do not input "\\" or "/"')
            }
            else {
                Inactivate();
            }
        }
    });
    $("#btnListQuery").click(function () {
        getListValue();
    });
    $("#btnListAdd").click(function () {
        if ($('#idForm').valid()) {
            addListValue();
        }
    });
    $("#btnListUpdate").click(function () {
        if ($('#idForm').valid()) {
            if (oListValue != null) {
                updateListValue();
            }
            else {
                alert("pls select a row")
            }
        }
    });
    $("#btnListDelete").click(function () {
        if ($('#idForm').valid()) {
            deleteListValue();
        }
    });
    if (window.location.pathname == '/pages/ListMaintain.html') {
        if (typeof $.cookie('userid') !== 'undefined' && $.cookie('userid') != 'null') {
            getList();
        }
        else {
            alert('please login Agile first');
            window.location.replace("/");
        }
    }
});
$(document).on('click', '.w3-medium', function () {
    oListValue = $(this).find("td:eq(0)").text().trim();
    $("#listValue").val(oListValue);
    $(window).scrollTop(0);
    //console.log(url);
    //    w3.getHttpObject(url, displayRDetail);
});
//----------------------public js----------------------------
function w3_open() {
    document.getElementById("main").style.marginLeft = "20%";
    document.getElementById("mySidebar").style.width = "20%";
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("openNav").style.display = 'none';
}

function w3_close() {
    document.getElementById("main").style.marginLeft = "0%";
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("openNav").style.display = "inline-block";
}

function clearValue() {
    $("input:text").val("");
    $("select").val("");
}

function Loading() {
    document.getElementById('idResult').innerHTML = "Loading...";
    $("#idSpinner").show();
    $("#idLoader").show();
}

function initialize() {
    //    $("#idRTable").hide();
    $(".admin").hide();
    $(".dcc").hide();
    $("#result").hide();
    $(".login").show();
    $("#idBtnLogout").hide();
    //    $(".res").hide();
    //    $(".res").show();
}

function resultShow() {
    $("#result").show();
    $("#idSpinner").hide();
    $('#idLoader').delay(2000).fadeOut(0);
}

function textIsValid() {
    var sInput = $("#inputDesc").val();
    var patt = new RegExp("[\\\\/]");
    var res = patt.test(sInput);
    return res;
}
//----------------------compare js----------------------------
function Compare() {
    //    var urlBOM = serverAddress + "compareBOM/";
    var urlCKDID = serverAddress + "compareCKDID/";
    var sPN = $('#idForm').find("input[name='PN']").val();
    var sCPN = $('#idForm').find("input[name='CPN']").val();
    //    var urlFinalBOM = urlBOM + sPN + '&' + sCPN;
    var urlFinalCKDID = urlCKDID + sPN + '&' + sCPN;
    //    console.log(urlFinalBOM);
    //        console.log(urlFinalCKDID);
    Loading();
    //    w3.getHttpObject(urlFinalBOM, getResultBOM);
    w3.getHttpObject(urlFinalCKDID, getResultCKDID);
}

function getResultBOM(dataArray) {
    //    console.log(dataArray);
    //    console.log(result.isSuccess);
    if (dataArray != null) {
        if (dataArray.jsonName.length > 0) {
            $("#idSpinner").hide();
            document.getElementById('idResult').innerHTML = "select completed.";
            $('#idLoader').delay(1500).fadeOut('fast')
            w3.displayObject('idRTable', dataArray);
            $("#idRTable").show();
        }
        else {
            $("#idSpinner").hide();
            document.getElementById('idResult').innerHTML = "no data, please check.";
            $('#idLoader').delay(1500).fadeOut('fast')
            $("#idRTable").hide();
        }
    }
}

function getResultCKDID(dataArray) {
    //    console.log(dataArray);
    //    console.log(result.isSuccess);
    if (dataArray != null) {
        if (dataArray.jsonName.length > 0) {
            $("#idSpinner").hide();
            document.getElementById('idResult').innerHTML = "select completed.";
            $('#idLoader').delay(1500).fadeOut('fast')
            w3.displayObject('idRTable2', dataArray);
            $("#result").show();
            $("#btnCopy").show();
        }
        else {
            $("#idSpinner").hide();
            document.getElementById('idResult').innerHTML = "no data, please check.";
            $('#idLoader').delay(1500).fadeOut('fast')
            $("#idRTable2").hide();
        }
    }
    var sPN = $('#idForm').find("input[name='PN']").val();
    var sCPN = $('#idForm').find("input[name='CPN']").val();
    //    var urlFinalBOM = urlBOM + sPN + '&' + sCPN;
    $('#idPN1').text(sPN);
    $('#idCPN1').text(sCPN);
}
//----------------------inactivate js----------------------------
function Inactivate() {
    //    var urlBOM = serverAddress + "compareBOM/";
    var url = serverAddress + "inactivate/";
    var sCol1 = $('#idForm').find("input[name='col1']").val();
    var sCol2 = $('#idForm').find("input[name='col2']").val();
    //    var urlFinalBOM = urlBOM + sPN + '&' + sCPN;
    var urlFinal = url + sCol1 + '&' + sCol2;
    //    console.log(urlFinalBOM);
    //        console.log(urlFinalCKDID);
    Loading();
    $.get(urlFinal, function (res) {}).done(function (res) {
        $("#idResult").text('success: ' + res);
        $("#idResponse").text(res);
    }).fail(function (res) {
        $("#idResult").text('failed: ' + res);
        $("#idResponse").text(res);
    }).always(function () {
        //        alert("finished");
        resultShow();
    });
    //    w3.getHttpObject(urlFinalBOM, getResultBOM);
}
//--------------------list maintain js --------------------
function getList() {
    $(".req").show();
    var url = serverAddress + "getDropdownList";
    var urlFinal = url;
    w3.getHttpObject(urlFinal, function (res) {
        //        console.log(res.joR.length);
        //        console.log(urlFinal);
        if (res != null) {
            if (res.joR.length > 0) {
                w3.displayObject("dropdownList", res);
                $(".res").show();
                $(".req").hide();
            }
            else {
                $(".res").hide();
                $(".req").text("list not exsit, please check.");
            }
        }
        else {
            $(".res").hide();
            $(".req").text("Get dropdown list failed, please check.");
        }
    });
}

function getListValue() {
    $("#result").hide();
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var urlFinal = url + sListName;
    //    console.log(urlFinal);
    w3.getHttpObject(urlFinal, function (res) {
        //        console.log(res.joR.length);
        //        console.log(res);
        if (res != null) {
            if (res.joR.length > 0) {
                w3.displayObject("idRTable", res);
                $("#result").show();
            }
            else {
                alert("no data in this list.");
            }
        }
        else {
            alert("Get list value failed, please check.");
        }
    });
}

function addListValue() {
    $("#result").hide();
    Loading();
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sListValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sListValue;
    //    console.log(urlFinal);
    $("#idLoader").show();
    $.ajax({
        url: urlFinal
        , type: 'POST'
        , success: function (res) {
            $("#idResult").text('success: ' + res);
            getListValue()
            resultShow();
        }
        , error: function (res) {
            $("#idResult").text('failed: ' + res);
            resultShow();
        }
    })
}

function updateListValue() {
    $("#result").hide();
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sOlistValue = oListValue;
    var sNlistValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sOlistValue + '&' + sNlistValue;
    $("#idConfirm").show();
    $("#idConfirmText").text('are you sure update ' + sOlistValue + ' to ' + sNlistValue + '?');
    //    console.log(urlFinal);
    //    $("#idConfirm").show();
    $("#btnYes").click(function () {
        Loading();
        $("#idLoader").show();
        $("#idConfirm").hide();
        $.ajax({
            url: urlFinal
            , type: 'PUT'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue()
                oListValue = null
                resultShow();
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                oListValue = null
                resultShow();
            }
        })
    });
}

function deleteListValue() {
    $("#result").hide();
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sListValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sListValue;
    console.log(urlFinal);
    $("#idConfirm").show();
    $("#idConfirmText").text('are you sure delete ' + sListValue + '?');
    //    console.log(urlFinal);
    //    $("#idConfirm").show();
    $("#btnYes").click(function () {
        Loading();
        $("#idLoader").show();
        $("#idConfirm").hide();
        $.ajax({
            url: urlFinal
            , type: 'DELETE'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue()
                resultShow();
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                resultShow();
            }
        })
    });
}