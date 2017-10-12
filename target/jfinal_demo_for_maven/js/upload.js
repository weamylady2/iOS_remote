$(document).ready(function() {
    $('#upload').click(function() {
        upload();
    });
});
function upload() {

    $.ajaxFileUpload({
        url : '/ios/upload',   //提交的路径
        type: 'post',
        secureuri : false, // 是否启用安全提交，默认为false
        fileElementId : 'uploadfile', // file控件id
        dataType : 'json',
        data:{

        },
        success : function(data, status) {
            console.log(data);
            console.log(status);
        },
        error : function(data, status) {
            alert("上传失败");
        }
    });
}