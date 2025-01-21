package com.laacrm.main.core.dao;

import com.laacrm.main.framework.AuthThreadLocal;
import com.laacrm.main.framework.entities.Ranges;
import com.laacrm.main.framework.service.tenant.TenantService;
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
import java.util.function.Consumer;

@Repository
@Transactional
@AllArgsConstructor
public class DaoHelper<T, ID>{

    private final EntityManager entityManager;
    private final TenantService tenantService;

    public List<T> findAll(Class<T> entityClass) {
        CriteriaBuilder crBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = crBuilder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(crBuilder.or(getAllPKPredicates(entityClass, crBuilder, root)));
        return entityManager.createQuery(query).getResultList();
    }

    public Optional<T> findById(Class<T> entityClass, ID id) {
        if(!isPKInRange(id)){
            throw new DaoException("ID is out of range");
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root).where(cb.or(getAllPKPredicates(entityClass, cb, root)), cb.equal(root.get("id"), id));
        T result = entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * For Ref
     *
     * List<MyEntity> results = daoHelper.findByUserCriteria(MyEntity.class, context -> {
     *     CriteriaBuilder cb = context.getCriteriaBuilder();
     *     Root<MyEntity> root = context.getRoot();
     *
     *     // Add an equals predicate
     *     context.addPredicate(cb.equal(root.get("firstName"), "John"));
     *
     *     // Add a not-equals predicate
     *     context.addPredicate(cb.notEqual(root.get("status"), "INACTIVE"));
     *
     *     // Add a range predicate
     *     context.addPredicate(cb.between(root.get("age"), 25, 40));
     *
     *     // Add an IN predicate
     *     context.addPredicate(root.get("role").in(Arrays.asList("ADMIN", "USER")));
     * });
     * @param entityClass
     * @param criteriaBuilderConsumer
     * @return
     */
    public List<T> findByUserCriteria(Class<T> entityClass, Consumer<CriteriaBuilderContext<T>> criteriaBuilderConsumer) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        CriteriaBuilderContext<T> context = new CriteriaBuilderContext<>(cb, root);

        criteriaBuilderConsumer.accept(context);

        List<Predicate> predicates = new ArrayList<>(context.getPredicates());
        predicates.add(cb.or(getAllPKPredicates(entityClass, cb, root)));

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }


    public <S extends T> S save(@NonNull S entity){
        entityManager.merge(entity);
        return entity;
    }

    public <S extends T> S update(@NonNull S entity) {
        Object primaryKeyValue = getPrimaryKeyValue(entity);
        if (primaryKeyValue == null) {
            throw new DaoException("Primary key value is null");
        }
        if (!isPKInRange((ID) primaryKeyValue)) {
            throw new DaoException("Primary key is out of range");
        }
        entityManager.merge(entity);
        return entity;
    }

    public void deleteById(Class<T> entityClass, ID id){
        T entity = findById(entityClass, id).orElseThrow(() -> new DaoException("Entity not found with id: " + id));
        entityManager.remove(entity);
    }

    private boolean isPKInRange(ID id){
        return tenantService.isPKInRange((Long) id);
    }

    private Object getPrimaryKeyValue(T entity) {
        Class<?> entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true); // Allow access to private fields
                try {
                    return field.get(entity); // Retrieve the value
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access the primary key field", e);
                }
            }
        }
        throw new IllegalArgumentException("No primary key field found in " + entityClass.getName());
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
