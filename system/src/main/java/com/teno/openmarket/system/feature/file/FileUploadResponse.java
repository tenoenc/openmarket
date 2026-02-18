package com.teno.openmarket.system.feature.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * [파일 업로드 응답 DTO]
 * <p>
 * S3 또는 스토리지에 파일 업로드가 완료된 후 반환되는 메타데이터입니다.
 * 클라이언트는 반환된 URL을 사용하여 이미지를 렌더링하거나 다운로드할 수 있습니다.
 */
@Getter
@Builder
@Schema(name = "FileUploadResponse")
public class FileUploadResponse {

    /**
     * 파일 접근 URL
     * <p>
     * 외부에서 접근 가능한 Public URL입니다. (예: S3 URL 또는 CloudFront 도메인)
     */
    @Schema(example = "https://teno-storage.s3.ap-northeast-2.amazonaws.com/products/uuid-image.png")
    private String url;

    /**
     * 원본 파일명
     * <p>
     * 사용자가 업로드할 당시의 파일명입니다. (확장자 포함)
     */
    @Schema(example = "profile_image.jpg")
    private final String originalFileName;

    /**
     * 저장된 파일명
     * <p>
     * 중복 방지를 위해 UUID 등이 적용된 서버 저장용 파일명(Key)입니다.
     */
    @Schema(example = "products/550e8400-e29b-41d4-a716-446655440000.jpg")
    private final String storedFileName;
}
