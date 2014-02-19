package org.myweb.utils.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SystemGetEnvWrapper {
    @Nullable
    public String getEnv(@NotNull String key) throws SecurityException;
}
