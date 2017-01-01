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
	
	$("ul.submenu li a").click(function() {
		changePullStatu($(this));
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
	
	$("ul.submenu2 li").click(function() {
		if($(this).attr("class")!="on"){
			$("ul.submenu2 li").removeAttr("class");
			$(this).attr("class","on");
		}
	});
	
	var workName = "${workName }";
	if(workName.length>13){
		$(".deploy2_icon").after("<i title='"+workName+"'>"+workName.substring(0, 4)+"..."+workName.substring(workName.length-7)+"</i>");
	}else{
		$(".deploy2_icon").after(workName);
	}
});
</script>
<div class="main_left">
	<div id="treeMenu" class="left_con">
		
		<h3 class="tit">
			<span class="deploy2_icon"></span>
			
			<span class="up"></span>
		</h3>
		<ul class="submenu">
        	<li><a>公共操作菜单<span class="up"></span></a></li>
            	<ul class="submenu2">
                    <li id='tree_bdcb_list'><a href="${pageContext.request.contextPath}/biz/create/${type}" target="_blank">发起问题</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=${type}&handleUser=${userName}">我的已处理</a></li>
                    <li><a href="${pageContext.request.contextPath}/biz?type=${type}">查询工单</a></li>
            </ul>
            <li><a>问题经理待办<span class="up"></span></a></li>
            <ul class="submenu2">
                    <li><a href="${pageContext.request.contextPath}/biz?action=myWork&type=${type}&taskAssignee=${userName}">我的待办</a></li>
            </ul>
            <li><a>问题专家待办<span class="up"></span></a></li>
            <ul class="submenu2">
                    <li><a href="${pageContext.request.contextPath}/biz?type=${type}&status=问题专家会签&taskAssignee=${userName}">我的待办</a></li>
            </ul>
            <li><a>用户待办<span class="up"></span></a></li>
            <ul class="submenu2">
                    <li><a href="${pageContext.request.contextPath}/biz?type=${type}&status=用户确认方案&taskAssignee=${userName}">我的待办</a></li>
            </ul>
		</ul>
	</div>
</div>
