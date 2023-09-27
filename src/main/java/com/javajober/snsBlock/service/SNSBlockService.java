package com.javajober.snsBlock.service;

import java.util.List;
import java.util.stream.Collectors;

import com.javajober.snsBlock.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.javajober.snsBlock.domain.SNSBlock;
import com.javajober.snsBlock.domain.SNSType;
import com.javajober.snsBlock.repository.SNSBlockRepository;

@Service
public class SNSBlockService {

	private final SNSBlockRepository snsBlockRepository;

	public SNSBlockService(SNSBlockRepository snsBlockRepository) {
		this.snsBlockRepository = snsBlockRepository;
	}

	@Transactional
	public void save(final SNSBlockRequests<SNSBlockRequest> snsBlockRequests) {
		snsBlockRequests.getSubData().forEach(snsBlockRequest -> {
			SNSBlock snsBlock = SNSBlockRequest.toEntity(snsBlockRequest);
			snsBlockRepository.save(snsBlock);
		});
	}

	public SNSBlockResponses find(final List<Long> snsBlockIds) {

		List<SNSBlockResponse> snsBlocks =
			snsBlockRepository.findAllById(snsBlockIds).stream()
				.map(SNSBlockResponse::from)
				.collect(Collectors.toList());

		return new SNSBlockResponses(snsBlocks);
	}

	public void update(@RequestBody final SNSBlockRequests<SNSBlockUpdateRequest> snsBlockRequests) {
		snsBlockRequests.getSubData().forEach(snsBlockRequest -> {

			SNSBlock snsBlock = snsBlockRepository.findSNSBlock(snsBlockRequest.getSnsId());

			SNSType snsType = SNSType.findSNSTypeByString(snsBlockRequest.getSnsType());

			snsBlock.update(snsBlockRequest.getSnsUUID(), snsType, snsBlockRequest.getSnsURL());

			snsBlockRepository.save(snsBlock);
		});
	}

	public void delete(final SNSBlockDeleteRequest snsBlockDeleteRequest) {
		List<SNSBlock> snsBlocks = snsBlockRepository.findAllById(snsBlockDeleteRequest.getSnsBlockIds());

		for (SNSBlock snsBlock: snsBlocks) {
			snsBlock.updateTimeOnDelete();
		}
		snsBlockRepository.saveAll(snsBlocks);
	}
}