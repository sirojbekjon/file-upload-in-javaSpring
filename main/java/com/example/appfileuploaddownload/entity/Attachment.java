package com.example.appfileuploaddownload.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fileOriginalName;

    private long size;  //2048000 byt

    private String contentType;  //application/pdf || image/png

    //BU FILE SISTEMGA SAQLASH UCHUN KERAK BO'LADI
    private String name;//PAPKANI ICHIDAN TOPISH UCHUN


}
