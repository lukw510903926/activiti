package com.activiti.core.dao;

import com.activiti.core.bean.BizTemplateFile;
import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;

public interface BizTemplateFileDao extends IBaseDao<BizTemplateFile>{

	public PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page,BizTemplateFile file,boolean islike);
}
