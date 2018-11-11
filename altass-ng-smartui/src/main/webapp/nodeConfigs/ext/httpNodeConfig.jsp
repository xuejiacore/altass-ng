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
    <title>Http执行器</title>
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
    <link href="res/framework/css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
    <%--<link href="res/framework/css/plugins/clockpicker/clockpicker.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/select2/select2.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/touchspin/jquery.bootstrap-touchspin.min.css" rel="stylesheet">--%>
    <%--<link href="res/framework/css/plugins/dualListbox/bootstrap-duallistbox.min.css" rel="stylesheet">--%>
    <link href="res/framework/css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="res/framework/css/plugins/dropzone/dropzone.css" rel="stylesheet">
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
            <a data-toggle="tab" href="#base-configuration">Http Basic</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#base-configuration">Http Header</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#base-configuration">ContentBody</a>
        </li>
        <li class="">
            <a data-toggle="tab" href="#advanced-configuration">高级</a>
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
                                <div class="m-b m-r" style="float: left;"><b>数据类型：</b></div>
                                <div class="radio radio-info radio-inline m-b">
                                    <input type="radio" id="jsonDataType" value="json" name="dataTypeRadio"
                                           checked="checked">
                                    <label for="jsonDataType">json</label>
                                </div>
                                <div class="radio radio-info radio-inline m-b">
                                    <input type="radio" id="textDataType" value="text/html" name="dataTypeRadio">
                                    <label for="textDataType">text/html</label>
                                </div>
                                <div class="radio radio-info radio-inline m-b">
                                    <input type="radio" id="xmlDataType" value="xml" name="dataTypeRadio">
                                    <label for="xmlDataType">xml</label>
                                </div>

                                <div class="input-group m-b">
                                    <div class="input-group-btn">
                                        <button id="requestMethod" tabindex="-1" class="btn btn-white" type="button"
                                                style="width: 80px;" value="GET"><i><b>GET</b></i>
                                        </button>
                                        <button data-toggle="dropdown"
                                                class="btn btn-white dropdown-toggle"
                                                type="button" aria-expanded="false" style="height: 34px;"><span
                                                class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a href="javascript:void(0);"
                                                   onclick="changeRequestMethod('GET');">GET</a>
                                            </li>
                                            <li><a href="javascript:void(0);"
                                                   onclick="changeRequestMethod('POST');">POST</a></li>
                                            <li class="divider"></li>
                                            <li><a href="javascript:void(0);"
                                                   onclick="changeRequestMethod('PUT');">PUT</a>
                                            </li>
                                            <li><a href="javascript:void(0);"
                                                   onclick="changeRequestMethod('DELETE');">DELETE</a></li>
                                        </ul>
                                    </div>
                                    <input id="requestUrl" type="text" class="form-control" placeholder="http://"
                                           value="http://"
                                           style="z-index: 1;">
                                </div>
                            </form>

                            <form role="form" name="paramKeyValueForm" id="paramKeyValue0" class="form-inline">
                                <div class="input-group bootstrap-touchspin form-inline">
                                    <span class="input-group-btn">
                                        <button class="btn btn-white bootstrap-touchspin-down" type="button">-</button>
                                    </span>

                                    <div class="form-group">
                                        <label for="paramKey0" class="sr-only">参数名</label>
                                        <input type="text" placeholder="Param Key" name="paramKey" id="paramKey0"
                                               class="form-control">
                                    </div>&nbsp;
                                    <div class="form-group">
                                        <label for="paramValue0" class="sr-only">参数值</label>
                                        <input type="text" placeholder="Param Value" name="paramValue" id="paramValue0"
                                               class="form-control">
                                    </div>

                                    <span class="input-group-btn">
                                        <button class="btn btn-white bootstrap-touchspin-up" type="button"
                                                onclick="appendParamKeyValue();">+</button>
                                    </span>

                                    <div class="checkbox m-r-xs">
                                        <input type="checkbox" id="encodeCkb0">
                                        <label for="encodeCkb0">Encode</label>
                                    </div>
                                    <div class="checkbox m-r-xs">
                                        <input type="checkbox" id="autowire0">
                                        <label for="autowire0">Autowire</label>
                                    </div>
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
                                <label for="paramKey0" class="sr-only">参数名</label>
                                <input type="text" placeholder="Param Key" id="publicParamKey0"
                                       class="form-control">
                            </div>&nbsp;
                            <div class="form-group">
                                <label for="paramValue0" class="sr-only">参数值</label>
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

        <div id="advanced-configuration" class="tab-pane">
            <div class="panel-body">
                <div class="form-group">
                    <form role="form" class="form-inline">
                        <div class="checkbox m-r-xs">
                            <input type="checkbox" id="checkbox1">
                            <label for="checkbox1">使用参数数据源</label>
                        </div>
                        <div class="checkbox m-r-xs">
                            <input type="checkbox" id="checkbox2">
                            <label for="checkbox1">是否包含列名</label>
                        </div>

                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">分隔符：</div>
                        <div class="form-group">
                            <label for="exampleInputEmail2" class="sr-only">分隔符</label>
                            <input type="email" placeholder="\t" id="exampleInputEmail2" class="form-control">
                        </div>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">栏位限定符：</div>
                        <div class="form-group">
                            <label for="exampleInputPassword2" class="sr-only">栏位限定符</label>
                            <input type="password" placeholder="无" id="exampleInputPassword2"
                                   class="form-control">
                        </div>
                    </form>
                </div>

                <div class="form-group">
                    <div class="col-ls-10">
                        <div class="fileinput input-group fileinput-new" data-provides="fileinput">
                            <div class="form-control" data-trigger="fileinput">
                                <i class="glyphicon glyphicon-file fileinput-exists"></i>
                                <span class="fileinput-filename"></span>
                            </div>
                            <span class="input-group-addon btn btn-default btn-file">
                            <span class="fileinput-new">选择参数源</span>
                            <span class="fileinput-exists">更改</span>
                            <input type="hidden" value="" name="...">
                            <input type="file" name=""></span>
                            <a href="#" class="input-group-addon btn btn-default fileinput-exists"
                               data-dismiss="fileinput">删除</a>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <form role="form" id="columnDescForm" class="form-inline">
                        <div class="input-group bootstrap-touchspin form-inline">
                            <span class="input-group-btn">
                                <button class="btn btn-white bootstrap-touchspin-down" type="button">-</button>
                            </span>

                            <div class="form-group">
                                <label for="paramKey0" class="sr-only">列索引</label>
                                <input type="text" placeholder="列索引:0, 1, ..." id="columnIdx"
                                       class="form-control">
                            </div>&nbsp;
                            <div class="form-group">
                                <label for="paramValue0" class="sr-only">对应参数名</label>
                                <input type="text" placeholder="映射参数" id="参数名"
                                       class="form-control">
                            </div>

                            <span class="input-group-btn">
                                <button class="btn btn-white bootstrap-touchspin-up" type="button" onclick="">+</button>
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

    var httpParamsIdx = 1;

    function changeRequestMethod(method) {
        var reqBtn = $('#requestMethod');
        reqBtn.val(method);
        reqBtn.html("<i><b>" + method + "</b></i>");
    }

    function appendParamKeyValue() {
        var index = httpParamsIdx++;

        var formId = 'paramKeyValue' + index;
        var keyInputId = 'paramKey' + index;
        var valueInputId = 'paramValue' + index;
        var encodeCkbId = 'encodeCkb' + index;
        var autowireId = 'autowire' + index;

        var paramKVTemplate = '<form role="form" name="paramKeyValueForm" id="' + formId + '" class="form-inline">\
                                    <div class="input-group bootstrap-touchspin form-inline">\
                                        <span class="input-group-btn">\
                                            <button class="btn btn-white bootstrap-touchspin-down" type="button" onclick="$(\'#' + formId + '\').remove();">-</button>\
                                        </span>\
                                        <div class="form-group">\
                                            <label for="' + keyInputId + '" class="sr-only">参数名</label>\
                                            <input type="text" placeholder="Param Key" name="paramKey" id="' + keyInputId + '" class="form-control">\
                                        </div>&nbsp;\
                                        <div class="form-group">\
                                            <label for="' + valueInputId + '" class="sr-only">参数值</label>\
                                            <input type="text" placeholder="Param Value" name="paramValue" id="' + valueInputId + '" class="form-control">\
                                        </div>\
                                        <span class="input-group-btn">\
                                            <button class="btn btn-white bootstrap-touchspin-up" type="button" onclick="appendParamKeyValue();">+</button>\
                                        </span>\
                                        <div class="checkbox m-r-xs">\
                                            <input type="checkbox" id="' + encodeCkbId + '">\
                                            <label for="' + encodeCkbId + '">Encode</label>\
                                        </div>\
                                        <div class="checkbox m-r-xs">\
                                            <input type="checkbox" id="' + autowireId + '">\
                                            <label for="' + autowireId + '">Autowire</label>\
                                        </div>\
                                    </div>\
                                </form>';

        $('#baseConfigPanel').append(paramKVTemplate);
    }

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
        var dataType = httpConfig.dataType;

        $('#requestUrl').val(url);
        console.log(dataType.toLowerCase(), dataType.toLowerCase().indexOf('json'));
        if (dataType.toLowerCase().indexOf('json') >= 0) {
            $('#jsonDataType').trigger('click');
        } else if (dataType.toLowerCase().indexOf('xml') >= 0) {
            $('#xmlDataType').trigger('click');
        } else {
            $('#textDataType').trigger('click');
        }
    }

    // 用于父页面收集节点配置信息
    function collectParams() {
        var method = $('#requestMethod').val();
        var url = $('#requestUrl').val();
        var dataType = $("input[name='dataTypeRadio']:checked").val();

        // 获得所有参数表单中的数据
        var forms = $('form[name=paramKeyValueForm]');
        var params = {};
        for (var i = 0; i < forms.length; i++) {
            var form = $(forms[i]);
            var key = form.find('input[name=paramKey]').val();
            var value = form.find('input[name=paramValue]').val();
            params[key] = value.split(',');
        }

        // 提交数据
        var param = {
            common: {
                extAttr: {
                    httpConfig: {
                        "method": method,
                        "url": url,
                        "params": params,
                        "header": {
//                            "Content-Type": "application/json",
//                            "Cookies": "_mc=123g;_mu=123sfas"
                        },
                        "dataType": dataType
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
<!-- Jasny -->
<script src="res/framework/js/plugins/jasny/jasny-bootstrap.min.js"></script>
<!-- DROPZONE -->
<script src="res/framework/js/plugins/dropzone/dropzone.js"></script>
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


<script>
    Dropzone.options.dropzoneForm = {
        paramName: "file", // The name that will be used to transfer the file
        maxFilesize: 2, // MB
        dictDefaultMessage: "<strong>Drop files here or click to upload. </strong></br> (This is just a demo dropzone. Selected files are not actually uploaded.)"
    };
</script>
</html>
