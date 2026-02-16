package com.teno.openmarket.user.infra.jpa;

import com.teno.openmarket.user.domain.term.TermAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermAgreementJpaRepository extends JpaRepository<TermAgreement, Long> {

}
