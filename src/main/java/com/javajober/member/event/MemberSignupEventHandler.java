package com.javajober.member.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.javajober.space.service.SpaceService;

@Component
public class MemberSignupEventHandler {

	private final SpaceService spaceService;

	public MemberSignupEventHandler(final SpaceService spaceService) {
		this.spaceService = spaceService;
	}

	@Async("threadPoolTaskExecutor")
	@TransactionalEventListener(classes = MemberSignupEvent.class, phase = TransactionPhase.AFTER_COMMIT)
	public void handleMemberSignupEvent(final MemberSignupEvent event) {
		spaceService.initializeAndSaveNewMemberSpaces(event.getMember());
	}
}
