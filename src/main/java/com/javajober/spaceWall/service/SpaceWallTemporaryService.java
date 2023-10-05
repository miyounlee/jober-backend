package com.javajober.spaceWall.service;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
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
    private final EntityManager entityManager;

    public SpaceWallTemporaryService(final SpaceWallRepository spaceWallRepository, final EntityManager entityManager){
        this.spaceWallRepository = spaceWallRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void delete(final Long memberId, final Long addSpaceId) {

        List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWallsOrThrow(memberId, addSpaceId);

        spaceWalls.forEach(spaceWall -> {
            if (spaceWall.getFlag().equals(FlagType.PENDING)) {
                entityManager.remove(spaceWall);
            }
        });
    }

    public SpaceWallTemporaryResponse hasSpaceWallTemporary(final Long memberId, final Long addSpaceId) {

        List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWalls(memberId, addSpaceId);

        if (spaceWalls == null || spaceWalls.isEmpty()) {
            return new SpaceWallTemporaryResponse(null, false);
        }

        for (SpaceWall spaceWall : spaceWalls) {
            if (spaceWall.getFlag().equals(FlagType.PENDING) && spaceWall.getDeletedAt() == null) {
                return new SpaceWallTemporaryResponse(spaceWall.getId(), true);
            }
            if (spaceWall.getFlag().equals(FlagType.SAVED) && spaceWall.getDeletedAt() == null) {
                throw new Exception404(ErrorMessage.SAVED_SPACE_WALL_ALREADY_EXISTS);
            }
        }

        return new SpaceWallTemporaryResponse(null, false);
    }
}