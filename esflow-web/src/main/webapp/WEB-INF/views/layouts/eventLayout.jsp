<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<%@ include file="../include/param.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<head>
<title>网管开发与运维平台 <sitemesh:title /></title>
<%@include file="../include/head.jsp"%>
<script type="text/javascript" src="${ctx}/js/layouts/default.js"></script>
<sitemesh:head />
<script type="text/javascript">
$(function() {
	$("#auto").addClass("on");
});
</script>
</head>
<body >
<div id="header" class="header">
	<div class="container">
        <div class="top">
            <div class="con_menu right">
				<div class="mem_wrap right">
                    <a href="#" class="mem_center"><span class="m_icon"></span><em>${userName}</em><i class="dropdown"></i></a>
                    <div class="mem_hover">
                        <span class="arrow_point"></span>
                        <%-- <a href="${ctx}">返回首页</a> --%>
                        <a href="/cmdb-web/index/secure">系统管理</a>
                        <a href="javascript:exit();">注销登录</a>
                        <!-- <a href="#">我的信息</a> -->
                    </div>
                </div>
                <ul class="menu_ul right">
                    <li id="network"><a href="/cmdb-web" class="blue"><i class="n1"></i>网管开发管理</a></li>
                    <li id="auto"><a href="/cmdb-web/index/autoManage" class="blue"><i class="n2"></i>自动化运维管理</a></li>
                    <li class="cut_off">|</li>
                    <li id="process"><a href="/cmdb-web/index/processManage">流程管理</a></li>
                    <li id="security"><a href="/cmdb-web/index/securityManage">安全管控支撑</a></li>
                </ul>
                </ul>
            </div>
            <a href="${ctx}" class="logo"></a>
        </div>
    </div>
</div>
<div class="container">
	<div class="main" id="mainDiv">
	<c:choose>
		<c:when test="${type=='eventManagement' }">
			<jsp:include page="eventLeftTree.jsp"></jsp:include>
		</c:when>
		<c:when test="${type=='maintainManagement' }">
		<jsp:include page="maintainLeftTree.jsp"></jsp:include>
		</c:when>
		<c:when test="${type=='problemManagement' }">
		<jsp:include page="problemLeftTree.jsp"></jsp:include>
		</c:when>
		<c:when test="${type=='all' }">
		<jsp:include page="publicLeftTree.jsp"></jsp:include>
		</c:when>
	<c:otherwise>
		<jsp:include page="workLeftTree.jsp"></jsp:include>
	</c:otherwise>
	</c:choose>
		<div class="main_right">
			<div class="con_main" id="content">
				<sitemesh:body />
			</div>
		</div>
	</div>
</div>

</body>
</html>