package org.myweb.db;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public interface Dao {

    public void setRollBackOnly();


    public int count(@NotNull Class<? extends DaoObject> clazz) throws DaoException;

    public int count(
            @NotNull Class<? extends DaoObject> clazz, @Nullable List<ImmutableTriple<String, String, String>> params
    ) throws DaoException;


    public <T extends DaoObject> T load(Class<T> clazz, Long id);

    public <T extends DaoObject> List<T> load(Class<T> clazz);

    public <T extends DaoObject> List<T> load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @Nullable List<ImmutableTriple<String, String, String>> params
    ) throws DaoException;


    public DaoObject merge(DaoObject elt);

    public void saveNew(DaoObject elt);

    public void remove(DaoObject elt);

    @Nullable
    public Object namedQuerySingleResult(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params
    );

    public int namedQueryCount(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params
    );

    public <T extends DaoObject> List<T> namedQueryWithPagination(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params,
            int page, int itemPerPage
    ) throws DaoException;

    public Object nativeQuerySingleResult(@NotNull String nativeSqlQuery, @Nullable Map<String, Object> params);
}
