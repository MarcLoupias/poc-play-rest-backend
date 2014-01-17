package org.myweb.services.crud.query;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;

public interface QueryServiceRest {
    @NotNull
    public RestServiceResult query(@NotNull Class<? extends DaoObject> clazz);
}
