package com.example.appfileuploaddownload.repository;

import com.example.appfileuploaddownload.entity.Attachment;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {
}
