package org.myweb.services.crud.delete;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

public interface DeleteServiceJava {
    @NotNull
    public JavaServiceResult remove(@NotNull DaoObject entity);
}
