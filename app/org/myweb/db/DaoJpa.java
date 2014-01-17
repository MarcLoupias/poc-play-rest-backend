package org.myweb.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * http://docs.oracle.com/javaee/6/api/javax/persistence/package-summary.html
 */
@SuppressWarnings("UnusedDeclaration")
public class DaoJpa implements Dao {

    @Override
    public void setRollBackOnly() {
        JPA.em().getTransaction().setRollbackOnly();
    }

    @Override
    public <T extends DaoObject> T load(Class<T> clazz, Long id) {
        return JPA.em().find(clazz, id);
    }

    // see http://stackoverflow.com/questions/957394/java-persistence-cast-to-something-the-result-of-query-getresultlist
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DaoObject> List<T> loadAll(Class<T> clazz) {
        return JPA.em().createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    public DaoObject merge(DaoObject elt) {
        return JPA.em().merge(elt);
    }

    @Override
    public void saveNew(DaoObject elt) {
        JPA.em().persist(elt);
    }

    @Override
    public void remove(DaoObject elt) {
        JPA.em().remove(elt);
    }

    @Nullable
    @Override
    public Object namedQuerySingleResult(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params) {

        TypedQuery q = JPA.em().createNamedQuery(queryName, clazz);
        if(params != null) {
            Set<String> keySet = params.keySet();
            for(String key : keySet){
                q.setParameter(key, params.get(key));
            }
        }

        try{
            return q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
