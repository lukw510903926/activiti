package com.activiti.web.controller;

import com.activiti.core.bean.BizTemplateFile;
import com.activiti.core.common.utils.Json;
import com.activiti.core.common.utils.LoginUser;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.service.BizTemplateFileService;
import com.activiti.core.util.WebUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
@RequestMapping("/bizTemplateFile")
public class BizTemplateFileController {
	
	@Value("${templateFilePath}")
	private String path ;
	
	@Autowired
	private BizTemplateFileService bizTemplateFileService;
	
	private Logger logger = Logger.getLogger("bizTemplateFileController");

	@RequestMapping("/index")
	public String index(){
		
		return "process/config/bizTemplateFileList";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public Map<String,Object> list(PageHelper<BizTemplateFile> page,BizTemplateFile file){
		
		PageHelper<BizTemplateFile> helper = bizTemplateFileService.findTemplateFlies(page,file,true);
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("total", helper.getTotal());
		data.put("rows", helper.getList());
		return data;
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public ResponseEntity<String> upload(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response){
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		Json json = new Json();
		try {
			if("".equals(file.getOriginalFilename())){
				json.setSuccess(false);
				json.setMsg("文件框不能为空!");
				ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
				return entity;
			}
			WebUtil.getLogoUser(request, response);
			LoginUser createUser = WebUtil.getLoginUser();
			String username = createUser.getUsername();
			String flowName = request.getParameter("flowName");
			logger.info("flowName : "+ flowName);
			BizTemplateFile bizTemplateFile = new BizTemplateFile();
			bizTemplateFile.setCreateUser(username);
			bizTemplateFile.setFullName(createUser.getName());
			bizTemplateFile.setFlowName(flowName);
			bizTemplateFileService.saveOrUpdate(bizTemplateFile,file);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("上传失败: " + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
			return entity;
		}
		json.setSuccess(true);
		json.setMsg("上传成功");
		ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
		return entity;
	}
	
	
	@ResponseBody
	@RequestMapping("/download")
	public void download(@RequestParam List<String> ids, HttpServletRequest request, HttpServletResponse response){
		
		try {
			if(ids.size()==1){
				BizTemplateFile file = bizTemplateFileService.get(ids.get(0));
				String fileName = file.getFileName();
				String suffix = "";
				if(fileName.lastIndexOf(".")!=-1){
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
				File inputfile = new File(path+File.separator+file.getId()+suffix);
				BufferedInputStream downfile = null;
				String headfilename = null;
				if(inputfile.exists()&&inputfile.isFile()){				
					headfilename = new String((file.getFileName()).getBytes("gb2312"),"ISO-8859-1");
					downfile = new BufferedInputStream(new FileInputStream(inputfile));		
				}else{
					headfilename = new String("错误报告.txt".getBytes("gb2312"),"ISO-8859-1");
				}
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename="+headfilename);
				BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
				if(downfile!=null){
					byte[] buff = new byte[512];
					while(downfile.read(buff)!=-1){
						outputStream.write(buff);
					}
					downfile.close();
				}else{
					outputStream.write("文件不存在!".getBytes());
				}
				outputStream.flush();
				outputStream.close();
			}else{
				File errorfile = null;
				PrintWriter erroeout = null;
				List<BizTemplateFile> fileList = bizTemplateFileService.findFileByIds(ids);
				File temp = new File(path + UUID.randomUUID().toString() + ".rar");
				FileOutputStream fileout = new FileOutputStream(temp);
				ZipOutputStream rar = new ZipOutputStream(fileout);
				rar.setEncoding("gbk");
				for(int i=0;i<fileList.size();i++){
					int num = 1;
					for(int j=i+1;j<fileList.size();j++){
						String filename = fileList.get(j).getFileName();
						if(fileList.get(i).getFileName().equals(filename)){
							int index = filename.lastIndexOf(".");
							if(index==-1){
								fileList.get(j).setFileName(filename+"("+num+")");
								num++;
							}else{
								fileList.get(j).setFileName(filename.substring(0,index)+"("+num+")"+filename.substring(index));
								num++;
							}
						}
					}
				}
				for(BizTemplateFile datafile : fileList){
					String fileName = datafile.getFileName();
					String suffix = "";
					if(fileName.lastIndexOf(".")!=-1){
						suffix = fileName.substring(fileName.lastIndexOf("."));
					}
					File file = new File(path+datafile.getId()+suffix);
					if(file.exists()&&file.isFile()){
						ZipEntry entry = new ZipEntry(fileName);
						rar.putNextEntry(entry);
						FileInputStream downfile = new FileInputStream(file);
						byte[] buff = new byte[512];
						while(downfile.read(buff)!=-1){
							rar.write(buff);
						}
						downfile.close();
					}else{
						if(errorfile==null){
							errorfile = new File(path+"错误报告.txt");
							erroeout = new  PrintWriter(new FileOutputStream(errorfile));
						}
						erroeout.println("文件:"+fileName+"\t不存在");
					}
				}
				if(errorfile!=null){
					erroeout.close();
					ZipEntry entry = new ZipEntry(errorfile.getName());
					rar.putNextEntry(entry);
					FileInputStream erroein = new FileInputStream(errorfile);
					byte[] buff = new byte[512];
					while(erroein.read(buff)!=-1){
						rar.write(buff);
					}
					erroein.close();
					errorfile.delete();
				}
				rar.close();
				fileout.close();
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-Disposition","attachment;filename="+(new String("文件合集.rar".getBytes("gb2312"),"ISO-8859-1")));
				ServletOutputStream outputStream = response.getOutputStream();
				FileInputStream filein = new FileInputStream(temp);
				byte[] buff = new byte[512];
				while(filein.read(buff)!=-1){
					outputStream.write(buff);
				}
				filein.close();
				outputStream.flush();
				outputStream.close();
				temp.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/downloadTemplate")
	@ResponseBody
	public void downloadTemplate(@RequestParam Map<String,String> params, HttpServletRequest request, HttpServletResponse response){
		
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			response.reset();
			response.setContentType("application/octet-stream;charset=UTF-8");
			params.put("fileName", URLDecoder.decode(request.getParameter("fileName"),"UTF-8"));
			BizTemplateFile file = bizTemplateFileService.getBizTemplateFile(params);
			String headfilename = null;
			if(file !=null){
				String fileName = file.getFileName();
				String suffix = "";
				if(fileName.lastIndexOf(".")!=-1){
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
				File inputfile = new File(path+File.separator+file.getId()+suffix);
				BufferedInputStream downfile = null;
				
				if(inputfile.exists()&&inputfile.isFile()){				
					headfilename = new String((file.getFileName()).getBytes("gb2312"),"ISO-8859-1");
					downfile = new BufferedInputStream(new FileInputStream(inputfile));		
				}else{
					headfilename = new String("错误报告.txt".getBytes("gb2312"),"ISO-8859-1");
				}
				response.setHeader("Content-Disposition", "attachment;filename="+headfilename);
				if(downfile!=null){
					byte[] buff = new byte[512];
					while(downfile.read(buff)!=-1){
						outputStream.write(buff);
					}
					downfile.close();
				}else{
					outputStream.write("文件不存在!".getBytes());
				}
			}else{
				logger.info(" templateFile is null ");
				headfilename = new String("错误报告.txt".getBytes("gb2312"),"ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment;filename="+headfilename);
				outputStream.write("文件不存在!请检查文件参数配置是否正确!".getBytes());
			}
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public Json remove(@RequestParam List<String> ids){
		
		Json json = new Json();
		try {
			bizTemplateFileService.deleteByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("删除失败: "+ (e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			return json;
		}
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}
}
