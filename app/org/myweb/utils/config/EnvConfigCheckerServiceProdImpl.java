package org.myweb.utils.config;

import com.google.inject.Inject;

public class EnvConfigCheckerServiceProdImpl extends EnvConfigCheckerService {

    @Inject
    public EnvConfigCheckerServiceProdImpl(EnvConfigService envConfigService) {
        super(envConfigService);
    }

    @Override
    public boolean check() {
        boolean checkStatus = true;

        if(!doCheck(EnvConfigService.PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN)) {
            checkStatus = false;
        }
        if(!doCheck(EnvConfigService.PPRB_MAILER_SMTP_URL)) {
            checkStatus = false;
        }
        if(!doCheck(EnvConfigService.PPRB_MAILER_SMTP_USERNAME)) {
            checkStatus = false;
        }
        if(!doCheck(EnvConfigService.PPRB_MAILER_SMTP_PWD)) {
            checkStatus = false;
        }
        if(!doCheck(EnvConfigService.PPRB_NOREPLY_EMAIL)) {
            checkStatus = false;
        }
        if(!doCheck(EnvConfigService.PPRB_TECH_EMAIL)) {
            checkStatus = false;
        }

        return checkStatus;
    }
}
