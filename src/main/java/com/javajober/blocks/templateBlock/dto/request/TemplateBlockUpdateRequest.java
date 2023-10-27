package com.javajober.blocks.templateBlock.dto.request;

import java.util.List;

import com.javajober.blocks.templateBlock.domain.TemplateBlock;

import lombok.Getter;

@Getter
public class TemplateBlockUpdateRequest {

	private Long templateBlockId;
	private String templateUUID;
	private String templateTitle;
	private String templateDescription;
	private List<Long> hasAccessTemplateAuth;
	private List<Long> hasDenyTemplateAuth;

	private TemplateBlockUpdateRequest() {

	}

	public static TemplateBlock toEntity(final TemplateBlockUpdateRequest templateBlockUpdateRequest) {
		return TemplateBlock.builder()
			.templateUUID(templateBlockUpdateRequest.getTemplateUUID())
			.templateTitle(templateBlockUpdateRequest.getTemplateTitle())
			.templateDescription(templateBlockUpdateRequest.getTemplateDescription())
			.build();

	}
}