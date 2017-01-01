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
			<span class="deploy2_icon"></span>事件管理<span class="up"></span>
		</h3>
		<ul class="submenu">
        	<li><a>公共操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                    <li id='tree_bdcb_list'><a href="${pageContext.request.contextPath}/biz/create/eventManagement" target="_blank">发起工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement">查询工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&user=${userName}">我发起的工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&handleUser=${userName}">我已处理列表</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&user=${userName}&status=草稿">我的草稿</a></li>
            	</ul>
            </li>
            <li><a>服务台操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                	<li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=myWork&taskDefKey=服务台处理&taskAssignee=${userName}">我的待办</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=myIntercept&taskAssignee=${userName}">我的拦截</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&status=服务台关闭">我的质检</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=myHandle&status=厂商处理&supervision=true">我的督办</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=hastenOrder&status=厂商处理&reminders=true">催办工单</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz/workload?type=eventManagement">工作量评估管理</a></li>
                </ul>
            </li>
            <li><a>服务厂商操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                	<li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=myWork&taskDefKey=厂商处理&taskAssignee=${userName}">我的待办</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz/workload?type=eventManagement">工作量评估管理</a></li>
                </ul>
            </li>
            <li><a>普通用户操作菜单<span class="up"></span></a>
            	<ul class="submenu2">
                	<li><a href="${pageContext.request.contextPath}/biz?type=eventManagement&action=evnetUserTask&taskAssignee=${userName}">我的待办</a></li>
                </ul>
            </li>
		</ul>
	</div>
</div>
