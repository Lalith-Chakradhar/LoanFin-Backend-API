package loanfin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import loanfin.enums.ReviewDecision;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanApplicationReviewRequest {

    @NotNull(message = "Decision is required")
    private ReviewDecision decision;

    @NotBlank(message = "Remarks are mandatory for review")
    private String remarks;
}
