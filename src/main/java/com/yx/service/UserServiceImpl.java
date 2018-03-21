package com.yx.service;

import com.yx.dao.IUserDAO;
import com.yx.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UserServiceImpl {
    @Autowired
    private IUserDAO userDAO;

    //登录
    public User login(String username, String password){
        return userDAO.login(username,password);
    }
    //通过主键userid查找user
    public User findOne(Integer userid){
        return userDAO.findOne(userid);
    }

    // 注册
    public User register(HttpServletRequest request){
        //范围：在整个应用有效
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        cmr.setDefaultEncoding("utf-8");
        cmr.setResolveLazily(true);//延迟解析，防止文件过大，未上传完就开始解析
        cmr.setMaxInMemorySize(1024*1024*5);//设置缓存
        cmr.setMaxUploadSizePerFile(1024*1024*5);  //每个文件大小
        cmr.setMaxUploadSize(1024*1024*20);//文件和的大小
        //设置流的request
        MultipartHttpServletRequest mReq = cmr.resolveMultipart(request);
        MultipartFile multipartFile = mReq.getFile("file0");    //获得头像文件
        String name = multipartFile.getOriginalFilename();  //获得头像文件名及格式
        String path = "upload"+ File.separator +name;   //设置文件上传存储路径
        File file = new File(path);//包装成文件
        try {
            multipartFile.transferTo(file); //文件传输
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User();
        String reusername = mReq.getParameter("reusername");
        String repassword = mReq.getParameter("repassword");
        User tmpU = userDAO.queryByUsername(reusername);
        if (tmpU == null){
            user.setUsername(reusername);
            user.setPassword(repassword);
            user.setPicpath(path);
            user.setPagenum(10);
            try(InputStream fis = new FileInputStream(file)) {  //头像图片写入数据库
                byte [] buffer = new byte[fis.available()];
                fis.read(buffer);
                user.setPic(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return userDAO.save(user);
        } else {
            return null;
        }
    }
}
