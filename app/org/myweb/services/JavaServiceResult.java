package org.myweb.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;
import org.myweb.utils.ExceptionUtils;
import play.i18n.Messages;

import java.util.List;

import static play.mvc.Http.Status.*;

@SuppressWarnings("UnusedDeclaration")
public class JavaServiceResult extends AbstractServiceResult {

    @Nullable
    private DaoObject singleContent = null;
    @Nullable
    private List<? extends DaoObject> listContent = null;

    @Nullable
    public DaoObject getSingleContent() {
        return singleContent;
    }

    @Nullable
    public List<? extends DaoObject> getListContent() {
        return listContent;
    }

    private JavaServiceResult() {
    }

    private JavaServiceResult(int httpStatus) {
        super(httpStatus);
    }

    private JavaServiceResult(int httpStatus, @Nullable String errorMsg, @Nullable String userMsg) {
        super(httpStatus, errorMsg, userMsg);
    }

    private JavaServiceResult(int httpStatus, @Nullable DaoObject singleContent) {
        super(httpStatus);
        this.singleContent = singleContent;
    }

    private JavaServiceResult(int httpStatus, @Nullable List<? extends DaoObject> listContent) {
        super(httpStatus);
        this.listContent = listContent;
    }

    public static JavaServiceResult buildServiceResult(int httpStatus) {
        JavaServiceResult res = new JavaServiceResult(httpStatus);
        res.logError();
        return res;
    }

    public static JavaServiceResult buildServiceResult(
            int httpStatus, @Nullable String errorMsg, @Nullable String userMsg
    ) {
        JavaServiceResult res = new JavaServiceResult(httpStatus, errorMsg, userMsg);
        res.logError();
        return res;
    }

    public static JavaServiceResult buildServiceResult(int httpStatus, @Nullable DaoObject singleContent) {
        JavaServiceResult res = new JavaServiceResult(httpStatus, singleContent);
        res.logError();
        return res;
    }

    public static JavaServiceResult buildServiceResult(int httpStatus, @Nullable List<? extends DaoObject> listContent) {
        JavaServiceResult res = new JavaServiceResult(httpStatus, listContent);
        res.logError();
        return res;
    }

    public static JavaServiceResult buildGenericServiceResultError(@NotNull Throwable t) {
        JavaServiceResult res = new JavaServiceResult(
                INTERNAL_SERVER_ERROR,
                ExceptionUtils.throwableToString(t),
                Messages.get("java.service.result.generic.error.msg")
        );
        res.logError();
        return res;
    }
}
