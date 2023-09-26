package com.javajober.fcm.repository;

import com.javajober.fcm.domain.Subscription;
import org.springframework.data.repository.Repository;

public interface SubscriptionRepository extends Repository<Subscription, Long> {
    Subscription save(Subscription subscription);
}