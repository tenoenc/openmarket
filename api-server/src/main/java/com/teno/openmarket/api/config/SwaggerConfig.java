package com.teno.openmarket.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.print.Pageable;
import java.security.Principal;

@Configuration
public class SwaggerConfig {

    private static final String JWT_SCHEME_NAME = "BearerAuth";

    static {
        SpringDocUtils.getConfig()
            // Pageable 인터페이스를 SwaggerPageable(단순화 클래스)로 대체해서 보여줌
            .replaceWithClass(Pageable.class, SwaggerPageable.class)

            // 컨트롤러 파라미터에 있어도 문서에서는 아예 무시
            .addRequestWrapperToIgnore(
                Principal.class,
                HttpSession.class,
                HttpServletRequest.class,
                HttpServletResponse.class
            );
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpenMarket API")
                        .description("오픈마켓 서비스 API 명세서\n\n" + "본 문서는 코드를 기반으로 자동 생성됩니다.")
                        .version("v0.0.1"))

                // 1. JWT 설정 (Authorize 버튼)
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(JWT_SCHEME_NAME, new SecurityScheme()
                                .name(JWT_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
