<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>执行代扣</title>
    <jsp:include page="../../common/include.jsp"></jsp:include>
    <link href="${basePath}/css/bootstrap-table.css" rel="stylesheet">
    <link href="${basePath}/css/bootstrap-datetimepicker.min.css" rel="stylesheet">

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
                <li><a href="main.jap">代扣</a></li>
            </ul>

            <div class="row-fluid sortable">
                <div class="box span12">
                    <div class="box-header" data-original-title>
                        <h2><i class="halflings-icon white user"></i><span class="break"></span>Members</h2>
                    </div>
                    <div class="box-content">
                        <div>
                            <form class="form-horizontal" id="form">
                                <table class="table-bordered">
                                    <tr with="33%">
                                        <td>开始日期</td>
                                        <td><input size="16" type="text" id="datetimeStart" class="form_datetime"
                                                   name="datetimeStart"/></td>
                                        <td>结束日期</td>
                                        <td>
                                            <input size="16" type="text" id="datetimeEnd" class="form_datetime"
                                                   name="datetimeEnd"/></td>
                                        <td>客户名称</td>
                                        <td>
                                            <input type="text" class="" id="customerName" name="customerName"/></td>
                                        <td><input type="button"  id="queryBtn" value="查询" /></td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <table id="table"></table>
                    </div>
                </div><!--/span-->

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
<script src="${basePath}/js/bootstrap-table.js"></script>
<script src="${basePath}/js/bootstrap-table-zh-CN.js"></script>
<script src="${basePath}/js/bootstrap-datetimepicker.min.js"></script>
<script src="${basePath}/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${basePath}/js/jquery.serializejson.min.js"></script>

<!-- end: JavaScript-->
<script type="text/javascript">
        var  columns = [{
            field: 'id',
            title: 'id'
        }, {
            field: 'contractNo',
            title: '合同编号'
        }, {
            field: 'customerName',
            title: '客户名称'
        }, {
            field: 'Param1',
            title: '开户行'
        }, {
            field: 'Param2',
            title: '卡折标志'
        }, {
            field: 'Param3',
            title: '银行卡号'
        }, {
            field: 'Param4',
            title: '持卡人姓名'
        }, {
            field: 'Param5',
            title: '证件类型'
        }, {
            field: 'Param6',
            title: '证件号码'
        }, {
            field: 'splitData1',
            title: '分账数据1'
        }, {
            field: 'splitData2',
            title: '分账数据2'
        }, {
            field: 'splitData2',
            title: '服务费管理公司'
        }];
    $(function () {
        $('#table').bootstrapTable({
            sidePagination: "server",
            cache: false,
            height:600,
            pagination: true,
            columns: columns
        });
        
        $('#table').bootstrapTable('hideColumn', 'id');
        $("#datetimeStart").datetimepicker({
            format: 'yyyy-mm-dd',
            minView: 'month',
            language: 'zh-CN',
            autoclose: true,
            startDate: new Date('1900:01:01')
        }).on("click", function () {
            $("#datetimeStart").datetimepicker("setEndDate", $("#datetimeEnd").val())
        });
        $("#datetimeEnd").datetimepicker({
            format: 'yyyy-mm-dd',
            minView: 'month',
            language: 'zh-CN',
            autoclose: true,
            startDate: new Date('1900:01:01')
        }).on("click", function () {
            $("#datetimeEnd").datetimepicker("setStartDate", $("#datetimeStart").val())
        });
        $("#queryBtn").click(function () {
            var cd =$("#form").serializeJSON();
            $.ajax({
                url:"${bathPath}/mortgageDeductionController/queryDeductionResult.do",
                type:"POST",
                data:$("#form").serializeJSON(),
                dataType: "json",
                success:function(data){
                    $('#table').bootstrapTable("load",data);
                }

            })
        })
        
        
    })

</script>
</head>
</body>
</html>
