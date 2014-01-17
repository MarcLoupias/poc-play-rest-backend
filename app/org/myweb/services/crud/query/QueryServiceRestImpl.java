package org.myweb.services.crud.query;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import play.libs.Json;

import static play.mvc.Http.Status.NO_CONTENT;
import static play.mvc.Http.Status.OK;

public class QueryServiceRestImpl implements QueryServiceRest {

    private final QueryServiceJava queryServiceJava;

    @Inject
    public QueryServiceRestImpl(QueryServiceJava queryServiceJava) {
        this.queryServiceJava = queryServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult query(@NotNull Class<? extends DaoObject> clazz) {

        JavaServiceResult jsr = queryServiceJava.loadAll(clazz);

        if(jsr.getHttpStatus() == NO_CONTENT) {
            return RestServiceResult.buildServiceResult(NO_CONTENT);
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getListContent()));
    }
}
