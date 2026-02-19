package com.teno.openmarket.user.infra.term;

import com.teno.openmarket.user.domain.term.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermJpaRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByIsRequiredTrue();
}
