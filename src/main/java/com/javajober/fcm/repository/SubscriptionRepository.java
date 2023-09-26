package com.javajober.fcm.repository;

import com.javajober.fcm.domain.Subscription;
import com.javajober.member.domain.Member;
import com.javajober.spaceWall.domain.SpaceWall;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SubscriptionRepository extends Repository<Subscription, Long> {
    Subscription save(Subscription subscription);

    Optional<Subscription> findBySubscriberAndSpaceWall(Member subscriber, SpaceWall spaceWall);

    void delete(Subscription subscription);
}