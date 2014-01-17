package org.myweb.services.crud.query;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.Dao;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;

import java.util.List;

import static play.mvc.Http.Status.*;

public class QueryServiceJavaImpl implements QueryServiceJava {
    private final Dao dao;

    @Inject
    public QueryServiceJavaImpl(Dao dao) {
        this.dao = dao;
    }

    @NotNull
    @Override
    public JavaServiceResult loadAll(@NotNull Class<? extends DaoObject> clazz) {
        List<? extends DaoObject> entityList;

        entityList = dao.loadAll(clazz);

        if(entityList == null || entityList.size() == 0) {
            return JavaServiceResult.buildServiceResult(NO_CONTENT);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entityList);
        }
    }




}
