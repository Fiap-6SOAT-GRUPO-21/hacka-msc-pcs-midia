package br.com.fiap.pcsmidia.service.imp;

import br.com.fiap.pcsmidia.service.ProcessMessageService;
import br.com.fiap.pcsmidia.sns.service.SendSmsService;
import br.com.fiap.pcsmidia.sqs.MediaMessage;
import br.com.fiap.pcsmidia.sqs.producer.SQSProducer;
import br.com.fiap.pcsmidia.storage.service.S3MediaService;
import br.com.fiap.pcsmidia.util.constant.MediaStatus;
import br.com.fiap.pcsmidia.util.constant.SmsTextMessages;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessMessageServiceImpl implements ProcessMessageService {

    final S3MediaService s3MediaService;
    final SendSmsService sendSmsService;
    final ExtractFramesService extractFramesService;
    final SQSProducer producer;

    @Override
    public void processMedia(String message) throws Exception {

        Gson gson = new Gson();
        MediaMessage mediaMessage = gson.fromJson(message, MediaMessage.class);

        log.info("----------- Starting media processing -----------");
        mediaMessage.setStatus(MediaStatus.PROCESSING);
        producer.publishMessageResult(mediaMessage);
        log.info("Updated media metadata status to PROCESSING in repository with ID: {}", mediaMessage.getMediaId());

        File file = s3MediaService.downloadMedia(mediaMessage.getStoragePath());
        Path framesPath = extractFramesService.extractFramesFromFile(file);
        String zippedFolderPath = s3MediaService.uploadFrames(mediaMessage.getUserReference(), mediaMessage.getMediaId(), framesPath);

        mediaMessage.setStatus(MediaStatus.PROCESSED);
        mediaMessage.setZippedPath(zippedFolderPath);
        producer.publishMessageResult(mediaMessage);
        log.info("Updated media metadata status to PROCESSED in repository with ID: {}", mediaMessage.getMediaId());


        sendSmsService.sendSms(mediaMessage.getPhoneNumber(), SmsTextMessages.SUCCESSFULLY_PROCESSED);

        log.info("----------- Finished media processing -----------");
    }
}
