package com.javajober.spaceWall.service;

import com.javajober.core.error.exception.Exception500;
import com.javajober.core.message.ErrorMessage;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.SpaceWallResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SpaceWallService {

    private final SpaceWallRepository spaceWallRepository;

    public SpaceWallResponse checkSpaceWallTemporary(Long memberId, Long addSpaceId) {

        SpaceWall spaceWallPS = spaceWallRepository.getById(memberId, addSpaceId);

        if (spaceWallPS.getFlag().equals(FlagType.PENDING)) {
            return SpaceWallResponse.from(spaceWallPS.getId(), true);
        }

        if (spaceWallPS.getFlag().equals(FlagType.SAVED)) {
            throw new Exception500(ErrorMessage.SAVED_SPACE_WALL_ALREADY_EXISTS);
        }

        return SpaceWallResponse.from(null, false);
    }
}
