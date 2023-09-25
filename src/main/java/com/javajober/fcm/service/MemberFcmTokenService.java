package com.javajober.fcm.service;

import com.javajober.member.domain.Member;
import com.javajober.fcm.domain.MemberFcmToken;
import com.javajober.fcm.dto.MemberFcmTokenRequest;
import com.javajober.fcm.repository.MemberFcmTokenRepository;
import com.javajober.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberFcmTokenService {

    private final MemberFcmTokenRepository memberFcmTokenRepository;
    private final MemberRepository memberRepository;

    public MemberFcmTokenService(MemberFcmTokenRepository memberFcmTokenRepository, MemberRepository memberRepository) {
        this.memberFcmTokenRepository = memberFcmTokenRepository;
        this.memberRepository = memberRepository;
    }

    public void saveFcmToken(MemberFcmTokenRequest request) {
        Member member = memberRepository.findMember(request.getMemberId());
        MemberFcmToken token = MemberFcmTokenRequest.toEntity(request, member);
        memberFcmTokenRepository.save(token);
    }
}