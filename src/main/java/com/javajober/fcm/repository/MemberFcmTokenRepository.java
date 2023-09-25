package com.javajober.fcm.repository;

import com.javajober.fcm.domain.MemberFcmToken;
import org.springframework.data.repository.Repository;

public interface MemberFcmTokenRepository extends Repository<MemberFcmToken, Long> {
    MemberFcmToken save(MemberFcmToken token);
}