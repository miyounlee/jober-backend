package com.javajober.spaceWall.strategy;

import com.javajober.spaceWall.dto.request.DataStringSaveRequest;

public interface FixBlockStrategy {
	Long saveBlocks(final DataStringSaveRequest data);

	String getStrategyName();
}
