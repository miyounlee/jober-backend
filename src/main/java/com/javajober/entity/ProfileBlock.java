package com.javajober.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name="profile_block")
@Entity
public class ProfileBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "background_image_url")
    private String background_image_url;

    @Column(name = "profile_image_url")
    private String profile_image_url;

    @Column(name = "page_title")
    private String page_title;

    @Column(name = "page_description")
    private String page_description;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected ProfileBlock() {}

    public ProfileBlock(final String background_image_url, final String profile_image_url, final String page_title, final String page_description){
        this.background_image_url = background_image_url;
        this.profile_image_url = profile_image_url;
        this.page_title = page_title;
        this.page_description = page_description;

    }
}