package loanfin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String tokenSecret;

    @Value("${jwt.expiration}")
    private long expiration;
}
