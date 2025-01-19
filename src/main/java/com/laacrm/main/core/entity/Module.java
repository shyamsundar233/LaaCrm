package com.laacrm.main.core.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.laacrm.main.core.ModuleConstants;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

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

    private Integer type = ModuleConstants.ModuleType.DEFAULT.getValue();

    private Integer status = ModuleConstants.ModuleStatus.ACTIVE.getValue();

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Field> fields = new ArrayList<>();

    public Module() {}

    public Module(String moduleName, String singularName, String pluralName, Integer type, Integer status, List<Field> fields) {
        this.moduleName = moduleName;
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.type = type;
        this.status = status;
        this.fields = fields;
    }

    public Module(Long moduleId, String moduleName, String singularName, String pluralName, Integer type, Integer status, List<Field> fields) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.type = type;
        this.status = status;
        this.fields = fields;
    }

}
