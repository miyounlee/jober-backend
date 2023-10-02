package com.javajober.listBlock.repository;

import org.springframework.data.repository.Repository;

import com.javajober.listBlock.domain.ListBlock;

public interface ListBlockRepository extends Repository<ListBlock, Long> {

	ListBlock save(ListBlock listBlock);

}
