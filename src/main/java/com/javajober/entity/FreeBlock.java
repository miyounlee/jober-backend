package com.javajober.entity;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Table(name = "free_block")
@Entity
public class FreeBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "free_title", length = 100,  nullable = false)
    private String freeTitle;

    @Lob
    @Column(name = "free_content", length = 5000, nullable = false)
    private String freeContent;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;


    protected FreeBlock() {};

    public FreeBlock(final String freeTitle, final String freeContent) {
        this.freeTitle = freeTitle;
        this.freeContent = freeContent;
    }
}