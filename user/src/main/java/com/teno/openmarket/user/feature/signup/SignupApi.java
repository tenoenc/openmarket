package com.teno.openmarket.user.feature.signup;

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

@Tag(name = "Auth", description = "회원 인증 및 관리 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class SignupApi {

    private final SignupService signupService;
    private final SignupMapper signupMapper;

    /**
     * 신규 회원 가입 처리
     * <p>
     * 미가입 사용자의 회원 가입 요청을 처리합니다.
     * 입력된 정보의 유효성을 검증하고, 이메일 중복 확인 후 새로운 회원 정보를 시스템에 등록합니다.
     *
     * @param request 회원 가입에 필요한 필수 정보
     * @return {@link ApiResponse} 성공 시 별도의 데이터 페이로드 없이 {@code 200 OK} 결과만 반환
     */
    @PostMapping("/signup")
    @Operation(summary = "회원 가입 요청")
    @ApiErrorCodeExamples({
        GlobalErrorCode.SYSTEM_INVALID_INPUT,
        GlobalErrorCode.USER_TERMS_REQUIRED,
        GlobalErrorCode.USER_ALREADY_EXISTS
    })
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequest request) {
        SignupCommand command = signupMapper.toCommand(request);

        signupService.signup(command);

        return ApiResponse.success();
    }
}
