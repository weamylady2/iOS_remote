
var deviceDetail = new Vue({
    el: "#deviceinfo",
    data: {
        device: {
            name: '',
            udid: '',
            apps: '',
        },
    },
    mounted: function () {
        this.refresh();

    },
    methods: {
        refresh: function () {
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
                    console.log(udid)
                })
        },

    },
});

function refreshdetail() {
    console.log("getting device info...");
    $.ajax({
        url: "/ios/getDeviceInfo?",
        method: "GET",
    })
        .then(function (ret) {
            deviceDetail.device.udid = ret.udid
            deviceDetail.device.name = ret.name
            deviceDetail.device.apps = ret.apps
            console.log(udid)
        })
}