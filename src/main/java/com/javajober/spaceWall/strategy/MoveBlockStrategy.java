package com.javajober.spaceWall.strategy;

import java.util.List;

public interface MoveBlockStrategy {
	List<Long> saveBlocks(final List<Object> subData);

	String getStrategyName();
}

