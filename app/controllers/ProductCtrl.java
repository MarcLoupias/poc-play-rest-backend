package controllers;

import models.Product;
import org.myweb.db.Dao;
import org.myweb.services.crud.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductCtrl extends Controller {

    @Transactional(readOnly = true)
    public static Result get(Long id){

        return GetService.getInstance(Dao.getInstance())
                .get( Product.class, id )
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = true)
    public static Result query(){

        return QueryService.getInstance(Dao.getInstance())
                .query(Product.class)
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    public static Result update(Long id){

        return UpdateService.getInstance(Dao.getInstance())
                .update( Product.class, request().body().asJson(), id )
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    public static Result create(){

        return CreateService.getInstance(Dao.getInstance())
                .create( Product.class, request().body().asJson() )
                .buildPlayCtrlResult();

    }

    @Transactional(readOnly = false)
    public static Result delete(Long id){

        return DeleteService.getInstance(Dao.getInstance())
                .delete( Product.class, id )
                .buildPlayCtrlResult();

    }
}
