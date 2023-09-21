package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Table(name = "list_block")
@Entity
public class ListBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "list_uuid", nullable = false)
    private String listUUID;

    @Column(name = "list_label" , nullable = false)
    private String listLabel;

    @Column(name = "list_title", nullable = false)
    private String listTitle;

    @Column(name = "list_description" , nullable = false)
    private String listDescription;

    @Column (name = "is_link", nullable = false)
    private Boolean isLink;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected ListBlock() {}

    public ListBlock(final String listLabel, final String listTitle, final String listDescription, final Boolean isLink) {
        this.listLabel = listLabel;
        this.listTitle = listTitle;
        this.listDescription = listDescription;
        this.isLink = isLink;
    }
}