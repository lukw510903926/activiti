<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>发起工单</title>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript">
	var key = "${key}"; // "eventWorkOrder"
	var createUser = ${createUser};
	var bizId = "${bizId}";
	var id = '';
	var countNum = "${countNum}";
	var cartId = "${cartId}";
</script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_edit_form.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_edit_paramfunc.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_show_table.js"></script>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_create.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
	  $(".js-example-basic-single").select2();
	});
</script>
<style>
/*input[type='text'],select{
	line-height: 16px;
	width: 70%;
}
.modal-header {
	padding: 0px;
}

.modal-body {
	padding: 0px;
}

.modal-footer {
	padding: 0px;
	border: 0;
} */
.Wdate{
	line-height: 20px;
	height:28px;
}
.listtable > tbody > tr > th{
	width: 15%;
}
.listtable > tbody > tr > td{
	width: 35%;
}
</style>
</head>
<body style="padding: 10px 150px;">
	<form id="form" method="post" enctype="multipart/form-data">
		<input type="hidden" id="base_tempID" name="base.tempID"/>
		<!-- <input type="file" name="files" style="display: none;"> -->
		<input type="hidden" name="uploadDesc"/>
		<input type="hidden" name="startProc"/>
		<input type="hidden" name="base.handleName" value="发起工单"/>
		<input type="hidden" name="base.buttonId"/>
		<input type="hidden" name="base.handleResult"/>
		<input type="hidden" name="configVid"/>
		<input type="hidden" name="configList"/>
		<input type="hidden" name="changeItemList"/>
		<input type="hidden" name="shopCartList"/>
		<input type="hidden" name="recyleProductList"/>
		<div class="t_content">
			<div class="wb_msg" style="height: auto;">
				<div class="wb_msg_all">
					<div class="wb_tit">
						<i id="msgtitle">报障人信息</i>
					</div>
					<table id="bjrxx" cellpadding="0" cellspacing="0" class="infor_table">
					</table>
				</div>
			</div>
		</div>
	</form>
</body>