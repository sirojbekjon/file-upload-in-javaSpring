package com.example.appfileuploaddownload.controller;


import com.example.appfileuploaddownload.entity.Attachment;
import com.example.appfileuploaddownload.entity.AttachmentContent;
import com.example.appfileuploaddownload.repository.AttachmentContentRepository;
import com.example.appfileuploaddownload.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    AttachmentRepository attachmentRepository;
//github tomonidan o'zgartirildi
    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @PostMapping(value = "/upload")
    public String uploadFile(MultipartHttpServletRequest request) throws IOException {
        System.out.println(System.currentTimeMillis());
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file!=null) {

            //FILE HAQIDA MA'LUMOT OLISH UCHUN
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            Attachment attachment = new Attachment();
            attachment.setFileOriginalName(originalFilename);
            attachment.setContentType(contentType);
            attachment.setSize(size);

            Attachment savedAttachment = attachmentRepository.save(attachment);

            //FILENI CONTENT(BYTE[]) SAQLAYMIZ
            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAsosiyContent(file.getBytes());
            attachmentContent.setAttachment(savedAttachment);

            attachmentContentRepository.save(attachmentContent);
            System.out.println(System.currentTimeMillis());

            return "file saved. ID si:"+savedAttachment.getId();

        }
            return "File saqlashda hatolik";
    }





    //FILENI SERVERGA YUKLASH
    private final static String uploadDirectory="yuklanganlar";
    @PostMapping("/uploadSystem")
    public String uploadFileToFileSystem(MultipartHttpServletRequest request) throws IOException {
        System.out.println(System.currentTimeMillis());
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null){
            Attachment attachment = new Attachment();
            attachment.setFileOriginalName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            String originalFilename = file.getOriginalFilename();
            String[] split = originalFilename.split("\\.");

            String name = UUID.randomUUID().toString()+"."+split[split.length-1];
            attachment.setName(name);
            attachmentRepository.save(attachment);

            Path path = Paths.get(uploadDirectory+"/"+name);
            Files.copy(file.getInputStream(),path);
            System.out.println(System.currentTimeMillis());
            return "fayl saqlandi. ID si: "+attachment.getId();

        }
        return "saqlanmadi";
    }




    @GetMapping("/getFile/{id}")
    public void getFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()){

            Attachment attachment = optionalAttachment.get();
            Optional<AttachmentContent> contentOptional = attachmentContentRepository.findByAttachmentId(id);

            if (contentOptional.isPresent()){
                AttachmentContent attachmentContent = contentOptional.get();

                response.setHeader("Content-Disposition","attachment; filename=\""+attachment.getFileOriginalName()+"\"");
                response.setContentType(attachment.getContentType());

                FileCopyUtils.copy(attachmentContent.getAsosiyContent(),response.getOutputStream());

            }

        }

    }

    @GetMapping("/getFileSystem/{id}")
    public void getFileSystem(@PathVariable Integer id,HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()){
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition","attachment;filename=\""+attachment.getFileOriginalName()+"\"");
            response.setContentType(attachment.getContentType());
            FileInputStream fileInputStream = new FileInputStream(uploadDirectory+"/"+attachment.getName());
//            String name = attachment.getName();
            FileCopyUtils.copy(fileInputStream,response.getOutputStream());

        }
    }



}
