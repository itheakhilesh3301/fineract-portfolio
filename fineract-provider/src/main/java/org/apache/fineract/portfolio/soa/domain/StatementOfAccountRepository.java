package org.apache.fineract.portfolio.soa.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatementOfAccountRepository extends JpaRepository<StatementOfAccount, Long>, JpaSpecificationExecutor<StatementOfAccount> {

}
