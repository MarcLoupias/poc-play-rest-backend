package controllers;

import controllers.actions.httpHeaders.CORSimplAction;
import com.google.inject.Inject;
import controllers.actions.httpHeaders.CacheControlAction;
import org.myweb.services.user.login.UserLoginServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@With({CacheControlAction.class, CORSimplAction.class})
public class LoginCtrl extends Controller {

    @Inject
    private UserLoginServiceRest userLoginServiceRest;

    @Transactional(readOnly = false)
    public Result login() {
        return userLoginServiceRest.loginUser(request().body().asJson()).buildPlayCtrlResult();
    }
}
