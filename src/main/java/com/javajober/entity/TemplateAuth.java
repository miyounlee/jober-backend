package com.javajober.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "template_auth")
@Entity
public class TemplateAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_member_id",  nullable = false)
    private Member authMember;

    @Column(name = "has_access")
    private Boolean hasAccess;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    protected TemplateAuth() {

    }

    public TemplateAuth(final Member authMember, final Boolean hasAccess) {
        this.authMember = authMember;
        this.hasAccess = hasAccess;
    }
}
