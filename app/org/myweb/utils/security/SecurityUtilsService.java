package org.myweb.utils.security;

import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface SecurityUtilsService {
    @NotNull
    public byte[] generateSalt(@NotNull Integer byteLength);

    @NotNull
    public byte[] hash(
            char[] password, byte[] salt, int iterations, int bitKeyLength, @NotNull String pbeAlgo
    ) throws NoSuchAlgorithmException, InvalidKeySpecException;

    public boolean isExpectedPassword(
            char[] password, byte[] salt, byte[] expectedHash, int iterations, int bitKeyLength, @NotNull String pbeAlgo
    ) throws InvalidKeySpecException, NoSuchAlgorithmException;


}
