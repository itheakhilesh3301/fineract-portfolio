package org.apache.fineract.portfolio.soa.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.portfolio.soa.data.StatementOfAccountData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatementOfAccountReadPlatformServiceImpl implements StatementOfAccountReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final SoaMapper soaMapper = new SoaMapper();

    @Override
    public Collection<StatementOfAccountData> retrieveAllByLoanId(final Long loanId) {
        final String sql = "select " + this.soaMapper.schema() + " where s.loan_id = ? order by s.transaction_date ASC, s.id ASC";
        return this.jdbcTemplate.query(sql, this.soaMapper, new Object[] { loanId }); // NOSONAR
    }

    private static final class SoaMapper implements RowMapper<StatementOfAccountData> {

        private final String schema;

        SoaMapper() {
            final StringBuilder sqlBuilder = new StringBuilder(400);
            sqlBuilder.append("s.id as id, s.loan_id as loanId, s.client_id as clientId, s.transaction_id as transactionId, ");
            sqlBuilder.append("s.transaction_date as transactionDate, s.description as description, s.debit_amount as debitAmount, ");
            sqlBuilder.append("s.credit_amount as creditAmount, s.principal as principal, s.interest as interest, ");
            sqlBuilder.append("s.fees as fees, s.penalty as penalty, s.outstanding_balance as outstandingBalance, ");
            sqlBuilder.append("s.created_on_utc as createdOnDate ");
            sqlBuilder.append("from m_statement_of_account s");
            this.schema = sqlBuilder.toString();
        }

        public String schema() {
            return this.schema;
        }

        @Override
        public StatementOfAccountData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            return StatementOfAccountData.builder()
                .id(rs.getLong("id"))
                .loanId(rs.getLong("loanId"))
                .clientId(rs.getLong("clientId"))
                .transactionId(JdbcSupport.getLong(rs, "transactionId"))
                .transactionDate(JdbcSupport.getLocalDate(rs, "transactionDate"))
                .description(rs.getString("description"))
                .debitAmount(rs.getBigDecimal("debitAmount"))
                .creditAmount(rs.getBigDecimal("creditAmount"))
                .principal(rs.getBigDecimal("principal"))
                .interest(rs.getBigDecimal("interest"))
                .fees(rs.getBigDecimal("fees"))
                .penalty(rs.getBigDecimal("penalty"))
                .outstandingBalance(rs.getBigDecimal("outstandingBalance"))
                .createdOnDate(JdbcSupport.getDateTime(rs, "createdOnDate"))
                .build();
        }
    }
}
