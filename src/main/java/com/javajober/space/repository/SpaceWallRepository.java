package com.javajober.space.repository;

import com.javajober.spaceWall.domain.SpaceWall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceWallRepository extends JpaRepository<SpaceWall, Long> {
    boolean existsByAddSpaceId(Long addSpaceId);
}
