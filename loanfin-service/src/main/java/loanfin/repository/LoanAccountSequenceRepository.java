package loanfin.repository;

import loanfin.entity.LoanAccountSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAccountSequenceRepository extends JpaRepository<LoanAccountSequenceEntity, Integer> {

    @Modifying
    @Query(
            value= """
                    UPDATE loan_account_sequence
                            SET last_value = last_value + 1
                            WHERE year = :year
                            RETURNING last_value
                    """,
            nativeQuery = true
    )
    Long incrementAndGet(@Param("year") int year);
}
