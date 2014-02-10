package controllers;

import controllers.actions.httpHeaders.CacheControlAction;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

public class CorsCtrl extends Controller {

    @Transactional(readOnly = false)
    @With(CacheControlAction.class)
    public static Result checkPreFlight(String path) {
        response().setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:9000"); // TODO load values from env
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response().setHeader("Access-Control-Max-Age", "0");
        response().setHeader("Access-Control-Allow-Headers","Content-Type");
        response().setHeader("Access-Control-Allow-Credentials", "true");

        return noContent();
    }
}
