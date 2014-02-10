package controllers.actions.credentials;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.myweb.utils.UtilsModule;
import org.myweb.utils.session.SessionUtilsService;
import play.Play;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class CredentialsCheckerAction extends Action<CredentialsChecker> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

        if(Play.isTest()){
            return delegate.call(ctx) ;
        }

        Injector injector = Guice.createInjector(new UtilsModule());
        SessionUtilsService sessionUtilsService = injector.getInstance(SessionUtilsService.class);

        if(sessionUtilsService.getSessionUserId() == null) {
            return F.Promise.pure(
                    (SimpleResult) unauthorized(Messages.get("global.unauthorized"))
            );
        } else {
            return delegate.call(ctx) ;
        }
    }
}
