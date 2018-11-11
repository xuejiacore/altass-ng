<%--
  Created by IntelliJ IDEA.
  User: Xuejia
  Date: 2016/11/11
  Time: 23:10
  To change this template use FileInfo | Settings | FileInfo Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>jsPlumb - state machine demonstration</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"/>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link href="//fonts.googleapis.com/css?family=Lato:400,700" rel="stylesheet">

    <link rel="stylesheet" href="res/plugin/jsPlumb/css/jsPlumbToolkit-defaults.css">
    <link rel="stylesheet" href="res/plugin/jsPlumb/css/main.css">
    <link rel="stylesheet" href="res/plugin/jsPlumb/css/jsPlumbToolkit-demo.css">
    <link rel="stylesheet" href="support/flow/nodeflow/demo.css">
</head>
<body>
<div class="jtk-demo-main">
    <!-- demo -->
    <div class="jtk-demo-canvas canvas-wide statemachine-demo jtk-surface jtk-surface-nopan" id="canvas">
        <div class="w" id="opened">BEGIN
            <div class="ep" action="begin"></div>
        </div>
        <div class="w" id="phone1">PHONE INTERVIEW 1
            <div class="ep" action="phone1"></div>
        </div>
        <div class="w" id="phone2">PHONE INTERVIEW 2
            <div class="ep" action="phone2"></div>
        </div>
        <div class="w" id="inperson">IN PERSON
            <div class="ep" action="inperson"></div>
        </div>
        <div class="w" id="rejected">REJECTED
            <div class="ep" action="rejected"></div>
        </div>
    </div>
</div>



<!-- JS -->
<!-- support lib for bezier stuff -->
<script src="res/plugin/jsPlumb/lib/jsBezier-0.8.js"></script>
<!-- event adapter -->
<script src="res/plugin/jsPlumb/lib/mottle-0.7.3.js"></script>
<!-- geometry functions -->
<script src="res/plugin/jsPlumb/lib/biltong-0.3.js"></script>
<!-- drag -->
<script src="res/plugin/jsPlumb/lib/katavorio-0.18.0.js"></script>
<!-- jsplumb util -->
<script src="res/plugin/jsPlumb/src/util.js"></script>
<script src="res/plugin/jsPlumb/src/browser-util.js"></script>
<!-- main jsplumb engine -->
<script src="res/plugin/jsPlumb/src/jsPlumb.js"></script>
<!-- base DOM adapter -->
<script src="res/plugin/jsPlumb/src/dom-adapter.js"></script>
<script src="res/plugin/jsPlumb/src/overlay-component.js"></script>
<!-- endpoint -->
<script src="res/plugin/jsPlumb/src/endpoint.js"></script>
<!-- connection -->
<script src="res/plugin/jsPlumb/src/connection.js"></script>
<!-- anchors -->
<script src="res/plugin/jsPlumb/src/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<script src="res/plugin/jsPlumb/src/defaults.js"></script>
<!-- bezier connectors -->
<script src="res/plugin/jsPlumb/src/connectors-bezier.js"></script>
<!-- state machine connectors -->
<script src="res/plugin/jsPlumb/src/connectors-statemachine.js"></script>
<!-- flowchart connectors -->
<script src="res/plugin/jsPlumb/src/connectors-flowchart.js"></script>
<!-- straight connectors -->
<script src="res/plugin/jsPlumb/src/connectors-straight.js"></script>
<!-- SVG renderer -->
<script src="res/plugin/jsPlumb/src/renderers-svg.js"></script>

<!-- common adapter -->
<script src="res/plugin/jsPlumb/src/base-library-adapter.js"></script>
<!-- no library jsPlumb adapter -->
<script src="res/plugin/jsPlumb/src/dom.jsPlumb.js"></script>
<script src="res/plugin/jsPlumb/src/bezier-editor.js"></script>
<!-- /JS -->

<!--  demo code -->
<script src="support/flow/nodeflow/demo.js"></script>

<script src="support/flow/nodeflow/demo-list.js"></script>
</body>
</html>

