package org.myweb.utils.config;

import com.google.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import play.Logger;

public abstract class EnvConfigCheckerService {
    public abstract boolean check();

    protected final EnvConfigService envConfigService;

    @Inject
    public EnvConfigCheckerService(EnvConfigService envConfigService) {
        this.envConfigService = envConfigService;
    }

    protected boolean doCheck(@NotNull String envVarName) {
        boolean checkStatus = true;

        String value = null;
        try {
            value = envConfigService.getEnvVarAsString(envVarName);
        } catch (EnvConfigServiceException e) {
            Logger.error(
                    "[onStart][EnvConfigCheckerService]Exception while loading " + envVarName + " :\n" +
                            ExceptionUtils.getStackTrace(e)
            );
            checkStatus = false;
        }
        if(value == null) {
            Logger.error("[onStart][EnvConfigCheckerService]<!>MANDATORY<!> " + envVarName + " env var is not set !");
            checkStatus = false;
        }

        return checkStatus;
    }
}
