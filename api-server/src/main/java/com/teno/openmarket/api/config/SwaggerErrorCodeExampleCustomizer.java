package com.teno.openmarket.api.config;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.error.ErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCode;
import com.teno.openmarket.core.security.response.ResultType;
import com.teno.openmarket.deal.domain.exception.DealErrorCodeExamples;
import com.teno.openmarket.order.domain.exception.OrderErrorCodeExamples;
import com.teno.openmarket.shop.domain.exception.ShopErrorCodeExamples;
import com.teno.openmarket.system.domain.exception.SystemErrorCodeExamples;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SwaggerErrorCodeExampleCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        List<ErrorCode> allErrorCodes = new ArrayList<>();

        GlobalErrorCodeExamples global = handlerMethod.getMethodAnnotation(GlobalErrorCodeExamples.class);
        if (global != null) {
            allErrorCodes.addAll(Arrays.asList(global.value()));
        }

        UserErrorCodeExamples user = handlerMethod.getMethodAnnotation(UserErrorCodeExamples.class);
        if (user != null) {
            allErrorCodes.addAll(Arrays.asList(user.value()));
        }

        SystemErrorCodeExamples system = handlerMethod.getMethodAnnotation(SystemErrorCodeExamples.class);
        if (system != null) {
            allErrorCodes.addAll(Arrays.asList(system.value()));
        }

        ShopErrorCodeExamples shop = handlerMethod.getMethodAnnotation(ShopErrorCodeExamples.class);
        if (shop != null) {
            allErrorCodes.addAll(Arrays.asList(shop.value()));
        }

        OrderErrorCodeExamples order = handlerMethod.getMethodAnnotation(OrderErrorCodeExamples.class);
        if (order != null) {
            allErrorCodes.addAll(Arrays.asList(order.value()));
        }

        DealErrorCodeExamples deal = handlerMethod.getMethodAnnotation(DealErrorCodeExamples.class);
        if (deal != null) {
            allErrorCodes.addAll(Arrays.asList(deal.value()));
        }

        boolean requiresAuth = AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), SecurityErrorDocs.class)
                || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), SecurityErrorDocs.class);

        if (requiresAuth) {
            allErrorCodes.add(SecurityErrorCode.SECURITY_LOGOUT);
            allErrorCodes.add(SecurityErrorCode.SECURITY_AUTHENTICATION_REQUIRED);
        }

        Map<Integer, List<ErrorCode>> errorCodesByStatus = allErrorCodes.stream()
                .collect(Collectors.groupingBy(code -> code.getStatus().value()));

        if (operation.getResponses() == null) {
            operation.setResponses(new ApiResponses());
        }

        errorCodesByStatus.forEach((status, errorCodes) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();

            errorCodes.forEach(errorCode -> {
                Example example = new Example();
                example.setValue(createErrorResponse(errorCode));
                example.setDescription(errorCode.getMessage());

                mediaType.addExamples(errorCode.name(), example);
            });

            content.addMediaType("application/json", mediaType);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setDescription("Common Error Responses");
            apiResponse.setContent(content);

            operation.getResponses().addApiResponse(String.valueOf(status), apiResponse);
        });

        return operation;
    }

    private Map<String, Object> createErrorResponse(ErrorCode errorCode) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", ResultType.FAIL);
        response.put("errorCode", errorCode.name());
        response.put("message", errorCode.getMessage());
        response.put("data", null);
        return response;
    }
}
