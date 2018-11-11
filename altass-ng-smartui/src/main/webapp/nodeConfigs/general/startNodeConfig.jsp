<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2017/2/9
  Time: 10:47
  To change this template use FileInfo | Settings | FileInfo Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>开始节点</title>
    <base href="<%=basePath%>">
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="res/framework/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="res/framework/css/plugins/chosen/bootstrap-chosen.css" rel="stylesheet">
    <link href="res/framework/css/plugins/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">
    <%--<link href="res/framework/css/plugins/colorpicker/bootstrap-colorpicker.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/cropper/cropper.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/switchery/switchery.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/nouslider/jquery.nouislider.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/datapicker/datepicker3.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/ionRangeSlider/ion.rangeSlider.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/ionRangeSlider/ion.rangeSlider.skinFlat.css" rel="stylesheet">--%>
    <link href="res/framework/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
    <%--<link href="res/framework/css/plugins/clockpicker/clockpicker.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/select2/select2.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/touchspin/jquery.bootstrap-touchspin.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/dualListbox/bootstrap-duallistbox.min.css" rel="stylesheet">--%>
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
            <a data-toggle="tab" href="#base-configuration">作业参数</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#remark">作业配置</a>
        </li>
    </ul>
    <div class="tab-content" style="padding-top: 40px;">
        <div id="base-configuration" class="tab-pane active">
            <div class="panel-body">
                <div class="form-group">
                    <label class="col-lg-2 control-label">Email</label>
                    <div class="col-lg-10">
                        <input type="email" placeholder="Email" class="form-control">
                        <span class="help-block m-b-none">Example block-level help text here.</span>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-lg-2 control-label">Password</label>
                    <div class="col-lg-10">
                        <input type="password" placeholder="Password" class="form-control">
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-offset-2 col-lg-10">
                        <div class="i-checks">
                            <label>
                                <input type="checkbox">
                                <i></i> Remember me
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-lg-offset-2 col-lg-10">
                        <button class="btn btn-sm btn-white" type="submit">Sign in</button>
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
