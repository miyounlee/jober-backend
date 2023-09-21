package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "member_group")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class MemberGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_hashtag_type", nullable = false)
    private MemberHashtagType memberHashtagType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_space_id", nullable = false)
    private AddSpace addSpace;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_group_id", nullable = false)
    private List<Member> members;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected MemberGroup() {}

    @Builder
    public MemberGroup(final MemberHashtagType memberHashtagType, final AccountType accountType, final AddSpace addSpace, final List<Member> members) {
        this.memberHashtagType = memberHashtagType;
        this.accountType = accountType;
        this.addSpace = addSpace;
        this.members = members;
    }
}
