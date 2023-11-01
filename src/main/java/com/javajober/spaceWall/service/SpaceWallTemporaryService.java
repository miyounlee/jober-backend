package com.javajober.spaceWall.service;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.response.SpaceWallTemporaryResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SpaceWallTemporaryService {

    private final SpaceWallRepository spaceWallRepository;

    public SpaceWallTemporaryService(final SpaceWallRepository spaceWallRepository){
        this.spaceWallRepository = spaceWallRepository;
    }

    @Transactional
    public void delete(final Long memberId, final Long addSpaceId) {

        List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWalls(memberId, addSpaceId);
        if (spaceWalls.isEmpty() || spaceWalls.stream().noneMatch(wall -> wall.getFlag().equals(FlagType.PENDING))) {
            throw new ApplicationException(ApiStatus.NOT_FOUND, "임시 저장된 공유페이지가 없습니다.");
        }

        spaceWallRepository.deleteByMemberIdAndAddSpaceIdAndFlag(memberId, addSpaceId, FlagType.PENDING);
    }

    public SpaceWallTemporaryResponse hasSpaceWallTemporary(final Long memberId, final Long addSpaceId) {

        return spaceWallRepository.findSpaceWallId(memberId, addSpaceId, FlagType.PENDING)
                .map(spaceWallId -> new SpaceWallTemporaryResponse(spaceWallId, true))
                .orElseGet(() -> new SpaceWallTemporaryResponse(0L, false));
    }
}
