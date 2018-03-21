package com.yx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * config包是Boot的全局配置
 * MVC模式全局配置，设置默认首页，对静态页进行处理
 */

@Configuration  //表明是配置类
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    //设置默认首页
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").    //访问根目录时
                setViewName("forward:/welcome"); //跳转到某个控制器
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);//最高优先级
        super.addViewControllers(registry);   //?????作用？
    }


    //让Boot把指定目录下的所有静态页文件不要拦截，放行，推给浏览器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**"). //目录,static下的所有文件
                addResourceLocations("classpath:/static/"). //位置：目录下的文件，classpath即src
                resourceChain(true);    //资源链
    }
}