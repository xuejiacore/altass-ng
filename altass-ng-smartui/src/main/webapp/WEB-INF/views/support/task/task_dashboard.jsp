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

    <title>节点 ${nodeId} 日志</title>
    <base href="<%=basePath%>">

    <link href="https://git.oschina.net/assets/favicon-f6562a1bc6a110e32367f6e0cab4ba89.ico" rel="shortcut icon"
          type="image/vnd.microsoft.icon"/>
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/codemirror.css" rel="stylesheet">
    <link href="res/framework/css/plugins/codemirror/ambiance.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">
    <link href="res/framework/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
    <style type="text/css">
        .pull-right button {
            margin-bottom: 2px;
        }

        .elements-list .list-group-item.active, .elements-list .list-group-item:hover {
            background: rgba(18, 150, 219, 0.91);
            color: #d5f1fc;
            border-color: #e7eaec;
            border-radius: 0;
        }

        .list-group-item:hover {
            cursor: pointer;
        }
    </style>
</head>


<body class="fixed-sidebar no-skin-config " style="height:800px;padding-bottom: 40px;background-color: #f3f3f4;">

<div id="wrapper">
    <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation"
             style="min-height: 30px;margin-top: 10px;margin-bottom: 0px;">
            <div class="form-group" id="data_5" style="margin-left: 20px;">
                <div class="input-daterange input-group" style="float: left;" id="datepicker">
                    <span class="input-group-addon">日期：</span>
                    <input type="text" class="input-sm form-control" style="width: 100px;" name="start"
                           value="05/14/2014">
                    <span class="input-group-addon">至</span>
                    <input type="text" class="input-sm form-control" style="width: 100px;" name="end"
                           value="05/22/2014">
                </div>
                <div class="input-group input-daterange">
                    <div style="margin-left: 20px;"></div>
                    <span class="input-group-addon">关键字：</span>
                    <input type="text" class="input-sm form-control" style="width: auto;" placeholder="请输入作业关键字">
                </div>
            </div>
        </nav>
    </div>
    <div class="fh-breadcrumb" style="height: 800px;">

        <div class="fh-column">
            <%-- 左侧分栏，显示当前日期范围内的所有作业列表 --%>
            <div class="full-height-scroll">
                <ul class="list-group elements-list">
                    <li class="list-group-item active" href="#tab-0" data-toggle="tab" style="display: none;"
                        id="default-tab">
                    </li>
                    <li class="list-group-item" href="#tab-1" data-toggle="tab">
                        <a data-toggle="tab" href="#tab-1">
                            <strong>数据清洗 - 文件转化传输</strong>
                            <div class="small m-t-xs">
                                <p>
                                    Survived not only five centuries, but also the leap scrambled it to make.
                                </p>
                                <p class="m-b-none">
                                    <span class="label pull-right label-primary">运行</span>
                                    <i class="fa fa-map-marker"></i> Riviera State 32/106
                                </p>
                            </div>
                        </a>
                    </li>
                    <li class="list-group-item" href="#tab-2" data-toggle="tab">
                        <a data-toggle="tab" href="#tab-2">
                            <strong>爬虫作业 - 入库</strong>
                            <div class="small m-t-xs">
                                <p class="m-b-xs">
                                    There are many variations of passages of Lorem Ipsum.
                                    <br/>
                                </p>
                                <p class="m-b-none">
                                    <span class="label pull-right label-primary">完成</span>
                                    <i class="fa fa-map-marker"></i> California 10F/32
                                </p>
                            </div>
                        </a>
                    </li>
                    <li class="list-group-item" href="#tab-2" data-toggle="tab">
                        <a data-toggle="tab" href="#tab-2">
                            <strong>页面清洗 - 上传</strong>
                            <div class="small m-t-xs">
                                <p class="m-b-xs">
                                    There are many variations of passages of Lorem Ipsum.
                                    <br/>
                                </p>
                                <p class="m-b-none">
                                    <span class="label pull-right label-primary">完成</span>
                                    <i class="fa fa-map-marker"></i> California 10F/32
                                </p>
                            </div>
                        </a>
                    </li>
                    <li class="list-group-item" href="#tab-2" data-toggle="tab">
                        <a data-toggle="tab" href="#tab-2">
                            <strong>应用上下线发布</strong>
                            <div class="small m-t-xs">
                                <p class="m-b-xs">
                                    There are many variations of passages of Lorem Ipsum.
                                    <br/>
                                </p>
                                <p class="m-b-none">

                                    <span class="label pull-right label-danger">异常</span>
                                    <i class="fa fa-map-marker"></i> California 10F/32
                                </p>
                            </div>
                        </a>
                    </li>
                    <li class="list-group-item" href="#tab-2" data-toggle="tab">
                        <a data-toggle="tab" href="#tab-2">
                            <strong>日志统计分析入库</strong>
                            <div class="small m-t-xs">
                                <p class="m-b-xs">
                                    There are many variations of passages of Lorem Ipsum.
                                    <br/>
                                </p>
                                <p class="m-b-none">
                                    <span class="label pull-right label-warning">警告</span>
                                    <i class="fa fa-map-marker"></i> California 10F/32
                                </p>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <div class="full-height">
            <%-- 右侧分栏，显示当前选定的作业对应的执行列表 --%>
            <div class="full-height-scroll white-bg border-left">
                <div class="element-detail-box">
                    <div class="tab-content">
                        <div id="tab-0" class="tab-pane" style="text-align: center;">
                            <div style="line-height: 450px;;font-size: 52px;color: #b7bec5;">Eureka Task List</div>
                        </div>
                        <div id="tab-1" class="tab-pane">
                            <div class="pull-right">
                                <div class="tooltip-demo">
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="left"
                                            title="更改作业配置（作业将会被停止）"><i class="fa fa-plug"></i> 配置
                                    </button>
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="top"
                                            title="查看作业视图"><i class="fa fa-eye"></i></button>
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="top"
                                            title="" data-original-title="删除作业"><i class="fa fa-trash-o"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="small text-muted">
                                <i class="fa fa-clock-o"></i> Friday, 12 April 2014, 12:32 am
                            </div>
                            <div class="col-lg-12">
                                <div class="ibox-content inspinia-timeline">
                                    <div class="row m-b-lg m-t-lg">
                                        <div class="col-md-6">
                                            <div class="profile-image">
                                            </div>
                                            <div class="profile-info">
                                                <div class="">
                                                    <div>
                                                        <h2 class="no-margins">
                                                            数据清洗-文件转化传输
                                                        </h2>
                                                        <h4>创建人</h4>
                                                        <small>
                                                            作业描述 blabla...blabla...blabla...
                                                        </small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <table class="table small m-b-xs">
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        节点总量 <strong>142</strong>
                                                    </td>
                                                    <td>
                                                        执行成功 <strong>22</strong>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        子作业量 <strong>61</strong>
                                                    </td>
                                                    <td>
                                                        执行失败 <strong>32</strong>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                    </td>
                                                    <td>
                                                        成功比率 <strong>79%</strong>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="col-md-3">
                                            <small>当前作业执行次数</small>
                                            <h2 class="no-margins">6,480</h2>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>
                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>

                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>
                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>

                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>
                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>

                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>
                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="timeline-item">
                                        <div class="row">
                                            <div class="col-xs-3 date">
                                                <i class="fa fa-briefcase"></i>
                                                6:00 am
                                                <br/>
                                                <small class="text-navy">2 hour ago</small>
                                            </div>
                                            <div class="col-xs-7 content no-top-border">
                                                <p class="m-b-xs"><strong>Meeting</strong></p>

                                                <p>Conference on the sales results for the previous year. Monica please
                                                    examine sales trends in marketing and products. Below please find
                                                    the current status of the
                                                    sale.</p>
                                            </div>
                                            <div class="col-xs-2 content no-top-border">
                                                <p class="m-b-xs"><strong>Option</strong></p>
                                                <div class="pull-right">
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-comments"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-user"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-list"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-pencil"></i></button>
                                                    <button type="button" class="btn btn-sm btn-white"><i
                                                            class="fa fa-print"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="tab-2" class="tab-pane">
                            <div class="pull-right">
                                <div class="tooltip-demo">
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="left"
                                            title="Plug this message"><i class="fa fa-plug"></i> Plug it
                                    </button>
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="top"
                                            title="Mark as read"><i class="fa fa-eye"></i></button>
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="top"
                                            title="" data-original-title="Mark as important"><i
                                            class="fa fa-exclamation"></i></button>
                                    <button class="btn btn-white btn-xs" data-toggle="tooltip" data-placement="top"
                                            title="" data-original-title="Move to trash"><i class="fa fa-trash-o"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="small text-muted">
                                <i class="fa fa-clock-o"></i> Monday, 21 May 2014, 10:32 am
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
</div>


<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>
<!-- Data picker -->
<script src="res/framework/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<script type="text/javascript">
    $(function () {
        $('#default-tab').trigger('click');
        $('#data_5 #datepicker').datepicker({
            keyboardNavigation: false,
            forceParse: false,
            autoclose: true
        });

    });
</script>
</body>

</html>
