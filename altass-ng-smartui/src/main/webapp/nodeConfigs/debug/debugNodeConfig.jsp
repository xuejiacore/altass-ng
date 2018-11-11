<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2017/2/14
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
    <title>Debug执行器</title>
    <base href="<%=basePath%>">
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="res/framework/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="res/framework/css/plugins/chosen/bootstrap-chosen.css" rel="stylesheet">
    <link href="res/framework/css/plugins/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
    <link href="res/framework/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">
</head>
<style>
    html {
        overflow-x: hidden;
    }
</style>
<body class="gray-bg">
<div class="tabs-container">
    <ul class="nav nav-tabs" style="position: fixed;z-index: 2;display: block;background: white;width: 100%;">
        <li class="active">
            <a data-toggle="tab" href="#base-configuration">Debug配置</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#public-params">公共参数</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#remark">备注</a>
        </li>
    </ul>
    <div class="tab-content" style="padding-top: 40px;">
        <div id="base-configuration" class="tab-pane active">
            <div class="panel-body">
                <div class="form-group">
                    <div class="col-lg-12">
                        <div class="row" id="baseConfigPanel">
                            <form role="form">
                                <div class="form-group">
                                    <label>异常配置：</label>
                                    <div class="input-group m-b">
                                        <div class="input-group-btn">
                                            <button id="exception" tabindex="-1" class="btn btn-white" type="button"
                                                    style="width: 80px;" value="false"><i><b>无异常</b></i>
                                            </button>
                                            <button data-toggle="dropdown"
                                                    class="btn btn-white dropdown-toggle"
                                                    type="button" aria-expanded="false" style="height: 34px;"><span
                                                    class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li><a href="javascript:void(0);"
                                                       onclick="changeRequestMethod('无异常');">无异常</a>
                                                </li>
                                                <li><a href="javascript:void(0);"
                                                       onclick="changeRequestMethod('抛出异常');">抛出异常</a></li>
                                            </ul>
                                        </div>
                                        <input id="exceptionDesc" type="text" class="form-control" placeholder="请输入异常信息"
                                               style="z-index: 1;">
                                    </div>
                                </div>
                            </form>
                            <form role="form" class="form-inline">
                                <div class="form-group">
                                    <label for="hour">延时配置：</label>
                                    <input style="width: 70px;" placeholder="0 小时" id="hour" class="form-control"
                                           type="text" value="0">
                                </div>

                                <div class="form-group">
                                    <label for="minutes">-</label>
                                    <input style="width: 70px;" placeholder="0 分钟" id="minutes" class="form-control"
                                           type="text" value="0">
                                </div>

                                <div class="form-group">
                                    <label for="seconds">-</label>
                                    <input style="width: 70px;" placeholder="0 秒" id="seconds" class="form-control"
                                           type="text" value="2">
                                </div>
                            </form>
                            <form role="form" class="form-inline">
                                <div class="form-group">
                                    <label>扩展配置：</label>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="public-params" class="tab-pane">
            <div class="panel-body">
                <div class="form-group">
                    <form role="form" id="publicKeyValue0" class="form-inline">
                        <div class="input-group bootstrap-touchspin form-inline">
                                    <span class="input-group-btn">
                                        <button class="btn btn-white bootstrap-touchspin-down" type="button">-</button>
                                    </span>

                            <div class="form-group">
                                <label for="publicParamKey0" class="sr-only">参数名</label>
                                <input type="text" placeholder="Param Key" id="publicParamKey0"
                                       class="form-control">
                            </div>&nbsp;
                            <div class="form-group">
                                <label for="publicParamValue0" class="sr-only">参数值</label>
                                <input type="text" placeholder="Param Value" id="publicParamValue0"
                                       class="form-control">
                            </div>

                            <span class="input-group-btn">
                                        <button class="btn btn-white bootstrap-touchspin-up" type="button"
                                                onclick="appendParamKeyValue();">+</button>
                                    </span>
                        </div>
                    </form>
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
<script type="text/javascript">

    function changeRequestMethod(method) {
        var exceptionBtn = $('#exception');
        if (method === '无异常') {
            exceptionBtn.val('false');
        } else {
            exceptionBtn.val('true')
        }
        exceptionBtn.html("<i><b>" + method + "</b></i>");
    }

</script>

<script type="text/javascript">
    var nodeConfiguration = undefined;

    // 节点配置页面初始化完毕
    function onInit(data) {
        nodeConfiguration = data;

        var year = nodeConfiguration.time.year;
        var month = nodeConfiguration.time.month;
        var day = nodeConfiguration.time.day;
        var hour = nodeConfiguration.time.hour;
        var minutes = nodeConfiguration.time.minutes;
        var seconds = nodeConfiguration.time.seconds;

    }

    // 用于父页面收集节点配置信息
    function collectParams() {
        var hour = $("#hour").val();
        var minutes = $("#minutes").val();
        var seconds = $("#seconds").val();

        var secondsSum = parseInt(hour * 60 * 60) + parseInt(minutes * 60) + parseInt(seconds);
        // 提交数据
        return {
            common: {
                extAttr: {
                    debugConfig: {
                        throwError: $('#exception').val() === 'true',
                        exceptionDesc: $('#exceptionDesc').val(),
                        delay: secondsSum
                    }
                }
            }
        };
    }

    // 提交结果
    function onSubmit(ret) {
        console.log("当前操作作业：" + JSON.stringify(ret) + " | 当前操作节点：");
    }

    // 取消事件
    function onCancel() {
    }
</script>

<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Chosen -->
<script src="res/framework/js/plugins/chosen/chosen.jquery.js"></script>
<!-- JSKnob -->
<%--<script src="res/framework/js/plugins/jsKnob/jquery.knob.js"></script>--%>
<!-- Input Mask-->
<%--<script src="res/framework/js/plugins/jasny/jasny-bootstrap.min.js"></script>--%>
<!-- Data picker -->
<%--<script src="res/framework/js/plugins/datapicker/bootstrap-datepicker.js"></script>--%>
<!-- NouSlider -->
<%--<script src="res/framework/js/plugins/nouslider/jquery.nouislider.min.js"></script>--%>
<!-- Switchery -->
<%--<script src="res/framework/js/plugins/switchery/switchery.js"></script>--%>
<!-- IonRangeSlider -->
<%--<script src="res/framework/js/plugins/ionRangeSlider/ion.rangeSlider.min.js"></script>--%>
<!-- iCheck -->
<%--<script src="res/framework/js/plugins/iCheck/icheck.min.js"></script>--%>

<!-- MENU -->
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<!-- Color picker -->
<%--<script src="res/framework/js/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>--%>
<!-- Clock picker -->
<%--<script src="res/framework/js/plugins/clockpicker/clockpicker.js"></script>--%>
<!-- Image cropper -->
<%--<script src="res/framework/js/plugins/cropper/cropper.min.js"></script>--%>
<!-- Date range use moment.js same as full calendar plugin -->
<%--<script src="res/framework/js/plugins/fullcalendar/moment.min.js"></script>--%>
<!-- Date range picker -->
<%--<script src="res/framework/js/plugins/daterangepicker/daterangepicker.js"></script>--%>
<!-- Select2 -->
<%--<script src="res/framework/js/plugins/select2/select2.full.min.js"></script>--%>
<!-- TouchSpin -->
<%--<script src="res/framework/js/plugins/touchspin/jquery.bootstrap-touchspin.min.js"></script>--%>
<!-- Tags Input -->
<%--<script src="res/framework/js/plugins/bootstrap-tagsinput/bootstrap-tagsinput.js"></script>--%>
<!-- Dual Listbox -->
<%--<script src="res/framework/js/plugins/dualListbox/jquery.bootstrap-duallistbox.js"></script>--%>
</html>
