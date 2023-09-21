package com.javajober.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Table(name = "file_block")
@Entity
public class FileBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_title", length = 100, nullable = false)
    private String fileTitle;

    @Lob
    @Column(name = "file_description",length = 1000, nullable = false)
    private String fileDescription;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    protected FileBlock() {

    }

    public FileBlock(final String fileTitle, final String fileDescription, final String fileName) {
        this.fileTitle = fileTitle;
        this.fileDescription = fileDescription;
        this.fileName = fileName;
    }
}

