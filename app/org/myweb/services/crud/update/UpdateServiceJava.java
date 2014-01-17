package org.myweb.services.crud.update;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

public interface UpdateServiceJava {
    @NotNull
    public JavaServiceResult merge(@NotNull DaoObject entity);
}
