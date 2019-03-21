//var serverAddress = 'http://localhost:8080/PLMWeb/';
var serverAddress = 'http://plmweb.tech-man.com.cn:8080/PLMWeb/';
//var serverAddress = 'http://192.168.99.100:8080/PLMWeb/'; //docker IP
var oListValue = null;
//----------------------main function---------------------
$(function () {
    $("#btnCompare").click(function () {
        if ($('#idForm').valid()) { //check if required information already input
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
    if (window.location.pathname == '/pages/ListMaintain.html') { //check if user already login
        if (typeof $.cookie('userid') !== 'undefined' && $.cookie('userid') != 'null') {
            getList();
        }
        else {
            alert('please login Agile first');
            window.location.replace("/");
        }
    }
});
$(document).on('click', '.w3-medium', function () { //click result it will auto setvalue to listvalue for update and delete use
    oListValue = $(this).find("td:eq(0)").text().trim();
    $("#listValue").val(oListValue);
    $(window).scrollTop(0);
});
//----------------------public js----------------------------
function selectElementContents(el) { //auto copy result to clipborad for use to paste to Excel
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

function clearValue() { //clean up all input value
    $("input:text").val("");
    $("select").val("");
}

function Loading() { //show loading div
    document.getElementById('idResult').innerHTML = "Loading...";
    $("#idSpinner").show();
    $("#idLoader").show();
}

function resultShow() { //show result data after CRUD
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
    var urlCKDID = serverAddress + "compareCKDID/";
    var sPN = $('#idForm').find("input[name='PN']").val();
    var sCPN = $('#idForm').find("input[name='CPN']").val();
    var urlFinalCKDID = urlCKDID + sPN + '&' + sCPN;
    Loading();
    w3.getHttpObject(urlFinalCKDID, getResultCKDID); //ajax GET to back-end
}

function getResultBOM(dataArray) {
    if (dataArray != null) {
        if (dataArray.jsonName.length > 0) {
            $("#idSpinner").hide();
            document.getElementById('idResult').innerHTML = "select completed.";
            $('#idLoader').delay(1500).fadeOut('fast')
            w3.displayObject('idRTable', dataArray); //display data from previous ajax GET 
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
    $('#idPN1').text(sPN);
    $('#idCPN1').text(sCPN);
}
//----------------------inactivate js----------------------------
function Inactivate() {
    var url = serverAddress + "inactivate/";
    var sCol1 = $('#idForm').find("input[name='col1']").val();
    var sCol2 = $('#idForm').find("input[name='col2']").val();
    var urlFinal = url + sCol1 + '&' + sCol2;
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
}
//--------------------list maintain js --------------------
function getList() { //get dropdown list for user to select in ListMaintain
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

function getListValue() { // ListMaintain Query
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

function addListValue() { // ListMaintain Add
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

function updateListValue() { // ListMaintain Update
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sOlistValue = oListValue;
    var sNlistValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sOlistValue + '&' + sNlistValue;
    $("#idConfirmUpdate").show();
    $("#idConfirmTextUpdate").text('are you sure update ' + sOlistValue + ' to ' + sNlistValue + '?');
    $("#btnYesUpdate").unbind().bind('click', function () {
        $("#result").hide();
        Loading();
        $("#idLoader").show();
        $("#idConfirmUpdate").hide();
        $.ajax({
            url: urlFinal
            , type: 'PUT'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue();
                oListValue = null;
                resultShow();
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                oListValue = null;
                resultShow();
            }
        })
    });
}

function deleteListValue() { // ListMaintain Delete
    var url = serverAddress + "getListValue/";
    var sListName = $("#dropdownList").val();
    var sListValue = $("#listValue").val();
    var urlFinal = url + sListName + '&' + sListValue;
    $("#idConfirmDelete").show();
    $("#idConfirmTextDelete").text('are you sure delete ' + sListValue + '?');
    $("#btnYesDelete").unbind().bind('click', function () { //unbind.bind is a method to avoid duplicate ajax request on click
        $("#result").hide();
        Loading();
        $("#idLoader").show();
        $("#idConfirmDelete").hide();
        $.ajax({
            url: urlFinal
            , type: 'DELETE'
            , success: function (res) {
                $("#idResult").text('success: ' + res);
                getListValue();
                resultShow();
            }
            , error: function (res) {
                $("#idResult").text('failed: ' + res);
                resultShow();
            }
        })
    });
}