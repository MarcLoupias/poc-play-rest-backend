package org.myweb.services.crud.query;

import com.google.inject.Inject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.myweb.db.DaoObject;
import org.myweb.services.JavaServiceResult;
import org.myweb.services.RestServiceResult;
import org.myweb.utils.rest.FilterParserService;
import org.myweb.utils.rest.FilterParserServiceException;
import play.libs.Json;

import java.util.List;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.mvc.Http.Status.OK;

public class QueryServiceRestImpl implements QueryServiceRest {

    private final QueryServiceJava queryServiceJava;
    private final FilterParserService filterParserService;

    @Inject
    public QueryServiceRestImpl(QueryServiceJava queryServiceJava, FilterParserService filterParserService) {
        this.queryServiceJava = queryServiceJava;
        this.filterParserService = filterParserService;
    }

    @NotNull
    @Override
    public RestServiceResult query(@NotNull Class<? extends DaoObject> clazz) {

        JavaServiceResult jsr = queryServiceJava.load(clazz);

        if(jsr.getHttpStatus() == NO_CONTENT) {
            return RestServiceResult.buildServiceResult(NO_CONTENT);
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getListContent()));
    }

    @NotNull
    @Override
    public RestServiceResult query(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage, @NotNull String filters
    ) {
        List<ImmutableTriple<String,String,String>> filterList;

        try {
            filterList = filterParserService.parse(filters);
        } catch (FilterParserServiceException e) {
            return RestServiceResult.buildServiceResult(BAD_REQUEST, ExceptionUtils.getStackTrace(e), "Filters malformed");
        }

        JavaServiceResult jsr = queryServiceJava.load(clazz, page, itemPerPage, filterList);

        if(jsr.getHttpStatus() == NO_CONTENT) {
            return RestServiceResult.buildServiceResult(NO_CONTENT);
        } else if (jsr.getHttpStatus() == BAD_REQUEST) {
            return RestServiceResult.buildServiceResult(BAD_REQUEST, jsr.getErrorMsg(), jsr.getUserMsg());
        }

        return RestServiceResult.buildServiceResult(OK, Json.toJson(jsr.getListContent()));
    }
}
