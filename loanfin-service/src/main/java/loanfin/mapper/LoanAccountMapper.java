package loanfin.mapper;

import loanfin.dto.LoanAccountSummary;
import loanfin.entity.LoanApplicationEntity;
import org.mapstruct.Mapper;

@Mapper
public interface LoanAccountMapper {
    LoanAccountSummary loanAppEntityToLoanAccountSummary(LoanApplicationEntity loanApplicationEntity);
}
