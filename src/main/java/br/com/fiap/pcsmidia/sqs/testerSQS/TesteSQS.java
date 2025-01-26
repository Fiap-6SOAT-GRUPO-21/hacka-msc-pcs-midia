package br.com.fiap.pcsmidia.sqs.testerSQS;

//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.model.*;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Log4j2
//@Service
//public class TesteSQS {
//
//    @Value("${variables.aws.queue-name-processing}")
//    private String consumerQueueName;
//
//    @Autowired
//    private AmazonSQS amazonSQSClient;
//
//    public void publishMessage(String body) {
//        try {
//            // Obter a URL da fila
//            GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(consumerQueueName);
//
//            // Criar os atributos da mensagem (opcional)
//            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
//            MessageAttributeValue timestampAttribute = new MessageAttributeValue();
//            timestampAttribute.setDataType("String");
//            timestampAttribute.setStringValue(new Date().toString());
//            messageAttributes.put("Timestamp", timestampAttribute);
//
//            // Enviar a mensagem para a fila
//            SendMessageRequest sendMessageRequest = new SendMessageRequest()
//                    .withQueueUrl(queueUrl.getQueueUrl())
//                    .withMessageBody(body)
//                    .withMessageAttributes(messageAttributes);
//
//            amazonSQSClient.sendMessage(sendMessageRequest);
//            log.info(" ========================== Message published: {}", body);
//        } catch (Exception e) {
//            log.error("Queue Exception Message: {}", e.getMessage(), e);
//        }
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void consumeMessages() {
//        try {
//            // Obter a URL da fila
//            String queueUrl = amazonSQSClient.getQueueUrl(consumerQueueName).getQueueUrl();
//
//            // Configurar a requisição para receber mensagens
//            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
//                    .withQueueUrl(queueUrl)
//                    .withMaxNumberOfMessages(1) // Receber até 5 mensagens por vez
//                    .withWaitTimeSeconds(10);  // Polling longo (10 segundos)
//
//            // Receber mensagens
//            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);
//
//            // Processar mensagens recebidas
//            for (Message message : receiveMessageResult.getMessages()) {
//                log.info("====================== Read Message from queue: {}", message.getBody());
//
//                // Apagar mensagem da fila
//                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
//            }
//
//        } catch (Exception e) {
//            log.error("Queue Exception Message: {}", e.getMessage(), e);
//        }
//    }
//}
