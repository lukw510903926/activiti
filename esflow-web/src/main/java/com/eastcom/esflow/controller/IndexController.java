package com.eastcom.esflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

	@RequestMapping(value = "/{name}")
	public String goIndex(@PathVariable String name) {
		
		return "index/" + name;
	}
}
