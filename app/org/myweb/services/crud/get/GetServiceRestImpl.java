package org.myweb.services.crud.get;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.i18n.Messages;
import play.libs.Json;

import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;

public class GetServiceRestImpl implements GetServiceRest {

    private final GetServiceJava getServiceJava;

    @Inject
    public GetServiceRestImpl(GetServiceJava getServiceJava) {
        this.getServiceJava = getServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) {
        JavaServiceResult jsr = getServiceJava.get(clazz, id);

        if(jsr.getHttpStatus() == NOT_FOUND) {
            return RestServiceResult.buildServiceResult(
                    NOT_FOUND,
                    "entity id " + id + " not found from class " + clazz.getSimpleName(),
                    Messages.get("crud.get.error.entity.not.found")
            );
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getSingleContent()));
    }

    @NotNull
    @Override
    public RestServiceResult count(@NotNull Class<? extends DaoObject> clazz) {
        JavaServiceResult jsr = getServiceJava.count(clazz);

        if(jsr.getHttpStatus() == INTERNAL_SERVER_ERROR) {
            RestServiceResult.buildServiceResult(INTERNAL_SERVER_ERROR);
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getCount()));
    }
}
