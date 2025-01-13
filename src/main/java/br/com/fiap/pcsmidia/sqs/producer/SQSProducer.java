package br.com.fiap.pcsmidia.sqs.producer;

import br.com.fiap.pcsmidia.sqs.model.MediaMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SQSProducer {

    @Value("${variables.aws.queue-name-result}")
    private String publisherQueueName;

    @Autowired
    private AmazonSQS amazonSQSClient;

    public void publishMessageResult(MediaMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Obter a URL da fila
            GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(publisherQueueName);

            // Criar os atributos da mensagem (opcional)
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            MessageAttributeValue timestampAttribute = new MessageAttributeValue();
            timestampAttribute.setDataType("String");
            timestampAttribute.setStringValue(new Date().toString());
            messageAttributes.put("Timestamp", timestampAttribute);

            // Enviar a mensagem para a fila
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl.getQueueUrl())
                    .withMessageBody(objectMapper.writeValueAsString(message))
                    .withMessageAttributes(messageAttributes);

            amazonSQSClient.sendMessage(sendMessageRequest);
            log.info(" ========================== Message published: {}", objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage(), e);
        }
    }
}
