package br.com.fiap.pcsmidia.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3MediaServiceImpl implements S3MediaService {

    @Value("${variables.aws.bucket-name-media}")
    private String bucketName;

    private final S3Client s3Client = S3Client.create();

    @Override
    public File downloadMedia(String path) {

        try {
            log.info("Downloading file from S3 bucket: {}", bucketName);
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(objectRequest);
            String fileName = Paths.get(path).getFileName().toString();
            File localFile = new File("./mediaToProcess/" + fileName);
            localFile.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = s3Object.read(buffer)) > 0) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            log.info("File downloaded to: {}", localFile.getAbsolutePath());
            return localFile;
        } catch (Exception e) {
            log.error("Error downloading file from S3 {}: ", path);
            throw new RuntimeException("Error downloading file from S3", e);
        }
    }

    @Override
    public InputStream downloadMediaAsStream(String path) {

        log.info("Downloading file as Stream from S3 bucket: {}", bucketName);
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();

        return s3Client.getObject(objectRequest);

//        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(objectRequest)) {
//            log.info("File downloaded successfully from S3 bucket: {}", bucketName);
//            return response;
//        } catch (IOException e) {
//            throw new IllegalStateException("Error downloading file from S3 bucket: " + bucketName, e);
//        }
    }

    @Override
    public void uploadFrames(String path, Path framesDir) {

        log.info("Uploading frames to S3 folder: {}", path);
        try {
            Files.list(framesDir).forEach(frame -> {
                String fileKey = "frames_teste001/" + path + "/" + frame.getFileName().toString();
                try {

                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .build();

                    s3Client.putObject(putObjectRequest, frame);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao enviar frame: " + frame.getFileName(), e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar frames no diretório: " + framesDir, e);
        }

        log.info("Uploaded all frames to S3 folder: {}", path);
    }
}
