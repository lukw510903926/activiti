package com.eastcom.esflow.launcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ImportResource("applicationContext.xml")
@ComponentScan("com.eastcom.esflow")
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter{
	
	private static Log log = LogFactory.getLog(Application.class);
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		log.info("springboot start success");
	}
}