package com.javajober.fcm.repository;

import com.javajober.fcm.domain.MemberFcmToken;
import com.javajober.member.domain.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberFcmTokenRepository extends Repository<MemberFcmToken, Long> {

    MemberFcmToken save(MemberFcmToken token);
    Optional<MemberFcmToken> findByMemberAndDeviceId(Member member, String deviceId);
}