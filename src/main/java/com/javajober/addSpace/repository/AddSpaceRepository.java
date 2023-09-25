package com.javajober.addSpace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.entity.AddSpace;
import com.javajober.entity.SpaceType;

public interface AddSpaceRepository extends Repository<AddSpace, Long> {

	List<AddSpace> findByMemberIdAndSpaceType(Long memberId, SpaceType spaceType);

	Optional<AddSpace> findById(Long id);


	default AddSpace findAddSpace (final Long id) {
		return findById(id)
			.orElseThrow(() -> new Exception404(ErrorMessage.ADD_SPACE_NOT_FOUND));
	}
}
