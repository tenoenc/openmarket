package com.teno.openmarket.api.architecture;

import com.teno.openmarket.common.response.ApiResponse;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Entity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
    packages = "com.teno.openmarket.api",
    importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class SwaggerArchitectureTest {

    // [규칙 1] API 설명 누락 방지
    @ArchTest
    static final ArchRule controllers_should_have_operation_annotation = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().beAnnotatedWith(Operation.class)
            .because("API 문서화를 위해 모든 컨트롤러 메서드는 @Operation 애노테이션이 필수입니다.");

    // [규칙 2] 응답 통일성 강제
    @ArchTest
    static final ArchRule controllers_should_return_api_response = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().haveRawReturnType(ApiResponse.class)
            .because("API 문서의 통일성을 위해 모든 응답은 ApiResponse 래퍼를 사용해야 합니다.");

    // [규칙 3] 명명 규칙 강제
    @ArchTest
    static final ArchRule controllers_should_be_named_ending_with_controller = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller")
            .because("컨트롤러 클래스는 명확한 식별을 위해 'Controller'로 이름이 끝나야 합니다.");

    // [규칙 4] Map 반환 금지
    @ArchTest
    static final ArchRule controllers_should_not_return_map = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().notHaveRawReturnType(Map.class)
            .because("Map을 반환하면 Swagger에 필드 정보가 나오지 않습니다. 반드시 DTO를 사용하세요.");

    // [규칙 5] Entity 직접 노출 금지
    @ArchTest
    static final ArchRule controllers_should_not_depend_on_entities = noClasses()
            .that().areAnnotatedWith(RestController.class)
            .should().dependOnClassesThat().areAnnotatedWith(Entity.class)
            .because("Controller에서 DB Entity를 직접 사용하거나 반환하면 안 됩니다. 반드시 DTO로 변환하세요.");

}