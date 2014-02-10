package org.myweb.services.cinema;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;

public interface QueryCinemaByCountyServiceJava {
    @NotNull
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @NotNull String countyName
    ) throws ServiceException;

    @NotNull
    public JavaServiceResult count(
            @NotNull Class<? extends DaoObject> clazz, @NotNull String countyName
    );
}
