package org.apache.fineract.portfolio.soa.service;

import java.util.Collection;
import org.apache.fineract.portfolio.soa.data.StatementOfAccountData;

public interface StatementOfAccountReadPlatformService {
    Collection<StatementOfAccountData> retrieveAllByLoanId(Long loanId);
}
