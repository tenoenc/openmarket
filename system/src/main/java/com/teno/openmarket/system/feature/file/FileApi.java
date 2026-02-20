package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.common.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.common.error.ErrorCode;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.system.domain.exception.SystemErrorCode;
import com.teno.openmarket.system.domain.exception.SystemErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System")
@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class FileApi {

    private final FileService fileService;

    /**
     * 이미지 파일 업로드
     * <p>
     * 이미지 파일을 클라우드 스토리지(S3)에 업로드하고, 외부에서 접근 가능한 Public URL을 반환합니다.
     * <br>
     * 요청은 반드시 {@code multipart/form-data} 형식이어야 합니다.
     *
     * @param request 업로드할 파일과 저장 경로 정보
     * @return 업로드된 파일의 URL 및 원본 파일명 정보를 담은 응답 객체
     */
    @Operation(summary = "이미지 파일 업로드")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @SystemErrorCodeExamples({
        SystemErrorCode.SYSTEM_FILE_UPLOAD_FAILED,
        SystemErrorCode.SYSTEM_FILE_SIZE_LIMIT,
        SystemErrorCode.SYSTEM_FILE_TYPE_ERROR
    })
    public ApiResponse<FileUploadResponse> uploadImage(
        // @ModelAttribute를 사용하면 Swagger가 DTO의 필드(@Schema)를 읽어 자동으로 문서를 만들어줍니다.
        @ModelAttribute @Valid FileUploadRequest request
    ) {
        return ApiResponse.success(fileService.uploadImage(request.getFile(), request.getDir()));
    }
}
