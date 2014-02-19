package org.myweb.utils.config;

import com.google.inject.Inject;

public class EnvConfigCheckerServiceDevImpl extends EnvConfigCheckerService {

    @Inject
    public EnvConfigCheckerServiceDevImpl(EnvConfigService envConfigService) {
        super(envConfigService);
    }

    @Override
    public boolean check() {
        return doCheck(EnvConfigService.PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN);
    }
}
