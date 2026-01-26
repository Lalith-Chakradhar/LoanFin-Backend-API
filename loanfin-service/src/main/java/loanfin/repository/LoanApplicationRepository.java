package loanfin.repository;

import loanfin.entity.LoanApplicationEntity;
import loanfin.entity.UserEntity;
import loanfin.enums.LoanApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.time.Instant;

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

    @Modifying
    @Query("""
UPDATE LoanApplicationEntity l
SET l.reviewerLeaseOwner = :admin,
    l.reviewerLeaseAcquiredAt = :now,
    l.reviewerLeaseExpiresAt = :expiry
WHERE l.id = :loanId
AND (
    l.reviewerLeaseOwner IS NULL
    OR l.reviewerLeaseExpiresAt < :now
)
""")
    int acquireLease(
            @Param("loanId") String loanId,
            @Param("admin") UserEntity admin,
            @Param("now") Instant now,
            @Param("expiry") Instant expiry
    );

}
