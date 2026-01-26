package loanfin.service.adminServices;

import loanfin.dto.LoanApplicationReviewRequest;
import loanfin.dto.LoanApplicationReviewResponse;
import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.entity.UserEntity;
import loanfin.enums.LoanApplicationStatus;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LoanAppService {
    public List<ViewAllLoanApplicationsResponse> viewLoanApplications(LoanApplicationStatus status, String borrowerName, Pageable pageable);
    public LoanApplicationEntity startLoanApplicationReview(String loanId, UserEntity admin);
    public LoanApplicationReviewResponse approveOrRejectLoanApplication(String loanId, LoanApplicationReviewRequest request, UserEntity admin);
}
