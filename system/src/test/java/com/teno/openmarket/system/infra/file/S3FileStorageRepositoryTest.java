package com.teno.openmarket.system.infra.file;

import com.teno.openmarket.system.domain.file.FileStorageRepository;
import com.teno.openmarket.test.support.BaseS3Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(classes = {S3FileStorageRepository.class})
public class S3FileStorageRepositoryTest extends BaseS3Test {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private S3Client s3Client; // BaseS3Test에서 주입됨

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전 버킷 생성 (LocalStack은 휘발성이므로 매번 생성 필요
        try {
            s3Client.createBucket(b -> b.bucket(bucket));
        } catch (Exception e) {
            // 이미 존재하면 무시
        }
    }

    @Test
    @DisplayName("파일을 업로드하면 S3 버킷에 저장되고 접근 가능한 URL을 반환해야 한다")
    void should_SaveFileToBucketAndReturnValidUrl_When_FileIsUploaded() {
        // given
        String fileName = "products/test-image.png";
        String content = "Dummy image content";
        long size = content.length();
        String contentType = "image/png";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        // when
        String resultUrl = fileStorageRepository.upload(inputStream, fileName, contentType, size);

        // then
        // 1. URL 형식이 올바른지 검증
        assertThat(resultUrl).contains(bucket);
        assertThat(resultUrl).contains(fileName);

        // 2. 실제 S3(LocalStack)에 파일이 존재하는지 검증
        assertThatCode(() -> s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build()))
                .doesNotThrowAnyException();
    }
}
