package com.activiti.web.controller.view;

import com.activiti.core.common.utils.DataGrid;
import com.activiti.core.common.utils.Json;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.service.IBizHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/bizHandle")
public class BizHandleController{

	private Logger logger = Logger.getLogger("bizHandle");
	
	@Autowired
	private IBizHandleService bizHandleService;
	
	/**
	 * 人员列表
	 * @param page
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/loadMembers")
	@ResponseBody
	public DataGrid loadMembers(PageHelper<Map<String, Object>> page, @RequestParam Map<String,Object> params){

		logger.info("params : " + params);
		try {
			String  value = null;
			for (String key : params.keySet()) {
				value = URLDecoder.decode((String) params.get(key),"UTF-8");
				params.put(key, value);
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		PageHelper<Map<String, Object>> helper = bizHandleService.loadMembers(page, params);
		DataGrid grid = new DataGrid();
		if (helper != null) {
			grid.setRows(helper.getList());
			grid.setTotal(helper.getTotal());
		}
		return grid;
	}
	
	/**
	 * 部门列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/loadSectors")
	@ResponseBody
	public Json loadSectors(@RequestParam Map<String,Object> params){
		Json res = new Json();
		res.setSuccess(false);
		try {
			List<Map<String,Object>> list =  bizHandleService.loadSectors(params);
			if(list !=null && list.size()>0){
				res.setObj(list);
			}
			res.setSuccess(true);
			
		} catch (Exception e) {
			res.setMsg(e.getLocalizedMessage());
		}
		return res;
	}
}
