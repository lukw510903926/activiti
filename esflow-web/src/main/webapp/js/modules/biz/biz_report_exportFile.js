$.namespace("biz.report.exportFile");

biz.report.exportFile = {
		
	exportDetail : function() {
		var param = {};
		param.action = action;
		param.field = field;
		param.status = status;
        param.taskDefKey = taskDefKey;
        param.taskAssignee = taskAssignee;
        param.systemName = systemName;
		$("#biz-query-form").find("[name]").each(function() {
			var value = $.trim($(this).val());
			if (value != null && value != "" && value != "undefined") {
				param[this.name] = value;
			}
		});
		var url = path + "/bizReport/exportFile";
		if($.trim(field)!=''){
			url =  path + "/bizReport/exportReport";
		}else if(action=='isShuffle'){
			url = path + "/workflow/exportWorkOrder";
			param.bizType = '事件管理';
			param.isShuffle = '1';
		}else if(action=='leftBiz'){
			delete  param.systemName;
			delete  param.compact;
			url = path + "/workflow/exportWorkOrder";
			param.bizType = '故障管理';
			param.isShuffle = '1';
		}
		post(url,param);
		var temp = document.createElement("form");
	}
}
function post(url, params) {
    var temp = document.createElement("form");
    temp.action = url;
    temp.method = "post";
    temp.style.display = "none";
    for (var x in params) {
        var opt = document.createElement("input");
        opt.name = x;
        opt.value = params[x];
        temp.appendChild(opt);
    }
    document.body.appendChild(temp);
    temp.submit();
}
