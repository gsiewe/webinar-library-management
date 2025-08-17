package com.manegement.library.shared.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.manegement.library.shared.events.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Configuration
public class AwsEventConfig {

    @Value("${aws.region:eu-west-1}")
    private String region;

    @Value("${aws.sns.topics.inventory:arn:aws:sns:eu-west-1:123456789012:inventory-events}")
    private String inventoryTopic;

    @Value("${aws.sns.topics.loan:arn:aws:sns:eu-west-1:123456789012:loan-events}")
    private String loanTopic;

    @Value("${aws.sns.topics.fine:arn:aws:sns:eu-west-1:123456789012:fine-events}")
    private String fineTopic;

    @Bean
    @Profile("!test")
    public SnsAsyncClient amazonSNS() {
        return SnsAsyncClient.builder().region(Region.of(region)).build();
    }

    @Bean
    @Profile("!test")
    public SqsAsyncClient amazonSQS() {
        return SqsAsyncClient.builder().region(Region.of(region)).build();
    }

    @Bean
    public ObjectMapper eventObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    @Profile("!test")
    public EventPublisher awsEventPublisher(SnsAsyncClient amazonSNS, ObjectMapper eventObjectMapper) {
        return new AwsEventPublisher(amazonSNS, eventObjectMapper);
    }

    @Bean
    @Profile("test")
    public EventPublisher mockEventPublisher() {
        return new MockEventPublisher();
    }

    // Implementation for AWS
    @Slf4j
    static class AwsEventPublisher implements EventPublisher {
        private final SnsAsyncClient snsClient;
        private final ObjectMapper objectMapper;

        public AwsEventPublisher(SnsAsyncClient snsClient, ObjectMapper objectMapper) {
            this.snsClient = snsClient;
            this.objectMapper = objectMapper;
        }

        @Override
        public void publish(String topic, DomainEvent event) {
            try {
                String message = objectMapper.writeValueAsString(event);
                PublishRequest publishRequest = PublishRequest.builder().message(message).topicArn(topic).build();
                snsClient.publish(publishRequest);
                log.info("Published event {} to topic {}", event.getEventType(), topic);
            } catch (Exception e) {
                log.error("Failed to publish event: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to publish event", e);
            }
        }

        @Override
        public void publishAsync(String topic, DomainEvent event) {
            // Pour la démo, on peut utiliser CompletableFuture
            java.util.concurrent.CompletableFuture.runAsync(() -> publish(topic, event));
        }
    }

    // Mock implementation for testing
    @Slf4j
    static class MockEventPublisher implements EventPublisher {
        @Override
        public void publish(String topic, DomainEvent event) {
            log.info("[MOCK] Published event {} to topic {}", event.getEventType(), topic);
        }

        @Override
        public void publishAsync(String topic, DomainEvent event) {
            log.info("[MOCK] Async published event {} to topic {}", event.getEventType(), topic);
        }
    }

    // Constants for topic names
    public static class Topics {
        public static final String INVENTORY_EVENTS = "inventory-events";
        public static final String LOAN_EVENTS = "loan-events";
        public static final String FINE_EVENTS = "fine-events";
    }
}
