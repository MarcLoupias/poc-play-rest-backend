package org.myweb.services.crud;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import play.i18n.Messages;
import play.libs.Json;

import static play.mvc.Http.Status.*;

public class GetService extends Service {
    private static GetService instance = new GetService();

    public static GetService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private GetService() {

    }

    @NotNull
    public JavaServiceResult load(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) {

        DaoObject entity;

        entity = db.load(clazz, id);

        if(entity == null) {
            return JavaServiceResult.buildServiceResult(NOT_FOUND);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entity);
        }
    }

    @NotNull
    public RestServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) {

        JavaServiceResult jsr = instance.load(clazz, id);

        if(jsr.getHttpStatus() == NOT_FOUND) {
            return RestServiceResult.buildServiceResult(
                    NOT_FOUND,
                    "entity id " + id + " not found from class " + clazz.getSimpleName(),
                    Messages.get("crud.get.error.entity.not.found")
            );
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getSingleContent()));

    }
}
