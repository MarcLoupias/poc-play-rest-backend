package org.myweb.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public interface Dao {

    public void setRollBackOnly();

    public <T extends DaoObject> T load(Class<T> clazz, Long id);

    public <T extends DaoObject> List<T> loadAll(Class<T> clazz);

    public DaoObject merge(DaoObject elt);

    public void saveNew(DaoObject elt);

    public void remove(DaoObject elt);

    @Nullable
    public Object namedQuerySingleResult(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params
    );

}
