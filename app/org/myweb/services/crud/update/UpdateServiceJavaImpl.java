package org.myweb.services.crud.update;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import static play.mvc.Http.Status.*;

public class UpdateServiceJavaImpl implements UpdateServiceJava {
    private final Dao dao;

    @Inject
    public UpdateServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult merge(@NotNull DaoObject entity) {

        dao.merge(entity);

        return JavaServiceResult.buildServiceResult(OK);
    }

}
