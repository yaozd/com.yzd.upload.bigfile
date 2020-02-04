package com.yzd.bigfile.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author: yaozh
 * @Description:
 */
@Component
public class FileUtil {

    public static String uploadFilePath;
    public static String uploadFilePathTemp;

    @Value("${upload.file.path}")
    public void setUploadFilePath(String uploadFilePath) {
        FileUtil.uploadFilePath = getAbsolutePath(uploadFilePath);
    }

    @Value("${upload.file.path.temp}")
    public void setUploadFilePathTemp(String uploadFilePathTemp) {
        FileUtil.uploadFilePathTemp = getAbsolutePath(uploadFilePathTemp);
    }
    private String getAbsolutePath(String path){
        File file=new File(path);
        return file.getAbsolutePath();
    }
    public static String getFilePathOrCreate(String path) {
        File fileDirty = new File(path);
        if (!fileDirty.exists()) {
            fileDirty.mkdirs();
        }
        return path;
    }
    public static String getDownloadFileUrl(String downloadUrl,String filePath){
        String url=downloadUrl+filePath.replace(uploadFilePath,"");
        return url.replace("\\","/").replace("//","/");
    }
}
