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
var userName = '${userName}';
$(function() {
	$("#process").addClass("on");
});
</script>
</head>
<body >
<nav class="navbar top-navbar">
  <div class="navbar-header"> <a class="navbar-brand" href="${base}/cmdb-web"></a> </div>
  <div class="pull-right">
    <ul class="list-unstyled header-func">
      <li class="head-member" ><span class="head-member-ico mrr5"></span>${deptment }</li>
      <li><a href="${base}/ipnet-kb/doc"><i class="head-ico1"></i></a></li>
      <li><a href="${base}/cmdb-web/index/secure"><i class="head-ico2"></i></a></li>
      <li><a href="javascript:exit();"><i class="head-ico3"></i></a></li>
    </ul>
  </div>
</nav>
<div class="top-menu-wp">
  <ul id="top-menu" class="list-unstyled clearfix top-menu">
    <li id="index"><a href="${base}/cmdb-web" class="top-mu">首页</a></li>
    <li  class="active"><a href="${base}/esflow/biz?action=myWork" class="top-mu">运维管理</a></li>
    <li id="fm"><a href="${base }/cmdb-web/index/autoManage/alarm" class="top-mu">保障管理</a>
	    <ul id="fm-ul" class="list-unstyled clearfix sub-menu">
	       <li id="mo"><a href="${base }/cmdb-web/index/autoManage/alarm" class="leaf-mu">告警监控</a></li>
	       <li id="am"><a href="${base }/cmdb-web/index/autoManage/am" class="leaf-mu">性能监控</a></li>
	       <li id="pa"><a href="${base }/cmdb-web/index/autoManage/patrol" class="leaf-mu">智能巡检</a></li>
	       <li id="di"><a href="${base }/cmdb-web/index/autoManage/dialTest" class="leaf-mu">拨测管理</a></li>
	     </ul>
    </li>
    <li><a href="${base }/cmdb-web/index/ipnetReport" class="top-mu">运维分析</a></li>
    <li><a href="${base }/cmdb-web/cim/index" class="top-mu">配置管理</a></li>
     <li><a href="${unmpUrl}/RequirementManage/Index.aspx" target="_blank" class="top-mu">项目管理</a>
      <ul class="list-unstyled clearfix sub-menu">
        <li><a href="${unmpUrl}/RequirementManage/Index.aspx" target="_blank" class="leaf-mu">需求管控</a></li>
        <li><a href="${unmpUrl}/RequirementManage/FB/VersionView.aspx?1=2" target="_blank" class="leaf-mu">版本发布管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/Week/WeeklyReportsView.aspx?1=2" target="_blank" class="leaf-mu">周报管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/YearReq/YearReq_RequirementInfoList.aspx?1=1"  target="_blank" class="leaf-mu">年度需求管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/Assessment/AssessmentPrjectList.aspx?1=1" target="_blank" class="leaf-mu">后评估管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/Cost/PartnerQualificationList.aspx" target="_blank" class="leaf-mu">成本管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/UserMgr/MyUserInfo.aspx?1=1" target="_blank" class="leaf-mu">我的信息</a></li>
        <li><a href="${unmpUrl}/WinService/WinServiceView.aspx?1=2" target="_blank" class="leaf-mu">后台配置</a></li>
        <li><a href="${unmpUrl}/RequirementManage/Info/InfoView.aspx?1=1" target="_blank" class="leaf-mu">信息发布管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/KPI/MonthlyList.aspx?wfid=DDD648C9B2664DD78918F3AABA9C5C97&1=1" target="_blank" class="leaf-mu">厂家考核管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/Company/TaskPlanEmployeeView.aspx?1=2" target="_blank" class="leaf-mu">团队成员管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/FM/FileManageView.aspx?1=2" target="_blank" class="leaf-mu">文档管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/MS/PersonMilepostInfoList.aspx?type=1" target="_blank" class="leaf-mu">里程碑管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/FPA/FPAPlanList.aspx?1=1" target="_blank" class="leaf-mu">需求基线管理</a></li>
        <li><a href="${unmpUrl}/RequirementManage/task/FillTaskList.aspx?1=1" target="_blank" class="leaf-mu">运维通报管理</a></li>
      </ul>
    </li>
  </ul>
</div>
<div>
<ol class="breadcrumb">
  <li><i class="ico-index mrr5"></i>您现在的位置</li>
  <li><a href="${base }/cmdb-web">首页</a></li>
  <li class="active"><a href="${ctx }">运维管理</a></li>
</ol>
	<div class="con-main" id="mainDiv">
		<c:choose>
			<c:when test="${decoratorType=='report' }">
				<jsp:include page="reportLeftTree.jsp"></jsp:include >
			</c:when>
			<c:otherwise>
				<jsp:include page="publicLeftTree.jsp"></jsp:include>
			</c:otherwise>
		</c:choose>
		<div class="con-right2">
			<div id="content">
				<sitemesh:body />
			</div>
		</div>
	</div>
</div>
</body>
</html>