package com.javajober.blocks.listBlock.repository;

import java.util.List;
import java.util.Optional;

import com.javajober.blocks.listBlock.domain.ListBlock;
import org.springframework.data.repository.Repository;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;

public interface ListBlockRepository extends Repository<ListBlock, Long> {

	ListBlock save(final ListBlock listBlock);

	List<ListBlock> saveAll(final Iterable<ListBlock> listBlocks);

	void deleteAllById(final Iterable<Long> id);

	Optional<ListBlock> findByIdAndDeletedAtIsNull(final Long id);

	default ListBlock findListBlock(final Long id) {
		return findByIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new ApplicationException(ApiStatus.NOT_FOUND, "리스트 블록 데이터를 찾을 수 없습니다."));
	}
}