package com.javajober.wallInfoBlock.repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import org.springframework.data.repository.Repository;

import com.javajober.wallInfoBlock.domain.WallInfoBlock;

import java.util.Optional;

public interface WallInfoBlockRepository extends Repository<WallInfoBlock, Long> {

	WallInfoBlock save(WallInfoBlock wallInfoBlock);

	Optional<WallInfoBlock> findByIdAndDeletedAtIsNull(Long id);

	default WallInfoBlock getById(final Long id) {
		return findByIdAndDeletedAtIsNull(id)
				.orElseThrow(() -> new Exception404(ErrorMessage.FILE_BLOCK_NOT_FOUND));
	}
}
