package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.common.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.system.domain.exception.SystemErrorCode;
import com.teno.openmarket.system.domain.exception.SystemErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "System")
@SecurityErrorDocs
public interface FileApiDocs {

    @Operation(summary = "이미지 파일 업로드")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @SystemErrorCodeExamples({
        SystemErrorCode.SYSTEM_FILE_UPLOAD_FAILED,
        SystemErrorCode.SYSTEM_FILE_SIZE_LIMIT,
        SystemErrorCode.SYSTEM_FILE_TYPE_ERROR
    })
    ApiResponse<FileUploadResponse> uploadImage(FileUploadRequest request);
}
