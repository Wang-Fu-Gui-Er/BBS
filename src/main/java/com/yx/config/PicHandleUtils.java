package com.yx.config;

import java.util.HashMap;
import java.util.Map;

public class PicHandleUtils {
    private static Map<String,String> types = new HashMap<String,String>();

    public static String getPicType(String type){
        types.put("jpg", "image/jpeg");
        types.put("gif", "image/gif");
        types.put("bmp", "image/x-ms-bmp");
        types.put("png", "image/png");
        return types.get(type);
    }
}
