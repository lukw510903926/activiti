<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="panel panel-box">
	<div class="panel-heading">
		查询条件
	</div>
	<div class="panel-body">
		<div class="mr5">
		<form class="form-horizontal" id="biz-query-form">
			<%-- <c:if test="${not empty handleUser }">
				<input type="hidden" id="handleUser" name="handleUser"
					value="${handleUser }">
			</c:if>
			<c:if test="${not empty taskDefKey }">
				<input type="hidden" id="taskDefKey" name="taskDefKey"
					value="${taskDefKey }">
			</c:if>
			<c:choose>
				<c:when test="${not empty type }">
					<input type="hidden" id="bizType" name="bizType" value="${workName }">
				</c:when>

			</c:choose>
			<c:choose>
				<c:when test="${not empty user && status=='草稿' }">
					<input type="hidden" id="createUser" name="createUser"
						value="${userName }">
					<input type="hidden" id="status" name="status" value="草稿">
					<div class="row">
						<div class="col-md-11">
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="title" class="col-md-4 control-label form-element">工单标题：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="title" name="title"
										placeholder="工单标题">
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">创建时间：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime"
										name="createTime" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">至：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime2"
										name="createTime2" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:when test="${status=='服务台关闭' }">
				<input type="hidden" id="status" name="status" value="服务台关闭">
				<div class="row">
						<div class="col-md-11">
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="bizId" class="col-md-4 control-label form-element">工单号：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="col-md-8 form-control" id="bizId"
										name="bizId" placeholder="工单号">
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="title" class="col-md-4 control-label form-element">工单标题：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="title" name="title"
										placeholder="工单标题">
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
									style="margin-bottom: 5px">
									<label for="createUser"
										class="col-md-4 control-label form-element">创建人：</label>
									<div class="col-md-8 form-element">
										<input type="text" class="form-control" id="createUser"
											name="createUser" placeholder="创建人">
									</div>
								</div>
						</div>
						<div class="col-md-1">
							<a data-toggle="collapse" data-parent="#accordion"
								href="#more-condition"> <span
								class="glyphicon glyphicon-collapse-down">更多</span>
							</a>
						</div>
					</div>
					<div class="col-md-11 panel-collapse collapse" id="more-condition">
					<div class="row">
						<div class="col-md-4 form-group form-element"
									style="margin-bottom: 5px">
									<label for="taskAssignee"
										class="col-md-4 control-label form-element">当前处理人：</label>
									<div class="col-md-8 form-element">
										<input type="text" class="form-control" id="taskAssignee"
											name="taskAssignee" placeholder="当前处理人">
									</div>
								</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">创建时间：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime"
										name="createTime" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">至：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime2"
										name="createTime2" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="row">
						<div class="col-md-11">
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="bizId" class="col-md-4 control-label form-element">工单号：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="col-md-8 form-control" id="bizId"
										name="bizId" placeholder="工单号">
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="title" class="col-md-4 control-label form-element">工单标题：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="title" name="title"
										placeholder="工单标题">
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="status" class="col-md-4 control-label form-element">工单状态：</label>
								<div class="col-md-8 form-element">
									<select class="form-control" name="status" id="status">
										<option value="ALL">--所有--</option>
										<c:forEach items="${statusList }" var="statusItem" >
										<c:forEach items="${statusItem }" var="statusmap" >
											<option value="${statusmap.key }"><c:out
													value="${statusmap.value }" /></option>
										</c:forEach>
										</c:forEach>
										<!-- <option value="temp">草稿</option>
										<option value="new">新建</option>
										<option value="handle">处理中</option>
										<option value="end">已关闭</option> -->
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-1">
							<a data-toggle="collapse" data-parent="#accordion"
								href="#more-condition"> <span
								class="glyphicon glyphicon-collapse-down">更多</span>
							</a>
						</div>
					</div>

					<div class="col-md-11 panel-collapse collapse" id="more-condition">
						<c:if test="${empty user && empty taskAssignee}">
							<div class="row">
							<c:if test="${empty type}">
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="bizType" class="col-md-4 control-label form-element">工单类型：</label>
								<div class="col-md-8 form-element">
									<select class="form-control" name="bizType" id="bizType"
										placeholder="工单类型">
										<option value="">--所有--</option>
										<c:forEach items="${ProcessMap }" var="item">
											<option value="${item.value }"><c:out
													value="${item.value }" /></option>
										</c:forEach>
									</select>
								</div>
							</div>
							</c:if>
								<div class="col-md-4 form-group form-element"
									style="margin-bottom: 5px">
									<label for="createUser"
										class="col-md-4 control-label form-element">创建人：</label>
									<div class="col-md-8 form-element">
										<input type="text" class="form-control" id="createUser"
											name="createUser" placeholder="创建人">
									</div>
								</div>
								<div class="col-md-4 form-group form-element"
									style="margin-bottom: 5px">
									<label for="taskAssignee"
										class="col-md-4 control-label form-element">当前处理人：</label>
									<div class="col-md-8 form-element">
										<input type="text" class="form-control" id="taskAssignee"
											name="taskAssignee" placeholder="当前处理人">
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${not empty user}">
							<input type="hidden" id="createUser" name="createUser"
								value="${user }">
						</c:if>
						<c:if test="${not empty taskAssignee}">
							<input type="hidden" id="taskAssignee" name="taskAssignee"
								value="${taskAssignee }">
							<input type="hidden" id="action" name="action" value="myWork">
						</c:if>
						<div class="row">
						<c:if test="${!(empty user && empty taskAssignee) && empty type}">
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="bizType" class="col-md-4 control-label form-element">工单类型：</label>
								<div class="col-md-8 form-element">
									<select class="form-control" name="bizType" id="bizType"
										placeholder="工单类型">
										<option value="">--所有--</option>
										<c:forEach items="${ProcessMap }" var="item">
											<option value="${item.value }"><c:out
													value="${item.value }" /></option>
										</c:forEach>
									</select>
								</div>
							</div>
							</c:if>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">创建时间：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime"
										name="createTime" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
							<div class="col-md-4 form-group form-element"
								style="margin-bottom: 5px">
								<label for="ud_order"
									class="col-md-4 control-label form-element">至：</label>
								<div class="col-md-8 form-element">
									<input type="text" class="form-control" id="createTime2"
										name="createTime2" readonly="readonly"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
								</div>
							</div>
						</div>
					</div>

				</c:otherwise>
			</c:choose> --%>
			<div class="col-xs-12 btn-list">
				<a id="queryBtn" class="btn btn-y">查询</a>
				<button type="reset" class="btn btn-n mrl10" onclick="biz.query.resetClick()">重置</button>
			</div>
		</form>
		</div>
	</div>
</div>