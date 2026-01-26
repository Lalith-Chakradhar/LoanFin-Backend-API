package loanfin.dto;

import loanfin.enums.ReviewDecision;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ReviewMetadata {
    private ReviewDecision decision;
    private String reviewedBy;
    private Instant reviewedAt;
    private String remarks;
}
