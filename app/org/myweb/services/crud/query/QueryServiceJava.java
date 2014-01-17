package org.myweb.services.crud.query;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

public interface QueryServiceJava {
    @NotNull
    public JavaServiceResult loadAll(@NotNull Class<? extends DaoObject> clazz);
}
