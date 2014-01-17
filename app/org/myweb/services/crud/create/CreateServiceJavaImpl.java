package org.myweb.services.crud.create;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import static play.mvc.Http.Status.CREATED;

public class CreateServiceJavaImpl implements CreateServiceJava {

    private final Dao dao;

    @Inject
    public CreateServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult saveNew(@NotNull DaoObject entity) {

        dao.saveNew(entity);

        return JavaServiceResult.buildServiceResult(CREATED, entity);
    }

}
