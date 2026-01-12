package loanfin.controller.adminAPIs;

import loanfin.constant.ApiStatus;
import loanfin.dto.IResponse;
import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.entity.UserEntity;
import loanfin.enums.LoanApplicationStatus;
import loanfin.exception.IException;
import loanfin.service.adminServices.LoanAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoanAppControllerImpl implements LoanAppController{

    //APIs -
    // 1. get - / - view all loan applications - minimal loan details pertaining to filters (DONE)
    // 2. get - /{loanappid} - enter into loan details and here status will be set from submitted to under_review.
    // But this admin may not use system for long time and loan application can get stuck. Should implement timeout
    // based under_review status.
    //3. put - /{loanappid}/review - change loan application status to approved or rejected. If approved, it becomes
    // a loan from loan application. If being rejected, the rejection reason must be shown.

    private final LoanAppService loanAppService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/loan-applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IResponse<List<ViewAllLoanApplicationsResponse>>> viewAllLoanApplications(
            Authentication authentication,
            LoanApplicationStatus status,
            String borrowerName,
            Pageable pageable
    ) throws IException
    {
        List<ViewAllLoanApplicationsResponse> loanApplications = loanAppService.viewLoanApplications(status,borrowerName,pageable);

        return ResponseEntity.ok(IResponse.<List<ViewAllLoanApplicationsResponse>>builder()
                .data(loanApplications)
                .status(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.name())
                .message(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/loan-application/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IResponse<LoanApplicationEntity>> viewUserLoanApplication(
            @PathVariable String id,
            Authentication authentication
    ) throws IException
    {
        UserEntity admin = (UserEntity) authentication.getPrincipal();

        LoanApplicationEntity loan =
                loanAppService.viewIndividualLoanApplication(id, admin);

        return ResponseEntity.ok(
                IResponse.<LoanApplicationEntity>builder()
                        .data(loan)
                        .status(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.name())
                        .message(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
