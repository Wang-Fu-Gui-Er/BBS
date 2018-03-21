package com.yx;

import com.yx.filter.EncodingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import javax.servlet.Filter;

@SpringBootApplication
@ServletComponentScan//扫描Servlet组件
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean encoderFilterRegistration(){
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(getFilter());	//设置过滤器
		frb.setName("Encoder1");
		frb.addUrlPatterns("/*");	//过滤所有请求
		frb.addInitParameter("encoder","utf-8");	//设置过滤码参数
		frb.setOrder(1);	//设置优先级
		return frb;
	}

	@Bean(name = "Encoder1")
	public Filter getFilter(){
		Filter filter = new EncodingFilter();
		return filter;
	}
}
