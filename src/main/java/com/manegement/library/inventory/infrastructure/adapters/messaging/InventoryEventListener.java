package com.manegement.library.inventory.infrastructure.adapters.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manegement.library.inventory.domain.ports.inbound.InventoryUseCase;
import com.manegement.library.shared.events.LoanCreated;
import com.manegement.library.shared.events.LoanReturned;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.List;

import static io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode.MANUAL;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventListener {

    private final SqsAsyncClient amazonSQS;
    private final ObjectMapper objectMapper;
    private final InventoryUseCase inventoryUseCase;

    @SqsListener(value = "${aws.sqs.queues.inventory-queue-name}", acknowledgementMode = MANUAL)
    public void listen(String event, Acknowledgement acknowledgement) {
        log.info("Inventory Event Listener - Event consumed: {}", event);
        try {
            // Process the event
            processMessage(event);
            // Acknowledge the message
            acknowledgement.acknowledge();
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage(), e);
            // Optionally, you can handle the error or requeue the message
        }
    }

    private void processMessage(String body) {
        try {

            // Déterminer le type d'événement et le traiter
            if (body.contains("LoanCreated")) {
                LoanCreated event = objectMapper.readValue(body, LoanCreated.class);
                handleLoanCreated(event);
            } else if (body.contains("LoanReturned")) {
                LoanReturned event = objectMapper.readValue(body, LoanReturned.class);
                handleLoanReturned(event);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    private void handleLoanCreated(LoanCreated event) {
        log.info("Handling LoanCreated event for ISBN: {}", event.getIsbn());
        inventoryUseCase.handleBookBorrowed(event.getIsbn(), event.getUserId());
    }

    private void handleLoanReturned(LoanReturned event) {
        log.info("Handling LoanReturned event for ISBN: {}", event.getIsbn());
        inventoryUseCase.handleBookReturned(event.getIsbn());
    }
}
