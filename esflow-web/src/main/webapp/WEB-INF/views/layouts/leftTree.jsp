<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">
$(function() {
	
	var bdcb_id = $("#navListId a :last").attr('id');
	if(bdcb_id) {
		var tree_bdcb = $("#tree_" + bdcb_id);
		if(tree_bdcb){
			tree_bdcb.addClass("on");
		}
	}
	
	$("ul.submenu>li a").click(function() {
		if($(this).children("span").attr("class")=="down"){
			$(this).children("span").attr("class","up");
		}else{
			$(this).children("span").attr("class","down");
		}
		$(this).parent().next("ul").toggle();
	});
	
	$("#treeMenu h3").click(function() {
		if($(this).children(".down").length>0){
			$(this).children("span.down").attr("class","up");
		}else{
			$(this).children("span.up").attr("class","down");
		}
		$(this).next("ul").toggle();
	});
	
	$("ul.submenu2 li a").click(function() {
		var sp = $(this).children("span");
		var cls = sp.attr("class");
		if(cls === "pull") {
			sp.attr("class","pulled");
		} else if(cls === "pulled") {
			sp.attr("class", "pull");
		} 
		$(this).parent().next("ul").toggle();
	});
	
	$("ul.submenu>li a").click();
	$("ul.submenu4>li a").click();
});
</script>
<div class="main_left">
	<div id="treeMenu" class="left_con">
		
			<div id="treeMenu" class="left_con">
			<h3 class="tit">
				<span class="deploy_icon"></span>通用功能
			</h3>
            <ul class="submenu">
                <li>
                    <ul class="submenu2">
                        <li><a href="${pageContext.request.contextPath}/biz?action=myWork&taskAssignee=admin">统一待办</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz?action=myTemp&status=草稿">工单草稿</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz?action=myCreate&user=${userName}">已创建工单</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz?handleUser=${userName}">已处理工单</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz?taskAssignee=${userName}&status=已阅">我的待阅</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz?taskAssignee=${userName}&status=未阅">我的已阅</a></li>
                        <li><a href="${pageContext.request.contextPath}/biz">工单查询</a></li>
                    </ul>
                </li>
            </ul>
			<h3 class="tit">
				<span class="operation_icon"></span>服务流程创建
			</h3>
			<ul class="submenu">
				<li><a href="${pageContext.request.contextPath}/biz/create/eventManagement" target="_blank">事件管理</a></li>
				<li><a href="${pageContext.request.contextPath}/biz/create/maintainManagement" target="_blank">交维管理</a></li>
				<li><a href="${pageContext.request.contextPath}/biz/create/problemManagement" target="_blank">问题管理</a></li>
				<li><a href="${pageContext.request.contextPath}/biz/create/nonFunctionalAcceptanceManagement" target="_blank">非功能点验收</a></li>
				<li><a href="${pageContext.request.contextPath}/biz/create/faultManagement" target="_blank">故障管理</a></li>
				<li><a href="${pageContext.request.contextPath}/biz/create/versionReleaseManagement" target="_blank">版本发布管理</a></li>
			</ul>
			<h3 class="tit">
				<span class="deploy2_icon"></span>流程管理<span class="up">
			</h3>
			<ul class="submenu">
				<li><a href="${pageContext.request.contextPath}/model">模型列表</a></li>
				<li><a href="${pageContext.request.contextPath}/process/">流程列表</a></li>
			</ul>
		<h3 class="tit">
			<span class="deploy2_icon"></span>配置管理<span class="up">
		</h3>
		<ul class="submenu">
			<li><a href="${pageContext.request.contextPath}/dict/config">字典管理</a></li>
			<li><a href="${pageContext.request.contextPath}/serviceRoleConf/roleconf">角色配置</a>	</li>
			<li><a href="${pageContext.request.contextPath}/serviceRoleConf/userconf?userType=officialType">局方维护负责人配置</a></li>
            <li><a href="${pageContext.request.contextPath}/serviceRoleConf/otherMaintainer?userType=otherType">第三方运维厂商配置</a></li>
		</ul>
	</div>
</div>
