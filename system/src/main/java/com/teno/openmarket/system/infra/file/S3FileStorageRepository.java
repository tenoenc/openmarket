package com.teno.openmarket.system.infra.file;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.system.domain.exception.SystemErrorCode;
import com.teno.openmarket.system.domain.file.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class S3FileStorageRepository implements FileStorageRepository {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public String upload(InputStream inputStream, String fileName, String contentType, long size) {
        try (inputStream) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, size));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
        } catch (Exception e) {
            log.error("S3 upload failed: {}", fileName, e);
            throw new BusinessException(SystemErrorCode.SYSTEM_FILE_UPLOAD_FAILED);
        }
    }
}
