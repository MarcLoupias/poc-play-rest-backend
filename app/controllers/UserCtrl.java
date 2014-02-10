package controllers;

import controllers.actions.httpHeaders.CORSimplAction;
import com.google.inject.Inject;
import controllers.actions.credentials.CredentialsCheckerAction;
import controllers.actions.httpHeaders.CacheControlAction;
import models.user.User;
import org.myweb.services.ServiceException;
import org.myweb.services.crud.delete.DeleteServiceRest;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.services.crud.query.QueryServiceRest;
import org.myweb.services.user.create.UserCreateServiceRest;
import org.myweb.services.user.update.UserUpdateServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@With({CacheControlAction.class, CORSimplAction.class, CredentialsCheckerAction.class})
public class UserCtrl extends Controller {

    @Inject
    private GetServiceRest getServiceRest;
    @Inject
    private QueryServiceRest queryServiceRest;
    @Inject
    private UserUpdateServiceRest userUpdateServiceRest;
    @Inject
    private UserCreateServiceRest userCreateServiceRest;
    @Inject
    private DeleteServiceRest deleteServiceRest;

    @Transactional(readOnly = true)
    public Result get(Long id) throws ServiceException {
        return getServiceRest.get(User.class, id).buildPlayCtrlResult();
    }

    @Transactional(readOnly = true)
    public Result query(){
        return queryServiceRest.query(User.class).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    public Result create() throws ServiceException {
        return userCreateServiceRest.createUser(request().body().asJson()).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    public Result update(Long id) throws ServiceException {
        return userUpdateServiceRest.updateUser(request().body().asJson(), id).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    public Result delete(Long id) throws ServiceException {
        return deleteServiceRest.delete(User.class, id).buildPlayCtrlResult();
    }

}
