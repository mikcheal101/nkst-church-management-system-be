package com.mikkytrionze.nkst.member.domain.gateway;

import com.mikkytrionze.member.application.dto.MemberCreatedEvent;

public interface MemberEventPublisher {
    void publishMemberCreated(MemberCreatedEvent memberCreatedEvent);
}
