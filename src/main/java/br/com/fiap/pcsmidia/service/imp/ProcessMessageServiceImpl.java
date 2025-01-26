package br.com.fiap.pcsmidia.service.imp;

import br.com.fiap.pcsmidia.repository.MediaRepository;
import br.com.fiap.pcsmidia.repository.entity.MediaMetadata;
import br.com.fiap.pcsmidia.service.ProcessMessageService;
import br.com.fiap.pcsmidia.storage.service.S3MediaService;
import br.com.fiap.pcsmidia.sns.service.SendSmsService;
import br.com.fiap.pcsmidia.sqs.model.MediaMessage;
import br.com.fiap.pcsmidia.util.enumerated.MediaStatus;
import br.com.fiap.pcsmidia.util.exception.MediaMetadataNotFound;
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

    final MediaRepository mediaRepository;
//    final SQSProducer sqsProducer;
    final S3MediaService s3MediaService;
    final SendSmsService sendSmsService;
    final ExtractFramesService extractFramesService;

    @Override
    public void processMedia(String message) throws Exception {

        // Ler mensagem do SQS
        Gson gson = new Gson();
        MediaMessage mediaMessage = gson.fromJson(message, MediaMessage.class);

        log.info("----------- Starting media processing -----------");

        // Alterando Status para Processando
        MediaMetadata mediaMetadata = mediaRepository.findById(mediaMessage.mediaId()).orElseThrow(MediaMetadataNotFound::new);
        mediaMetadata.setStatus(MediaStatus.PROCESSING);
        mediaRepository.save(mediaMetadata);
        log.info("Updated media metadata status to PROCESSING in repository with ID: {}", mediaMetadata.getMediaId());

        File file = s3MediaService.downloadMedia(mediaMessage.storagePath());
        Path framesPath = extractFramesService.extractFramesFromFile(file);
        s3MediaService.uploadFrames(file.getName(), framesPath);

        String zippedFolderPath = "frames_teste001/" + file.getName() + "/";
        mediaMetadata.setZippedFolderPath(zippedFolderPath);
        mediaMetadata.setStatus(MediaStatus.PROCESSED);
        mediaRepository.save(mediaMetadata);
        log.info("Updated media metadata status to PROCESSED in repository with ID: {}", mediaMetadata.getMediaId());

        sendSmsService.sendSms("+5511982478239", "Mensagem Teste Gabriel");
        sendSmsService.sendSms("+5511947015353", "Mensagem Teste Gabriel");
        sendSmsService.sendSms("+5511972656287", "Mensagem Teste Gabriel");
        sendSmsService.sendSms("+5511959554644", "Mensagem Teste Gabriel");


        // Fazer Download do vídeo como InputStream
        // InputStream file = s3MediaService.downloadMediaAsStream(mediaMessage.storagePath());

        // Processar o vídeo de um InputStream
//        extractFramesService.extractFramesFromStream(InputStream file, Path.of("./downloadLocal"));;
//
//        // Enviar para o managment uma mensagem de sucesso

    }
}
