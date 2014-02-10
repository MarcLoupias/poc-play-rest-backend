package org.myweb.services.cinema;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;
import org.myweb.services.ServiceException;

public interface QueryCinemaByCountyServiceRest {
    @NotNull
    public RestServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @NotNull String countyName
    ) throws ServiceException;
}
