package loanfin.service;

import jakarta.transaction.Transactional;
import loanfin.dto.LoanApplicationRequest;
import loanfin.dto.LoanApplicationResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.entity.UserEntity;
import loanfin.enums.LoanApplicationStatus;
import loanfin.enums.UserRole;
import loanfin.repository.LoanApplicationRepository;
import loanfin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowerServiceImpl implements BorrowerService{

    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRepository userRepository;

    @Override
    public LoanApplicationResponse applyLoanService(LoanApplicationRequest request){

        //Fetch borrower
        UserEntity borrower = userRepository.findById(request.getBorrowerId()).orElseThrow(()-> new IllegalArgumentException("Borrower not found"));

        //Role validation (CRITICAL)
        if(borrower.getRole() != UserRole.BORROWER)
        {
            throw new IllegalStateException("User is not allowed to apply for loan");
        }

        //Create loan application entity
        LoanApplicationEntity loanApplication = LoanApplicationEntity.builder()
                .borrower(borrower)
                .requestedAmount(request.getRequestedAmount())
                .tenureMonths(request.getTenureMonths())
                .purpose(request.getPurpose())
                .status(LoanApplicationStatus.SUBMITTED)
                .build();

        //Save
        LoanApplicationEntity saved = loanApplicationRepository.save(loanApplication);

        //Return response
        return new LoanApplicationResponse(
                saved.getId(),
                saved.getStatus().name()
        );
    }
}
