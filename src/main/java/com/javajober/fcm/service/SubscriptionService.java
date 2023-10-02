package com.javajober.fcm.service;

import com.javajober.fcm.domain.Subscription;
import com.javajober.fcm.dto.request.SubscriptionRequest;
import com.javajober.fcm.repository.SubscriptionRepository;
import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final SpaceWallRepository spaceWallRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, MemberRepository memberRepository, SpaceWallRepository spaceWallRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.memberRepository = memberRepository;
        this.spaceWallRepository = spaceWallRepository;
    }

    public void subscribe(SubscriptionRequest request) {
        Member subscriber = memberRepository.findMember(request.getSubscriberMemberId());
        SpaceWall spaceWall = spaceWallRepository.getById(request.getMemberId(), request.getSpaceWallId());

        Subscription subscription = SubscriptionRequest.toEntity(request, subscriber, spaceWall);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(SubscriptionRequest request) {
        Member subscriber = memberRepository.findMember(request.getSubscriberMemberId());
        SpaceWall spaceWall = spaceWallRepository.getById(request.getMemberId(), request.getSpaceWallId());

        Optional<Subscription> existingSubscription = subscriptionRepository.findBySubscriberAndSpaceWall(subscriber, spaceWall);
        existingSubscription.ifPresent(subscriptionRepository::delete);
    }
}