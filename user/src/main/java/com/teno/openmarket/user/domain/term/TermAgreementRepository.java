package com.teno.openmarket.user.domain.term;

import java.util.List;

public interface TermAgreementRepository {

    List<TermAgreement> saveAll(List<TermAgreement> termAgreements);
}
