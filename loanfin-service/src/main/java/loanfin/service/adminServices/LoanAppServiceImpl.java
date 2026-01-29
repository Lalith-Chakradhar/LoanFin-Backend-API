package loanfin.service.adminServices;

import loanfin.dto.LoanApplicationReviewRequest;
import loanfin.dto.LoanApplicationReviewResponse;
import loanfin.dto.ReviewMetadata;
import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.entity.UserEntity;
import loanfin.enums.LoanApplicationStatus;
import loanfin.enums.ReviewDecision;
import loanfin.exception.IException;
import loanfin.mapper.LoanAccountMapper;
import loanfin.repository.LoanApplicationRepository;
import loanfin.service.LoanAccountNumberGenerator;
import loanfin.util.NameHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanAppServiceImpl implements LoanAppService{

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanAccountMapper loanAccountMapper;
    private final LoanRepository loanRepository;
    private final LoanAccountNumberGenerator loanAccountNumberGenerator;

    @Override
    public List<ViewAllLoanApplicationsResponse> viewLoanApplications(LoanApplicationStatus status, String borrowerName, Pageable pageable)
    {

        String nameHash = null;

        if (borrowerName != null && !borrowerName.isBlank()) {
            nameHash = NameHasher.hash(borrowerName);
        }

        Page<LoanApplicationEntity> page =
                loanApplicationRepository.findByFilters(status, nameHash, pageable);

        return page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LoanApplicationEntity startLoanApplicationReview(String loanId, UserEntity admin)
    {
        Instant now = Instant.now();
        Instant expiry = now.plus(30, ChronoUnit.MINUTES);

        int locked = loanApplicationRepository.acquireLease(loanId, admin, now, expiry);

        if(locked == 0)
        {
            throw new IException("LOAN_ALREADY_UNDER_REVIEW");
        }

        LoanApplicationEntity loan = loanApplicationRepository.findById(loanId).orElseThrow();

        loan.markUnderReview(admin, now, expiry);

        return loan;
    }

    @Override
    @Transactional
    public LoanApplicationReviewResponse approveOrRejectLoanApplication(
            String loanId,
            LoanApplicationReviewRequest request,
            UserEntity admin)
    {
        //Get loan application details of particular loan application id
        LoanApplicationEntity loan = loanApplicationRepository
                .findById(loanId)
                .orElseThrow(() -> new IException("LOAN_APPLICATION_NOT_FOUND"));

        //Validate application is under review
        if(loan.getStatus() != LoanApplicationStatus.UNDER_REVIEW)
        {
            throw new IException("LOAN_APPLICATION_NOT_UNDER_REVIEW");
        }

        //Validate reviewer lease ownership
        if(!admin.equals(loan.getReviewerLeaseOwner())){
            throw new IException("REVIEWER_LEASE_NOT_OWNED_BY_ADMIN");
        }

        //Approve or Reject using DOMAIN METHODS
        if(request.getDecision() == ReviewDecision.APPROVE){
            loan.approve(admin, request.getRemarks());
        } else if (request.getDecision() == ReviewDecision.REJECT)
        {
            loan.reject(admin, request.getRemarks());
        }
        else
        {
            throw new IException("INVALID_REVIEW_DECISION");
        }

        LoanApplicationReviewResponse response = new LoanApplicationReviewResponse();
        response.setApplicationId(loan.getId());
        response.setStatus(loan.getStatus());
        response.setMessage(
                loan.getStatus() == LoanApplicationStatus.APPROVED
                ? "Loan application approved successfully"
                        :"Loan application rejected"
        );

        ReviewMetadata reviewMetadata = new ReviewMetadata();
        reviewMetadata.setDecision(request.getDecision());
        reviewMetadata.setReviewedBy(admin.getId());
        reviewMetadata.setReviewedAt(loan.getLastStatusUpdatedAt());
        reviewMetadata.setRemarks(request.getRemarks());

        response.setReview(reviewMetadata);

        //Return loan account on approval
        if(loan.getStatus() == LoanApplicationStatus.APPROVED)
        {
            response.setLoanAccount(
                    loanAccountMapper.mapToLoanAccountSummaryDto(loan.getLoanAccount())
            );
        }

        return response;
    }

    private ViewAllLoanApplicationsResponse mapToResponse(LoanApplicationEntity entity)
    {
        ViewAllLoanApplicationsResponse response = new ViewAllLoanApplicationsResponse();
        response.setLoanApplicationId(entity.getId());
        response.setBorrowerId(entity.getBorrower().getId());
        response.setBorrowerName(entity.getBorrower().getUserName());
        response.setRequestedAmount(entity.getRequestedAmount());
        response.setTenureMonths(entity.getTenureMonths());
        response.setStatus(entity.getStatus());
        return response;
    }
}
