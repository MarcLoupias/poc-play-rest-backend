package org.myweb.services.crud;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.Service;
import play.data.Form;
import play.libs.Json;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;

public class CreateService extends Service {
    private static CreateService instance = new CreateService();

    public static CreateService getInstance(@NotNull Dao db) {
        instance.setDb(db);
        return instance;
    }

    private CreateService() {

    }

    public JavaServiceResult saveNew(@NotNull DaoObject entity) {

        db.saveNew(entity);

        return JavaServiceResult.buildServiceResult(CREATED, entity);
    }

    @NotNull
    public RestServiceResult create(@NotNull Class<? extends DaoObject> clazz, @NotNull JsonNode jsContent) {

        Form<? extends DaoObject> entityForm = Form.form(clazz);
        entityForm = entityForm.bind(jsContent);

        if(entityForm.hasErrors()) {

            return RestServiceResult.buildServiceResult(BAD_REQUEST, entityForm.errorsAsJson());

        } else {
            DaoObject entity = entityForm.bind(jsContent).get();

            JavaServiceResult jsr = instance.saveNew(entity);

            return RestServiceResult.buildServiceResult(CREATED, Json.toJson(jsr.getSingleContent()));
        }

    }

}
