package org.myweb.db;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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
    public int count(@NotNull Class<? extends DaoObject> clazz) throws DaoException {
        return count(clazz, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int count(
            @NotNull Class<? extends DaoObject> clazz, @Nullable List<ImmutableTriple<String, String, String>> params
    ) throws DaoException {

        CriteriaBuilder qb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);

        Root<? extends DaoObject> entity = cq.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        if(params != null) {

            for(ImmutableTriple<String, String, String> param : params){
                if(param.getMiddle() == null || param.getMiddle().isEmpty()){
                    throw new DaoException("Logical operator missing (null or empty)");
                }

                switch(param.getMiddle()) {
                    case "eq": {
                        predicates.add(qb.equal(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "ne": {
                        predicates.add(qb.notEqual(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "gt": {
                        predicates.add(qb.greaterThan(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "ge": {
                        predicates.add(qb.greaterThanOrEqualTo(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "lt": {
                        predicates.add(qb.lessThan(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "le": {
                        predicates.add(qb.lessThanOrEqualTo(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "like": {
                        predicates.add(qb.like(entity.<String>get(param.getLeft()), "%" + param.getRight() + "%"));
                        break;
                    }
                    default: {
                        throw new DaoException("Unknown logical operator : " + param.getMiddle());
                    }
                }
            }
        }

        cq.select(qb.count(entity));
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        cq.where(predicates.toArray(new Predicate[0]));

        return JPA.em().createQuery(cq).getSingleResult().intValue();
    }

    @Override
    public <T extends DaoObject> T load(Class<T> clazz, Long id) {
        return JPA.em().find(clazz, id);
    }

    // see http://stackoverflow.com/questions/957394/java-persistence-cast-to-something-the-result-of-query-getresultlist
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DaoObject> List<T> load(Class<T> clazz) {
        return JPA.em().createQuery("from " + clazz.getName()).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DaoObject> List<T> load(
            @NotNull Class<? extends DaoObject> clazz, int page, int itemPerPage,
            @Nullable List<ImmutableTriple<String, String, String>> params
    ) throws DaoException {

        if(page <= 0) {
            throw new DaoException("page arg can't be <= 0");
        }

        if(itemPerPage <= 0) {
            throw new DaoException("itemPerPage arg can't be <= 0");
        }

        CriteriaBuilder qb = JPA.em().getCriteriaBuilder();
        CriteriaQuery cq = qb.createQuery();
        Root<? extends DaoObject> entity = cq.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        if(params != null) {

            for(ImmutableTriple<String, String, String> param : params){
                if(param.getMiddle() == null || param.getMiddle().isEmpty()){
                    throw new DaoException("Logical operator missing (null or empty)");
                }

                switch(param.getMiddle()) {
                    case "eq": {
                        predicates.add(qb.equal(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "ne": {
                        predicates.add(qb.notEqual(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "gt": {
                        predicates.add(qb.greaterThan(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "ge": {
                        predicates.add(qb.greaterThanOrEqualTo(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "lt": {
                        predicates.add(qb.lessThan(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "le": {
                        predicates.add(qb.lessThanOrEqualTo(entity.<String>get(param.getLeft()), param.getRight()));
                        break;
                    }
                    case "like": {
                        predicates.add(qb.like(entity.<String>get(param.getLeft()), "%" + param.getRight() + "%"));
                        break;
                    }
                    default: {
                        throw new DaoException("Unknown logical operator : " + param.getMiddle());
                    }
                }
            }
        }

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        cq.select(entity).where(predicates.toArray(new Predicate[0]));

        return JPA.em().createQuery(cq)
                .setFirstResult( (page - 1) * itemPerPage )
                .setMaxResults(itemPerPage)
                .getResultList();
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

    @SuppressWarnings("unchecked")
    @Override
    public int namedQueryCount(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params
    ) {
        Query q = JPA.em().createNamedQuery(queryName);
        if(params != null) {
            Set<String> keySet = params.keySet();
            for(String key : keySet){
                q.setParameter(key, params.get(key));
            }
        }

        return ((Number)q.getSingleResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DaoObject> List<T> namedQueryWithPagination(
            @NotNull String queryName, @NotNull Class<? extends DaoObject> clazz, @Nullable Map<String, Object> params,
            int page, int itemPerPage
    ) throws DaoException {

        if(page <= 0) {
            throw new DaoException("page arg can't be <= 0");
        }

        if(itemPerPage <= 0) {
            throw new DaoException("itemPerPage arg can't be <= 0");
        }

        TypedQuery q = JPA.em().createNamedQuery(queryName, clazz);
        if(params != null) {
            Set<String> keySet = params.keySet();
            for(String key : keySet){
                q.setParameter(key, params.get(key));
            }
        }

        try{
            return q
                    .setFirstResult( (page - 1) * itemPerPage )
                    .setMaxResults(itemPerPage)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Object nativeQuerySingleResult(@NotNull String nativeSqlQuery, @Nullable Map<String, Object> params) {
        Query query = JPA.em().createNativeQuery(nativeSqlQuery);
        if(params != null) {
            Set<String> keySet = params.keySet();
            for(String key : keySet){
                query.setParameter(key, params.get(key));
            }
        }

        return query.getSingleResult();
    }

}
