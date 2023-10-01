package com.javajober.space.repository;

import com.javajober.member.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    List<MemberGroup> findByMemberIdAndAddSpaceId(Long memberId, Long addSpaceId);
}
