package com.laacrm.main.core.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilderContext<T> {

    private final CriteriaBuilder cb;

    @Getter
    private final Root<T> root;

    @Getter
    private final List<Predicate> predicates = new ArrayList<>();

    public CriteriaBuilderContext(CriteriaBuilder cb, Root<T> root) {
        this.cb = cb;
        this.root = root;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return cb;
    }

    public void addPredicate(Predicate predicate) {
        predicates.add(predicate);
    }

}
