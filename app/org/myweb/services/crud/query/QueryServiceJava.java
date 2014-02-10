package org.myweb.services.crud.query;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import java.util.List;

public interface QueryServiceJava {
    @NotNull
    public JavaServiceResult load(@NotNull Class<? extends DaoObject> clazz);

    @NotNull
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage
    );

    @NotNull
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @NotNull String filters
    );

    @NotNull
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @Nullable List<ImmutableTriple<String,String,String>> filterList
    );
}
