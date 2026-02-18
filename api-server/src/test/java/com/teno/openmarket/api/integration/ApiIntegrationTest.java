package com.teno.openmarket.api.integration;

import com.teno.openmarket.test.support.BaseIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    // 빈 이름이 겹칠 때 에러를 내지 않고 나중에 등록된 빈(자식의 운영 설정)으로 덮어씁니다.
    properties = "spring.main.allow-bean-definition-overriding=true"
)
@Testcontainers
@Import(ApiIntegrationTest.TestS3Config.class)
public abstract class ApiIntegrationTest extends BaseIntegrationTest {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:3.2.0");

    @Container
    static final LocalStackContainer LOCALSTACK = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(LocalStackContainer.Service.S3);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cloud.aws.s3.bucket", () -> "test-bucket");
        registry.add("cloud.aws.region.static", LOCALSTACK::getRegion);
        registry.add("cloud.aws.credentials.access-key", LOCALSTACK::getAccessKey);
        registry.add("cloud.aws.credentials.secret-key", LOCALSTACK::getSecretKey);
    }

    @TestConfiguration
    static class TestS3Config {
        @Bean
        public S3Client s3Client() {
            return S3Client.builder()
                    .endpointOverride(LOCALSTACK.getEndpointOverride(LocalStackContainer.Service.S3))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(LOCALSTACK.getAccessKey(), LOCALSTACK.getSecretKey())
                    ))
                    .region(Region.of(LOCALSTACK.getRegion()))
                    .build();
        }
    }
}
