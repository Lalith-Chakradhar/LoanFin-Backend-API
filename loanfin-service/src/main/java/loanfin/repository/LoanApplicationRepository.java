package loanfin.repository;

import loanfin.entity.LoanApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity,String> {

}
