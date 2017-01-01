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
	
	$("#treeMenu ul li a").click(function() {
		changePullStatu($(this));
		if($(this).children("span").attr("class")=="down"){
			$(this).children("span").attr("class","up");
		}else{
			$(this).children("span").attr("class","down");
		}
		$(this).next("ul").toggle();
	});
	
	$("#treeMenu h3").click(function() {
		if($(this).children(".down").length>0){
			$(this).children("span.down").attr("class","up");
		}else{
			$(this).children("span.up").attr("class","down");
		}
		$(this).next("ul").toggle();
	});
	
	$("ul.submenu2 li").click(function() {
		if($(this).attr("class")!="on"){
			$("ul.submenu2 li").removeAttr("class");
			$(this).attr("class","on");
		}
	});
	
	function changePullStatu(ele) {
		var sp = $($(ele.children("a").get(0)).children("span").get(0));
		var cls = sp.attr("class");
		if(cls === "pull") {
			sp.attr("class","pulled");
		} else if(cls === "pulled") {
			sp.attr("class", "pull");
		} 
	}
});
</script>
<div class="main_left">
	<div id="treeMenu" class="left_con">
		
		<h3 class="tit">
			<span class="deploy2_icon"></span>交维管理<span class="up"></span>
		</h3>
		<ul class="submenu">
        	<li><a>公共操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                    <li id='tree_bdcb_list'><a href="${pageContext.request.contextPath}/biz/create/maintainManagement" target="_blank">发起工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement">查询工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&user=${userName}&status=草稿">我的草稿</a></li>
            	</ul>
            </li>
            <li><a>普通用户操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myWork&taskAssignee=${userName}">我的待办</a></li>
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myHandle&status=项目负责人&taskAssignee=${userName}">局方项目负责人审批</a></li>
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myHandle&status=维护负责人&taskAssignee=${userName}">局方维护负责人审批</a></li>
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myHandle&status=第三方维护&taskAssignee=${userName}">第三方运维厂商审批</a></li>
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myWork&status=确认文档入库&taskAssignee=${userName}">确认文档入库</a></li>
                	<li><a href="${pageContext.request.contextPath}/biz?type=maintainManagement&action=myHandle&status=用户关闭&taskAssignee=${userName}">用户关闭</a></li>
                </ul>
            </li>
             <li><a>相关操作<span class="up"></span></a>
            	<ul class="submenu2">
                	<li><a href="${pageContext.request.contextPath}/serviceRoleConf/userconf?type=maintainManagement&userType=officialType">局方维护负责人配置</a></li>
                	<li><a href="${pageContext.request.contextPath}/serviceRoleConf/otherMaintainer?type=maintainManagement&userType=otherType">第三方运维厂商配置</a></li>
                </ul>
            </li>
		</ul>
	</div>
</div>
