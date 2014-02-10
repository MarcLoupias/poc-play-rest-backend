package org.myweb.services.crud.get;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.ServiceException;

import java.util.List;

public interface GetServiceJava {
    @NotNull
    public JavaServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) throws ServiceException;

    @NotNull
    public JavaServiceResult count(@NotNull Class<? extends DaoObject> clazz) throws ServiceException;

    @NotNull
    public JavaServiceResult count(
            @NotNull Class<? extends DaoObject> clazz, @NotNull String filters
    ) throws ServiceException;

    @NotNull
    public JavaServiceResult count(
            @NotNull Class<? extends DaoObject> clazz, @Nullable List<ImmutableTriple<String,String,String>> filterList
    ) throws ServiceException;
}
