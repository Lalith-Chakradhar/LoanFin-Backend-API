package loanfin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import loanfin.dto.IResponse;
import loanfin.dto.LoanApplicationRequest;
import loanfin.dto.LoanApplicationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Loan APIs", description = "APIs for applying, fetching all loans and particular loan of a borrower")
public interface BorrowerController {

    @Operation(
            summary = "Apply for a loan",
            description = "Borrower submits a loan application which will be reviewed by an admin (checker)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Loan application submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Borrower not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<IResponse<LoanApplicationResponse>> applyLoan(
            @Valid @RequestBody LoanApplicationRequest request
            );
}
