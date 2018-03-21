package com.yx.controller;

import com.yx.config.FreemarkerUtils;
import com.yx.po.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "WelcomeController", urlPatterns = {"/welcome"},
        initParams = {
                @WebInitParam(name = "show",
                        value = "article?action=queryAllArticle&curPage=0&pageSize=5")
        }
)
public class WelcomeController extends HttpServlet {
    private Map<String,String> map = new HashMap<String,String>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("show",config.getInitParameter("show"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //FreemarkerUtils.forward(resp,map.get("show"),null);
        RequestDispatcher dispatcher = req.getRequestDispatcher(map.get("show"));
        dispatcher.forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
