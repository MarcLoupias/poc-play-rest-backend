package org.myweb.utils.security;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SecurityUtilsServiceImplTest {

    @Test
    public void hashPassword() {

        SecurityUtilsServiceImpl service = new SecurityUtilsServiceImpl();

        String pwd = "AbR46korRps5-@";

        byte[] salt = service.generateSalt(16);
        byte[] hash = new byte[0];

        try {
            hash = service.hash(pwd.toCharArray(), salt, 10000, 256, SecurityUtilsServiceImpl.PBKDF2WithHmacSHA1);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assert.fail(ExceptionUtils.getStackTrace(e));
        }

        try {
            Assert.assertTrue(
                    service.isExpectedPassword(
                            pwd.toCharArray(), salt, hash, 10000, 256, SecurityUtilsServiceImpl.PBKDF2WithHmacSHA1
                    )
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Assert.fail(ExceptionUtils.getStackTrace(e));
        }

    }
}
