package com.javajober.spaceWall.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.javajober.blocks.listBlock.domain.ListBlock;
import com.javajober.blocks.listBlock.dto.request.ListBlockSaveRequest;
import com.javajober.blocks.listBlock.repository.ListBlockRepository;
import com.javajober.spaceWall.strategy.BlockJsonProcessor;
import com.javajober.spaceWall.strategy.BlockStrategyName;
import com.javajober.spaceWall.strategy.MoveBlockStrategy;

@Component
public class ListBlockStrategy implements MoveBlockStrategy {

	private final BlockJsonProcessor blockJsonProcessor;
	private final ListBlockRepository listBlockRepository;


	public ListBlockStrategy(final BlockJsonProcessor blockJsonProcessor, final ListBlockRepository listBlockRepository) {
		this.blockJsonProcessor = blockJsonProcessor;
		this.listBlockRepository = listBlockRepository;
	}


	@Override
	public List<Long> saveBlocks(final List<Object> subData) {

		List<Long> listBlockIds = new ArrayList<>();

		subData.forEach(block -> {
			ListBlockSaveRequest request = blockJsonProcessor.convertValue(block, ListBlockSaveRequest.class);
			ListBlock listBlock = ListBlockSaveRequest.toEntity(request);
			listBlockIds.add(listBlockRepository.save(listBlock).getId());
		});

		return listBlockIds;
	}

	@Override
	public String getStrategyName() {
		return BlockStrategyName.ListBlockStrategy.name();
	}
}
