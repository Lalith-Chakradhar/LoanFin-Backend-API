package loanfin.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.Random;

public class UserIdGenerator implements BeforeExecutionGenerator {
    private static final String PREFIX = "LFID";
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    @Override
    public Object generate(
            SharedSessionContractImplementor sharedSessionContractImplementor,
            Object object1,
            Object object2,
            EventType eventType
    ){
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(FORMATTER);
        int random = 1000 + RANDOM.nextInt(9000); // Ensures 4-digit random
        return PREFIX + timestamp + random;
    }

    @Override
    public EnumSet<EventType> getEventTypes() { return EventTypeSets.INSERT_ONLY; }
}
