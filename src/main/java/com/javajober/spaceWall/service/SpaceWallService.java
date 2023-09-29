package com.javajober.spaceWall.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.javajober.core.config.FileDirectoryConfig;
import com.javajober.core.error.exception.Exception404;
import com.javajober.core.error.exception.Exception500;
import com.javajober.core.message.ErrorMessage;

import com.javajober.fileBlock.domain.FileBlock;
import com.javajober.fileBlock.dto.request.FileBlockSaveRequest;
import com.javajober.fileBlock.repository.FileBlockRepository;
import com.javajober.freeBlock.domain.FreeBlock;
import com.javajober.freeBlock.dto.request.FreeBlockSaveRequest;
import com.javajober.freeBlock.repository.FreeBlockRepository;
import com.javajober.member.domain.MemberGroup;
import com.javajober.snsBlock.domain.SNSBlock;
import com.javajober.snsBlock.dto.SNSBlockRequest;
import com.javajober.snsBlock.repository.SNSBlockRepository;
import com.javajober.spaceWall.domain.FlagType;
import com.javajober.spaceWall.domain.SpaceWall;
import com.javajober.spaceWall.dto.request.SpaceWallRequest;
import com.javajober.spaceWall.dto.response.SpaceWallResponse;
import com.javajober.spaceWall.repository.SpaceWallRepository;
import com.javajober.template.domain.TemplateAuth;
import com.javajober.template.domain.TemplateBlock;
import com.javajober.template.dto.TemplateBlockRequest;
import com.javajober.template.repository.MemberGroupRepository;
import com.javajober.template.repository.TemplateAuthRepository;
import com.javajober.template.repository.TemplateBlockRepository;
import com.javajober.wallInfoBlock.domain.WallInfoBlock;
import com.javajober.wallInfoBlock.dto.request.WallInfoBlockRequest;
import com.javajober.wallInfoBlock.repository.WallInfoBlockRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SpaceWallService {

	private final SpaceWallRepository spaceWallRepository;
	private final SNSBlockRepository snsBlockRepository;
	private final FreeBlockRepository freeBlockRepository;
	private final TemplateBlockRepository templateBlockRepository;
	private final MemberGroupRepository memberGroupRepository;
	private final TemplateAuthRepository templateAuthRepository;
	private final WallInfoBlockRepository wallInfoBlockRepository;
	private final FileBlockRepository fileBlockRepository;
	private final FileDirectoryConfig fileDirectoryConfig;

	public SpaceWallService(SpaceWallRepository spaceWallRepository, SNSBlockRepository snsBlockRepository,
							FreeBlockRepository freeBlockRepository, TemplateBlockRepository templateBlockRepository,
							MemberGroupRepository memberGroupRepository, TemplateAuthRepository templateAuthRepository, WallInfoBlockRepository wallInfoBlockRepository,
              FileBlockRepository fileBlockRepository, FileDirectoryConfig fileDirectoryConfig) {

		this.spaceWallRepository = spaceWallRepository;
		this.snsBlockRepository = snsBlockRepository;
		this.freeBlockRepository = freeBlockRepository;
		this.templateBlockRepository = templateBlockRepository;
		this.memberGroupRepository = memberGroupRepository;
		this.templateAuthRepository = templateAuthRepository;
		this.wallInfoBlockRepository = wallInfoBlockRepository;
		this.fileBlockRepository = fileBlockRepository;
		this.fileDirectoryConfig = fileDirectoryConfig;
	}

	public SpaceWallResponse checkSpaceWallTemporary(Long memberId, Long addSpaceId) {

		List<SpaceWall> spaceWalls = spaceWallRepository.findSpaceWalls(memberId, addSpaceId);

		if (spaceWalls == null || spaceWalls.isEmpty()) {
			return new SpaceWallResponse(null, false);
		}

		for (SpaceWall spaceWall : spaceWalls) {
			if (spaceWall.getFlag().equals(FlagType.PENDING) && spaceWall.getDeletedAt() == null) {
				return new SpaceWallResponse(spaceWall.getId(), true);
			}
			if (spaceWall.getFlag().equals(FlagType.SAVED) && spaceWall.getDeletedAt() == null) {
				throw new Exception404(ErrorMessage.SAVED_SPACE_WALL_ALREADY_EXISTS);
			}
		}

		return new SpaceWallResponse(null, false);
	}

	@Transactional
	public void save(final SpaceWallRequest spaceWallRequest, final List<MultipartFile> files,
					 final MultipartFile backgroundImgURL, final MultipartFile wallInfoImgURL, final MultipartFile styleImgURL) {
    
		WallInfoBlockRequest wallInfoBlockRequest = spaceWallRequest.getData().getWallInfoBlock();
		saveWallInfoBlock(wallInfoBlockRequest, backgroundImgURL, wallInfoImgURL);
    
		ObjectMapper mapper = new ObjectMapper();
		AtomicInteger i = new AtomicInteger();

		spaceWallRequest.getData().getBlocks().forEach(block -> {
			switch (block.getBlockType()) {
				case FREE_BLOCK:
					List<FreeBlockSaveRequest> freeBlockRequests = mapper.convertValue(block.getSubData(),
						new TypeReference<List<FreeBlockSaveRequest>>() {
						});
					saveFreeBlocks(freeBlockRequests);
					break;
				case SNS_BLOCK:
					List<SNSBlockRequest> snsBlockRequests = mapper.convertValue(block.getSubData(),
						new TypeReference<List<SNSBlockRequest>>() {
						});
					saveSnsBlocks(snsBlockRequests);
					break;
				case TEMPLATE_BLOCK:
					List<TemplateBlockRequest> templateBlockRequests = mapper.convertValue(block.getSubData(),
						new TypeReference<List<TemplateBlockRequest>>() {
						});
					saveTemplateBlock(templateBlockRequests);
					break;
       case FILE_BLOCK:
					List<FileBlockSaveRequest> fileBlockSaveRequests = mapper.convertValue(block.getSubData(),
							new TypeReference<List<FileBlockSaveRequest>>() {
							});
					saveFileBlocks(fileBlockSaveRequests, files.get(i.getAndIncrement()));
			}
		});
	}

	private void saveWallInfoBlock(WallInfoBlockRequest wallInfoBlockRequest, MultipartFile backgroundImgURL,
								   MultipartFile wallInfoImgURL) {

		String backgroundImgName = uploadFile(backgroundImgURL);
		String wallInfoImgName = uploadFile(wallInfoImgURL);

		WallInfoBlock wallInfoBlock = WallInfoBlockRequest.toEntity(wallInfoBlockRequest, backgroundImgName, wallInfoImgName);
		wallInfoBlockRepository.save(wallInfoBlock);
	}

	private void saveFreeBlocks(List<FreeBlockSaveRequest> subData) {
		subData.forEach(block -> {
			FreeBlock freeBlock = FreeBlockSaveRequest.toEntity(block);
			freeBlockRepository.save(freeBlock);
		});
	}

	private void saveSnsBlocks(List<SNSBlockRequest> subData) {
		subData.forEach(block -> {
			SNSBlock snsBlock = SNSBlockRequest.toEntity(block);
			snsBlockRepository.save(snsBlock);
		});
	}

	private void saveTemplateBlock(List<TemplateBlockRequest> subData){
		subData.forEach(block -> {
			TemplateBlock templateBlock = TemplateBlockRequest.toEntity(block);
			templateBlockRepository.save(templateBlock);

			block.getAllAuthIds().forEach(authId -> {
				MemberGroup memberGroup = memberGroupRepository.getById(authId);
				Boolean hasAccess = block.getHasAccessTemplateAuth().contains(authId);
				TemplateAuth templateAuth = new TemplateAuth(memberGroup, hasAccess, templateBlock);
				templateAuthRepository.save(templateAuth);
			});
		});
	}

	private void saveFileBlocks(List<FileBlockSaveRequest> subData, MultipartFile file) {

		String fileName = uploadFile(file);
		subData.forEach(block -> {
			FileBlock fileBlock = FileBlockSaveRequest.toEntity(block, fileName);
			fileBlockRepository.save(fileBlock);
		});
	}

	private String uploadFile(MultipartFile file) {

		if (file.isEmpty()) {   // 파일 첨부를 안했을 경우
			return null;
		}

		if (file.getOriginalFilename() == null) {   // 이름이 없는 파일일 경우
			throw new Exception404(ErrorMessage.INVALID_FILE_NAME);
		}
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 테스트용
		String fileUploadPth = getDirectoryPath() + fileName;

		try {
			file.transferTo(new File(fileUploadPth));
		} catch (IOException e) {
			throw new Exception500(ErrorMessage.FILE_UPLOAD_FAILED);
		}

		return fileName;
	}

	private String getDirectoryPath() {
		return fileDirectoryConfig.getDirectoryPath();
	}
}
