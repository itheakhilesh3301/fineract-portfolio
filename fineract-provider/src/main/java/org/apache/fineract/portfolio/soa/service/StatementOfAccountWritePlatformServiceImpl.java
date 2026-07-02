package org.apache.fineract.portfolio.soa.service;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;
import org.apache.fineract.portfolio.soa.domain.StatementOfAccount;
import org.apache.fineract.portfolio.soa.domain.StatementOfAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementOfAccountWritePlatformServiceImpl implements StatementOfAccountWritePlatformService {

    private final StatementOfAccountRepository statementOfAccountRepository;

    @Override
    @Transactional
    public void handleLoanTransactionEvent(final LoanTransaction transaction) {
        log.info("Processing SOA for loan id: {} transaction id: {}", transaction.getLoan().getId(), transaction.getId());

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;

        if (transaction.isDisbursement()) {
            debitAmount = transaction.getAmount();
        } else if (transaction.isRepayment() || transaction.isWaiver() || transaction.isChargePayment()) {
            creditAmount = transaction.getAmount();
        } else if (transaction.isIncomePosting()) { // Charge applied, accruals, etc.
            debitAmount = transaction.getAmount();
        } else if (transaction.isRefund() || transaction.isChargeback()) {
            debitAmount = transaction.getAmount(); // Refunds typically increase outstanding balance
        } else if (transaction.isAccrualActivity()) {
            // Usually we don't reflect accruals in SOA until they are billed or we might depending on configuration.
            // Assuming we debit them if they are an interest/fee accrual:
            debitAmount = transaction.getAmount();
        }

        // Handle reversal
        if (transaction.isReversed()) {
            // Reversal of a debit is a credit, and vice-versa.
            BigDecimal temp = debitAmount;
            debitAmount = creditAmount;
            creditAmount = temp;
        }
        
        final BigDecimal principal = transaction.getPrincipalPortion() != null ? transaction.getPrincipalPortion() : BigDecimal.ZERO;
        final BigDecimal interest = transaction.getInterestPortion() != null ? transaction.getInterestPortion() : BigDecimal.ZERO;
        final BigDecimal fees = transaction.getFeeChargesPortion() != null ? transaction.getFeeChargesPortion() : BigDecimal.ZERO;
        final BigDecimal penalty = transaction.getPenaltyChargesPortion() != null ? transaction.getPenaltyChargesPortion() : BigDecimal.ZERO;
        final BigDecimal outstandingBalance = transaction.getOutstandingLoanBalance() != null ? transaction.getOutstandingLoanBalance() : transaction.getLoan().getSummary().getTotalOutstanding();

        final String description = transaction.getTypeOf().getCode(); // Or a more mapped description

        StatementOfAccount soa = StatementOfAccount.create(
            transaction.getLoan(),
            transaction.getLoan().getClient(),
            transaction,
            transaction.getTransactionDate(),
            description,
            debitAmount,
            creditAmount,
            principal,
            interest,
            fees,
            penalty,
            outstandingBalance
        );

        this.statementOfAccountRepository.save(soa);
    }
}
