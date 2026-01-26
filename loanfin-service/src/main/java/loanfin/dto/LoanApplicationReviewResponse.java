package loanfin.dto;

import loanfin.enums.LoanApplicationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoanApplicationReviewResponse {
    private String applicationId;
    private LoanApplicationStatus status;

    private ReviewMetadata review;
    private LoanAccountSummary loanAccount;  // null if rejected

    private String message;

}
