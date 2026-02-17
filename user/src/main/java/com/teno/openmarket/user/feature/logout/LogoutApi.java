package com.teno.openmarket.user.feature.logout;

import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LogoutApi {

    private final LogoutService logoutService;

    /**
     * 로그아웃 처리
     * <p>
     * 인증된 사용자의 로그아웃 요청을 처리하여 현재 토큰의 유효성을 파기합니다.
     * 서버 세션이 없는 Stateless 환경이므로, 토큰 자체를 무효화하는 전략을 사용합니다.
     *
     * @param request Authorization 헤더에 Bearer Access Token을 포함한 HTTP 요청
     * @return {@link ApiResponse} 성공 시 별도의 데이터 페이로드 없이 {@code 200 OK} 결과만 반환
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String token = resolveToken(request);

        if (token != null) {
            logoutService.logout(token);
        }

        return ApiResponse.success();
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
