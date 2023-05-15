package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file){
        //当前file是一个临时文件，需要转存到指定位置，否则本次请求完成后会临时文件会删除自动
        log.info(file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名重复覆盖
        String fileName = UUID.randomUUID().toString()+suffix;

        //创建一个目录对象
        File dir = new File(basePath);

        //判断当前目录是否存在
        if (!dir.exists()){
            //mulu目录不存在需要创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /*文件下载
    */
     @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
            //输入流 通过输入流来读取文件内容
         FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));

         byte[] bytes=new byte[1024];
         int len=0;
     response.setContentType("image/jpeg");
         //输出流 通过输出流 文件写回游览器
        ServletOutputStream outputStream = response.getOutputStream();



         while ((len=fileInputStream.read(bytes )) != -1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
         }
         outputStream.close();
         fileInputStream.close();
    }
}
