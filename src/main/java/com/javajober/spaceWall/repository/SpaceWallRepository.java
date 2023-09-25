package com.javajober.spaceWall.repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.spaceWall.domain.SpaceWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpaceWallRepository extends Repository<SpaceWall, Long> {

    @Query("SELECT s FROM SpaceWall s LEFT JOIN s.addSpace a " +
            "WHERE a.id = :addSpaceId AND a.member.id = :memberId")
    Optional<SpaceWall> findByMemberIdAndAddSpaceId(@Param("memberId") Long memberId, @Param("addSpaceId") Long addSpaceId);

    default SpaceWall getById(final Long memberId, final Long addSpaceId) {

        return findByMemberIdAndAddSpaceId(memberId, addSpaceId)
                .orElseThrow(() -> new Exception404(ErrorMessage.ADD_SPACE_NOT_FOUND));
    }
}
