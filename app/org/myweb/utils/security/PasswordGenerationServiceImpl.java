package org.myweb.utils.security;

import com.google.inject.Inject;
import models.user.UserPasswordSettings;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordGenerationServiceImpl implements PasswordGenerationService {

    private final SecurityUtilsService securityUtilsService;

    @Inject
    public PasswordGenerationServiceImpl(SecurityUtilsService securityUtilsService) {
        this.securityUtilsService = securityUtilsService;
    }

    @NotNull
    @Override
    public UserPasswordSettings generate(@NotNull UserPasswordSettings settings, char[] password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        settings.setSalt(securityUtilsService.generateSalt(settings.getSaltByteLength()));

        settings.setHashedPwd(
                securityUtilsService.hash(
                        password,
                        settings.getSalt(),
                        settings.getIterations(),
                        settings.getPwdDerivatedKeyBitLength(),
                        settings.getPwdPBKDF2algo()
                )
        );

        return settings;
    }
}
