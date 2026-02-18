package com.teno.openmarket.system.feature.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * [파일 업로드 요청 DTO]
 * <p>
 * {@code multipart/form-data} 요청 시 전송되는 파일과 메타데이터를 바인딩합니다.
 */
@Getter
@Setter // @ModelAttribute 바인딩을 위해 Setter 필요
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileUploadRequest")
public class FileUploadRequest {

    /**
     * 업로드할 이미지 파일
     * <p>
     * 제약 사항
     * <ul>
     * <li>허용 확장자: jpg, jpeg, png, webp, gif</li>
     * <li>최대 크기: 5MB</li>
     * </ul>
     */
    @Schema
    @NotNull
    private MultipartFile file;

    /**
     * 저장 경로 (디렉토리)
     * <p>
     * S3 버킷 내에서 파일이 저장될 폴더명입니다. (예: products, banners, reviews)
     */
    @Schema
    @NotBlank
    private String dir;
}
