package loanfin.entity;

import jakarta.persistence.*;
import loanfin.enums.LoanStatus;

import java.time.Instant;

@Entity
@Table(name="loans")
public class LoanEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // internal PK

    @Column(name="loan_account_number", unique=true, nullable=false)
    private String loanAccountNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity borrower;

    @OneToOne(optional = false)
    private LoanApplicationEntity loanApplication;

    @Column(name = "principal_amount", nullable = false)
    private Double principalAmount;

    @Column(name = "interest_rate", nullable = false)
    private Double interestRate;

    @Column(name="tenure_months", nullable = false)
    private Integer tenureMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity approvedBy;

    @Column(name = "approved_at", nullable = false)
    private Instant approvedAt;

    @Column(name = "disbursed_at")
    private Instant disbursedAt;

}
