package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.system.domain.exception.SystemErrorCode;
import com.teno.openmarket.system.domain.file.FileStorageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileStorageRepository fileStorageRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    @DisplayName("정상적인 이미지 파일을 업로드하면 S3 URL과 메타데이터를 반환해야 한다")
    void should_ReturnUrlAndMeta_When_ValidImageUploaded() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "Test content".getBytes()
        );
        String expectedUrl = "https://cdn.openmarket.com/products/uuid.png";

        given(fileStorageRepository.upload(any(InputStream.class), anyString(), anyString(), anyLong()))
                .willReturn(expectedUrl);

        // when
        FileUploadResponse response = fileService.uploadImage(file, "products");

        // then
        assertThat(response.getUrl()).isEqualTo(expectedUrl);
        assertThat(response.getOriginalFileName()).isEqualTo("test.png");
        // 저장된 파일명은 UUID가 포함되어야 하므로 원본 파일명과 달라야 함
        assertThat(response.getStoredFileName()).isNotEqualTo("test.png");
        assertThat(response.getStoredFileName()).endsWith(".png");
        assertThat(response.getStoredFileName()).startsWith("products/");
    }

    @Test
    @DisplayName("허용되지 않은 확장자(.exe) 파일을 업로드하면 예외가 발생해야 한다")
    void should_ThrowException_When_InvalidExtensionFileUploaded() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "virus.exe", "application/octet-stream", "Bad code".getBytes()
        );

        // when & then
        assertThatThrownBy(() -> fileService.uploadImage(file, "products"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SystemErrorCode.SYSTEM_FILE_TYPE_ERROR.getMessage());
    }

    @Test
    @DisplayName("MIME 타입이 이미지가 아니면 예외가 발생해야 한다")
    void should_ThrowException_When_MimeTypeIsNotImage() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello".getBytes()
        );

        // when & then
        assertThatThrownBy(() -> fileService.uploadImage(file, "banners"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SystemErrorCode.SYSTEM_FILE_TYPE_ERROR.getMessage());
    }

    @Test
    @DisplayName("5MB를 초과하는 대용량 파일은 업로드가 거부되어야 한다")
    void should_ThrowException_When_FileSizeExceeded() {
        // given
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB 더미 데이터
        MockMultipartFile file = new MockMultipartFile(
                "file", "big_photo.jpg", "image/jpeg", largeContent
        );

        // when & then
        assertThatThrownBy(() -> fileService.uploadImage(file, "products"))
                .isInstanceOf(BusinessException.class)
                .hasMessage(SystemErrorCode.SYSTEM_FILE_SIZE_LIMIT.getMessage());
    }
}
