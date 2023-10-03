package com.javajober.listBlock.repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import org.springframework.data.repository.Repository;

import com.javajober.listBlock.domain.ListBlock;

import java.util.Optional;

public interface ListBlockRepository extends Repository<ListBlock, Long> {

	ListBlock save(ListBlock listBlock);

	Optional<ListBlock> findByIdAndDeletedAtIsNull(Long id);

	default ListBlock getById(final Long id) {
		return findByIdAndDeletedAtIsNull(id)
				.orElseThrow(() -> new Exception404(ErrorMessage.LIST_BLOCK_NOT_FOUND));
	}

}
