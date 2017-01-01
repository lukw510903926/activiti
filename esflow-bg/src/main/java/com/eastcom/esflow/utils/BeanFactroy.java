package com.eastcom.esflow.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.eastcom.esflow.util.MailUtil;

@Component
public class BeanFactroy {

	@Value("${email.host}")
	private String host;
	
	@Value("${email.userName}")
	private String userName;
	
	@Value("${email.password}")
	private String password;
	
	@Value("${unmp.jdbcUrl}")
	private String unmpUrl;
	
	@Value("${unmp.jdbcUsername}")
	private String unmpUsername;
	
	@Value("${unmp.jdbcPwd}")
	private String unmpPassword;
	
	@Value("${unmp.jdbcDriver}")
	private String driverClassName;
	
	@Bean
	public MailUtil mailUtil() {
		
		MailUtil mailUtil = new MailUtil();
		mailUtil.setHost(host);
		mailUtil.setUserName(userName);
		mailUtil.setPassword(password);
		return mailUtil;
	}
	
	@Bean
	@Primary
	public JdbcTemplate unmpJdbcTemplate(){
		
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(unmpUrl);  
        datasource.setUsername(unmpUsername);  
        datasource.setPassword(unmpPassword);  
        datasource.setDriverClassName(driverClassName);  
        datasource.setInitialSize(10);  
        datasource.setMinIdle(10);  
        datasource.setMaxActive(6000);  
        datasource.setMaxWait(6000);  
        datasource.setTimeBetweenEvictionRunsMillis(6000); 
        datasource.setMinEvictableIdleTimeMillis(3000); 
        datasource.setTestWhileIdle(true);  
        datasource.setTestOnBorrow(true);  
        datasource.setTestOnReturn(true);  
		JdbcTemplate unmpJdbcTemplate = new JdbcTemplate(datasource);
		return unmpJdbcTemplate;
	}
}
