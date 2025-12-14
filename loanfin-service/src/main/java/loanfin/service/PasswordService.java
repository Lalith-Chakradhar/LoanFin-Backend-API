package loanfin.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    // Hash the password before saving
    public String hashPassword(String rawPassword)
    {
        //OWASP recommended parameters:
        int iterations = 3;
        int memory = 65536; //64MB
        int parallelism = 1;

        return argon2.hash(iterations, memory, parallelism, rawPassword);
    }

    //Verify the password on login
    public boolean verify(String hash, String rawPassword)
    {
        return argon2.verify(hash, rawPassword);
    }
}
