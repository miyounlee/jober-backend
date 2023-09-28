package com.javajober.spaceWall.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.freeBlock.domain.FreeBlock;
import com.javajober.freeBlock.dto.request.FreeBlockSaveRequest;
import com.javajober.freeBlock.repository.FreeBlockRepository;
import com.javajober.snsBlock.domain.SNSBlock;
import com.javajober.snsBlock.dto.SNSBlockRequest;
import com.javajober.snsBlock.repository.SNSBlockRepository;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.request.SpaceWallRequest;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpaceWallService {

	private final SpaceWallRepository spaceWallRepository;
	private final SNSBlockRepository snsBlockRepository;
	private final FreeBlockRepository freeBlockRepository;

	public SpaceWallService(SpaceWallRepository spaceWallRepository, SNSBlockRepository snsBlockRepository,
		FreeBlockRepository freeBlockRepository) {
		this.spaceWallRepository = spaceWallRepository;
		this.snsBlockRepository = snsBlockRepository;
		this.freeBlockRepository = freeBlockRepository;
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

	@Transactional
	public void save(final SpaceWallRequest spaceWallRequest) {
		ObjectMapper mapper = new ObjectMapper();

		spaceWallRequest.getData().getBlocks().forEach(block -> {
			switch (block.getBlockType()) {
				case FREE_BLOCK:
					List<FreeBlockSaveRequest> freeBlockRequests = mapper.convertValue(block.getSubData(),
						new TypeReference<List<FreeBlockSaveRequest>>() {
						});
					saveFreeBlocks(freeBlockRequests);
					break;
				case SNS_BLOCK:
					List<SNSBlockRequest> snsBlockRequests = mapper.convertValue(block.getSubData(),
						new TypeReference<List<SNSBlockRequest>>() {
						});
					saveSnsBlocks(snsBlockRequests);
					break;
			}
		});
	}

	private void saveFreeBlocks(List<FreeBlockSaveRequest> subData) {
		subData.forEach(block -> {
			FreeBlock freeBlock = FreeBlockSaveRequest.toEntity(block);
			freeBlockRepository.save(freeBlock);
		});
	}

	private void saveSnsBlocks(List<SNSBlockRequest> subData) {
		subData.forEach(block -> {
			SNSBlock snsBlock = SNSBlockRequest.toEntity(block);
			snsBlockRepository.save(snsBlock);
		});
	}
}
