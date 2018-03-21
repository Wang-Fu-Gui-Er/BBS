package com.yx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yx.config.FreemarkerUtils;
import com.yx.po.Article;
import com.yx.po.ArticleValue;
import com.yx.po.User;
import com.yx.service.ArticleServiceImpl;
import com.yx.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(
        name = "articleController", urlPatterns = {"/article"},
        initParams = {
                @WebInitParam(name = "show", value = "show.ftl"),
                @WebInitParam(name = "welcome", value = "/welcome")
        }
)
public class ArticleController extends HttpServlet {
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private UserServiceImpl userService;

    private Map<String,String> map = new HashMap<String, String>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("show",config.getInitParameter("show"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action){
            case "queryAllArticle" : //查询主帖,分页
                queryAll(req,resp);
                break;
            case "delZC" :// 删除主从帖
                delArticlesByArtid(req,resp);
                break;
            case "add" :// 增加主帖
                addZhuArticle(req,resp);
                break;
            case "queryCArt" :// 查找每个主帖的从帖
                queryCArt(req,resp);
                break;
            case "delCArt" :// 删除从帖
                delCArticle(req,resp);
                break;
            case "reply" :// 增加从帖
                addCArticle(req,resp);
                break;
        }
    }
    private void addCArticle(HttpServletRequest req, HttpServletResponse resp) {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String rootid = req.getParameter("rootid");//主帖artid
        String userid = req.getParameter("userid");
        Article article = new Article();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        article.setRootid(Integer.parseInt(rootid));
        article.setTitle(title);
        article.setContent(content);
        article.setTime(timestamp);
        //User u = userService.findOne(Integer.parseInt(userid));
        User u = (User)req.getSession().getAttribute("loginUser");
        article.setUser(u);
        Article result = articleService.save(article);
        queryCArt(req,resp);//刷新该主帖的全部从帖
    }

    private void delCArticle(HttpServletRequest req, HttpServletResponse resp) {
        String artid = req.getParameter("artid");
        articleService.delete(Integer.parseInt(artid));//删除从帖
        queryCArt(req,resp);//刷新该主帖的全部从帖
    }
    private void queryCArt(HttpServletRequest req, HttpServletResponse resp) {
        Map<String,Object> vmap = new HashMap<String, Object>();
        String rootid = req.getParameter("rootid");
        if (rootid != null){
            // 全部从帖
            List<Article> articles = articleService.queryArticleByRootid(Integer.parseInt(rootid));
            Iterator<Article> iterator = articles.iterator();
            List<ArticleValue> articleValues = new ArrayList<ArticleValue>();
            while (iterator.hasNext()){
                Article article = iterator.next();
                ArticleValue value = new ArticleValue();
                value.setArtid(article.getArtid());
                value.setRootid(article.getRootid());
                value.setTitle(article.getTitle());
                value.setContent(article.getContent());
                value.setTime(article.getTime());
                value.setUserid(article.getUser().getUserid());
                articleValues.add(value);
            }
            vmap.put("list",articleValues);
            // 主帖
            Article a = articleService.queryArticleByArtid(Integer.parseInt(rootid));
            vmap.put("title",a.getTitle());
            vmap.put("userid",a.getUser().getUserid());
            String json = JSON.toJSONString(vmap,true);
            System.out.println(json);
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");
            try {
                PrintWriter out = resp.getWriter();
                out.print(json);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addZhuArticle(HttpServletRequest req, HttpServletResponse resp) {
        String userid = req.getParameter("userid");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        int rootid = 0;
        Article article = new Article();
        article.setRootid(rootid);
        article.setTitle(title);
        article.setContent(content);
        article.setTime(timestamp);
        User u = userService.findOne(Integer.parseInt(userid));
        article.setUser(u);
        Article result = articleService.save(article);
        FreemarkerUtils.servletFrowad(req,resp);
    }

    private void delArticlesByArtid(HttpServletRequest req, HttpServletResponse resp) {
        String artid = req.getParameter("artid");
        String rootid = artid;
        articleService.delArticles(Integer.parseInt(artid), Integer.parseInt(rootid));
        FreemarkerUtils.servletFrowad(req,resp);
    }

    private void queryAll(HttpServletRequest req, HttpServletResponse resp) {
        Map<String,Object> vmap = new HashMap<String, Object>();
        this.handleSessionValue(vmap,req);
        // 分页设置
        int curPage = Integer.parseInt(req.getParameter("curPage"));//当前页
        //int pageSize = 5;
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        Sort sort = new Sort(Sort.Direction.DESC,"artid");
        PageRequest pageable = new PageRequest(curPage,pageSize,sort);
        int rootid = 0;
        Page<Article> articles = articleService.queryArticlesByPage(pageable,rootid);
        vmap.put("articles",articles);
        vmap.put("pageSize",pageSize);
        FreemarkerUtils.forward(resp,map.get("show"),vmap);
    }

    // 把处理信息传给ftl页
    private void handleSessionValue(Map<String,Object> vmap, HttpServletRequest req){
        User loginUser = (User)req.getSession().getAttribute("loginUser");
        Object loginMsg =  req.getSession().getAttribute("loginMsg");
        Object regMsg =  req.getSession().getAttribute("regMsg");
        if (loginUser != null){
            vmap.put("loginUser",loginUser);
        }
        if (loginMsg != null){
            vmap.put("loginMsg",loginMsg.toString());
            req.getSession().setAttribute("loginMsg","");
        }
        if (regMsg != null){
            vmap.put("regMsg",regMsg.toString());
            req.getSession().setAttribute("regMsg","");
        }
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
