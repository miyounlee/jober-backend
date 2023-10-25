package com.javajober.spaceWall.strategy;

import static com.javajober.core.exception.ApiStatus.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.javajober.core.exception.ApplicationException;
import com.javajober.core.util.response.CommonResponse;
import com.javajober.spaceWall.dto.request.DataStringSaveRequest;
import com.javajober.spaceWall.filedto.DataSaveRequest;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FixBlockStrategy {
	void saveStringBlocks(final DataStringSaveRequest data, final ArrayNode blockInfoArray, final Long position);

	void saveBlocks(final DataSaveRequest data, ArrayNode blockInfoArray, Long position);

	CommonResponse createFixBlockDTO(final List<JsonNode> fixBlocks);

	String getStrategyName();

	default void uploadTwoFiles(final MultipartFile firstFile, final MultipartFile secondFile) {
		throw new ApplicationException(FILE_PROCESSING_NOT_SUPPORTED, "파일 처리를 지원하지 않습니다.");
	}

	default void uploadSingleFile(final MultipartFile singleFile) {
		throw new ApplicationException(FILE_PROCESSING_NOT_SUPPORTED, "파일 처리를 지원하지 않습니다.");
	}
}
