package org.myweb.services.crud;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import play.data.Form;
import play.i18n.Messages;

import static play.mvc.Http.Status.*;

public class UpdateService extends Service {
    private static UpdateService instance = new UpdateService();

    public static UpdateService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private UpdateService() {

    }

    public JavaServiceResult merge(@NotNull DaoObject entity) {

        db.merge(entity);

        return JavaServiceResult.buildServiceResult(OK);
    }

    @NotNull
    public RestServiceResult update(@NotNull Class<? extends DaoObject> clazz, @NotNull JsonNode jsContent, @Nullable Long entityId) {

        if(entityId == null || entityId < 1l) {
            return RestServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    "put request without pojo id",
                    Messages.get("crud.update.error.id.missing")
            );
        }

        Form<? extends DaoObject> entityForm = Form.form(clazz);
        entityForm = entityForm.bind(jsContent);

        if(entityForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, entityForm.errorsAsJson());

        } else {
            DaoObject entity = entityForm.bind(jsContent).get();

            if(entity.getId() == null || (entity.getId().longValue() != entityId.longValue()) ){
                return RestServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        "put request id differ from put content id",
                        Messages.get("crud.update.error.url.id.and.pojo.id.differ")
                );
            }

            RestServiceResult rsrLoad = GetService.getInstance(instance.getDb()).get(clazz, entityId);
            if(rsrLoad.getHttpStatus() == NOT_FOUND) {
                return RestServiceResult.buildServiceResult(NOT_FOUND, rsrLoad.getErrorMsg(), rsrLoad.getUserMsg());
            }

            instance.merge(entity);

            return RestServiceResult.buildServiceResult(OK);
        }

    }

}
