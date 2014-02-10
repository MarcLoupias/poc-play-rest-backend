package org.myweb.services.crud.get;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

public interface GetServiceRest {
    @NotNull
    public RestServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) throws ServiceException;

    @NotNull
    public RestServiceResult count(@NotNull Class<? extends DaoObject> clazz) throws ServiceException;
}
