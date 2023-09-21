package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Table(name = "template_block")
@Entity
public class TemplateBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "template_uuid",  nullable = false)
    private String templateUUID;

    @Column(name = "template_title",  nullable = false)
    private String templateTitle;

    @Column(name = "template_description",  nullable = false)
    private String templateDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_auth_id",  nullable = false)
    private TemplateAuth templateAuth;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected TemplateBlock() {}

    public TemplateBlock(final String templateTitle, final String templateDescription, final TemplateAuth templateAuth) {
        this.templateTitle = templateTitle;
        this.templateDescription = templateDescription;
        this.templateAuth = templateAuth;
    }
}
