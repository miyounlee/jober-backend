package com.javajober.snsBlock.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class SNSBlockRequests<T> {

	List<T> subData;

	private SNSBlockRequests() {

	}

	public SNSBlockRequests(List<T> subData) {
		this.subData = subData;
	}
}