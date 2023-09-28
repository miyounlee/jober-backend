package com.javajober.wallInfoBlock.repository;

import org.springframework.data.repository.Repository;

import com.javajober.wallInfoBlock.domain.WallInfoBlock;

public interface WallInfoBlockRepository extends Repository<WallInfoBlock, Long> {
	WallInfoBlock save(WallInfoBlock wallInfoBlock);
}
