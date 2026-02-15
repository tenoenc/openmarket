package com.teno.openmarket.common.config;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ResultType;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SwaggerErrorCodeExampleCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiErrorCodeExamples annotation = handlerMethod.getMethodAnnotation(ApiErrorCodeExamples.class);

        if (annotation == null) {
            return operation;
        }

        Map<Integer, List<GlobalErrorCode>> errorCodesByStatus = Arrays.stream(annotation.value())
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

    private Map<String, Object> createErrorResponse(GlobalErrorCode errorCode) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", ResultType.FAIL);
        response.put("errorCode", errorCode.name());
        response.put("message", errorCode.getMessage());
        response.put("data", null);
        return response;
    }
}
