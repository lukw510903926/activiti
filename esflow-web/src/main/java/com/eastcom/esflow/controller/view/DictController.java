package com.eastcom.esflow.controller.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eastcom.esflow.bean.Dict;
import com.eastcom.esflow.common.utils.Json;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.IDictService;

@Controller
@RequestMapping(value = "/dict")
public class DictController{
	
	@Autowired
	private IDictService dictService;

	/**
	 * 字典管理视图
	 * @return
	 */
	@RequestMapping(value = "/config")
	public String dictView() {
		return "dict/dictConfigList";
	}
	/**
	 * 新增/编辑字典数据视图
	 * @return
	 */
	@RequestMapping(value = "/addDataUI")
	public String addDataView(String dataId,String dictId,Model model) {
		model.addAttribute("dictId",dictId);
		if(StringUtils.isNotEmpty(dataId)){
			Map<String,Object> map = dictService.findDictData(dictId, dataId);
			model.addAttribute("dataId",dataId);
			model.addAttribute("name",map.get("name"));
		}
		return "dict/addDictData";
	}
	
	//--------------------------字典配置管理-------------------------------------
	/**
	 * 新增/编辑字典管理视图
	 * @return
	 */
	@RequestMapping(value = "/addUI/{operateType}")
	public String addView(@PathVariable String operateType,String dictId,Model model) {
		model.addAttribute("operateType", operateType);
		if(StringUtils.equals(operateType, "edit")){
			Dict dict = dictService.getDict(dictId);
			model.addAttribute("dictId",dict.getId());
			model.addAttribute("dictName",dict.getDictName());
			model.addAttribute("status",dict.getStatus());
		}
		return "dict/addDictConfig";
	}
	
	/**
	 * 字典数据列表视图
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String dictDataView(HttpServletRequest request, @RequestParam Map<String, String> params,Model model) {
		model.addAttribute("dictId", params.get("dictId"));
		return "dict/dictDataList";
	}
	
	/**
	 * 字典配置列表
	 * @param page
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/findDictConfig")
	@ResponseBody
	public Map<String,Object> findDictConfig(PageHelper<Map<String, Object>> page,@RequestParam Map<String,Object> params){
		Map<String,Object> map = new HashMap<String, Object>();
		PageHelper<Map<String, Object>> helper = dictService.findDictConfig(page, params);
		if (helper != null) {
			map.put("total", helper.getTotal());
			map.put("rows", helper.getList());
		}
		return map;
	}
	
	/**
	 * 启用状态字典配置列表
	 * @param page
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/findDictEnable")
	@ResponseBody
	public List<Dict> findDictEnable(){
		return dictService.findDictEnable();
	}
	
	
	@RequestMapping(value="/addDict")
	@ResponseBody
	public Json addDictConfig(@RequestParam String id,String dictName,Integer status){
		Json resJson = new Json();
		Dict dict = new Dict();
		try {
			dict.setId(id);
			dict.setDictName(dictName);
			dict.setStatus(status);
			dict.setUpdateUser("admin");
			dictService.addDictConfig(dict);
			resJson.setSuccess(true);
			resJson.setMsg("操作成功!");
		} catch (Exception e) {
			resJson.setSuccess(false);
			resJson.setMsg("操作失败!"+e.getLocalizedMessage());
		}
		
		return resJson;
	}
	
	@RequestMapping(value="/delDict")
	@ResponseBody
	public Json delDictConfig(@RequestParam List<String> ids){
		Json resJson = new Json();
		try {
			dictService.delDictConfig(ids);
			resJson.setSuccess(true);
			resJson.setMsg("操作成功!");
		} catch (Exception e) {
			resJson.setSuccess(false);
			resJson.setMsg("操作失败!"+e.getLocalizedMessage());
		}
		
		return resJson;
	}
	//--------------------------------字典数据管理-----------------------------------------------------
	/**
	 * 字典数据列表
	 * @param page
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/findDictData")
	@ResponseBody
	public Map<String,Object> findDictData(String dictId,PageHelper<Map<String, Object>> page,Map<String,Object> params){
		Map<String,Object> map = new HashMap<String, Object>();
		PageHelper<Map<String, Object>> helper = dictService.findDictData(dictId,page, map);
		if (helper != null) {
			map.put("total", helper.getTotal());
			map.put("rows", helper.getList());
		}
		return map;
	}
	
	/**
	 * 字典数据列表
	 * @param page
	 * @param params
	 * @return
	 */
	/*@RequestMapping(value = "/findDictDataByParam")
	@ResponseBody
	public List<Map<String,Object>> findDictDataByParam(PageHelper<Map<String, Object>> page,Map<String,Object> params){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("dictId", "4028949b5262274d01526229189e0000");
		map.put("dictName", "");
		
		return dictService.loadDictDataByParam(map);
	}*/
	
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Json importDictData(String dictId,MultipartFile files, HttpServletRequest request,
			HttpServletResponse response){
		Json resJson = new Json();
//		SessionInfo ssi = SSOUtil.getInstance(request, response);
//		String userName = ssi.getName();
		try {
			dictService.importDictData(dictId, "admin", files);
			resJson.setMsg("导入成功");
			resJson.setSuccess(true);
		} catch (Exception e) {
			resJson.setMsg("导入失败:"+e.getLocalizedMessage());
			resJson.setSuccess(true);
		}
		return resJson;
	}
	
	@RequestMapping("/addDictData")
	@ResponseBody
	public Json addDictData(@RequestParam String dictId,String dataId,String name, HttpServletRequest request,
			HttpServletResponse response){
		Json resJson = new Json();
//		SessionInfo ssi = SSOUtil.getInstance(request, response);
//		String userName = ssi.getName();
		try {
			if(StringUtils.isEmpty(dataId)||dataId.equals("null")){
				dictService.addDictData(dictId, "admin", name);
			}else{
				dictService.editDictData(dictId,dataId, "admin", name);
			}
			resJson.setMsg("操作成功");
			resJson.setSuccess(true);
		} catch (Exception e) {
			resJson.setMsg("操作失败:"+e.getLocalizedMessage());
			resJson.setSuccess(true);
		}
		return resJson;
	}
	
	@RequestMapping("/delDictData")
	@ResponseBody
	public Json delDictData(@RequestParam String dictId,@RequestParam(required=false) List<String> ids,String isAll){
		Json resJson = new Json();
		try {
			if(StringUtils.equals(isAll, "true")){
				dictService.delDictDataAll(dictId);
			}else{
				dictService.delDictData(dictId, ids);
			}
			resJson.setMsg("删除成功");
			resJson.setSuccess(true);
		} catch (Exception e) {
			resJson.setMsg("删除失败:"+e.getLocalizedMessage());
			resJson.setSuccess(true);
		}
		return resJson;
	}
	
}
