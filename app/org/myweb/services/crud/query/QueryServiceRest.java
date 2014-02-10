package org.myweb.services.crud.query;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

import java.util.Map;

public interface QueryServiceRest {
    @NotNull
    public RestServiceResult query(@NotNull Class<? extends DaoObject> clazz);

    @NotNull
    public RestServiceResult query(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage, @NotNull String filters
    ) throws ServiceException;
}
