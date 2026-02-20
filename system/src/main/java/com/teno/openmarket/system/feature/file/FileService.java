package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.system.domain.exception.SystemErrorCode;
import com.teno.openmarket.system.domain.file.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorageRepository fileStorageRepository;

    // 허용 확장자 화이트리스트
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp", "gif");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * 이미지 파일 업로드 처리
     * <p>
     * 전달받은 {@link MultipartFile}의 유효성을 검증하고, UUID 기반의 고유한 파일명을 생성하여
     * 저장소(S3 등)에 업로드합니다.
     *
     * @param file      업로드할 이미지 파일 (필수, 이미지 타입이어야 함)
     * @param directory 파일이 저장될 경로 (예: "products", "banners")
     * @return 업로드된 파일의 접근 URL 및 메타데이터가 담긴 {@link FileUploadResponse}
     * @throws BusinessException 다음의 경우 발생
     * <ul>
     * <li>파일이 비어있거나 null인 경우 (INVALID_INPUT)</li>
     * <li>파일 크기가 허용치({@value #MAX_FILE_SIZE} bytes)를 초과한 경우 (FILE_SIZE_LIMIT)</li>
     * <li>지원하지 않는 확장자이거나 MIME 타입이 이미지가 아닌 경우 (FILE_TYPE_ERROR)</li>
     * <li>파일 저장소 업로드 중 I/O 에러가 발생한 경우 (FILE_UPLOAD_FAILED)</li>
     * </ul>
     */
    public FileUploadResponse uploadImage(MultipartFile file, String directory) {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String extension = getExtension(originalFileName);

        // 디렉토리/UUID.확장자 포맷 생성
        String storedFileName = createStoredFileName(directory, extension);

        try {
            String url = fileStorageRepository.upload(
                    file.getInputStream(),
                    storedFileName,
                    file.getContentType(),
                    file.getSize()
            );

            return FileUploadResponse.builder()
                    .url(url)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .build();
        } catch (IOException e) {
            throw new BusinessException(SystemErrorCode.SYSTEM_FILE_UPLOAD_FAILED);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.SYSTEM_INVALID_INPUT);
        }

        // 1. 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(SystemErrorCode.SYSTEM_FILE_SIZE_LIMIT);
        }

        // 2. MIME Type 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(SystemErrorCode.SYSTEM_FILE_TYPE_ERROR);
        }

        // 3. 확장자 검증
        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException(SystemErrorCode.SYSTEM_FILE_TYPE_ERROR);
        }
    }

    private String getExtension(String fileName) {
        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String createStoredFileName(String directory, String extension) {
        return String.format("%s/%s.%s", directory, UUID.randomUUID(), extension);
    }
}
