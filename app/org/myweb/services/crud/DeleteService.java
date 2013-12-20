package org.myweb.services.crud;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import play.i18n.Messages;

import static play.mvc.Http.Status.*;

public class DeleteService extends Service {
    private static DeleteService instance = new DeleteService();

    public static DeleteService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private DeleteService() {

    }

    public JavaServiceResult remove(@NotNull DaoObject entity) {

        db.remove(entity);

        return JavaServiceResult.buildServiceResult(NO_CONTENT, entity);
    }

    @NotNull
    public RestServiceResult delete(@NotNull Class<? extends DaoObject> clazz, @Nullable Long entityId) {

        if(entityId == null || entityId < 1l) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "delete request without delete id",
                    Messages.get("crud.delete.error.id.missing")
            );
        }

        JavaServiceResult jsrLoad = GetService.getInstance(instance.getDb()).load(clazz, entityId);
        if(jsrLoad.getHttpStatus() == NOT_FOUND || jsrLoad.getSingleContent() == null) {
            return RestServiceResult.buildServiceResult(
                    NOT_FOUND,
                    "delete request cant load entity to delete",
                    Messages.get("crud.delete.error.entity.not.found")
            );
        }

        DaoObject dbEntity = jsrLoad.getSingleContent();

        instance.remove(dbEntity);

        return RestServiceResult.buildServiceResult(NO_CONTENT);
    }
}
