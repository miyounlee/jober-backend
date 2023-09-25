package com.javajober.spaceWall.domain;

import com.javajober.entity.AddSpace;
import com.javajober.entity.SpaceWallCategoryType;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "space_wall")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class SpaceWall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocks", columnDefinition = "json", nullable = false)
    private String blocks;

    @Column(name ="share_url", nullable = false)
    private String shareURL;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_space_id", nullable = false)
    private AddSpace addSpace;

    @Enumerated(EnumType.STRING)
    @Column(name = "space_wall_category", nullable = false)
    private SpaceWallCategoryType spaceWallCategoryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "flag", nullable = false)
    private FlagType flag;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected SpaceWall() {

    }

    public SpaceWall(final String blocks, final SpaceWallCategoryType spaceWallCategoryType, final String shareURL, final FlagType flag) {
        this.blocks = blocks;
        this.spaceWallCategoryType = spaceWallCategoryType;
        this.shareURL = shareURL;
        this.flag = flag;
    }
}
