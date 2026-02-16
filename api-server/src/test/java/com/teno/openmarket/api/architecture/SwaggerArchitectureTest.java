package com.teno.openmarket.api.architecture;

import com.teno.openmarket.common.response.ApiResponse;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Entity;

import java.util.Map;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
    packages = "com.teno.openmarket",
    importOptions = {}
)
public class SwaggerArchitectureTest {

    private static final String REST_CONTROLLER = "org.springframework.web.bind.annotation.RestController";

    private static final DescribedPredicate<JavaClass> reside_in_test_folder =
            new DescribedPredicate<>("reside in test folder") {
                @Override
                public boolean test(JavaClass item) {
                    return item.getSource().map(source -> {
                        String uri = source.getUri().toString();
                        // 💡 경로에 /test/ 또는 /test-fixtures/가 포함되어 있는지 확인
                        return uri.contains("/test/") || uri.contains("/testFixtures/");
                    }).orElse(false);
                }
            };

    // [규칙 1] API 설명 누락 방지
    @ArchTest
    static final ArchRule controllers_should_have_operation_annotation = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(REST_CONTROLLER)
            .and().areDeclaredInClassesThat(not(reside_in_test_folder))
            .should().beAnnotatedWith(Operation.class)
            .because("API 문서화를 위해 모든 컨트롤러 메서드는 @Operation 애노테이션이 필수입니다.");

    // [규칙 2] 응답 통일성 강제
    @ArchTest
    static final ArchRule controllers_should_return_api_response = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(REST_CONTROLLER)
            .and().areDeclaredInClassesThat(not(reside_in_test_folder))
            .should().haveRawReturnType(ApiResponse.class)
            .because("API 문서의 통일성을 위해 모든 응답은 ApiResponse 래퍼를 사용해야 합니다.");

    // [규칙 3] 명명 규칙 강제
    @ArchTest
    static final ArchRule controllers_should_be_named_ending_with_controller = classes()
            .that().areAnnotatedWith(REST_CONTROLLER)
            .and(not(reside_in_test_folder))
            .should().haveSimpleNameEndingWith("Api")
            .because("컨트롤러 클래스는 명확한 식별을 위해 'Api'로 이름이 끝나야 합니다.");

    // [규칙 4] Map 반환 금지
    @ArchTest
    static final ArchRule controllers_should_not_return_map = methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areAnnotatedWith(REST_CONTROLLER)
            .and().areDeclaredInClassesThat(not(reside_in_test_folder))
            .should().notHaveRawReturnType(Map.class)
            .because("Map을 반환하면 Swagger에 필드 정보가 나오지 않습니다. 반드시 DTO를 사용하세요.");

    // [규칙 5] Entity 직접 노출 금지
    @ArchTest
    static final ArchRule controllers_should_not_depend_on_entities = noClasses()
            .that().areAnnotatedWith(REST_CONTROLLER)
            .and(not(reside_in_test_folder))
            .should().dependOnClassesThat().areAnnotatedWith(Entity.class)
            .because("Controller에서 DB Entity를 직접 사용하거나 반환하면 안 됩니다. 반드시 DTO로 변환하세요.");
}