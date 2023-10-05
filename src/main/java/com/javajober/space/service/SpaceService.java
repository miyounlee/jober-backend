package com.javajober.space.service;

import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.memberGroup.domain.MemberGroup;
import com.javajober.memberGroup.domain.MemberHashtagType;
import com.javajober.memberGroup.repository.MemberGroupRepository;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.repository.AddSpaceRepository;
import com.javajober.space.dto.response.DataResponse;
import com.javajober.space.dto.response.MemberGroupResponse;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceService {

    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final AddSpaceRepository addSpaceRepository;
    private final SpaceWallRepository spaceWallRepository;

    public SpaceService(final MemberRepository memberRepository,
                        final MemberGroupRepository memberGroupRepository,
                        final AddSpaceRepository addSpaceRepository,
                        final SpaceWallRepository spaceWallRepository) {
        this.memberRepository = memberRepository;
        this.memberGroupRepository = memberGroupRepository;
        this.addSpaceRepository = addSpaceRepository;
        this.spaceWallRepository = spaceWallRepository;
    }

    @Transactional
    public DataResponse find (final Long memberId, final Long addSpaceId, final String spaceType) {

        AddSpace addSpace = addSpaceRepository.findAddSpace(addSpaceId);
        Member member = memberRepository.findMember(memberId);

        if (addSpace == null || member == null || !addSpace.getSpaceType().name().equalsIgnoreCase(spaceType)) {
            throw new IllegalArgumentException("Invalid request parameters");
        }

        boolean hasWall = spaceWallRepository.existsByAddSpaceId(addSpaceId);
        List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWalls(memberId, addSpaceId);

        Long spaceWallId = null;
        if (hasWall) {
            List<SpaceWall> foundSpaceWalls = spaceWallRepository.findByAddSpaceId(addSpaceId);
            if (!foundSpaceWalls.isEmpty()) {
                spaceWallId = foundSpaceWalls.get(0).getId();
            }
        }

        List<MemberGroup> memberGroups = memberGroupRepository.findAllByAddSpaceId(addSpaceId);
        List<MemberGroupResponse> list = memberGroups.stream()
                .sorted((mg1, mg2) -> {
                    if (mg1.getMemberHashtagType() == MemberHashtagType.EMPTY) return -1;
                    if (mg2.getMemberHashtagType() == MemberHashtagType.EMPTY) return 1;
                    return mg1.getMemberHashtagType().compareTo(mg2.getMemberHashtagType());
                })
                .map(mg -> new MemberGroupResponse(
                        mg.getMember().getId(),
                        mg.getMember().getMemberName(),
                        mg.getMemberHashtagType().toString(),
                        mg.getAccountType().toString(),
                        mg.getMember().getPhoneNumber()
                )).collect(Collectors.toList());

        return DataResponse.builder()
                .hasWall(hasWall)
                .spaceWallId(spaceWallId)
                .list(list)
                .build();
    }
}