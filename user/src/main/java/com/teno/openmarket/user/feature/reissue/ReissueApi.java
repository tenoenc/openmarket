package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ReissueApi {
    private final ReissueService reissueService;
    private final ReissueMapper reissueMapper;

    /**
     * 인증 토큰 재발급 (Reissue)
     * <p>
     * 만료된 Access Token을 대신하여, Refresh Token을 사용해 새로운 토큰 쌍을 발급받습니다.
     * RTR(Refresh Token Rotation) 정책에 따라 사용된 Refresh Token은 폐기되고 새로운 Refresh Token이 발급됩니다.
     *
     * @param request 유효한 Refresh Token을 포함한 요청 객체
     * @return {@link ApiResponse} 갱신된 Access/Refresh Token 정보
     */
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급 요청")
    @ApiErrorCodeExamples({
        GlobalErrorCode.SYSTEM_INVALID_INPUT,
        GlobalErrorCode.USER_TOKEN_EXPIRED
    })
    public ApiResponse<TokenResponse> reissue(@RequestBody @Valid ReissueRequest request) {
        ReissueCommand command = reissueMapper.toCommand(request);

        TokenResponse response = reissueService.reissue(command);

        return ApiResponse.success(response);
    }
}