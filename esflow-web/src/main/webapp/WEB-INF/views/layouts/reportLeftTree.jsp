<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/pages/include/taglib.jsp"%>
<script type="text/javascript">
$(function() {
	
	var bdcb_id = $("#navListId a :last").attr('id');
	if(bdcb_id) {
		var tree_bdcb = $("#tree_" + bdcb_id);
		if(tree_bdcb){
			tree_bdcb.addClass("on");
		}
	}
});
</script>
<div class="con-left2">
  <script type="text/javascript">
  	function extractor(html){
		var pattern = /<body[^>]*>((.|[\n\r])*)<\/body>/im;//只有body内的html有效
		var body = pattern.exec(html);
		if (body) {
			return body[1];
		} else {
			return html;
		}
	}
	function load(url,el){
		$.ajax({
			url : path+ url,
			cache : false,
			dataType : "html",
			success : function(data){
				$("#content").empty();
				$("#content").append(extractor($(data).find("#content")));
				if(el){
					$(".sidebar-nav1 li").removeClass("active");
					$(".sidebar-nav3 li").removeClass("active");
					$(el).parent().addClass("active");
					$(el).parent().parent().parent().addClass("active");
				}
			}
		})
	}
	$(function(){
		$(".mu").click(function(){
			$(this).find("span > i").toggleClass("icon-chevron-up");
			$(this).find("span > i").toggleClass("icon-chevron-down");
		})
	})
</script>
  	<!--一级菜单-->
	<ul class="list-unstyled sidebar-nav1">
		<li class="active">
	        <a class="mu collapsed" data-toggle="collapse" href="#ul0"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>故障管理</a>
			<ul id="ul0" class="list-unstyled sidebar-nav3 collapse in"">
				<li class="active"><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=leftBiz",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>遗留工单</a></li>
			</ul>
		</li>
		<li>
	        <a class="mu collapsed" data-toggle="collapse" href="#ul1"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>事件管理</a>
			<ul id="ul1" class="list-unstyled sidebar-nav3 collapse in">
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=isShuffle",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>推诿单</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=systemEff",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统效能统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=systemReject",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统驳回统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=userSatis",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>用户满意度统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=handleTime",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>处理时间统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=support",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统支持方式统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=delay",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统延期工单统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=reportSource",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统报障来源统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=systemStatus",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统事件工单统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=intercept",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统事件工单拦截统计</a></li>
				<li><a class="mu3" onclick='load("/bizReport/index?decoratorType=report&action=timeout",this)'  href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>各系统事件工单超时统计</a></li>
			</ul>
		</li>
	</ul>
</div>