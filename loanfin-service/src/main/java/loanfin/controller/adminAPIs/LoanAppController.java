package loanfin.controller.adminAPIs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import loanfin.dto.IResponse;
import loanfin.dto.LoanApplicationReviewRequest;
import loanfin.dto.LoanApplicationReviewResponse;
import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.enums.LoanApplicationStatus;
import loanfin.exception.IException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Tag(name = "Loan Application APIs", description = "APIs for viewing all loan applications, approving and rejecting them.")
public interface LoanAppController {

    @Operation(
            summary = "View all loan applications",
            description = "Table of all loan applications. Additional filters can be applied. Submitted will be default filter"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan applications fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<IResponse<List<ViewAllLoanApplicationsResponse>>> viewAllLoanApplications(
            Authentication authentication,
            @RequestParam(required = false) LoanApplicationStatus status,
            @RequestParam(required = false) String borrowerName,
            Pageable pageable
    ) throws IException;

    @Operation(
            summary = "View individual loan applications",
            description = "Details of an individual loan application. Submitted state goes to under-review."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan application details fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<IResponse<LoanApplicationEntity>> viewUserLoanApplication(
            @PathVariable String id,
            Authentication authentication
    ) throws IException;


    @Operation(
            summary = "Mark individual loan application - approved or rejected",
            description = "Approve or reject an individual loan application"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan application approved or rejected successfully"),
            @ApiResponse(responseCode = "409", description = "Loan application is already APPROVED and cannot be reviewed again"),
            @ApiResponse(responseCode = "403", description = "Only admins are allowed to review loan applications"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<IResponse<LoanApplicationReviewResponse>> reviewUserLoanApplication(
            @PathVariable String id,
            LoanApplicationReviewRequest request,
            Authentication authentication
    ) throws IException;
}
