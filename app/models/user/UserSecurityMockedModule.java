package models.user;

import com.google.inject.AbstractModule;
import org.myweb.utils.security.SecurityUtilsServiceImpl;

public class UserSecurityMockedModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(Integer.class).annotatedWith(SaltByteLength.class)
                .toInstance(16);
        bind(Integer.class).annotatedWith(Iterations.class)
                .toInstance(10000);
        bind(Integer.class).annotatedWith(PwdDerivatedKeyBitLength.class)
                .toInstance(256);
        bind(String.class).annotatedWith(PwdPBKDF.class)
                .toInstance(SecurityUtilsServiceImpl.PBKDF2WithHmacSHA1);
    }
}
