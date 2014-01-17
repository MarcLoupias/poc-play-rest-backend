package controllers;

import com.google.inject.Inject;
import credentials.CredentialsCheckerAction;
import models.Product;
import org.myweb.services.crud.create.CreateServiceRest;
import org.myweb.services.crud.delete.DeleteServiceRest;
import org.myweb.services.crud.get.GetServiceRest;
import org.myweb.services.crud.query.QueryServiceRest;
import org.myweb.services.crud.update.UpdateServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class ProductCtrl extends Controller {

    @Inject
    private GetServiceRest getServiceRest;
    @Inject
    private QueryServiceRest queryServiceRest;
    @Inject
    private UpdateServiceRest updateServiceRest;
    @Inject
    private CreateServiceRest createServiceRest;
    @Inject
    private DeleteServiceRest deleteServiceRest;

    @Transactional(readOnly = true)
    @With(CredentialsCheckerAction.class)
    public Result get(Long id){
        return getServiceRest.get( Product.class, id ).buildPlayCtrlResult();
    }

    @Transactional(readOnly = true)
    @With(CredentialsCheckerAction.class)
    public Result query(){
        return queryServiceRest.query(Product.class).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result update(Long id){
        return updateServiceRest.update( Product.class, request().body().asJson(), id ).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result create(){
        return createServiceRest.create( Product.class, request().body().asJson() ).buildPlayCtrlResult();
    }

    @Transactional(readOnly = false)
    @With(CredentialsCheckerAction.class)
    public Result delete(Long id){
        return deleteServiceRest.delete( Product.class, id ).buildPlayCtrlResult();
    }
}
