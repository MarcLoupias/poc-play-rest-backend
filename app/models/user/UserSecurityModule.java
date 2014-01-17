package models.user;

import com.google.inject.AbstractModule;
import play.Play;

public class UserSecurityModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(Integer.class).annotatedWith(SaltByteLength.class)
                .toInstance(Play.application().configuration().getInt("security.user.pwd.salt.byte.length"));
        bind(Integer.class).annotatedWith(Iterations.class)
                .toInstance(Play.application().configuration().getInt("security.user.pwd.iterations"));
        bind(Integer.class).annotatedWith(PwdDerivatedKeyBitLength.class)
                .toInstance(Play.application().configuration().getInt("security.user.pwd.PBKDF2.bit.length"));
        bind(String.class).annotatedWith(PwdPBKDF.class)
                .toInstance(Play.application().configuration().getString("security.user.pwd.algo"));
    }
}
