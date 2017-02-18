/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.activiti.web.controller.act;

import com.activiti.core.common.utils.DataGrid;
import com.activiti.core.common.utils.DateUtils;
import com.activiti.core.common.utils.Json;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.service.impl.ProcessModelServiceImpl;
import org.activiti.engine.repository.Model;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程模型相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "/act/model")
public class ActModelController{

	@Autowired
	private ProcessModelServiceImpl processModelService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 流程模型列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public DataGrid modelList(PageHelper<Model> page ,String category, org.springframework.ui.Model model) {

		DataGrid grid = new DataGrid();
		try {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			PageHelper<Model> helper = processModelService.modelList(page, category);
			List<Model> tempResult = helper.getList();
			if (CollectionUtils.isNotEmpty(tempResult)) {
				for (Model temp : tempResult) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", temp.getId());
					item.put("key", temp.getKey());
					item.put("name", temp.getName());
					item.put("version", temp.getVersion());
					item.put("createTime", DateUtils.formatDateTime(temp.getCreateTime()));
					item.put("lastUpdateTime", DateUtils.formatDateTime(temp.getLastUpdateTime()));
					result.add(item);
				}
			}
			grid.setRows(result);
			grid.setTotal(helper.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grid;
	}

	/**
	 * 创建模型
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(String name, String key, String description, String category, HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model model) {
		try {
			Model modelData = processModelService.create(name, key, description, category);
			model.addAttribute("message", "success");
			model.addAttribute("modelId", modelData.getId());
		} catch (Exception e) {
			logger.error("创建模型失败：", e);
		}
		return "process/act/actModelCreate";
	}

	/**
	 * 根据Model部署流程
	 */
	@RequestMapping(value = "deploy")
	@ResponseBody
	public Json deploy(String id, RedirectAttributes redirectAttributes) {
		Json json = new Json();
		try {
			String procdefId = processModelService.deploy(id);
			json.setSuccess(true);
			if (StringUtils.isNotBlank(procdefId)) {
				json.setMsg("部署成功," + procdefId);
			} else {
				json.setMsg("部署失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败!");
		}
		return json;
	}

	/**
	 * 导出model的xml文件
	 */
	@RequestMapping(value = "export")
	public void export(String id, HttpServletResponse response) {
		processModelService.export(id, response);
	}

	/**
	 * 更新Model分类
	 */
	@RequestMapping(value = "updateCategory")
	public String updateCategory(String id, String category, RedirectAttributes redirectAttributes) {
		processModelService.updateCategory(id, category);
		redirectAttributes.addFlashAttribute("message", "设置成功，模块ID=" + id);
		return "redirect:/act/model";
	}

	/**
	 * 删除Model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Json delete(String id) {
		logger.info("删除Model---delete");
		Json json = new Json();
		try {
			processModelService.delete(id);
			json.setSuccess(true);
			json.setMsg("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("删除失败!");
		}
		return json;
	}
}
