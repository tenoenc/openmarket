package com.teno.openmarket.user.infra.jpa;

import com.teno.openmarket.user.domain.term.Term;
import com.teno.openmarket.user.domain.term.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermRepositoryImpl implements TermRepository {

    private final TermJpaRepository jpaRepository;

    @Override
    public List<Term> saveAll(List<Term> terms) {
        return jpaRepository.saveAll(terms);
    }

    @Override
    public List<Term> findAllByIsRequiredTrue() {
        return jpaRepository.findAllByIsRequiredTrue();
    }
}
