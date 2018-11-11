<%--suppress ALL --%>
<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2016/10/26
  Time: 22:07
  To change this template use FileInfo | Settings | FileInfo Templates.
--%>
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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <base href="<%=basePath%>">
    <title>Dashboard</title>
    <%--加载流程主样式--%>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Toastr style -->
    <link href="res/css/override/toastr.min.css" rel="stylesheet">
    <!-- Sweet Alert -->
    <link href="res/framework/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

    <!-- Gritter -->
    <link href="res/framework/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">

    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">
    <link href="res/css/support/nodeflow/x-flow-style.css" rel="stylesheet">
    <%--Jquery ui--%>
    <link href="res/plugin/jquery-ui-1.12.1/jquery-ui.min.css" rel="stylesheet">
    <%--上下文菜单--%>
</head>
<body style="background: #f3f3f4;">
<div id="wrapper">
    <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="min-height: 30px;margin-bottom: 0">
        </nav>
    </div>
    <div class="row  border-bottom white-bg dashboard-header">

        <div class="col-sm-4">
            <h2>Welcome Eureka Dashboard</h2>
            <small>您当前有 12 条新作业通知.</small>
            <ul class="list-group clear-list m-t">
                <li class="list-group-item fist-item">
                    <span class="pull-right">09:00 pm</span>
                    <span class="label label-success">1</span> 数据清洗 - 文件转化传输
                </li>
                <li class="list-group-item">
                    <span class="pull-right">10:16 am</span>
                    <span class="label label-info">2</span> 爬虫作业 - 入库
                </li>
                <li class="list-group-item">
                    <span class="pull-right">08:22 pm</span>
                    <span class="label label-primary">3</span> 页面清洗 - 上传
                </li>
                <li class="list-group-item">
                    <span class="pull-right">11:06 pm</span>
                    <span class="label label-default">4</span> 图片清洗压缩上传
                </li>
                <li class="list-group-item">
                    <span class="pull-right">12:00 am</span>
                    <span class="label label-primary">5</span> 日志统计分析入库
                </li>
            </ul>
        </div>
        <div class="col-sm-5">
            <div class="flot-chart dashboard-chart">
                <div class="flot-chart-content" id="flot-dashboard-chart"></div>
            </div>
            <div class="row text-left">
                <div class="col-xs-4">
                    <div class=" m-l-md">
                        <span class="h4 font-bold m-t block"> 6,102</span>
                        <small class="text-muted m-b block">已执行作业总数</small>
                    </div>
                </div>
                <div class="col-xs-4">
                    <span class="h4 font-bold m-t block"> 5102/1000</span>
                    <small class="text-muted m-b block">成功/失败总数</small>
                </div>
                <div class="col-xs-4">
                    <span class="h4 font-bold m-t block"> 90%</span>
                    <small class="text-muted m-b block">成功率</small>
                </div>

            </div>
        </div>
        <div class="col-sm-3">
            <div class="statistic-box">
                <h4>
                    Job Health Dashboard
                </h4>
                <p>
                    当前作业运行健康情况
                </p>
                <div class="row text-center">
                    <div class="col-lg-6">
                        <canvas id="doughnutChart2" width="80" height="80" style="margin: 18px auto 0"></canvas>
                        <h5>运行状况</h5>
                    </div>
                    <div class="col-lg-6">
                        <canvas id="doughnutChart" width="80" height="80" style="margin: 18px auto 0"></canvas>
                        <h5>资源状况</h5>
                    </div>
                </div>
                <div class="m-t">
                    <small>最近10条作业中，有 2/10 失败，失败节点总数 32</small>
                </div>

            </div>
        </div>

    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="wrapper wrapper-content">
                <div class="row">
                    <div class="col-lg-4">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>最新执行作业</h5> <span class="label label-primary">2+</span>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                        <i class="fa fa-wrench"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user">
                                        <li><a href="#">Config option 1</a>
                                        </li>
                                        <li><a href="#">Config option 2</a>
                                        </li>
                                    </ul>
                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content">
                                <div>

                                    <div class="pull-right text-right">

                                        <span class="bar_dashboard">5,3,9,6,5,9,7,3,5,2,4,7,3,2,7,9,6,4,5,7,3,2,1,0,9,5,6,8,3,2,1</span>
                                        <br/>
                                        <small class="font-bold">$ 20 054.43</small>
                                    </div>
                                    <h4>应用上下线发布
                                        <br/>
                                        <small class="m-r"><a href="graph_flot.html"> 点击查看作业执行详情</a>
                                        </small>
                                    </h4>
                                </div>
                            </div>
                        </div>
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>工作台</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                        <i class="fa fa-wrench"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user">
                                        <li><a href="#">Config option 1</a>
                                        </li>
                                        <li><a href="#">Config option 2</a>
                                        </li>
                                    </ul>
                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content no-padding">
                                <ul class="list-group">
                                    <li class="list-group-item">
                                        <p><a class="text-info" href="#">@Alan Marry</a> I belive that. Lorem Ipsum is
                                            simply dummy text of the printing and typesetting industry.</p>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 1 minuts ago
                                        </small>
                                    </li>
                                    <li class="list-group-item">
                                        <p><a class="text-info" href="#">@Stock Man</a> Check this stock chart. This
                                            price is crazy! </p>
                                        <div class="text-center m">
                                            <span id="sparkline8"></span>
                                        </div>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 2 hours ago
                                        </small>
                                    </li>
                                    <li class="list-group-item">
                                        <p><a class="text-info" href="#">@Kevin Smith</a> Lorem ipsum unknown printer
                                            took a galley </p>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 2 minuts ago
                                        </small>
                                    </li>
                                    <li class="list-group-item ">
                                        <p><a class="text-info" href="#">@Jonathan Febrick</a> The standard chunk of
                                            Lorem Ipsum</p>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 1 hour ago</small>
                                    </li>
                                    <li class="list-group-item">
                                        <p><a class="text-info" href="#">@Alan Marry</a> I belive that. Lorem Ipsum is
                                            simply dummy text of the printing and typesetting industry.</p>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 1 minuts ago
                                        </small>
                                    </li>
                                    <li class="list-group-item">
                                        <p><a class="text-info" href="#">@Kevin Smith</a> Lorem ipsum unknown printer
                                            took a galley </p>
                                        <small class="block text-muted"><i class="fa fa-clock-o"></i> 2 minuts ago
                                        </small>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>您的作业列表</h5>
                                <div class="ibox-tools">
                                    <a href="/task/index?uid=chim">
                                        <span class="label label-warning-light pull-right">点击查看更多</span>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content">

                                <div>
                                    <div class="feed-activity-list">

                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/createFolder_sm.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">5m ago</small>
                                                <strong>Monica Smith</strong> posted a new blog. <br>
                                                <small class="text-muted">Today 5:60 pm - 12.06.2014</small>

                                            </div>
                                        </div>

                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/aggregation_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">2h ago</small>
                                                <strong>Mark Johnson</strong> posted message on <strong>Monica
                                                Smith</strong> site. <br>
                                                <small class="text-muted">Today 2:10 pm - 12.06.2014</small>
                                            </div>
                                        </div>
                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/delay_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">2h ago</small>
                                                <strong>Janet Rosowski</strong> add 1 photo on <strong>Monica
                                                Smith</strong>. <br>
                                                <small class="text-muted">2 days ago at 8:30am</small>
                                            </div>
                                        </div>
                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/job_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right text-navy">5h ago</small>
                                                <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica
                                                Smith</strong>. <br>
                                                <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
                                                <div class="actions">
                                                    <a class="btn btn-xs btn-white"><i class="fa fa-thumbs-up"></i> Like
                                                    </a>
                                                    <a class="btn btn-xs btn-white"><i class="fa fa-heart"></i> Love</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/aggregation_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">2h ago</small>
                                                <strong>Kim Smith</strong> posted message on <strong>Monica
                                                Smith</strong> site. <br>
                                                <small class="text-muted">Yesterday 5:20 pm - 12.06.2014</small>
                                                <div class="well">
                                                    Lorem Ipsum is simply dummy text of the printing and typesetting
                                                    industry. Lorem Ipsum has been the industry's standard dummy text
                                                    ever since the 1500s.
                                                    Over the years, sometimes by accident, sometimes on purpose
                                                    (injected humour and the like).
                                                </div>
                                                <div class="pull-right">
                                                    <a class="btn btn-xs btn-white"><i class="fa fa-thumbs-up"></i> Like
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/aggregation_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">23h ago</small>
                                                <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                                <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                            </div>
                                        </div>
                                        <div class="feed-element">
                                            <a href="profile.html" class="pull-left">
                                                <img alt="image" class="img-circle"
                                                     src="res/image/support/nodeflow/bgs/file_existed_bg.png">
                                            </a>
                                            <div class="media-body ">
                                                <small class="pull-right">46h ago</small>
                                                <strong>Mike Loreipsum</strong> started following <strong>Monica
                                                Smith</strong>. <br>
                                                <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
                                            </div>
                                        </div>
                                    </div>

                                    <button class="btn btn-primary btn-block m-t"><i class="fa fa-arrow-down"></i>
                                        查看更多
                                    </button>

                                </div>

                            </div>
                        </div>

                    </div>
                    <div class="col-lg-4">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>今日作业执行线</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                        <i class="fa fa-wrench"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user">
                                        <li><a href="#">Config option 1</a>
                                        </li>
                                        <li><a href="#">Config option 2</a>
                                        </li>
                                    </ul>
                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content ibox-heading">
                                <h3>您今日有 73 个节点已完成</h3>
                                <small><i class="fa fa-map-marker"></i> 最后检查时间为 01:55:03</small>
                                <small class="m-r"><a href="graph_flot.html"> 点击查看更多.</a></small>
                            </div>
                            <div class="ibox-content inspinia-timeline">

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
                                                examine sales trends in marketing and products. Below please find the
                                                current status of the
                                                sale.</p>

                                            <p><span data-diameter="40" class="updating-chart">5,3,9,6,5,9,7,3,5,2,5,3,9,6,5,9,4,7,3,2,9,8,7,4,5,1,2,9,5,4,7,2,7,7,3,5,2</span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="timeline-item">
                                    <div class="row">
                                        <div class="col-xs-3 date">
                                            <i class="fa fa-file-text"></i>
                                            7:00 am
                                            <br/>
                                            <small class="text-navy">3 hour ago</small>
                                        </div>
                                        <div class="col-xs-7 content">
                                            <p class="m-b-xs"><strong>Send documents to Mike</strong></p>
                                            <p>Lorem Ipsum is simply dummy text of the printing and typesetting
                                                industry. Lorem Ipsum has been the industry's standard dummy text ever
                                                since.</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="timeline-item">
                                    <div class="row">
                                        <div class="col-xs-3 date">
                                            <i class="fa fa-coffee"></i>
                                            8:00 am
                                            <br/>
                                        </div>
                                        <div class="col-xs-7 content">
                                            <p class="m-b-xs"><strong>Coffee Break</strong></p>
                                            <p>
                                                Go to shop and find some products.
                                                Lorem Ipsum is simply dummy text of the printing and typesetting
                                                industry. Lorem Ipsum has been the industry's.
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="timeline-item">
                                    <div class="row">
                                        <div class="col-xs-3 date">
                                            <i class="fa fa-phone"></i>
                                            11:00 am
                                            <br/>
                                            <small class="text-navy">21 hour ago</small>
                                        </div>
                                        <div class="col-xs-7 content">
                                            <p class="m-b-xs"><strong>Phone with Jeronimo</strong></p>
                                            <p>
                                                Lorem Ipsum has been the industry's standard dummy text ever since.
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="timeline-item">
                                    <div class="row">
                                        <div class="col-xs-3 date">
                                            <i class="fa fa-user-md"></i>
                                            09:00 pm
                                            <br/>
                                            <small>21 hour ago</small>
                                        </div>
                                        <div class="col-xs-7 content">
                                            <p class="m-b-xs"><strong>Go to the doctor dr Smith</strong></p>
                                            <p>
                                                Find some issue and go to doctor.
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="timeline-item">
                                    <div class="row">
                                        <div class="col-xs-3 date">
                                            <i class="fa fa-comments"></i>
                                            12:50 pm
                                            <br/>
                                            <small class="text-navy">48 hour ago</small>
                                        </div>
                                        <div class="col-xs-7 content">
                                            <p class="m-b-xs"><strong>Chat with Monica and Sandra</strong></p>
                                            <p>
                                                Web sites still in their infancy. Various versions have evolved over the
                                                years, sometimes by accident, sometimes on purpose (injected humour and
                                                the like).
                                            </p>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
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
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Flot -->
<script src="res/framework/js/plugins/flot/jquery.flot.js"></script>
<script src="res/framework/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="res/framework/js/plugins/flot/jquery.flot.spline.js"></script>
<script src="res/framework/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="res/framework/js/plugins/flot/jquery.flot.pie.js"></script>

<!-- Peity -->
<script src="res/framework/js/plugins/peity/jquery.peity.min.js"></script>
<script src="res/framework/js/demo/peity-demo.js"></script>

<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>

<!-- jQuery UI -->
<script src="res/framework/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- GITTER -->
<script src="res/framework/js/plugins/gritter/jquery.gritter.min.js"></script>

<!-- Sparkline -->
<script src="res/framework/js/plugins/sparkline/jquery.sparkline.min.js"></script>

<!-- Sparkline demo data  -->
<script src="res/framework/js/demo/sparkline-demo.js"></script>

<!-- ChartJS-->
<script src="res/framework/js/plugins/chartJs/Chart.min.js"></script>

<!-- Toastr -->
<script src="res/framework/js/plugins/toastr/toastr.min.js"></script>
<script>
    $(document).ready(function () {

        var data1 = [
            [0, 4], [1, 8], [2, 5], [3, 10], [4, 4], [5, 16], [6, 5], [7, 11], [8, 6], [9, 11], [10, 30], [11, 10], [12, 13], [13, 4], [14, 3], [15, 3], [16, 6]
        ];
        var data2 = [
            [0, 1], [1, 0], [2, 2], [3, 0], [4, 1], [5, 3], [6, 1], [7, 5], [8, 2], [9, 3], [10, 2], [11, 1], [12, 0], [13, 2], [14, 8], [15, 0], [16, 0]
        ];
        $("#flot-dashboard-chart").length && $.plot($("#flot-dashboard-chart"), [
                data1, data2
            ],
            {
                series: {
                    lines: {
                        show: false,
                        fill: true
                    },
                    splines: {
                        show: true,
                        tension: 0.4,
                        lineWidth: 1,
                        fill: 0.4
                    },
                    points: {
                        radius: 0,
                        show: true
                    },
                    shadowSize: 2
                },
                grid: {
                    hoverable: true,
                    clickable: true,
                    tickColor: "#d5d5d5",
                    borderWidth: 1,
                    color: '#d5d5d5'
                },
                colors: ["#1ab394", "#c64a00"],
                xaxis: {},
                yaxis: {
                    ticks: 4
                },
                tooltip: false
            }
        );

        var doughnutData = {
            labels: ["Thread", "CPU", "Mem"],
            datasets: [{
                data: [300, 50, 100],
                backgroundColor: ["#a3e1d4", "#dedede", "#9CC3DA"]
            }]
        };


        var doughnutOptions = {
            responsive: false,
            legend: {
                display: false
            }
        };


        var ctx4 = document.getElementById("doughnutChart").getContext("2d");
        new Chart(ctx4, {type: 'doughnut', data: doughnutData, options: doughnutOptions});

        var doughnutData = {
            labels: ["执行中", "失败", "成功"],
            datasets: [{
                data: [70, 27, 85],
                backgroundColor: ["#a3e1d4", "#dedede", "#9CC3DA"]
            }]
        };


        var doughnutOptions = {
            responsive: false,
            legend: {
                display: false
            }
        };


        var ctx4 = document.getElementById("doughnutChart2").getContext("2d");
        new Chart(ctx4, {type: 'doughnut', data: doughnutData, options: doughnutOptions});

    });
</script>
</html>
