package com.teno.openmarket.user.domain.repository;

import com.teno.openmarket.test.support.BaseRepositoryTest;
import com.teno.openmarket.user.domain.entity.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TermRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private TermRepository termRepository;

    @Test
    @DisplayName("필수 약관 목록만 정확히 조회되어야 한다")
    void should_RetrieveRequiredTerms_When_Finding() {
        // given
        Term mandatoryTerm1 = Term.builder()
                .title("서비스 이용약관")
                .content("필수 내용...")
                .version("v1.0")
                .isRequired(true) // 필수 O
                .build();

        Term mandatoryTerm2 = Term.builder()
                .title("개인정보 처리방침")
                .content("필수 내용...")
                .version("v1.0")
                .isRequired(true) // 필수 O
                .build();

        Term optionalTerm = Term.builder()
                .title("마케팅 수신 동의")
                .content("선택 내용...")
                .version("v1.0")
                .isRequired(false) // 필수 X
                .build();

        termRepository.saveAll(List.of(mandatoryTerm1, mandatoryTerm2, optionalTerm));

        // when
        List<Term> result = termRepository.findAllByIsRequiredTrue();

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("title")
                .containsExactlyInAnyOrder("서비스 이용약관", "개인정보 처리방침");

        assertThat(result)
                .extracting("isRequired")
                .containsOnly(true);
    }
}