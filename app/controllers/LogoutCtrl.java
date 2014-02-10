package controllers;

import com.google.inject.Inject;
import controllers.actions.credentials.CredentialsCheckerAction;
import controllers.actions.httpHeaders.CORSimplAction;
import controllers.actions.httpHeaders.CacheControlAction;
import org.myweb.services.user.logout.UserLogoutServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@With({CacheControlAction.class, CORSimplAction.class, CredentialsCheckerAction.class})
public class LogoutCtrl extends Controller {

    @Inject
    private UserLogoutServiceRest userLogoutServiceRest;

    @Transactional(readOnly = false)
    public Result logout() {
        return userLogoutServiceRest.logoutUser().buildPlayCtrlResult();
    }
}
