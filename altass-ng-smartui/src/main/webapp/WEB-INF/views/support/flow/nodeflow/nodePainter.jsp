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
    <title>节点绘制主面板</title>
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
    <link rel="stylesheet" href="res/plugin/jsPlumb/css/jsPlumbToolkit-defaults.css">
    <link rel="stylesheet" href="res/plugin/jsPlumb/css/main.css">
    <%--上下文菜单--%>
    <link rel="stylesheet" href="res/plugin/smart-menu/smartMenu.css">

    <link rel="stylesheet" href="res/plugin/jsPlumb/css/jsPlumbToolkit-demo.css">
    <link rel="stylesheet" href="res/css/support/nodeflow/demo.css">

    <style type="text/css">
        /*工具箱的表项*/
        .tool-item {
            padding-bottom: 5px;
            padding-top: 5px;
            padding-left: 5px;
            margin-top: auto;
        }

        /*工具箱可拖拽对象*/
        .draggable-tool-item {
            width: 150px;
            height: 150px;
            padding: 0.5em;
        }

        .draggable-node-dragging {
            max-width: 40px;
            max-height: 40px;
        }

        .node-shadow {
            box-shadow: 0px 0px 7px 1px;
            color: #959595;
        }

        .node-gray-border {
            border: 0.04em solid #0099cc;
        }

        .menu-item-first {
            padding: 2px 2px 2px 10px;
            min-height: 20px;
            border-color: #0e9aef;
        }

        .menu-item {
            padding: 2px 2px 2px 10px;
            min-height: 20px;
            border-top-width: 1px;
        }

        .draggable-node-item {
            font-size: 12px;
            text-align: center;
        }

        .node-desc {
            font-size: 10px;
            margin-top: 48px;
            cursor: move;
            border: 1px dashed #9e9b9b;
        }

        .job-highlight {
            box-shadow: 0 0 20px 2px #03A9F4;
        }

        /*高亮框选对象*/
        .selector-highlight {
            box-shadow: 0 0 20px 2px #E91E63;
        }

        .node-list {
            margin-top: 36px;
            box-shadow: 0px 1px 20px;
            z-index: 10;
            width: 42px;
            height: 560px;
            padding: 5px;
        }

        .node-small-list li {
            margin-bottom: 2px;
        }

        .node-small-icon {
            width: 32px;
            height: 32px;
            border-radius: 2px;
            border: 1px solid #e7eaec;
            padding: 2px;
            margin-bottom: 2px;
        }

        .node-small-icon:hover {
            box-shadow: 0 1px 4px #5e5e5e;
            transition: box-shadow 0.25s ease-in;
            cursor: move;
        }

        .circle, .ring {
            height: 180px;
            position: relative;
            width: 180px;
        }

        .circle {
            position: fixed;
            top: 100px;
            left: 380px;
            z-index: -1;
        }

        .ring {
            border-radius: 50%;
            opacity: 0;
            -webkit-transform-origin: 50% 50%;
            -moz-transform-origin: 50% 50%;
            transform-origin: 50% 50%;
            -webkit-transform: scale(0.1) rotate(-270deg);
            -moz-transform: scale(0.1) rotate(-270deg);
            -transform: scale(0.1) rotate(-270deg);
            -webkit-transition: all 0.4s ease-out;
            -moz-transition: all 0.4s ease-out;
            transition: all 0.4s ease-out;
        }

        .open .ring {
            opacity: 1;
            -webkit-transform: scale(1) rotate(0deg);
            -moz-transform: scale(1) rotate(0deg);
            -transform: scale(1) rotate(0deg);
            border: 1px dashed #0099cc;
            box-shadow: 0px 0px 10px 0px #5e5e5e;
        }

        .center {
            background-color: rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border: 1px dashed #ffffff;
            bottom: 0;
            color: white;
            height: 45px;
            left: 0;
            line-height: 80px;
            margin: auto;
            position: absolute;
            right: 0;
            text-align: center;
            top: 0;
            width: 45px;
            -webkit-transition: all 0.4s ease-out;
            -moz-transition: all 0.4s ease-out;
            transition: all 0.4s ease-out;
        }

        .open .center {
            border-color: #0099cc;
        }

        .menuItem {
            border-radius: 50%;
            color: #eeeeee;
            display: block;
            height: 40px;
            line-height: 40px;
            margin-left: -20px;
            margin-top: -20px;
            position: absolute;
            text-align: center;
            width: 40px;
        }

        .collection-item {
            border: 1px dashed #0099cc;
            border-radius: 50%;
        }

        .node-detail-panel {
            position: fixed;
            top: 300px;
            left: 330px;
            z-index: 999;
            background: rgba(255, 255, 255, 0.64);
            padding: 15px;
            border: 1px solid #0099cc;
            border-radius: 3px;
            box-shadow: 0px 0px 6px 0px #3895d4b3;
            font-size: 9px;
        }

        .debug-panel {
            width: 320px;
            height: 80px;
            position: fixed;
            left: 10px;
            bottom: 40px;
            z-index: 999;
            background: rgba(255, 255, 255, 0.09);
            padding: 15px;
            border: 1px solid rgba(128, 128, 128, 0.45);
            border-radius: 1px;
            font-size: 6px;
        }

        .node-detail-panel tr td:first-child {
            font-weight: bold;
            border-bottom: 1px dashed #d1d1d1;
            line-height: 15px;
            text-align: right;
        }

        .node-detail-panel tr {
        }

        .close-symbol {
            margin-top: -10px;
            margin-right: -10px;
            float: right;
            padding-left: 10px;
            padding-right: 5px;
        }

        .close-symbol:hover {
            cursor: pointer;
            color: red;
        }

        .text-muted {
            float: left;
        }

        @media (min-width: 100px) {
            .col-lg-3 {
                width: 25%;
            }

            .feed-activity-list {
                height: 380px;
                overflow-y: auto;
            }
        }

        @media (min-width: 1500px) {
            .col-lg-3 {
                width: 16%;
            }

            .feed-activity-list {
                height: 500px;
                overflow-y: auto;
            }
        }
    </style>

    <%-- 旋转特效 --%>
    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
            font: 12px normal Verdana, Arial, Helvetica, sans-serif;
            height: 100%;
        }

        * {
            margin: 0;
            padding: 0;
            outline: none;
        }

        img {
            border: none;
        }

        a {
            text-decoration: none;
            color: #00c6ff;
        }

        h1 {
            font: 4em normal Arial, Helvetica, sans-serif;
            padding: 20px;
            margin: 0;
            text-align: center;
            color: #bbb;
        }

        h1 small {
            font: 0.2em normal Arial, Helvetica, sans-serif;
            text-transform: uppercase;
            letter-spacing: 0.2em;
            line-height: 5em;
            display: block;
        }

        .container {
            width: 960px;
            margin: 0 auto;
            overflow: hidden;
        }

        .content {
            width: 800px;
            margin: 0 auto;
            padding-top: 50px;
        }

        .contentBar {
            width: 90px;
            margin: 0 auto;
            padding-top: 50px;
            padding-bottom: 50px;
        }

        /* STOP ANIMATION */

        .stop {
            -webkit-animation-play-state: paused;
            -moz-animation-play-state: paused;
        }

        /* Loading Circle */
        .ball {
            background-color: rgba(0, 0, 0, 0);
            border: 5px solid rgba(0, 183, 229, 0.9);
            opacity: .9;
            border-top: 5px solid rgba(0, 0, 0, 0);
            border-left: 5px solid rgba(0, 0, 0, 0);
            border-radius: 50px;
            box-shadow: 0 0 35px #2187e7;
            width: 50px;
            height: 50px;
            margin: 0 auto;
            -moz-animation: spin .5s infinite linear;
            -webkit-animation: spin .5s infinite linear;
        }

        .ball1 {
            background-color: rgba(0, 0, 0, 0);
            border: 5px solid rgba(0, 183, 229, 0.9);
            opacity: .9;
            border-top: 5px solid rgba(0, 0, 0, 0);
            border-left: 5px solid rgba(0, 0, 0, 0);
            border-radius: 50px;
            box-shadow: 0 0 15px #2187e7;
            width: 30px;
            height: 30px;
            margin: 0 auto;
            position: relative;
            top: -50px;
            -moz-animation: spinoff .5s infinite linear;
            -webkit-animation: spinoff .5s infinite linear;
        }

        @-moz-keyframes spin {
            0% {
                -moz-transform: rotate(0deg);
            }
            100% {
                -moz-transform: rotate(360deg);
            }
        }

        @-moz-keyframes spinoff {
            0% {
                -moz-transform: rotate(0deg);
            }
            100% {
                -moz-transform: rotate(-360deg);
            }
        }

        @-webkit-keyframes spin {
            0% {
                -webkit-transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
            }
        }

        @-webkit-keyframes spinoff {
            0% {
                -webkit-transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(-360deg);
            }
        }

        /* 状态标记 */
        .status-indication {
            position: absolute;
            background-color: rgba(0, 0, 0, 0);
            border: 5px solid red;
            opacity: .9;
            border-right: 5px solid rgba(0, 0, 0, 0);
            border-left: 5px solid rgba(0, 0, 0, 0);
            border-radius: 50px;
            box-shadow: 0 0 35px red;
            width: 50px;
            height: 50px;
            margin-left: -5px;
            margin-top: -5px;
            -moz-animation: spinPulse 2s infinite ease-in-out;
            -webkit-animation: spinPulse 2s infinite linear;
        }

        .circle1 {
            background-color: rgba(0, 0, 0, 0);
            border: 5px solid rgba(0, 183, 229, 0.9);
            opacity: .9;
            border-left: 5px solid rgba(0, 0, 0, 0);
            border-right: 5px solid rgba(0, 0, 0, 0);
            border-radius: 50px;
            box-shadow: 0 0 15px #2187e7;
            width: 30px;
            height: 30px;
            margin: 0 auto;
            position: relative;
            top: -50px;
            -moz-animation: spinoffPulse 1s infinite linear;
            -webkit-animation: spinoffPulse 1s infinite linear;
        }

        @-moz-keyframes spinPulse {
            0% {
                -moz-transform: rotate(160deg);
                opacity: 0;
                box-shadow: 0 0 1px #2187e7;
            }
            50% {
                -moz-transform: rotate(145deg);
                opacity: 1;
            }
            100% {
                -moz-transform: rotate(-320deg);
                opacity: 0;
            }
        }

        @-moz-keyframes spinoffPulse {
            0% {
                -moz-transform: rotate(0deg);
            }
            100% {
                -moz-transform: rotate(360deg);
            }
        }

        @-webkit-keyframes spinPulse {
            0% {
                -webkit-transform: rotate(160deg);
                opacity: 0;
                box-shadow: 0 0 1px #2187e7;
            }
            50% {
                -webkit-transform: rotate(145deg);
                opacity: 1;
            }
            100% {
                -webkit-transform: rotate(-320deg);
                opacity: 0;
            }
        }

        @-webkit-keyframes spinoffPulse {
            0% {
                -webkit-transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
            }
        }

        /* LITTLE BAR */

        .barlittle {
            background-color: #2187e7;
            background-image: -moz-linear-gradient(45deg, #2187e7 25%, #a0eaff);
            background-image: -webkit-linear-gradient(45deg, #2187e7 25%, #a0eaff);
            border-left: 1px solid #111;
            border-top: 1px solid #111;
            border-right: 1px solid #333;
            border-bottom: 1px solid #333;
            width: 10px;
            height: 10px;
            float: left;
            margin-left: 5px;
            opacity: 0.1;
            -moz-transform: scale(0.7);
            -webkit-transform: scale(0.7);
            -moz-animation: move 1s infinite linear;
            -webkit-animation: move 1s infinite linear;
        }

        #block_1 {
            -moz-animation-delay: .4s;
            -webkit-animation-delay: .4s;
        }

        #block_2 {
            -moz-animation-delay: .3s;
            -webkit-animation-delay: .3s;
        }

        #block_3 {
            -moz-animation-delay: .2s;
            -webkit-animation-delay: .2s;
        }

        #block_4 {
            -moz-animation-delay: .3s;
            -webkit-animation-delay: .3s;
        }

        #block_5 {
            -moz-animation-delay: .4s;
            -webkit-animation-delay: .4s;
        }

        @-moz-keyframes move {
            0% {
                -moz-transform: scale(1.2);
                opacity: 1;
            }
            100% {
                -moz-transform: scale(0.7);
                opacity: 0.1;
            }
        }

        @-webkit-keyframes move {
            0% {
                -webkit-transform: scale(1.2);
                opacity: 1;
            }
            100% {
                -webkit-transform: scale(0.7);
                opacity: 0.1;
            }
        }

        /* Trigger button for javascript */

        .trigger, .triggerFull, .triggerBar {
            background: #000000;
            background: -moz-linear-gradient(top, #161616 0%, #000000 100%);
            background: -webkit-linear-gradient(top, #161616 0%, #000000 100%);
            border-left: 1px solid #111;
            border-top: 1px solid #111;
            border-right: 1px solid #333;
            border-bottom: 1px solid #333;
            font-family: Verdana, Geneva, sans-serif;
            font-size: 0.8em;
            text-decoration: none;
            text-transform: lowercase;
            text-align: center;
            color: #fff;
            padding: 10px;
            border-radius: 3px;
            display: block;
            margin: 0 auto;
            width: 140px;
        }

        .trigger:hover, .triggerFull:hover, .triggerBar:hover {
            background: -moz-linear-gradient(top, #202020 0%, #161616 100%);
            background: -webkit-linear-gradient(top, #202020 0%, #161616 100%);
        }
    </style>
</head>
<body>
<div id="nodePainter" class="nodePainter">
    <%-- 主工具栏定义 开始 --%>
    <div class="plumb-toolbar-area">
        <ul class="tag-list" style="padding: 0">
            <li>
                <a id="toggle-toolbox" href="javascript:void(0);">
                    <i class="fa fa-suitcase" style="color: #0e9aef;"></i> 工具箱
                </a>
            </li>
            <li>
                <a id="collection-toolbox" href="javascript:void(0);" onclick="location.reload();">
                    <i class="fa fa-refresh" style="color: #0e9aef"></i> 重建作业
                </a>
            </li>

            <li style="float: right;">
                <a id="job-running-testing" href="javascript:void(0);"
                   onclick="RequestManager.postTestingRunning();">
                    <i class="fa fa-bug" style="color: red;"></i> 测试并监控作业
                </a>
            </li>
            <li style="float: right;">
                <a id="toggle-toolbox" href="javascript:void(0);" onclick="$('#debug-panel').show();">
                    <i class="fa fa-heart" style="color: #0e9aef;"></i> 状态
                </a>
            </li>
            <li style="float: right;">
                <a id="toggle-toolbox" href="javascript:void(0);"
                   onclick="$('#load-job-data-file').trigger('click');">
                    <i class="fa fa-folder-open" style="color: orange;"></i> 加载作业
                </a>
            </li>
            <li>
                <a id="testJobId" href="javascript:void(0);"></a>
            </li>
            <li>
                <a id="collection-toolbox" href="javascript:void(0);" onclick="RequestManager.onJdfSave();">
                    <i class="fa fa-save" style="color: #0e9aef"></i> 另存为
                </a>
            </li>
            <li>
                <a id="collection-toolbox" href="javascript:void(0);" target="_blank"
                   onclick="RequestManager.viewJdfContent();">
                    <i class="fa fa-code" style="color: #0e9aef"></i> JDF
                </a>
            </li>
            <li>
                <input id="nodeSearch" type="text" class="form-control"
                       style="font-size: 10px;height: 23px;margin-top: 5px;"
                       placeholder="节点查询"/>
            </li>
        </ul>
        <form enctype="multipart/form-data" method="post" id="fileInfo" name="fileInfo">
            <%-- 加载作业文件到当前内存中 --%>
            <input id="load-job-data-file" type="file" style="display: none;"
                   onchange="RequestManager.uploadJobDataFile();">
        </form>
    </div>
    <%-- 主工具栏定义 结束 --%>

    <%--<div class="plumb-toolbar-area node-list">--%>
    <%--<ul class="tag-list node-small-list" id="node-tool-box" style="padding: 0"></ul>--%>
    <%--</div>--%>


    <div class="circle" id="collectionCircle">
        <div class="ring">
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='开始节点'>
                    <img alt='image' title='开始节点' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/bgs/node-start.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='结束节点'>
                    <img alt='image' title='结束节点' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/bgs/node-end.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='{nodeName}'>
                    <img alt='image' title='{nodeName}' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/bgs/start_bg.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='{nodeName}'>
                    <img alt='image' title='{nodeName}' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/bgs/end_bg.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='{nodeName}'>
                    <img alt='image' title='{nodeName}' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/ping_bg.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='{nodeName}'>
                    <img alt='image' title='{nodeName}' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/databaseIntoDatabase_bg.png'>
                </div>
            </a>
            <a href="#" class="menuItem">
                <div class='draggable-node-item' title='{nodeName}'>
                    <img alt='image' title='{nodeName}' class='node-small-icon collection-item' data-config-url="-"
                         src='res/image/support/nodeflow/bgs/node-start.png'>
                </div>
            </a>
            <a href="javascript:void(0);" class="center"></a>
        </div>
    </div>
    <script type="text/javascript">
        var items = document.querySelectorAll('.menuItem');
        for (var i = 0, l = items.length; i < l; i++) {
            items[i].style.left = (50 - 28 * Math.cos(-0.5 * Math.PI - 2 * (1 / l) * i * Math.PI)).toFixed(4) + "%";
            items[i].style.top = (50 + 28 * Math.sin(-0.5 * Math.PI - 2 * (1 / l) * i * Math.PI)).toFixed(4) + "%";
        }
    </script>


    <div class="canvas-container" id="canvas">
        <%-- 选定矩形框 --%>
        <svg id="rectSelector" width="auto" height="auto" version="1.1" xmlns="http://www.w3.org/2000/svg"
             style="left:0; top:0; position: absolute;z-index: 99;display: none;">
            <rect x="0" y="0" width="250" height="100"
                  style="fill-opacity: 0.3;fill:#0099cc;stroke:#0099cc;stroke-width:1;" ry="2"
                  rx="2"></rect>
        </svg>
        <div class="debug-panel" id="debug-panel">
            <div class="close-symbol" onclick="$('#debug-panel').hide();">&times;</div>
            <ul>
                <li>坐标：(
                    <pos id="mouse_DebugX">x</pos>
                    ,
                    <pos id="mouse_DebugY">y</pos>
                    )
                </li>
                <li>节点：(
                    <pos id="node_DebugX">x</pos>
                    ,
                    <pos id="node_DebugY">y</pos>
                    )
                </li>
                <li>nodeId：
                    <nodeId id="nodeId_Debug"></nodeId>
                </li>
            </ul>
        </div>
        <div class="node-detail-panel" title="节点运行详情" style="display: none;">
            <div>
                <table>
                    <tr>
                        <td>状态：</td>
                        <td>
                            <div style="width: 10px;height: 10px;background: green;border-radius: 50%;"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>开始时间：</td>
                        <td>2016-12-26 00:03:13</td>
                    </tr>
                    <tr>
                        <td>运行时间：</td>
                        <td>2016-12-26 00:03:13</td>
                    </tr>
                    <tr>
                        <td>节点总数：</td>
                        <td>12</td>
                    </tr>
                </table>
            </div>
        </div>

        <%-- 绘图主面板 开始 --%>
        <div class="canvas-area" id="canvas-area"></div>
        <%-- 绘图主面板 结束 --%>

        <%--  工具箱开始  --%>
        <div class="tool-list" id="tool-list">
            <div class="tool tool-box" id="tool-box" style="display: block;">
                <div class="row">
                    <div class="col-lg-3 toolbox-panel">
                        <div class="ibox float-e-margins toolbox-shadow" style="margin-bottom: 5px;">
                            <div class="ibox-title" style="border-color: #0e9aef;">
                                <h5>
                                    <i class="fa fa-cogs" style="color:gray;"></i> Toolkits
                                </h5>

                                <%--工具表头--%>
                                <div class="ibox-tools">
                                    <a class="collapse-min-max">
                                        <i class="fa fa-compress"></i>
                                    </a>

                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                        <i class="fa fa-wrench"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user">
                                        <li><a href="#">工具箱配置1</a>
                                        </li>
                                        <li><a href="#">工具箱配置2</a>
                                        </li>
                                    </ul>
                                    <%--调整工具箱的位置按钮--%>
                                    <a class="toggle-toolbox-panel-position">
                                        <i class="fa fa-angle-double-right"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content box-context-scroll">
                                <%--TODO：工具箱列表--%>
                                <div class="feed-activity-list"
                                     id="node-tool-box">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--  工具箱结束  --%>
    </div>

</div>

<%-- 对话框开始 --%>
<%--
    节点配置信息
--%>
<div class="modal inmodal" id="nodeConfigureModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="padding-top: 50px; width: 900px;">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="padding-bottom: 10px;">
                <button type="button" class="close" data-dismiss="modal"
                        onclick="window.frames['configFrame'].contentWindow.onCancel(jobId, operatingNodeID);$('#nodeConfigureModal').modal('hide');">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>

                <h6 class="modal-title" style="font-size: 18px;" id="node-config-modal-title">节点属性配置</h6>
                <small class="font-bold" id="node-config-modal-desc">节点配置页面（嵌套）</small>
            </div>
            <div class="modal-body" style="padding: 10px 30px 10px 30px;">
                <iframe id="configFrame" width="850" height="300" frameborder="0"></iframe>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white"
                        onclick="window.frames['configFrame'].contentWindow.onCancel(jobId, operatingNodeID);$('#nodeConfigureModal').modal('hide');">
                    关闭
                </button>
                <button type="button" class="btn btn-primary"
                        onclick="RequestManager.postSubmitConfigNode();">
                    保存
                </button>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal" id="confirmDialog" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-sm" style="padding-top: 150px;">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="padding: 10px 15px;">
                <button type="button" class="close" data-dismiss="modal" onclick="DialogManager.doCancel();"><span
                        aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <%--<h6 class="modal-title" style="font-size: 18px;">操作提示</h6>--%>
                <small class="font-bold">操作提示</small>
            </div>
            <div class="modal-body" style="text-align: center;">
                <p id="confirmDialogMsg">确认删除节点? (将删除关联的连线)</p>
            </div>
            <div class="modal-footer" style="padding: 5px;">
                <button type="button" class="btn btn-white" onclick="DialogManager.doCancel();">取消</button>
                <button type="button" class="btn btn-primary" onclick="DialogManager.doConfirm();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal" id="inputDialog" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-sm" style="padding-top: 150px;">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="padding: 10px 15px;">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#inputDialog').hide();"><span
                        aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <small class="font-bold">操作提示</small>
            </div>
            <div class="modal-body" style="text-align: center;">
                <input placeholder="请输入节点的名称" class="form-control" type="text">
            </div>
            <div class="modal-footer" style="padding: 5px;">
                <button type="button" class="btn btn-primary" onclick="$('#inputDialog').hide();">确认</button>
            </div>
        </div>
    </div>
</div>

<%-- 对话框结束 --%>


</body>
<!-- Mainly scripts -->
<script src="res/framework/js/jquery-2.1.1.js"></script>
<script src="res/framework/js/bootstrap.min.js"></script>
<script src="res/framework/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="res/framework/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="res/framework/js/inspinia.js"></script>
<script src="res/framework/js/plugins/pace/pace.min.js"></script>

<!-- jQuery UI -->
<script src="res/plugin/jquery-ui-1.12.1/jquery-ui.min.js"></script>

<!-- GITTER -->
<script src="res/framework/js/plugins/gritter/jquery.gritter.min.js"></script>
<!-- Toastr script -->
<script src="res/framework/js/plugins/toastr/toastr.min.js"></script>
<!-- Sweet alert -->
<script src="res/framework/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="res/scripts/common/x-common.js"></script>


<!-- JS -->
<!-- support lib for bezier stuff -->
<%--<script src="res/plugin/jsPlumb/lib/jsBezier-0.8.js"></script>--%>
<script src="res/plugin/jsPlumb/disk/js/jsPlumb-2.2.8-min.js"></script>
<!-- event adapter -->
<%--<script src="res/plugin/jsPlumb/lib/mottle-0.7.3.js"></script>--%>
<!-- geometry functions -->
<%--<script src="res/plugin/jsPlumb/lib/biltong-0.3.js"></script>--%>
<!-- drag -->
<%--<script src="res/plugin/jsPlumb/lib/katavorio-0.18.0.js"></script>--%>
<!-- jsplumb util -->
<%--<script src="res/plugin/jsPlumb/src/util.js"></script>--%>
<%--<script src="res/plugin/jsPlumb/src/browser-util.js"></script>--%>
<!-- main jsplumb engine -->
<%--<script src="res/plugin/jsPlumb/src/jsPlumb.js"></script>--%>
<!-- base DOM adapter -->
<%--<script src="res/plugin/jsPlumb/src/dom-adapter.js"></script>--%>
<%--<script src="res/plugin/jsPlumb/src/overlay-component.js"></script>--%>
<!-- endpoint -->
<%--<script src="res/plugin/jsPlumb/src/endpoint.js"></script>--%>
<!-- connection -->
<%--<script src="res/plugin/jsPlumb/src/connection.js"></script>--%>
<!-- anchors -->
<script src="res/plugin/jsPlumb/src/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<%--<script src="res/plugin/jsPlumb/src/defaults.js"></script>--%>
<!-- bezier connectors -->
<%--<script src="res/plugin/jsPlumb/src/connectors-bezier.js"></script>--%>
<!-- state machine connectors -->
<%--<script src="res/plugin/jsPlumb/src/connectors-statemachine.js"></script>--%>
<!-- flowchart connectors -->
<%--<script src="res/plugin/jsPlumb/src/connectors-flowchart.js"></script>--%>
<!-- straight connectors -->
<%--<script src="res/plugin/jsPlumb/src/connectors-straight.js"></script>--%>
<!-- SVG renderer -->
<%--<script src="res/plugin/jsPlumb/src/renderers-svg.js"></script>--%>

<!-- common adapter -->
<%--<script src="res/plugin/jsPlumb/src/base-library-adapter.js"></script>--%>
<!-- no library jsPlumb adapter -->
<%--<script src="res/plugin/jsPlumb/src/dom.jsPlumb.js"></script>--%>
<%--<script src="res/plugin/jsPlumb/src/bezier-editor.js"></script>--%>
<%--右键菜单--%>
<script src="res/plugin/smart-menu/jquery-smartMenu.js"></script>

<%-- 节点核心 --%>
<script src="res/scripts/support/nodeflow/nodeflow.js"></script>
<!-- /JS -->

<%-------------------------------------------------------------------------------------------------------
    模态框管理器：
-------------------------------------------------------------------------------------------------------%>
<script type="text/javascript">

    var DialogManager = {};
    <%-- 对话框管理器 --%>
    var currentOperatingNodeType = undefined;
    <%-- 当前操作节点类型 --%>

    <%--
     * 打开确认模态框
     * @param msg 模态框中需要展示的消息内容
     * @param onConfirm 点击确定后的回调函数
     * @param onCancel 点击取消后的回调函数
     --%>
    DialogManager.confirmDialog = function (msg, onConfirm, onCancel) {
        $('#confirmDialogMsg').html(msg);

        <%--
         * 点击确认的处理函数
         --%>
        DialogManager.doConfirm = function doConfirmAction() {
            $('#confirmDialog').modal('hide');
            if (onConfirm) {
                onConfirm();
            }
        };

        <%--
         * 点击取消的处理函数
         --%>
        DialogManager.doCancel = function doConfirmAction() {
            $('#confirmDialog').modal('hide');
            if (onCancel) {
                onCancel();
            }
        };
        <%-- 显示确认模态框 --%>
        $('#confirmDialog').modal('show');
    };

    <%--
     * 点击节点编辑节点配置信息窗口
     * url: 点击节点配置后显示的页面URL
     --%>
    DialogManager.nodeConfigureEditor = function (nodeId, url) {
        console.log("当前操作节点ID：" + nodeId + " | 类型：" + currentOperatingNodeType + " | url：" + url);
        <%-- TODO:打开节点属性编辑页面，设置对应的页面URL到iframe中 --%>
        $('#configFrame').attr('src', url);
        $('#node-config-modal-title').html($('#' + operatingNodeID).find('.node-desc').html() + " 配置");
        $('#node-config-modal-desc').html(operatingNodeID);
        <%-- 显示节点配置窗口 --%>
        $('#nodeConfigureModal').modal('show');
    }

</script>
<%-------------------------------------------------------------------------------------------------------
    请求管理器
-------------------------------------------------------------------------------------------------------%>
<%--suppress JSUnresolvedFunction --%>
<script type="text/javascript">
    <%--
     * Http 请求管理器：管理所有的请求处理，统一使用管理器管理和配置请求
     --%>
    var RequestManager = {};
    var jobId = undefined;

    <%--
     * 初始化绘制面板，初始化开始和结束节点、获得节点工具面板的项目
     --%>
    RequestManager.postInitNodePainter = function () {
        $.post("/nodePainter/initNodePainter", {}, function (ret) {
            if (ret['flag']) {
                <%-- 获取成功,装载工具面板 --%>
                initNodeToolBox(ret['result']);
                draggableInit();

                setTimeout(function () {
                    <%-- 添加默认的开始以及结束节点 --%>
                    var preLoadNodes = ret['result']['PRE_LOAD_NODES'];
                    <%-- 加载需要预加载的节点到绘制面板中 --%>
                    for (var key in preLoadNodes) {
                        var configUrl = preLoadNodes[key]['nodeResource']['configUrl'];
                        createNodeOnCanvas(new Vec2(preLoadNodes[key]['x'], preLoadNodes[key]['y']), preLoadNodes[key]['nodeId'],
                            preLoadNodes[key]['nodeResource']['nodeName'], NODE_TEMPLATE.format({
                                title: preLoadNodes[key]['nodeResource']['nodeName'],
                                imgSrc: preLoadNodes[key]['nodeResource']['midImage'],
                                configUrl: configUrl
                            }), configUrl);
                    }
                }, 50)
                jobId = ret['result']['KEY_JOB_ID'];
                // 初始化并连接作业监听
                $('#testJobId').html(jobId);
            } else {
                <%-- 处理失败 --%>
            }
        }, "json");
    };

    function createNodeOnCanvasFromJsonData(jsonNode) {
        var canvasId = "canvas-area";
        var nodeResource = jsonNode['nodeResource'];
        createNodeOnCanvas(new Vec2(jsonNode.x, jsonNode.y), jsonNode.nodeId,
            nodeResource.nodeName, NODE_TEMPLATE.format({
                title: nodeResource.nodeName,
                imgSrc: nodeResource.midImage,
                configUrl: nodeResource.configUrl,
                uuids: uuid(),
                Container: canvasId
            }), nodeResource.configUrl);
    }

    RequestManager.viewJdfContent = function () {
        window.open("/home/node/jdfViewer?jobId=" + jobId);
    }
    RequestManager.onJdfSave = function () {
        window.location.href = "/jobService/saveJdf?jobId=" + jobId;
        /*
        $.ajax({
            url: "/jobService/saveJdf",
            type: "POST",
            data: {
                jobId: $('#testJobId').html()
            },
            success: function (data) {
                if (data['flag']) {
                    toastr.success('作业保存成功', '操作成功');
                } else {
                    toastr.error('作业保存失败', '操作失败');
                }
            },
            error: function () {
                toastr.error('作业保存失败', '操作失败');
            }
        }, "json");*/
    }

    RequestManager.showRunLog = function (jobId, operatingNodeID) {
        window.open("/home/node/nodeRunLog?jobId=" + jobId + "&nodeId=" + operatingNodeID);
    }

    <%--
     * 需要加载的作业数据文件
    --%>
    RequestManager.uploadJobDataFile = function () {
        var formData = new FormData($('#fileInfo')[0]);
        formData.append("jdfElem", document.getElementById("load-job-data-file").files[0]);
        $.ajax({
            url: "/jobService/uploadJdf",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function (data) {
                console.log('data', data);
                if (data['flag']) {

                    isJobReload = true;
                    var result = data['result'];
                    console.log('result', result);

                    jobId = result.jobId;
                    $('#testJobId').html(jobId);

                    jsPlumbInstance.empty('canvas-area');
                    var startNode = result['startNode'];
                    var endNode = result['endNode'];

                    var connectors = result['connectors'] ? result['connectors']['connectors'] : undefined;
                    var entries = result['entries']['entries'];

                    createNodeOnCanvasFromJsonData(startNode);
                    createNodeOnCanvasFromJsonData(endNode);
                    // 获得开始结束节点，单独处理
                    for (var i = 0; i < entries.length; i++) {
                        createNodeOnCanvasFromJsonData(entries[i]);
                    }

                    if (connectors) {
                        for (var c = 0; c < connectors.length; c++) {
                            var connection = jsPlumbInstance.connect({
                                source: connectors[c]['sourceId'],
                                target: connectors[c]['targetId'],
                                anchor: ["Perimeter", {shape: "Circle"}]
                            });

                            if (connectors[c]['connectType'] == 2) {
                                var label = connection.getOverlay("label");
                                label.setLabel('失败');
                                connection.toggleType("failureLine");
                            }
                        }
                    }

                    toastr.success('作业读取加载成功', '操作成功');
                    isJobReload = false;
                } else {
                    toastr.error('作业读取加载失败', '操作失败');
                }
            },
            error: function () {
                dlgAlert("上传失败！");
            }
        }, "json");
    }

    <%--
     * 创建一个节点，如果创建失败，那么删除对应的节点
     --%>
    RequestManager.postCreateNode = function (param) {
        $.post('/nodePainter/createNode', param, function (ret) {
            if (!ret['flag']) {
                console.log('创建失败，删除创建的节点', param.nodeId);
                <%-- 删除节点 --%>
                jsPlumbInstance.remove($('#' + param.nodeId));
                toastr.error(ret['msg'], "节点创建失败")
            }
        }, 'json');
    };

    <%--
     * 删除节点
     --%>
    RequestManager.postDeleteNode = function (param) {
        $.post("/nodePainter/deleteNode", param, function (ret) {
            if (ret['flag']) {
                <%-- 删除节点 --%>
                var delNode = $('#' + operatingNodeID);
                jsPlumbInstance.detachAllConnections(delNode);
                delNode.remove();
            } else {
                toastr.error(ret['msg'], "操作失败")
            }
        }, 'json');
    };

    <%--
     * 创建连线
     --%>
    RequestManager.postConnected = function (param) {
        $.post("/nodePainter/connectNodes", param, function (ret) {
            if (!ret['flag']) {
                <%-- 连线失败 --%>
                toastr.error(ret['msg'], "操作失败");
                var conn = jsPlumbInstance.getConnections({
                    source: param.sourceId,
                    target: param.targetId
                });
                if (conn[0]) {
                    jsPlumbInstance.detach(conn[0]);
                }
                var warnNode = ret['result'];
                for (var i = 0; i < warnNode.length; i++) {
                    $('#' + warnNode[i]).find("img").addClass("selector-highlight");
                }
            }
        }, 'json');
    };

    <%--
     * 删除节点之间的连线
     --%>
    RequestManager.postDisconnect = function (param) {
        $.post("/nodePainter/disconnectNodes", param, function (ret) {
            if (!ret['flag']) {
                <%-- 删除连线失败 --%>
                toastr.error(ret['msg'], '操作失败');
            } else {
                var conn = jsPlumbInstance.getConnections({
                    source: param.sourceId,
                    target: param.targetId
                });
                if (conn[0]) {
                    jsPlumbInstance.detach(conn[0]);
                }
            }
        }, 'json');

    };

    <%--
     * 移动节点，重新设置节点的坐标
     --%>
    RequestManager.postMoveNode = function (param) {
        $.post("/nodePainter/moveNode", param, function (ret) {
            if (!ret['flat']) {
                <%-- 节点失败 --%>
            } else {
                <%-- 移动节点成功 --%>
            }
        }, 'json');
    };

    <%--
     * 测试运行作业
     --%>
    RequestManager.postTestingRunning = function () {
        // TODO:开启监控
        initSocket(jobId);
        $.post("/nodePainter/testRunning", {
            jobId: jobId
        }, function (ret) {
            if (!ret['flag']) {
                <%-- 节点运行失败 --%>
            } else {
                <%-- 节点开始运行 --%>
                toastr.info("节点正在运行");
            }
        }, 'json');
    };

    <%--
     * 提交节点配置
     --%>
    RequestManager.postSubmitConfigNode = function () {
        var childObj = window.frames['configFrame'].contentWindow;
        <%-- 开始处理提交，此处添加等待锁定提交按钮等操作 --%>

        <%-- 使用相同的配置地址，从子页面中收集配置数据，并将返回后的数据交给子页面中的处理函数进行处理通知 --%>
        var submitFunc = childObj.onSubmit;
        var childParams = childObj.collectParams();
        console.log(childParams);
        $.post("/nodePainter/configNode", {
            jobId: jobId,
            nodeId: operatingNodeID,
            jsonData: JSON.stringify(childParams)
        }, function (ret) {
            if (!ret['flag']) {
                toastr.info("更新节点配置失败，请重试!");
            } else {
                submitFunc(ret);
            }
        }, 'json');

        <%-- 提交处理完毕，此处添加等待解锁提交按钮并关闭窗口操作 --%>
        $('#nodeConfigureModal').modal('hide');
    };

    <%--
     * 配置节点信息
     --%>
    RequestManager.postNodeConfiguration = function () {
        var childObj = window.frames['configFrame'].contentWindow;
        $.post("/nodePainter/nodeConfiguration", {
            jobId: jobId,
            nodeId: operatingNodeID
        }, function (ret) {
            if (!ret['flag']) {
                toastr.err("获取节点信息失败，请重试!");
            } else {
                childObj.onInit(ret['result']);
            }
        }, 'json');
    };

    <%--
     * 修改节点之间的连线类型
     --%>
    RequestManager.postChangeConnectorType = function (connectorType) {
        $.post("/nodePainter/changeConnectorType", {
            jobId: jobId,
            sourceId: operatingConnection.sourceId,
            targetId: operatingConnection.targetId,
            connectorType: connectorType
        }, function (ret) {
            if (!ret['flag']) {
                toastr.err("修改连接类型失败，请重试!");
            } else {
                var conn = jsPlumbInstance.getConnections({
                    source: operatingConnection.sourceId,
                    target: operatingConnection.targetId
                });
                var label = conn[0].getOverlay("label");
                if (conn) {
                    conn[0].toggleType("failureLine");
                    label.setLabel('失败');
                } else {
                    label.setLabel('成功');
                }
            }
        }, 'json');
    };

    <%-- ================================================================================================================
     请求管理器 END
     ================================================================================================================ --%>

    <%--
     * 初始化节点面板
     * @param data 初始化的面板数据
     --%>

    function initNodeToolBox(data) {
        var nodePanelData = data['NODE_PANEL'];
        <%-- 节点工具箱的html模板
        <%-- 追加分组
        <%-- var template = "<li><div class='draggable-node-item' title='{nodeName}'><img alt='image' title='{nodeName}' class='node-small-icon' src='{bigImage}'></div></li>";--%>
        var template = "<div class='feed-element tool-item'>\
                            <div class='draggable-node-item' title='{nodeName}'>\
                                <a href='javascript:void(0);' class='pull-left' src='{midImage}' data-config-url='{configUrl}' data-resource-id='{resId}'>\
                                    <img alt='image' class='img-circle draggable-node-dragging' src='{midImage}'>\
                                </a>\
                            </div>\
                            <div class='media-body' style='float: left;margin-left: 15px;'>\
                                <small class='pull-right'></small>\
                                    <strong style='float: left;'>{nodeName}</strong>  <br>\
                                <small class='text-muted'>节点描述</small>\
                            </div>\
                        </div>";
        var nodeGroup = "<li><a id='toggle-toolbox' href='javascript:void(0);'><i class='fa fa-suitcase' style='color: #0e9aef;'></i>&nbsp;&nbsp;{groupName}</a></li>";
        var groupSet = new Set();
        for (var key in nodePanelData) {
            groupSet.add(nodePanelData[key]['groupName']);
            nodeToolBox.append(template.format(nodePanelData[key]));
        }
        groupSet.forEach(function (item) {
            $('#node-classify').append(nodeGroup.format({groupName: item}));
        });
    }
</script>

<script type="text/javascript">
    var webSocket = null;

    function initSocket(jobId) {
        if (!webSocket) {
            <%-- 判断当前浏览器是否支持WebSocket --%>
            if (!('WebSocket' in window)) {
                alert('Not support webSocket')
            } else {
                webSocket = new WebSocket("ws://localhost:8080/executorStatus?jobId=" + jobId);
            }

            <%-- 连接发生错误的回调方法 --%>
            webSocket.onerror = function () {
                toastr.err("无法连接监控服务");
            };

            <%-- 连接成功建立的回调方法 --%>
            webSocket.onopen = function (event) {
                processMessage("open");
            };

            <%-- 接收到消息的回调方法 --%>
            webSocket.onmessage = function (event) {
                processMessage(event.data);
                console.log(event.data);
            };

            <%-- 连接关闭的回调方法 --%>
            webSocket.onclose = function () {
                processMessage("close");
            };

            <%-- 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。 --%>
            window.onbeforeunload = function () {
                webSocket.close();
            };

            <%-- 将消息显示在网页上 --%>

            function processMessage(data) {
                var infos = data.split(',');
                var statusObj = $('#' + infos[0]).find('.node-status');
                if (infos[1] == '0') {
                    <%-- 任务正在执行，添加执行状态效果 --%>
                    statusObj.addClass('status-indication');
                    console.log('添加执行状态');
                } else if (infos[1] == '12') {
                    statusObj.removeClass('status-indication');
                }
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
    }
</script>


<%-------------------------------------------------------------------------------------------------------
    页面加载成功处理区
-------------------------------------------------------------------------------------------------------%>
<script type="text/javascript">

    <%--
     * 定义二维坐标类型
     --%>
    var Vec2 = function (x, y) {
        this.x = x ? x : 0;
        this.y = y ? y : 0;
    };

    <%--
     * 两个节点添加
     --%>
    Vec2.add = function (vec) {
        this.x = this.x + vec.x;
        this.y = this.y + vec.y;
        return this;
    };

    <%--
     * 节点绘制类
     --%>
    var NodePainter = function (canvasId, hadoopConfig) {
        var that = this;
        this.hadoopConfig = $.extend({
            canvas_id: '#' + canvasId, <%-- 节点绘制的id --%>
            menuCallBack: undefined
        }, hadoopConfig || {});
        this.currMouse = new Vec2(0, 0);
        <%-- 初始化鼠标的移动事件 --%>
        this.isMenuVisible = false;
        <%-- 当前菜单的可见性 --%>
        this.isRectSelectable = true;
        <%-- 框选功能可用性 --%>

        this.mouseType = 0;
        this.mouseClick = new Vec2(0, 0);
        <%-- 鼠标点击坐标 --%>
        this.mouseRelease = new Vec2(0, 0);
        <%-- 鼠标释放坐标 --%>

        this.canvasDragging = false;
        <%-- 画板的拖拽状态 --%>
        this.isNodeDragging = false;
        <%-- 节点处于拖动状态 --%>
        this.multiSelectedNode = [];

        $canvas_obj = $(this.hadoopConfig.canvas_id);
        <%-- 获得绘制面板实例 --%>

        <%--
         * 初始化绘制面板的时间监听
         --%>
        this.init = function () {
            <%-- 捕捉实时鼠标位置 --%>
            $canvas_obj.mousemove(function (event) {
                that.currMouse.x = event.pageX;
                that.currMouse.y = event.pageY;
                $('#mouse_DebugX').html(that.currMouse.x);
                $('#mouse_DebugY').html(that.currMouse.y);

                <%-- 如果当前处于拖动状态 --%>
                if (that.canvasDragging) {
                    var rectSelector = $("#rectSelector");
                    <%-- 计算当前的宽度值 --%>
                    var width = that.currMouse.x - that.mouseClick.x;
                    var height = that.currMouse.y - that.mouseClick.y;

                    <%-- 如果当前的width为正数，那么说明点击的坐标即为x值 --%>
                    <%-- 如果当前的width为负数，那么说明起始坐标x值为点击的坐标减去宽度 --%>
                    var x = width >= 0 ? that.mouseClick.x : that.mouseClick.x + width;
                    var y = height >= 0 ? that.mouseClick.y : that.mouseClick.y + height;

                    $canvas_obj.css('cursor', 'crosshair');
                    rectSelector.css('display', 'block');
                    rectSelector.css('left', x + "px");
                    rectSelector.css('top', y + "px");

                    rectSelector.css('width', Math.abs(width) + "px");
                    rectSelector.css('height', Math.abs(height) + "px");

                    rectSelector.find("rect").attr('width', Math.abs(width) + "px");
                    rectSelector.find("rect").attr('height', Math.abs(height) + "px");

                    rectSelector.css('margin-left', (width < 0 ? "" : "-") + "8px");
                    rectSelector.css('margin-top', (height < 0 ? "" : "-") + "8px");
                }
            });
        };
    };


    <%-- ===============================================================================================================*/
        全局参数定义开始
    /*=============================================================================================================== --%>
    var jsPlumbInstance = undefined;
    <%-- 节点绘制实例 --%>
    var nodeToolBox = undefined;
    <%-- 工具箱实例 --%>
    var $canvas_obj = undefined;
    <%-- 节点绘制对象 --%>
    var painter = new NodePainter('canvas-area', {});
    <%-- 绘制对象 --%>
    var nodeInfoCache = {};
    <%-- 节点信息缓存，记录节点的名称、坐标、ID等信息 --%>
    var operatingNodeID = undefined;
    var operatingNodeConfigUrl = undefined;
    var dragSrc = 'tool';
    var operatingConnection = undefined;
    var isJobReload = false;
    var NODE_TEMPLATE = "<div class='draggable-node-item ui-draggable ui-draggable-handle' _title='{title}' data-config-url='{configUrl}' data-resource-id='{resId}'>\
                               <div class='node-status'></div>\
                               <a href='javascript:void(0);' class='pull-left'>\
                                    <img alt='image' class='img-circle draggable-node-dragging node-shadow node-gray-border' src='{imgSrc}'>\
                               </a>\
                           </div>";
    <%--
     * 右键菜单的配置项
     --%>
    var MenuConfig = {
        <%-- 菜单的基本配置 --%>
        menuOperationOpt: {
            name: "generalOperation",
            offsetX: 2,
            offsetY: 2,
            textLimit: 10,
            beforeShow: $.noop,
            afterShow: $.noop
        },
        menuConnOpt: {
            name: "connectionOperation",
            offsetX: 2,
            offsetY: 2,
            textLimit: 10,
            beforeShow: $.noop,
            afterShow: $.noop
        },
        nodeOperateMenu: [
            [{
                text: "删除节点", <%-- 删除节点菜单 --%>
                func: function () {
                    DialogManager.confirmDialog("确认删除节点? (将删除关联的连线)", function () {
                        var param = {
                            jobId: jobId,
                            nodeId: operatingNodeID
                        };
                        RequestManager.postDeleteNode(param);
                    })
                }
            }], [{
                text: "节点配置", <%-- 弹出节点的属性配置页面 --%>
                func: function () {
                    DialogManager.nodeConfigureEditor(operatingNodeID, operatingNodeConfigUrl);
                }
            }, {
                text: "编辑名称",
                func: function () {
                    $('#inputDialog').show();
                }
            }, {
                text: "运行日志",
                func: function () {
                    RequestManager.showRunLog(jobId, operatingNodeID);
                }
            }]
        ],
        connectionOperateMenu: [
            [{
                text: "删除连线", <%-- 删除节点菜单 --%>
                func: function () {
                    $('#confirmDialog').modal('show');
                    DialogManager.confirmDialog("确认删除节点之间的连线?", function () {
                        var sourceId = operatingConnection.sourceId;
                        var targetId = operatingConnection.targetId;
                        var param = {
                            jobId: jobId,
                            sourceId: sourceId,
                            targetId: targetId
                        };
                        RequestManager.postDisconnect(param);
                    })
                }
            }], [{
                text: "标记为成功线", <%-- 弹出节点的属性配置页面 --%>
                func: function () {
                    RequestManager.postChangeConnectorType('success');
                }
            }, {
                text: "标记为失败线",
                func: function () {
                    RequestManager.postChangeConnectorType('failure');
                }
            }]
        ],
        jobChildOperateMenu: [
            [{
                text: "打开作业（只读）", <%-- 删除节点菜单 --%>
                func: function () {

                }
            }], [{
                text: "作业配置", <%-- 弹出节点的属性配置页面 --%>
                func: function () {
//                    DialogManager.nodeConfigureEditor(operatingNodeID, operatingNodeConfigUrl);
                }
            }, {
                text: "删除作业",
                func: function () {
                    DialogManager.confirmDialog("确认删除子作业? (将删除关联的连线)", function () {
//                        var param = {
//                            jobId: jobId,
//                            nodeId: operatingNodeID
//                        };
//                        RequestManager.postDeleteNode(param);
                    })
                }
            }]
        ]
    };
    /*===============================================================================================================*/

    <%--
     * 页面加载成功后的处理
     --%>
    $(function () {
        prepareJsPlumbConfiguration();
        <%-- 初始化节点绘制JsPlumb配置 --%>
        preparePainterConfiguration();
        <%-- 初始化节点绘制参数配置 --%>
        painterInit();
        <%-- 绘制面板初始化 --%>

        setTimeout(function () {
            RequestManager.postInitNodePainter();
            bindingClassListener();
            pageInit();
        }, 200);

        $("#nodeSearch").keydown(function (event) {
            var keycode = event.which || event.keyCode;
            if (keycode == 13) {
                var nodeId = $('#nodeSearch').val();
                $('#' + nodeId).find(".node-status").addClass("status-indication");
                $('#nodeSearch').val('');
            }
        });
    });

    <%--
     * 准备JsPlumb的配置
     --%>

    function prepareJsPlumbConfiguration() {
        jsPlumb.ready(function () {
            var canvasId = "canvas-area";
            <%-- 初始化节点绘制样式 --%>
            jsPlumbInstance = jsPlumb.getInstance({
                <%-- 设置端点的样式 --%>
                Endpoint: ["Dot", {radius: 2}],
                <%-- 设置连接的类型 --%>
                PaintStyle: {strokeWidth: 1, stroke: "#0099cc", "dashstyle": "2 2"},
                <%--                Connector: ["Bezier", {proximityLimit: 70}],                                  --%><%-- 设置连线的类型 --%>
                Connector: ["Straight", {stub: 70}], <%-- 设置连线的类型 --%>
                connectorStyle: {strokeStyle: "#316b31", lineWidth: 6}, <%-- 设置连线的颜色 --%>
                <%-- 指定鼠标指示时的颜色和宽度 --%>
                HoverPaintStyle: {stroke: "#1e8151", strokeWidth: 2},
                ConnectionOverlays: [
                    ["Arrow", {
                        location: 1,
                        id: "arrow",
                        length: 14,
                        foldback: 0.8
                    }],
                    ["Label", {id: "label", cssClass: "aLabel"}]
                ],
                uuids: uuid(),
                Container: canvasId
            });
            <%-- 注册连线时的线样式 --%>
            jsPlumbInstance.registerConnectionTypes({
                "basic": {
                    anchor: ["Perimeter", {shape: "Circle"}],
                    connector: "StateMachine"
                },
                "failureLine": {
                    paintStyle: {stroke: "red", strokeWidth: 1},
                    hoverPaintStyle: {strokeWidth: 1},
                    cssClass: "connector-selected"
                }
            });
            window.jsp = jsPlumbInstance;

            var canvas = document.getElementById(canvasId);
            var windows = jsPlumb.getSelector(".statemachine-demo .w");

            <%-- 设置点击连线的事件
            <%-- jsPlumbInstance.bind("click", function (connection) {});--%>

            <%-- TODO：节点连接的连线描述 --%>
            jsPlumbInstance.bind("connection", function (info) {
                var param = {
                    jobId: jobId,
                    sourceId: info.sourceId,
                    targetId: info.targetId
                };
                if (!isJobReload) {
                    RequestManager.postConnected(param);
                }
                // "连接_" + info.connection.id
                var label = info.connection.getOverlay("label");
                label.setLabel("成功");
                var labelId = label.canvas.id;

                <%-- 绑定连接的点击事件 --%>
                var connLabelObj = $('#' + labelId);
                connLabelObj.mousedown(function (event) {
                    checkAndCloseCollection();
                    if (event.which == 3) {
                        <%-- 获得当前线对象连接的两个端点 --%>
                        operatingConnection = info.connection;
                    }
                });
                $(connLabelObj).smartMenu(MenuConfig.connectionOperateMenu, MenuConfig.menuConnOpt);
            });

            jsPlumb.on(canvas, "dblclick", function (e) {
                $('#collectionCircle').css('top', painter.mouseRelease.y - 90 + "px");
                $('#collectionCircle').css('left', painter.mouseRelease.x - 90 + "px");
                $('#collectionCircle').css('z-index', 99999);
                $('#collectionCircle').addClass('open');
            });
        });
    }

    <%--
     * 页面加载成功后准备节点绘制参数
     --%>

    function preparePainterConfiguration() {
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "progressBar": false,
            "preventDuplicates": false,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "400",
            "hideDuration": "1000",
            "timeOut": "2000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        nodeToolBox = $('#node-tool-box');
    }
</script>

<script type="text/javascript">

    <%-- 初始化拖拽对象 --%>

    function draggableInit() {
        <%-- 可拖拽对象的初始化 --%>
        $('#node-tool-box .draggable-node-item').draggable({
            cursorAt: {top: 25, left: 25},
            appendTo: ".canvas-container", <%-- 可以添加到的对象的class中 --%>
            helper: function (event) {
                dragSrc = 'tool';
                return $(NODE_TEMPLATE.format({
                    title: event.currentTarget.attributes['title'].value,
                    imgSrc: event.currentTarget.childNodes[1].childNodes[1].attributes['src'].value,
                    configUrl: event.currentTarget.childNodes[1].attributes['data-config-url'].value
                }));
            },
            zIndex: 99999
        });

        $('#collectionCircle .draggable-node-item').draggable({
            cursorAt: {top: 25, left: 25},
            appendTo: ".canvas-container", <%-- 可以添加到的对象的class中 --%>
            helper: function (event) {
                console.log(event);
                dragSrc = 'coll';
                return $(NODE_TEMPLATE.format({
                    title: event.currentTarget.childNodes[1].attributes['title'].value,
                    imgSrc: event.currentTarget.childNodes[1].attributes['src'].value
                }));
            },
            zIndex: 99999
        });

        <%-- 向主面板中拖放 --%>
        $canvas_obj.droppable({
            accept: ".draggable-node-item",
            drop: function (event, ui) {
                <%-- 获取当前拖拽在画板中的坐标 --%>
                var dropVec2 = new Vec2(parseInt(ui.offset.left) + $canvas_obj.scrollLeft(), parseInt(ui.offset.top) + $canvas_obj.scrollTop());
                <%-- 获取当前拖拽结点的内容并添加到画板中 --%>
                var nodeId = uuid().replace(/-/g, '');
                var childIdx = dragSrc == 'tool' ? 0 : 1;
                var title = ui.draggable[0].attributes['title'].value;
                var configUrl = ui.draggable[0].childNodes[1].attributes['data-config-url'].value;
                var resId = ui.draggable[0].childNodes[1].attributes['data-resource-id'].value;
                createNodeOnCanvas(dropVec2, nodeId, title, NODE_TEMPLATE.format({
                    title: title,
                    imgSrc: ui.draggable[0].childNodes[1].attributes['src'].value,
                    configUrl: configUrl,
                    resId: resId
                }), configUrl);

                <%-- 拖放的过程中同步向后台创建一个节点，如果创建失败，则从前台中删除这个节点 --%>
                <%-- TODO:要已一个默认的初始化数据进行节点创建初始化，如时间默认2s延时等 --%>
                RequestManager.postCreateNode({
                    resId: resId,
                    jobId: jobId,
                    nodeId: nodeId,
                    title: title,
                    x: dropVec2.x,
                    y: dropVec2.y,
                    entryJson: JSON.stringify({
                        time: {year: 0, month: 0, day: 0, hour: 0, minutes: 0, seconds: 2},
                        ssh: {
                            host: {host: "58.32.68.69", user: "mopper", password: "mopper", port: 41122},
                            command: "pwd;ls -lah;"
                        }
                    })
                });
            }
        });
    }

    <%-- + 在绘制面板上创建一个节点 --%>

    function createNodeOnCanvas(vec, nodeId, title, content, configUrl) {
        <%-- 包裹当前从控制面板中拖放出来的节点，使之能够被控制 --%>
        $canvas_obj.append("<div class='ui-draggable ui-draggable-dragging' uuid='" + nodeId + "' id='" + nodeId +
            "' data-config-url='" + configUrl + "' style='position: absolute; z-index: 99999;left:" + vec.x + "px;top:" + vec.y + "px;border-radius:50%;text-align: center;'>" +
            content + "</div>");

        <%-- 获得当前拖动对象 --%>
        var currNode = $('#' + nodeId);
        <%-- 处理节点，修饰当前追加到画板中的节点以及绑定相关的事件 --%>
        nodeInfoCache[nodeId] = new NodeInfo(nodeId, currNode.find(".draggable-node-item").attr("_title"), vec);
        decorate(nodeId, currNode, title);
    }

    <%-- ++ 装饰指定的节点 --%>

    var movingNodeCache = [];

    function calculateNodeCenterCordination(nodeId, node) {
        var tmpNode = $('#' + nodeId);
        var nodeStyle = node ? node[0].style : tmpNode[0].style;
        var left = parseInt(tmpNode.css('left'));
        var top = parseInt(tmpNode.css('top'));
        var width = tmpNode.width();
        var height = tmpNode.height();
        return new Vec2(parseInt(left + (width / 2)), parseInt(top + (height / 2)));
    }

    function decorate(nodeId, node, title) {
        var nodeTitle = title ? title : node.find(".draggable-node-item").attr("_title");
        <%-- 向节点中添加一个可以作为连线对象的对象 --%>
        node.find('.draggable-node-item').append("<div class='ep'></div>");
        <%-- 为节点添加一个阴影效果 --%>
        node.find('img').addClass("node-shadow").addClass("node-gray-border");
        if (title === "子作业") {
            node.find('img').addClass("job-highlight");
        }
        <%-- 使节点变得可以拖动 --%>
        jsPlumbInstance.draggable(node, {
            start: function () {
                painter.isNodeDragging = true;
            },
            stop: function (data) {
                console.log('--->', data);
                var dropVec2 = new Vec2(data['finalPos'][0], data['finalPos'][1]);
                var multiSelectSize = checkMultiSelected();
                <%-- 拖动停止运动的时候重新更新节点缓存信息列表 --%>
                nodeInfoCache[nodeId] = new NodeInfo(nodeId, nodeTitle, dropVec2);
                painter.isNodeDragging = false;

                if (multiSelectSize) {
                    movingNodeCache[movingNodeCache.length++] = {
                        jobId: jobId,
                        nodeId: nodeId,
                        title: nodeTitle,
                        toX: dropVec2.x,
                        toY: dropVec2.y
                    }
                    if (movingNodeCache.length == multiSelectSize) {
                        RequestManager.postMoveNode({data: movingNodeCache});
                        movingNodeCache = [];
                    }
                } else {
                    <%-- 调用服务，更新节点的位置 --%>
                    RequestManager.postMoveNode({
                        data: [{
                            jobId: jobId,
                            nodeId: nodeId,
                            title: nodeTitle,
                            toX: dropVec2.x,
                            toY: dropVec2.y
                        }]
                    });
                }
            }
        });
        <%-- 设置节点为可出度节点 --%>
        jsPlumbInstance.makeSource(node, {
            filter: ".ep",
            anchor: ["Perimeter", {shape: "Circle"}],
            connectionType: "basic",
            extract: {
                "action": "the-action"
            },
            maxConnections: 10,
            onMaxConnections: function (info, e) {
                alert("当前达到了最大的连接数 (" + info.maxConnections + ") 条");
            }
        });
        <%-- 设置节点为可入度节点 --%>
        jsPlumbInstance.makeTarget(node, {
            anchor: ["Perimeter", {shape: "Circle"}],
            allowLoopback: false
        });
        jsPlumbInstance.fire("jsPlumbDemoNodeAdded", node);

        <%-- 继续装饰添加到画布上的节点 --%>
        decorateNodeDesc(nodeId, node, nodeTitle);
    }

    <%-- +++ 继续装饰节点，绑定操作时间等 --%>
    var nodeOverTimer = null;
    <%-- 是否保持节点Debug信息 Ctrl+鼠标左键保持，点击面板释放 --%>
    var keepNodeDebugInfo = false;

    function decorateNodeDesc(nodeId, node, nodeTitle) {
        <%-- 处理新增节点后的节点描述 --%>
        node.append("<div class='node-desc'>" + nodeTitle + "</div>");
        <%-- 处理节点的菜单点击事件 --%>
        node.find('.ep').bind("mousedown", function (e) {
            checkAndCloseCollection();
            if (e.which == 3) {
                operatingNodeID = $(this).closest('.ui-draggable-dragging').attr('id');
                operatingNodeConfigUrl = $(this).closest('.ui-draggable-dragging').attr('data-config-url');
                $(this).smartMenu(nodeTitle === '子作业' ? MenuConfig.jobChildOperateMenu : MenuConfig.nodeOperateMenu, MenuConfig.menuOperationOpt);
            } else {
                if (e.ctrlKey) {
                    keepNodeDebugInfo = true;
                    <%-- TODO：CTRL键控制选择，选择对应的节点 --%>
                    var nodeId = $(this).closest('.draggable-node-item').parent().attr('id');
                    $('#' + nodeId).find("img").addClass("selector-highlight");
                    painter.multiSelectedNode.push(nodeId);
                    jsPlumbInstance.addToPosse(nodeId, "posse");

                    var vec = calculateNodeCenterCordination(nodeId);
                    $('#node_DebugX').html(vec.x);
                    $('#node_DebugY').html(vec.y);
                    $('#nodeId_Debug').html(nodeId);
                }
            }

            painter.mouseType = e.which;
            painter.canvasDragging = false;
            painter.mouseClick.x = e.pageX;
            painter.mouseClick.y = e.pageY;
        });

        node.find('.draggable-node-item').bind("mouseover", function (e) {
            var vec = calculateNodeCenterCordination(nodeId, node);
            $('#node_DebugX').html(vec.x);
            $('#node_DebugY').html(vec.y);
            $('#nodeId_Debug').html(nodeId);
        });

        node.find('.draggable-node-item').bind("mouseout", function (e) {
            if (!keepNodeDebugInfo) {
                $('#node_DebugX').html('x');
                $('#node_DebugY').html('y');
                $('#nodeId_Debug').html('-');
            }
        });

        <%-- 处理节点文本点击事件 --%>
        node.find('.node-desc').bind("mousedown", function (event) {
            checkAndCloseCollection();
            painter.mouseType = event.which;
            painter.canvasDragging = false;
            painter.mouseClick.x = event.pageX;
            painter.mouseClick.y = event.pageY;
        });
    }

    <%--
     * 初始化绘制参数
     --%>

    function painterInit() {
        <%-- 初始化绘制画笔 --%>
        painter.init();

        <%-- 绑定鼠标点击画板的事件 --%>
        $canvas_obj.mousedown(function (event) {
            console.log("鼠标点击");
            checkAndCloseCollection();
            if (!painter.isNodeDragging && event.button != 3) {
                painter.mouseType = event.button;
                painter.canvasDragging = true;
                painter.mouseClick.x = event.pageX;
                painter.mouseClick.y = event.pageY;
            } else {
                <%-- 重置区域选取记录 --%>
                painter.mouseClick = painter.mouseRelease = new Vec2(0, 0);
            }
            jsPlumbInstance.removeFromAllPosses(painter.multiSelectedNode);
            painter.multiSelectedNode = [];
            $('.selector-highlight').removeClass("selector-highlight");
            $('.status-indication').removeClass("status-indication");

            keepNodeDebugInfo = false;
            $('#node_DebugX').html('x');
            $('#node_DebugY').html('y');
            $('#nodeId_Debug').html('-');
        });

        <%-- 绑定鼠标从画板释放的事件 --%>
        $canvas_obj.mouseup(function (event) {
            console.log("释放鼠标");
            $canvas_obj.css('cursor', 'auto');
            <%-- 节点非拖动状态，并且拖动对象是canvas，那么绘制框选指示 --%>
            painter.mouseRelease.x = event.pageX;
            painter.mouseRelease.y = event.pageY;
            if (!painter.isNodeDragging && painter.canvasDragging) {
                if (event.ctrlKey) {
                    <%-- TODO：CTRL键控制选择，选择对应的节点 --%>
                    console.log("CTRL键控制选择，选择对应的节点");
                }
                painter.mouseType = undefined;
                painter.canvasDragging = false;

                <%-- console.log(painter.mouseRelease, painter.mouseClick); --%>

                <%-- 释放了拖动的鼠标之后，将当前的选定区域的元素进行高亮显示 --%>

                var rectSelector = $('#rectSelector');
                rectSelector.css('display', 'none');

                var beginX = parseInt(rectSelector.css("left").replace("px", ""));
                var beginY = parseInt(rectSelector.css("top").replace("px", ""));

                var endX = beginX + parseInt(rectSelector.find("rect").attr("width").replace("px", ""));
                var endY = beginY + parseInt(rectSelector.find("rect").attr("height").replace("px", ""));

                if (Math.abs(painter.mouseClick.x - painter.mouseRelease.x) > 10
                    && Math.abs(painter.mouseClick.y - painter.mouseRelease.y) > 10) {
                    $.each(nodeInfoCache, function (nodeId) {
                        if (checkInRect(nodeInfoCache[nodeId]._vec2.x, nodeInfoCache[nodeId]._vec2.y, beginX, beginY, endX, endY)) {
                            $('#' + nodeId).find("img").addClass("selector-highlight");
                            painter.multiSelectedNode.push(nodeId);
                        }
                    });
                    jsPlumbInstance.addToPosse(painter.multiSelectedNode, "posse");
                }
            }
        });

        <%-- 检查节点是否在框选的范围中 --%>

        function checkInRect(x, y, beginX, beginY, endX, endY) {
            return x > beginX && x < endX && y > beginY && y < endY;
        }
    }

</script>

<script type="text/javascript">
    <%-- 初始化操作 --%>

    function pageInit() {
        $('.collapse-min-max').trigger('click');
        <%-- 绑定属性配置页面关闭的事件，防止配置页面关闭后依然存在 --%>
        $('#nodeConfigureModal').on('hidden.bs.modal', function () {
            $('#configFrame').attr('src', "about:blank");
        });
        var frame = window.frames['configFrame'];
        if (frame.attachEvent) {
            frame.attachEvent("onload", function () {
                if (frame.src && frame.src != 'about:blank') {
                    RequestManager.postNodeConfiguration();
                }
            });
        } else {
            frame.onload = function () {
                if (frame.src && frame.src != 'about:blank') {
                    RequestManager.postNodeConfiguration();
                }
            };
        }
    }

    <%-- 检查快捷工具面板是否可关闭，如果可关闭则关闭面板 --%>

    function checkAndCloseCollection() {
        if ($('#collectionCircle').hasClass('open')) {
            $('#collectionCircle').removeClass('open');
            setTimeout(function () {
                $('#collectionCircle').css('z-index', -1);
            }, 401);
        }
    }

    <%-- 判断是否选中了多个节点 --%>

    function checkMultiSelected() {
        return $('.selector-highlight').length;
    }

    <%-- 事件绑定监听器 --%>

    function bindingClassListener() {
        <%-- 绑定工具箱面板的位置更改按钮的事件 --%>
        $('.toggle-toolbox-panel-position').bind('click', toggleToolBoxPosition);
        <%-- 点击最小化或最大化工具箱面板 --%>
        $('.collapse-min-max').bind('click', bindingMaxMinBtn);
        <%-- 点击显示/隐藏工具箱面板 --%>
        $('#toggle-toolbox').bind('click', toggleToolboxPanel);
        $('#collection-toolbox').bind('click', toggleCollectionBoxPanel);
    }

    <%-- 绑定工具箱面板的位置变更事件 --%>

    function toggleToolBoxPosition() {
        var toolboxPanel = $('.toolbox-panel');
        <%-- 去掉居右显示 --%>
        if (toolboxPanel.hasClass('pull-right')) {
            toolboxPanel.removeClass('pull-right');
            $('.toggle-toolbox-panel-position i').removeClass('fa-angle-double-left').addClass('fa-angle-double-right');
        } else {
            toolboxPanel.addClass('pull-right');
            $('.toggle-toolbox-panel-position i').removeClass('fa-angle-double-right').addClass('fa-angle-double-left');
        }
    }

    <%-- 绑定最大化最小化按钮的点击事件 --%>

    function bindingMaxMinBtn() {
        var box = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = box.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-expand').toggleClass('fa-compress');
        box.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            box.resize();
            box.find('[id^=map-]').resize();
        }, 50);
    }

    <%-- 打开工具箱的控制面板 --%>

    function toggleToolboxPanel() {
        var toolboxPanel = $('#tool-box');
        if (toolboxPanel.css("display") == 'none') {
            toolboxPanel.css('display', 'block');
        } else {
            toolboxPanel.css('display', 'none');
        }
    }

    <%-- 打开收藏夹控制面板 --%>

    function toggleCollectionBoxPanel() {
        var toolboxPanel = $('#collection-item');
        if (toolboxPanel.css('display') == 'none') {
            toolboxPanel.css('display', 'block');
        } else {
            toolboxPanel.css('display', 'none');
        }
    }

    <%-- 菜单事件 --%>

    function onMenuShow(event) {
        $('#canvas-menu').css("display", "block");
    }
</script>

</html>
