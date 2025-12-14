package loanfin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanApplicationResponse {
    private String loanApplicationId;
    private String status;
}
