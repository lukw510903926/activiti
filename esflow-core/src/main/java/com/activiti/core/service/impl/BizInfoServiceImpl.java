package com.activiti.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.activiti.core.bean.BizInfo;
import com.activiti.core.bean.BizInfoConf;
import com.activiti.core.bean.ProcessVariableInstance;
import com.activiti.core.common.service.BaseServiceImpl;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.dao.BizInfoConfDao;
import com.activiti.core.dao.IBizFileDao;
import com.activiti.core.dao.IBizInfoDao;
import com.activiti.core.dao.IProcessVarInstanceDao;
import com.activiti.core.service.IBizInfoService;
import com.activiti.core.service.RoleService;
import com.activiti.core.util.MailUtil;
import com.activiti.core.util.WebUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activiti.core.bean.BizFile;
import com.activiti.core.common.utils.DateUtils;
import com.activiti.core.service.act.ActProcessService;
import com.activiti.core.util.Constants;

@Service
@Transactional(readOnly = true)
public class BizInfoServiceImpl extends BaseServiceImpl<BizInfo> implements IBizInfoService {
	
	private Logger logger = Logger.getLogger("bizInfoServiceImpl");
	
	@Autowired
	private IBizInfoDao dao;
	
	@Autowired
	private BizInfoConfDao bizInfoConfDao;

	@Autowired
	private IProcessVarInstanceDao processInstanceDao;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private IBizFileDao bizFileDao;
	
	@Autowired
	private ActProcessService actProcessService;
	
	@Autowired
	private MailUtil mailUtil;
	
	/**
	 * 返回唯一的用户，如果角色对应的用户不止一个，则返回null
	 * @param roles
	 * @return
	 */
	public String loadOnlyUser(List<String> roles) {
		if (roles == null || roles.size() <= 0) {
			return null;
		}
		return dao.loadOnlyUser(roles);
	}
	
	@Override
	public String loadStructCity(String structId){
		
		return this.dao.loadStructCity(structId);
	}
	@Override
	public List<BizInfo> getBizByParentId(String parentId){
		
		return this.dao.getBizByParentId(parentId);
	}
	
	@Override
	public List<String> loadBizInfoStatus(String processId){
		
		
		List<String> list = new ArrayList<String>();
		list.add(Constants.BIZ_TEMP);
		list.add(Constants.BIZ_NEW);
		if(StringUtils.isNotBlank(processId)){
			getProcessStatus(processId, list);
		}else{
			List<ProcessDefinition> processList = actProcessService.getList();
			for (ProcessDefinition processDefinition : processList) {
				processId = processDefinition.getId();
				getProcessStatus(processId, list);
			}
		}
		list.add(Constants.BIZ_END);
		return list;
	}
	
	private void getProcessStatus(String processId,List<String> list){
		
		try {
			List<Map<String, Object>> result = actProcessService.getAllTaskByProcessKey(processId);
			for(Map<String,Object> map : result){
				String status = (String)map.get("name");
				if(!list.contains(status)){
					list.add(status);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> loadAllUsers(List<String> roles) {
		
		if (roles == null || roles.size() <= 0) {
			return null;
		}
		return dao.loadAllUsers(roles);
	}
	

	/**
	 * 加载用户及用户组<br>
	 * 如果parentID为空，则加载父及为空的组，否则加载该组下面的所有组和用户
	 * 
	 * @return
	 * @
	 */
	public List<Map<String, Object>> loadUserGroup(String parentID) {
		return dao.loadUserGroup(parentID);
	}

	@Transactional
	public void addBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			dao.save(bean);
		}
	}

	@Transactional
	public void updateBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.update(bean);
		}
	}
	
	@Override
	public BizInfo copyBizInfo(String bizId, String processInstanceId,Map<String, Object> variables){
		
		BizInfo oldBiz = this.dao.getByBizId(bizId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", bizId);
		params.put("status", Constants.BIZ_NEW);
		params.put("parentTaskName", oldBiz.getTaskName());
		PageHelper<BizInfo> page = new PageHelper<BizInfo>();
		page.setPage(-1);
		page.setRows(-1);
		List<BizInfo> list = this.getBizInfoList(params,page).getList();
		BizInfo newBiz = oldBiz.clone();
		if(variables.get("lastHandleTime")!=null){
			newBiz.setLimitTime(DateUtils.parseDate(variables.get("lastHandleTime")));
		}
		try {
			newBiz.setId(null);
			if(list == null)
				newBiz.setBizId(newBiz.getBizId()+"-00"+1);
			else
				newBiz.setBizId(newBiz.getBizId()+"-00"+(list.size()+1));
			newBiz.setProcessInstanceId(processInstanceId);
			newBiz.setParentId(bizId);
			newBiz.setCreateTime(new Date());
			newBiz.setParentTaskName(oldBiz.getTaskName());
			newBiz.setStatus(Constants.BIZ_NEW);
			String username = WebUtil.getLoginUser().getUsername();
			newBiz.setCreateUser(username);
			this.addBizInfo(newBiz);
			List<ProcessVariableInstance> processInstances = processInstanceDao.loadProcessInstances(oldBiz.getProcessInstanceId());
			if(processInstances != null && !processInstances.isEmpty()) {
				for (ProcessVariableInstance oldInstance : processInstances) {
					ProcessVariableInstance newInstance = oldInstance.clone();
					newInstance.setId(null);
					String systemName = (String)variables.get("systemName");
					if("processSystemName".equals(oldInstance.getVariable().getName())){
						newInstance.setValue(systemName);
					}
					newInstance.setBizId(newBiz.getId());
					newInstance.setProcessInstanceId(processInstanceId);
					newInstance.setCreateTime(new Date());
					processInstanceDao.save(newInstance);
				}
			}
			BizInfoConf bizInfoConf = new BizInfoConf();
			bizInfoConf.setBizInfo(oldBiz);
			List<BizInfoConf> bizInfoConfs = this.bizInfoConfDao.findBizInfoConf(bizInfoConf);
			if(bizInfoConfs != null && !bizInfoConfs.isEmpty()) {
				for (BizInfoConf bizConf : bizInfoConfs) {
					BizInfoConf newConf = bizConf.clone();
					newConf.setId(null);
					newConf.setBizInfo(newBiz);
					newConf.setCreateTime(new Date());
					bizInfoConfDao.save(newConf);
				}
			}
			List<BizFile> files = bizFileDao.loadBizFileByBizId(bizId,oldBiz.getTaskId());
			if(files != null && !files.isEmpty()) {
				for (BizFile oldFile : files) {
					BizFile bizFile = oldFile.clone();
					bizFile.setId(null);
					bizFile.setBizInfo(newBiz);
					bizFile.setCreateDate(new Date());
					bizFile.setCreateUser(username);
					bizFileDao.save(bizFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBiz;
	}

	@Transactional
	public void deleteBizInfo(BizInfo... beans) {
		for (BizInfo bean : beans) {
			if (bean.getId() == null)
				continue;
			dao.delete(bean);
		}
	}

	@Transactional
	public void deleteBizInfo(String... ids) {
		for (String id : ids) {
			dao.deleteById(id);
		}
	}

	@Override
	public BizInfo getBizInfo(String id,String loginUser) {

		return dao.getBizInfo(id,loginUser);
	}
	
	@Override
	public BizInfo getByBizId(String id) {

		return dao.getByBizId(id);
	}
	
	@Override
	@Transactional
	public void updateBizByIds(List<String> list) {
		
		BizInfo bizInfo = null;
		for (String id : list) {
			if(StringUtils.isNotBlank(id)){
				bizInfo = this.dao.getByBizId(id);
				if(bizInfo != null){
					bizInfo.setStatus(Constants.BIZ_DELETE);
					this.updateBizInfo(bizInfo);
				}
			}
		}
	} 

	@Override
	public PageHelper<BizInfo> getBizInfoList(Map<String, Object> params, PageHelper<BizInfo> page) {
		
		List<BizInfo> result = new ArrayList<BizInfo>();
		Map<String,Map<String,String>> userCache = new HashMap<String, Map<String,String>>();
		// 处理时间
		Object ct1 = params.get("createTime");
		Object ct2 = params.get("createTime2");
		if (!(ct1 == null && ct2 == null)) {
			if (ct1 == null) {
				java.util.Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, 1900);
				ct1 = calendar.getTime();
				params.put("createTime", ct1);
			} else if (ct2 == null) {
				ct2 = new Date();
				params.put("createTime2", ct2);
			}
		}

		Object pt1 = params.get("processTime");
		Object pt2 = params.get("processTime2");
		if (!(pt1 == null && pt2 == null)) {
			if (pt1 == null) {
				java.util.Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, 1900);
				pt1 = calendar.getTime();
				params.put("processTime", pt1);
			} else if (pt2 == null) {
				pt2 = new Date();
				params.put("processTime2", pt2);
			}
		}
		PageHelper<BizInfo> pageHelper = dao.queryWorkOrder(params,page);
		List<BizInfo> list = pageHelper.getList();
		if (list != null) {
			for (BizInfo bizInfo : list) {
				bizInfo.setCreateUser(this.roleService.getFullNameByUserName(bizInfo.getCreateUser(), userCache));
				bizInfo.setTaskAssignee(this.roleService.getFullNameByUserName(bizInfo.getTaskAssignee(), userCache));
				result.add(bizInfo);
			}
		}
		pageHelper.setList(result);
		userCache.clear();
		return pageHelper;
	}
	
	@Override
	public void sendEmail(List<String> bizIds){
		
		String message = null;
		BizInfo bizInfo = null;
		try {
			for (String bizId : bizIds) {
				List<String> emailList = new ArrayList<String>();
				bizInfo = this.get(bizId);
				if (bizInfo != null) {
					this.getTargetList(bizInfo, emailList, "email");
					message = "请及时处理 " + bizInfo.getBizType() + " 工单 ,工单号: " + bizInfo.getBizId();
					logger.info("emailList : " + emailList);
					if (!emailList.isEmpty()) {
						mailUtil.sendEmail(emailList, "工单处理提醒", message);
					}
				}
			}
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(" send eamil error: " + e.getLocalizedMessage());
		}
	}
	/**
	 * 获取用户 手机号 或 邮箱 
	 * @param bizInfo
	 * @param targetList
	 * @param targetType
	 * 			email
	 * 			moblie
	 */
	private void getTargetList(BizInfo bizInfo,List<String> targetList,String targetType){
		
		List<BizInfoConf> list = null;
		list = this.bizInfoConfDao.getBizInfoConf(bizInfo.getId());
		for(BizInfoConf biz : list){
			String target = null;
			String taskAssignee = biz.getTaskAssignee();
			if(StringUtils.isNotBlank(taskAssignee)){
				if(taskAssignee.startsWith("GROUP:")){
					String group = taskAssignee.split("\\:")[1].split("\\,")[1];
					PageHelper<Map<String,Object>> page = new PageHelper<Map<String,Object>>();
					page.setPage(-1);
					page.setRows(-1);
					List<String> roleList = new ArrayList<String>();
					roleList.add(group);
					List<Map<String,Object>> userList = this.roleService.loadUsersByRole(page,roleList).getList();
					if(userList != null && !userList.isEmpty()){
						for(Map<String,Object> map : userList){
							target = (String)map.get(targetType);
							if(StringUtils.isNotBlank(target))
								targetList.add(target);
						}
					}
				}else{
					target = this.roleService.loadUsersByUserName(taskAssignee).get(targetType);
					if(StringUtils.isNotBlank(target))
						targetList.add(target);
				}
			}
		}
	}
	
	@Override
	@Transactional
	public void sendMessage(List<String> bizIds,String message){
		
		BizInfo bizInfo = null;
		for(String bizId : bizIds){
			if(StringUtils.isNotBlank(bizId)){
				bizInfo = this.dao.getByBizId(bizId);
				if(bizInfo != null){
					bizInfo.setPressCount(bizInfo.getPressCount()+1);
					this.dao.update(bizInfo);
					StringBuilder info = new StringBuilder(" 请及时处理工单 : ").append(bizInfo.getBizId());
					info.append(" ").append(message);
					List<String> targetList = new ArrayList<String>();
					this.getTargetList(bizInfo, targetList, "mobile");
						
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void removeLeft(List<String> bizIds){
		
		if(bizIds != null && !bizIds.isEmpty()){
			for (String bizId : bizIds) {
				BizInfo bizInfo = this.get(bizId);
				if(bizInfo != null){
					bizInfo.setIsShuffle(0);
					this.update(bizInfo);
				}
			}
		}
	}
	
	@Override
	public List<BizInfo> getBizInfos(List<String> list) {

		return this.dao.getBizInfos(list);
	}

}
