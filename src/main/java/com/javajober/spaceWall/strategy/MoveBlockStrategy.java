package com.javajober.spaceWall.strategy;

import static com.javajober.core.exception.ApiStatus.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.core.exception.ApplicationException;
import com.javajober.core.util.response.CommonResponse;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

public interface MoveBlockStrategy {
	void saveStringBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position);

	void saveBlocks(final BlockSaveRequest<?> block, final ArrayNode blockInfoArray, final Long position);

	List<CommonResponse> createMoveBlockDTO(List<JsonNode> blocksWithSamePosition);

	Set<Long> updateBlocks(final BlockSaveRequest<?> blocks, final ArrayNode blockInfoArray, final Long position);

	void deleteAllById(final Set<Long> blockIds);

	String getStrategyName();

	default void uploadFile(final MultipartFile file) {
		throw new ApplicationException(FILE_PROCESSING_NOT_SUPPORTED, "파일 처리를 지원하지 않습니다.");
	}
}

