package org.apache.fineract.portfolio.soa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.fineract.infrastructure.core.domain.AbstractAuditableWithUTCDateTimeCustom;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransaction;

@Entity
@Table(name = "m_statement_of_account")
public class StatementOfAccount extends AbstractAuditableWithUTCDateTimeCustom<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private LoanTransaction transaction;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "debit_amount", scale = 6, precision = 19)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", scale = 6, precision = 19)
    private BigDecimal creditAmount;

    @Column(name = "principal", scale = 6, precision = 19)
    private BigDecimal principal;

    @Column(name = "interest", scale = 6, precision = 19)
    private BigDecimal interest;

    @Column(name = "fees", scale = 6, precision = 19)
    private BigDecimal fees;

    @Column(name = "penalty", scale = 6, precision = 19)
    private BigDecimal penalty;

    @Column(name = "outstanding_balance", scale = 6, precision = 19)
    private BigDecimal outstandingBalance;

    protected StatementOfAccount() {
        //
    }

    public StatementOfAccount(final Loan loan, final Client client, final LoanTransaction transaction, final LocalDate transactionDate,
            final String description, final BigDecimal debitAmount, final BigDecimal creditAmount, final BigDecimal principal,
            final BigDecimal interest, final BigDecimal fees, final BigDecimal penalty, final BigDecimal outstandingBalance) {
        this.loan = loan;
        this.client = client;
        this.transaction = transaction;
        this.transactionDate = transactionDate;
        this.description = description;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.principal = principal;
        this.interest = interest;
        this.fees = fees;
        this.penalty = penalty;
        this.outstandingBalance = outstandingBalance;
    }

    public static StatementOfAccount create(final Loan loan, final Client client, final LoanTransaction transaction,
            final LocalDate transactionDate, final String description, final BigDecimal debitAmount, final BigDecimal creditAmount,
            final BigDecimal principal, final BigDecimal interest, final BigDecimal fees, final BigDecimal penalty,
            final BigDecimal outstandingBalance) {
        return new StatementOfAccount(loan, client, transaction, transactionDate, description, debitAmount, creditAmount, principal,
                interest, fees, penalty, outstandingBalance);
    }
}
