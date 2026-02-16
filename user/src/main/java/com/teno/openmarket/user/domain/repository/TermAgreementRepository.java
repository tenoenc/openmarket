package com.teno.openmarket.user.domain.repository;

import com.teno.openmarket.user.domain.entity.TermAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermAgreementRepository extends JpaRepository<TermAgreement, Long> {

}
