package com.javajober.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Table(name = "block_setting")
@Entity
public class BlockSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shape")
    private String shape;

    @Column(name = "style")
    private String style;

    @Column(name = "style_color")
    private String styleColor;

    @Column(name = "gradation")
    private Boolean gradation;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    protected BlockSetting() {

    }

    public BlockSetting(final String shape, final String style, final String styleColor, final Boolean gradation) {
        this.shape = shape;
        this.style = style;
        this.styleColor = styleColor;
        this.gradation = gradation;

    }
}