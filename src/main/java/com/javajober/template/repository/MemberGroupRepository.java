package com.javajober.template.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;
import com.javajober.entity.MemberGroup;


public interface MemberGroupRepository extends Repository<MemberGroup, Long> {
	List<MemberGroup> findByAddSpaceId(Long addSpaceId);

	default List<MemberGroup> getByAddSpaceId(final Long addSpaceId) {
		List<MemberGroup> memberGroups = findByAddSpaceId(addSpaceId);

		if (memberGroups == null || memberGroups.isEmpty()) {
			throw new Exception404(ErrorMessage.ADD_SPACE_NOT_FOUND);
		}

		return memberGroups;
	}
}