package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Table(name = "sns_block")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class SnsBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type", nullable = false)
    private SnsType snsType;

    @Column(name = "sns_url", nullable = false)
    private String snsURL;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected SnsBlock() {}

    @Builder
    public SnsBlock(final SnsType snsType, final String snsURL) {
        this.snsType = snsType;
        this.snsURL = snsURL;
    }
}
