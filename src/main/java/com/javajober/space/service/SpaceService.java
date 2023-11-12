package com.javajober.space.service;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.member.domain.Member;
import com.javajober.member.repository.MemberRepository;
import com.javajober.member.memberGroup.repository.MemberGroupRepository;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.domain.SpaceType;
import com.javajober.space.dto.request.SpaceSaveRequest;
import com.javajober.space.dto.response.SpaceSaveResponse;
import com.javajober.space.repository.AddSpaceRepository;
import com.javajober.space.dto.response.SpaceResponse;
import com.javajober.space.dto.response.MemberGroupResponse;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.repository.SpaceWallRepository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpaceService {

    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final AddSpaceRepository addSpaceRepository;
    private final SpaceWallRepository spaceWallRepository;

    public SpaceService(
        final MemberRepository memberRepository,
        final MemberGroupRepository memberGroupRepository,
        final AddSpaceRepository addSpaceRepository,
        final SpaceWallRepository spaceWallRepository
    ) {
        this.memberRepository = memberRepository;
        this.memberGroupRepository = memberGroupRepository;
        this.addSpaceRepository = addSpaceRepository;
        this.spaceWallRepository = spaceWallRepository;
    }


    public void initializeAndSaveNewMemberSpaces(final Member member) {

        log.info("initializeAndSaveNewMemberSpaces 시작: {}, 스레드 이름: {}", member.getMemberName(), Thread.currentThread().getName());

        SpaceSaveRequest personalSpaceRequest = createSpaceSaveRequest(member.getMemberName(), SpaceType.PERSONAL.getEngTitle(), member.getMemberName());
        SpaceSaveRequest organizationSpaceRequest = createSpaceSaveRequest(member.getMemberName(), SpaceType.ORGANIZATION.getEngTitle(), "임시회사명");

        Set<AddSpace> spaces = new HashSet<>();

        AddSpace personalSpace = SpaceSaveRequest.toEntity(personalSpaceRequest, member);
        spaces.add(personalSpace);

        AddSpace organizationSpace = SpaceSaveRequest.toEntity(organizationSpaceRequest, member);
        spaces.add(organizationSpace);

        saveSpaces(spaces);

        log.info("initializeAndSaveNewMemberSpaces 종료: {}, 스레드 이름: {}", member.getMemberName(), Thread.currentThread().getName());
    }

    private SpaceSaveRequest createSpaceSaveRequest(final String spaceTitle, final String spaceType, final String representativeName) {
        return SpaceSaveRequest.builder()
            .spaceTitle(spaceTitle)
            .spaceType(spaceType)
            .representativeName(representativeName)
            .build();
    }

    @Transactional
    public void saveSpaces(final Set<AddSpace> spaces) {
        addSpaceRepository.saveAll(spaces);
    }


    public SpaceSaveResponse save(final SpaceSaveRequest request, final Long memberId) {
        Member member = memberRepository.findMember(memberId);
        AddSpace space = SpaceSaveRequest.toEntity(request, member);
        Long spaceId = addSpaceRepository.save(space).getId();

        return new SpaceSaveResponse(spaceId);
    }

    public SpaceResponse find(final Long addSpaceId, final String spaceTypeString, final Long memberId) {
        SpaceType spaceType = SpaceType.findSpaceTypeByString(spaceTypeString);
        List<Long> addSpaceIds = addSpaceRepository.findAddSpaceIds(spaceType, memberId);
        existsAddSpace(addSpaceId, addSpaceIds);

        List<MemberGroupResponse> memberGroupResponses = memberGroupRepository.findMemberGroup(addSpaceId);

        Optional<Long> spaceWallId = spaceWallRepository.findSpaceWallId(memberId, addSpaceId, FlagType.SAVED);

        return spaceWallId.map(id -> new SpaceResponse(true, id, memberGroupResponses))
                .orElseGet(() -> new SpaceResponse(false, 0L, memberGroupResponses));
    }

    private static void existsAddSpace(final Long addSpaceId, final List<Long> addSpaceIds) {
        boolean containsId = addSpaceIds.stream().anyMatch(id -> Objects.equals(id, addSpaceId));
        if(!containsId) {
            throw new ApplicationException(ApiStatus.NOT_FOUND, "존재하지 않는 스페이스입니다.");
        }
    }
}