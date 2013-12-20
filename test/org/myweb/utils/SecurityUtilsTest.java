package org.myweb.utils;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SecurityUtilsTest {

    @Test
    public void hashPasswordTest() {

        String pwd = "AbR46korRps5-@";

        byte[] salt = SecurityUtils.generateSalt(16);
        byte[] hash = new byte[0];

        try {
            hash = SecurityUtils.hash(pwd.toCharArray(), salt, 10000, 256, SecurityUtils.PBKDF2WithHmacSHA1);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assert.fail(ExceptionUtils.throwableToString(e));
        }

        try {
            Assert.assertTrue(
                    SecurityUtils
                            .isExpectedPassword(
                                    pwd.toCharArray(), salt, hash, 10000, 256, SecurityUtils.PBKDF2WithHmacSHA1
                            )
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assert.fail(ExceptionUtils.throwableToString(e));
        }

    }
}
