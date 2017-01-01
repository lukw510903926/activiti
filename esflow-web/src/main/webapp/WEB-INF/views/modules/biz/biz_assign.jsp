<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="${ctx}">
<title>工单流转</title>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript" src="${ctx}/js/modules/biz/biz_assign.js"></script>
<script type="text/javascript">
	var id = "${id}";
</script>
<style>
input[type='text'],select{
	line-height: 16px;
	width: 70%;
}
.assign_table{
	border: 1px solid #e7ecf2;
}
.assign_table th{
	width: 15%;
	text-align: right;
}
.select_yes{
	margin: 4px 0 0 0;
}
.select_no{
	margin: 4px 0 0 2px;
}
</style>
</head>
<body style="padding: 10px 200px 0 200px;">
<div class="import_form">
                <h2 class="white_tit"><span class="white_tit_icon"></span>角色信息</h2>
            </div>
	<div class="ntb" style="padding:10px;">
	<table cellpadding="0" cellspacing="0" width="100%" border="1" class="assign_table">
	<tr>
	<th>服务厂商列表：</th>
	<td colspan="3">
		<table cellpadding="5" cellspacing="0" width="100%">
			<tr>
				<td>
					<div class="import_form">
						<h2 class="white_tit">
							<span class="white_tit_icon"></span>可选服务厂商列表：
						</h2>
					</div>
					<div class="ntb">
						<div class="user_select">内容</div>
					</div>
				</td>
				<td width="35">
				<center>
				<a href="#" class="select_btn"><span
						class="select_yes"></span></a> <a href="#" class="select_btn mrt10"><span
						class="select_no"></span></a>
						</center>
						</td>
				<td>
					<div class="import_form">
						<h2 class="white_tit">
							<span class="white_tit_icon"></span>已选服务厂商列表：
						</h2>
					</div>
					<div class="ntb">
						<div class="user_select">内容</div>
					</div>
				</td>
			</tr>
		</table>
	</td>
	</tr>
	<tr>
	<th>地市公司：</th>
	<td width="35%"><select></select></td>
	<th>业务类型：</th>
	<td><select></select></td>
	</tr>
	<tr>
	<th>服务名称：</th>
	<td><select></select></td>
	<th></th>
	<td></td>
	</tr>
	<tr>
	<th>工作内容：</th>
	<td colspan="3"><textarea style="width: 100%" rows="3"></textarea></td>
	</tr>
	</table>
		<div class="btn_list">
			<a href="#" class="yes_btn">提交</a><a href="#" class="mrl10">取消</a>
		</div>
	</div>
</body>