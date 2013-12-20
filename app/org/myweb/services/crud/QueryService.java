package org.myweb.services.crud;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import play.libs.Json;

import java.util.List;

import static play.mvc.Http.Status.*;

public class QueryService extends Service {
    private static QueryService instance = new QueryService();

    public static QueryService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private QueryService() {

    }

    @NotNull
    public JavaServiceResult loadAll(@NotNull Class<? extends DaoObject> clazz) {
        List<? extends DaoObject> entityList;

        entityList = db.loadAll(clazz);

        if(entityList == null || entityList.size() == 0) {
            return JavaServiceResult.buildServiceResult(NO_CONTENT);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entityList);
        }
    }

    @NotNull
    public RestServiceResult query(@NotNull Class<? extends DaoObject> clazz) {

        JavaServiceResult jsr = instance.loadAll(clazz);

        if(jsr.getHttpStatus() == NO_CONTENT) {
            return RestServiceResult.buildServiceResult(NO_CONTENT);
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getListContent()));
    }


}
