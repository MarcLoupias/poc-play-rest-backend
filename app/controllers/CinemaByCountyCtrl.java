package controllers;

import com.google.inject.Inject;
import controllers.actions.credentials.CredentialsCheckerAction;
import controllers.actions.httpHeaders.CORSimplAction;
import controllers.actions.httpHeaders.CacheControlAction;
import models.Cinema;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.cinema.QueryCinemaByCountyServiceJava;
import org.myweb.services.cinema.QueryCinemaByCountyServiceRest;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

@With({CacheControlAction.class, CORSimplAction.class, CredentialsCheckerAction.class})
public class CinemaByCountyCtrl extends Controller {

    @Inject
    private QueryCinemaByCountyServiceJava queryCinemaByCountyServiceJava;
    @Inject
    private QueryCinemaByCountyServiceRest queryCinemaByCountyServiceRest;

    @Transactional(readOnly = true)
    public Result query(Integer page, Integer perPage, String countyName) throws ServiceException {

        JavaServiceResult jsr = queryCinemaByCountyServiceJava.count(Cinema.class, countyName);
        if(jsr.getHttpStatus() != OK) {
            return internalServerError();
        }

        int count = jsr.getCount();
        response().setHeader("X-Total-Count", String.valueOf(count));

        return queryCinemaByCountyServiceRest.load(Cinema.class, page, perPage, countyName).buildPlayCtrlResult();
    }
}
