package com.teno.openmarket.user.domain.term;

import java.util.List;

public interface TermRepository {

    List<Term> saveAll(List<Term> terms);

    List<Term> findAllByIsRequiredTrue();
}
