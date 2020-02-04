package com.yzd.bigfile.controller;

import com.yzd.bigfile.exception.ExceptionEnum;
import com.yzd.bigfile.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @Author: yaozh
 * @Description:
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@Controller
@Validated
@Slf4j
public class IndexController {


    @GetMapping("/")
    public String index() {
        return "webuploader";
    }

    @GetMapping("/webuploader")
    public String webuploader() {
        return "webupload";
    }

}
