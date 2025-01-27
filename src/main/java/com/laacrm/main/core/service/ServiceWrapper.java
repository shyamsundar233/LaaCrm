package com.laacrm.main.core.service;

import java.util.List;

public interface ServiceWrapper<T> {

    List<T> findAll(Object... param);

    T findById(Long id);

    T save(T entity);

    T update(T entity);

    void delete(Long id);

}
