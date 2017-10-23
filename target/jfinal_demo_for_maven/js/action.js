function home() {
    $.ajax({
        url: "/ios/home",
        method: "GET",
    })
        .then(function (ret) {
            console.log(ret);
        })
}

function launchApp() {
    var id = document.getElementById('bundleid').value
    if (id != "") {
        $.ajax({
            url: "/ios/launchapp?id=" + id,
            method: "GET",

        })
            .then(function (ret) {
                console.log(ret);
            })
    }
}

function uninstallApp() {
    var id = document.getElementById('uninstallid').value
    if (id != "") {
        $.ajax({
            url: "/ios/uninstall?id=" + id,
            method: "GET",

        })
            .then(function (ret) {
                console.log(ret);
            })
    }
}

function getDeviceInfo() {
    var udid = "";
    $.ajax({
        url: "/ios/getDeviceInfo?",
        method: "GET",
    })
        .then(function (ret) {
            udid = ret.udid
            console.log(udid)
        })
}

function sendText() {
    var sendValue = encodeURI(encodeURI(document.getElementById('input').value))
    console.log("Input Text = " + sendValue)
    $.ajax({
        url: "/ios/inputText?value=" + sendValue,
        method: "GET",

    })
        .then(function (ret) {
            console.log(ret);
        })
}

function clearText() {
    $.ajax({
        url: "/ios/clear",
        method: "GET",
    })
        .then(function (ret) {
            console.log(ret);
        })
}