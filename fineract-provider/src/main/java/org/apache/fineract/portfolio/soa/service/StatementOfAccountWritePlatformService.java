package org.apache.fineract.portfolio.soa.service;

import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;

public interface StatementOfAccountWritePlatformService {
    void handleLoanTransactionEvent(LoanTransaction transaction);
}
