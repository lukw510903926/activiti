package com.activiti.core.service.impl;

import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.service.IProcessModelService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 流程模型相关Controller
 *
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
public class ProcessModelServiceImpl implements IProcessModelService {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程模型列表
     */
    @Override
    public PageHelper<Model> modelList(PageHelper<Model> page, String category) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
        if (StringUtils.isNotEmpty(category)) {
            modelQuery.modelCategory(category);
        }
        page.setTotal(modelQuery.count());
        page.setList(modelQuery.listPage(page.getFirstRow(), page.getMaxRow()));
        return page;
    }

    /**
     * 根据Model部署流程
     */
    @Override
    @Transactional
    public String deploy(String id) {
        String procdefId = "";
        try {
            org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            com.fasterxml.jackson.databind.JsonNode editorNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            String processName = modelData.getName();
            if (!StringUtils.endsWith(processName, ".bpmn20.xml")) {
                processName += ".bpmn20.xml";
            }
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addInputStream(processName, in).deploy();
            // 设置流程分类
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            for (ProcessDefinition processDefinition : list) {
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
                procdefId = processDefinition.getId();
            }
            if (CollectionUtils.isEmpty(list)) {
                procdefId = "";
            }
        } catch (Exception e) {
            throw new ActivitiException("设计模型图不正确，检查模型正确性，模型ID=" + id, e);
        }
        return procdefId;
    }

    /**
     * 导出model的xml文件
     *
     * @throws IOException
     */
    @Override
    public void export(String id, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ActivitiException("导出model的xml文件失败，模型ID=" + id, e);
        }

    }

    /**
     * 更新Model分类
     */
    @Override
    @Transactional
    public void updateCategory(String id, String category) {
        org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
        modelData.setCategory(category);
        repositoryService.saveModel(modelData);
    }

    /**
     * 删除模型
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void delete(String id) {
        repositoryService.deleteModel(id);
    }

}
