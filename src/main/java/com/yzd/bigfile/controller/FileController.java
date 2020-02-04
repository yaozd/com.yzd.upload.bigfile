package com.yzd.bigfile.controller;

import com.yzd.bigfile.exception.ExceptionEnum;
import com.yzd.bigfile.exception.ServiceException;
import com.yzd.bigfile.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author: yaozh
 * @Description:
 */
@Controller
@Validated
@Slf4j
public class FileController {

    /**
     * 分片上传
     *
     * @return ResponseEntity<Void>
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Void> decrypt(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, Integer chunks, Integer chunk, String name, String guid) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            if (file == null) {
                throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
            }
            System.out.println("guid:" + guid);
            if (chunks == null && chunk == null) {
                chunk = 0;
            }
            String filePath=FileUtil.getFilePathOrCreate(FileUtil.uploadFilePathTemp + File.separator + guid);
            File outFile = new File(filePath, chunk + ".part");
            InputStream inputStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, outFile);
        }
        return ResponseEntity.ok().build();
    }
    /**
     * 合并所有分片
     *
     * @throws Exception Exception
     */
    @GetMapping("/merge")
    @ResponseBody
    public ResponseEntity<String> byteMergeAll(String guid,String fileName) throws Exception {
        System.out.println("merge:" + guid);
        String fileDownloadUrl = null;
        File file = new File(FileUtil.uploadFilePathTemp + File.separator + guid);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                String filePath=FileUtil.getFilePathOrCreate(FileUtil.uploadFilePath + File.separator+guid);
                File partFile = new File(filePath,fileName);
                String fileAbsolutePath=partFile.getAbsolutePath();
                System.out.println("file:"+fileAbsolutePath);
                for (int i = 0; i < files.length; i++) {
                    File s = new File(FileUtil.uploadFilePathTemp + File.separator + guid, i + ".part");
                    try(FileOutputStream destTempStream = new FileOutputStream(partFile, true)){
                        FileUtils.copyFile(s, destTempStream);
                    }
                }
                FileUtils.deleteDirectory(file);
                fileDownloadUrl=FileUtil.getDownloadFileUrl(DOWNLOAD_URL_PREFIX,fileAbsolutePath);
            }
        }
        lastFileDownloadUrl=fileDownloadUrl;
        return  ResponseEntity.ok(fileDownloadUrl);
    }
    public static final String DOWNLOAD_URL_PREFIX="/download/";

    /**
     * 文件下载
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/download/**")
    public ResponseEntity<Void> download(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String filePath= URLDecoder.decode(request.getRequestURI(),StandardCharsets.UTF_8.name()).replace(DOWNLOAD_URL_PREFIX,"/");
        // 文件地址，真实环境是存放在数据库中的
        File file = new File(FileUtil.uploadFilePath,filePath);
        if (!file.exists()) {
            throw new ServiceException(ExceptionEnum.FILE_NOT_EXIST);
        }
        // 穿件输入对象
        try (FileInputStream fis = new FileInputStream(file)) {
            // 设置相关格式
            response.setContentType("application/force-download");
            //设置文件长度
            response.addHeader("Content-Length", String.valueOf(file.length()));
            //下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
            String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
            // 设置下载后的文件名以及header
            response.addHeader("Content-disposition", "attachment;fileName=" + fileName);
            // 创建输出对象
            try (OutputStream os = response.getOutputStream()) {
                // 常规操作
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = fis.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 下载最新上传的文件
     *
     */
    private String lastFileDownloadUrl;
    @GetMapping("/downloadLastFile")
    public void downloadLastFile(HttpServletResponse response) throws IOException {
        if(lastFileDownloadUrl==null){
            throw new ServiceException(ExceptionEnum.FILE_NOT_EXIST);
        }
        response.sendRedirect(lastFileDownloadUrl);
    }
}
