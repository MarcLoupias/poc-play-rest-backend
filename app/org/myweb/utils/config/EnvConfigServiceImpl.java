package org.myweb.utils.config;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnvConfigServiceImpl implements EnvConfigService {

    private final SystemGetEnvWrapper systemGetEnvWrapper;

    @Inject
    public EnvConfigServiceImpl(SystemGetEnvWrapper systemGetEnvWrapper) {
        this.systemGetEnvWrapper = systemGetEnvWrapper;
    }

    @Override
    @Nullable
    public String getEnvVarAsString(@NotNull String key) throws EnvConfigServiceException {

        String res;

        try {
            res = systemGetEnvWrapper.getEnv(key);
        } catch(SecurityException e) {
            throw new EnvConfigServiceException(e);
        }

        return res;
    }

    @Nullable
    @Override
    public Integer getEnvVarAsInteger(@NotNull String key) throws EnvConfigServiceException {
        String sRes;

        try {
            sRes = systemGetEnvWrapper.getEnv(key);
        } catch(SecurityException e) {
            throw new EnvConfigServiceException(e);
        }

        if(sRes == null) {
            return null;
        }

        Integer res;

        try {
            res = Integer.parseInt(sRes);
        } catch(NumberFormatException e) {
            throw new EnvConfigServiceException(e);
        }

        return res;
    }

    @Nullable
    @Override
    public Long getEnvVarAsLong(@NotNull String key) throws EnvConfigServiceException {
        String sRes;

        try {
            sRes = systemGetEnvWrapper.getEnv(key);
        } catch(SecurityException e) {
            throw new EnvConfigServiceException(e);
        }

        if(sRes == null) {
            return null;
        }

        Long res;

        try {
            res = Long.parseLong(sRes);
        } catch(NumberFormatException e) {
            throw new EnvConfigServiceException(e);
        }

        return res;
    }
}
