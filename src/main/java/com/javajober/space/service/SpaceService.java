package com.javajober.space.service;

import com.javajober.member.domain.MemberGroup;
import com.javajober.space.dto.MemberGroupResponse;
import com.javajober.space.dto.SpaceResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.template.repository.MemberGroupRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceService {
    private final SpaceWallRepository spaceWallRepository;
    private final MemberGroupRepository memberGroupRepository;

    public SpaceService(SpaceWallRepository spaceWallRepository, MemberGroupRepository memberGroupRepository) {
        this.spaceWallRepository = spaceWallRepository;
        this.memberGroupRepository = memberGroupRepository;
    }

    public SpaceResponse getSpaceDetails(Long memberId, Long addSpaceId) {
        boolean hasWall = spaceWallRepository.existsByAddSpaceId(addSpaceId);

        List<MemberGroup> memberGroups = memberGroupRepository.findByMemberIdAndAddSpaceId(memberId, addSpaceId);
        List<MemberGroupResponse> list = memberGroups.stream().map(mg ->
                new MemberGroupResponse(
                        mg.getMember().getId(),
                        mg.getMember().getMemberName(),
                        mg.getMemberHashtagType().name(),
                        mg.getAccountType().name(),
                        mg.getMember().getPhoneNumber()
                )
        ).collect(Collectors.toList());

        return new SpaceResponse(hasWall, list);
    }
}
