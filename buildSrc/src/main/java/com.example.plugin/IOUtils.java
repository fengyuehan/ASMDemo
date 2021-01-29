package com.example.plugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author : zhangzf
 * date   : 2021/1/28
 * desc   :
 */
public class IOUtils {

    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[4096];
        while ((len = is.read(buffer)) != -1){
            bos.write(buffer,0,len);
        }
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }
}
