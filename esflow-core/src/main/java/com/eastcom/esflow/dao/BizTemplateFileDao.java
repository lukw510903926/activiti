package com.eastcom.esflow.dao;

import com.eastcom.esflow.bean.BizTemplateFile;
import com.eastcom.esflow.common.dao.IBaseDao;
import com.eastcom.esflow.common.utils.PageHelper;

public interface BizTemplateFileDao extends IBaseDao<BizTemplateFile>{

	public PageHelper<BizTemplateFile> findTemplateFlies(PageHelper<BizTemplateFile> page,BizTemplateFile file,boolean islike);
}
