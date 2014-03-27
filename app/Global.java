
import com.google.inject.Guice;
import com.google.inject.Injector;
import models.ModelFactoryHelper;
import models.user.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.myweb.db.Dao;
import org.myweb.db.DaoJpa;
import org.myweb.services.ServiceException;
import org.myweb.services.user.create.UserCreateServiceJava;
import models.user.UserSecurityModule;
import org.myweb.utils.config.EnvConfigCheckerService;
import org.myweb.utils.config.EnvConfigService;
import org.myweb.utils.config.EnvConfigServiceException;
import org.myweb.utils.mail.MailUtilsService;
import play.*;
import play.db.jpa.JPA;
import play.i18n.Messages;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;

import javax.persistence.RollbackException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static play.mvc.Controller.response;
import static play.mvc.Http.Status.*;
import static play.mvc.Results.*;
import static play.mvc.Results.internalServerError;

public class Global extends GlobalSettings {

    private static Injector injector;
    private MailUtilsService mailerService;
    private Dao dao;
    private UserCreateServiceJava userCreateService;
    private ModelFactoryHelper modelFactoryHelper;
    private EnvConfigService envConfigService;
    private EnvConfigCheckerService envConfigCheckerService;

    private String getCORSallowOrigin() throws EnvConfigServiceException {
        String origin;

        origin = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN);
        if(origin == null) {
            throw new EnvConfigServiceException("PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var is not set !");
        }

        return origin;
    }

    private void onStartInitGuice() {

        injector = Guice.createInjector(new PocPlayRestBackendModule(), new UserSecurityModule());

        modelFactoryHelper = injector.getInstance(ModelFactoryHelper.class);

        mailerService = injector.getInstance(MailUtilsService.class);
        dao = injector.getInstance(DaoJpa.class);
        userCreateService = injector.getInstance(UserCreateServiceJava.class);
        envConfigService = injector.getInstance(EnvConfigService.class);
        envConfigCheckerService = injector.getInstance(EnvConfigCheckerService.class);
    }

    private void onStartInitAdminUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Map<String, Object> param = new HashMap<>();
                param.put("login", "admin");

                User admin = (User) dao.namedQuerySingleResult("User.findByLogin", User.class, param);

                if(admin == null) {
                    admin = modelFactoryHelper.userFactory(null, "admin", "@dm1nPwd", "@dm1nPwd", "admin@domain.tld");
                    userCreateService.createUser(admin);
                }
            }
        });
    }

    private void onStartInitDemoUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Map<String, Object> param = new HashMap<>();
                param.put("login", "demo-user");

                User demoUser = (User) dao.namedQuerySingleResult("User.findByLogin", User.class, param);

                if(demoUser == null) {
                    demoUser = modelFactoryHelper.userFactory(
                            null, "demo-user", "d3moNstr@tion", "d3moNstr@tion", "demo-user@domain.tld"
                    );
                    userCreateService.createUser(demoUser);
                }
            }
        });
    }

    private void onStartMailTech() {
        String recipient = null;
        boolean mailerConfigOK = true;
        try {
            recipient = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_TECH_EMAIL);
            if(recipient == null) {
                throw new EnvConfigServiceException("PPRB_TECH_EMAIL is not set.");
            }
        } catch (EnvConfigServiceException e) {
            mailerConfigOK = false;
            Logger.error("Exception when trying to get PPRB_TECH_EMAIL env var :\n" + ExceptionUtils.getStackTrace(e));
        }
        if(!mailerConfigOK){
            Logger.error("[onStart] PPRB_TECH_EMAIL is not set or cannot be retrieved. Can't start app.");
            System.exit(-1);
        }
        String subject = "[poc-play-rest-backend] Application started";
        StringBuilder textContent = new StringBuilder();
        textContent.append("The application started.");

        mailerService.sendTechTextEmail(recipient, subject, textContent.toString());
    }

    public void onStart(Application app) {
        Logger.info("[onStart] App is starting");

        Logger.info("[onStart] Guice init");
        onStartInitGuice();
        Logger.info("[onStart] Guice init is done");

        Logger.info("[onStart] Checking environment configuration");
        if(!envConfigCheckerService.check()) {
            Logger.error("[onStart] App environment configuration is baaaaad. Check it plz ... App have to stop here.");
            System.exit(-1);
        }
        Logger.info("[onStart] Environment configuration seems ok");

        Logger.info("[onStart] Init admin user");
        onStartInitAdminUser();
        Logger.info("[onStart] Init admin user is done");

        Logger.info("[onStart] Init demo user");
        onStartInitDemoUser();
        Logger.info("[onStart] Init demo user is done");

        Logger.info("[onStart] Sending starting email to tech support");
        onStartMailTech();
        Logger.info("[onStart] Sending starting email to tech support is done");

        Logger.info("[onStart] App has started");
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    private SimpleResult buildPlayCtrlResult(ServiceException se) {
        switch(se.getHttpStatus()) {
            case BAD_REQUEST: {
                if(se.getUserMessage() != null) {
                    return badRequest(se.getUserMessage());
                } else if (se.getSerializedFormErrors() != null) {
                    return badRequest(se.getSerializedFormErrors());
                } else {
                    return badRequest();
                }
            }
            case UNAUTHORIZED: {
                return unauthorized(se.getUserMessage());
            }
            case NOT_FOUND: {
                return notFound(se.getUserMessage());
            }
            case INTERNAL_SERVER_ERROR: {
                return internalServerError(se.getUserMessage());
            }
            default: {
                return internalServerError(Messages.get("global.onerror"));
            }
        }
    }

    // when an exception is thrown by a controller
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {

        String origin = null;
        try {
            origin = getCORSallowOrigin();
        } catch (EnvConfigServiceException e) {
            Logger.error(
                    "CORS impl : Impossible to load PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var !\n" +
                            ExceptionUtils.getStackTrace(e)
            );
            return Promise.<SimpleResult>pure( // without Access-Control-Allow-Origin browsers cannot read that ...
                    internalServerError(Messages.get("cors.error.missing.allow.origin")) // ... but a rest tool can
            );
        }
        response().setHeader("Access-Control-Allow-Origin", origin);
        response().setHeader("Access-Control-Allow-Credentials", "true");

        StringBuilder sb = new StringBuilder();
        sb.append("[").append( "Global.onError()" ).append("]");
        sb.append("[").append( request.path() ).append("]");
        sb.append("[").append( request.method() ).append("]");

        String recipient = null;
        boolean mailerConfigOK = true;
        try {
            recipient = envConfigService.getEnvVarAsString(EnvConfigService.PPRB_TECH_EMAIL);
            if(recipient == null) {
                throw new EnvConfigServiceException("PPRB_TECH_EMAIL is not set.");
            }
        } catch (EnvConfigServiceException e) {
            mailerConfigOK = false;
            Logger.error("Exception when trying to get PPRB_TECH_EMAIL env var :\n" + ExceptionUtils.getStackTrace(e));
        }

        if(t.getCause() != null && t.getCause() instanceof RollbackException){
            sb.append(Messages.get("global.onerror.rollback-exception"));
            Logger.error(sb.toString());

            if(mailerConfigOK) {
                String subject = "[poc-play-rest-backend] An error occurred causing a rollback :( ...";
                mailerService.sendTechTextEmail(recipient, subject, sb.toString());
            }

            return Promise.<SimpleResult>pure(
                    internalServerError(Messages.get("global.onerror.rollback-exception"))
            );

        } else if (t.getCause() != null && t.getCause() instanceof ServiceException) {
            sb.append(t.getMessage());

            Logger.error(sb.toString());

            if(mailerConfigOK){
                String subject = "[poc-play-rest-backend] A ServiceException occurred ...";
                mailerService.sendTechTextEmail(recipient, subject, sb.toString());
            }

            return Promise.<SimpleResult>pure(
                    buildPlayCtrlResult((ServiceException) t.getCause())
            );
        }

        sb.append(ExceptionUtils.getStackTrace(t));
        Logger.error(sb.toString());

        if(mailerConfigOK){
            String subject = "[poc-play-rest-backend] An error occurred :( ...";
            mailerService.sendTechTextEmail(recipient, subject, sb.toString());
        }

        return Promise.<SimpleResult>pure(
                internalServerError(Messages.get("global.onerror"))
        );
    }

    // when an action is not found
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        String origin;
        try {
            origin = getCORSallowOrigin();
        } catch (EnvConfigServiceException e) {
            Logger.error(
                    "CORS impl : Impossible to load PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var !\n" +
                            ExceptionUtils.getStackTrace(e)
            );
            return Promise.<SimpleResult>pure( // without Access-Control-Allow-Origin browsers cannot read that ...
                    internalServerError(Messages.get("cors.error.missing.allow.origin")) // ... but a rest tool can
            );
        }
        response().setHeader("Access-Control-Allow-Origin", origin);
        response().setHeader("Access-Control-Allow-Credentials", "true");

        StringBuilder sb = new StringBuilder();
        sb.append("[").append( "Global.onHandlerNotFound()" ).append("]");
        sb.append("[").append( request.path() ).append("]");
        sb.append("[").append( request.method() ).append("]");
        sb.append(Messages.get("global.url.not.found"));
        Logger.error(sb.toString());

        return Promise.<SimpleResult>pure(notFound(
                Messages.get("global.url.not.found")
        ));
    }

    // when a route was found but impossible to match parameters
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
        String origin;
        try {
            origin = getCORSallowOrigin();
        } catch (EnvConfigServiceException e) {
            Logger.error(
                    "CORS impl : Impossible to load PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN env var !\n" +
                            ExceptionUtils.getStackTrace(e)
            );
            return Promise.<SimpleResult>pure( // without Access-Control-Allow-Origin browsers cannot read that ...
                    internalServerError(Messages.get("cors.error.missing.allow.origin")) // ... but a rest tool can
            );
        }
        response().setHeader("Access-Control-Allow-Origin", origin);
        response().setHeader("Access-Control-Allow-Credentials", "true");

        StringBuilder sb = new StringBuilder();
        sb.append("[").append( "Global.onBadRequest()" ).append("]");
        sb.append("[").append( request.path() ).append("]");
        sb.append("[").append( request.method() ).append("]");
        sb.append(Messages.get("global.malformed.request"));
        sb.append(" - error msg : ").append(error);
        Logger.error(sb.toString());

        return Promise.<SimpleResult>pure(badRequest(
                Messages.get("global.malformed.request")
        ));
    }

    private void logIncomingRequest(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("[http request] ");
        sb.append(request.toString());
        if(request.body() != null) {
            sb.append(" [http request body] ");
            sb.append(request.body().toString());
        }

        Logger.info(sb.toString());
    }

    // before each request
    public Action onRequest(Request request, Method actionMethod) {

        logIncomingRequest(request);

        return super.onRequest(request, actionMethod);
    }

    @Override
    public <T> T getControllerInstance(Class<T> clazz) throws Exception {
        return injector.getInstance(clazz);
    }
}
