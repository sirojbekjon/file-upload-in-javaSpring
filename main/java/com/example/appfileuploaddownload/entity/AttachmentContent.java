package com.example.appfileuploaddownload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttachmentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private byte[] asosiyContent; //ASOSIY CONTENT

    //select all from attachment_content where attachment_id=100
    @OneToOne
    private Attachment attachment;

}
