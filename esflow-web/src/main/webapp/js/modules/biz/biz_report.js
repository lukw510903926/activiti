$.namespace("biz.report");
biz.report.query = {
	
	queryParam : {},
	// 初始化查询
	init : function() {
		biz.report.query.$queryForm = $('#biz-query-form');
		$("#queryBtn").click(biz.report.query.queryClick);
		$("#deletBiz").click(biz.report.query.removeBizInfo);
		$("#pressBiz").click(biz.report.query.terminalPressBiz);
		$('#bizType').change(biz.report.query.loadProcessStatus);
	},
	
	loadProcessStatus : function(){
		
		var bizType = $('#bizType').val();
		if(biz.report.query.form.queryParam.bizType){
			bizType = biz.report.query.form.queryParam.bizType;
		}
		$.ajax({
			url : path +'/biz/getProcessStatus',
			type : 'post',
			async: false, 
			dataType: 'json',
			data : {
				processName : bizType
			},
			success : function(data){
				if(data){
					var select = $('#status');
					select.empty();
					var df = $('<option>')
					df.val('');
					df.text('');
					select.append(df);
					statusList=data;
					for (var i = 0; i < data.length; i++) {
						var option = $('<option>')
						option.val(data[i]);
						option.text(data[i]);
						select.append(option);
					}
				}
				select.selectpicker('render');
				select.selectpicker('refresh');
			}
		});
	},
	// 执行查询
	queryClick : function() {
		biz.report.$table.bootstrapTable('refresh');
	},
	// 重置查询条件
	resetClick : function() {
		$('.selectpicker').selectpicker('val','');
	}
};

biz.report.query.form = {
	queryParam : {},
	systemList : new Array(),
	compactList : new Array(),
	loadSystem : function() {
		$.ajax({
			url : path + "/workflow/comboBoxList",
			type : "post",
			async : false,
			data : {
				selectName : "业务系统"
			},
			success : function(data) {
				biz.report.query.form.systemList = data
			}
		});
	},
	loadCompact : function() {
		$.ajax({
			url : path + "/workflow/dictComboBoxList",
			type : "post",
			async : false,
			data : {
				dictName : "网管网维保子项目"
			},
			success : function(data) {
				biz.report.query.form.compactList = data
			}
		});
	},
	init : function() {
		if(action == 'isShuffle'){
			$('#bizType').val('事件管理');
			biz.report.query.form.queryParam.bizType = '事件管理';
			biz.report.query.loadProcessStatus();
		}
		if(action != 'isShuffle'){
			$('#bizType').val('故障管理');
			biz.report.query.form.queryParam.bizType = '故障管理';
			biz.report.query.loadProcessStatus();
		}
		var queryFormList = [ {
			name : "bizId",
			align : 'center',
			text : "工单号",
			type : "text"
		}, {
			name : "title",
			align : 'center',
			text : "工单标题",
			type : "text"
		}, {
			name : "systemName",
			align : 'center',
			text : "所属系统",
			type : "combobox",
			params : {
				data : biz.report.query.form.systemList,
				type : "list"
			}
		}, {
			name : "compact",
			align : 'center',
			text : "所属合同",
			type : "combobox",
			params : {
				data : biz.report.query.form.compactList,
				type : "listmap",
				key : 'NAME',
				value : 'NAME'
			}
		}, {
			name : "status",
			align : 'center',
			text : "工单状态",
			type : "combobox",
			params : {
				data : statusList,
				type : "list"
			}
		}, {
			name : "createUser",
			align : 'center',
			text : "创建人",
			type : "text"
		}, {
			name : "createTime",
			align : 'center',
			text : "创建时间",
			type : "createTime"
		}, {
			name : "taskAssignee",
			align : 'center',
			text : "当前处理人",
			type : "text"
		} ];
		if (!action) {
			queryFormList[5].type = "hidden";
		}
		if(action == 'handleTime'){
			queryFormList.push( {name : "ignoreTime",align : 'center', text : "忽略时间",type : "createTime" });
		}
		if (action == 'leftBiz') {
			var temp = queryFormList[2];
			queryFormList[2] = queryFormList[5];
			queryFormList[5] = temp;
			queryFormList[3].type = "hidden";
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
		}
		if (action == 'isShuffle') {
			var temp = queryFormList[3];
			queryFormList[3] = queryFormList[7];
			queryFormList[7] = temp;
			queryFormList[7].type = "hidden";
		}
		if (action != 'leftBiz' && action != 'isShuffle' && field=='') {
			var createTime = queryFormList[0];
			queryFormList[0] = queryFormList[6];
			queryFormList[6] = createTime;
			
			var system = queryFormList[1];
			queryFormList[1] = queryFormList[2];
			queryFormList[2] = system;
			
			queryFormList[2].type = "hidden";
			queryFormList[3].type = "hidden";
			queryFormList[4].type = "hidden";
			queryFormList[5].type = "hidden";
			queryFormList[6].type = "hidden";
			queryFormList[7].type = "hidden";
		}
		if(field != ''){
			var system = queryFormList[2];
			queryFormList[2] = queryFormList[3];
			queryFormList[3] = system;
			queryFormList[3].type = "hidden";
		}
		var actionArray = ['intercept','systemEff', 'userSatis' ,'support','reportSource'];
		if($.inArray(action, actionArray) != -1){
			queryFormList[4].type = "hidden";
			queryFormList[7].type = "hidden";
		}
		if(action == 'systemStatus'&& field !='TOTAL'){
			queryFormList[4].type = "hidden";
		}
		biz.report.query.form.createForm(queryFormList);
	},
    createForm:function(list){
        var length = 0;
        for(var i=0;i<list.length;i++){
            if(list[i].type=="createTime")
                length = length+2;
            else if(list[i].type=="hidden")
                continue;
            else
                length++;
        }
        if(length<=3){
            biz.report.query.form.row = $("<div class='col-md-11'>");
            $("#queryBtn").parent().before(this.row);
        }else{
            var row = $("<div class='row'>");
            biz.report.query.form.row = $("<div class='col-md-11'>");
            row.append(this.row);
            row.append("<div class='col-md-1'><a data-toggle='collapse' data-parent='#accordion' href='#more-condition' style='line-height:26px;'>"
            +"更多<i class='mrl5 icon-double-angle-down'></i></a></div>");
            $("#queryBtn").parent().before(row);
        }
        this.loadForm(list);
    },
    loadForm:function(list){
        for(var i=0;i<list.length;i++){
            switch(list[i].type){
                case "text":
                    this.addTextField(list[i],biz.report.query.form.row);break;
                case "hidden":
                    this.addHidden(list[i],biz.report.query.form.row);break;
                case "combobox":
                    this.addCombobox(list[i],biz.report.query.form.row);break;
                case "createTime":
                    this.addCreateTime(list[i],biz.report.query.form.row);break;
                default:
                    this.addTextField(list[i],biz.report.query.form.row);
            }
        }
    },
    addTextField:function(data,row){
        if(row)
            biz.report.query.form.row = row;
        var col = $("<div class='col-md-4 form-group form-element' >");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var input = $("<input type='text' class='form-control' id='"+data.name+"' name='"+data.name+"' placeholder='"+data.text+"'>");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
    },
    addCreateTime:function(data,row){
        if(row)
            biz.report.query.form.row = row;
        if(this.row.children(".col-md-4").length>=2){
           if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
        var col = $("<div class='col-md-4 form-group form-element' >");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var input = $("<input type='text' class='form-control' id='"+data.name+"' name='"+data.name+"' readonly='readonly'>");
        input.attr("onFocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        col = $("<div class='col-md-4 form-group form-element' >");
        lable = "<label for='"+data.name+"2' class='col-md-1 control-label form-element' style='text-align:center'>至</label>";
        div = $("<div class='col-md-8 form-element'>");
        input = $("<input type='text' class='form-control' id='"+data.name+"2' name='"+data.name+"2' readonly='readonly'>");
        input.attr("onFocus","WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})");
        div.append(input);
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
    },
    addCombobox:function(data,row){
        if(row)
            biz.report.query.form.row = row;
        var col = $("<div class='col-md-4 form-group form-element' >");
        var lable = "<label for='"+data.name+"' class='col-md-4 control-label form-element'>"+data.text+"：</label>";
        var div = $("<div class='col-md-8 form-element'>");
        var select = $("<select type='text' data-width='100%' class='form-control selectpicker'  data-live-search='true'id='"+data.name+"' name='"+data.name+"' placeholder='"+data.text+"'>");
        div.append(select);
        select.append("<option value=''>请选择</option>");
        col.append(lable);
        col.append(div);
        this.row.append(col);
        if(this.row.children(".col-md-4").length==3){
            if(!this.collapse){
                this.collapse = $("<div class='col-md-11 panel-collapse collapse' id='more-condition'>");
                $("#queryBtn").parent().before(this.collapse);
            }
            this.row = $("<div class='row'>");
            this.collapse.append(this.row);
        }
        this.loadCombobox(select,data.params.data,data.params.type,data.params.key,data.params.value);
    },
    loadCombobox:function(select,params,type,key,value){
        for(var k in params){
            var option = $("<option>");
            if(type=="list"||type==undefined){
                option.val(params[k]);
                option.text(params[k]);
            }else if(type=="map"){
                if(key=="value")
                    option.val(params[k]);
                else
                    option.val(k);
                if(value=="key")
                    option.text(k);
                else
                    option.text(params[k]);
            }else if(type=="listmap"){
            	option.val(params[k][key]);
                option.text(params[k][value]);
            }else{
                if(key==true||key==undefined){
                    for(var i in params[k]){
                        option.val(i);
                        option.text(params[k][i]);
                    }
                }else if(key=="value"){
                    for(var i in params[k]){
                        option.val(params[k][i]);
                        option.text(params[k][i]);
                    }
                }else{
                    option.val(params[k][key]);
                    option.text(params[k][value]);
                }
            }
            select.append(option);
        }
        $('.selectpicker').selectpicker('render');
	    $('.selectpicker').selectpicker('refresh');
    },
    addHidden:function(data){
        var hidden = "<input type='hidden' name='"+data.name+"' value='"+data.value+"'/>";
        $("#biz-query-form").append(hidden);
    }
};

biz.report.table = {
	init : function() {
		var url = '';
		var columns  = new Array();
		if ($.trim(field) == '') {

			if (action == 'userSatis') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "GOOD",
					title : "好",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'GOOD';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "MIDDLE",
					title : "中",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'MIDDLE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "BAD",
					title : "差",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'BAD';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				});
			} else if (action == 'systemEff') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "REJECT",
					title : "退回",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'REJECT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "TIMEOUT",
					title : "超时",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TIMEOUT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "DELAY",
					title : "延期",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'DELAY';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "BAD",
					title : "差",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'BAD';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "TOTAL",
					title : "汇总",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				});
			} else if (action == 'timeout') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "FIRSTCOUNT",
					title : "一线超时",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'FIRSTCOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "SECONDCOUNT",
					title : "二线超时",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'SECONDCOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "THREECOUNT",
					title : "三线超时",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'THREECOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "TIMECOUNT",
					title : "超时",
					align : "center",
					formatter : function(value, row) {
						var total = row.FIRSTCOUNT + row.SECONDCOUNT	+ row.THREECOUNT;
						var systemName = row.SYSTEM;
						var field = 'TIMECOUNT';
						return '<a onclick=queryWorkOrder("' + systemName + '","' + field + '")>' + total + '</a>';
					}
				},{
					field : "TIMECOUNTPercent",
					title : "超时率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = (row.FIRSTCOUNT + row.SECONDCOUNT + row.THREECOUNT)/ row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				}, {
					field : "SHUFFLE",
					title : "推诿单",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'SHUFFLE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				});
			} else if (action == 'intercept') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "FIRSTCOUNT",
					title : "一线拦截",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'FIRSTCOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				},{
					field : "firstPercent",
					title : "一线拦截率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = row.FIRSTCOUNT/ row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				}, {
					field : "SECONDCOUNT",
					title : "二线拦截",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'SECONDCOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				},{
					field : "secondPercent",
					title : "二线拦截率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = row.SECONDCOUNT/ row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				}, {
					field : "THREECOUNT",
					title : "三线拦截",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'THREECOUNT';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				},{
					field : "threePercent",
					title : "三线拦截率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = row.THREECOUNT/ row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				});
			} else if (action == 'systemReject') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "USER",
					title : "用户驳回数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'USER';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "REJECTPERCENT",
					title : "用户驳回率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = row.USER / row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				}, {
					field : "VERDER",
					title : "二线驳回数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'VERDER';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "REJECTPERCENT",
					title : "二线驳回率",
					align : "center",
					formatter : function(value, row) {
						if (row.TOTAL) {
							var percent = row.VERDER / row.TOTAL;
							return (percent * 100).toFixed(2) + '%';
						}
						return '';
					}
				});
			} else if (action == 'systemStatus') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TEMP",
					title : "草稿",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TEMP';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "NEWBIZ",
					title : "新建",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'NEWBIZ';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "INTE",
					title : "服务台拦截",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'INTE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "SERVICE",
					title : "服务台处理",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'SERVICE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "VENDOR",
					title : "厂商处理",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'VENDOR';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "USER",
					title : "用户反馈",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'USER';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "CLOSE",
					title : "服务台关闭",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'CLOSE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "REBUILD",
					title : "重新提交",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'REBUILD';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "DELAYTASK",
					title : "延期申请确认",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'DELAYTASK';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "BIZEND",
					title : "已完成",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'BIZEND';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "TOTAL",
					title : "汇总",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				});
			} else if (action == 'reportSource') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "QQ",
					title : "QQ",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'QQ';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "ONESELF",
					title : "自监控",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'ONESELF';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "BIZ",
					title : "工单",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'BIZ';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "MOBILE",
					title : "电话",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'MOBILE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "EMAIL",
					title : "邮件",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'EMAIL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				})
			} else if (action == 'delay') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "DELAY",
					title : "延期工单数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'DELAY';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "DELAYS",
					title : "重复延期工单数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'DELAYS';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				})
			}else if (action == 'handleTime') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "WORKNUM",
					title : "工单号",
					align : "center",
					formatter : function(value, row, index) {
						var url = path + "/biz/" + row.BIZID;
						if (row.STATUS == "草稿"){
							url = path + "/biz/create/" + row.PROCESSDEFINITIONID.split(":")[0] + "?bizId=" + row.BIZID;
						}
						return "<a onclick=\"window.open('" + url + "');\">" + value + "</a>";
					}
				}, {
					field : "STATUS",
					title : "工单状态",
					align : "center"
				}, {
					field : "CREATETIME",
					title : "创建时间",
					align : "center"
				}, {
					field : "SERVICESIGN",
					title : "服务台签收时间",
					align : "center"
				}, {
					field : "SIGN",
					title : "服务台响应时长",
					align : "center"
				}, {
					field : "SERVICEHANDLE",
					title : "服务台处理时间",
					align : "center"
				}, {
					field : "SHANDLE",
					title : "服务台处理时长",
					align : "center"
				}, {
					field : "SERVICEASSIGN",
					title : "服务台分派时间",
					align : "center"
				}, {
					field : "ASSIGN",
					title : "服务台分派时长",
					align : "center"
				}, {
					field : "VENDERSIGN",
					title : "厂家签收时间",
					align : "center"
				}, {
					field : "VSIGN",
					title : "厂家响应时长",
					align : "center"
				}, {
					field : "SECONDHANLE",
					title : "二线处理时间",
					align : "center"
				}, {
					field : "SEHANDLE",
					title : "二线处理时长",
					align : "center"
				}, {
					field : "THREEHANDLE",
					title : "三线处理时间",
					align : "center"
				}, {
					field : "THANDLE",
					title : "三线处理时长",
					align : "center"
				});
			}  else if (action == 'support') {
				url = '/bizReport/bizCount';
				columns.push({
					field : "SYSTEM",
					title : "系统名称",
					align : "center"
				}, {
					field : "TOTAL",
					title : "总数",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'TOTAL';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "REMOTE",
					title : "远程",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'REMOTE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "LIVE",
					title : "现场",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'LIVE';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				}, {
					field : "OTHER",
					title : "其它",
					align : "center",
					formatter : function(value, row, index) {
						var systemName = row.SYSTEM;
						var field = 'OTHER';
						return "<a onclick='queryWorkOrder(\"" + systemName + "\",\"" + field + "\")'>" + value + "</a>";
					}
				})
			} else {
				url = '/workflow/queryWorkOrder/';
				columns.push({
					field : "bizId",
					title : "工单号",
					align : "center",
					formatter : function(value, row, index) {
						var url = path + "/biz/" + row.id;
						if (row.status == "草稿"){
							url = path + "/biz/create/" + row.processDefinitionId.split(":")[0] + "?bizId=" + row.id;
						}
						return "<a onclick=\"window.open('" + url + "');\">" + value + "</a>";
					}
				}, {
					field : "bizType",
					title : "工单类型",
					align : "center"
				}, {
					field : "title",
					title : "工单标题",
					align : "center",
					'class' : "data-resize",
					sortable : true,
					formatter : function(value, row) {
						if (value && value.length > 13) {
							return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
						} else {
							return value;
						}
					}
				}, {
					field : "createUser",
					title : "创建人",
					align : "center"
				}, {
					field : "createTime",
					title : "创建时间",
					align : "center"
				}, {
					field : "status",
					title : "工单状态",
					align : "center"
				}, {
					field : "taskAssignee",
					title : "当前处理人",
					align : "center"
				});
			}
		} else {
			url = '/bizReport/findReportEventBiz/';
			columns.push({
				field : "workNum",
				title : "工单号",
				align : "center",
				formatter : function(value, row, index) {
					var bizResource = row.bizResource;
					var url = path + "/biz/" + row.bizId;
					if(bizResource=='12583'){
						url = unmpUrl + "/RequirementManage/inc/IncFullFlow.aspx?incidentID=" + row.bizId;
					}else{
						if (row.taskDefKey == "草稿") {
							url = path + "/biz/create/" + row.processDefinitionId.split(":")[0] + "?bizId=" + row.bizId;
						}
					}
					return "<a onclick=\"window.open('" + url + "');\">" + value + "</a>";
				}
			}, {
				field : "title",
				title : "工单标题",
				align : "center",
				'class' : "data-resize",
				sortable : true,
				formatter : function(value, row) {
					if (value && value.length > 20) {
						return "<i title='" + value + "'>" + value.substring(0, 20) + "...</i>";
					} else {
						return value;
					}
				}
			}, {
				field : "createUser",
				title : "创建人",
				align : "center"
			}, {
				field : "createTime",
				title : "创建时间",
				align : "center"
			}, {
				field : "systemName",
				title : "所属系统",
				align : "center"
			}, {
				field : "taskDefKey",
				title : "工单状态",
				align : "center"
			}, {		 	
				field : "handleVender",
				title : "处理厂家",
				align : "center",
				formatter : function(value, row) {
					if (value && value.length >15) {
						return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
					} else {
						return value;
					}
				}
			}, {
				field : "taskAssignee",
				title : "当前处理人",
				align : "center",
				formatter : function(value, row) {
					if (value && value.length >15) {
						return "<i title='" + value + "'>" + value.substring(0, 10) + "...</i>";
					} else {
						return value;
					}
				}
			}, {
				field : "limitTime",
				title : "最次解决时间",
				align : "center"
			}, {
				field : "bizResource",
				title : "工单来源",
				align : "center"
			});
		}	
        if(action=='leftBiz'){
            columns.splice(0,0,{field : "state",checkbox : true,align : "center"});
        }
		biz.report.$table = $("#biz-table");
		biz.report.$table.bootstrapTable({
			method : 'post',
			contentType:"application/x-www-form-urlencoded",
			sidePagination : 'server',
			url : path + url,
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 50 ],
			queryParams : function queryParams(param) {
                
                param.status = status;
                param.taskDefKey = taskDefKey;
                param.taskAssignee = taskAssignee;
                param.action = action;
                param.systemName = systemName;
                param.field= field;
                if(action =='isShuffle'){
                	param.bizType = '事件管理';
                	param.isShuffle = '1';
                }else if(action != 'leftBiz'){
                	param.bizType = '故障管理';
                }else{
                	param.bizType = '事件管理';
                }
                $("#biz-query-form").find("[name]").each(function() {
                    if ($(this).val() != null && ($.trim($(this).val()))!="") {
                        if($(this).val()!='undefined')
                            param[this.name] = $.trim($(this).val());
                    }
                });
                return param;
        },
			columns : columns
		});
	},
	refresh : function(param) {
		biz.report.$table.bootstrapTable('refresh');
		
	},
	getSelections : function() {
		var rows = biz.report.$table.bootstrapTable("getSelections");
		return rows;
	},
	removeLeft:function(){
	    var rows = this.getSelections();
	    if(rows.length<1){
	       bsAlert("提示","请选择要移除的工单!");
	       return;
       }
	    var ids = []; 
	    for(var i=0;i<rows.length;i++){
	        ids.push(rows[i].id);
	    }
		$.confirm({
            title:"提示",
            content:"确认移除选中的工单？",
            confirmButton: "确认",
            cancelButton: "取消",
            confirm:function() {
            	 $.ajax({
          	       url:path+"/biz/removeLeft",
          	       type:"post",
          	       data:{bizIds:ids},
          	       async:false,
          	       traditional:true,
          	       success:function(data){
          	           if(data.success){
          	               bsAlert("提示",data.msg);
          	               biz.report.$table.bootstrapTable('refresh');
          	           }else{
          	               bsAlert("提示",data.msg);
          	           }
          	       }
          	    });
            }
		});
	},
	exportDetail:function(){
        
        var param = {};
        $("#biz-query-form").find("[name]").each(function() {
            if ($(this).val() != null && $.trim($(this).val() != "")) {
                param[this.name] = $.trim($(this).val());
            }
         });
        
        var temp = document.createElement("form");
        temp.action = path+"/biz/exportSupervisionBiz";
        temp.method = "post";
        temp.style.display = "none";
        for (var x in param) {
            var opt = document.createElement("input");
            opt.name = x;
            opt.value = param[x];
            temp.appendChild(opt);
        }
        var description = document.createElement("input");
        description.name = 'description';
        description.value = $("#description")[0].checked;
        temp.appendChild(description);
        var solution = document.createElement("input");
        solution.name = 'solution';
        solution.value = $("#solution")[0].checked;
        temp.appendChild(solution);
        document.body.appendChild(temp);
        temp.submit();
    }
};

function queryWorkOrder(systemName, field) {
	var param = {
		action : action,
		systemName : systemName,
		field : field
	};
	var temForm = document.createElement("form");
	temForm.action = path + "/bizReport/index?action="+action;
	temForm.target = "_blank";
	temForm.method = "post";
	temForm.style.display = "none";
	for ( var x in param) {
		var opt = document.createElement("input");
		opt.name = x;
		opt.value = param[x];
		temForm.appendChild(opt);
	}
	document.body.appendChild(temForm);
	temForm.submit();
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
    return temp;
}
$(function() {
	biz.report.query.form.loadSystem();
	biz.report.query.form.loadCompact();
    biz.report.query.form.init();
	biz.report.query.init();
	biz.report.table.init();
});
