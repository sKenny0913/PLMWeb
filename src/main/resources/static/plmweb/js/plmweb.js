//var serverAddress = 'http://localhost:8080/PLMWeb/';
var serverAddress = 'http://plmweb.tech-man.com.cn:8080/PLMWeb/';
//var serverAddress = 'http://192.168.99.100:8080/PLMWeb/'; //docker IP
$(function () {
    initialize();
    $("#nav-placeholder").load("../pages/nav.html", function () {});
    $("#loading-placeholder").load("../pages/loading.html", function () {});
    $("#btnCompare").click(function () {
        if ($('#idForm').valid()) {
            Compare();
        }
    });
    $("#btnInactivate").click(function () {
        if ($('#idForm').valid()) {
            Inactivate();
        }
    });
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
}
//------------------------------------------------------------
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
//------------------------------------------------------------
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
        document.getElementById('idResult').innerHTML = res;
        $("#idResponse").text(res);
    }).fail(function (res) {
        document.getElementById('idResult').innerHTML = res;
        $("#idResponse").text(res);
    }).always(function () {
        //        alert("finished");
        $("#result").show();
        $("#idSpinner").hide();
        $('#idLoader').delay(1000).fadeOut('fast')
    });;
    //    w3.getHttpObject(urlFinalBOM, getResultBOM);
}