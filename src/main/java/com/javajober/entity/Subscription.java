package com.javajober.entity;


import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "subscription")
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private Member subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_wall_id", nullable = false)
    private SpaceWall spaceWall;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    protected Subscription() {

    }

    public Subscription(final Member subscriber, final SpaceWall spaceWall) {
        this.subscriber = subscriber;
        this.spaceWall = spaceWall;
    }
}