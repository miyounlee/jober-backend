package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_hashtag_type", nullable = false)
    private MemberHashtagType memberHashtagType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    protected Member() {

    }

    @Builder
    public Member(final String memberName, final String memberEmail, final String password, final String phoneNumber,
                  final MemberHashtagType memberHashtagType, final AccountType accountType) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberHashtagType = memberHashtagType;
        this.accountType = accountType;
    }
}