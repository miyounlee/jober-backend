package com.javajober.home.service;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javajober.member.domain.Member;
import com.javajober.space.domain.SpaceType;

import com.javajober.home.dto.response.AddSpaceResponse;
import com.javajober.home.dto.response.HomeResponse;
import com.javajober.home.dto.response.MemberResponse;
import com.javajober.space.repository.AddSpaceRepository;
import com.javajober.member.repository.MemberRepository;

@Service
public class HomeService {

	private final AddSpaceRepository addSpaceRepository;
	private final MemberRepository memberRepository;

	public HomeService(final AddSpaceRepository addSpaceRepository, final MemberRepository memberRepository) {
		this.addSpaceRepository = addSpaceRepository;
		this.memberRepository = memberRepository;
	}

	public HomeResponse find (final Long memberId) {

		Member member = memberRepository.findMember(memberId);

		MemberResponse memberInfo = MemberResponse.from(member);

		List<AddSpaceResponse> personalSpaces =
			addSpaceRepository.findByMemberIdAndSpaceType(memberId, SpaceType.PERSONAL).stream()
				.map(AddSpaceResponse::from)
				.collect(Collectors.toList());

		List<AddSpaceResponse> organizationSpaces =
			addSpaceRepository.findByMemberIdAndSpaceType(memberId, SpaceType.ORGANIZATION).stream()
				.map(AddSpaceResponse::from)
				.collect(Collectors.toList());

		EnumMap<SpaceType, List<AddSpaceResponse>> spaces = new EnumMap<>(SpaceType.class);
		spaces.put(SpaceType.PERSONAL, personalSpaces);
		spaces.put(SpaceType.ORGANIZATION, organizationSpaces);

		return new HomeResponse(memberInfo, spaces);
	}
}