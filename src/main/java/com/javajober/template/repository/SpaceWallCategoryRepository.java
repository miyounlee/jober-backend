package com.javajober.template.repository;


import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.spaceWall.domain.SpaceWallCategory;
import com.javajober.spaceWall.domain.SpaceWallCategoryType;

public interface SpaceWallCategoryRepository extends Repository<SpaceWallCategory, Long> {
	Optional<SpaceWallCategory> findBySpaceWallCategory(SpaceWallCategoryType categoryType);

	default SpaceWallCategory getBySpaceWallCategory(final SpaceWallCategoryType categoryType){
		return findBySpaceWallCategory(categoryType)
			.orElseThrow(() -> new Exception404(ErrorMessage.TEMPLATE_CATEGORY_NOT_FOUND));
	}
}
