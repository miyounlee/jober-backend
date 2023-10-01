package com.javajober.space.service;

import com.javajober.member.domain.MemberGroup;
import com.javajober.space.dto.MemberGroupResponse;
import com.javajober.space.dto.SpaceResponse;
import com.javajober.space.repository.MemberGroupRepository;
import com.javajober.space.repository.SpaceWallRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceService {
    private final SpaceWallRepository spaceWallRepo;
    private final MemberGroupRepository memberGroupRepo;

    public SpaceService(SpaceWallRepository spaceWallRepo, MemberGroupRepository memberGroupRepo) {
        this.spaceWallRepo = spaceWallRepo;
        this.memberGroupRepo = memberGroupRepo;
    }

    public SpaceResponse getSpaceDetails(Long memberId, Long addSpaceId) {
        boolean hasWall = spaceWallRepo.existsByAddSpaceId(addSpaceId);

        List<MemberGroup> memberGroups = memberGroupRepo.findByMemberIdAndAddSpaceId(memberId, addSpaceId);
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
