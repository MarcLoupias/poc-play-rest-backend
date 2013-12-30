package controllers;

import credentials.CredentialsCheckerAction;
import models.User;
import org.myweb.db.Dao;
import org.myweb.services.crud.DeleteService;
import org.myweb.services.crud.GetService;
import org.myweb.services.crud.QueryService;
import org.myweb.services.user.UserCreateService;
import org.myweb.services.user.UserUpdateService;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class UserCtrl extends Controller {

    @Transactional(readOnly = true)
    @With(CredentialsCheckerAction.class)
    public static Result get(Long id){

        return GetService.getInstance(Dao.getInstance())
                .get( User.class, id )
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = true)
    @With(CredentialsCheckerAction.class)
    public static Result query(){

        return QueryService.getInstance(Dao.getInstance())
                .query(User.class)
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public static Result create(){

        return UserCreateService.getInstance(Dao.getInstance())
                .createUser(request().body().asJson())
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public static Result update(Long id){

        return UserUpdateService.getInstance(Dao.getInstance())
                .updateUser(request().body().asJson(), id)
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public static Result delete(Long id){

        return DeleteService.getInstance(Dao.getInstance())
                .delete( User.class, id )
                .buildPlayCtrlResult();

    }

}
