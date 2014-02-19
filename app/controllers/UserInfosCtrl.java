package controllers;

import com.google.inject.Inject;
import controllers.actions.credentials.CredentialsCheckerAction;
import controllers.actions.httpHeaders.CORSimplAction;
import controllers.actions.httpHeaders.CacheControlAction;
import org.myweb.services.ServiceException;
import org.myweb.services.user.infos.UserInfosServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@SuppressWarnings("UnusedDeclaration")
@With({CacheControlAction.class, CORSimplAction.class, CredentialsCheckerAction.class})
public class UserInfosCtrl extends Controller {

    @Inject
    private UserInfosServiceRest userInfosServiceRest;

    @Transactional(readOnly = true)
    public Result get() throws ServiceException {
        return userInfosServiceRest.getUserInfos().buildPlayCtrlResult();
    }
}
