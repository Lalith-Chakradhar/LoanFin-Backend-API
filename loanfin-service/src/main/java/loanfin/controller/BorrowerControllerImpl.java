package loanfin.controller;

import jakarta.validation.Valid;
import loanfin.constant.ApiStatus;
import loanfin.dto.IResponse;
import loanfin.dto.LoanApplicationRequest;
import loanfin.dto.LoanApplicationResponse;
import loanfin.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BorrowerControllerImpl implements BorrowerController {

    BorrowerService borrowerService;

    @Override
    @PostMapping(value = "/borrowers/apply-loan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IResponse<LoanApplicationResponse>> applyLoan(
            @Valid @RequestBody LoanApplicationRequest request
    ){
        LoanApplicationResponse loanApplicationResponse = borrowerService.applyLoanService(request);

        return ResponseEntity.ok(IResponse.<LoanApplicationResponse>builder()
                .data(loanApplicationResponse)
                .status(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.name())
                .message(ApiStatus.LOAN_APPLICATION_SUCCESSFUL.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
