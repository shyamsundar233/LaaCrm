package com.laacrm.main.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Module {

    @Id
    private Long moduleId;

    private String moduleName;

    private String singularName;

    private String pluralName;

    private Integer type;

    private Integer status;

}
