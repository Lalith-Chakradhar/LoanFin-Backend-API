package loanfin.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

@Component
@Converter
public class EncryptDecryptConverter implements AttributeConverter<String, String> {

    private static final String ENV_KEY_NAME = "DB_ENCRYPTION_SECRET_KEY";
    private static final String ALGO = "AES/GCM/NoPadding";

    private static final int IV_LENGTH = 12;          // Recommended for GCM
    private static final int TAG_LENGTH = 128;        // 16 bytes authentication tag

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptDecryptConverter() {
        String base64Key = System.getenv(ENV_KEY_NAME);

        if (base64Key == null) {
            throw new RuntimeException("Missing AES key in environment: " + ENV_KEY_NAME);
        }

        byte[] decodedKey = Base64.getDecoder().decode(base64Key);

        if (decodedKey.length != 16) { // 128-bit key
            throw new RuntimeException("AES key must be 128 bits (16 bytes) after Base64 decoding");
        }

        this.secretKey = new SecretKeySpec(decodedKey, "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;

        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGO);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encrypted = cipher.doFinal(attribute.getBytes());

            // Final stored value = Base64(IV + CIPHERTEXT)
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        }
        catch (Exception e) {
            throw new RuntimeException("Error encrypting value", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        try {
            byte[] decoded = Base64.getDecoder().decode(dbData);

            // Extract IV and ciphertext
            byte[] iv = new byte[IV_LENGTH];
            byte[] ciphertext = new byte[decoded.length - IV_LENGTH];

            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
            System.arraycopy(decoded, IV_LENGTH, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance(ALGO);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            return new String(cipher.doFinal(ciphertext));
        }
        catch (Exception e) {
            throw new RuntimeException("Error decrypting value", e);
        }
    }
}
