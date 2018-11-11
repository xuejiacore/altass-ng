<%--suppress ALL --%>
<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2016/10/26
  Time: 8:40
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

    <title>Eureka Beta v0.0.1</title>
    <link href="/favicon.ico" rel="shortcut icon"
          type="image/vnd.microsoft.icon"/>
    <link href="res/framework/css/bootstrap.min.css" rel="stylesheet">
    <link href="res/framework/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Toastr style -->
    <link href="res/framework/css/plugins/toastr/toastr.min.css" rel="stylesheet">

    <!-- Gritter -->
    <link href="res/framework/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">

    <link href="res/framework/css/animate.css" rel="stylesheet">
    <link href="res/framework/css/style.css" rel="stylesheet">
</head>

<body class="fixed-nav skin-1">
<div id="wrapper">
    <nav class="navbar-default navbar-static-side" role="navigation" style="position: fixed;">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu"><%--菜单ID--%>
                <%--TODO：首块--%>
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <span>
                            <img alt="image" width="50px" class="img-circle" src="res/framework/img/developer_img.png"/>
                         </span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear">
                                <span class="block m-t-xs">
                                    <strong class="font-bold">Hacker Chim</strong>
                                </span>
                                <span class="text-muted text-xs block">Architect
                                    <b class="caret"></b>
                                </span>
                            </span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a href="profile.html">Profile</a></li>
                            <li><a href="contacts.html">Contacts</a></li>
                            <li><a href="mailbox.html">Mailbox</a></li>
                            <li class="divider"></li>
                            <li><a href="login.html">Logout</a></li>
                        </ul>
                    </div>
                    <%--TODO：产品简拼--%>
                    <div class="logo-element close-canvas-menu">
                        EUREKA
                    </div>
                </li>

                <%--菜单开始--%>
                <%----------------------------------------------------------------------------------------------------%>
                <li class="active">
                    <a href="/home/dashboard" target="mainFrame">
                        <i class="fa fa-dashboard"></i>
                        <span class="nav-label">Dashboard</span>
                        <span class="fa arrow"></span>
                    </a>
                </li>

                <li>
                    <a href="/nodePainter/painter">
                        <i class="fa fa-drupal"></i>
                        <span class="nav-label">作业视图配置</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li><a href="nodePainter/painter" target="mainFrame">作业视图</a></li>
                        <li><a href="/task/index?uid=chim" target="mainFrame">作业中心</a></li>
                    </ul>
                </li>

                <li>
                    <a href="index.html">
                        <i class="fa fa-git-square"></i>
                        <span class="nav-label">Dashboards</span>
                        <span class="fa arrow"></span>
                    </a>
                </li>

                <li>
                    <a href="javascript:void(0);">
                        <i class="fa fa-bug"></i>
                        <span class="nav-label">Dashboards</span>
                        <span class="fa arrow"></span>
                    </a>
                </li>

                <li>
                    <a href="javascript:void(0);">
                        <i class="fa fa-leaf"></i>
                        <span class="nav-label">Dashboards</span>
                        <span class="fa arrow"></span>
                    </a>
                </li>

                <li>
                    <a href="javascript:void(0);">
                        <i class="fa fa-trash"></i>
                        <span class="nav-label">Dashboards</span>
                        <span class="fa arrow"></span>
                    </a>
                </li>
                <%----------------------------------------------------------------------------------------------------%>
                <%--菜单结束--%>
            </ul>

        </div>
    </nav>

    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-fixed-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">

                    <img src="res/image/logo.png"
                         style="float: left;height: 48px;display: inline-table;padding: 0px;margin-left: 10px;margin-top: 7px;">

                    <a style="float: left;display: inline-table;font-family: 'open sans', 'Helvetica Neue', Helvetica, Arial,
                    sans-serif;font-size: 26px;margin-left: 10px;line-height: 55px;color: rgb(170, 172, 172);">Eureka</a>

                    <div style="position: relative;float: left;display: inline-table;height: 60px;width: 54px;">
                        <div style="float: left;display: inline-table;font-family: 'open sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;
                        color: rgb(170, 172, 172);font-size: 9px;position: absolute;bottom: 20px;right: 0px;">
                            Beta v0.0.1
                        </div>
                    </div>

                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="javascript:void(0);">
                        <i class="fa fa-bars"></i>
                    </a>
                    <form role="search" class="navbar-form-custom" action="search_results.html">
                        <div class="form-group">
                            <input type="text" placeholder="作业查询..." class="form-control"
                                   name="top-search" id="top-search">
                        </div>
                    </form>
                </div>
                <ul class="nav navbar-top-links navbar-right">

                    <%--TODO：邮件提醒--%>
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0);">
                            <i class="fa fa-envelope"></i> <span class="label label-warning">16</span>
                        </a>
                        <ul class="dropdown-menu dropdown-messages">
                            <li>
                                <div class="dropdown-messages-box">
                                    <a href="profile.html" class="pull-left">
                                        <img alt="image" class="img-circle" src="res/framework/img/a7.jpg">
                                    </a>
                                    <div class="media-body">
                                        <small class="pull-right">46h ago</small>
                                        <strong>Mike Loreipsum</strong> started following <strong>Monica Smith</strong>.
                                        <br>
                                        <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
                                    </div>
                                </div>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="dropdown-messages-box">
                                    <a href="profile.html" class="pull-left">
                                        <img alt="image" class="img-circle" src="res/framework/img/a4.jpg">
                                    </a>
                                    <div class="media-body ">
                                        <small class="pull-right text-navy">5h ago</small>
                                        <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica
                                        Smith</strong>. <br>
                                        <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
                                    </div>
                                </div>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="dropdown-messages-box">
                                    <a href="profile.html" class="pull-left">
                                        <img alt="image" class="img-circle" src="res/framework/img/profile.jpg">
                                    </a>
                                    <div class="media-body ">
                                        <small class="pull-right">23h ago</small>
                                        <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                        <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                    </div>
                                </div>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a href="mailbox.html">
                                        <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li>

                    <%--TODO：消息提醒--%>
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="javascript:void(0);">
                            <i class="fa fa-bell"></i> <span class="label label-primary">8</span>
                        </a>
                        <ul class="dropdown-menu dropdown-alerts">
                            <li>
                                <a href="mailbox.html">
                                    <div>
                                        <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                        <span class="pull-right text-muted small">4 minutes ago</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="profile.html">
                                    <div>
                                        <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                        <span class="pull-right text-muted small">12 minutes ago</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="grid_options.html">
                                    <div>
                                        <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                        <span class="pull-right text-muted small">4 minutes ago</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a href="notifications.html">
                                        <strong>See All Alerts</strong>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li>


                    <li>
                        <img alt="image" style="border-radius: 15px;width: 30px;" class="img-circle"
                             src="res/framework/img/developer_img.png"/>
                    </li>
                    <li>
                        <a href="login.html">
                            <i class="fa fa-sign-out"></i> 登出
                        </a>
                    </li>
                    <li>
                        <a class="right-sidebar-toggle">
                            <i class="fa fa-tasks"></i>
                        </a>
                    </li>
                </ul>

            </nav>
        </div>

        <%--TODO:主内容区域--%>
        <iframe class="main-frame" id="mainFrame" name="mainFrame" scrolling="no"
                src="/home/dashboard"
                frameborder="0" style="padding: 0px; width: 100%; height: 300px;"
                onload="startInit('mainFrame');"></iframe>

        <%--TODO:版权--%>
        <div class="footer">
            <div class="pull-right">
                10GB of <strong>250GB</strong> Free.
            </div>
            <div>
                <strong>Copyright</strong> Chim·Zigui Technology &copy; 2016-2017. All Rights Reserved.
            </div>
        </div>
    </div>
    <div class="small-chat-box fadeInRight animated">

        <div class="heading" draggable="true">
            <small class="chat-date pull-right">
                02.19.2015
            </small>
            Small chat
        </div>

        <div class="content">

            <div class="left">
                <div class="author-name">
                    Monica Jackson
                    <small class="chat-date">
                        10:02 am
                    </small>
                </div>
                <div class="chat-message active">
                    Lorem Ipsum is simply dummy text input.
                </div>

            </div>
            <div class="right">
                <div class="author-name">
                    Mick Smith
                    <small class="chat-date">
                        11:24 am
                    </small>
                </div>
                <div class="chat-message">
                    Lorem Ipsum is simpl.
                </div>
            </div>
            <div class="left">
                <div class="author-name">
                    Alice Novak
                    <small class="chat-date">
                        08:45 pm
                    </small>
                </div>
                <div class="chat-message active">
                    Check this stock char.
                </div>
            </div>
            <div class="right">
                <div class="author-name">
                    Anna Lamson
                    <small class="chat-date">
                        11:24 am
                    </small>
                </div>
                <div class="chat-message">
                    The standard chunk of Lorem Ipsum
                </div>
            </div>
            <div class="left">
                <div class="author-name">
                    Mick Lane
                    <small class="chat-date">
                        08:45 pm
                    </small>
                </div>
                <div class="chat-message active">
                    I belive that. Lorem Ipsum is simply dummy text.
                </div>
            </div>


        </div>
        <div class="form-chat">
            <div class="input-group input-group-sm">
                <input type="text" class="form-control">
                <span class="input-group-btn"> <button
                        class="btn btn-primary" type="button">Send
                </button> </span></div>
        </div>

    </div>
    <div id="small-chat">

        <span class="badge badge-warning pull-right">5</span>
        <a class="open-small-chat">
            <i class="fa fa-comments"></i>

        </a>
    </div>
    <div id="right-sidebar" class="animated">
        <div class="sidebar-container">

            <ul class="nav nav-tabs navs-3">

                <li class="active"><a data-toggle="tab" href="#tab-1">
                    Notes
                </a></li>
                <li><a data-toggle="tab" href="#tab-2">
                    Projects
                </a></li>
                <li class=""><a data-toggle="tab" href="#tab-3">
                    <i class="fa fa-gear"></i>
                </a></li>
            </ul>

            <div class="tab-content">


                <div id="tab-1" class="tab-pane active">

                    <div class="sidebar-title">
                        <h3><i class="fa fa-comments-o"></i> Latest Notes</h3>
                        <small><i class="fa fa-tim"></i> You have 10 new message.</small>
                    </div>

                    <div>

                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a1.jpg">

                                    <div class="m-t-xs">
                                        <i class="fa fa-star text-warning"></i>
                                        <i class="fa fa-star text-warning"></i>
                                    </div>
                                </div>
                                <div class="media-body">

                                    There are many variations of passages of Lorem Ipsum available.
                                    <br>
                                    <small class="text-muted">Today 4:21 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a2.jpg">
                                </div>
                                <div class="media-body">
                                    The point of using Lorem Ipsum is that it has a more-or-less normal.
                                    <br>
                                    <small class="text-muted">Yesterday 2:45 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a3.jpg">

                                    <div class="m-t-xs">
                                        <i class="fa fa-star text-warning"></i>
                                        <i class="fa fa-star text-warning"></i>
                                        <i class="fa fa-star text-warning"></i>
                                    </div>
                                </div>
                                <div class="media-body">
                                    Mevolved over the years, sometimes by accident, sometimes on purpose (injected
                                    humour and the like).
                                    <br>
                                    <small class="text-muted">Yesterday 1:10 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a4.jpg">
                                </div>

                                <div class="media-body">
                                    Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the
                                    <br>
                                    <small class="text-muted">Monday 8:37 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a8.jpg">
                                </div>
                                <div class="media-body">

                                    All the Lorem Ipsum generators on the Internet tend to repeat.
                                    <br>
                                    <small class="text-muted">Today 4:21 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a7.jpg">
                                </div>
                                <div class="media-body">
                                    Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes
                                    from a line in section 1.10.32.
                                    <br>
                                    <small class="text-muted">Yesterday 2:45 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a3.jpg">

                                    <div class="m-t-xs">
                                        <i class="fa fa-star text-warning"></i>
                                        <i class="fa fa-star text-warning"></i>
                                        <i class="fa fa-star text-warning"></i>
                                    </div>
                                </div>
                                <div class="media-body">
                                    The standard chunk of Lorem Ipsum used since the 1500s is reproduced below.
                                    <br>
                                    <small class="text-muted">Yesterday 1:10 pm</small>
                                </div>
                            </a>
                        </div>
                        <div class="sidebar-message">
                            <a href="javascript:void(0);">
                                <div class="pull-left text-center">
                                    <img alt="image" class="img-circle message-avatar" src="res/framework/img/a4.jpg">
                                </div>
                                <div class="media-body">
                                    Uncover many web sites still in their infancy. Various versions have.
                                    <br>
                                    <small class="text-muted">Monday 8:37 pm</small>
                                </div>
                            </a>
                        </div>
                    </div>

                </div>

                <div id="tab-2" class="tab-pane">

                    <div class="sidebar-title">
                        <h3><i class="fa fa-cube"></i> Latest projects</h3>
                        <small><i class="fa fa-tim"></i> You have 14 projects. 10 not completed.</small>
                    </div>

                    <ul class="sidebar-list">
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Business valuation</h4>
                                It is a long established fact that a reader will be distracted.

                                <div class="small">Completion with: 22%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 22%;" class="progress-bar progress-bar-warning"></div>
                                </div>
                                <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Contract with Company </h4>
                                Many desktop publishing packages and web page editors.

                                <div class="small">Completion with: 48%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 48%;" class="progress-bar"></div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Meeting</h4>
                                By the readable content of a page when looking at its layout.

                                <div class="small">Completion with: 14%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 14%;" class="progress-bar progress-bar-info"></div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <span class="label label-primary pull-right">NEW</span>
                                <h4>The generated</h4>
                                There are many variations of passages of Lorem Ipsum available.
                                <div class="small">Completion with: 22%</div>
                                <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Business valuation</h4>
                                It is a long established fact that a reader will be distracted.

                                <div class="small">Completion with: 22%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 22%;" class="progress-bar progress-bar-warning"></div>
                                </div>
                                <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Contract with Company </h4>
                                Many desktop publishing packages and web page editors.

                                <div class="small">Completion with: 48%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 48%;" class="progress-bar"></div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <div class="small pull-right m-t-xs">9 hours ago</div>
                                <h4>Meeting</h4>
                                By the readable content of a page when looking at its layout.

                                <div class="small">Completion with: 14%</div>
                                <div class="progress progress-mini">
                                    <div style="width: 14%;" class="progress-bar progress-bar-info"></div>
                                </div>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0);">
                                <span class="label label-primary pull-right">NEW</span>
                                <h4>The generated</h4>
                                <!--<div class="small pull-right m-t-xs">9 hours ago</div>-->
                                There are many variations of passages of Lorem Ipsum available.
                                <div class="small">Completion with: 22%</div>
                                <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                            </a>
                        </li>

                    </ul>

                </div>

                <div id="tab-3" class="tab-pane">

                    <div class="sidebar-title">
                        <h3><i class="fa fa-gears"></i> Settings</h3>
                        <small><i class="fa fa-tim"></i> You have 14 projects. 10 not completed.</small>
                    </div>

                    <div class="setings-item">
                    <span>
                        Show notifications
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example">
                                <label class="onoffswitch-label" for="example">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Disable Chat
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" name="collapsemenu" checked class="onoffswitch-checkbox"
                                       id="example2">
                                <label class="onoffswitch-label" for="example2">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Enable history
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example3">
                                <label class="onoffswitch-label" for="example3">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Show charts
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example4">
                                <label class="onoffswitch-label" for="example4">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Offline users
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" checked name="collapsemenu" class="onoffswitch-checkbox"
                                       id="example5">
                                <label class="onoffswitch-label" for="example5">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Global search
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" checked name="collapsemenu" class="onoffswitch-checkbox"
                                       id="example6">
                                <label class="onoffswitch-label" for="example6">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="setings-item">
                    <span>
                        Update everyday
                    </span>
                        <div class="switch">
                            <div class="onoffswitch">
                                <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example7">
                                <label class="onoffswitch-label" for="example7">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="sidebar-content">
                        <h4>Settings</h4>
                        <div class="small">
                            I belive that. Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                            And typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since
                            the 1500s.
                            Over the years, sometimes by accident, sometimes on purpose (injected humour and the like).
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
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
        setTimeout(function () {
            toastr.options = {
                closeButton: true,
                progressBar: true,
                showMethod: 'slideDown',
                timeOut: 1000
            };
//            toastr.success('Hello Chim!', 'Welcome');

        }, 1300);


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
                colors: ["#1ab394", "#1C84C6"],
                xaxis: {},
                yaxis: {
                    ticks: 4
                },
                tooltip: false
            }
        );
    });
</script>

<%--
    高度自适应
--%>
<script type="text/javascript">
    // iframe自适应高度
    var browserVersion = window.navigator.userAgent.toUpperCase();
    var isOpera = false;
    var isFireFox = false;
    var isChrome = false;
    var isSafari = false;
    var isIE = false;
    var iframeTime;

    function reinitIframe(iframeId, minHeight) {
        try {
            var iframe = document.getElementById(iframeId);
            var bHeight = 0;
            if (isChrome == false && isSafari == false)
                bHeight = iframe.contentWindow.document.body.scrollHeight;

            var dHeight = 0;
            if (isFireFox == true)
                dHeight = iframe.contentWindow.document.documentElement.offsetHeight;
            else if (isIE == false && isOpera == false)
                dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
            else
                bHeight += 3;
            var height = Math.max(bHeight, dHeight);
            if (height < minHeight) height = minHeight;
            iframe.style.height = height + "px";
        } catch (ex) {
        }
    }

    function startInit(iframeId) {
        var minHeight;
        // 获取窗口高度
        if (window.innerHeight)
            minHeight = window.innerHeight * 0.85;
        var iframe = document.getElementById(iframeId);
        iframe.style.height = "100px"
        setTimeout(function () {
            isOpera = browserVersion.indexOf("OPERA") > -1 ? true : false;
            isFireFox = browserVersion.indexOf("FIREFOX") > -1 ? true : false;
            isChrome = browserVersion.indexOf("CHROME") > -1 ? true : false;
            isSafari = browserVersion.indexOf("SAFARI") > -1 ? true : false;
            if (!!window.ActiveXObject || "ActiveXObject" in window)
                isIE = true;
            reinitIframe(iframeId, minHeight);
            /*            if (iframeTime != null) {
                            clearInterval(iframeTime);
                        }
                        else {*/
            if (document.getElementById(iframeId).style.height == '680px') {
            } else {
                iframeTime = window.setInterval("reinitIframe('" + iframeId + "'," + minHeight + ")", 100);
            }
//            }
        }, 300);
    }
</script>

<script type="text/javascript">
    $(function () {
        if (false) {
            var webSocket = null;

            //判断当前浏览器是否支持WebSocket
            if ('WebSocket' in window) {
                webSocket = new WebSocket("ws://localhost:8080/sktservice/notification");
            } else {
                alert('Not support webSocket')
            }

            //连接发生错误的回调方法
            webSocket.onerror = function () {
                setMessageInnerHTML("error");
            };

            //连接成功建立的回调方法
            webSocket.onopen = function (event) {
                setMessageInnerHTML("open");
            };

            //接收到消息的回调方法
            webSocket.onmessage = function (event) {
                setMessageInnerHTML(event.data);
            };

            //连接关闭的回调方法
            webSocket.onclose = function () {
                setMessageInnerHTML("close");
            };

            //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
            window.onbeforeunload = function () {
                webSocket.close();
            };

            //将消息显示在网页上
            function setMessageInnerHTML(innerHTML) {
//        setMessageInnerHTML(event.data);
                toastr.options = {
                    closeButton: true,
                    progressBar: true,
                    showMethod: 'slideDown',
                    timeOut: 4000
                };
                toastr.success('Responsive Admin Theme', innerHTML);
//        document.getElementById('message').innerHTML += innerHTML;
            }

            //关闭连接
            function closeWebSocket() {
                webSocket.close();
            }

            //发送消息
            function send() {
                var message = document.getElementById('text').value;
                webSocket.send(message);
            }
        }
    });
</script>
</body>
</html>
