package com.javajober.member.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.javajober.core.error.exception.Exception404;
import com.javajober.core.message.ErrorMessage;

import com.javajober.member.domain.Member;

public interface MemberRepository extends Repository<Member, Long> {

	Optional<Member> findById(final Long id);

	default Member findMember (final Long id) {
		return findById(id)
			.orElseThrow(() -> new Exception404(ErrorMessage.MEMBER_NOT_FOUND));
	}
}