package com.javajober.entity;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "member_fcm_token")
@Entity
public class MemberFcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    protected MemberFcmToken() {

    }

    public MemberFcmToken(final Member member, final String fcmToken) {
        this.member = member;
        this.fcmToken = fcmToken;
    }
}