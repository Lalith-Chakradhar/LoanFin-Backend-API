package loanfin.service;

import loanfin.dto.LoanApplicationRequest;
import loanfin.dto.LoanApplicationResponse;

public interface BorrowerService {
    LoanApplicationResponse applyLoanService(LoanApplicationRequest request);
}
