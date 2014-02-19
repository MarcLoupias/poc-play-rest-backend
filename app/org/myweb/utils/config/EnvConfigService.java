package org.myweb.utils.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnvConfigService {

    public static String PPRB_MAILER_SMTP_URL = "PPRB_MAILER_SMTP_URL";
    public static String PPRB_MAILER_SMTP_USERNAME = "PPRB_MAILER_SMTP_USERNAME";
    public static String PPRB_MAILER_SMTP_PWD = "PPRB_MAILER_SMTP_PWD";
    public static String PPRB_NOREPLY_EMAIL = "PPRB_NOREPLY_EMAIL";
    public static String PPRB_TECH_EMAIL = "PPRB_TECH_EMAIL";

    public static String PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN = "PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN";

    public static String DEV_QUERY_PROCESSING_DELAY = "DEV_QUERY_PROCESSING_DELAY";

    @Nullable
    public String getEnvVarAsString(@NotNull String key) throws EnvConfigServiceException;

    @Nullable
    public Integer getEnvVarAsInteger(@NotNull String key) throws EnvConfigServiceException;

    @Nullable
    public Long getEnvVarAsLong(@NotNull String key) throws EnvConfigServiceException;
}
