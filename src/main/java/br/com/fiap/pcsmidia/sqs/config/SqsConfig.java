package br.com.fiap.pcsmidia.sqs.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class SqsConfig {

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
    public AmazonSQS amazonSQSClient() {
        BasicSessionCredentials awsCredentials = new BasicSessionCredentials(accessKey, secretKey, sessionToken);

        if (env.equals("local")) {
            log.info("Running in local environment, using localstack");
            return AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withEndpointConfiguration(new AmazonSQSClientBuilder.EndpointConfiguration("http://localhost:4566", region))
                    .build();
        }

        log.info("Running in AWS environment");
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.fromName(region))
                .build();
    }

}
