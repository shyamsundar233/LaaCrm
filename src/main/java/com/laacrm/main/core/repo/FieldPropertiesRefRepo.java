package com.laacrm.main.core.repo;

import com.laacrm.main.core.entity.FieldPropertiesRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldPropertiesRefRepo extends JpaRepository<FieldPropertiesRef, Long> {

    Optional<FieldPropertiesRef> findByPropertyNameAndFieldType(String propertyName, Integer fieldType);

    List<FieldPropertiesRef> findByFieldType(Integer fieldType);

}
