package org.myweb.services;

import org.jetbrains.annotations.Nullable;
import org.myweb.db.DaoObject;

import java.util.List;

public class JavaServiceResult extends AbstractServiceResult {

    private int count;
    @Nullable
    private DaoObject singleContent = null;
    @Nullable
    private List<? extends DaoObject> listContent = null;

    public int getCount() {
        return count;
    }

    @Nullable
    public DaoObject getSingleContent() {
        return singleContent;
    }

    @Nullable
    public List<? extends DaoObject> getListContent() {
        return listContent;
    }

    @SuppressWarnings("UnusedDeclaration")
    private JavaServiceResult() {
    }

    private JavaServiceResult(int httpStatus) {
        super(httpStatus);
    }

    private JavaServiceResult(int httpStatus, int count) {
        super(httpStatus);
        this.count = count;
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
        return new JavaServiceResult(httpStatus);
    }

    public static JavaServiceResult buildServiceResult(int httpStatus, int count) {
        return new JavaServiceResult(httpStatus, count);
    }

    public static JavaServiceResult buildServiceResult(int httpStatus, @Nullable DaoObject singleContent) {
        return new JavaServiceResult(httpStatus, singleContent);
    }

    public static JavaServiceResult buildServiceResult(int httpStatus, @Nullable List<? extends DaoObject> listContent) {
        return new JavaServiceResult(httpStatus, listContent);
    }
}
