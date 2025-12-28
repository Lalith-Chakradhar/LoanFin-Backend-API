package loanfin.repository;

import loanfin.entity.LoanApplicationEntity;
import loanfin.enums.LoanApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity,String> {

    @Query("""
            SELECT la FROM LoanApplicationEntity la
            JOIN la.borrower b
            WHERE (:status IS NULL OR la.status = :status)
            AND (:nameHash IS NULL OR b.nameHash = :nameHash)
            ORDER BY la.createdAt DESC
            """)
    Page<LoanApplicationEntity> findByFilters(
            @Param("status") LoanApplicationStatus status,
            @Param("nameHash") String nameHash,
            Pageable pageable
            );
}
