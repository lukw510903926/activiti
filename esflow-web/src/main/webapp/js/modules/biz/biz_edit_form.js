$.namespace("biz.edit");

function checkEmpty(ele){
    if(ele==undefined){
        ele = this;
    }else if(ele.originalEvent){
        ele = this;
    }
	$(ele).siblings("i").remove();
	if(/^(\s*)+$/.test(ele.value)){
		$(ele).after("<i style='color:red;'>&nbsp;不能为空！</i>");
	}
}

function lastSevenDay(){
	var today=new Date();
	var y = today.getFullYear();
	var M = today.getMonth()+1;
	if(M<10) M='0'+M;
	var day = new Date(y,M,0);  
	var d = day.getDate();
	var d2 = d-6-20;
	var disableDay = y+"-"+M+"-(2["+d2+"-9]|3[0-1])";
	return disableDay;
}

function banNumber(){
	var keyCode = event.keyCode;
    if((keyCode<48 && keyCode!=8)||(keyCode>57&&keyCode<96)||(keyCode>105&&keyCode!=110&&keyCode!=190)){ 
    	event.keyCode=0;
    	event.returnValue=false;
    }
}
function checkNumber(ele){
    if(ele==undefined){
        ele = this;
    }else if(ele.originalEvent){
        ele = this;
    }
    if(!/^((([1-9]+\d*)?|\d?)(\.\d*)?)?$/.test(ele.value )){
        ele.value = "";
        $(ele).after("<i style='color:red;'>&nbsp;请输入数字！</i>");
    }
}

function cleanCheck(ele){
    if(ele==undefined){
        ele = this;
    }else if(ele.originalEvent){
        ele = this;
    }
	$(ele).siblings("i").remove();
}
$(function(){
	biz.edit.data.form = $("#form");
});
biz.edit = {
        ConfirmUser:{},
        fileNumber:0,
		data:{
			tr:$("<tr></tr>")
		},
		getView:function(option){
			for(var key in biz.edit.data){
				delete biz.edit.data[key];
			}
			for(var key in option){
			    biz.edit.data[key] = option[key];
			}
			if(option.form==undefined){
				biz.edit.data.form = $("#form");
			}else{
				biz.edit.data.form = option.form;
			}
			biz.edit.data.tr = $("<tr></tr>");
			return biz.edit.form;
		}
};

biz.edit.form = {
    //动态加载表单组件
		setDynamic:function(option){
			if(option!=undefined){
				if(option.table!=undefined){
					biz.edit.data.table = option.table;
				}
				if(option.tr!=undefined){
					biz.edit.data.tr = option.tr;
				}
			}else{
				option = {};
			}
			if(option.list==undefined){
				option.list = biz.edit.data.list;
			}
			if(option.list!=null&&option.list.length>0){
				for(var j=0;j<option.list.length;j++){
					switch(option.list[j].viewComponent){
						case "TEXTAREA":	
							biz.edit.form.addTextarea(option.list[j]);
							break;
						case "TEXT":
							biz.edit.form.addTextField(option.list[j],option.list);	
							break;
						case "BUTTON":
							biz.edit.form.addButton(option.list[j],option.list);	
							break;
						case "COMBOBOX":
							biz.edit.form.addComboBox(option.list[j],option.list);
							break;
						case "CONFIGURATIONUSER":
                            biz.edit.form.addChangeUser(option.list[j]);
                            break;
						case "VENDORCOMBOBOX":
                            biz.edit.form.addVendorComboBox(option.list[j]);
                            break;
						case "DICTCOMBOBOX":
							biz.edit.form.addComboBox(option.list[j],option.list);
							break;
						case "MCMLISTBOX":
							biz.edit.form.addComboBox(option.list[j],option.list);
							break;
						case "TERMINALUSER":
							biz.edit.form.addTerminalUser(option.list[j],option.list);
							break;
						case "TREATMENT":
                            biz.edit.form.addComboBox(option.list[j],option.list);
                            break;
						case "URGENCYLEVEL":
							biz.edit.form.addUrgencyLevel(option.list[j]);
							break;
						case "MEMBERBOX":
							biz.edit.form.addMember(option.list[j]);
							break;
						case "BOOLEAN":
							biz.edit.form.addBoolean(option.list[j]);
							break;
						case "MCMGRID":
							biz.edit.form.addConfigItem(option.list[j]);
							break;
						case "MEMBERLINKAGE":
						    biz.edit.form.addMemberLinkage(option.list[j],option.list);
						    break;
					    case "MEMBERLIST":
    					    biz.edit.form.addMemberList(option.list[j]);
                            break;
					    case "MEMBERSEELCT":
					    	 biz.edit.form.addMemberSelect(option.list[j]);
					    	break;
                        case "CONFIRMUSER":
                            biz.edit.form.addConfirmUser(option.list[j]);
                            break;
                        case "EVENTBIZ":
                            biz.edit.form.addEventBiz(option.list[j],option.list);
                            break;
                        case "GROUPHEAD":
                        	biz.edit.form.addGroupHead(option.list[j]);
                        	break;
                        case "ORGCOMBOBOX":
                        	biz.edit.form.addorgComBobox(option.list[j]);
                        	break;
                        case "STAFFCOMBOBOX":
                        	biz.edit.form.addMemberLinkage(option.list[j],option.list,null,null,1);
                        	break;
                        case "REMARK":
                        	biz.edit.form.addRemark(option.list[j]);
                        	break;
                        case "ONLINETIME":
                        	biz.edit.form.addOnLineTime(option.list[j]);
                        	break;
                        case "REQUIREDFILE":
                        	biz.edit.form.addRequiredFile(option.list[j]);
                        	break;
                        case "MANAGERAPPROVER":
                        	biz.edit.form.addManagerApprover(option.list[j]);
                        	break;
                        case "STAFFINFOLINKAGECOMBOBOX":
                        	biz.edit.form.addMemberLinkage(option.list[j],option.list,null,null,2);
                        	break;
						default:
							biz.edit.form.addTextField(option.list[j]);
					}						
				}
				if(option.end||option.end==undefined)
					biz.edit.form.appendTd();				
			}
			biz.edit.data.tr = $("<tr></tr>");
//			//联动节点数据
//			for(var i=0;i<option.list.length;i++){
//				if(option.list[i].refVariable&&!option.list[i].name=='processSystemName')
//					biz.edit.form.refVariable.setRefVariable(option.list,option.list[i]);
//			}
			return biz.edit.data.tr;
		},
		
		//处理方式分组表单元素
		variableGroup:function(option){
			if(option!=undefined){
				if(option.table!=undefined){
					biz.edit.data.table = option.table;
				}
				if(option.tr!=undefined){
					biz.edit.data.tr = option.tr;
				}
			}else{
				option = {};
			}
			if(option.list==undefined){
				option.list = biz.edit.data.list;
			}
			if(option.list==null){
				return;
			}
			var treatment = null;
			var group=null;
			if($(option.ele).length>0&&$(option.ele).val()!=""){
				group = $(option.ele).val();
			}else{
				if(option.list) {
					for(var i=0;i<option.list.length;i++){
						if(option.list[i].viewComponent=="TREATMENT"){
							var treatmentList = option.list[i].viewDatas.split(",");
//							if(biz.detail && biz.detail.workInfo && biz.detail.workInfo.bizType=='事件管理' && biz.detail.workInfo.status=='服务台拦截'){
//								treatmentList = $.grep(treatmentList, function(value) {
//									return value != '拦截';
//								});
//								treatment = treatmentList.join(",");
//							}
							group=treatmentList[0];
							treatment = option.list[i];
							break;
						}
					}
				}
			}
			if(group!=null){
				var array = [];
				for(var i=0;i<option.list.length;i++){
					var vgroup = option.list[i].variableGroup;
					if(vgroup==null||vgroup==""||option.list[i].viewComponent=="TREATMENT"){
						var treatments = option.list[i].viewDatas.split(",");
						array.push(option.list[i]);
						continue;
					}
					var groups = vgroup.split(",");
					for(var j=0;j<groups.length;j++){
						if(groups[j]==group){
							array.push(option.list[i]);
							break;
						}
					}
				}
			}else{
				array = option.list;
			}
			var trs = biz.edit.data.table.find("tr");
			var length = trs.length;
			if(length>2){
				var tr1 = trs.eq(length-2).clone();
				var tr2 = trs.eq(length-1).clone();
			}
			biz.edit.data.table.empty();
			option.list = array;
			biz.edit.form.variableButton({});
			biz.edit.form.setDynamic(option);
			if(biz.edit.data.buttonGroup){
			    biz.edit.form.variableButton(biz.edit.data.buttonGroup[group]==undefined?biz.edit.data.buttonGroup.all:biz.edit.data.buttonGroup[group]);
			}
			if($(option.ele).length>0){
			    $(option.ele).val(group);
			}else{
    			if(treatment)
    			    $("[name='"+treatment.name+"']").val(group);
			}
//			//服务厂商与业务系统联动
//			if($("[name='systemName']").length>0&&$("[name='serviceVendor']").length>0){
//				biz.edit.form.combobox.showTurnSend("[name='systemName']");
//				$("[name='systemName']").attr("onchange","biz.edit.form.combobox.showTurnSend(this)");
//			}
//			//局方人员与业务系统联动 
//            if($("[name='systemName']").length>0&&$("[name='officialUser']").length>0){
//                biz.edit.form.combobox.showOfficialUser("[name='systemName']");
//                $("[name='systemName']").attr("onchange","biz.edit.form.combobox.showOfficialUser(this)");
//            }
//            if($("[name='systemName']").length>0&&$("[name='otherUser']").length>0){
//                biz.edit.form.combobox.showOfficialUser("[name='systemName']");
//                $("[name='systemName']").attr("onchange","biz.edit.form.combobox.showOfficialUser(this)");
//            }
//			if(length>2){
//	//		    biz.edit.data.table.append(tr1);
//	//			biz.edit.data.table.append(tr2);
//			}
			biz.edit.form.addFile();
		},
		//处理方式分组按钮
		variableButton:function(buttons){
            $("#formButtons").remove();
            var buttonlist = $("<div id='formButtons' class='btn_list' style='padding:10px 0;margin:0;'></div>");
            $("#workForm").append(buttonlist);    
            $.each(buttons,function(k,n){
                buttonlist.append("<a class='yes_btn mrr10' onclick=biz.detail.save('"+k.trim()+"')>"+n+"</a>");
            });
//            if(biz.detail&&biz.detail.workInfo.bizType=="事件管理"&&biz.detail.reSetWorkTime&&biz.detail.workInfo.taskDefKey=='已完成'){
//            	buttonlist.append("<a class='yes_btn mrr10' onclick=biz.detail.saveWorkTime()>工时修改</a>");
//            }
            buttonlist.append("<a onclick='javascript:window.opener=null;window.close();'>关闭</a>");
		},
		addTextarea:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			biz.edit.form.appendTd();
			var th = $("<th></th>");
			var td = $("<td colspan='3'></td>");
			th.append(data.alias+":");
			var textarea = "<textarea placeholder='不可超过400个中文' name='"+data.name+"' rows='2' cols='20' class='fslTextBox' style='height:81px;width:90%;'></textarea>";
			td.html(textarea);
			biz.edit.form.addCkeckEmpty(data,th,td.children("textarea"));
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			return biz.edit.data.tr;
		},
		addButton:function(data,list,table,tr){
			
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			biz.edit.form.appendTd();
			var th = $("<th></th>");
			var td = $("<td colspan='3'></td>");
			th.append(data.alias+":");
			var viewParams = data.viewParams;
			var viewDatas = data.viewDatas
			if(viewParams){
				var buttons = viewParams.split(",");
				var urls = viewDatas.split(",");
				var styleClassName ='btn btn-y mrr10';
				for (var i = 0; i < buttons.length; i++) {
					var button = $("<a style='' target='_blank'>"+buttons[i]+"</a>");
					button.attr("name",buttons[i]);
					button.attr('class',styleClassName);
					if(urls[i]=='problemManagement'){
						button.on('click',function(){biz.edit.form.createProblemBiz();});
					}else{
						button.attr('href',path+'/biz/create/'+urls[i]);
					}
					td.append(button);
				}
			}
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			return biz.edit.data.tr;
		},
		
		createProblemBiz : function(){
			
			if(biz.detail && biz.detail.workInfo){
				var taskDefKey = biz.detail.workInfo.taskDefKey;
				if(biz.detail.workInfo.bizType=='事件管理' && (taskDefKey=='服务台处理'||taskDefKey=='厂商处理')){
					if(taskDefKey=='服务台处理'){
						var url = path+'/biz/create/problemManagement'
						window.open(url);
						return;
					}
					var serviceInfo = biz.detail.serviceInfo;
					var params = {};
					params['base.buttonId']='sumbmit';
					params['base.workTitle']=biz.detail.workInfo.title;
					params['startProc']=false;
					params['evnetInfo']=biz.detail.workInfo.bizId;
					params['base.handleName']='发起工单';
					params['base.handleResult']='提交';
					params['problemSource']='事件管理';
					var now = new Date();
		            now.setDate(now.getDate()+7); 
		            var limitTime = now.Format("yyyy-MM-dd hh:mm:ss");
					params['base.limitTime']=limitTime;
					params['mcminof']=true;
					params['btSelectItem']='on';
					var problemDetails='';
					var systemName = '';
					var detail ='';
					for (var i = 0; i < serviceInfo.length; i++) {
						var element = serviceInfo[i];
						if(element.variable.name=='systemName'){
							systemName = '【'+element.value+'】';
						}
						if(element.variable.name=='incidentContent'){
							detail = element.value;
						}
					}
					params['problemDetails']=systemName+' '+detail;
					var base_tempID = '';
					$.ajax({
						url : path +'/processModelMgr/getProcessId/problemManagement',
						data : {},
						async : false,
						success : function(result){
							base_tempID = result;
						}
					});
					params['base.tempID']=base_tempID;
					$.ajax({
						url : path + '/workflow/createProblem',
						async : false,
						type : 'post',
						data :params,
						success : function(result) {
							var url = path + '/biz/create/problemManagement';
							if(result.success){
								url = path + '/biz/create/problemManagement'+'?bizId='+ result.msg;
							}
							window.open(url);
						}
				});
				}
			}
		},		
		addTextField:function(data,list,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			if(data.order==1){
				biz.edit.form.appendTd();
			}
			var th = $("<th></th>");
			var td = $("<td></td>");
			var input = $("<input type='text' class='fslTextBox'/>");
			biz.edit.form.addCkeckEmpty(data,th,input);
			//三个文本框类型不同组件
			if(data.viewComponent=="NUMBER"){
				input.keydown(banNumber);
				input.change(checkNumber);
				input.val(0);
			}else if(data.viewComponent=="DATETIME"){
			    input.attr("readonly","readonly");
				input.addClass("Wdate");
				if(data.name=="lastHandleTime"){
					var lastHandleTime = biz.detail.workInfo.limitTime;
					lastHandleTime = lastHandleTime.replace(/-/g,"/");
					var date = new Date(lastHandleTime);
					date -= 12*60*60*1000;
					lastHandleTime = new Date(parseInt(date));
					input.focus(function(){WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:$.formatDate('yyyy-MM-dd HH:mm:ss',lastHandleTime)})});
				}else
					input.focus(function(){WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm:ss'})});
				if(data.name=='delayTime'){
					input.val(biz.detail.delayTime2);
				}		
			}else if(data.viewComponent=="DATE"){
			    input.attr("readonly","readonly");
				input.addClass("Wdate");
				input.focus(function(){WdatePicker({lang:'zh-cn'})})
			}
			th.append(data.alias+":");
			input.attr("name",data.name);
			td.append(input);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			if(biz.edit.data.tr.children("td").length==2){
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			};
			return biz.edit.data.tr;
		},
		
		addTerminalUser:function(data,list,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			var td = $("<td></td>");
			var select = $("<select class='fslTextBox'></select>");
			biz.edit.form.addCkeckEmpty(data,th,select);
			th.append(data.alias+":");
			
			select.attr("name",data.name);
			biz.edit.form.combobox.data.terminalUserType = data.viewParams;
			biz.edit.form.combobox.data.systemType = data.viewDatas;
			biz.edit.form.combobox.data.select = select;
			biz.edit.form.combobox.terminaluser();
			td.append(select);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			if(biz.edit.data.tr.children("td").length==2){
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			};
			return biz.edit.data.tr;
		},	
		
		addComboBox:function(data,list,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			var td = $("<td></td>");
			var select = $("<select id='"+data.name+"' name='"+data.name+"' class='fslTextBox'></select>");
			biz.edit.form.addCkeckEmpty(data,th,select);
			th.append(data.alias+":");
			
			if(data.viewComponent=="TREATMENT"){
                select.attr("onchange","biz.edit.form.variableGroup({list:biz.edit.data.list,ele:'[name="+data.name+"]'});$('.js-example-basic-single').select2();");
            }
			select.attr("name",data.name);
			if(data.viewComponent=="DICTCOMBOBOX"){
				select.addClass("js-example-basic-single");
				select.attr('data-width','60%');
				biz.edit.form.combobox.loadDictComboBox(select,data.viewParams);
			}else if(data.viewComponent=="MCMLISTBOX"){
				biz.edit.form.combobox.loadMcfComboBox(select,data.viewParams);
			}else{
				if(data.viewDatas!=""&&data.viewDatas!=null){
					biz.edit.form.combobox.loadComboBox(select,data.viewDatas);
				}
			}
			td.append(select);
			var refArray = new Array();
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			if(biz.edit.data.tr.children("td").length==2){
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			};
			return biz.edit.data.tr;
		},	
		
		addChangeUser:function(data,table,tr){
           
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var th = $("<th></th>");
            var td = $("<td></td>");
            var select = $("<select class='fslTextBox'></select>");
            biz.edit.form.addCkeckEmpty(data,th,select);
            th.append(data.alias+":");
            
            select.attr("name",data.name);
            biz.edit.form.config.loadChangeUser(select,data.viewParams);
            td.append(select);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            return biz.edit.data.tr;
        },  
		
		//紧急级别组件
		addUrgencyLevel:function(data,table,tr){
		    if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var th = $("<th></th>");
            var td = $("<td></td>");
            var select = $("<select class='fslTextBox'></select>");
            biz.edit.form.addCkeckEmpty(data,th,select);
            th.append(data.alias+":");
            select.attr("name",data.name);
            biz.edit.form.combobox.loadComboBox(select,data.viewDatas);
            td.append(select);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            th = $("<th></th>");
            td = $("<td></td>");
            th.append("最迟解决时间:");
            select.change(biz.edit.form.combobox.otherUrgencyLevel);
            var input = $("<input type='text' class='fslTextBox' readonly='readonly'/>");
            input.attr("name","base.limitTime");
            input.addClass("Wdate");
            input.attr("style","height: 22px;");
            var now = new Date();
            now.setDate(now.getDate()+7);  
            input.val(now.Format("yyyy-MM-dd hh:mm:ss"));
            td.append(input);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            return biz.edit.data.tr;
		},
		addBoolean:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			var td = $("<td></td>");
			th.append(data.alias+":");
			var yes = $("<input type='radio' value='是'/>");
			var no = $("<input type='radio' value='否' checked='checked'/>");
			yes.attr("name",data.name);
			no.attr("name",data.name);
			td.append(yes);
			td.append("是");
			td.append(no);
			td.append("否");
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			if(biz.edit.data.tr.children("td").length==2){
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			};
			return biz.edit.data.tr;
		},
		addorgComBobox:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			var td = $("<td></td>");
			th.append(data.alias+":");
			var sectorCombo = $("<input id='orgSectorCombo' type='text' readonly value='' class='fslTextBox' onclick='biz.edit.form.orgTree.showMenu();' />");
			var sectorCombo2 = $("<input id='orgSectorComboVal' type='hidden' value=''/>");
			td.append(sectorCombo);
			td.append(sectorCombo2);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			div=$('<div id="orgSectorMenuContent" class="menuContent" style="display:none; position: absolute;"></div>');
			var ul = $('<ul id="orgSectorTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>');
			div.append(ul);
			td.append(div);
			biz.edit.form.addCkeckEmpty(data,th,td);
			if(biz.edit.data.tr.children("td").length==2){
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			};
			biz.edit.form.orgTree.loadSectorBox();
			return biz.edit.data.tr;
		},
        showMenu:function() {//显示树图
            var cityObj = $("#sectorCombo");
            var cityOffset = $("#sectorCombo").offset();
            $("#sectorMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

            $("body").bind("mousedown", biz.edit.form.memberLinkage.onBodyDown);
        },
		//补充本行单元格及生成新行
		appendTd:function(table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			if(biz.edit.data.tr.children("td").length==1&&biz.edit.data.tr.children("td").attr("colspan")!=3){
				var th = $("<th></th>");
				var td = $("<td></td>");
				biz.edit.data.tr.append(th);
				biz.edit.data.tr.append(td);
				table.append(biz.edit.data.tr);
				biz.edit.data.tr = $("<tr></tr>");
			}
			return biz.edit.data.tr;
		},
		addMessage:function(data,table,tr){
            
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            th.append(data.alias+":");
            var textarea = "<textarea name='base.handleMessage' rows='2' cols='20' class='fslTextBox' style='height:81px;width:90%;'></textarea>";
            td.html(textarea);
            biz.edit.form.addCkeckEmpty(data,th,td.children("textarea"));
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            return biz.edit.data.tr;
        },
		addTitle:function(title,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			th.html("<span title='*' style='color: #ff0000'>*</span>"+title+":");
			var td = $("<td colspan='3'></td>");
			var input = $("<input type='text' maxlength='100' class='fslTextBox' style='width:70%;'/>");
			input.attr("name","base.workTitle");
			input.attr("onchange","checkEmpty(this)");
			input.attr("onfocus","cleanCheck(this)");
			input.attr("checkEmpty",true);
			td.append(input);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			return biz.edit.data.tr;
		},
		//备注禁用文本框
		addRemark:function(data,table,tr){
         
			if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3' style='color:#FF0000;'></td>");
            th.text("备注:");
			td.text(data.viewDatas);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            return biz.edit.data.tr;
        },
        addOnLineTime:function(data,table,tr){
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var th = $("<th></th>");
            var td = $("<td></td>");
            var minidate = $("input[name='base.limitTime']").val();
            var input = $("<input name='onLineTime' id='onLineTime' class='fslTextBox Wdate' type='text' onFocus='WdatePicker({dateFmt:\"yyyy-MM-dd HH:mm:ss\",minDate:\""+minidate+"\"})'/>");
            biz.edit.form.addCkeckEmpty(data,th,input);
            th.append(data.alias+":");
            td.append(input);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            return biz.edit.data.tr;
        },
		addFile:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			var th = $("<th></th>");
			th.text("相关附件:");
			var td = $("<td colspan='3'></td>");
			var tdText = "<span class='fslFileUpload' inputfileclass='FileUploadInputFileClass'><div class='fslFileUpload'>"+
			"<div class='FileUploadOperation'><img src='"+path+"/themes/default/img/attach.gif' style='border-width:0px;'/>"+
			"<a onclick='biz.edit.form.file.addFileInput(this)' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>"+
			"<img src='"+path+"/themes/default/img/deleteAll.gif' style='border-width:0px;'/>"+
			"<a onclick='biz.edit.form.file.removeFile(this)' class='RemoveButton'>移除附件</a></div></div></span>";
			td.html(tdText);
			if(data){
			    for(var i=0;i<data.length;i++){
			    	if(data[i].fileCatalog==null||data[i].fileCatalog==""||data[i].fileCatalog=="uploadFile"){
				        biz.edit.fileNumber++;
	        			span = $("<span style='margin-right: 10px; display: block;'></span>");
	                    span.attr("id","spanfile"+biz.edit.fileNumber);
	                    var checkbox = $("<input type='checkbox'/>");
	                    span.append(checkbox);
	                    var a = $("<a id='"+data[i].id+"' href='"+path+"/biz/download?id="+data[i].id+"'></a>");
	                    a.text(data[i].name);
	                    span.append(a);
	                    td.append(span);
			    	}
                }
            }
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			if($("#selectFile").length<1){
				biz.edit.form.file.creatFileWindow();
			}
			return biz.edit.data.tr;
		},

		addRequiredFile:function(data,table,tr){

			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
            biz.edit.form.appendTd();
			var th = $("<th></th>");
			th.append(data.alias+":");
			var td = $("<td colspan='3'></td>");
			var tdText = "<span class='fslFileUpload' inputfileclass='FileUploadInputFileClass'><div class='fslFileUpload'>"+
			"<div class='FileUploadOperation'><img src='"+path+"/themes/default/img/attach.gif' style='border-width:0px;'/>"+
			"<a onclick='biz.edit.form.file.addFileInput(this,\""+data.name+"\")' style='padding-right: 6px;' data-toggle='modal' data-target='#selectFile' class='UploadButton'>继续添加</a>"+
			"<img src='"+path+"/themes/default/img/deleteAll.gif' style='border-width:0px;'/>"+
			"<a onclick='biz.edit.form.file.removeFile(this)' style='padding-right: 6px;' class='RemoveButton'>移除附件</a>";
			if(data.viewDatas){
				biz.edit.form.file.data.downLoadFile = data.viewDatas;
				tdText = tdText + "<img src='"+path+"/themes/default/img/download.png' style='border-width:0px;'/>";
				tdText = tdText+"<a class='c_download' onclick='biz.edit.form.file.downLoadFile()'>模板下载</a>";
			}
			tdText = tdText+ "</div></div></span>";
			td.html(tdText);
            var hiddenInput = $("<input type='hidden' name='requiredFileCount' />");
            td.append(hiddenInput);
            if(data.name=='itPlanFile'){
				var planInput = $('[name="isPlan"]');
				planInput.on('change',function(){
					var value = planInput.val();
					if(value == '是'){
						data.required = true;
						biz.edit.form.addCkeckEmpty(data,th,hiddenInput);
					}else{
						data.required = false;
						hiddenInput.removeAttr('checkempty');
						th.text(data.alias+":");
					}
				});
			}
            biz.edit.form.addCkeckEmpty(data,th,hiddenInput);
			if((biz.create&&biz.create.draftData&&biz.create.draftData.annexs)||(biz.detail&&biz.detail.annexs)){
				var data2;
				if(biz.create&&biz.create.draftData&&biz.create.draftData.annexs){
					data2 = biz.create.draftData.annexs;
				}else{
					data2 = biz.detail.annexs;
				}
			    for(var i=0;i<data2.length;i++){
			    	if(data.name==data2[i].fileCatalog){
				        biz.edit.fileNumber++;
	        			var span = $("<span style='margin-right: 10px; display: block;'></span>");
	                    span.attr("id","spanfile"+biz.edit.fileNumber);
	                    var checkbox = $("<input type='checkbox' />");
	                    span.append(checkbox);
	                    var a = $("<a id='"+data2[i].id+"' href='"+path+"/biz/download?id="+data2[i].id+"'></a>");
	                    a.text(data2[i].name);
	                    span.append(a);
	                    td.append(span);
	                    hiddenInput.val("附件不为空");
			    	}
                }
            }
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			if($("#selectFile").length<1){
				biz.edit.form.file.creatFileWindow();
			}
			return biz.edit.data.tr;
		},
		addMember:function(data,table,tr){
			
			biz.edit.form.memberbox.loadSectorBox();
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			biz.edit.form.appendTd();
			var th = $("<th></td>");
			var td = $("<td colspan='3'></td>");
			th.append(data.alias+":");
			var handleUser = data.name;
			biz.edit.form.memberbox.data.input = handleUser;
			biz.edit.form.memberbox.data.viewDatas = data.viewDatas;
			biz.edit.form.memberbox.data.showDept = true;
			var input = $("<input type='hidden' name='"+handleUser+"' class='fslTextBox'/>");
			td.append(input);
			handleUser = data.name+'Name';
			input = $("<input type='text' name='"+handleUser+"' class='fslTextBox' style='width:60%' readonly='readonly'/>");
			biz.edit.form.memberbox.data.inputname = handleUser;
			
			biz.edit.form.addCkeckEmpty(data,th,input);
			var add = "<a class='btn btn-y' onclick='biz.edit.form.memberbox.openMemberContainer()'>选择人员</a>";
			var remove = "<a class='btn btn-n' onclick='biz.edit.form.memberbox.clearMember()'>清空人员</a>";
			td.append(input);
			td.append(add);
			td.append(remove);
			
			var container = biz.edit.form.memberbox.createMemberContainer();
			td.append(container);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			
			return biz.edit.data.tr;
			
		},
		//人员及联系方式联动
		addMemberLinkage:function(data,list,table,tr,type){
            biz.edit.form.memberLinkage.loadSectorBox();
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            th.append(data.alias+":");
            var handleUser = data.name;
            var input = $("<input type='hidden' name='"+handleUser+"' class='fslTextBox' />");
            handleUser = data.name+'Name';
            var chInput = $("<input type='text' name='"+handleUser+"' class='fslTextBox' style='width:40%;' readonly='readonly'/>");
            biz.edit.form.addCkeckEmpty(data,th,input);
            var add = "<a class='btn btn-y mrl10' onclick='biz.edit.form.memberLinkage.openMemberContainer(\""+data.name+"\")'>人员</a>";
            var addRole = "<a class='btn btn-y mrl10' onclick='biz.edit.form.memberLinkage.openRoleContainer()'>组别</a>";
            var remove = "<a class='btn btn-n mrl10' onclick='biz.edit.form.memberLinkage.clearMember(\""+data.name+"\")'>清空</a>";
            td.append(input);
            td.append(chInput);
            td.append(add);
            if(type!=1&&type!=2)
            	td.append(addRole);
            td.append(remove);
            biz.edit.form.memberLinkage.data[data.name+"inputName"] = data.name;
            //加入联动，注意联系方式参数是否设置联动
            if(list){
                if(list!=null && list.length>0 && type!=2){
                    for(var i=0;i<list.length;i++){
                        if(!list[i].refVariable)
                            continue;
                        if(data.id == list[i].refVariable){
                            biz.edit.form.memberLinkage.data.mobileName = list[i].name;
                        }
                    }
                }else if(list!=null && list.length>0 && type==2){
                    for(var i=0;i<list.length;i++){
                        if(!list[i].refVariable)
                            continue;
                        if(data.id == list[i].refVariable){
                        	if(list[i].viewComponent=="TEXT")
                        		biz.edit.form.memberLinkage.data[data.name+"department"] = list[i].name;
                        	else if(list[i].viewComponent=="MOBILE")
                        		biz.edit.form.memberLinkage.data[data.name+"mobileName"] = list[i].name;
                        	else if(list[i].viewComponent=="EMAIL")
                        		biz.edit.form.memberLinkage.data[data.name+"email"] = list[i].name;
                        }
                    }
                }
            }
            var container = biz.edit.form.memberLinkage.createMemberContainer(data.name);
            td.append(container);
            var div=$('<div id="roleMenuContent" class="menuContent" style="display:none; position: absolute;"></div>');
            var ul = $('<ul id="roleTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>');
            div.append(ul);
            td.append(div);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            biz.edit.form.memberLinkage.roleTree.loadRoleTree();
            return biz.edit.data.tr;
        },
        //角色人员下拉组件
        addMemberSelect:function(data,table,tr){
           
        	if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var th = $("<th></th>");
            var td = $("<td></td>");
            var select = $("<select name='"+data.name+"' id='mainDepartment'></select>");
            biz.edit.form.addCkeckEmpty(data,th,select);
            biz.edit.form.combobox.loadMemberSelect(select,data);
            th.append(data.alias+":");
            td.append(select);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            return biz.edit.data.tr;
        },
        //角色人员组件
        addMemberList:function(data,table,tr){
            
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></td>");
            var td = $("<td colspan='3'></td>");
            var input = $("<input type='text' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
            input.attr("id",data.name);
            input.click(function(){biz.edit.form.memberList.openWindow()});
            biz.edit.form.addCkeckEmpty(data,th,input);
            th.append(data.alias+":");
            td.append(input);
            var hidden = $("<input type='hidden' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
            hidden.attr("name",data.name);
            td.append(hidden);
            td.append(biz.edit.form.memberList.createWindow(data));
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            return biz.edit.data.tr;
        },
        
        //部门人员组件
        addDeptMemberList:function(data,table,tr){
            
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            var input = $("<input type='text' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
            input.attr("id",data.name);
            input.click(function(){biz.edit.form.memberList.openWindow()});
            biz.edit.form.addCkeckEmpty(data,th,input);
            th.append(data.alias+":");
            td.append(input);
            var hidden = $("<input type='hidden' class='fslTextBox' readonly='readonly' style='width:50%;'/>");
            hidden.attr("name",data.name);
            td.append(hidden);
            td.append(biz.edit.form.memberList.createWindow(data));
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            return biz.edit.data.tr;
        },
        
		//交维工单的交维工作组件
		addJwWork:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			biz.edit.form.appendTd();
			var th = $("<th></th>");
			th.html("交维工作:");
			var td = $("<td colspan='3'></td>");
			//定义工作名称
			var labels = {
					"功能答疑":["台账","常见问题FAQ","用户操作手册","系统需求规格说明书","系统概要设计说明书","系统详细设计方案","版本发布说明","数据字典","接口清单","第三方支撑说明","故障处理知识库","安装部署手册"],
					"数据维护":["台账","运维操作手册","第三方支撑说明","安装部署手册","日志解读说明书","接口清单","接口规范说明书"],
					"接口接入":["台账","运维操作手册","第三方支撑说明","安装部署手册","接口清单","接口规范说明书"],
					"例行检查":["系统健康检查操作指南","健康检查报告","日志解读说明书"],
					"故障处理":["台账","故障处理知识库","运维操作手册","第三方支撑说明","安装部署手册","故障处理报告","日志解读说明书"],
					"应急演练":["台账","应急演练方案","第三方支撑说明","应急演练报告","日志解读说明书"],
					"节日保障":["台账","节日保障方案","第三方支撑说明"],
					"系统备份":["台账","节日保障方案","第三方支撑说明"],
					"发布部署":["部署升级方案","版本发布说明","第三方支撑说明","日志解读说明书"],
					"容量管理":["第三方支撑说明"],
					"安全整改":["第三方支撑说明"],
					"培训计划":["培训计划"]
			};
			var trs = biz.edit.form.jwWork.addCheckBox(td,labels,"jwWork");	
			var hidden = $("<input type='hidden' class='fslTextBox'/>");
            hidden.attr("name",data.name);
            td.append(hidden);		
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			biz.edit.form.addCkeckEmpty(data,th,hidden);
			table.append(biz.edit.data.tr);
			for(var i=0;i<trs.length;i++){
				table.append(trs[i]);
			}
			biz.edit.data.tr = $("<tr></tr>");
			return biz.edit.data.tr;
		},
		//非空处理
		addCkeckEmpty:function(data,th,component){
			if(data.required){
			    if(th.text()){
			        th.empty();
			        th.append("<span title='*' style='color: #ff0000'>*</span>");
			        th.append(data.alias+":");
			    }else{
				    th.append("<span title='*' style='color: #ff0000'>*</span>");
			    }
			    component.attr("checkEmpty",true);
				component.change(checkEmpty);
				component.focus(cleanCheck);
			}
		},
		//变更工单号
		addChangeBiz:function(data,table,tr){
			if(table==undefined){
				table = biz.edit.data.table;
			}
			if(tr!=undefined){
				biz.edit.data.tr = tr;
			}
			biz.edit.data.tr = $("<tr></tr>");
			var th = $("<th></th>");
			th.html("变更工单编号:");
			var td = $("<td colspan='3'></td>");
			var input = $("<input type='text' maxlength='100' class='fslTextBox' style='width:50%;' readonly='readonly'/>");
			input.attr("name",data.name);
			biz.edit.form.addCkeckEmpty(data,th,input);
			td.append(input);
			var a = $("<a data-toggle='modal' data-target='#selectChangeBiz' class='btn btn-y mrl10'></a>");
			a.text("选择变更工单 ");
			var a = "<a data-toggle='modal' data-target='#selectChangeBiz' class='btn btn-y mrl10'>选择变更工单</a>";
			td.append(a);
			a = "<a onclick=biz.edit.form.changeBiz.removeChangeBiz('"+data.name+"') class='btn btn-n mrl10'>删除变更工单</a>";
			td.append(a);
			biz.edit.data.tr.append(th);
			biz.edit.data.tr.append(td);
			table.append(biz.edit.data.tr);
			biz.edit.data.tr = $("<tr></tr>");
			biz.edit.form.changeBiz.createBizWindow(data.name);
			return biz.edit.data.tr;
		},
        //添加个分组头部
        addGroupHead:function(data,table,tr){
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var hidden = $("<input type='hidden' name='"+data.name+"' value='true'>");
            var th = $("<th>");
            var td = $("<td colspan='5' style='padding:0;'>");
            var div = $("<h5 style='padding:6px 5px;background:#f7f7f7;'>" + data.alias+"："+data.groupName+"</h5>");
            td.append(hidden,div);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr>");
            return biz.edit.data.tr;
        },
        //事件工单列表
        addEventBizList:function(data,table,tr){
           if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            biz.edit.form.appendTd();
            var th = $("<th></th>");
            var td = $("<td colspan='3'></td>");
            th.append(data.alias+":");
            var hidden = $("<input type='hidden' name='"+data.name+"'/>");
            td.append(hidden);
            biz.edit.form.addCkeckEmpty(data,th,hidden);
            var buttonlist = $("<div class='mrb5'></div>");
            buttonlist.html("<a href='javascript:void(0)' id='addEvenBiz' class='btn btn-y mrr10'>关联事件工单</a>");
            buttonlist.append("<a href='javascript:void(0)' id='removeEvenBiz' class='btn btn-n mrr10'>删除事件工单</a>")
            var eventTable = $("<table style='width:100%;' name='eventTable' class='table base-table table-striped'></table>");  
            td.append(buttonlist);
            td.append(eventTable);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            table.append(biz.edit.data.tr);
            biz.edit.data.tr = $("<tr></tr>");
            biz.edit.form.eventBizList.inputName = data.name;
            biz.edit.form.eventBizList.loadEventBizList(data);
            $("#addEvenBiz").click(function(){biz.edit.form.eventBizList.openContainer()});
            $("#removeEvenBiz").click(function(){biz.edit.form.eventBizList.removeEventBiz()});
            td.append(biz.edit.form.eventBizList.createEventBizListContainer());
            biz.edit.form.eventBizList.loadEventBizGrid();
            return biz.edit.data.tr;
        },
        //确认人组件，包括确认人、联系方式、时间、部门
        addConfirmUser:function(data,table,tr){
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var hidden = $("<input type='hidden' name='"+data.name+"'/>");
            biz.edit.data.tr.append(hidden);
            //text：label，name：文本框相关标志涉及回显，value：显示值，disable：是否可编辑
            var list = [
            {
                text:"处理人",
                name:"user",
                value:currentUser.name,
                disabled:true
            },{
                text:"联系方式",
                name:"mobile",
                value:currentUser.mobile
            },{
                text:"邮箱",
                name:"email",
                value:currentUser.email
            },{
                text:"确认时间",
                name:"date",
                value:(new Date()).Format("yyyy-MM-dd hh:mm"),
                disabled:true
            },{
                text:"确认部门",
                name:"dep",
                value:currentUser.dep
            }];
            for(var key in list){
                var input = biz.edit.form.addDisable(list[key]);
                biz.edit.ConfirmUser[list[key].name] = list[key].value;
                input.change(function(){
                    biz.edit.ConfirmUser[$(this).attr("title")] = $(this).val();
                    biz.edit.data.table.find("[name='"+data.name+"']").val(JSON.stringify(biz.edit.ConfirmUser));
                });
            }
            hidden.val(JSON.stringify(biz.edit.ConfirmUser));
        },
        //加入禁用文本框，不属于流程组件
        addDisable:function(data,table,tr){
            if(table==undefined){
                table = biz.edit.data.table;
            }
            if(tr!=undefined){
                biz.edit.data.tr = tr;
            }
            var th = $("<th></th>");
            th.html(data.text);
            var td = $("<td></td>");
            var input = $("<input type='text' class='fslTextBox' title='"+data.name+"'/>");
            if(data.disabled)
                input.attr("disabled","disabled");
            input.val(data.value);
            td.append(input);
            biz.edit.data.tr.append(th);
            biz.edit.data.tr.append(td);
            if(biz.edit.data.tr.children("td").length==2){
                table.append(biz.edit.data.tr);
                biz.edit.data.tr = $("<tr></tr>");
            };
            return input;
        }
};

biz.edit.form.orgTree = {
	    
	    setting:{
	            view: {
	                dblClickExpand: false
	            },
	            data: {
	                simpleData: {
	                    enable: true
	                }
	            },
	            callback: {
	                beforeClick: function(treeId, treeNode) {
	                    var check = (treeNode && !treeNode.isParent);
	                    },
	                onClick: function(e, treeId, treeNode){
	                    var zTree = $.fn.zTree.getZTreeObj("orgSectorTree");
	                    nodes = zTree.getSelectedNodes(),
	                    vId="",
	                    v = "";
	                    nodes.sort(function compare(a,b){return a.id-b.id;});
	                    for (var i=0, l=nodes.length; i<l; i++) {
	                        v += nodes[i].name + ",";
	                        vId+=nodes[i].id+",";
	                    }
	                    if (v.length > 0 ) v = v.substring(0, v.length-1);
	                    if (vId.length > 0 ) vId = vId.substring(0, vId.length-1);
	                    var cityObj = $("#orgSectorCombo"),cityValue = $('#orgSectorComboVal');
	                    if(vId==""){
	                        cityObj.val("");
	                        cityValue.val("");
	                    }else{
	                        cityObj.val(v);   
	                        cityValue.val(vId);    
	                    } 
	                    biz.edit.form.orgTree.hideMenu();
	                }
	            }
	        },
	        zNodes:null,
	        showMenu:function() {
	            biz.edit.form.orgTree.sectorInit();
	            var cityObj = $("#orgSectorCombo");
	            var cityOffset = $("#orgSectorCombo").position();
	            $("#orgSectorMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight()  + "px"}).slideDown("fast");
	            $("body").bind("mousedown", biz.edit.form.orgTree.onBodyDown);
	        },
	        hideMenu:function() {
	            $("#orgSectorMenuContent").fadeOut("fast");
	            $("body").unbind("mousedown", biz.edit.form.orgTree.onBodyDown);
	        },
	        onBodyDown:function(event) {
	            if (!(event.target.id == "orgSectorCombo" || event.target.id == "orgSectorMenuContent" || $(event.target).parents("#orgSectorMenuContent").length>0)) {
	                biz.edit.form.orgTree.hideMenu();
	            }
	        },
	        sectorInit:function(){
	            $.fn.zTree.init($("#orgSectorTree"), biz.edit.form.orgTree.setting, biz.edit.form.orgTree.zNodes);
	            //初始化选择第一个
	            var zTree = $.fn.zTree.getZTreeObj("orgSectorTree");
	            zTree.addNodes(null,0,{
	                id : "",
	                name : "选空",
	                nocheck : true
	            });
	            var nodes = zTree.getNodes();    
	            zTree.selectNode(nodes[0]); 
	            zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
	        },
	        loadSectorBox:function(){
	            $.ajax({
	                type:"post",
	                url:path+"/bizHandle/loadSectors",
	                async:false,
	                success:function(data){
	                    if(data!=null && data.success && data.obj != null){
	                        biz.edit.form.orgTree.zNodes = data.obj;
	                    }else{
	                    }
	                }
	            });
	       }
};

$(window).resize(function(){
	if($('#configcontainer').length>0&&!$('#configcontainer').is(":hidden")){
		$('#configcontainer').css('width',"0");
		var width = $("[name='configtable']").css('width');
		var width = width.substring(0,width.length-2);
		$('#configcontainer').css('width',(parseInt(width)+2));
	}
	if($('#memberContainer').length>0&&!$('#memberContainer').is(":hidden")){
	    $('#memberContainer').css('width',"0");
	    var width = $("[name='"+biz.edit.form.memberbox.data.inputname+"']").parent('td').css('width');
        width = width.substring(0,width.length-2)-10;
        $('#memberContainer').css('width',(parseInt(width)+2));
    }
    if($('#memberLinkageContainer').length>0&&!$('#memberLinkageContainer').is(":hidden")){
        $('#memberLinkageContainer').css('width',"0");
        var width = $("#memberLinkageContainer").parent('td').css('width');
        width = width.substring(0,width.length-2)-10;
        $('#memberLinkageContainer').css('width',(parseInt(width)+2));
    }
});