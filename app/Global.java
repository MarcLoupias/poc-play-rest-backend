
import com.google.inject.Guice;
import com.google.inject.Injector;
import models.ModelFactoryHelper;
import models.user.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.myweb.db.Dao;
import org.myweb.db.DaoJpa;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.user.create.UserCreateServiceJava;
import models.user.UserSecurityModule;
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

import static play.mvc.Results.*;

public class Global extends GlobalSettings {

    private static Injector injector;
    private MailUtilsService mailerService;
    private Dao dao;
    private UserCreateServiceJava userCreateService;
    private ModelFactoryHelper modelFactoryHelper;

    private void onStartInitGuice() {

        injector = Guice.createInjector(new PocPlayRestBackendModule(), new UserSecurityModule());

        modelFactoryHelper = injector.getInstance(ModelFactoryHelper.class);

        mailerService = injector.getInstance(MailUtilsService.class);
        dao = injector.getInstance(DaoJpa.class);
        userCreateService = injector.getInstance(UserCreateServiceJava.class);
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

                    JavaServiceResult res = userCreateService.createUser(admin);

                    if(res.getHttpStatus() != Http.Status.CREATED) {
                        Logger.error("[Application Start] Impossible to create admin account : " + res.getErrorMsg());
                    }
                }
            }
        });
    }

    private void onStartMailTech() {
        String recipient = Play.application().configuration().getString("tech.mail");
        String subject = "[poc-play-rest-backend] Application started";
        StringBuilder textContent = new StringBuilder();
        textContent.append("The application started.");
        mailerService.sendTechTextEmail(recipient, subject, textContent.toString());
    }

    public void onStart(Application app) {
        Logger.info("[onStart] App is starting");

        // TODO rajouter une méthode de test de chargement des var env nécessaire à l'application.

        Logger.info("[onStart] Guice init");
        onStartInitGuice();
        Logger.info("[onStart] Guice init is done");

        Logger.info("[onStart] Init admin user");
        onStartInitAdminUser();
        Logger.info("[onStart] Init admin user is done");

        Logger.info("[onStart] Sending starting email to tech support");
        onStartMailTech();
        Logger.info("[onStart] Sending starting email to tech support is done");

        Logger.info("[onStart] App has started");
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

    // when an exception is thrown by a controller
    public Promise<SimpleResult> onError(RequestHeader request, Throwable t) {

        StringBuilder sb = new StringBuilder();
        sb.append("[").append( request.path() ).append("]");
        sb.append("[").append( request.method() ).append("]");

        if(t.getCause() != null && t.getCause() instanceof RollbackException){
            sb.append(Messages.get("global.onerror.rollback-exception"));
            Logger.error(sb.toString());

            String recipient = Play.application().configuration().getString("tech.mail");
            String subject = "[poc-play-rest-backend] An error occurred causing a rollback :( ...";
            mailerService.sendTechTextEmail(recipient, subject, sb.toString());

            return Promise.<SimpleResult>pure(
                    internalServerError(Messages.get("global.onerror.rollback-exception"))
            );
        }

        sb.append(ExceptionUtils.getStackTrace(t));
        Logger.error(sb.toString());

        String recipient = Play.application().configuration().getString("tech.mail");
        String subject = "[poc-play-rest-backend] An error occurred :( ...";
        mailerService.sendTechTextEmail(recipient, subject, sb.toString());

        return Promise.<SimpleResult>pure(
                internalServerError(Messages.get("global.onerror"))
        );

    }

    // when an action is not found
    public Promise<SimpleResult> onHandlerNotFound(RequestHeader request) {
        return Promise.<SimpleResult>pure(notFound(
                Messages.get("global.url.not.found")
        ));
    }

    // when a route was found but impossible to match parameters
    public Promise<SimpleResult> onBadRequest(RequestHeader request, String error) {
        return Promise.<SimpleResult>pure(badRequest(
                Messages.get("global.malformed.request")
        ));
    }

    // before each request
    public Action onRequest(Request request, Method actionMethod) {

        StringBuilder sb = new StringBuilder();
        sb.append("[http request] ");
        sb.append(request.toString());
        if(request.body() != null) {
            sb.append(" [http request body] ");
            sb.append(request.body().toString());
        }

        Logger.info(sb.toString());

        return super.onRequest(request, actionMethod);
    }

    @Override
    public <T> T getControllerInstance(Class<T> clazz) throws Exception {
        return injector.getInstance(clazz);
    }
}
