package controllers;

import com.google.inject.Inject;
import org.myweb.services.user.login.UserLoginServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginCtrl extends Controller {

    @Inject
    private UserLoginServiceRest userLoginServiceRest;

    @Transactional(readOnly = false)
    public Result login() {
        return userLoginServiceRest.loginUser(request().body().asJson()).buildPlayCtrlResult();
    }
}
