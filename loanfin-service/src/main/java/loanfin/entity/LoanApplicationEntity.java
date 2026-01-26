package loanfin.entity;

import jakarta.persistence.*;
import loanfin.enums.LoanApplicationStatus;
import lombok.*;

import java.time.Instant;

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

    @Column(name = "review_remarks", length = 500)
    private String reviewRemarks;

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

    public void markUnderReview(UserEntity admin, Instant now, Instant expiry) {
        validateSubmittedState();
        this.status = LoanApplicationStatus.UNDER_REVIEW;
        this.underReviewAt = now;
        this.reviewerLeaseOwner = admin;
        this.reviewerLeaseAcquiredAt = now;
        this.reviewerLeaseExpiresAt = expiry;
    }

    public void approve(UserEntity admin, String remarks) {
        validateUnderReviewState(admin);
        this.status = LoanApplicationStatus.APPROVED;
        this.reviewedBy = admin;
        this.reviewRemarks = remarks;
        this.reviewerLeaseOwner = null;
        this.reviewerLeaseExpiresAt = null;
    }

    public void reject(UserEntity admin, String remarks) {
        validateUnderReviewState(admin);
        this.status = LoanApplicationStatus.REJECTED;
        this.reviewedBy = admin;
        this.reviewRemarks = remarks;
        this.reviewerLeaseOwner = null;
        this.reviewerLeaseExpiresAt = null;
    }

    private void validateSubmittedState() {
        if (this.status != LoanApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("Application is not in SUBMITTED state");
        }
    }

    private void validateUnderReviewState(UserEntity admin) {
        if (this.status != LoanApplicationStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Application is not under review");
        }
        if (!admin.equals(this.reviewerLeaseOwner)) {
            throw new IllegalStateException("Reviewer lease not owned by this admin");
        }
    }


}
