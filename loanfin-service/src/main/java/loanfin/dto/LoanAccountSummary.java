package loanfin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import loanfin.enums.LoanAccountStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL) // for reject cases
public class LoanAccountSummary {
    private String loanAccountId;
    private Double principalAmount;
    private Integer tenureMonths;
    private Double interestRate;
    private LoanAccountStatus state;
}
