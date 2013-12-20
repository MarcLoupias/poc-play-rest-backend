package org.myweb.utils;

import org.jetbrains.annotations.NotNull;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Storing password NIST recommendations : http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
 *
 * Original code from stackoverflow : http://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
 *
 */
public class SecurityUtils {

    public static final String PBKDF2WithHmacSHA1 = "PBKDF2WithHmacSHA1";

    @NotNull
    public static byte[] generateSalt(@NotNull Integer byteLength) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[byteLength];
        random.nextBytes(bytes);

        return bytes;
    }

    public static byte[] hash(
            char[] password, byte[] salt, int iterations, int bitKeyLength, @NotNull String pbeAlgo
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {

        char[] pwd = cloneArrayAndEraseOriginal(password);
        KeySpec spec = new PBEKeySpec(pwd, salt, iterations, bitKeyLength);

        SecretKeyFactory f = SecretKeyFactory.getInstance(pbeAlgo);

        return f.generateSecret(spec).getEncoded();
    }

    public static boolean isExpectedPassword(
            char[] password, byte[] salt, byte[] expectedHash, int iterations, int bitKeyLength, @NotNull String pbeAlgo
    ) throws InvalidKeySpecException, NoSuchAlgorithmException {

        char[] pwd = cloneArrayAndEraseOriginal(password);

        byte[] pwdHash = hash(pwd, salt, iterations, bitKeyLength, pbeAlgo);

        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }

    private static char[] cloneArrayAndEraseOriginal(char[] password) {
        char[] pwd = password.clone();
        Arrays.fill(password, Character.MIN_VALUE);
        return pwd;
    }
}
