package loanfin.service.borrowerServices;

import loanfin.dto.LoanApplicationRequest;
import loanfin.dto.LoanApplicationResponse;

public interface LoanService {
    LoanApplicationResponse applyLoanService(String borrowerId, LoanApplicationRequest request);
}
