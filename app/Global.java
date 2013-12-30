
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.myweb.db.Dao;
import org.myweb.db.TestHelper;
import org.myweb.services.RestServiceResult;
import org.myweb.services.user.UserCreateService;
import org.myweb.utils.ExceptionUtils;
import play.*;
import play.db.jpa.JPA;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;

import javax.persistence.RollbackException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static play.mvc.Results.*;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started");

        JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Map<String, Object> param = new HashMap<>();
                param.put("login", "admin");
                User admin = (User) Dao.getInstance().namedQuerySingleResult("User.findByLogin", User.class, param);
                if(admin == null) {
                    admin = TestHelper.userFactory(null, "admin", "@dm1nPwd", "@dm1nPwd", "admin@domain.tld");
                    JsonNode jsAdmin = Json.toJson(admin);
                    RestServiceResult res = UserCreateService.getInstance(Dao.getInstance()).createUser(jsAdmin);
                    if(res.getHttpStatus() != Http.Status.CREATED) {
                        Logger.error("[Application Start] Impossible to create admin account : " + res.getErrorMsg());
                    }
                }
            }
        });
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
            return Promise.<SimpleResult>pure(
                    internalServerError(Messages.get("global.onerror.rollback-exception"))
            );
        }

        sb.append(ExceptionUtils.throwableToString(t));
        Logger.error(sb.toString());

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
}
