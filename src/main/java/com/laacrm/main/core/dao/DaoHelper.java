package com.laacrm.main.core.dao;

import com.laacrm.main.core.AuthThreadLocal;
import com.laacrm.main.framework.entities.Ranges;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@AllArgsConstructor
public class DaoHelper<T, ID>{

    private final EntityManager entityManager;

    public List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder crBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = crBuilder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(crBuilder.or(getAllPKPredicates(entityClass, crBuilder, root)));
        return entityManager.createQuery(query).getResultList();
    }

    public Optional<T> findById(Class<T> entityClass, ID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.or(getAllPKPredicates(entityClass, cb, root)), cb.equal(root.get("id"), id));
        T result = entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    public <S extends T> S save(@NonNull S entity){
        entityManager.persist(entity);
        return entity;
    }

    public <S extends T> S update(S entity){
        entityManager.merge(entity);
        return entity;
    }

    public void deleteById(Class<T> entityClass, ID id){
        T entity = findById(entityClass, id).orElseThrow(() -> new DaoException("Entity not found with id: " + id));
        entityManager.remove(entity);
    }

    public Predicate[] getAllPKPredicates(Class<T> entityClass, CriteriaBuilder cb, Root<T> root) {
        return getAllPKPredicates(entityClass, cb, root, null);
    }

    public Predicate[] getAllPKPredicates(Class<T> entityClass, CriteriaBuilder cb, Root<T> root, Long tenantId) {
        List<Predicate> predicates = new ArrayList<>();
        if(tenantId == null){
            List<Ranges> currentTenantRanges = AuthThreadLocal.getCurrentTenant().getRanges();
            for(Ranges range : currentTenantRanges){
                predicates.add(cb.between(root.get(getPrimaryKeyFieldName(entityClass)), range.getStartRange(), range.getEndRange()));
            }
        }
        return predicates.toArray(new Predicate[0]);
    }

    public String getPrimaryKeyFieldName(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        throw new IllegalArgumentException("No primary key field found in " + entityClass.getName());
    }
}
