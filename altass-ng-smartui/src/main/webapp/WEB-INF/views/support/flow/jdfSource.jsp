<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>作业${jobId}</title>
    <base href="<%=basePath%>">

    <link href="https://git.oschina.net/assets/favicon-f6562a1bc6a110e32367f6e0cab4ba89.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/codemirror.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/ambiance.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">

</head>

<body>
<textarea id="code1" rows="300">

</textarea>

<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- CodeMirror -->
<script src="res/framework/js/plugins/codemirror/codemirror.js"></script>
<script src="res/framework/js/plugins/codemirror/mode/javascript/javascript.js"></script>

<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>


<script>
    var command = [];

    $(function () {
        // 代码编辑器的初始化操作
        codeEditor = CodeMirror.fromTextArea(document.getElementById("code1"), {
            lineNumbers: true,
            matchBrackets: true,
            styleActiveLine: true,
            theme: "ambiance",
            tabSize: 8,
            readOnly: true
        });

        $.post("/jobService/jdfContent", {
            jobId: '${jobId}'
        }, function (data) {
            console.log(data);
            if (data.flag) {
                codeEditor.setValue(data.result);
            }
        }, "json");
    });
</script>

</body>
</html>
