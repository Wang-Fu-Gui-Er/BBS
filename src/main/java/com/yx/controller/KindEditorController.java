package com.yx.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 源文件 static.editor.plugins.image.image.js
 * kindupload ： 源文件中设置的请求地址
 * dir : 源文件中设置的目录
 */
@WebServlet(name = "/file", urlPatterns = {"/kindupload"})
public class KindEditorController extends HttpServlet {
    private Map<String,String> types = new HashMap<String,String>();
    public KindEditorController(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = uploadPic(req);
        PrintWriter out = resp.getWriter();
        out.print(s);
        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public String uploadPic(HttpServletRequest req) {    //发帖上传图片

        CommonsMultipartResolver cmr =
                new CommonsMultipartResolver(req.getServletContext());  //应用层有效
        if (cmr.isMultipart(req)) {   //流的形式，不是键值对的形式
            FileItemFactory factory = new DiskFileItemFactory();    //文件工厂
            ServletFileUpload upload = new ServletFileUpload(factory);  //上传组件
            upload.setHeaderEncoding("utf-8");  //允许上传图片名有中文
            try {
                FileItemIterator iterator = upload.getItemIterator(req);//Iterator迭代器
                while (iterator.hasNext()) { //遍历文件工厂
                    FileItemStream fileItemStream = iterator.next(); //获取将要上传文件
                    fileItemStream.getFieldName();
                    InputStream inputStream = fileItemStream.openStream();//打开输入流
                    //判断是流形式文件，且文件大小大于0
                    if (!fileItemStream.isFormField() && fileItemStream.getName().length() > 0) {
                        //<code>false</code> if it represents an uploaded file.
                        //@return The original filename
                        String contentType = fileItemStream.getContentType();
                        if (!types.containsKey(contentType)) {
                            return "文件格式错误，请重新上传！";
                        }
                        //上传到服务器的绝对路径（src）
                        String mainPath = this.getClass().getResource("/").getPath();
                        //文件上传后的名字，全球唯一编码
                        String id = UUID.randomUUID().toString();
                        //上传后的文件目录  image
                        String dir = req.getParameter("dir");
                        //上传后完整路径
                        String filePath = mainPath + "static/editor/upload/" +
                                dir + "/" + id + types.get(contentType);
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        BufferedOutputStream bos = new BufferedOutputStream(
                                new FileOutputStream(new File(filePath)));
                        String tPath = req.getRequestURL().toString();  //请求路径
                        tPath = tPath.substring(0,tPath.lastIndexOf("/"));
                        String finalPath = tPath + "/editor/upload/"+dir+"/";//最终显示的路径
                        Streams.copy(bis,bos,true);
                        JSONObject jo = new JSONObject();
                        jo.put("error",0);  //无错误
                        //使用json格式把上传文件信息传递到前端
                        jo.put("url",finalPath+id+types.get(contentType));
                        return jo.toJSONString();
                    }
                }

            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}

