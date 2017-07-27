<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页面</title>
    <jsp:include page="common/include.jsp"></jsp:include>
    <link href="css/uploadify.css" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.addtabs.css" type="text/css" media="screen"/>


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
            <a class="brand" href="main__bak.jsp"><span>润通代扣系统</span></a>

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
                    <li><a class="list-group-item" data-addtab="mail"   data-url="resullt.jsp">导入账户信息</a></li>
                    <li><a class="list-group-item" data-addtab="mail"   data-url="main.jsp">代扣</a></li>
                    <li><a class="list-group-item" data-addtab="mail"    data-url="main.jsp">扣款结果统计</a></li>
                </ul>
                <%--<ul class="nav nav-tabs nav-stacked main-menu">--%>
                   <%--<li> <a class="list-group-item" data-addtab="mail" data-url="./ajax/mailbox.txt">mailbox</a></li>--%>
                    <%--<a class="list-group-item" data-addtab="mail1" data-url="./ajax/mailbox.txt" data-ajax="true">Use Ajax</a>--%>
                    <%--<a class="list-group-item" data-addtab="message" data-url="./ajax/mailbox.txt" data-content="Customize the content">--%>
                        <%--<i class="glyphicon glyphicon-bullhorn"></i>指定内容--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" data-addtab="setting" data-url="./ajax/mailbox.txt" data-title="Customize the title">--%>
                        <%--<i class="glyphicon glyphicon-cog"></i>指定标题--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" data-addtab="userMenu1" data-url="./ajax/mailbox.txt">--%>
                        <%--用户菜单1--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" data-addtab="userMenu2" data-url="./ajax/button.html">--%>
                        <%--用户菜单2--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" data-addtab="sina" data-url="http://sina.com.cn">--%>
                        <%--新浪--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" data-addtab="baidu" data-url="http://baidu.com">--%>
                        <%--百度--%>
                    <%--</a>--%>
                    <%--<a class="list-group-item" onclick="Addtabs.closeAll();">--%>
                        <%--关闭所有--%>
                    <%--</a>--%>
                <%--</ul>--%>
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
            <%--<ul class="breadcrumb">--%>
            <%--<li>--%>
            <%--<i class="icon-home"></i>--%>
            <%--<a href="main__bak.jsp">主页</a>--%>
            <%--<i class="icon-angle-right"></i>--%>
            <%--</li>--%>
            <%--<li><a href="main.jap">导入账户信息</a></li>--%>
            <%--</ul>--%>

            <%--<div class="row-fluid">--%>
            <%--<div class="span3 statbox" >--%>
            <%--<input type="file" name="uploadFile" id="uploadify" value="浏览"/>--%>
            <%--<div id="fileQueue" ></div>--%>
            <%--</div>--%>
            <%--</div>--%>
            <div class="col-sm-9">
                <div id="tabs">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active">
                            <a href="#home" aria-controls="home" role="tab" data-toggle="tab">导入账户信息</a></li>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="home">
                            <input type="file" name="uploadFile" id="uploadify" value="浏览"/>
                            <div id="fileQueue"></div>
                        </div>
                    </div>

                </div>
            </div>
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

<%--<script src="js/jquery-1.9.1.min.js"></script>--%>
<script src="js/jquery.min.js"></script>
<%--<script src="js/jquery-migrate-1.0.0.min.js"></script>--%>

<%--<script src="js/jquery-ui-1.10.0.custom.min.js"></script>--%>

<%--<script src="js/jquery.ui.touch-punch.js"></script>--%>

<%--<script src="js/modernizr.js"></script>--%>

<script src="js/bootstrap.min.js"></script>

<%--<script src="js/jquery.cookie.js"></script>--%>

<%--<script src='js/fullcalendar.min.js'></script>--%>

<%--<script src='js/jquery.dataTables.min.js'></script>--%>
<%--<script src="js/excanvas.js"></script>--%>
<%--<script src="js/jquery.flot.js"></script>--%>
<%--<script src="js/jquery.flot.pie.js"></script>--%>
<%--<script src="js/jquery.flot.stack.js"></script>--%>
<%--<script src="js/jquery.flot.resize.min.js"></script>--%>

<%--<script src="js/jquery.chosen.min.js"></script>--%>

<%--<script src="js/jquery.uniform.min.js"></script>--%>

<%--<script src="js/jquery.cleditor.min.js"></script>--%>

<%--<script src="js/jquery.noty.js"></script>--%>

<%--<script src="js/jquery.elfinder.min.js"></script>--%>

<%--<script src="js/jquery.raty.min.js"></script>--%>

<%--<script src="js/jquery.iphone.toggle.js"></script>--%>

<%--<script src="js/jquery.uploadify-3.1.min.js"></script>--%>

<%--<script src="js/jquery.gritter.min.js"></script>--%>

<%--<script src="js/jquery.imagesloaded.js"></script>--%>

<%--<script src="js/jquery.masonry.min.js"></script>--%>

<%--<script src="js/jquery.knob.modified.js"></script>--%>

<%--<script src="js/jquery.sparkline.min.js"></script>--%>

<%--<script src="js/counter.js"></script>--%>

<%--<script src="js/retina.js"></script>--%>

<%--<script src="js/custom.js"></script>--%>

<script src="js/bootstrap.addtabs.js"></script>
<!-- end: JavaScript-->
<script type="text/javascript">
    $(function () {
        $("#uploadify").uploadify({
            //注:这里uploadify为html标签，文件输入框id
            swf: 'css/uploadify.swf',
            uploader: '${basePath}/mortgageDeductionUploadController/upload.do',
            cancelImg: '/img/uploadify-cancel.png',
            buttonText: '<div>选择文件</div>',
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
                if (response) {
                    currentNode = data;
                }
            },
        });
    })
    $(function () {
        $.addtabs({
            iframeHeight: 320
        });
    })
</script>
</head>
</body>
</html>
