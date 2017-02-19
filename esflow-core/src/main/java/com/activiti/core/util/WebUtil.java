package com.activiti.core.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.activiti.core.common.utils.LoginUser;

public class WebUtil {
	
	private static final ThreadLocal<LoginUser> loginUsers = new ThreadLocal<LoginUser>();
	
	public static final String LOGIN_USER = "LOGIN_USER";
    
    public static LoginUser getLoginUser(){
        return loginUsers.get();
    }

	public static LoginUser getLogoUser(HttpServletRequest request, HttpServletResponse response) {

		LoginUser user = (LoginUser) request.getSession().getAttribute(WebUtil.LOGIN_USER);
		loginUsers.set(user);
		return user;
	}

	public static Map<String,String> userToMap(Map<String,String> user){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", user.get("username"));
		map.put("name", user.get("fullname"));
		map.put("mobile", user.get("mobile"));
		map.put("dep", user.get("dept"));
		map.put("email", user.get("email"));
		String description= user.get("description");
		if(StringUtils.isNotBlank(description)){
			String[] strings = description.split("\\.");
			map.put("city", strings[0]);
		}
		return map;
	}
}
