package loanfin.service.adminServices;

import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.enums.LoanApplicationStatus;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LoanAppService {
    public List<ViewAllLoanApplicationsResponse> viewLoanApplications(LoanApplicationStatus status, String borrowerName, Pageable pageable);
}
