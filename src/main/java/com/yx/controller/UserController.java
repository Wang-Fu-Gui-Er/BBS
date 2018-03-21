package com.yx.controller;

import com.yx.config.FreemarkerUtils;
import com.yx.config.PicHandleUtils;
import com.yx.po.User;
import com.yx.service.UserServiceImpl;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "userController", urlPatterns = {"/user"},
        initParams = {
                @WebInitParam(name = "show", value = "show.ftl"),
                @WebInitParam(name = "welcome", value = "/welcome")
        }
)
public class UserController extends HttpServlet {
    @Autowired
    private UserServiceImpl userService;
    Map<String, Object> vmap = null;
    private Map<String, String> map = new HashMap<String, String>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("show", config.getInitParameter("show"));
        map.put("welcome", config.getInitParameter("welcome"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (ServletFileUpload.isMultipartContent(req)) {
            action = "register";
        }
        switch (action) {
            case "login":
                login(req, resp);
                break;
            case "logout":
                logout(req, resp);
                break;
            case "headPic":
                getPic(req, resp);
                break;
            case "register":
                register(req, resp);
                break;
        }
    }


    //用户注册
    private void register(HttpServletRequest req, HttpServletResponse resp) {
        User u = userService.register(req);
        vmap = new HashMap<String, Object>();
        if (u != null) {
//            vmap.put("regMsg", "注册成功，请登录！");
            req.getSession().setAttribute("regMsg", "注册成功，请登录！");
        } else {
//            vmap.put("regMsg", "用户名已存在， 注册失败！");
            req.getSession().setAttribute("regMsg", "用户名已存在， 注册失败！");
        }
        //vmap.put("loginMsg", "");
        //FreemarkerUtils.forward(resp, map.get("show"), vmap);
        req.getSession().setAttribute("loginMsg", "");
        //this.servletFrowad(req,resp);
        FreemarkerUtils.servletFrowad(req,resp);
    }

    // 登录之后，显示头像
    private void getPic(HttpServletRequest req, HttpServletResponse resp) {
        String userid = req.getParameter("userid");
        User u = userService.findOne(Integer.parseInt(userid));
        String picPath = u.getPicpath();
        int start = picPath.lastIndexOf(".");
        String picType = picPath.substring(start + 1, picPath.length());
        resp.setContentType(PicHandleUtils.getPicType(picType));
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            os.write(u.getPic());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 用户退出登录
    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();//会话失效
        //FreemarkerUtils.forward(resp, map.get("show"), null);
        //this.servletFrowad(req,resp);
        FreemarkerUtils.servletFrowad(req,resp);
    }

    // 用户登录
    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User u = userService.login(username, password);
        vmap = new HashMap<String, Object>();
        if (u != null) { // 对登录进行处理
            String sun = req.getParameter("sun");
            if (sun != null) {   // 选中记住七天
                try {
                    String cusername = URLEncoder.encode(username, "utf-8");
                    String cpassword = URLEncoder.encode(password, "utf-8");
                    Cookie cu = new Cookie("myusername", cusername);
                    cu.setMaxAge(60 * 60 * 24 * 7);
                    Cookie cp = new Cookie("mypassword", cpassword);
                    cp.setMaxAge(60 * 60 * 24 * 7);
                    resp.addCookie(cu);
                    resp.addCookie(cp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //vmap.put("loginUser",u);
            req.getSession().setAttribute("loginUser",u);
        } else {
            //vmap.put("loginMsg","用户不存在！");
            req.getSession().setAttribute("loginMsg","用户不存在！");
        }
//        FreemarkerUtils.forward(resp,map.get("show"),vmap);
        //this.servletFrowad(req,resp);
        FreemarkerUtils.servletFrowad(req,resp);
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

