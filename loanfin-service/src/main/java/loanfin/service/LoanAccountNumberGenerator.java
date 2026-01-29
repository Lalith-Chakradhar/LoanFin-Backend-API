package loanfin.service;

import jakarta.transaction.Transactional;
import loanfin.entity.LoanAccountSequenceEntity;
import loanfin.repository.LoanAccountSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class LoanAccountNumberGenerator {

    private final LoanAccountSequenceRepository sequenceRepository;

    @Transactional
    public String generate() {
        int year = Year.now().getValue();

        Long nextValue = sequenceRepository.incrementAndGet(year);

        // If no row exists yet for this year
        if (nextValue == null) {
            sequenceRepository.save(
                    new LoanAccountSequenceEntity(year, 0L)
            );
            nextValue = sequenceRepository.incrementAndGet(year);
        }

        return String.format("LN-%d-%06d", year, nextValue);
    }
}

