package org.myweb.services.crud.get;

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

public class GetServiceJavaImpl implements GetServiceJava {
    private final Dao dao;
    private final FilterParserService filterParserService;

    @Inject
    public GetServiceJavaImpl(Dao dao, FilterParserService filterParserService) {
        this.dao = dao;
        this.filterParserService = filterParserService;
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

    @NotNull
    @Override
    public JavaServiceResult count(@NotNull Class<? extends DaoObject> clazz) {
        return count(clazz, (List<ImmutableTriple<String,String,String>>)null);
    }

    @NotNull
    @Override
    public JavaServiceResult count(
            @NotNull Class<? extends DaoObject> clazz, @NotNull String filters
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

        return count(clazz, filterList);
    }

    @NotNull
    @Override
    public JavaServiceResult count(
            @NotNull Class<? extends DaoObject> clazz, @Nullable List<ImmutableTriple<String,String,String>> filterList
    ) {
        int count;

        try {
            count = dao.count(clazz, filterList);
        } catch (DaoException e) {
            return JavaServiceResult.buildServiceResult(
                    INTERNAL_SERVER_ERROR,
                    ExceptionUtils.getStackTrace(e),
                    Messages.get("java.service.result.generic.error.msg")
            );
        }

        return JavaServiceResult.buildServiceResult(OK, count);
    }
}
