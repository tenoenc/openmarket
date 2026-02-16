package com.teno.openmarket.user.infra.jpa;

import com.teno.openmarket.user.domain.term.TermAgreement;
import com.teno.openmarket.user.domain.term.TermAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermAgreementRepositoryImpl implements TermAgreementRepository {

    private final TermAgreementJpaRepository jpaRepository;

    @Override
    public List<TermAgreement> saveAll(List<TermAgreement> termAgreements) {
        return jpaRepository.saveAll(termAgreements);
    }
}
