package org.myweb.utils.security;

import models.user.UserPasswordSettings;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface PasswordGenerationService {
    @NotNull
    public UserPasswordSettings generate(@NotNull UserPasswordSettings settings, char[] password)
            throws InvalidKeySpecException, NoSuchAlgorithmException;
}
