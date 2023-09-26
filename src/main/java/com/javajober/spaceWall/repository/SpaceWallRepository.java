package com.javajober.spaceWall.repository;

import com.javajober.spaceWall.domain.SpaceWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceWallRepository extends Repository<SpaceWall, Long> {

    @Query("SELECT s FROM SpaceWall s LEFT JOIN s.addSpace a " +
            "WHERE a.id = :addSpaceId AND a.member.id = :memberId")
    List<SpaceWall> findSpaceWall(@Param("memberId") Long memberId, @Param("addSpaceId") Long addSpaceId);

}
