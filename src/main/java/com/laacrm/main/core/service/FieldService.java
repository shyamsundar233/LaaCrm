package com.laacrm.main.core.service;

import com.laacrm.main.core.controller.APIException;
import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.repo.FieldPropertiesRefRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FieldService implements ServiceWrapper<Field> {

    private final DaoHelper<Field, Long> fieldDaoHelper;
    private final ModuleService moduleService;
    private final FieldPropertiesRefRepo fieldPropertiesRefRepo;

    @Autowired
    public FieldService(DaoHelper<Field, Long> fieldDaoHelper, @Lazy ModuleService moduleService, @Lazy FieldPropertiesRefRepo fieldPropertiesRefRepo) {
        this.fieldDaoHelper = fieldDaoHelper;
        this.moduleService = moduleService;
        this.fieldPropertiesRefRepo = fieldPropertiesRefRepo;
    }

    @Override
    public List<Field> findAll(Object... param) {
        Long moduleId = param.length > 0 ? Long.valueOf(param[0].toString()) : null;
        if(moduleId == null){
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module ID is required");
        }
        Module module = moduleService.findById(moduleId);
        if(module == null){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Module does not exist");
        }
        return fieldDaoHelper.findByCriteria(Field.class, context -> {
            CriteriaBuilder cb = context.getCriteriaBuilder();
            Root<Field> root = context.getRoot();
            context.addPredicate(cb.equal(root.get("module"), module));
        });
    }

    @Override
    public Field findById(Long fieldId) {
        return fieldDaoHelper.findById(Field.class, fieldId).orElse(null);
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

    public FieldPropertiesRef getFieldPropertiesRefByPropertyName(String propertyName, Integer fieldType) {
        return fieldPropertiesRefRepo.findByPropertyNameAndFieldType(propertyName, fieldType).orElse(null);
    }
}
