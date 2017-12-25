

function refreshdetail() {
    console.log("getting device info...");
    var self = this;
    $.ajax({
        url: "/ios/getDeviceInfo?",
        method: "GET",
    })
        .then(function (ret) {
            self.device.udid = ret.udid
            self.device.name = ret.name
            self.device.apps = ret.apps
            self.device.type = ret.type
            self.device.version = ret.version
            console.log(udid)
            var typeString = self.device.type
            var realTypeName = iosinfo[typeString]["iPhoneType"]
            console.log(realTypeName)
            document.getElementById('deviceTypeName').textContent = realTypeName
            imgX = iosinfo[typeString]["ptX"]
            imgY = iosinfo[typeString]["ptY"]
        })
}