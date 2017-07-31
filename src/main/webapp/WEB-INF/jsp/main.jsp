<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页面</title>
    <jsp:include page="../../common/include.jsp"></jsp:include>
    <link href="${basePath}/css/uploadify.css" rel="stylesheet">
<body>
<!-- start: Header -->
<div class="navbar">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse"
               data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="main.jsp"><span>润通代扣系统</span></a>

            <!-- start: Header Menu -->
            <div class="nav-no-collapse header-nav">
                <ul class="nav pull-right">
                    <!-- start: User Dropdown -->
                    <li class="dropdown">
                        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="halflings-icon white user"></i> admin
                            <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-menu-title">
                                <span>账户设置</span>
                            </li>
                            <li><a href="/logout.do"><i class="halflings-icon off"></i> 退出</a></li>
                        </ul>
                    </li>
                    <!-- end: User Dropdown -->
                </ul>
            </div>
            <!-- end: Header Menu -->

        </div>
    </div>
</div>
<!-- start: Header -->
<div class="container-fluid-full">
    <div class="row-fluid">
        <!-- start: Main Menu -->
        <div id="sidebar-left" class="span2">
            <div class="nav-collapse sidebar-nav">
                <ul class="nav nav-tabs nav-stacked main-menu">
                    <li><a href="main.jsp"><i class="icon-list-alt"></i><span class="hidden-tablet"> 导入账户信息</span></a>
                    </li>
                    <li><a href="deduction.jsp"><i class="icon-align-justify"></i><span class="hidden-tablet"> 代扣</span></a>
                    </li>
                    <li><a href="result.jsp"><i class="icon-bar-chart"></i><span
                            class="hidden-tablet"> 扣款结果统计</span></a></li>
                </ul>
            </div>
        </div>
        <!-- end: Main Menu -->

        <noscript>
            <div class="alert alert-block span10">
                <h4 class="alert-heading">Warning!</h4>
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>
                    enabled to use this site.</p>
            </div>
        </noscript>

        <!-- start: Content -->
        <div id="content" class="span10">
            <ul class="breadcrumb">
                <li>
                    <i class="icon-home"></i>
                    <a href="main.jsp">主页</a>
                    <i class="icon-angle-right"></i>
                </li>
                <li><a href="main.jap">导入账户信息</a></li>
            </ul>

            <div class="row-fluid sortable">
                <div class="box span12">
                    <div class="box-header" data-original-title>
                        <h2><i class="halflings-icon white edit"></i><span class="break"></span>导入账户信息</h2>
                    </div>
                    <div class="box-content">
                        <form class="form-horizontal" id="fileInputForm">
                            <fieldset>

                                <div class="control-group">
                                    <label class="control-label" for="uploadify">文件上传</label>
                                    <div class="controls">
                                        <input class="input-file uniform_on" id="uploadify" type="file">
                                        <div id="fileQueue" ></div>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div style="display: none">
                        <button id="successBtn" class="btn btn-primary noty"
                                data-noty-options='{"text":"文件上传成功","layout":"center","type":"information"}'>
                        </button>
                        <button id="failBtn" class="btn btn-primary noty"
                                data-noty-options='{"text":"文件上传失败","layout":"center","type":"error"}'>
                        </button>
                    </div>
                </div><!--/span-->

            </div><!--/row-->

        </div><!--/row-->
    </div><!--/.fluid-container-->

    <!-- end: Content -->
</div><!--/#content.span10-->
</div><!--/fluid-row-->


<footer>

    <p>
        <span style="text-align:left;float:left">版权所有 2017 重庆两江新区润通小额贷款有限公司 </span>

    </p>

</footer>

<!-- start: JavaScript-->

<script src="${basePath}/js/jquery-1.9.1.min.js"></script>
<script src="${basePath}/js/jquery-migrate-1.0.0.min.js"></script>

<script src="${basePath}/js/jquery-ui-1.10.0.custom.min.js"></script>

<script src="${basePath}/js/jquery.ui.touch-punch.js"></script>

<script src="${basePath}/js/modernizr.js"></script>

<script src="${basePath}/js/bootstrap.min.js"></script>
<script src="${basePath}/js/jquery.noty.js"></script>
<script src="${basePath}/js/jquery.cookie.js"></script>

<script src='${basePath}/js/fullcalendar.min.js'></script>

<script src='${basePath}/js/jquery.dataTables.min.js'></script>
<script src="${basePath}/js/excanvas.js"></script>
<script src="${basePath}/js/jquery.flot.js"></script>
<script src="${basePath}/js/jquery.flot.pie.js"></script>
<script src="${basePath}/js/jquery.flot.stack.js"></script>
<script src="${basePath}/js/jquery.flot.resize.min.js"></script>

<script src="${basePath}/js/jquery.chosen.min.js"></script>

<script src="${basePath}/js/jquery.uniform.min.js"></script>

<script src="${basePath}/js/jquery.cleditor.min.js"></script>

<script src="${basePath}/js/jquery.elfinder.min.js"></script>

<script src="${basePath}/js/jquery.raty.min.js"></script>

<script src="${basePath}/js/jquery.iphone.toggle.js"></script>

<script src="${basePath}/js/jquery.uploadify-3.1.min.js"></script>

<script src="${basePath}/js/jquery.gritter.min.js"></script>

<script src="${basePath}/js/jquery.imagesloaded.js"></script>

<script src="${basePath}/js/jquery.masonry.min.js"></script>

<script src="${basePath}/js/jquery.knob.modified.js"></script>

<script src="${basePath}/js/jquery.sparkline.min.js"></script>

<script src="${basePath}/js/counter.js"></script>

<script src="${basePath}/js/retina.js"></script>

<script src="${basePath}/js/custom.js"></script>

<!-- end: JavaScript-->
<script type="text/javascript">
    $(function () {
        $("#uploadify").uploadify({
            //注:这里uploadify为html标签，文件输入框id
            swf: 'css/uploadify.swf',
            uploader: '${basePath}/mortgageDeductionController/upload.do',
            cancelImg: '/img/uploadify-cancel.png',
            buttonText: '浏览',
            fileObjName: 'uploadFile',
            auto: true,
            multi: true,
            Debug: true,
            queueID: 'fileQueue',//这里fileQueue 为上传进度条显示在哪个div里
            removeCompleted: true,
            queueSizeLimit: 999,
            fileSizeLimit: '100MB',
            fileTypeExts: '*.xls',
            height: 30,
            width: 190,
            successTimeout: 30,
            requeueErrors: true,
            uploadLimit: 100,
            onUploadSuccess: function (file, data, response) {
                if ("1" == data) {
                    $("#successBtn").click();
                } else {
                    $("#failBtn").click();
                }
            },
        });
        <%--$("#fileInputForm").submit(function () {--%>
        <%--var form = new FormData(document.getElementById("fileInputForm"));--%>
        <%--$.ajax({--%>
        <%--url: "${basePath}/mortgageDeductionUploadController/upload.do",--%>
        <%--type: "POST",--%>
        <%--data: form,--%>
        <%--processData: false,--%>
        <%--contentType: false,--%>
        <%--success: function (data) {--%>
        <%--$("#fileInput").after("<span>" + data + "</span>")--%>
        <%--},--%>
        <%--error: function (e) {--%>

        <%--}--%>
        <%--});--%>
        <%--})--%>
    })
</script>
</head>
</body>
</html>
