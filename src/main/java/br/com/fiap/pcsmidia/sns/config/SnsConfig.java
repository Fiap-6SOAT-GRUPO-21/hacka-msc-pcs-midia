package br.com.fiap.pcsmidia.sns.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Log4j2
@Configuration
public class SnsConfig {

    @Value("${variables.aws.region}")
    private String region;

    @Value("${variables.aws.access-key}")
    private String accessKey;

    @Value("${variables.aws.secret-key}")
    private String secretKey;

    @Value("${variables.aws.session-token}")
    private String sessionToken;

    @Value("${variables.env}")
    private String env;

    @Bean
    public SnsClient snsClient() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Se for ambiente local (usando o LocalStack)
        if (env.equals("local")) {
            log.info("Running in local environment, using LocalStack for S3");
            return SnsClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(() -> awsBasicCredentials)
                    .endpointOverride(URI.create("http://localhost:4566"))
                    .build();
        }


        log.info("Running SNS in AWS environment");
        return SnsClient.builder()
                .credentialsProvider(() -> awsBasicCredentials)
                .region(Region.of(region)) // Altere para sua região, se necessário
                .build();
    }
}
