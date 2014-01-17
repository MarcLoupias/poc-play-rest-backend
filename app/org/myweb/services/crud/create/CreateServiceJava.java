package org.myweb.services.crud.create;

import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

public interface CreateServiceJava {
    @NotNull
    public JavaServiceResult saveNew(@NotNull DaoObject entity);
}
