package com.javajober.blocks.snsBlock.dto.request;

import com.javajober.blocks.snsBlock.domain.SNSBlock;
import com.javajober.blocks.snsBlock.domain.SNSType;

import lombok.Getter;

@Getter
public class SNSBlockUpdateRequest {

	private Long snsBlockId;
	private String snsUUID;
	private String snsType;
	private String snsURL;

	private SNSBlockUpdateRequest() {

	}
	public static SNSBlock toEntity(SNSBlockUpdateRequest request) {
		return SNSBlock.builder()
			.snsUUID(request.getSnsUUID())
			.snsType(SNSType.findSNSTypeByString(request.getSnsType()))
			.snsURL(request.getSnsURL())
			.build();
	}
}