package controllers;

import org.myweb.db.Dao;
import org.myweb.services.user.UserLoginService;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginCtrl extends Controller {

    @Transactional(readOnly = false)
    public static Result login() {

        return UserLoginService.getInstance(Dao.getInstance())
                .loginUser(request().body().asJson())
                .buildPlayCtrlResult();
    }
}
