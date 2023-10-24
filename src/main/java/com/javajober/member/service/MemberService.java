package com.javajober.member.service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javajober.core.exception.ApiStatus;
import com.javajober.core.exception.ApplicationException;
import com.javajober.member.domain.Member;
import com.javajober.member.dto.MemberLoginRequest;
import com.javajober.member.dto.MemberLoginResponse;
import com.javajober.member.dto.MemberSignupRequest;
import com.javajober.member.dto.MemberSignupResponse;
import com.javajober.member.repository.MemberRepository;
import com.javajober.core.refreshToken.repository.RefreshTokenRepository;
import com.javajober.core.security.JwtTokenizer;
import com.javajober.core.refreshToken.domain.RefreshToken;
import com.javajober.space.domain.AddSpace;
import com.javajober.space.domain.SpaceType;
import com.javajober.space.dto.request.SpaceSaveRequest;
import com.javajober.space.repository.AddSpaceRepository;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenizer jwtTokenizer;
	private final RefreshTokenRepository refreshTokenRepository;
	private final AddSpaceRepository addSpaceRepository;

	public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer,
		RefreshTokenRepository refreshTokenRepository, AddSpaceRepository addSpaceRepository) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenizer = jwtTokenizer;
		this.refreshTokenRepository = refreshTokenRepository;
		this.addSpaceRepository = addSpaceRepository;
	}

	@Transactional
	public MemberSignupResponse signup(MemberSignupRequest memberSignupRequest) {
		Optional<Member> existingMember = memberRepository.findMember(memberSignupRequest.getEmail());

		if (existingMember.isPresent()) {
			throw new ApplicationException(ApiStatus.ALREADY_EXIST, "이미 등록된 이메일입니다.");
		}

		Member member = memberSignupRequest.toEntity(memberSignupRequest);
		member.setPassword(passwordEncoder.encode(memberSignupRequest.getPassword()));
		Member saveMember = memberRepository.save(member);

		initializeAndSaveNewMemberSpaces(member);

		return new MemberSignupResponse(saveMember);
	}

	private void initializeAndSaveNewMemberSpaces(Member member) {

		SpaceSaveRequest personalSpaceRequest = createSpaceSaveRequest(member.getMemberName(), SpaceType.PERSONAL.getEngTitle(), member.getMemberName());
		SpaceSaveRequest organizationSpaceRequest = createSpaceSaveRequest(member.getMemberName(), SpaceType.ORGANIZATION.getEngTitle(), "임시회사명");

		Set<AddSpace> spaces = new HashSet<>();

		AddSpace personalSpace = SpaceSaveRequest.toEntity(personalSpaceRequest, member);
		spaces.add(personalSpace);

		AddSpace organizationSpace = SpaceSaveRequest.toEntity(organizationSpaceRequest, member);
		spaces.add(organizationSpace);

		saveSpaces(spaces);
	}

	private SpaceSaveRequest createSpaceSaveRequest(String spaceTitle, String spaceType, String representativeName) {
			return SpaceSaveRequest.builder()
				.spaceTitle(spaceTitle)
				.spaceType(spaceType)
				.representativeName(representativeName)
				.build();
	}

	private void saveSpaces(Set<AddSpace> spaces) {
		addSpaceRepository.saveAll(spaces);
	}

	@Transactional
	public MemberLoginResponse login(MemberLoginRequest loginDto) {
		Member member = memberRepository.findMember(loginDto.getEmail()).orElseThrow(()
			-> new ApplicationException(ApiStatus.NOT_FOUND, "존재하지 않는 회원 아이디입니다."));

		if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
			throw new ApplicationException(ApiStatus.NOT_FOUND, "비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getMemberEmail());
		String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getMemberEmail());

		RefreshToken refreshTokenEntity = new RefreshToken(member.getId(),refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);

		return new MemberLoginResponse(member,accessToken,refreshToken);
	}
}