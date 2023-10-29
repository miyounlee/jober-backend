package com.javajober.spaceWall.repository;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaceWallRepository extends Repository<SpaceWall, Long> {

    SpaceWall save(final SpaceWall spaceWall);

    boolean existsByShareURLAndFlag(final String shareURL, final FlagType flagType);

    boolean existsByAddSpaceId(final Long spaceId);

    @Query("SELECT s FROM SpaceWall s LEFT JOIN s.addSpace a LEFT JOIN s.member m " +
            "WHERE a.id = :addSpaceId AND m.id = :memberId")
    List<SpaceWall> findSpaceWalls(@Param("memberId") final Long memberId, @Param("addSpaceId") final Long addSpaceId);

    @Query("SELECT s.id FROM SpaceWall s WHERE s.member.id = :memberId AND s.addSpace.id = :addSpaceId AND s.flag = :flag")
    Optional<Long> findSpaceWallId(@Param("memberId") final Long memberId, @Param("addSpaceId") final Long addSpaceId, @Param("flag") final FlagType flag);

    Optional<SpaceWall> findByIdAndAddSpaceIdAndMemberIdAndFlagAndDeletedAtIsNull(final Long id, final Long addSpaceId, final Long memberId, final FlagType flag);

    Optional<SpaceWall> findByShareURLAndFlagAndDeletedAtIsNull(final String shareURL, final FlagType flag);

    default List<SpaceWall> findSpaceWallsOrThrow(final Long memberId, final Long addSpaceId) {
        List<SpaceWall> spaceWalls = findSpaceWalls(memberId, addSpaceId);
        if (spaceWalls.isEmpty()) {
            throw new ApplicationException(ApiStatus.NOT_FOUND, "임시 저장된 공유페이지가 없습니다.");
        }
        return spaceWalls;
    }

    default SpaceWall getById(final Long memberId, final Long spaceWallId) {
        return findSpaceWalls(memberId, spaceWallId).stream()
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ApiStatus.NOT_FOUND, "존재하지 않는 스페이스입니다."));
    }

    default SpaceWall findSpaceWall(Long id, Long addSpaceId, Long memberId, FlagType flag) {
        return findByIdAndAddSpaceIdAndMemberIdAndFlagAndDeletedAtIsNull(id, addSpaceId, memberId, flag)
                .orElseThrow(() -> new ApplicationException(ApiStatus.NOT_FOUND, "공유페이지를 찾을 수 없습니다."));
    }

    default SpaceWall getByShareURL(final String shareURL) {
        try {
            return findByShareURLAndFlagAndDeletedAtIsNull(shareURL, FlagType.SAVED)
                    .orElseThrow(() -> new ApplicationException(ApiStatus.NOT_FOUND, "존재하지 않는 shareURL입니다."));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ApplicationException(ApiStatus.EXCEPTION, "중복된 shareURL이 존재합니다.");
        }
    }
}