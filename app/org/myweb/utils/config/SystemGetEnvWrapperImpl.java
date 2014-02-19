package org.myweb.utils.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SystemGetEnvWrapperImpl implements SystemGetEnvWrapper {
    @Nullable
    @Override
    public String getEnv(@NotNull String key) throws SecurityException{
        return System.getenv(key);
    }
}
