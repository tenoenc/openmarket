package com.teno.openmarket.user.domain.repository;

import com.teno.openmarket.user.domain.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByIsRequiredTrue();
}
