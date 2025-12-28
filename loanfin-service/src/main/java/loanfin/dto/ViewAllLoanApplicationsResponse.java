package loanfin.dto;
import loanfin.enums.LoanApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewAllLoanApplicationsResponse {

    private String loanApplicationId;

    private String borrowerId;
    private String borrowerName;

    private Double requestedAmount;
    private Integer tenureMonths;
    private LoanApplicationStatus status;
}
