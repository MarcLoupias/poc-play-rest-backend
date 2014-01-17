package controllers;

import com.google.inject.Inject;
import credentials.CredentialsCheckerAction;
import models.user.User;
import org.myweb.services.crud.delete.DeleteServiceRest;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.services.crud.query.QueryServiceRest;
import org.myweb.services.user.create.UserCreateServiceRest;
import org.myweb.services.user.update.UserUpdateServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

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
    @With(CredentialsCheckerAction.class)
    public Result get(Long id){
        return getServiceRest.get(User.class, id).buildPlayCtrlResult();
    }

    @Transactional(readOnly = true)
    @With(CredentialsCheckerAction.class)
    public Result query(){
        return queryServiceRest.query(User.class).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result create(){
        return userCreateServiceRest.createUser(request().body().asJson()).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result update(Long id){
        return userUpdateServiceRest.updateUser(request().body().asJson(), id).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result delete(Long id){
        return deleteServiceRest.delete(User.class, id).buildPlayCtrlResult();
    }

}
