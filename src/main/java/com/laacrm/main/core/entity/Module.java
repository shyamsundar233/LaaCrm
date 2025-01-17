package com.laacrm.main.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Module {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.laacrm.main.core.entity.PKGenerator")
    private Long moduleId;

    private String moduleName;

    private String singularName;

    private String pluralName;

    private Integer type;

    private Integer status;

    public Module() {}

    public Module(String moduleName, String singularName, String pluralName, Integer type, Integer status) {
        this.moduleName = moduleName;
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.type = type;
        this.status = status;
    }

    public Module(Long moduleId, String moduleName, String singularName, String pluralName, Integer type, Integer status) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.type = type;
        this.status = status;
    }

}
