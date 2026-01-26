package loanfin.entity;

import jakarta.persistence.*;
import loanfin.enums.LoanApplicationStatus;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="loan_applications",
        indexes = {
                @Index(name = "idx_loan_borrower_id", columnList = "borrower_id"),
                @Index(name = "idx_loan_status", columnList = "status"),
                @Index(name = "idx_loan_under_review_at", columnList = "under_review_at")
        }
)
public class LoanApplicationEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "borrower_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "loan_applications_borrower_id_fkey")
    )
    private UserEntity borrower;

    @Column(name = "requested_amount", nullable = false)
    private Double requestedAmount;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus status;

    // Admin who reviewed (checker)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reviewed_by",
            foreignKey = @ForeignKey(name = "loan_applications_reviewed_by_fkey")
    )
    private UserEntity reviewedBy;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @Column(name = "under_review_at")
    private Instant underReviewAt;

    @Column(name = "last_status_updated_at", nullable = false)
    private Instant lastStatusUpdatedAt;

    //admin leasing for avoiding review conflict
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity reviewerLeaseOwner;

    @Column(name = "reviewer_lease_acquired_at")
    private Instant reviewerLeaseAcquiredAt;

    @Column(name = "reviewer_lease_expires_at")
    private Instant reviewerLeaseExpiresAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.submittedAt = now;
        this.lastStatusUpdatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastStatusUpdatedAt = Instant.now();
    }

}
