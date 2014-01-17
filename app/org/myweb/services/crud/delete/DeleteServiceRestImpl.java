package org.myweb.services.crud.delete;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.crud.get.GetServiceJava;
import play.i18n.Messages;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.NO_CONTENT;

public class DeleteServiceRestImpl implements DeleteServiceRest {

    private final DeleteServiceJava deleteServiceJava;
    private final GetServiceJava getServiceJava;

    @Inject
    public DeleteServiceRestImpl(DeleteServiceJava deleteServiceJava, GetServiceJava getServiceJava) {
        this.deleteServiceJava = deleteServiceJava;
        this.getServiceJava = getServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult delete(@NotNull Class<? extends DaoObject> clazz, @Nullable Long entityId) {

        if(entityId == null || entityId < 1l) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "delete request without delete id",
                    Messages.get("crud.delete.error.id.missing")
            );
        }

        JavaServiceResult jsrLoad = getServiceJava.get(clazz, entityId);
        if(jsrLoad.getHttpStatus() == NOT_FOUND || jsrLoad.getSingleContent() == null) {
            return RestServiceResult.buildServiceResult(
                    NOT_FOUND,
                    "delete request cant load entity to delete",
                    Messages.get("crud.delete.error.entity.not.found")
            );
        }

        DaoObject dbEntity = jsrLoad.getSingleContent();

        deleteServiceJava.remove(dbEntity);

        return RestServiceResult.buildServiceResult(NO_CONTENT);
    }
}
