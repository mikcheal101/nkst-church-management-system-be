package com.mikkytrionze.nkst.member.infrastructure.messaging.kafka;

import com.mikkytrionze.member.application.dto.MemberCreatedEvent;
import com.mikkytrionze.nkst.member.domain.gateway.MemberEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMemberEventPublisher implements MemberEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final static String MEMBER_CREATED_TOPIC = "member-created-topic";

    @Override
    public void publishMemberCreated(MemberCreatedEvent memberCreatedEvent) {
        log.info("Infrastructure: Publishing MemberCreatedEvent to kafka. Member ID: {}", memberCreatedEvent.getMemberId());

        try {
            kafkaTemplate.send(MEMBER_CREATED_TOPIC, memberCreatedEvent.getMemberId().toString(), memberCreatedEvent);
            log.info("Infrastructure: Event published successfully!");
        } catch (Exception e) {
            log.error("Infrastructure: Failed to publish event to kafka", e);
            throw new RuntimeException("messaging failed!", e);
        }
    }
}
