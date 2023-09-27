package com.javajober.snsBlock.dto;

import com.javajober.snsBlock.domain.SNSBlock;
import com.javajober.snsBlock.domain.SNSType;

import lombok.Getter;

@Getter
public class SNSBlockRequest {

	private String snsUUID;
	private String snsType;
	private String snsURL;

	private SNSBlockRequest() {

	}

	public static SNSBlock toEntity(final SNSBlockRequest snsBlockRequest) {
		return SNSBlock.builder()
			.snsUUID(snsBlockRequest.getSnsUUID())
			.snsType(SNSType.findSNSTypeByString(snsBlockRequest.getSnsType()))
			.snsURL(snsBlockRequest.getSnsURL())
			.build();
	}
}
