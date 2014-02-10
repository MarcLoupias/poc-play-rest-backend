package org.myweb.services.crud.delete;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;
import org.myweb.services.crud.get.GetServiceJava;
import play.i18n.Messages;

import static play.mvc.Http.Status.BAD_REQUEST;
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
    public RestServiceResult delete(@NotNull Class<? extends DaoObject> clazz, @Nullable Long entityId) throws ServiceException {

        if(entityId == null || entityId < 1l) {

            throw new ServiceException(
                    DeleteServiceRestImpl.class.getName(), BAD_REQUEST, "delete request without delete id",
                    Messages.get("crud.delete.error.id.missing"));
        }

        JavaServiceResult jsrLoad = getServiceJava.get(clazz, entityId);

        DaoObject dbEntity = jsrLoad.getSingleContent();
        assert dbEntity != null;

        deleteServiceJava.remove(dbEntity);

        return RestServiceResult.buildServiceResult(NO_CONTENT);
    }
}
