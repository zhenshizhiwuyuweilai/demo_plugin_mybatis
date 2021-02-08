package com.lansoft.utils;

import java.io.File;

/**
 * 文件相关工具类
 *
 * @Author 郭伟东
 * @Date 2021/2/8  17:46
 */
public class FileUtil {
    public static void mkdirs(String value) {
        File dir = new File(value);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result) {
                System.out.println(("创建目录： [" + value + "]"));
            }
        }
    }
}
