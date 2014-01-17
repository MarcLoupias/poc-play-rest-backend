package org.myweb.services.crud.delete;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.services.RestServiceResult;

public interface DeleteServiceRest {
    @NotNull
    public RestServiceResult delete(@NotNull Class<? extends DaoObject> clazz, @Nullable Long entityId);
}
