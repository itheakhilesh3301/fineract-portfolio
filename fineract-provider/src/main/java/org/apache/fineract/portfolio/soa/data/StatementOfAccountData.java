package org.apache.fineract.portfolio.soa.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatementOfAccountData implements Serializable {

    private final Long id;
    private final Long loanId;
    private final Long clientId;
    private final Long transactionId;
    private final LocalDate transactionDate;
    private final String description;
    private final BigDecimal debitAmount;
    private final BigDecimal creditAmount;
    private final BigDecimal principal;
    private final BigDecimal interest;
    private final BigDecimal fees;
    private final BigDecimal penalty;
    private final BigDecimal outstandingBalance;
    private final ZonedDateTime createdOnDate;
}
