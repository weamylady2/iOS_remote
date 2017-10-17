$(document).ready(function() {
    $('#upload').click(function() {
        upload();
    });
});
function upload() {
    var file = document.getElementById("uploadfile");
    var fileInfo = file.files[0];
    if(!fileInfo){
        alert("请选择文件！");
        return false;
    }

    var fileName = fileInfo.name;
//获取文件后缀名
    var file_typename = fileName.substring(
        fileName.lastIndexOf('.') + 1, fileName.length);
    if(file_typename.toLowerCase() != 'ipa'){
        alert("请上传.ipa类型的文件！");
        return false;
    }

    try{
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
    }catch(err) {
        alert("请上传.ipa文件，不要上传其它类型！" + err.message);
    }

}