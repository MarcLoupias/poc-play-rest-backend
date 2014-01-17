package org.myweb.services.crud.delete;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import static play.mvc.Http.Status.*;

public class DeleteServiceJavaImpl implements DeleteServiceJava {
    private final Dao dao;

    @Inject
    public DeleteServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult remove(@NotNull DaoObject entity) {

        dao.remove(entity);

        return JavaServiceResult.buildServiceResult(NO_CONTENT, entity);
    }
}
