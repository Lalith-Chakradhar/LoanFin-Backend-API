package loanfin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoanApplicationRequest {
    @NonNull
    private String borrowerId;

    @Min(1)
    private Double requestedAmount;

    @Min(1)
    private Integer tenureMonths;

    @NotBlank
    private String purpose;
}
