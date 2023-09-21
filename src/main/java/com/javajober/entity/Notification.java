package com.javajober.entity;


import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "notification")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_wall_id")
    private SpaceWall spaceWall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id")
    private Member subscriber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Notification(){

    }

    public Notification(final SpaceWall spaceWall, final Member subscriber){
        this.spaceWall = spaceWall;
        this.subscriber = subscriber;
    }
}
