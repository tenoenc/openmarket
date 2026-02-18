package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
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
            throw new BusinessException(GlobalErrorCode.SYSTEM_FILE_UPLOAD_FAILED);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(GlobalErrorCode.SYSTEM_INVALID_INPUT);
        }

        // 1. 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(GlobalErrorCode.SYSTEM_FILE_SIZE_LIMIT);
        }

        // 2. MIME Type 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(GlobalErrorCode.SYSTEM_FILE_TYPE_ERROR);
        }

        // 3. 확장자 검증
        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException(GlobalErrorCode.SYSTEM_FILE_TYPE_ERROR);
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
