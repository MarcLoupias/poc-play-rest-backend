package org.myweb.services.crud.query;

import com.google.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.Dao;
import org.myweb.db.DaoException;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceException;
import play.i18n.Messages;

import java.util.List;

import static play.mvc.Http.Status.*;

public class QueryServiceJavaImpl implements QueryServiceJava {
    private final Dao dao;
    private final FilterParserService filterParserService;

    @Inject
    public QueryServiceJavaImpl(Dao dao, FilterParserService filterParserService) {
        this.dao = dao;
        this.filterParserService = filterParserService;
    }

    @NotNull
    @Override
    public JavaServiceResult load(@NotNull Class<? extends DaoObject> clazz) {
        List<? extends DaoObject> entityList;

        entityList = dao.load(clazz);

        if(entityList == null || entityList.size() == 0) {
            return JavaServiceResult.buildServiceResult(NO_CONTENT);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entityList);
        }
    }

    @NotNull
    @Override
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage
    ) {
        return load(clazz, page, itemPerPage, (List<ImmutableTriple<String,String,String>>)null);
    }

    @NotNull
    @Override
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @NotNull String filters
    ) {
        List< ImmutableTriple<String, String, String> > filterList = null;

        if(!filters.isEmpty()) {
            try {
                filterList = filterParserService.parse(filters);
            } catch (FilterParserServiceException e) {
                return JavaServiceResult.buildServiceResult(
                        BAD_REQUEST,
                        ExceptionUtils.getStackTrace(e),
                        Messages.get("java.service.result.generic.error.msg")
                );
            }
        }

        return load(clazz, page, itemPerPage, filterList);
    }

    @NotNull
    @Override
    public JavaServiceResult load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @Nullable List<ImmutableTriple<String,String,String>> filterList
    ) {
        List<? extends DaoObject> entityList;

        try {
            entityList = dao.load(clazz, page, itemPerPage, filterList);
        } catch (IllegalArgumentException iae) {
            return JavaServiceResult.buildServiceResult(
                    BAD_REQUEST,
                    ExceptionUtils.getStackTrace(iae),
                    Messages.get("java.service.result.generic.error.msg")
            );
        } catch (DaoException e) {
            return JavaServiceResult.buildServiceResult(
                    INTERNAL_SERVER_ERROR,
                    ExceptionUtils.getStackTrace(e),
                    Messages.get("java.service.result.generic.error.msg")
            );
        }

        if(entityList == null || entityList.size() == 0) {
            return JavaServiceResult.buildServiceResult(NO_CONTENT);
        } else {
            return JavaServiceResult.buildServiceResult(OK, entityList);
        }
    }
}
