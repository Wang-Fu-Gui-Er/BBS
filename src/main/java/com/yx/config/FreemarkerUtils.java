package com.yx.config;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Freemarker全局配置，由首页控制WelcomeController,将页面转到Freemarker工具集，
 * 解析页面，跳转到相应的页面
 * 实现页面的跳转
 */
public class FreemarkerUtils {
    private static Configuration configuration = null;
    //获得Configuration对象
    private static Configuration buildConfiguration() {
        if (configuration == null) {
            //初始化，创建对象
            configuration = new Configuration(Configuration.VERSION_2_3_27);
            //根目录src
            String path = FreemarkerUtils.class.getResource("/").getPath();
            //templates路径
            File file = new File(path + File.separator + "templates");
            try {
                //模板加载
                configuration.setDirectoryForTemplateLoading(file);
                //设置默认编码格式
                configuration.setDefaultEncoding("utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configuration;
    }

    /**
     * 设置页面跳转
     * @param response 响应对象
     * @param targetView 目标页 name.ftl
     * @param vmap 传值的载体
     */
    public static void forward(HttpServletResponse response,
                               String targetView,
                               Map<String, Object> vmap
    ) {
        try {
            //要跳转到的目标页
            Template template = buildConfiguration().getTemplate(targetView);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            template.process(vmap, out);//核心方法,把响应流和参数传递到目标页面
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转到Servlet控制器,WelcomeController
    public static void servletFrowad(HttpServletRequest req, HttpServletResponse resp){
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/welcome");
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
