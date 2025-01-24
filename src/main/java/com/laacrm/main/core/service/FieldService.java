package com.laacrm.main.core.service;

import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.repo.FieldPropertiesRefRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class FieldService implements ServiceWrapper<Field> {

    private final FieldPropertiesRefRepo fieldPropertiesRefRepo;

    @Override
    public List<Field> findAll() {
        return List.of();
    }

    @Override
    public Field findById(Long id) {
        return null;
    }

    @Override
    public Field save(Field entity) {
        return null;
    }

    @Override
    public Field update(Field entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    public void saveFieldPropertiesRef(FieldPropertiesRef fieldPropertiesRef) {
        fieldPropertiesRefRepo.save(fieldPropertiesRef);
    }

    public boolean isFieldPropertiesRefPopulated() {
        return fieldPropertiesRefRepo.count() > 0;
    }

    public FieldPropertiesRef getFieldPropertiesRefByPropertyName(String propertyName) {
        return fieldPropertiesRefRepo.findByPropertyName(propertyName).orElse(null);
    }
}
