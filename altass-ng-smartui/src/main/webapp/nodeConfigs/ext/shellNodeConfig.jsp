<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2018/05/12
  Time: 8:43
  To change this template use FileInfo | Settings | FileInfo Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>Shell执行器</title>
    <base href="<%=basePath%>">
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="res/framework/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="res/framework/css/plugins/chosen/bootstrap-chosen.css" rel="stylesheet">
    <link href="res/framework/css/plugins/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/codemirror.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/ambiance.css" rel="stylesheet">
    <link href="res/framework/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
    <link href="res/framework/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="res/framework/css/plugins/dropzone/dropzone.css" rel="stylesheet">
    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">
</head>
<style>
    html {
        overflow-x: hidden;
    }

    .CodeMirror {
        border: 1px solid #eee;
        height: auto;
    }

    .CodeMirror-scroll {
        height: auto;
        overflow-y: hidden;
        overflow-x: auto;
    }
</style>
<body class="gray-bg">
<div class="tabs-container">
    <ul class="nav nav-tabs" style="position: fixed;z-index: 2;display: block;background: white;width: 100%;">
        <li class="active">
            <a data-toggle="tab" href="#script-configuration">Script</a>
        </li>

        <li class="">
            <a data-toggle="tab" href="#base-configuration">SSH Basic</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#public-params">公共参数</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#remark">备注</a>
        </li>
    </ul>
    <div class="tab-content" style="padding-top: 40px;">
        <div id="script-configuration" class="tab-pane active">
            <div class="panel-body" style="z-index: 1">
                <div class="form-group">
                    <textarea id="shell_code_area">

                    </textarea>
                </div>
            </div>
        </div>

        <div id="base-configuration" class="tab-pane">
            <div class="panel-body">
                <div class="form-group">
                    <div class="col-lg-12">
                        <div class="row" id="baseConfigPanel">
                            <form role="form" class="form-inline">
                                <div class="form-group">
                                    <label for="ssh_host" style="width: 110px;">Host</label>
                                    <input id="ssh_host" type="text" class="form-control" placeholder="127.0.0.1"
                                           style="z-index: 1;">
                                </div>

                                <div class="form-group">
                                    <label for="ssh_port">Port</label>
                                    <input id="ssh_port" type="text" class="form-control" placeholder="22"
                                           style="z-index: 1;width: 80px;">
                                </div>
                            </form>

                            <form role="form" class="form-inline">
                                <div class="form-group">
                                    <label for="ssh_user" style="width: 110px;">Login User</label>
                                    <input id="ssh_user" type="text" class="form-control" placeholder="login user"
                                           style="z-index: 1;">
                                </div>
                            </form>

                            <form role="form" class="form-inline">
                                <div class="form-group">
                                    <label for="ssh_passwd" style="width: 110px;">Login Password</label>
                                    <input id="ssh_passwd" type="password" class="form-control" placeholder="password"
                                           style="z-index: 1;">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="remark" class="tab-pane">
            <div class="panel-body">
                <div class="form-group">
                    <div class="col-lg-offset-2 col-lg-10">
                        <textarea class="form-control" placeholder="Write comment..." style="height: 200px;"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Chosen -->
<script src="res/framework/js/plugins/chosen/chosen.jquery.js"></script>

<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- CodeMirror -->
<script src="res/framework/js/plugins/codemirror/codemirror.js"></script>
<script src="res/framework/js/plugins/codemirror/mode/shell/shell.js"></script>
<script src="res/framework/js/plugins/jasny/jasny-bootstrap.min.js"></script>
<!-- DROPZONE -->
<script src="res/framework/js/plugins/dropzone/dropzone.js"></script>
<!-- MENU -->
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>


<script>
    Dropzone.options.dropzoneForm = {
        paramName: "file", // The name that will be used to transfer the file
        maxFilesize: 2, // MB
        dictDefaultMessage: "<strong>Drop files here or click to upload. </strong></br> (This is just a demo dropzone. Selected files are not actually uploaded.)"
    };
</script>


<script>
    var command = [];

    $(function () {
        // 代码编辑器的初始化操作
        codeEditor = CodeMirror.fromTextArea(document.getElementById("shell_code_area"), {
            lineNumbers: true,
            matchBrackets: true,
            styleActiveLine: true,
            theme: "ambiance",
            tabSize: 8,
            readOnly: false,
            mode: "shell"
        });

        codeEditor.setValue('#!/bin/bash\n\n\n\n\n\n\n\n\n');
    });
</script>

<script type="text/javascript">
    var nodeConfiguration = undefined;

    // 节点配置页面初始化完毕
    function onInit(data) {
        nodeConfiguration = data;
        console.log(nodeConfiguration);
        var common = nodeConfiguration.common;
        var httpConfig = common.extAttr.httpConfig;

        var url = httpConfig.url;

        $('#requestUrl').val(url);

    }

    // 用于父页面收集节点配置信息
    function collectParams() {
        var host = $('#ssh_host').val();
        var port = $('#ssh_port').val();
        var user = $('#ssh_user').val();
        var passwd = $('#ssh_passwd').val();
        var script = codeEditor.getValue();

        // 提交数据
        var param = {
            common: {
                extAttr: {
                    aSSH: {
                        host: {
                            host: host,
                            port: port,
                            user: user,
                            password: passwd
                        },
                        command: script,
                        expectResult: 0
                    }
                }
            }
        };
        return param;
    }

    // 提交结果
    function onSubmit(ret) {
        console.log("当前操作作业：" + JSON.stringify(ret) + " | 当前操作节点：");
    }

    // 取消事件
    function onCancel() {
    }
</script>

</html>
