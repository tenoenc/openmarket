package com.teno.openmarket.user.feature.login;

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
public class LoginApi {

    private final LoginService loginService;
    private final LoginMapper loginMapper;

    /**
     * 사용자 로그인 및 토큰 발급 처리
     * <p>
     * 등록된 사용자의 이메일과 비밀번호를 검증하여 시스템 접근 권한을 부여합니다.
     * 인증 성공 시, API 요청 권한을 얻을 수 있는 Access Token과 만료 시 갱신을 위한 Refresh Token을 발급합니다.
     * </p>
     *
     * @param request 로그인에 필요한 이메일과 비밀번호 ({@link LoginRequest})
     * @return {@link ApiResponse} 성공 시 발급된 토큰 정보({@link TokenResponse})를 데이터로 포함하여 반환
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 요청")
    @ApiErrorCodeExamples({
            GlobalErrorCode.SYSTEM_INVALID_INPUT,
            GlobalErrorCode.USER_LOGIN_FAILED
    })
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginCommand command = loginMapper.toCommand(request);

        TokenResponse response = loginService.login(command);

        return ApiResponse.success(response);
    }
}
