package org.myweb.services.crud.get;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import static play.mvc.Http.Status.*;

public class GetServiceJavaImpl implements GetServiceJava {
    private final Dao dao;

    @Inject
    public GetServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult get(@NotNull Class<? extends DaoObject> clazz, @NotNull Long id) {

        DaoObject entity;

        entity = dao.load(clazz, id);

        if(entity == null) {
            return JavaServiceResult.buildServiceResult(NOT_FOUND);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entity);
        }
    }
}
