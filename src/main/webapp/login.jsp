<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
    <jsp:include page="common/include.jsp" ></jsp:include>
    <style type="text/css">
        body {
            background: url(img/bg-login.jpg) !important;
        }
    </style>

</head>
<body>
<div class="container-fluid-full">
<div class="row-fluid">

    <div class="row-fluid">
        <div class="login-box">
            <h2>登录润通代扣系统</h2>
            <form class="form-horizontal" action="${basePath}/login.do" method="post">
                <fieldset>

                    <div class="input-prepend" title="loginName">
                        <span class="add-on"><i class="halflings-icon user"></i></span>
                        <input class="input-large span10" name="username" id="username" type="text"
                               placeholder="请输入用户名"/>
                    </div>
                    <div class="clearfix"></div>

                    <div class="input-prepend" title="Password">
                        <span class="add-on"><i class="halflings-icon lock"></i></span>
                        <input class="input-large span10" name="password" id="password" type="password"
                               placeholder="请输入密码"/>
                    </div>
                    <div class="clearfix"></div>

                    <div class="button-login">
                        <button type="submit" class="btn btn-primary">Login</button>
                    </div>
                    <div class="clearfix"></div>
                </fieldset>
            </form>
        </div><!--/span-->
    </div><!--/row-->


</div><!--/.fluid-container-->

</div><!--/fluid-row-->
<!-- start: JavaScript-->

<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/jquery-migrate-1.0.0.min.js"></script>

<script src="js/jquery-ui-1.10.0.custom.min.js"></script>

<script src="js/jquery.ui.touch-punch.js"></script>

<script src="js/modernizr.js"></script>

<script src="js/bootstrap.min.js"></script>

<script src="js/jquery.cookie.js"></script>

<script src='js/fullcalendar.min.js'></script>

<script src='js/jquery.dataTables.min.js'></script>

<script src="js/excanvas.js"></script>
<script src="js/jquery.flot.js"></script>
<script src="js/jquery.flot.pie.js"></script>
<script src="js/jquery.flot.stack.js"></script>
<script src="js/jquery.flot.resize.min.js"></script>

<script src="js/jquery.chosen.min.js"></script>

<script src="js/jquery.uniform.min.js"></script>

<script src="js/jquery.cleditor.min.js"></script>

<script src="js/jquery.noty.js"></script>

<script src="js/jquery.elfinder.min.js"></script>

<script src="js/jquery.raty.min.js"></script>

<script src="js/jquery.iphone.toggle.js"></script>

<script src="js/jquery.gritter.min.js"></script>

<script src="js/jquery.imagesloaded.js"></script>

<script src="js/jquery.masonry.min.js"></script>

<script src="js/jquery.knob.modified.js"></script>

<script src="js/jquery.sparkline.min.js"></script>

<script src="js/counter.js"></script>

<script src="js/retina.js"></script>

<script src="js/custom.js"></script>
<!-- end: JavaScript-->
</body>
</html>
