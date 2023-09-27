package com.javajober.spaceWall.service;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceWallService {

    private final SpaceWallRepository spaceWallRepository;

    public SpaceWallService(SpaceWallRepository spaceWallRepository) {
        this.spaceWallRepository = spaceWallRepository;
    }

    public SpaceWallResponse checkSpaceWallTemporary(Long memberId, Long addSpaceId) {

        List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWalls(memberId, addSpaceId);

        if (spaceWalls == null || spaceWalls.isEmpty()) {
            return new SpaceWallResponse(null, false);
        }

        for (SpaceWall spaceWall : spaceWalls) {
            if (spaceWall.getFlag().equals(FlagType.PENDING) && spaceWall.getDeletedAt() == null) {
                return new SpaceWallResponse(spaceWall.getId(), true);
            }
            if (spaceWall.getFlag().equals(FlagType.SAVED) && spaceWall.getDeletedAt() == null) {
                throw new Exception404(ErrorMessage.SAVED_SPACE_WALL_ALREADY_EXISTS);
            }
        }

        return new SpaceWallResponse(null, false);
    }
}
