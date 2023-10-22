package com.javajober.spaceWall.strategy;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javajober.spaceWall.dto.request.BlockSaveRequest;

@Component
public class BlockJsonProcessor {
	private final ObjectMapper jsonMapper;

	public BlockJsonProcessor(final ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public ArrayNode createArrayNode() {
		return jsonMapper.createArrayNode();
	}

	public <T> T convertValue(final Object fromValue, final Class<T> toValueType) {
		return jsonMapper.convertValue(fromValue, toValueType);
	}

	public void addBlockInfoToArray(final ArrayNode blockInfoArray, final Long position, final Long blockId, final BlockSaveRequest block) {

		String currentBlockTypeTitle = block.getBlockType();
		String blockUUID = block.getBlockUUID();

		ObjectNode blockInfoObject = jsonMapper.createObjectNode();

		blockInfoObject.put("position", position);
		blockInfoObject.put("block_type", currentBlockTypeTitle);
		blockInfoObject.put("block_id", blockId);
		blockInfoObject.put("block_uuid", blockUUID);

		blockInfoArray.add(blockInfoObject);
	}


	public void addBlockToJsonArray(final ArrayNode blockInfoArray, final Long position, final String blockType, final Long blockId) {

		ObjectNode blockInfoObject = jsonMapper.createObjectNode();

		blockInfoObject.put("position", position);
		blockInfoObject.put("block_type", blockType);
		blockInfoObject.put("block_id", blockId);
		blockInfoObject.put("block_uuid", "");

		blockInfoArray.add(blockInfoObject);
	}
}
