package br.com.fiap.pcsmidia.storage;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public class S3Test {

    public static void testeS3() {
        S3Client s3Client = S3Client.create();

        try {
            s3Client.listBuckets().buckets().forEach(bucket -> {
                System.out.println("Bucket name: " + bucket.name());
            });

            s3Client.listObjectsV2Paginator(builder -> builder.bucket("media-files-001"))
                    .contents()
                    .forEach(s3Object -> {
                        System.out.println("Arquivo: " + s3Object.key());
                        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket("media-files-001")
                                .key(s3Object.key())
                                .build();

                        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
                        System.out.println("File downloaded successfully from S3 bucket");
                    });

        } catch (Exception e) {
            System.err.println("Erro ao listar buckets: " + e.getMessage());
        }
    }
}
