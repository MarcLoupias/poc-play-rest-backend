package org.myweb.services.crud.get;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import play.libs.Json;

import static play.mvc.Http.Status.OK;

public class GetServiceRestImpl implements GetServiceRest {

    private final GetServiceJava getServiceJava;

    @Inject
    public GetServiceRestImpl(GetServiceJava getServiceJava) {
        this.getServiceJava = getServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) throws ServiceException {
        JavaServiceResult jsr = getServiceJava.get(clazz, id);

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getSingleContent()));
    }

    @NotNull
    @Override
    public RestServiceResult count(@NotNull Class<? extends DaoObject> clazz) throws ServiceException {
        JavaServiceResult jsr = getServiceJava.count(clazz);

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getCount()));
    }
}
