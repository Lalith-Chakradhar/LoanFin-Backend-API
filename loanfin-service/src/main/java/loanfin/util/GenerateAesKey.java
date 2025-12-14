package loanfin.util;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateAesKey {
    public static void main(String[] args) {
        byte[] key = new byte[16]; // 128-bit key
        new SecureRandom().nextBytes(key);
        System.out.println("DB_ENCRYPTION_SECRET_KEY=" + Base64.getEncoder().encodeToString(key));
    }
}
