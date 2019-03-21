//var serverAddress = 'http://localhost:8080/PLMWeb/';
var serverAddress = 'http://plmweb.tech-man.com.cn:8080/PLMWeb/';
//var serverAddress = 'http://192.168.99.100:8080/PLMWeb/'; //docker IP
var oListValue = null;
//----------------------main function---------------------
$(function () {
    initialize();
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
        //        console.log($("#dropdownList").val());
        if ($("#dropdownList").val() != "") {
            getListValue();
        }
        else {
            alert("pls select a list")
        }
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
            if ($("#listValue").val() != "") {
                deleteListValue();
            }
        }
        else {
            alert("pls select a row")
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
function selectElementContents(el) {
    var body = document.body
        , range, sel;
    if (document.createRange && window.getSelection) {
        range = document.createRange();
        sel = window.getSelection();
        sel.removeAllRanges();
        try {
            range.selectNodeContents(el);
            sel.addRange(range);
        }
        catch (e) {
            range.selectNode(el);
            sel.addRange(range);
        }
    }
    else if (body.createTextRange) {
        range = body.createTextRange();
        range.moveToElementText(el);
        range.select();
    }
    document.execCommand("Copy");
    alert('table copied.')
}
/*function w3_open() {
    document.getElementById("main").style.marginLeft = "20%";
    document.getElementById("mySidebar").style.width = "20%";
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("openNav").style.display = 'none';
}*/
/*function w3_close() {
    document.getElementById("main").style.marginLeft = "0%";
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("openNav").style.display = "inline-block";
}*/
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
    //    $(".admin").hide();
    //    $(".dcc").hide();
    //    $("#result").hide();
    //    $(".login").show();
    //    $("#idBtnLogout").hide();
    //    $(".res").hide();
    //    $(".res").show();
}

function resultShow() {
    //    setTimeout(function () {
    //        window.location.replace("/pages/ListMaintain.html");
    //    }, 2000);
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
    //    console.log(urlFinal);
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
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sOlistValue = oListValue;
    var sNlistValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sOlistValue + '&' + sNlistValue;
    $("#idConfirmUpdate").show();
    $("#idConfirmTextUpdate").text('are you sure update ' + sOlistValue + ' to ' + sNlistValue + '?');
    //    console.log(urlFinal);
    //    $("#idConfirm").show();
    $("#btnYesUpdate").unbind().bind('click', function () {
        //        var aa = $(this);
        $("#result").hide();
        Loading();
        $("#idLoader").show();
        $("#idConfirmUpdate").hide();
        //        console.log(urlFinal);
        //        $("#btnYesUpdate").prop('disabled', true);
        $.ajax({
            url: urlFinal
            , type: 'PUT'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue();
                oListValue = null;
                resultShow();
                //                aa.prop('disabled', false);
                //                                $("#btnYesUpdate").prop('disabled', false);
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                oListValue = null;
                //                window.location.replace("/pages/ListMaintain.html");
                resultShow();
                //                aa.prop('disabled', false);
                //                                $("#btnYesUpdate").prop('disabled', false);
            }
        })
    });
}

function deleteListValue() {
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    //    var sOlistValue = oListValue;
    var sListValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sListValue;
    //    var htmlname = document.location.href.match(/[^\/]+$/)[0];
    //    console.log(urlFinal);
    $("#idConfirmDelete").show();
    $("#idConfirmTextDelete").text('are you sure delete ' + sListValue + '?');
    //    console.log(urlFinal);
    //    $("#idConfirm").show();
    $("#btnYesDelete").unbind().bind('click', function () {
        //        if(htmlname=='ListMaintain.html'){}
        $("#result").hide();
        Loading();
        $("#idLoader").show();
        $("#idConfirmDelete").hide();
        //        console.log(222);
        $.ajax({
            url: urlFinal
            , type: 'DELETE'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue();
                //                oListValue = null;
                resultShow();
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                //                oListValue = null;
                resultShow();
            }
        })
    });
}