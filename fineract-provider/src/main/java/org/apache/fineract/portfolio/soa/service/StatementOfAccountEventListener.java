package org.apache.fineract.portfolio.soa.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fineract.infrastructure.event.business.BusinessEventListener;
import org.apache.fineract.infrastructure.event.business.domain.BusinessEvent;
import org.apache.fineract.infrastructure.event.business.domain.loan.transaction.*;
import org.apache.fineract.infrastructure.event.business.service.BusinessEventNotifierService;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementOfAccountEventListener {

    private final BusinessEventNotifierService businessEventNotifierService;
    private final StatementOfAccountWritePlatformService statementOfAccountWritePlatformService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostConstruct
    public void addListeners() {
        BusinessEventListener listener = new LoanTransactionListener();

        businessEventNotifierService.addPostBusinessEventListener(LoanDisbursalTransactionBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionMakeRepaymentPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanChargePaymentPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionInterestPaymentWaiverPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanRefundPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionMerchantIssuedRefundPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionPayoutRefundPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionRecoveryPaymentPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionGoodwillCreditPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanForeClosurePostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanCreditBalanceRefundPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanChargeOffPostBusinessEvent.class, listener);
        businessEventNotifierService.addPostBusinessEventListener(LoanTransactionAccrualActivityPostBusinessEvent.class, listener);
    }

    private final class LoanTransactionListener implements BusinessEventListener<BusinessEvent<LoanTransaction>> {
        @Override
        public void onBusinessEvent(BusinessEvent<LoanTransaction> event) {
            LoanTransaction transaction = event.get();
            log.debug("Received SOA transaction event {} for loan id {} transaction id {}", event.getType(), transaction.getLoan().getId(), transaction.getId());
            statementOfAccountWritePlatformService.handleLoanTransactionEvent(transaction);
        }
    }
}
