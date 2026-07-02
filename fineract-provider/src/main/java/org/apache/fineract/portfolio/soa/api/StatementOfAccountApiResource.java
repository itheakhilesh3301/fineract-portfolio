package org.apache.fineract.portfolio.soa.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.portfolio.soa.data.StatementOfAccountData;
import org.apache.fineract.portfolio.soa.service.StatementOfAccountReadPlatformService;
import org.springframework.stereotype.Component;

@Path("/v1/loans/{loanId}/statement-of-account")
@Component
@Tag(name = "Statement of Account", description = "The Statement of Account API")
@RequiredArgsConstructor
public class StatementOfAccountApiResource {

    private final StatementOfAccountReadPlatformService statementOfAccountReadPlatformService;

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Operation(summary = "Retrieve Statement of Account for a loan", description = "Retrieve SOA")
    public Collection<StatementOfAccountData> retrieveAll(
            @PathParam("loanId") @Parameter(description = "loanId") final Long loanId) {
        return this.statementOfAccountReadPlatformService.retrieveAllByLoanId(loanId);
    }
}
