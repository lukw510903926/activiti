package com.activiti.core.service;

import com.activiti.core.common.utils.PageHelper;
import org.activiti.engine.repository.Model;

import javax.servlet.http.HttpServletResponse;

public interface IProcessModelService {

    /**
     * 流程模型列表
     */
    PageHelper<Model> modelList(PageHelper<Model> page, String category);

    /**
     * 根据Model部署流程
     */
    String deploy(String id);

    /**
     * 导出model的xml文件
     */
    void export(String id, HttpServletResponse response);

    /**
     * 更新Model分类
     */
    void updateCategory(String id, String category);

    /**
     * 删除模型
     *
     * @param id
     * @return
     */
    void delete(String id);
}
