package loanfin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import loanfin.enums.LoanAccountStatus;
import loanfin.enums.LoanApplicationStatus;
import loanfin.enums.ReviewDecision;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LoanApplicationReviewResponse {
    private String applicationId;
    private LoanApplicationStatus status;

    private ReviewMetadata review;
    private LoanAccountSummary loanAccount;  // null if rejected

    private String message;

    // -------- Nested DTOs --------

    @Data
    @Builder
    public static class ReviewMetadata {
        private ReviewDecision decision;
        private String reviewedBy;
        private Instant reviewedAt;
        private String remarks;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL) // for reject cases
    public static class LoanAccountSummary {
        private String loanAccountId;
        private Double principalAmount;
        private Integer tenureMonths;
        private Double interestRate;
        private LoanAccountStatus state;
    }
}
