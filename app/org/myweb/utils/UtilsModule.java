package org.myweb.utils;

import com.google.inject.AbstractModule;
import org.myweb.utils.config.*;
import org.myweb.utils.mail.MailUtilsService;
import org.myweb.utils.mail.MailUtilsServiceJavamailImpl;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceImpl;
import org.myweb.utils.security.PasswordGenerationService;
import org.myweb.utils.security.PasswordGenerationServiceImpl;
import org.myweb.utils.security.SecurityUtilsService;
import org.myweb.utils.security.SecurityUtilsServiceImpl;
import org.myweb.utils.session.SessionUtilsService;
import org.myweb.utils.session.SessionUtilsServiceImpl;
import play.Play;

public class UtilsModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(SystemGetEnvWrapper.class).to(SystemGetEnvWrapperImpl.class);
        bind(EnvConfigService.class).to(EnvConfigServiceImpl.class);
        if(Play.isDev()) {
            bind(EnvConfigCheckerService.class).to(EnvConfigCheckerServiceDevImpl.class);
        } else {
            bind(EnvConfigCheckerService.class).to(EnvConfigCheckerServiceProdImpl.class);
        }

        bind(MailUtilsService.class).to(MailUtilsServiceJavamailImpl.class);
        bind(SecurityUtilsService.class).to(SecurityUtilsServiceImpl.class);
        bind(PasswordGenerationService.class).to(PasswordGenerationServiceImpl.class);
        bind(SessionUtilsService.class).to(SessionUtilsServiceImpl.class);
        bind(FilterParserService.class).to(FilterParserServiceImpl.class);
    }
}
