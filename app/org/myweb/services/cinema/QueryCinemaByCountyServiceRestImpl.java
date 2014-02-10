package org.myweb.services.cinema;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import play.libs.Json;

import static play.mvc.Http.Status.NO_CONTENT;
import static play.mvc.Http.Status.OK;

public class QueryCinemaByCountyServiceRestImpl implements QueryCinemaByCountyServiceRest {

    private final QueryCinemaByCountyServiceJava queryCinemaByCountyServiceJava;

    @Inject
    public QueryCinemaByCountyServiceRestImpl(QueryCinemaByCountyServiceJava queryCinemaByCountyServiceJava) {
        this.queryCinemaByCountyServiceJava = queryCinemaByCountyServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage, @NotNull String countyName
    ) throws ServiceException {

        JavaServiceResult jsr = queryCinemaByCountyServiceJava.load(clazz, page, itemPerPage, countyName);

        if(jsr.getHttpStatus() == NO_CONTENT) {
            return RestServiceResult.buildServiceResult(NO_CONTENT);
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getListContent()));
    }
}
