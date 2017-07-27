<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>执行代扣</title>
    <jsp:include page="common/include.jsp" ></jsp:include>

<body>
<!-- start: Header -->
<div class="navbar">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
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
                    <li><a href="main.jsp"><i class="icon-list-alt"></i><span class="hidden-tablet"> 导入账户信息</span></a></li>
                    <li><a href="deduction.jsp"><i class="icon-align-justify"></i><span class="hidden-tablet"> 代扣</span></a></li>
                    <li><a href="result.jsp"><i class="icon-bar-chart"></i><span class="hidden-tablet"> 扣款结果统计</span></a></li>
                </ul>
            </div>
        </div>
        <!-- end: Main Menu -->

        <noscript>
            <div class="alert alert-block span10">
                <h4 class="alert-heading">Warning!</h4>
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a> enabled to use this site.</p>
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
                        <div class="box-icon">
                            <a href="#" class="btn-setting"><i class="halflings-icon white wrench"></i></a>
                            <a href="#" class="btn-minimize"><i class="halflings-icon white chevron-up"></i></a>
                            <a href="#" class="btn-close"><i class="halflings-icon white remove"></i></a>
                        </div>
                    </div>
                    <div class="box-content">
                        <table class="table table-striped table-bordered bootstrap-datatable datatable">
                            <thead>
                            <tr>
                                <th>Username</th>
                                <th>Date registered</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/02/01</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/02/01</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/21</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/21</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/08/23</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/08/23</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/06/01</td>
                                <td class="center">Admin</td>
                                <td class="center">
                                    <span class="label">Inactive</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/06/01</td>
                                <td class="center">Admin</td>
                                <td class="center">
                                    <span class="label">Inactive</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/02/01</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/02/01</td>
                                <td class="center">Admin</td>
                                <td class="center">
                                    <span class="label">Inactive</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/02/01</td>
                                <td class="center">Admin</td>
                                <td class="center">
                                    <span class="label">Inactive</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/21</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/01/21</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-success">Active</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/08/23</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/08/23</td>
                                <td class="center">Staff</td>
                                <td class="center">
                                    <span class="label label-important">Banned</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/06/01</td>
                                <td class="center">Admin</td>
                                <td class="center">
                                    <span class="label">Inactive</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td>Dennis Ji</td>
                                <td class="center">2012/03/01</td>
                                <td class="center">Member</td>
                                <td class="center">
                                    <span class="label label-warning">Pending</span>
                                </td>
                                <td class="center">
                                    <a class="btn btn-success" href="#">
                                        <i class="halflings-icon white zoom-in"></i>
                                    </a>
                                    <a class="btn btn-info" href="#">
                                        <i class="halflings-icon white edit"></i>
                                    </a>
                                    <a class="btn btn-danger" href="#">
                                        <i class="halflings-icon white trash"></i>

                                    </a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
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

<script src="js/jquery-1.9.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-table.js"></script>
<script src="js/bootstrap-table-zh-CN.js"></script>

<!-- end: JavaScript-->
<%--<script type="text/javascript">--%>
<%--</script>--%>
</head>
</body>
</html>
