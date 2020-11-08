package com.aegis.es_demo.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@SuppressWarnings({"unchecked", "deprecation"})
public class QueryHelper {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//    public <T> PageVO<T> pageQuery(String sql, int page, int pageSize, TypeReference<T> typeReference) {
//        return pageQuery(sql, page, pageSize, getClass(typeReference));
//    }
//
//    public <T> PageVO<T> pageQuery(String sql, int page, int pageSize, Class<T> clz) {
//        return pageQuery(sql, new HashMap<>(0), page, pageSize, clz);
//    }
//
//    public <T> PageVO<T> pageQuery(String sql, Map<String, Object> params, int page, int pageSize, TypeReference<T> typeReference) {
//        return pageQuery(sql, params, page, pageSize, getClass(typeReference));
//    }
//
//    public <T> PageVO<T> pageQuery(String sql, Map<String, Object> params, int page, int pageSize, Class<T> clz) {
//        String countSql = " SELECT COUNT(1) FROM ( " + sql + ") QUERYHELPER";
//        long total = countQuery(countSql, params);
//        String querySql = sql + " LIMIT :page,:pageSize ";
//        params.put("page", (page - 1) * pageSize);
//        params.put("pageSize", pageSize);
//        List<T> result = listQuery(querySql, params, clz);
//        return new PageVO<>(result, page, pageSize, total);
//    }

    public long countQuery(String countSql) {
        return countQuery(countSql, new HashMap<>(0));
    }

    public long countQuery(String countSql, Map<String, Object> params) {
        Query countQuery = entityManager.createNativeQuery(countSql);
        params.forEach(countQuery::setParameter);
        return ((BigInteger) countQuery.getSingleResult()).longValue();
    }

    public <T> List<T> listQuery(String querySql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(querySql);
        params.forEach(query::setParameter);
        return query.getResultList();
    }


    public <T> List<T> listQuery(String querySql, TypeReference<T> typeReference) {
        return listQuery(querySql, getClass(typeReference));
    }

    public <T> List<T> listQuery(String querySql, Class<T> clz) {
        return listQuery(querySql, new HashMap<>(0), clz);
    }

    public <T> List<T> listQuery(String querySql, Map<String, Object> params, TypeReference<T> typeReference) {
        return listQuery(querySql, params, getClass(typeReference));
    }

    public <T> List<T> listQuery(String querySql, Map<String, Object> params, Class<T> clz) {
        Query query;
        if (clz.isAnnotationPresent(Entity.class)) {
            query = entityManager.createNativeQuery(querySql, clz);
        } else {
            query = entityManager.createNativeQuery(querySql);
            if (clz.isAssignableFrom(Map.class)) {
                query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            } else if (!clz.getName().startsWith("java")){
                query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(clz));
            }
        }
        params.forEach(query::setParameter);
        return query.getResultList();
    }

//    public <T> T singleQuery(String querySql, TypeReference<T> typeReference) {
//        return singleQuery(querySql, getClass(typeReference));
//    }

//    public <T> T singleQuery(String querySql, Class<T> clz) {
//        return singleQuery(querySql, new HashMap<>(0), clz);
//    }
//
//    public <T> T singleQuery(String querySql, Map<String, Object> params, TypeReference<T> typeReference) {
//        return singleQuery(querySql, params, getClass(typeReference));
//    }

//    public <T> T singleQuery(String querySql, Map<String, Object> params, Class<T> clz) {
//        List<T> result = listQuery(querySql, params, clz);
//        if (result.size() == 0) {
//            return null;
//        } else if (result.size() > 1) {
//            throw new ServiceException(Msg.ERROR, "只查询一条数据时，获得多条数据");
//        }
//        return result.get(0);
//    }

    private static <T> Class<T> getClass(TypeReference<T> typeReference) {
        Type type = typeReference.getType();
        if (type instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl parameterizedType = (ParameterizedTypeImpl) type;
            return (Class<T>) parameterizedType.getRawType();
        }
        return (Class<T>) type;
    }
}
