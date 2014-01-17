package org.myweb.services.crud.get;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

public interface GetServiceJava {
    @NotNull
    public JavaServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id);
}
