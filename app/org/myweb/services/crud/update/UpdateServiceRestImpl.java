package org.myweb.services.crud.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.crud.get.GetServiceJava;
import play.data.Form;
import play.i18n.Messages;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;

public class UpdateServiceRestImpl implements UpdateServiceRest {

    private final GetServiceJava getServiceJava;
    private final UpdateServiceJava updateServiceJava;

    @Inject
    public UpdateServiceRestImpl(UpdateServiceJava updateServiceJava, GetServiceJava getServiceJava) {
        this.updateServiceJava = updateServiceJava;
        this.getServiceJava = getServiceJava;
    }

    @NotNull
    @Override
    public RestServiceResult update(
            @NotNull Class<? extends DaoObject> clazz, @NotNull JsonNode jsContent, @Nullable Long entityId
    ) throws ServiceException {

        if(entityId == null || entityId < 1l) {

            throw new ServiceException(
                    UpdateServiceRestImpl.class.getName(), BAD_REQUEST, "put request without pojo id",
                    Messages.get("crud.update.error.id.missing")
            );
        }

        Form<? extends DaoObject> entityForm = Form.form(clazz);
        entityForm = entityForm.bind(jsContent);

        if(entityForm.hasErrors()) {

            throw new ServiceException(
                    UpdateServiceRestImpl.class.getName(), BAD_REQUEST, entityForm.errorsAsJson().asText(),
                    Messages.get("global.malformed.request"), entityForm.errorsAsJson());

        } else {
            DaoObject entity = entityForm.bind(jsContent).get();

            if(entity.getId() == null || (entity.getId().longValue() != entityId.longValue()) ){

                throw new ServiceException(
                        UpdateServiceRestImpl.class.getName(), BAD_REQUEST, "put request id differ from put content id",
                        Messages.get("crud.update.error.url.id.and.pojo.id.differ")
                );
            }

            JavaServiceResult jsrLoad = getServiceJava.get(clazz, entityId);
            if(jsrLoad.getHttpStatus() == NOT_FOUND || jsrLoad.getSingleContent() == null) {

                throw new ServiceException(
                        UpdateServiceRestImpl.class.getName(), NOT_FOUND,
                        "update request cant load entity to update",
                        Messages.get("crud.update.error.entity.not.found")
                );
            }

            updateServiceJava.merge(entity);

            return RestServiceResult.buildServiceResult(OK);
        }

    }
}
