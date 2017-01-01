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
	$('#bizCount').on('click',function(){window.open(path +'/bizReport/index');})
});
</script>
<script type="text/javascript" src="${ctx}/js/modules/it/it_product_cart.js"></script>
<style type="text/css">
.submenu a {
    border-bottom: 1px solid #bdc6cf;
    font-size: 14px;
    line-height: 30px;
    background: url(../img/submenu.png) no-repeat 10px 12px;
    background: #ecf0f5;
    padding-left: 20px;
    display: block;
    position: relative;
}
</style>
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
        <a class="mu collapsed" data-toggle="collapse" href="#ul0"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>通用功能</a>
        <!--三级菜单-->
	    <ul id="ul0" class="list-unstyled sidebar-nav3 collapse in">
				<li class="active"><a class="mu3" onclick='load("/biz?action=myWork",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>统一待办</a></li>
				<li><a class="mu3" onclick='load("/biz?action=myTemp",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>工单草稿</a></li>
				<li><a class="mu3" onclick='load("/biz?action=myCreate",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>已创建工单</a></li>
				<li><a class="mu3" onclick='load("/biz?action=myHandle",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>已处理工单</a></li>
				<li><a class="mu3" onclick='load("/biz",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>工单查询</a></li>
		<%-- 	<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.统一待办')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单草稿')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.已创建工单')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.已处理工单')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单查询')}">
			</c:if>
			--%>
		</ul>
      </li>
      <li>
        <a class="mu collapsed" data-toggle="collapse" href="#ul1"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>服务台待办</a>
        <ul id="ul1" class="list-unstyled sidebar-nav3 collapse in">
			<li><a class="mu3" onclick='load("/biz?action=myEventService",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>我的待办</a></li>
			<li><a class="mu3" onclick='load("/biz?action=myIntercept",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>我的拦截</a></li>
			<li><a class="mu3" onclick='load("/biz?action=myEventQA",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>我的质检</a></li>
			<li><a class="mu3" onclick='load("/biz?action=myEventHandle",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>我的督办</a></li>
			<li><a class="mu3" onclick='load("/biz/workload?type=service",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>工作量评估管理</a></li>
			<%-- <c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.我的待办')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.我的拦截')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.我的质检')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.我的督办')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.工作量评估管理')}">
			</c:if> --%>
        </ul>
      </li>
      <li>
        <a class="mu collapsed" data-toggle="collapse" href="#ul2"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>厂家待办</a>
        <ul id="ul2" class="list-unstyled sidebar-nav3 collapse in">
				<li><a class="mu3" onclick='load("/biz?action=venderHandle",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>我的待办</a></li>
				<li><a class="mu3" onclick='load("/biz/workload?type=vender",this)' href="javascript:void(0)"><i class="ico-dot2 mrr5"></i>工作量评估管理</a></li>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.我的待办')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.事件管理.工作量评估管理')}">
			</c:if>
        </ul>
      </li>
	  <li>
		<a class="mu collapsed" data-toggle="collapse"  href="#ul3"><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>IT超市</a>
	  	<ul id="ul3" class="list-unstyled sidebar-nav2 collapse in">
				<li><a class="mu2"  href="${pageContext.request.contextPath}/market/index" target="_blank"><i class="ico-dot mrr5"></i>IT超市申请</a></li>
				<li><a data-toggle="modal" class="mu2" onclick="checkBuyCar()" data-target="#shopCar"><i class="ico-dot mrr5"></i>我的购物车</a></li>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.IT超市.IT超市配置')}">
				<li><a class="mu2"  href="${pageContext.request.contextPath}/it/index" target="_blank"><i class="ico-dot mrr5"></i>IT超市配置</a></li>
			</c:if>
				<li><a class="mu2"  href="${pageContext.request.contextPath}/it/productDocument" target="_blank"><i class="ico-dot mrr5"></i>IT超市指引文档</a></li>
		  	<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.IT超市.IT超市申请')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.IT超市.IT超市回收1')}">
				<li><a class="mu2"  href="${pageContext.request.contextPath}/biz/create/itSuperMarketRecyced" target="_blank"><i class="ico-dot mrr5"></i>IT超市回收</a></li>
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.IT超市.购物车')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.IT超市.指引文档')}">
			</c:if>
		</ul>
	  </li>
      <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul4"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程创建</a>
		<ul id="ul4" class="list-unstyled sidebar-nav2 collapse">
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/eventManagement" target="_blank"><i class="ico-dot mrr5"></i>事件管理</a></li>
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/maintainManagement" target="_blank"><i class="ico-dot mrr5"></i>交维管理</a></li>
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/changeManagement" target="_blank"><i class="ico-dot mrr5"></i>变更管理</a></li>
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/problemManagement" target="_blank"><i class="ico-dot mrr5"></i>问题管理</a></li>
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/faultManagement" target="_blank"><i class="ico-dot mrr5"></i>故障管理</a></li>
				<li><a class="mu2" href="${pageContext.request.contextPath}/biz/create/nonFunctionalAcceptanceManagement" target="_blank"><i class="ico-dot mrr5"></i>非功能点验收</a></li>
			<%-- <c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.事件管理')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.交维管理')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.变更管理')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.问题管理')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.故障管理')}">
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单发起.非功能点验收')}">
			</c:if> --%>
		</ul>
      </li>
	  <li>
	  	<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.工单统计')}">
		    <a class="mu collapsed" data-toggle="collapse" id='bizCount'><i class="ico-menu mrr5" ></i>工单统计</a>
		</c:if>
		<a class="mu collapsed" data-toggle="collapse" href="#ul5"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程管理</a>
		<ul id="ul5" class="list-unstyled sidebar-nav2 collapse">
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.模型列表')}">
				<li><a class="mu2" onclick="load('/model',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>模型列表</a></li>
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程列表')}">
				<li><a class="mu2" onclick="load('/process/',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>流程列表</a></li>
			</c:if>
		</ul>
	  </li>
	  <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul6"><span class="pull-right"><i class="icon-chevron-down"></i></span><i class="ico-menu mrr5"></i>运维流程配置</a>
	  	<ul id="ul6" class="list-unstyled sidebar-nav2 collapse">
	  		<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程配置.字典管理')}">
				<li><a class="mu2" onclick="load('/dict/config',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>字典管理</a></li>
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程配置.模版管理')}">
				<li><a class="mu2" onclick="load('/bizTemplateFile/index',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>模版管理</a></li>
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程配置.角色配置')}">
				<li><a class="mu2" onclick="load('/serviceRoleConf/roleconf',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>角色配置</a>	</li>
			</c:if>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程配置.维护负责人配置')}">
				<li><a class="mu2" onclick="load('/serviceRoleConf/userconf',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>维护负责人配置</a></li>
			</c:if>
	          <li><a class="mu2" onclick="load('/contract/contract',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>网管维保合同管理</a></li>
			<c:if test="${fns:authenticate(resList,'运维管理平台.esflow.流程配置.第三方厂商')}">
	            <li><a class="mu2" onclick="load('/serviceRoleConf/otherMaintainer',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>第三方运维厂商配置</a></li>
	        </c:if>
		</ul>
	  </li>
	  <li>
		<a class="mu collapsed" data-toggle="collapse" href="#ul7" aria-expanded='true'><span class="pull-right"><i class="icon-chevron-up"></i></span><i class="ico-menu mrr5"></i>服务台</a>
	  	<ul id="ul7" class="list-unstyled sidebar-nav2 collapse in" aria-expanded='true'>
	  		<li><a class="mu2" onclick="load('/index/serviceIntro',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>服务台介绍</a></li>
	  		<li><a class="mu2" onclick="load('/index/contact',this)" href="javascript:void(0)"><i class="ico-dot mrr5"></i>联系方式</a></li>
	  	</ul>
	  </li>  
    </ul>
  </div>
  <!--弹出框-->
  <div class="modal fade" id="shopCar">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
          <h5 class="modal-title">购物车</h5>
        </div>
        <div class="modal-body">
          <div class="base-table-wrap" style="border:1px solid #ddd;">
          	  <table id="dataTable" class="shop-car" class="table base-table shop-car"></table>
          </div>
          <div class="clearfix shop-car-page">
              <ul class="pagination pagination-sm pull-right" id = "ulPageination"> </ul>
          </div>
        </div>
        <div class="shop-btn-list">
			<a href="javascript:void(0)" onclick="goBuy()" class="btn btn-y mrr10">购买</a>
			<a href="#" class="btn btn-n" data-dismiss="modal">关闭</a>
		</div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->