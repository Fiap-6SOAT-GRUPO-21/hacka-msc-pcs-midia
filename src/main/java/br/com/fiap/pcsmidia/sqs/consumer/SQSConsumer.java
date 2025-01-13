package br.com.fiap.pcsmidia.sqs.consumer;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SQSConsumer {

    @Value("${variables.aws.queue-name-result}")
    private String consumerQueueName;

    @Autowired
    private AmazonSQS amazonSQSClient;

    @Scheduled(fixedDelay = 5000)
    public void consumeResultMessages() {
        try {
            // Obter a URL da fila
            String queueUrl = amazonSQSClient.getQueueUrl(consumerQueueName).getQueueUrl();

            // Configurar a requisição para receber mensagens
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMaxNumberOfMessages(1) // Receber até 5 mensagens por vez
                    .withWaitTimeSeconds(10);  // Polling longo (10 segundos)

            // Receber mensagens
            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);

            // Processar mensagens recebidas
            for (com.amazonaws.services.sqs.model.Message message : receiveMessageResult.getMessages()) {
                log.info("====================== Read Message from queue: {}", message.getBody());

                // Apagar mensagem da fila
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }

        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage(), e);
        }
    }
}
