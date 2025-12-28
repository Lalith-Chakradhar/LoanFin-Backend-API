package loanfin.service.adminServices;

import loanfin.dto.ViewAllLoanApplicationsResponse;
import loanfin.entity.LoanApplicationEntity;
import loanfin.enums.LoanApplicationStatus;
import loanfin.repository.LoanApplicationRepository;
import loanfin.util.NameHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanAppServiceImpl implements LoanAppService{

    private final LoanApplicationRepository loanApplicationRepository;

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
