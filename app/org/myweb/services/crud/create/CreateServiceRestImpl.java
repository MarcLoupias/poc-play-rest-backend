package org.myweb.services.crud.create;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;

public class CreateServiceRestImpl implements CreateServiceRest {

    private final CreateServiceJava createServiceJava;

    @Inject
    public CreateServiceRestImpl(CreateServiceJava createServiceJava) {
        this.createServiceJava = createServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult create(@NotNull Class<? extends DaoObject> clazz, @NotNull JsonNode jsContent)
            throws ServiceException {

        Form<? extends DaoObject> entityForm = Form.form(clazz);
        entityForm = entityForm.bind(jsContent);

        if(entityForm.hasErrors()) {

            throw new ServiceException(
                    CreateServiceRestImpl.class.getName(), BAD_REQUEST, entityForm.errorsAsJson().asText(),
                    Messages.get("global.malformed.request"), entityForm.errorsAsJson());

        } else {
            DaoObject entity = entityForm.bind(jsContent).get();

            JavaServiceResult jsr = createServiceJava.saveNew(entity);

            return RestServiceResult.buildServiceResult(CREATED, Json.toJson(jsr.getSingleContent()));
        }

    }
}
