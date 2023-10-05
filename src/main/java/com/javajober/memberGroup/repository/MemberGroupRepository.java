package com.javajober.memberGroup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.memberGroup.domain.MemberGroup;

public interface MemberGroupRepository extends Repository<MemberGroup, Long> {

	List<MemberGroup> findAllByAddSpaceId(final Long addSpaceId);

	List<MemberGroup> findByAddSpaceId(final Long addSpaceId);

	default List<MemberGroup> getByAddSpaceId(final Long addSpaceId) {
		List<MemberGroup> memberGroups = findByAddSpaceId(addSpaceId);

		if (memberGroups == null || memberGroups.isEmpty()) {
			throw new Exception404(ErrorMessage.ADD_SPACE_NOT_FOUND);
		}

		return memberGroups;
	}

	Optional<MemberGroup> findById(final Long Id);

	default MemberGroup getById(final Long Id){
		return findById(Id)
			.orElseThrow(() -> new Exception404(ErrorMessage.MEMBER_NOT_FOUND));
	}
}