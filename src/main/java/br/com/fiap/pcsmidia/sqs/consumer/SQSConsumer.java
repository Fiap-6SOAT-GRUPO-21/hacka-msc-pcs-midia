package br.com.fiap.pcsmidia.sqs.consumer;

import br.com.fiap.pcsmidia.service.ProcessMessageService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class SQSConsumer {

    @Value("${variables.aws.queue-name-processing}")
    private String consumerQueueName;

    @Autowired
    private ProcessMessageService service;

    @Autowired
    private AmazonSQS amazonSQSClient;

    @Scheduled(fixedDelay = 5000)
    public void consumePrecessingMessages() {
        String queueUrl = null;

        try {
            queueUrl = amazonSQSClient.getQueueUrl(consumerQueueName).getQueueUrl();
        } catch (Exception e) {
            log.error("Failed to get SQS queue URL for {}: {}", consumerQueueName, e.getMessage(), e);
            return;
        }

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(20); // Espera até 20 segundos por novas mensagens;

        try {
            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);
            List<Message> messages = receiveMessageResult.getMessages();

            if (messages.isEmpty()) {
                log.info("No messages found in the queue.");
                return;
            }

            for (Message message : messages) {
                try {
                    service.processMedia(message.getBody());
                } catch (Exception e) {
                    log.error("Error processing message {}: {}", message.getMessageId(), e.getMessage(), e);
                }
                finally {
                    amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
                }
            }
        } catch (Exception e) {
            log.error("Failed to consume messages from SQS queue: {}", e.getMessage(), e);
        }

    }
}
