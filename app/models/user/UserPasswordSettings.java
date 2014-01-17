package models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Embeddable;

@Embeddable
public class UserPasswordSettings {
    @JsonIgnore
    private byte[] hashedPwd;

    @JsonIgnore
    private byte[] salt;

    @JsonIgnore
    private int saltByteLength;

    @JsonIgnore
    private int iterations;

    @JsonIgnore
    private int pwdDerivatedKeyBitLength;

    @NotNull
    @JsonIgnore
    private String pwdPBKDF2algo;

    public byte[] getHashedPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(byte[] hashedPwd) {
        this.hashedPwd = hashedPwd;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public int getSaltByteLength() {
        return saltByteLength;
    }

    public void setSaltByteLength(int saltByteLength) {
        this.saltByteLength = saltByteLength;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getPwdDerivatedKeyBitLength() {
        return pwdDerivatedKeyBitLength;
    }

    public void setPwdDerivatedKeyBitLength(int pwdDerivatedKeyBitLength) {
        this.pwdDerivatedKeyBitLength = pwdDerivatedKeyBitLength;
    }

    @NotNull
    public String getPwdPBKDF2algo() {
        return pwdPBKDF2algo;
    }

    public void setPwdPBKDF2algo(@NotNull String pwdPBKDF2algo) {
        this.pwdPBKDF2algo = pwdPBKDF2algo;
    }

    public UserPasswordSettings() {
    }

    @Inject
    public UserPasswordSettings(
            @SaltByteLength int saltByteLength,
            @Iterations int iterations,
            @PwdDerivatedKeyBitLength int pwdDerivatedKeyBitLength,
            @NotNull @PwdPBKDF String pwdPBKDF2algo
    ) {
        this.saltByteLength = saltByteLength;
        this.iterations = iterations;
        this.pwdDerivatedKeyBitLength = pwdDerivatedKeyBitLength;
        this.pwdPBKDF2algo = pwdPBKDF2algo;
    }
}
